/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import java.util.List;

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
	
	/**
	 * Get the {@link LeagueEntity} that a given team is part of.
	 * 
	 * @param teamId Id of the team to get the league for.
	 * @return {@link LeagueEntity} for the given tea.
	 * @throws FantasyDraftIntegrationException If a team with the given id does not exist.
	 */
	LeagueEntity getLeagueForTeam(int teamId) throws FantasyDraftIntegrationException;
	
	/**
	 * Get all leagues stored in the backend.
	 * 
	 * @return All {@link LeagueEntity} objects currently stored in the backend.
	 */
	List<LeagueEntity> getLeagues();
}
