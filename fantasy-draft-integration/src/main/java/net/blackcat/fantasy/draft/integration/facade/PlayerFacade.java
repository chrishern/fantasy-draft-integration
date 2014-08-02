/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.List;

import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.Position;

/**
 * Facade for operations on the Player domain.
 * 
 * @author Chris
 *
 */
public interface PlayerFacade {

	/**
	 * Add a list of players.  
	 * 
	 * @param players Players to add.
	 */
	void addPlayers(List<Player> players);
	
	/**
	 * Get a list of all players.
	 * 
	 * @return List of all {@link Player} objects.
	 */
	List<Player> getPlayers();
	
	/**
	 * Get a list of all players in a given {@link Position}.
	 * 
	 * @return List of all {@link Player} objects in the requested position.
	 */
	List<Player> getPlayers(Position position);
}
