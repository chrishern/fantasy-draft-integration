/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.Test;

/**
 * Unit tests for {@link Team}
 * 
 * @author Chris Hern
 * 
 */
public class TeamTest {

    private static final BigDecimal ONE_MILLION = new BigDecimal("1.0");

    @Test
    public void testEquals_True() {
        // arrange
        final Team team1 = new Team(TestDataConstants.TEAM_ONE_NAME);
        final Team team2 = new Team(TestDataConstants.TEAM_ONE_NAME);

        // act
        final boolean equals = team1.equals(team2);

        // assert
        assertThat(equals).isTrue();
    }

    @Test
    public void testEquals_False() {
        // arrange
        final Team team1 = new Team(TestDataConstants.TEAM_ONE_NAME);
        final Team team2 = new Team(TestDataConstants.TEAM_TWO_NAME);

        // act
        final boolean equals = team1.equals(team2);

        // assert
        assertThat(equals).isFalse();
    }

    @Test
    public void testAddPlayer() {
        // arrange
        final Player player = PlayerTestDataBuilder.aPlayer().build();
        final SelectedPlayer selectedPlayer = new SelectedPlayer(player, ONE_MILLION, ONE_MILLION);
        final Team team = new Team(TestDataConstants.TEAM_ONE_NAME);

        // act
        team.addSelectedPlayer(selectedPlayer);

        // assert
        assertThat(team.getSelectedPlayers()).contains(selectedPlayer);
    }

    @Test
    public void testAddToTotalScore() {
        // arrange
        final Team team = new Team(TestDataConstants.TEAM_ONE_NAME);
        final int startingScore = team.getTotalScore();
        final int amountToAdd = 15;
        final int expectedTotalScore = startingScore + amountToAdd;

        // act
        team.addToTotalScore(amountToAdd);

        // assert
        assertThat(team.getTotalScore()).isEqualTo(expectedTotalScore);
    }
}
