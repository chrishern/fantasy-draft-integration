/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import java.util.List;

import net.blackcat.fantasy.draft.player.GameweekScorePlayer;

import org.springframework.stereotype.Controller;


/**
 * Controller class for invoking operations around gameweek score scores.  
 * 
 * This is the entry point for all external applications who wish to process gameweek score data.
 * 
 * @author Chris
 *
 */
@Controller(value = "gameweekScoreIntegrationController")
public class GameweekScoreController {

	/**
	 * Stores the gameweek scores for a given list of players.
	 * 
	 * @param gameweekScores List of {@link GameweekScorePlayer} storing the gameweek scores for each player.
	 */
	public void storeGameweekScores(final List<GameweekScorePlayer> gameweekScores) {
		
	}
}
