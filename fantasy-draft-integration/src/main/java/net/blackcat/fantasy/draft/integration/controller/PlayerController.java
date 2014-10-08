/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.PlayerFacade;
import net.blackcat.fantasy.draft.player.FplCostPlayer;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.player.types.Position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Controller class for invoking {@link Player} based operations.  
 * 
 * This is the entry point for all external applications who wish to access/process {@link Player} data.
 * 
 * @author Chris
 *
 */
@Controller(value = "playerIntegrationController")
public class PlayerController {

	@Autowired
	@Qualifier(value = "playerFacade")
	private PlayerFacade playerFacade;
	
	/**
	 * Add a list of players.  
	 * 
	 * @param players Players to add.
	 */
	public void addPlayers(final List<Player> players) {
		playerFacade.addPlayers(players);
	}
	
	/**
	 * Get a list of all players.
	 * 
	 * @return List of all {@link Player} objects.
	 */
	public List<Player> getPlayers() {
		return playerFacade.getPlayers();
	}
	
	/**
	 * Get a list of all players in a given {@link Position}.
	 * 
	 * @return List of all {@link Player} objects in the requested position.
	 */
	public List<Player> getPlayers(final Position position) {
		return playerFacade.getPlayers(position);
	}
	
	/**
	 * Get a list of all players in a given {@link Position} and with a certain {@link PlayerSelectionStatus}.
	 * 
	 * @param position Position of the players we want.
	 * @param selectionStatus The selection status of the players we want.
	 * @return List of all {@link Player} objects in the requested position.
	 */
	public List<Player> getPlayers(final Position position, final PlayerSelectionStatus selectionStatus) {
		return playerFacade.getPlayers(position, selectionStatus);
	}
	
	public List<Player> getPlayers(final Position position, final PlayerSelectionStatus selectionStatus, final int teamId) throws FantasyDraftIntegrationException {
		return playerFacade.getPlayers(position, selectionStatus, teamId);
	}
	
	/**
	 * Update the current price of all players in the game with the latest data from FPL.
	 * 
	 * @param playersWithScores Map of the players containing the latest price from FPL.
	 */
	public void updatePlayersCurrentPrice(final Map<Integer, FplCostPlayer> playersWithScores) {
		playerFacade.updatePlayersCurrentPrice(playersWithScores);
	}
}
