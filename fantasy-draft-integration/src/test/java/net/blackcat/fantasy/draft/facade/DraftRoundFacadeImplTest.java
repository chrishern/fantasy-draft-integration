/**
 * 
 */
package net.blackcat.fantasy.draft.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.blackcat.fantasy.draft.integration.data.service.DraftRoundDataService;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.facade.DraftRoundFacadeImpl;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;
import net.blackcat.fantasy.draft.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.test.util.TestDataUtil;

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
	
	@InjectMocks
	private DraftRoundFacadeImpl draftRoundFacade = new DraftRoundFacadeImpl();
	
	@Captor
	private ArgumentCaptor<DraftRoundEntity> draftRoundCaptor;
	
	private LeagueEntity league;
	
	@Before
	public void setup() {
		league = new LeagueEntity(TestDataUtil.LEAGUE_NAME);
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

}
