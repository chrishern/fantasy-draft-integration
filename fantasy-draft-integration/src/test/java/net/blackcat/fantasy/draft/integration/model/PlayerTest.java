/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.testdata.PlayerStatisticsTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for the {@link Player} class.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerTest {

    @Test
    public void testMarkPlayerAsSelected() {
        // arrange
        final Player player = PlayerTestDataBuilder.aPlayer().build();

        // act
        player.markPlayerAsSelected();

        // assert
        assertThat(player.getSelectionStatus()).isEqualTo(PlayerSelectionStatus.SELECTED);
    }

    @Test
    public void testWithStatistics() {
        // arrange
        final Player player = PlayerTestDataBuilder.aPlayer().build();
        final PlayerStatistics statistics = PlayerStatisticsTestDataBuilder.aPlayerStatistics().build();

        // act
        player.withStatistics(statistics);

        // assert
        assertThat(player.getTotalPoints()).isEqualTo(statistics.getTotalPoints());
        assertThat(player.getGoals()).isEqualTo(statistics.getGoals());
        assertThat(player.getAssists()).isEqualTo(statistics.getAssists());
        assertThat(player.getCleanSheets()).isEqualTo(statistics.getCleanSheets());
        assertThat(player.getPointsPerGame()).isEqualTo(statistics.getPointsPerGame());
    }
}
