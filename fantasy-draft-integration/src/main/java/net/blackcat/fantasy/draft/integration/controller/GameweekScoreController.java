/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import java.util.Map;

import net.blackcat.fantasy.draft.integration.facade.GameweekScoreFacade;
import net.blackcat.fantasy.draft.player.GameweekScorePlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

	@Autowired
	@Qualifier(value = "gameweekScoreFacade")
	private GameweekScoreFacade gameweekScoreFacade;
	
	/**
	 * Stores the gameweek scores for a given list of players.
	 * 
	 * @param gameweekScores Map of player ID to {@link GameweekScorePlayer} storing the gameweek scores for each player.
	 */
	public void storeGameweekScores(final Map<Integer, GameweekScorePlayer> gameweekScores) {
		gameweekScoreFacade.storeCurrentGameweekScores(gameweekScores);
	}
}
