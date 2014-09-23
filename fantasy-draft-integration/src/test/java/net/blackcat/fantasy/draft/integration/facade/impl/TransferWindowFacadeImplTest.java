/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TransferWindowDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferWindowEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
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
 * Unit tests for {@link TransferWindowFacadeImplTest}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TransferWindowFacadeImplTest {

	private static final int LEAGUE_ID = 1;
	
	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@Mock
	private LeagueDataService leagueDataService;
	
	@Mock
	private TransferWindowDataService transferWindowDataService;
	
	@InjectMocks
	private TransferWindowFacadeImpl transferWindowFacade = new TransferWindowFacadeImpl();
	
	@Captor
	private ArgumentCaptor<TransferWindowEntity> transferWindowCaptor;
	
	private LeagueEntity league;
	
	@Before
	public void setup() {
		league = new LeagueEntity(TestDataUtil.LEAGUE_NAME);
	}
	
	@Test
	public void testStartTransferWindow_Success() throws Exception {
		// arrange
		when(leagueDataService.getLeague(LEAGUE_ID)).thenReturn(league);
		
		// act
		transferWindowFacade.startTransferWindow(LEAGUE_ID, 1);
		
		// assert
		verify(transferWindowDataService).createTransferWindow(transferWindowCaptor.capture());
		
		assertThat(transferWindowCaptor.getValue().getStatus()).isEqualTo(DraftRoundStatus.OPEN);
		assertThat(transferWindowCaptor.getValue().getLeague()).isEqualTo(league);
		assertThat(transferWindowCaptor.getValue().getKey().getBiddingPhase()).isEqualTo(DraftRoundPhase.TRANSFER_WINDOW);
		assertThat(transferWindowCaptor.getValue().getKey().getSequenceNumber()).isEqualTo(1);
	}
	
	@Test
	public void testStartTransferWindow_LeagueDoesNotExist() throws Exception {
		// arrange
		when(leagueDataService.getLeague(LEAGUE_ID)).thenThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		// act
		transferWindowFacade.startTransferWindow(LEAGUE_ID, 1);
		
		// assert
		Assert.fail("Exception expected");
	}

	@Test
	public void testStartAuctionPhase_OpenRoundAlreadyExists() throws Exception {
		// arrange
		when(leagueDataService.getLeague(LEAGUE_ID)).thenReturn(league);
		doThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE)).when(
				transferWindowDataService).createTransferWindow(any(TransferWindowEntity.class));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE));
		
		// act
		transferWindowFacade.startTransferWindow(LEAGUE_ID, 1);
		
		// assert
		Assert.fail("Exception expected");
	}

}
