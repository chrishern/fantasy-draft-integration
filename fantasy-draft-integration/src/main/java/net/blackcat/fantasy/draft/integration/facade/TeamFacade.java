/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.team.Team;

/**
 * Operations for manipulating team related data.
 * 
 * @author Chris
 *
 */
public interface TeamFacade {

	/**
	 * Create a new team within the back end.
	 * 
	 * @param teamName Name of the team to create.
	 */
	void createTeam(String teamName);
	
	/**
	 * Get a team based on the name of the team.
	 * 
	 * @param teamName Name of the team to find
	 * @return {@link Team} with the given name. 
	 * @throws FantasyDraftIntegrationException If a team with the given name does not exist.
	 */
	Team getTeam(String teamName) throws FantasyDraftIntegrationException;
}
