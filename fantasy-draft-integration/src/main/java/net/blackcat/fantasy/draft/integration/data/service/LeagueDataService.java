/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;

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
}
