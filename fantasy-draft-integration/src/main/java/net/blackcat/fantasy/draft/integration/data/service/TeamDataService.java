/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;

/**
 * Defines operations on the {@link TeamEntity}.
 * 
 * @author Chris
 *
 */
public interface TeamDataService {

	/**
	 * Create a new team within the back end.
	 * 
	 * @param teamName Name of the team to create.
	 */
	void createTeam(String teamName);
	
	/**
	 * Update a {@link TeamEntity}.
	 * 
	 * @param team Team to update.
	 */
	void updateTeam(TeamEntity team);

	/**
	 * Get a team based on the name of the team.
	 * 
	 * @param teamName Name of the team to find
	 * @return {@link TeamEntity} with the given name. 
	 * @throws FantasyDraftIntegrationException If a team with the given name does not exist.
	 */
	TeamEntity getTeam(String teamName) throws FantasyDraftIntegrationException;

}
