/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.Map;

import net.blackcat.fantasy.draft.player.GameweekScorePlayer;

/**
 * Facade containing operations concerning gameweek scores.
 * 
 * @author Chris
 *
 */
public interface GameweekScoreFacade {

	/**
	 * Stores the gameweek scores for a given list of players.
	 * 
	 * @param gameweek The number of the gameweek the scores relate to.
	 * @param gameweekScores Map of player ID to {@link GameweekScorePlayer} storing the gameweek scores for each player.
	 */
	void storeGameweekScores(int gameweek, final Map<Integer, GameweekScorePlayer> gameweekScores);
}
