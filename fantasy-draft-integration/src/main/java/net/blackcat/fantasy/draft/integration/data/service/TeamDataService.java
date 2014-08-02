/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.entity.TeamEntity;

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
}
