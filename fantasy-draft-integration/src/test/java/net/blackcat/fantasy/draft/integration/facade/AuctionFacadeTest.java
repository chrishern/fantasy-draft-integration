package net.blackcat.fantasy.draft.integration.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.facade.dto.AuctionBidsDto;
import net.blackcat.fantasy.draft.integration.model.Auction;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.testdata.AuctionPhaseTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.AuctionTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.LeagueTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;
import net.blackcat.fantasy.draft.integration.testdata.dto.AuctionBidsDtoTestDataBuilder;

import org.junit.Before;
import org.junit.Test;
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

    @Mock
    private LeagueDataService leagueDataService;

    @Mock
    private TeamDataService teamDataService;

    @Mock
    private PlayerDataService playerDataService;

    private AuctionFacade auctionFacade;

    @Before
    public void setup() {

        auctionFacade = new AuctionFacade(leagueDataService, teamDataService, playerDataService);
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
}
