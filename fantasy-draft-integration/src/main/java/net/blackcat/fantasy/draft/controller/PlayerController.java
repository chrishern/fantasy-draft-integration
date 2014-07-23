/**
 * 
 */
package net.blackcat.fantasy.draft.controller;

import java.util.List;

import net.blackcat.fantasy.draft.facade.PlayerFacade;
import net.blackcat.fantasy.draft.player.Player;

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
}
