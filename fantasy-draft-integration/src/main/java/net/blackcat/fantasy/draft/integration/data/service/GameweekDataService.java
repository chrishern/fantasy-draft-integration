/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.model.Gameweek;

/**
 * Data service for manipulating {@link Gameweek} entity objects.
 * 
 * @author Chris
 *
 */
public interface GameweekDataService {

	/**
	 * Update the given gameweek.
	 * 
	 * @param gameweek Updated gameweek to save.
	 */
	void updateGameweek(Gameweek gameweek);

	/**
	 * Get gameweek data.
	 * 
	 * @return Gameweek data.
	 */
	Gameweek getGameweek();
	
	/**
	 * Get the Gameweek score in a given gameweek for a certain team.
	 * 
	 * @param teamId ID of the team to get the team ID for.
	 * @param gameweek Gameweek number to get the score for.
	 * @return Gameweek score for the team in the given gameweek.
	 * @throws FantasyDraftIntegrationException
	 */
	int getGameweekScoreForTeam(int teamId, int gameweek) throws FantasyDraftIntegrationException;
}
