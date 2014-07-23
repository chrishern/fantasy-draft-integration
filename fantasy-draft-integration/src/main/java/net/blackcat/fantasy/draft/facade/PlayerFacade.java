/**
 * 
 */
package net.blackcat.fantasy.draft.facade;

import java.util.List;

import net.blackcat.fantasy.draft.player.Player;

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
}
