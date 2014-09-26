/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;

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
}