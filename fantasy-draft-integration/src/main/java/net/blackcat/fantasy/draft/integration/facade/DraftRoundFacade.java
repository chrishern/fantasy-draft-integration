/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;

/**
 * Facade operations for a draft window.
 * 
 * @author Chris
 *
 */
public interface DraftRoundFacade {

	/**
	 * Start the auction phase with the given number for the given league.
	 * 
	 * @param leagueId Id of the league to start the auction phase for.
	 * @param phase The number of the auction phase to start.
	 * @throws FantasyDraftIntegrationException for the following reasons:
	 * 		<ul>
	 * 			<li><b>LEAGUE_DOES_NOT_EXIST</b> - If the given league does not exist.</li>
	 * 			<li><b>OPEN_DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE</b> - If the given league already has an open draft round.</li>
	 * 			<li><b>DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE</b> - If an action round for the given phase number already exists.</li>
	 * 		</ul>
	 */
	void startAuctionPhase(int leagueId, final int phase) throws FantasyDraftIntegrationException;
}
