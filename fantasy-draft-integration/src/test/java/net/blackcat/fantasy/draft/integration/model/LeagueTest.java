/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionStatus;
import net.blackcat.fantasy.draft.integration.testdata.LeagueTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.Test;

/**
 * Unit tests for the {@link League} class.
 * 
 * @author Chris Hern
 * 
 */
public class LeagueTest {

    @Test
    public void testAddTeam() {
        // arrange
        final League league = new League(TestDataConstants.LEAGUE_ONE_NAME);
        final Team team = new Team(TestDataConstants.TEAM_ONE_NAME);

        // act
        league.addTeam(team);

        // assert
        assertThat(league.getTeams()).containsOnly(team);
        assertThat(team.getLeague()).isEqualTo(league);
    }

    @Test
    public void testHasOpenAuction_True() {
        // arrange
        final League league = LeagueTestDataBuilder.aLeague().withAuction().build();

        // act
        final boolean hasOpenAuction = league.hasOpenAuction();

        // assert
        assertThat(hasOpenAuction).isTrue();
    }

    @Test
    public void testHasOpenAuction_False_AuctionNull() {
        // arrange
        final League league = LeagueTestDataBuilder.aLeague().build();

        // act
        final boolean hasOpenAuction = league.hasOpenAuction();

        // assert
        assertThat(hasOpenAuction).isFalse();
    }

    @Test
    public void testHasOpenAuction_False_AuctionStatusClosed() {
        // arrange
        final Auction auction = new Auction();
        auction.setStatus(AuctionStatus.CLOSED);

        final League league = LeagueTestDataBuilder.aLeague().withAuction(auction).build();

        // act
        final boolean hasOpenAuction = league.hasOpenAuction();

        // assert
        assertThat(hasOpenAuction).isFalse();
    }
}
