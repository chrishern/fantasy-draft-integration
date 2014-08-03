/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.auction.AuctionPlayerResult;
import net.blackcat.fantasy.draft.auction.AuctionRoundResults;
import net.blackcat.fantasy.draft.integration.data.service.DraftRoundDataService;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.BidEntity;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.facade.DraftRoundFacadeImpl;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link DraftRoundFacadeImpl}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DraftRoundFacadeImplTest {

	private static final int LEAGUE_ID = 1;

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@Mock
	private LeagueDataService leagueDataService;
	
	@Mock
	private DraftRoundDataService draftRoundDataService;
	
	@Mock
	private TeamDataService teamDataService;
	
	@InjectMocks
	private DraftRoundFacadeImpl draftRoundFacade = new DraftRoundFacadeImpl();
	
	@Captor
	private ArgumentCaptor<DraftRoundEntity> draftRoundCaptor;
	
	@Captor
	private ArgumentCaptor<TeamEntity> teamCaptor;
	
	private LeagueEntity league;
	
	private TeamEntity team1;
	private TeamEntity team2;
	private TeamEntity team3;
	
	private PlayerEntity player1;
	private PlayerEntity player2;
	
	@Before
	public void setup() {
		league = new LeagueEntity(TestDataUtil.LEAGUE_NAME);
		
		team1 = new TeamEntity(TestDataUtil.TEST_TEAM_1);
		team2 = new TeamEntity(TestDataUtil.TEST_TEAM_2);
		team3 = new TeamEntity(TestDataUtil.TEST_TEAM_3);
		
		player1 = TestDataUtil.createEntityPlayer(1);
		player2 = TestDataUtil.createEntityPlayer(2);
	}
	
	@Test
	public void testStartAuctionPhase_Success() throws Exception {
		// arrange
		when(leagueDataService.getLeague(LEAGUE_ID)).thenReturn(league);
		
		// act
		draftRoundFacade.startAuctionPhase(LEAGUE_ID, 1);
		
		// assert
		verify(draftRoundDataService).createDraftRound(draftRoundCaptor.capture());
		
		assertThat(draftRoundCaptor.getValue().getStatus()).isEqualTo(DraftRoundStatus.OPEN);
		assertThat(draftRoundCaptor.getValue().getLeague()).isEqualTo(league);
		assertThat(draftRoundCaptor.getValue().getKey().getBiddingPhase()).isEqualTo(DraftRoundPhase.AUCTION);
		assertThat(draftRoundCaptor.getValue().getKey().getSequenceNumber()).isEqualTo(1);
	}
	
	@Test
	public void testStartAuctionPhase_LeagueDoesNotExist() throws Exception {
		// arrange
		when(leagueDataService.getLeague(LEAGUE_ID)).thenThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		// act
		draftRoundFacade.startAuctionPhase(LEAGUE_ID, 1);
		
		// assert
		verify(draftRoundDataService, never()).createDraftRound(any(DraftRoundEntity.class));
	}

	@Test
	public void testStartAuctionPhase_OpenRoundAlreadyExists() throws Exception {
		// arrange
		when(leagueDataService.getLeague(LEAGUE_ID)).thenReturn(league);
		doThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE)).when(
				draftRoundDataService).createDraftRound(any(DraftRoundEntity.class));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE));
		
		// act
		draftRoundFacade.startAuctionPhase(LEAGUE_ID, 1);
		
		// assert
	}
	
	@Test
	public void testCloseAuctionPhase_Success() throws Exception {
		// arrange
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		
		final BidEntity bidP1T1 = new BidEntity(team1, player1, new BigDecimal("1.5"));
		final BidEntity bidP1T2 = new BidEntity(team2, player1, new BigDecimal("4.5"));
		final BidEntity bidP1T3 = new BidEntity(team3, player1, new BigDecimal("4"));
		final BidEntity bidP2T1 = new BidEntity(team1, player2, new BigDecimal("1"));
		
		draftRound.addBids(Arrays.asList(bidP1T1, bidP1T2, bidP1T3, bidP2T1));
		
		when(draftRoundDataService.getOpenDraftRound(anyInt())).thenReturn(draftRound);
		
		// act
		final AuctionRoundResults auctionRoundResults = draftRoundFacade.closeAuctionPhase(0);
		
		// assert
		assertThat(auctionRoundResults.getAuctionPhase()).isEqualTo(1);
		
		final List<AuctionPlayerResult> playerResults = auctionRoundResults.getPlayerResults();
		assertThat(playerResults).hasSize(2);
		
		final AuctionPlayerResult player1Result = playerResults.get(0);
		assertThat(player1Result.getMatchingHighBids()).isNull();
		
		assertThat(player1Result.getSuccessfulBid()).isNotNull();
		assertThat(player1Result.getSuccessfulBid().getAmount().doubleValue()).isEqualTo(4.5d);
		assertThat(player1Result.getSuccessfulBid().getTeam().getTeamName()).isEqualTo(team2.getName());
		
		assertThat(player1Result.getUnsuccessfulBids()).isNotNull();
		assertThat(player1Result.getUnsuccessfulBids()).hasSize(2);
		assertThat(player1Result.getUnsuccessfulBids().get(0).getAmount().doubleValue()).isEqualTo(4d);
		assertThat(player1Result.getUnsuccessfulBids().get(0).getTeam().getTeamName()).isEqualTo(team3.getName());
		assertThat(player1Result.getUnsuccessfulBids().get(1).getAmount().doubleValue()).isEqualTo(1.5d);
		assertThat(player1Result.getUnsuccessfulBids().get(1).getTeam().getTeamName()).isEqualTo(team1.getName());
		
		verify(draftRoundDataService).updateDraftRound(draftRoundCaptor.capture());
		
		final DraftRoundEntity updatedDraftRound = draftRoundCaptor.getValue();

		assertThat(updatedDraftRound.getStatus()).isEqualTo(DraftRoundStatus.CLOSED);
		assertThat(updatedDraftRound.getBids().get(0).isSuccessful()).isFalse();
		assertThat(updatedDraftRound.getBids().get(0).getPlayer().getSelectionStatus()).isEqualTo(PlayerSelectionStatus.SELECTED);
		assertThat(updatedDraftRound.getBids().get(1).isSuccessful()).isTrue();
		assertThat(updatedDraftRound.getBids().get(2).isSuccessful()).isFalse();
		assertThat(updatedDraftRound.getBids().get(3).isSuccessful()).isTrue();
		
		verify(teamDataService, times(2)).updateTeam(teamCaptor.capture());
		
		assertThat(teamCaptor.getAllValues()).hasSize(2);
		assertThat(teamCaptor.getAllValues()).containsOnly(team1, team2);
	}
	
	@Test
	public void testCloseAuctionPhase_Success_PlayersWithMatchingBids() throws Exception {
		// arrange
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		
		final BidEntity bidP1T1 = new BidEntity(team1, player1, new BigDecimal("1.5"));
		final BidEntity bidP1T2 = new BidEntity(team2, player1, new BigDecimal("1.5"));
		final BidEntity bidP1T3 = new BidEntity(team3, player1, new BigDecimal("1"));
		
		draftRound.addBids(Arrays.asList(bidP1T1, bidP1T2, bidP1T3));
		
		when(draftRoundDataService.getOpenDraftRound(anyInt())).thenReturn(draftRound);
		
		// act
		final AuctionRoundResults auctionRoundResults = draftRoundFacade.closeAuctionPhase(0);
		
		// assert
		final List<AuctionPlayerResult> playerResults = auctionRoundResults.getPlayerResults();
		assertThat(playerResults).hasSize(1);
		
		final AuctionPlayerResult player1Result = playerResults.get(0);
		assertThat(player1Result.getSuccessfulBid()).isNull();
		assertThat(player1Result.getUnsuccessfulBids()).isNotNull();
		assertThat(player1Result.getMatchingHighBids()).isNotNull();
		
		assertThat(player1Result.getMatchingHighBids()).hasSize(2);
		assertThat(player1Result.getMatchingHighBids().get(0).getAmount().doubleValue()).isEqualTo(1.5d);
		assertThat(player1Result.getMatchingHighBids().get(0).getTeam().getTeamName()).isEqualTo(team1.getName());
		
		verify(draftRoundDataService).updateDraftRound(draftRoundCaptor.capture());
		
		final DraftRoundEntity updatedDraftRound = draftRoundCaptor.getValue();
		
		assertThat(updatedDraftRound.getBids().get(0).getPlayer().getSelectionStatus()).isEqualTo(PlayerSelectionStatus.RESTRICTED_SELECTION);
		assertThat(updatedDraftRound.getBids().get(0).getPlayer().getTeamsWhoCanBid()).hasSize(2);
		
		verify(teamDataService, never()).updateTeam(any(TeamEntity.class));
	}

	@Test
	public void testCloseAuctionPhase_NoOpenAuctionPhaseForLeague() throws Exception {
		// arrange
		when(draftRoundDataService.getOpenDraftRound(anyInt())).thenThrow(
				new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_DOES_NOT_EXIST_FOR_LEAGUE));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_DOES_NOT_EXIST_FOR_LEAGUE));
		
		// act
		draftRoundFacade.closeAuctionPhase(0);
		
		// assert
		Assert.fail("Exception expected");
	}
}
