/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import java.util.List;

import net.blackcat.fantasy.draft.auction.AuctionRoundResults;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.TransferWindowFacade;
import net.blackcat.fantasy.draft.round.TeamBids;
import net.blackcat.fantasy.draft.transfer.LeagueTransferWindowSummary;
import net.blackcat.fantasy.draft.transfer.Transfer;
import net.blackcat.fantasy.draft.transfer.TransferSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Controller class for invoking transfer window based operations.  
 * 
 * This is the entry point for all external applications who wish to  access/process transfer window data.
 * 
 * @author Chris
 *
 */
@Controller(value = "transferWindowIntegrationController")
public class TransferWindowController {

	@Autowired
	@Qualifier(value = "transferWindowFacade")
	private TransferWindowFacade transferWindowFacade;
	
	/**
	 * Start the transfer window with the given number for the given league.
	 * 
	 * @param leagueId Id of the league to start the transfer window for.
	 * @param phase The number of the transfer window to start.
	 * @throws FantasyDraftIntegrationException for the following reasons:
	 * 		<ul>
	 * 			<li><b>LEAGUE_DOES_NOT_EXIST</b> - If the given league does not exist.</li>
	 * 			<li><b>OPEN_TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE</b> - If the given league already has an open transfer window.</li>
	 * 			<li><b>TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE</b> - If a transfer window for the given phase number already exists.</li>
	 * 		</ul>
	 */
	public void startTransferWindow(final int leagueId, final int phase) throws FantasyDraftIntegrationException {
		transferWindowFacade.startTransferWindow(leagueId, phase);
	}
	
	/**
	 * Add a given transfer to a window.
	 * 
	 * @param transfer Transfer to add.
	 * @throws FantasyDraftIntegrationException
	 */
	public void addTransfer(final Transfer transfer) throws FantasyDraftIntegrationException {
		transferWindowFacade.addTransfer(transfer);
	}
	
	/**
	 * Sell a given player from a given team to the pot.
	 * 
	 * @param teamId The ID of the team selling the player to the pot.
	 * @param playerId The ID of the player being sold to the pot.
	 * @throws FantasyDraftIntegrationException
	 */
	public void sellPlayerToPot(final int teamId, final int playerId) throws FantasyDraftIntegrationException {
		transferWindowFacade.sellPlayerToPot(teamId, playerId);
	}
	
	/**
	 * Get the transfers for a given team in the open transfer window.
	 * 
	 * @param teamId ID of the team we want the transfers for.
	 * @return Transfers for the given team.
	 * @throws FantasyDraftIntegrationException
	 */
	public List<TransferSummary> getTransfersForTeamInOpenWindow(int teamId) throws FantasyDraftIntegrationException {
		return transferWindowFacade.getTransfersForTeamInOpenWindow(teamId);
	}
	
	/**
	 * Move the transfer window status onto auction phase.  This involves moving all players in and out of the
	 * respective teams based on all transfers in the window.
	 * 
	 * @param leagueId ID of the league to move the transfer window phase on for.
	 * @throws FantasyDraftIntegrationException
	 */
	public void moveTransferWindowOntoAuction(int leagueId) throws FantasyDraftIntegrationException {
		transferWindowFacade.moveTransferWindowOntoAuction(leagueId);
	}
	
	/**
	 * Get the transfer window summary for a given league.
	 * 
	 * @param leagueId ID of the league to get the transfer window summary for.
	 * @return Transfer window summary for the league,
	 */
	public LeagueTransferWindowSummary getLeagueTransferWindowSummary(final int leagueId, final int overallSequenceNumber) throws FantasyDraftIntegrationException {
		return transferWindowFacade.getLeagueTransferWindowSummary(leagueId, overallSequenceNumber);
	}
	
	/**
	 * Make a list of bids for a team.
	 * 
	 * @param teamId Team the bids are being made for.
	 * @param teamBids List of bids the team wants to make.
	 * @throws FantasyDraftIntegrationException for the following reasons:
	 */
	public void makeBids(final TeamBids teamBids) throws FantasyDraftIntegrationException {
		transferWindowFacade.makeBids(teamBids);
	}
	
	public AuctionRoundResults closeTransferWindow(int leagueId) throws FantasyDraftIntegrationException {
		return transferWindowFacade.closeTransferWindow(leagueId);
	}
}
