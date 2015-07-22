/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.model.types.team.TeamStatus;
import net.blackcat.fantasy.draft.integration.testdata.BidTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TeamTestDataBuilder;
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

    @Test
    public void testProcessSuccessfulBid_DoesNotResultInCompleteSquad() {
        // arrange
        final Team team = new Team(TestDataConstants.TEAM_ONE_NAME);
        final BigDecimal startingBudget = team.getRemainingBudget();
        final BigDecimal bidAmount = new BigDecimal("15");
        final BigDecimal expectedRemainingBudget = startingBudget.subtract(bidAmount);

        final Bid bid = BidTestDataBuilder.aSuccessfulBid().withAmount(bidAmount).build();

        final Player playerBiddedFor = bid.getPlayer();

        // act
        team.processSuccessfulBid(bid);

        // assert
        assertThat(team.getRemainingBudget()).isEqualTo(expectedRemainingBudget);
        assertThat(team.getSelectedPlayers()).hasSize(1);
        assertThat(team.getSelectedPlayers().get(0).getPlayer()).isEqualTo(playerBiddedFor);
        assertThat(team.getStatus()).isEqualTo(TeamStatus.INCOMPLETE);
    }

    @Test
    public void testProcessSuccessfulBid_ResultsInCompleteSquad() {
        // arrange
        final Team team = TeamTestDataBuilder.aTeam().withSquadSize(15).build();
        final Bid bid = BidTestDataBuilder.aSuccessfulBid().build();

        // act
        team.processSuccessfulBid(bid);

        // assert
        assertThat(team.getStatus()).isEqualTo(TeamStatus.COMPLETE);
    }
}
