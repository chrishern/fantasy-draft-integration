/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter.player;

import net.blackcat.fantasy.draft.integration.converter.IntegrationConverter;
import net.blackcat.fantasy.draft.integration.facade.dto.PlayerDto;
import net.blackcat.fantasy.draft.integration.model.Player;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Converter which converts {@link Player} objects to {@link PlayerDto} objects.
 * 
 * @author Chris Hern
 * 
 */
@IntegrationConverter
public class PlayerToPlayerDtoConverter implements Converter<Player, PlayerDto> {

    @Override
    public PlayerDto convert(final Player player) {

        final PlayerDto playerDto = new PlayerDto(player.getId());

        BeanUtils.copyProperties(player, playerDto);

        return playerDto;
    }

}
