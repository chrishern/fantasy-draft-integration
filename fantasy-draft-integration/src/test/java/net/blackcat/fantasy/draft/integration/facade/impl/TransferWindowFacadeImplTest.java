/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.data.service.TransferWindowDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferWindowEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferredPlayerEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;
import net.blackcat.fantasy.draft.transfer.Transfer;
import net.blackcat.fantasy.draft.transfer.types.TransferStatus;
import net.blackcat.fantasy.draft.transfer.types.TransferType;

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

	private static final BigDecimal REMAINING_BUDGET = new BigDecimal("0.5");

	private static final int LEAGUE_ID = 1;
	
	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@Mock
	private LeagueDataService leagueDataService;
	
	@Mock
	private TeamDataService teamDataService;
	
	@Mock
	private PlayerDataService playerDataService;
	
	@Mock
	private TransferWindowDataService transferWindowDataService;
	
	@InjectMocks
	private TransferWindowFacadeImpl transferWindowFacade = new TransferWindowFacadeImpl();
	
	@Captor
	private ArgumentCaptor<TransferWindowEntity> transferWindowCaptor;
	
	@Captor
	private ArgumentCaptor<TeamEntity> teamCaptor;
	
	private LeagueEntity league;

	private TransferWindowEntity transferWindow;
	
	@Before
	public void setup() {
		league = new LeagueEntity(TestDataUtil.LEAGUE_NAME);
		
		transferWindow = new TransferWindowEntity(1, league);
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
	
	@Test
	public void testAddTransfer_Pending() throws Exception {
		// arrange
		final List<TeamEntity> teamEntities = TestDataUtil.createTeamEntitiesWithScore();
		final int buyingTeamId = 1;
		final int sellingTeamId = 2;
		
		final Transfer transfer = new Transfer(buyingTeamId, sellingTeamId, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), new BigDecimal("3.5"));

		when(teamDataService.getTeam(buyingTeamId)).thenReturn(teamEntities.get(0));
		when(teamDataService.getTeam(sellingTeamId)).thenReturn(teamEntities.get(1));

		when(playerDataService.getPlayer(TestDataUtil.PLAYER_1_ID)).thenReturn(TestDataUtil.createEntityPlayer(TestDataUtil.PLAYER_1_ID));
		
		when(leagueDataService.getLeagueForTeam(anyInt())).thenReturn(league);
		
		when(transferWindowDataService.getOpenTransferWindow(anyInt())).thenReturn(transferWindow);
		
		// act
		transferWindowFacade.addTransfer(transfer);
		
		// assert
		verify(transferWindowDataService).updateTransferWindow(transferWindowCaptor.capture());
		
		assertThat(transferWindowCaptor.getValue().getTransfers()).hasSize(1);
		assertThat(transferWindowCaptor.getValue().getTransfers().get(0).getStatus()).isEqualTo(TransferStatus.PENDING);
	}
	
	@Test
	public void testAddTransfer_Confirmed() throws Exception {
		// arrange
		final TeamEntity buyingTeam = mock(TeamEntity.class);
		final TeamEntity sellingTeam = mock(TeamEntity.class);
		
		final PlayerEntity entityPlayer = TestDataUtil.createEntityPlayer(TestDataUtil.PLAYER_1_ID);
		final TransferredPlayerEntity transferredPlayer = new TransferredPlayerEntity(entityPlayer);
		final int buyingTeamId = 1;
		final int sellingTeamId = 2;
		
		final TransferEntity transferEntity = new TransferEntity(Arrays.asList(transferredPlayer), sellingTeam, buyingTeam, null, new BigDecimal("3.5"));
		transferEntity.setStatus(TransferStatus.PENDING);
		transferWindow.addTransfer(transferEntity);
		
		final Transfer transfer = new Transfer(buyingTeamId, sellingTeamId, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), new BigDecimal("3.5"));

		when(buyingTeam.getId()).thenReturn(buyingTeamId);
		when(sellingTeam.getId()).thenReturn(sellingTeamId);
		when(buyingTeam.getRemainingBudget()).thenReturn(REMAINING_BUDGET);
		when(sellingTeam.getRemainingBudget()).thenReturn(REMAINING_BUDGET);
		
		when(teamDataService.getTeam(buyingTeamId)).thenReturn(buyingTeam);
		when(teamDataService.getTeam(sellingTeamId)).thenReturn(sellingTeam);

		when(playerDataService.getPlayer(TestDataUtil.PLAYER_1_ID)).thenReturn(entityPlayer);
		
		when(leagueDataService.getLeagueForTeam(anyInt())).thenReturn(league);
		
		when(transferWindowDataService.getOpenTransferWindow(anyInt())).thenReturn(transferWindow);
		
		// act
		transferWindowFacade.addTransfer(transfer);
		
		// assert
		verify(teamDataService).updateTeam(sellingTeam);
		verify(teamDataService).updateTeam(buyingTeam);
		verify(transferWindowDataService).updateTransferWindow(transferWindowCaptor.capture());
		
		assertThat(transferWindowCaptor.getValue().getTransfers()).hasSize(1);
		assertThat(transferWindowCaptor.getValue().getTransfers().get(0).getStatus()).isEqualTo(TransferStatus.CONFIRMED);
	}
	
	@Test
	public void testSellPlayerToPot() throws Exception {
		// arrange
		final List<TeamEntity> teamEntities = TestDataUtil.createTeamEntitiesWithScore();

		when(teamDataService.getTeam(anyInt())).thenReturn(teamEntities.get(0));
		when(leagueDataService.getLeagueForTeam(anyInt())).thenReturn(league);
		when(transferWindowDataService.getOpenTransferWindow(anyInt())).thenReturn(transferWindow);
		
		// act
		transferWindowFacade.sellPlayerToPot(1, TestDataUtil.PLAYER_1_ID);
		
		// assert
		verify(teamDataService).updateTeam(teamCaptor.capture());
		
		assertThat(teamCaptor.getValue().getSelectedPlayers().get(0).getSelectedPlayerStatus()).isEqualTo(SelectedPlayerStatus.PENDING_SALE_TO_POT);
		assertThat(teamCaptor.getValue().getRemainingBudget().doubleValue()).isEqualTo(52.5d);
		
		verify(transferWindowDataService).updateTransferWindow(transferWindowCaptor.capture());
		
		assertThat(transferWindowCaptor.getValue().getTransfers()).hasSize(1);
		
		final TransferEntity transfer = transferWindowCaptor.getValue().getTransfers().get(0);
		
		assertThat(transfer.getBuyingTeam()).isNull();
		assertThat(transfer.getExchangedPlayers()).isNull();
		assertThat(transfer.getAmount().doubleValue()).isEqualTo(7.0d);
		assertThat(transfer.getSellingTeam()).isEqualTo(teamEntities.get(0));
		assertThat(transfer.getPlayers()).hasSize(1);
		assertThat(transfer.getPlayers().get(0).getPlayer()).isEqualTo(teamEntities.get(0).getSelectedPlayers().get(0).getPlayer());
		assertThat(transfer.getStatus()).isEqualTo(TransferStatus.CONFIRMED);
	}
	
	@Test
	public void testSellPlayerToPot_AlreadySold() throws Exception {
		// arrange
		final List<TeamEntity> teamEntities = TestDataUtil.createTeamEntitiesWithScore();
		
		for (final SelectedPlayerEntity selectedPlayer : teamEntities.get(0).getSelectedPlayers()) {
			if (selectedPlayer.getPlayer().getId() == TestDataUtil.PLAYER_1_ID) {
				selectedPlayer.setStillSelected(SelectedPlayerStatus.PENDING_SALE_TO_POT);
			}
		}

		when(teamDataService.getTeam(anyInt())).thenReturn(teamEntities.get(0));
		
		// act
		transferWindowFacade.sellPlayerToPot(1, TestDataUtil.PLAYER_1_ID);
		
		// assert
		verify(teamDataService, never()).updateTeam(any(TeamEntity.class));
		verify(transferWindowDataService, never()).updateTransferWindow(any(TransferWindowEntity.class));
	}

}
