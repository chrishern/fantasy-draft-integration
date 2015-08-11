/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

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
}
