/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.data.service.TransferWindowDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferWindowEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.TransferWindowFacade;
import net.blackcat.fantasy.draft.integration.util.TransferUtils;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.transfer.Transfer;
import net.blackcat.fantasy.draft.transfer.types.TransferStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of transfer window operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "transferWindowFacade")
public class TransferWindowFacadeImpl implements TransferWindowFacade {

	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Autowired
	@Qualifier(value = "transferWindowDataServiceJpa")
	private TransferWindowDataService transferWindowDataService;
	
	@Autowired
	@Qualifier(value = "teamDataServiceJpa")
	private TeamDataService teamDataService;
	
	@Autowired
	@Qualifier(value = "playerDataServiceJpa")
	private PlayerDataService playerDataService;
	
	@Override
	public void startTransferWindow(final int leagueId, final int phase) throws FantasyDraftIntegrationException {
		final LeagueEntity league = leagueDataService.getLeague(leagueId);
		
		final TransferWindowEntity draftRound = new TransferWindowEntity(phase, league);
		transferWindowDataService.createTransferWindow(draftRound);
	}

	@Override
	public void addTransfer(final Transfer transfer) throws FantasyDraftIntegrationException {
		final TeamEntity buyingTeam = teamDataService.getTeam(transfer.getBuyingTeam());
		final TeamEntity sellingTeam = teamDataService.getTeam(transfer.getSellingTeam());
		
		final List<PlayerEntity> players = getPlayerEntitiesInvolvedInTransfer(transfer);
		final List<PlayerEntity> exchangedPlayers = getPlayersBeingExchangedInTransfer(transfer);
		
		final LeagueEntity league = leagueDataService.getLeagueForTeam(transfer.getBuyingTeam());
		final TransferWindowEntity openWindow = transferWindowDataService.getOpenTransferWindow(league.getId());
		
		final TransferEntity newTransfer = new TransferEntity(players, sellingTeam, buyingTeam, exchangedPlayers, transfer.getAmount());
		
		setTransferStatus(transfer, openWindow, newTransfer);
		
		openWindow.addTransfer(newTransfer);
		
		updateSellingTeamData(transfer, sellingTeam, players);
		updateBuyingTeamData(transfer, buyingTeam, exchangedPlayers);
		transferWindowDataService.updateTransferWindow(openWindow);
	}
	
	@Override
	public void sellPlayerToPot(final int teamId, final int playerId) throws FantasyDraftIntegrationException {
		final TeamEntity sellingTeam = teamDataService.getTeam(teamId);
		
		for (final SelectedPlayerEntity selectedPlayer : sellingTeam.getSelectedPlayers()) {
			if (selectedPlayer.getPlayer().getId() == playerId) {
				selectedPlayer.setStillSelected(SelectedPlayerStatus.PENDING_SALE_TO_POT);
				
				final BigDecimal newTeamRemainingBudget = sellingTeam.getRemainingBudget().add(selectedPlayer.getCurrentSellToPotPrice());
				sellingTeam.setRemainingBudget(newTeamRemainingBudget);
				
				teamDataService.updateTeam(sellingTeam);
				
				break;
			}
		}
	}

	/**
	 * Update the details for the team who has bought the players.
	 * 
	 * @param transfer Transfer details.
	 * @param buyingTeam Team buying the players.
	 * @param exchangedPlayers Any players the buying team is exchanging.
	 */
	private void updateBuyingTeamData(final Transfer transfer, final TeamEntity buyingTeam, final List<PlayerEntity> exchangedPlayers) {
		final BigDecimal newRemainingBudget = buyingTeam.getRemainingBudget().subtract(transfer.getAmount());
		buyingTeam.setRemainingBudget(newRemainingBudget);
		updateTransferredOutPlayerSelectionStatus(buyingTeam, exchangedPlayers);
	}
	
	/**
	 * Update the details for the team who has bought the players.
	 * 
	 * @param transfer Transfer details.
	 * @param sellingTeam Team buying the players.
	 * @param soldPlayers Any players the buying team is exchanging.
	 */
	private void updateSellingTeamData(final Transfer transfer, final TeamEntity sellingTeam, final List<PlayerEntity> soldPlayers) {
		final BigDecimal newRemainingBudget = sellingTeam.getRemainingBudget().add(transfer.getAmount());
		sellingTeam.setRemainingBudget(newRemainingBudget);
		updateTransferredOutPlayerSelectionStatus(sellingTeam, soldPlayers);
	}
	
	/**
	 * Update the selection status of players who are transferred out to record the pending transfer out.
	 * 
	 * @param sellingTeam The team selling the players.
	 * @param players The players being sold.
	 */
	private void updateTransferredOutPlayerSelectionStatus(final TeamEntity sellingTeam, final List<PlayerEntity> players) {
		for (final PlayerEntity transferredOutPlayer : players) {
			for (final SelectedPlayerEntity selectedPlayer : sellingTeam.getSelectedPlayers()) {
				if (selectedPlayer.getPlayer().getId() == transferredOutPlayer.getId()) {
					selectedPlayer.setStillSelected(SelectedPlayerStatus.PENDING_TRANSFER_OUT);
				}
			}
		}
		
		teamDataService.updateTeam(sellingTeam);
	}
	
	/**
	 * Set the status of a new transfer and any equivalent transfers to that one.
	 * 
	 * @param transfer New transfer model object containing all data on the transfer.
	 * @param openWindow The open transfer window object of the league in question.
	 * @param newTransfer New transfer entity object to set the status of. 
	 */
	private void setTransferStatus(final Transfer transfer, final TransferWindowEntity openWindow, final TransferEntity newTransfer) {
		try {
			final TransferEntity equivalentTransfer = TransferUtils.getEquivalentTransfer(transfer, openWindow.getTransfers());
			equivalentTransfer.setStatus(TransferStatus.CONFIRMED);
			
			newTransfer.setStatus(TransferStatus.CONFIRMED);
		} catch(final FantasyDraftIntegrationException e) {
			newTransfer.setStatus(TransferStatus.PENDING);
		}
	}

	/**
	 * Create a list of the players who are being exchanged in a transfer.
	 * 
	 * @param transfer {@link Transfer} object containing transfer details.
	 * @return List of {@link PlayerEntity} objects who are who are being exchanged in a transfer.
	 * @throws FantasyDraftIntegrationException
	 */
	private List<PlayerEntity> getPlayersBeingExchangedInTransfer(final Transfer transfer) throws FantasyDraftIntegrationException {
		final List<PlayerEntity> exchangedPlayers = new ArrayList<PlayerEntity>();
		
		if (transfer.getExchangedPlayers() != null) {
			for (final int playerId : transfer.getExchangedPlayers()) {
				exchangedPlayers.add(playerDataService.getPlayer(playerId));
			}
		}
		
		return exchangedPlayers;
	}

	/**
	 * Create a list of the players who are the subject of a transfer.
	 * 
	 * @param transfer {@link Transfer} object containing transfer details.
	 * @return List of {@link PlayerEntity} objects who are part of the transfer.
	 * @throws FantasyDraftIntegrationException
	 */
	private List<PlayerEntity> getPlayerEntitiesInvolvedInTransfer(final Transfer transfer) throws FantasyDraftIntegrationException {
		final List<PlayerEntity> players = new ArrayList<PlayerEntity>();
		
		for (final int playerId : transfer.getPlayers()) {
			players.add(playerDataService.getPlayer(playerId));
		}
		return players;
	}
}
