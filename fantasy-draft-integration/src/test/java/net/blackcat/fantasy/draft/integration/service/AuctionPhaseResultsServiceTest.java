/**
 * 
 */
package net.blackcat.fantasy.draft.integration.service;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.testdata.AuctionPhaseTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.BidTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link AuctionPhaseResultsService}.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionPhaseResultsServiceTest {

    private static final BigDecimal ONE_MILLION_BID = new BigDecimal("1.0");
    private static final BigDecimal TWO_MILLION_BID = new BigDecimal("2.0");

    private AuctionPhaseResultsService auctionPhaseResultsService;

    @Before
    public void setup() {
        auctionPhaseResultsService = new AuctionPhaseResultsService();
    }

    @Test
    public void testBuildUpPlayerBidList() {
        // arrange

        // @formatter:off
        
        final Player player1 = PlayerTestDataBuilder.aPlayer().withId(TestDataConstants.PLAYER_ONE_ID).build();
        final Player player2 = PlayerTestDataBuilder.aPlayer().withId(TestDataConstants.PLAYER_TWO_ID).build();

        final Team team1 = new Team(TestDataConstants.TEAM_ONE_NAME);
        final Team team2 = new Team(TestDataConstants.TEAM_TWO_NAME);
        
        final AuctionPhase phase = AuctionPhaseTestDataBuilder.anOpenAuctionPhase()
                .withBid(player1, team1, ONE_MILLION_BID)
                .withBid(player2, team1, ONE_MILLION_BID)
                .withBid(player1, team2, ONE_MILLION_BID)
                .build();

        // @formatter:on

        // act
        final Map<Integer, List<Bid>> playerBidList = auctionPhaseResultsService.buildUpPlayerBidList(phase);

        // assert
        assertThat(playerBidList.keySet()).containsOnly(TestDataConstants.PLAYER_ONE_ID, TestDataConstants.PLAYER_TWO_ID);
        assertThat(playerBidList.get(TestDataConstants.PLAYER_ONE_ID)).hasSize(2);
        assertThat(playerBidList.get(TestDataConstants.PLAYER_TWO_ID)).hasSize(1);
    }

    @Test
    public void testDetermineSuccessfulBids_OnlyOneBid() {
        // arrange
        final Bid bid = BidTestDataBuilder.aBid().build();

        final Map<Integer, List<Bid>> playerBids = new HashMap<Integer, List<Bid>>();

        playerBids.put(1, Arrays.asList(bid));

        // act
        final Map<Integer, List<Bid>> successfulBids = auctionPhaseResultsService.determineSuccessfulBids(playerBids);

        // assert
        assertThat(successfulBids.keySet()).containsOnly(1);
        assertThat(successfulBids.get(1)).hasSize(1);
        assertThat(successfulBids.get(1).get(0).isSuccessful()).isTrue();
        assertThat(successfulBids.get(1).get(0).getPlayer().getSelectionStatus()).isEqualTo(PlayerSelectionStatus.SELECTED);
    }

    @Test
    public void testDetermineSuccessfulBids_MatchingBids() {
        // arrange
        final Bid bid1 = BidTestDataBuilder.aBid().withAmount(ONE_MILLION_BID).build();
        final Bid bid2 = BidTestDataBuilder.aBid().withAmount(ONE_MILLION_BID).build();

        final Map<Integer, List<Bid>> playerBids = new HashMap<Integer, List<Bid>>();

        playerBids.put(1, Arrays.asList(bid1, bid2));

        // act
        final Map<Integer, List<Bid>> successfulBids = auctionPhaseResultsService.determineSuccessfulBids(playerBids);

        // assert
        assertThat(successfulBids.keySet()).containsOnly(1);
        assertThat(successfulBids.get(1)).hasSize(2);
        assertThat(successfulBids.get(1).get(0).isSuccessful()).isFalse();
        assertThat(successfulBids.get(1).get(0).getPlayer().getSelectionStatus()).isEqualTo(PlayerSelectionStatus.NOT_SELECTED);
        assertThat(successfulBids.get(1).get(1).isSuccessful()).isFalse();
        assertThat(successfulBids.get(1).get(1).getPlayer().getSelectionStatus()).isEqualTo(PlayerSelectionStatus.NOT_SELECTED);
    }

    @Test
    public void testDetermineSuccessfulBids_MultipleBids_OneWinning() {
        // arrange
        final Bid bid1 = BidTestDataBuilder.aBid().withAmount(ONE_MILLION_BID).build();
        final Bid bid2 = BidTestDataBuilder.aBid().withAmount(TWO_MILLION_BID).build();

        final Map<Integer, List<Bid>> playerBids = new HashMap<Integer, List<Bid>>();

        playerBids.put(1, Arrays.asList(bid1, bid2));

        // act
        final Map<Integer, List<Bid>> successfulBids = auctionPhaseResultsService.determineSuccessfulBids(playerBids);

        // assert
        assertThat(successfulBids.keySet()).containsOnly(1);
        assertThat(successfulBids.get(1)).hasSize(2);
        assertThat(successfulBids.get(1).get(0).isSuccessful()).isTrue();
        assertThat(successfulBids.get(1).get(0).getAmount()).isEqualTo(TWO_MILLION_BID);
        assertThat(successfulBids.get(1).get(0).getPlayer().getSelectionStatus()).isEqualTo(PlayerSelectionStatus.SELECTED);
        assertThat(successfulBids.get(1).get(1).isSuccessful()).isFalse();
        assertThat(successfulBids.get(1).get(1).getAmount()).isEqualTo(ONE_MILLION_BID);
        assertThat(successfulBids.get(1).get(1).getPlayer().getSelectionStatus()).isEqualTo(PlayerSelectionStatus.NOT_SELECTED);
    }

    @Test
    public void testGetSuccessfulTeamBids() {
        // arrange
        final Bid bid1 = BidTestDataBuilder.aSuccessfulBid().withAmount(TWO_MILLION_BID).withTeam(TestDataConstants.TEAM_ONE_NAME).build();
        final Bid bid2 = BidTestDataBuilder.aBid().withAmount(ONE_MILLION_BID).withTeam(TestDataConstants.TEAM_TWO_NAME).build();
        final Bid bid3 = BidTestDataBuilder.aSuccessfulBid().withAmount(TWO_MILLION_BID).withTeam(TestDataConstants.TEAM_ONE_NAME).build();
        final Bid bid4 = BidTestDataBuilder.aBid().withAmount(ONE_MILLION_BID).withTeam(TestDataConstants.TEAM_ONE_NAME).build();
        final Bid bid5 = BidTestDataBuilder.aBid().withAmount(ONE_MILLION_BID).withTeam(TestDataConstants.TEAM_TWO_NAME).build();

        final Map<Integer, List<Bid>> bids = new HashMap<Integer, List<Bid>>();

        bids.put(1, Arrays.asList(bid1, bid2));
        bids.put(2, Arrays.asList(bid3));
        bids.put(3, Arrays.asList(bid4, bid5));

        // act
        final Map<Team, List<Bid>> successfulTeamBids = auctionPhaseResultsService.getSuccessfulTeamBids(bids);

        // assert
        assertThat(successfulTeamBids.keySet()).containsOnly(new Team(TestDataConstants.TEAM_ONE_NAME));
        assertThat(successfulTeamBids.get(new Team(TestDataConstants.TEAM_ONE_NAME))).containsOnly(bid1, bid3);

    }
}
