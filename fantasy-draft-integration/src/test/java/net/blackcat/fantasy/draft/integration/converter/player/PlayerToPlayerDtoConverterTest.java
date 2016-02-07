/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter.player;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.facade.dto.PlayerDto;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;

import org.junit.Test;

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

        // act
        final PlayerDto actualPlayerDto = converter.convert(player);

        // assert
        assertThat(actualPlayerDto.getAssists()).isEqualTo(player.getAssists());
        assertThat(actualPlayerDto.getForename()).isEqualTo(player.getForename());
    }
}
