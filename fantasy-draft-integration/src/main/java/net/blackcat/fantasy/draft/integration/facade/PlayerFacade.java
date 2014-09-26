/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.player.FplCostPlayer;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
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
	
	/**
	 * Get a list of all players in a given {@link Position} and with a certain {@link PlayerSelectionStatus}.
	 * 
	 * @param position Position of the players we want.
	 * @param selectionStatus The selection status of the players we want.
	 * @return List of all {@link Player} objects in the requested position.
	 */
	List<Player> getPlayers(Position position, PlayerSelectionStatus selectionStatus);
	
	/**
	 * Update the current price of all players in the game with the latest data from FPL.
	 * 
	 * @param playersWithScores Map of the players containing the latest price from FPL.
	 */
	void updatePlayersCurrentPrice(Map<Integer, FplCostPlayer> playersWithScores);
}
