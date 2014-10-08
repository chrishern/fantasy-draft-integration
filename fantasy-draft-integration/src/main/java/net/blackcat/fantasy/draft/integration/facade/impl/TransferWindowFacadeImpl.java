/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.data.service.TransferWindowDataService;
import net.blackcat.fantasy.draft.integration.entity.ExchangedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferWindowEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferredPlayerEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.TransferWindowFacade;
import net.blackcat.fantasy.draft.integration.util.TransferUtils;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.transfer.Transfer;
import net.blackcat.fantasy.draft.transfer.TransferSummary;
import net.blackcat.fantasy.draft.transfer.types.TransferStatus;
import net.blackcat.fantasy.draft.transfer.types.TransferType;

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
		
		final List<TransferredPlayerEntity> players = getPlayerEntitiesInvolvedInTransfer(transfer);
		final List<ExchangedPlayerEntity> exchangedPlayers = getPlayersBeingExchangedInTransfer(transfer);
		
		final LeagueEntity league = leagueDataService.getLeagueForTeam(transfer.getBuyingTeam());
		final TransferWindowEntity openWindow = transferWindowDataService.getOpenTransferWindow(league.getId());
		
		final TransferEntity newTransfer = new TransferEntity(players, sellingTeam, buyingTeam, exchangedPlayers, transfer.getAmount());
		
		final TransferStatus transferStatus = setTransferStatus(transfer, openWindow, newTransfer);
		
		updateSellingTeamData(transfer, sellingTeam, players, transferStatus);
		updateBuyingTeamData(transfer, buyingTeam, exchangedPlayers, transferStatus);
		transferWindowDataService.updateTransferWindow(openWindow);
	}
	
	@Override
	public void sellPlayerToPot(final int teamId, final int playerId) throws FantasyDraftIntegrationException {
		final TeamEntity sellingTeam = teamDataService.getTeam(teamId);
		
		for (final SelectedPlayerEntity selectedPlayer : sellingTeam.getSelectedPlayers()) {
			if (selectedPlayer.getPlayer().getId() == playerId) {
				
				updateTeamDataForSaleToPot(sellingTeam, selectedPlayer);
				updateTransferWindowDataForSaleToPot(sellingTeam, selectedPlayer);
				
				break;
			}
		}
	}
	
	@Override
	public List<TransferSummary> getTransfersForTeamInOpenWindow(final int teamId) throws FantasyDraftIntegrationException {
		final List<TransferSummary> transfers = new ArrayList<TransferSummary>();
		
		final LeagueEntity league = leagueDataService.getLeagueForTeam(teamId);
		final TransferWindowEntity openWindow = transferWindowDataService.getOpenTransferWindow(league.getId());
		
		for (final TransferEntity transferEntity : transferWindowDataService.getTransfers(openWindow, teamId)) {
			final TransferSummary modelTransfer = new TransferSummary();
			
			modelTransfer.setStatus(transferEntity.getStatus());
			
			if (isTeamSellingTeam(teamId, transferEntity)) {
				modelTransfer.setType(TransferType.SELL);
				modelTransfer.setCost(transferEntity.getAmount());

				setOpposingTeamForTransfer(transferEntity, modelTransfer);
				setTransferSummaryPlayers(transferEntity, modelTransfer);
				setTransferSummaryExchangedPlayers(transferEntity, modelTransfer);
			} else if (isTeamBuyingTeam(teamId, transferEntity)) {
				modelTransfer.setType(TransferType.BUY);
				modelTransfer.setCost(transferEntity.getAmount());
				modelTransfer.setOpposingTeam(transferEntity.getSellingTeam().getName());

				setTransferSummaryPlayers(transferEntity, modelTransfer);
				setTransferSummaryExchangedPlayers(transferEntity, modelTransfer);
			}
			
			transfers.add(modelTransfer);
		}
		
		return transfers;
	}
	
	/**
	 * @param teamId
	 * @param transferEntity
	 * @return
	 */
	private boolean isTeamSellingTeam(final int teamId, final TransferEntity transferEntity) {
		return transferEntity.getSellingTeam().getId() == teamId;
	}

	/**
	 * @param teamId
	 * @param transferEntity
	 * @return
	 */
	private boolean isTeamBuyingTeam(final int teamId, final TransferEntity transferEntity) {
		return transferEntity.getBuyingTeam() != null && transferEntity.getBuyingTeam().getId() == teamId;
	}

	/**
	 * @param transferEntity
	 * @param modelTransfer
	 */
	private void setTransferSummaryExchangedPlayers(final TransferEntity transferEntity, final TransferSummary modelTransfer) {
		final List<Player> exchangedPlayers = new ArrayList<Player>();
		for (final ExchangedPlayerEntity entity : transferEntity.getExchangedPlayers()) {
			final Player player = new Player();
			
			player.setForename(entity.getPlayer().getForename());
			player.setSurname(entity.getPlayer().getSurname());
			
			exchangedPlayers.add(player);
		}
		
		modelTransfer.setExchangedPlayers(exchangedPlayers);
	}

	/**
	 * @param transferEntity
	 * @param modelTransfer
	 */
	private void setTransferSummaryPlayers(final TransferEntity transferEntity, final TransferSummary modelTransfer) {
		final List<Player> players = new ArrayList<Player>();
		for (final TransferredPlayerEntity entity : transferEntity.getPlayers()) {
			final Player player = new Player();
			
			player.setForename(entity.getPlayer().getForename());
			player.setSurname(entity.getPlayer().getSurname());
			
			players.add(player);
		}
		
		modelTransfer.setPlayers(players);
	}

	/**
	 * @param transferEntity
	 * @param modelTransfer
	 */
	private void setOpposingTeamForTransfer(final TransferEntity transferEntity, final TransferSummary modelTransfer) {
		if (transferEntity.getBuyingTeam() == null) {
			modelTransfer.setOpposingTeam("Pot");
		} else {
			modelTransfer.setOpposingTeam(transferEntity.getBuyingTeam().getName());
		}
	}

	/**
	 * Update transfer window data to reflect a player has been sold to the pot.
	 * 
	 * @param sellingTeam Team selling the player to the pot.
	 * @param selectedPlayer Player who has been sold to the pot.
	 * @throws FantasyDraftIntegrationException
	 */
	private void updateTransferWindowDataForSaleToPot(final TeamEntity sellingTeam, final SelectedPlayerEntity selectedPlayer) throws FantasyDraftIntegrationException {
		final TransferredPlayerEntity transferredPlayer = new TransferredPlayerEntity(selectedPlayer.getPlayer());
		final TransferEntity transfer = new TransferEntity(Arrays.asList(transferredPlayer), sellingTeam, null, null, selectedPlayer.getCurrentSellToPotPrice());
		transfer.setStatus(TransferStatus.CONFIRMED);
		
		final LeagueEntity league = leagueDataService.getLeagueForTeam(sellingTeam.getId());
		final TransferWindowEntity openWindow = transferWindowDataService.getOpenTransferWindow(league.getId());
		
		openWindow.addTransfer(transfer);
		
		transferWindowDataService.updateTransferWindow(openWindow);
	}

	/**
	 * Update team data for a player that has been sold to the pot.
	 * 
	 * @param sellingTeam Team selling the player.
	 * @param selectedPlayer Player who has been sold to the pot.
	 */
	private void updateTeamDataForSaleToPot(final TeamEntity sellingTeam, final SelectedPlayerEntity selectedPlayer) {
		selectedPlayer.setStillSelected(SelectedPlayerStatus.PENDING_SALE_TO_POT);
		
		final BigDecimal newTeamRemainingBudget = sellingTeam.getRemainingBudget().add(selectedPlayer.getCurrentSellToPotPrice());
		sellingTeam.setRemainingBudget(newTeamRemainingBudget);
		
		teamDataService.updateTeam(sellingTeam);
	}

	/**
	 * Update the details for the team who has bought the players.
	 * 
	 * @param transfer Transfer details.
	 * @param buyingTeam Team buying the players.
	 * @param exchangedPlayers Any players the buying team is exchanging.
	 */
	private void updateBuyingTeamData(final Transfer transfer, final TeamEntity buyingTeam, final List<ExchangedPlayerEntity> exchangedPlayers, final TransferStatus transferStatus) {
		if (transferStatus == TransferStatus.CONFIRMED) {
			final BigDecimal newRemainingBudget = buyingTeam.getRemainingBudget().subtract(transfer.getAmount());
			buyingTeam.setRemainingBudget(newRemainingBudget);
		}
		
		updateExchangedPlayerSelectionStatus(buyingTeam, exchangedPlayers);
		
		teamDataService.updateTeam(buyingTeam);
	}
	
	/**
	 * Update the details for the team who has bought the players.
	 * 
	 * @param transfer Transfer details.
	 * @param sellingTeam Team buying the players.
	 * @param soldPlayers Any players the buying team is exchanging.
	 */
	private void updateSellingTeamData(final Transfer transfer, final TeamEntity sellingTeam, final List<TransferredPlayerEntity> soldPlayers, final TransferStatus transferStatus) {
		if (transferStatus == TransferStatus.CONFIRMED) {
			final BigDecimal newRemainingBudget = sellingTeam.getRemainingBudget().add(transfer.getAmount());
			sellingTeam.setRemainingBudget(newRemainingBudget);
		}
		
		updateSoldPlayerSelectionStatus(sellingTeam, soldPlayers);
		
		teamDataService.updateTeam(sellingTeam);
	}
	
	/**
	 * Update the selection status of players who are transferred out to record the pending transfer out.
	 * 
	 * @param sellingTeam The team selling the players.
	 * @param players The players being sold.
	 */
	private void updateSoldPlayerSelectionStatus(final TeamEntity sellingTeam, final List<TransferredPlayerEntity> players) {
		for (final TransferredPlayerEntity transferredOutPlayer : players) {
			for (final SelectedPlayerEntity selectedPlayer : sellingTeam.getSelectedPlayers()) {
				if (selectedPlayer.getPlayer().getId() == transferredOutPlayer.getPlayer().getId()) {
					selectedPlayer.setStillSelected(SelectedPlayerStatus.PENDING_TRANSFER_OUT);
				}
			}
		}
	}
	
	/**
	 * Update the selection status of players who are transferred out to record the pending transfer out.
	 * 
	 * @param sellingTeam The team selling the players.
	 * @param players The players being sold.
	 */
	private void updateExchangedPlayerSelectionStatus(final TeamEntity sellingTeam, final List<ExchangedPlayerEntity> players) {
		for (final ExchangedPlayerEntity transferredOutPlayer : players) {
			for (final SelectedPlayerEntity selectedPlayer : sellingTeam.getSelectedPlayers()) {
				if (selectedPlayer.getPlayer().getId() == transferredOutPlayer.getPlayer().getId()) {
					selectedPlayer.setStillSelected(SelectedPlayerStatus.PENDING_TRANSFER_OUT);
				}
			}
		}
	}
	
	/**
	 * Set the status of a new transfer and any equivalent transfers to that one.
	 * 
	 * @param transfer New transfer model object containing all data on the transfer.
	 * @param openWindow The open transfer window object of the league in question.
	 * @param newTransfer New transfer entity object to set the status of. 
	 */
	private TransferStatus setTransferStatus(final Transfer transfer, final TransferWindowEntity openWindow, final TransferEntity newTransfer) {
		try {
			final TransferEntity equivalentTransfer = TransferUtils.getEquivalentTransfer(transfer, openWindow.getTransfers());
			equivalentTransfer.setStatus(TransferStatus.CONFIRMED);
			return TransferStatus.CONFIRMED;
			
		} catch (final FantasyDraftIntegrationException e) {
			newTransfer.setStatus(TransferStatus.PENDING);
			openWindow.addTransfer(newTransfer);
			return TransferStatus.PENDING;
		}
	}

	/**
	 * Create a list of the players who are being exchanged in a transfer.
	 * 
	 * @param transfer {@link Transfer} object containing transfer details.
	 * @return List of {@link PlayerEntity} objects who are who are being exchanged in a transfer.
	 * @throws FantasyDraftIntegrationException
	 */
	private List<ExchangedPlayerEntity> getPlayersBeingExchangedInTransfer(final Transfer transfer) throws FantasyDraftIntegrationException {
		final List<ExchangedPlayerEntity> exchangedPlayers = new ArrayList<ExchangedPlayerEntity>();
		
		if (transfer.getExchangedPlayers() != null) {
			for (final int playerId : transfer.getExchangedPlayers()) {
				exchangedPlayers.add(new ExchangedPlayerEntity(playerDataService.getPlayer(playerId)));
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
	private List<TransferredPlayerEntity> getPlayerEntitiesInvolvedInTransfer(final Transfer transfer) throws FantasyDraftIntegrationException {
		final List<TransferredPlayerEntity> players = new ArrayList<TransferredPlayerEntity>();
		
		for (final int playerId : transfer.getPlayers()) {
			players.add(new TransferredPlayerEntity(playerDataService.getPlayer(playerId)));
		}
		return players;
	}
}
