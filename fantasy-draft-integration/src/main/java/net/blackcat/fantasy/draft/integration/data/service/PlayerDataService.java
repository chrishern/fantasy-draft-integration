/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import java.util.List;

import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.player.types.Position;

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
	void addPlayers(List<PlayerEntity> players);
	
	/**
	 * Get a list of all players from the back end.
	 * 
	 * @return List of all {@link PlayerEntity} objects.
	 */
	List<PlayerEntity> getPlayers();
	
	/**
	 * Get a list of players who play a given {@link Position}.
	 * 
	 * @param position The position we are interested in.
	 * @return List of {@link PlayerEntity} objects in the required position.
	 */
	List<PlayerEntity> getPlayers(Position position);
}
