/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.entity.GameweekEntity;

/**
 * Data service defining data operations around gameweek data.
 * 
 * @author Chris
 *
 */
public interface GameweekDataService {

	/**
	 * Get the gameweek data from the back end.
	 * 
	 * @return Gameweek data stored in the back end.
	 */
	GameweekEntity getGameweekData();
	
	/**
	 * Update the stored gameweek data.
	 * 
	 * @param gameweekData Updated gameweek data.
	 */
	void updateGameweekData(GameweekEntity gameweekData);
}
