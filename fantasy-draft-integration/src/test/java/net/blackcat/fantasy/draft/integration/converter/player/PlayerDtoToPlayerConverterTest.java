/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter.player;

import net.blackcat.fantasy.draft.integration.facade.dto.PlayerDto;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.PlayerStatistics;
import net.blackcat.fantasy.draft.integration.testdata.dto.PlayerDtoTestDataBuilder;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * Unit tests for {@link PlayerDtoToPlayerConverter}.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerDtoToPlayerConverterTest {

    private PlayerDtoToPlayerConverter converter = new PlayerDtoToPlayerConverter();

    @Test
    public void testConvert() {
        // arrange
        final PlayerDto dto = PlayerDtoTestDataBuilder.aPlayer().withStatistics().build();
        final Player expectedPlayer =
                new Player(dto.getId(), dto.getForename(), dto.getSurname(), dto.getTeam(), dto.getPosition(), dto.getCurrentPrice());
        final PlayerStatistics statistics =
                new PlayerStatistics(dto.getTotalPoints(), dto.getGoals(), dto.getAssists(), dto.getCleanSheets(), dto.getPointsPerGame());

        expectedPlayer.withStatistics(statistics);

        // act
        final Player actualPlayer = converter.convert(dto);

        // assert
        ReflectionAssert.assertReflectionEquals(expectedPlayer, actualPlayer);
    }

}
