/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.converter.ConverterService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.facade.dto.PlayerDto;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;
import net.blackcat.fantasy.draft.integration.model.types.player.SelectedPlayerStatus;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Facade over player operations.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerFacade {

    private PlayerDataService playerDataService;
    private ConverterService converterService;

    @Autowired
    public PlayerFacade(final PlayerDataService playerDataService, final ConverterService converterService) {

        this.playerDataService = playerDataService;
        this.converterService = converterService;
    }

    /**
     * Add a list of players to the game.
     * 
     * @param playerDtos
     *            List of {@link PlayerDto} objects containing the player data to add to the system.
     */
    public void addPlayers(final List<PlayerDto> playerDtos) {

        final List<Player> modelPlayers = new ArrayList<Player>();

        for (final PlayerDto playerDto : playerDtos) {
            final Player modelPlayer = converterService.convert(playerDto, Player.class);
            modelPlayers.add(modelPlayer);
        }

        playerDataService.addPlayers(modelPlayers);
    }

    /**
     * Get a list of {@link Player} objects in a given {@link Position} and with a certain {@link PlayerSelectionStatus}
     * .
     * 
     * @param position
     *            {@link Position} of the players to get.
     * @param selectionStatus
     *            {@link SelectedPlayerStatus} of the players to get.
     * @return {@link Player} objects with the desired {@link Position} and {@link SelectedPlayerStatus}.
     */
    public List<PlayerDto> getPlayers(final Position position, final PlayerSelectionStatus selectionStatus) {

        final List<PlayerDto> playerDtos = new ArrayList<PlayerDto>();

        for (final Player player : playerDataService.getPlayers(position, selectionStatus)) {
            final PlayerDto playerDto = converterService.convert(player, PlayerDto.class);
            playerDtos.add(playerDto);
        }

        return playerDtos;
    }
}
