/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;

/**
 * Defined data operations on the {@link LeagueEntity}.
 * 
 * @author Chris
 *
 */
public interface LeagueDataService {

	/**
	 * Create a new league within the backend.
	 * 
	 * @param leagueName Name of the league to create.
	 */
	void createLeague(String leagueName);
	
	/**
	 * Get a {@link LeagueEntity} with a given id.
	 * 
	 * @param leagueId Id of the league to get.
	 * @return {@link LeagueEntity} with the given id.
	 * @throws FantasyDraftIntegrationException If a league with the given id does not exist.
	 */
	LeagueEntity getLeague(int leagueId) throws FantasyDraftIntegrationException;
}
