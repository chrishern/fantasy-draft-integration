/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import java.util.List;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;

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
     * @param players
     *            Players to add.
     */
    void addPlayers(List<Player> players);

    /**
     * Get a list of players who play a given {@link Position}.
     * 
     * @param position
     *            The position we are interested in.
     * @return List of {@link Player} objects in the required position.
     */
    List<Player> getPlayers(Position position);

    /**
     * Get a list of players who play a given {@link Position} and have a given {@link PlayerSelectionStatus}.
     * 
     * @param position
     *            The position we are interested in.
     * @param selectionStatus
     *            The selection status we are interested in.
     * @return List of {@link Player} objects in the required position.
     */
    List<Player> getPlayers(Position position, PlayerSelectionStatus selectionStatus);

    /**
     * Get a single players based on ID.
     * 
     * @param id
     *            ID of the player to search for.
     * @return {@link Player} with the given ID.
     * @throws FantasyDraftIntegrationException
     *             If a player with the given ID does not exist.
     */
    Player getPlayer(int id) throws FantasyDraftIntegrationException;

    /**
     * Update the given player in the backend.
     * 
     * @param updatedPlayer
     *            {@link Player} to update with updated data.
     */
    void updatePlayer(Player updatedPlayer);
}
