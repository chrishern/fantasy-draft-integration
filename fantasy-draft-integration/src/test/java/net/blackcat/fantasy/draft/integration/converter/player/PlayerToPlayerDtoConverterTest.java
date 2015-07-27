/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter.player;

import net.blackcat.fantasy.draft.integration.facade.dto.PlayerDto;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * Unit tests for {@link PlayerToPlayerDtoConverter}.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerToPlayerDtoConverterTest {

    private PlayerToPlayerDtoConverter converter = new PlayerToPlayerDtoConverter();

    @Test
    public void testConvert() {
        // arrange
        final Player player = PlayerTestDataBuilder.aPlayer().build();
        final PlayerDto expectedPlayerDto =
                new PlayerDto(player.getId(), player.getForename(), player.getSurname(), player.getTeam(), player.getPosition(),
                        player.getCurrentPrice());

        // act
        final PlayerDto actualPlayerDto = converter.convert(player);

        // assert
        ReflectionAssert.assertReflectionEquals(expectedPlayerDto, actualPlayerDto);
    }

}
