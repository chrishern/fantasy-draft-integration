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
	 * Stores the current gameweek scores for a given list of players.
	 * 
	 * @param currentGameweekScores Map of player ID to {@link GameweekScorePlayer} storing the gameweek scores for each player.
	 */
	void storeCurrentGameweekScores(final Map<Integer, GameweekScorePlayer> currentGameweekScores);
}
