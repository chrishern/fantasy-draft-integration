/**
 * 
 */
package net.blackcat.fantasy.draft.data.service;

import java.util.List;

import net.blackcat.fantasy.draft.player.types.Player;

/**
 * Defines operations for accessing Player data from the back end data store.
 * 
 * @author Chris
 *
 */
public interface PlayerDataService {

	/**
	 * Add a list of players to the back end.  
	 * 
	 * If any error occurs when saving any of the players, this will be written to an audit file.
	 * 
	 * @param players Players to add.
	 */
	void addPlayers(List<Player> players);
	
	/**
	 * Get a list of all players from the back end.
	 * 
	 * @return List of all {@link Player} objects.
	 */
	List<Player> getPlayers();
}
