/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.List;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.transfer.Transfer;
import net.blackcat.fantasy.draft.transfer.TransferSummary;

/**
 * Facade for performing transfer window related operations.
 * 
 * @author Chris
 *
 */
public interface TransferWindowFacade {

	/**
	 * Start the transfer window with the given number for the given league.
	 * 
	 * @param leagueId Id of the league to start the transfer window for.
	 * @param phase The number of the transfer window to start.
	 * @throws FantasyDraftIntegrationException for the following reasons:
	 * 		<ul>
	 * 			<li><b>LEAGUE_DOES_NOT_EXIST</b> - If the given league does not exist.</li>
	 * 			<li><b>OPEN_TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE</b> - If the given league already has an open transfer window.</li>
	 * 			<li><b>TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE</b> - If transfer window for the given phase number already exists.</li>
	 * 		</ul>
	 */
	void startTransferWindow(int leagueId, final int phase) throws FantasyDraftIntegrationException;
	
	/**
	 * Add a given transfer to a window.
	 * 
	 * @param transfer Transfer to add.
	 * @throws FantasyDraftIntegrationException
	 */
	void addTransfer(Transfer transfer) throws FantasyDraftIntegrationException;
	
	/**
	 * Sell a given player from a given team to the pot.
	 * 
	 * @param teamId The ID of the team selling the player to the pot.
	 * @param playerId The ID of the player being sold to the pot.
	 * @throws FantasyDraftIntegrationException
	 */
	void sellPlayerToPot(int teamId, int playerId) throws FantasyDraftIntegrationException;
	
	/**
	 * Get the transfers for a given team in the open transfer window.
	 * 
	 * @param teamId ID of the team we want the transfers for.
	 * @return Transfers for the given team.
	 * @throws FantasyDraftIntegrationException
	 */
	List<TransferSummary> getTransfersForTeamInOpenWindow(int teamId) throws FantasyDraftIntegrationException;
	
	/**
	 * Move the transfer window status onto auction phase.  This involves moving all players in and out of the
	 * respective teams based on all transfers in the window.
	 * 
	 * @param leagueId ID of the league to move the transfer window phase on for.
	 * @throws FantasyDraftIntegrationException
	 */
	void moveTransferWindowOntoAuction(int leagueId) throws FantasyDraftIntegrationException;
}
