/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter.player;

import net.blackcat.fantasy.draft.integration.converter.IntegrationConverter;
import net.blackcat.fantasy.draft.integration.facade.dto.PlayerDto;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.PlayerStatistics;

import org.springframework.core.convert.converter.Converter;

/**
 * Converter to convert {@link PlayerDto} objects into {@link Player} objects.
 * 
 * @author Chris Hern
 * 
 */
@IntegrationConverter
public class PlayerDtoToPlayerConverter implements Converter<PlayerDto, Player> {

    @Override
    public Player convert(final PlayerDto dto) {

        final Player player = new Player(dto.getId(), dto.getForename(), dto.getSurname(), dto.getTeam(), dto.getPosition(), dto.getCurrentPrice());

        final PlayerStatistics statistics =
                new PlayerStatistics(dto.getTotalPoints(), dto.getGoals(), dto.getAssists(), dto.getCleanSheets(), dto.getPointsPerGame());

        player.withStatistics(statistics);

        return player;
    }

}
