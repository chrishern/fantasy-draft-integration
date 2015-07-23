package net.blackcat.fantasy.draft.integration.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.facade.dto.AuctionBidsDto;
import net.blackcat.fantasy.draft.integration.model.Auction;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.service.AuctionPhaseResultsService;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.testdata.AuctionPhaseTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.AuctionTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.LeagueTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;
import net.blackcat.fantasy.draft.integration.testdata.dto.AuctionBidsDtoTestDataBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * Unit tests for {@link AuctionFacade}.
 * 
 * @author Chris Hern
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class AuctionFacadeTest {

    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Mock
    private LeagueDataService leagueDataService;

    @Mock
    private TeamDataService teamDataService;

    @Mock
    private PlayerDataService playerDataService;

    @Mock
    private AuctionPhaseResultsService auctionPhaseResultsService;

    private AuctionFacade auctionFacade;

    @Before
    public void setup() {

        auctionFacade = new AuctionFacade(leagueDataService, teamDataService, playerDataService, auctionPhaseResultsService);
    }

    @Test
    public void testStartAuction() throws Exception {
        // arrange
        final ArgumentCaptor<League> leagueCaptor = ArgumentCaptor.forClass(League.class);
        final League league = new League(TestDataConstants.LEAGUE_ONE_NAME);

        when(leagueDataService.getLeague(TestDataConstants.LEAGUE_ONE_ID)).thenReturn(league);

        // act
        auctionFacade.startAuction(TestDataConstants.LEAGUE_ONE_ID);

        // assert
        verify(leagueDataService).updateLeague(leagueCaptor.capture());
        assertThat(leagueCaptor.getValue().getAuction()).isNotNull();
    }

    @Test
    public void testMakeBids() throws Exception {
        // arrange

        // @formatter:off
        final ArgumentCaptor<League> leagueCaptor = ArgumentCaptor.forClass(League.class);
        
        final BigDecimal player1BidAmount = new BigDecimal("3.5");
        final BigDecimal player2BidAmount = new BigDecimal("5.0");

        final AuctionBidsDto auctionBidsDto = AuctionBidsDtoTestDataBuilder
                .anAuctionBidsDto()
                .withBid(TestDataConstants.PLAYER_ONE_ID, player1BidAmount)
                .withBid(TestDataConstants.PLAYER_TWO_ID, player2BidAmount)
                .build();
        
        final AuctionPhase auctionPhase = AuctionPhaseTestDataBuilder.anOpenAuctionPhase().build();

        final Auction auction = AuctionTestDataBuilder.anAuction().withPhase(auctionPhase).build();
        
        final League league = LeagueTestDataBuilder
                .aLeague()
                .withAuction(auction)
                .build();
        
        final Team team = new Team(TestDataConstants.TEAM_ONE_NAME);
        team.setLeague(league);
        
        final Player player1 = PlayerTestDataBuilder.aPlayer().withId(TestDataConstants.PLAYER_ONE_ID).build();
        final Player player2 = PlayerTestDataBuilder.aPlayer().withId(TestDataConstants.PLAYER_TWO_ID).build();
        
        final AuctionPhase expectedAuctionPhase = AuctionPhaseTestDataBuilder.anOpenAuctionPhase()
                .withBid(player1, team, player1BidAmount)
                .withBid(player2, team, player2BidAmount)
                .build();
        
        final Auction expectedAuction = AuctionTestDataBuilder.anAuction()
                .withPhase(expectedAuctionPhase)
                .build();

        final League expectedLeague = LeagueTestDataBuilder
                .aLeague()
                .withAuction(expectedAuction)
                .build();
        
        // @formatter:on

        when(teamDataService.getTeam(TestDataConstants.TEAM_ONE_ID)).thenReturn(team);
        when(playerDataService.getPlayer(TestDataConstants.PLAYER_ONE_ID)).thenReturn(player1);
        when(playerDataService.getPlayer(TestDataConstants.PLAYER_TWO_ID)).thenReturn(player2);
        when(leagueDataService.getOpenAuctionPhase(league)).thenReturn(auctionPhase);

        // act
        auctionFacade.makeBids(auctionBidsDto);

        // assert
        verify(leagueDataService).updateLeague(leagueCaptor.capture());
        ReflectionAssert.assertReflectionEquals(expectedLeague, leagueCaptor.getValue());
    }

    @Test
    public void testCloseAuction_Success() throws Exception {
        // arrange
        final League league = mock(League.class);

        when(leagueDataService.getLeague(league.getId())).thenReturn(league);
        when(leagueDataService.doesOpenAuctionPhaseExist(league)).thenReturn(false);
        when(league.hasOpenAuction()).thenReturn(true);

        // act
        auctionFacade.closeAuction(league.getId());

        // assert
        verify(league).closeAuction();
        verify(leagueDataService).updateLeague(league);
    }

    @Test
    public void testCloseAuction_LeagueHasNoAuction() throws Exception {
        // arrange
        final League league = mock(League.class);

        when(leagueDataService.getLeague(1)).thenReturn(league);
        when(league.hasOpenAuction()).thenReturn(false);

        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_NOT_FOUND));

        // act
        auctionFacade.closeAuction(1);

        // assert
        Assert.fail("Exception expected.");
    }

    @Test
    public void testCloseAuction_LeagueHasOpenAuctionPhase() throws Exception {
        // arrange
        final League league = mock(League.class);

        when(leagueDataService.getLeague(1)).thenReturn(league);
        when(leagueDataService.doesOpenAuctionPhaseExist(league)).thenReturn(true);
        when(league.hasOpenAuction()).thenReturn(true);

        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_PHASE_EXISTS));

        // act
        auctionFacade.closeAuction(1);

        // assert
        Assert.fail("Exception expected.");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCloseAuctionPhase() throws Exception {
        // arrange
        final Auction auction = AuctionTestDataBuilder.anAuctionWithOpenPhase().build();
        final AuctionPhase openAuctionPhase = auction.getPhases().get(0);
        final League league = LeagueTestDataBuilder.aLeague().withAuction(auction).build();
        final Team team = mock(Team.class);
        final Map<Integer, List<Bid>> playerBids = mock(HashMap.class);
        final Map<Integer, List<Bid>> playerBidsWithSuccess = mock(HashMap.class);
        final Map<Team, List<Bid>> successfulTeamBids = new HashMap<Team, List<Bid>>();
        final Bid successfulBid = mock(Bid.class);

        successfulTeamBids.put(team, Arrays.asList(successfulBid));

        when(leagueDataService.getLeague(1)).thenReturn(league);
        when(leagueDataService.getOpenAuctionPhase(league)).thenReturn(openAuctionPhase);
        when(auctionPhaseResultsService.buildUpPlayerBidList(openAuctionPhase)).thenReturn(playerBids);
        when(auctionPhaseResultsService.determineSuccessfulBids(playerBids)).thenReturn(playerBidsWithSuccess);
        when(auctionPhaseResultsService.getSuccessfulTeamBids(playerBidsWithSuccess)).thenReturn(successfulTeamBids);

        // act
        auctionFacade.closeAuctionPhase(1);

        // assert
        verify(team).processSuccessfulBid(successfulBid);
        verify(leagueDataService).updateLeague(league);
    }

    @Test
    public void testOpenAuctionPhase_Success() throws Exception {
        // arrange
        final League league = mock(League.class);

        when(leagueDataService.getLeague(1)).thenReturn(league);
        when(leagueDataService.doesOpenAuctionPhaseExist(league)).thenReturn(false);
        when(league.hasOpenAuction()).thenReturn(true);

        // act
        auctionFacade.openAuctionPhase(1);

        // assert
        verify(league).openAuctionPhase();
        verify(leagueDataService).updateLeague(league);
    }

    @Test
    public void testOpenAuctionPhase_NoOpenAuction() throws Exception {
        // arrange
        final League league = mock(League.class);

        when(leagueDataService.getLeague(1)).thenReturn(league);
        when(league.hasOpenAuction()).thenReturn(false);

        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_NOT_FOUND));

        // act
        auctionFacade.openAuctionPhase(1);

        // assert
        Assert.fail("Exception expected");
    }

    @Test
    public void testOpenAuctionPhase_AlreadyOpenAuctionPhase() throws Exception {
        // arrange
        final League league = mock(League.class);

        when(leagueDataService.getLeague(1)).thenReturn(league);
        when(leagueDataService.doesOpenAuctionPhaseExist(league)).thenReturn(true);
        when(league.hasOpenAuction()).thenReturn(true);

        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_PHASE_EXISTS));

        // act
        auctionFacade.openAuctionPhase(1);

        // assert
        Assert.fail("Exception expected");
    }
}
