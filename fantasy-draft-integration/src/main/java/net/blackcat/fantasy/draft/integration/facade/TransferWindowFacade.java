package net.blackcat.fantasy.draft.integration.facade;

import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.dto.OpenTransferWindowTeamTransfersDto;
import net.blackcat.fantasy.draft.integration.facade.dto.SellPlayerToPotDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TransferBidDto;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.Transfer;
import net.blackcat.fantasy.draft.integration.model.TransferWindow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TransferWindowFacade {

	private TeamDataService teamDataService;
	private LeagueDataService leagueDataService;
	
	@Autowired
	public TransferWindowFacade(final TeamDataService teamDataService, final LeagueDataService leagueDataService) {
		this.teamDataService = teamDataService;
		this.leagueDataService = leagueDataService;
	}

	/**
	 * Sell a player to the pot.
	 * 
	 * @param sellPlayerToPotDto DTO containing details the player being sold to the pot.
	 * @throws FantasyDraftIntegrationException
	 */
	public void sellPlayerToPot(final SellPlayerToPotDto sellPlayerToPotDto) throws FantasyDraftIntegrationException {
		
		final Team teamForManager = teamDataService.getTeamForManager(sellPlayerToPotDto.getManagerEmailAddress());
    	final League league = teamForManager.getLeague();
    	final TransferWindow transferWindow = league.getTransferWindows().get(0);
    	final SelectedPlayer soldToPotPlayer = teamForManager.getSelectedPlayer(sellPlayerToPotDto.getSelectedPlayerId());
    	
    	teamForManager.sellPlayerToPot(sellPlayerToPotDto.getSelectedPlayerId(), sellPlayerToPotDto.getAmount());
    	transferWindow.addPotSale(teamForManager, soldToPotPlayer, sellPlayerToPotDto.getAmount());
    	
    	leagueDataService.updateLeague(league);
	}
	
	/**
	 * Make a transfer bid in the current transfer window.
	 * 
	 * @param transferBid DTO containing transfer bid details.
	 * @throws FantasyDraftIntegrationException
	 */
	public void makeTransferBid(final TransferBidDto transferBid) throws FantasyDraftIntegrationException {
		
		final Team buyingTeam = teamDataService.getTeam(transferBid.getBuyingTeam());
		final Team sellingTeam = teamDataService.getTeam(transferBid.getSellingTeam());
		final SelectedPlayer playerSubjectOfBid = sellingTeam.getSelectedPlayer(transferBid.getSelectedPlayerSubjectOfBid());
		final League league = buyingTeam.getLeague();
		final TransferWindow transferWindow = league.getTransferWindows().get(0);
		
		transferWindow.makeTransferBid(buyingTeam, sellingTeam, playerSubjectOfBid, transferBid.getAmount());
		
		leagueDataService.updateLeague(league);
	}
	
	/**
	 * Accept a transfer bid that has been made in this window.
	 * 
	 * @param managerEmailAddress Email address of the manager who has accepted the bid.
	 * @param transferId ID of the bid to accept.
	 * @throws FantasyDraftIntegrationException
	 */
	public void acceptTransferBid(final String managerEmailAddress, final int transferId) throws FantasyDraftIntegrationException {
		
		final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);
    	final League league = teamForManager.getLeague();
    	final TransferWindow transferWindow = league.getTransferWindows().get(0);
    	
    	transferWindow.acceptTransfer(transferId);
    	
    	leagueDataService.updateLeague(league);
	}
	
	/**
	 * Reject a transfer bid that has been made in this window.
	 * 
	 * @param managerEmailAddress Email address of the manager who has rejected the bid.
	 * @param transferId ID of the bid to reject.
	 * @throws FantasyDraftIntegrationException
	 */
	public void rejectTransferBid(final String managerEmailAddress, final int transferId) throws FantasyDraftIntegrationException {
		
		final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);
    	final League league = teamForManager.getLeague();
    	final TransferWindow transferWindow = league.getTransferWindows().get(0);
    	
    	transferWindow.rejectTransfer(transferId);
    	
    	leagueDataService.updateLeague(league);
	}
	
	public OpenTransferWindowTeamTransfersDto getTeamTransferWindowTransferDetail(final String managerEmailAddress) throws FantasyDraftIntegrationException {
		
		final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);
    	final League league = teamForManager.getLeague();
    	final TransferWindow transferWindow = league.getTransferWindows().get(0);
    	final int teamId = teamForManager.getId();
    	
    	final OpenTransferWindowTeamTransfersDto transferDetail = new OpenTransferWindowTeamTransfersDto(teamId);
    	
    	for (final Transfer transfer : transferWindow.getTransfers()) {
    		
    		final int transferId = transfer.getId();
    		final String playerForename = transfer.getPlayerBiddedFor().getPlayer().getForename();
    		final String playerSurname = transfer.getPlayerBiddedFor().getPlayer().getSurname();
    		final String buyingTeam = transfer.getBuyingTeam().getName();
    		final String sellingTeam = transfer.getSellingTeam().getName();
    		final BigDecimal amount = transfer.getAmount();
    		
    		if (transfer.getBuyingTeam().getId() == teamId) {
    			if (transfer.isAccepted()) {
    				transferDetail.addBoughtPlayer(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			} else if (transfer.isRejected()) {
    				transferDetail.addOutgoingRejectedBid(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			} else {
    				transferDetail.addPendingBid(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			}
    		} else if (transfer.getSellingTeam().getId() == teamId) {
    			if (transfer.isAccepted()) {
    				transferDetail.addSoldPlayer(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			} else if (transfer.isRejected()) {
    				transferDetail.addIncomingRejectedBid(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			} else {
    				transferDetail.addIncomingPendingBid(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			}
    		}
    	}
    	
    	return transferDetail;
	}
}
