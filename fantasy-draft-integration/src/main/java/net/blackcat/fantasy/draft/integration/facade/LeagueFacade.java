/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.league.League;


/**
 * Operations for performing functionality on leagues.
 * 
 * @author Chris
 *
 */
public interface LeagueFacade {

	/**
	 * Create a new league.
	 * 
	 * @param leagueName Name of the league to create.
	 */
	void createLeague(String leagueName);
	
	/**
	 * Get the current league table for a given league.
	 * 
	 * @param leagueId The ID of the league we want the table for.
	 * @return {@link League} object containing the teams in the league in descending points order.
	 * @throws FantasyDraftIntegrationException If a league with the given ID can't be found.
	 */
	League getLeagueTable(int leagueId) throws FantasyDraftIntegrationException;
}
