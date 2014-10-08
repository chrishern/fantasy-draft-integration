/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferEntity;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit tests for {@link TransferWindowDataServiceJpa}.
 * 
 * @author Chris
 *
 */
@Transactional		
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"/hsqlDatasourceContext.xml", "/testApplicationContext.xml"})
public class TransferWindowDataServiceJpaTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "transferWindowDataServiceJpa")
	private TransferWindowDataServiceJpa dataService;
	
	private LeagueEntity league;
	
	@Before
	public void setup() {
		league = new LeagueEntity(TestDataUtil.LEAGUE_NAME);
	}
	
	@Test
	public void testCreateTransferWindow() throws Exception {
		// arrange
		entityManager.persist(league);
		
		final TransferWindowEntity transferWindow = new TransferWindowEntity(1, league);
		
		// act
		dataService.createTransferWindow(transferWindow);
		
		// assert
		final List<TransferWindowEntity> transferWindows = entityManager.createQuery("SELECT d FROM TransferWindowEntity d", TransferWindowEntity.class).getResultList();
		
		assertThat(transferWindows).hasSize(1);
		
		final TransferWindowEntity retrievedTransferWindow = transferWindows.get(0);
		
		assertThat(retrievedTransferWindow.getStatus()).isEqualTo(DraftRoundStatus.OPEN);
		assertThat(retrievedTransferWindow.getKey().getBiddingPhase()).isEqualTo(DraftRoundPhase.TRANSFER_WINDOW);
		assertThat(retrievedTransferWindow.getKey().getSequenceNumber()).isEqualTo(1);
		assertThat(retrievedTransferWindow.getKey().getLeague()).isEqualTo(league.getId());
	}
	
	@Test
	public void testCreateDraftRound_OpenDraftRoundAlreadyExists() throws Exception {
		// arrange
		final TransferWindowEntity transferWindow = new TransferWindowEntity(1, league);
		dataService.createTransferWindow(transferWindow);
		
		final LeagueEntity secondLeague = new LeagueEntity("Second League");
		entityManager.persist(secondLeague);
		
		final TransferWindowEntity transferWindowForSecondLeague = new TransferWindowEntity(1, secondLeague);
		transferWindowForSecondLeague.setStatus(DraftRoundStatus.CLOSED);
		dataService.createTransferWindow(transferWindowForSecondLeague);
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE));
		
		// act
		final TransferWindowEntity draftRound2 = new TransferWindowEntity(2, league);
		dataService.createTransferWindow(draftRound2);
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testCreateDraftRound_DraftRoundAlreadyExists() throws Exception {
		// arrange
		final TransferWindowEntity draftRound = new TransferWindowEntity(1, league);
		draftRound.setStatus(DraftRoundStatus.CLOSED);
		dataService.createTransferWindow(draftRound);
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE));
		
		// act
		final TransferWindowEntity duplicateDraftRound = new TransferWindowEntity(1, league);
		dataService.createTransferWindow(duplicateDraftRound);
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetOpenTransferWindow_Success() throws Exception {
		// arrange
		final TransferWindowEntity draftRound = new TransferWindowEntity(1, league);
		draftRound.setStatus(DraftRoundStatus.OPEN);
		dataService.createTransferWindow(draftRound);
		
		// act
		final TransferWindowEntity openTransferWindow = dataService.getOpenTransferWindow(league.getId());
		
		// assert
		assertThat(openTransferWindow.getLeague()).isEqualTo(league);
	}
	
	@Test
	public void testGetOpenTransferWindow_OnlyClosedWindowExists() throws Exception {
		// arrange
		final TransferWindowEntity draftRound = new TransferWindowEntity(1, league);
		draftRound.setStatus(DraftRoundStatus.CLOSED);
		dataService.createTransferWindow(draftRound);

		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_TRANSFER_DOES_NOT_EXIST_FOR_LEAGUE));
		
		// act
		dataService.getOpenTransferWindow(league.getId());
		
		// assert
		Assert.fail("Exception expected");
	}

	@Test
	public void testGetOpenTransferWindow_WindowForLeagueDoesNotExist() throws Exception {
		// arrange
		final TransferWindowEntity draftRound = new TransferWindowEntity(1, league);
		draftRound.setStatus(DraftRoundStatus.CLOSED);
		dataService.createTransferWindow(draftRound);
		
		final LeagueEntity league2 = new LeagueEntity();
		final TransferWindowEntity draftRound2 = new TransferWindowEntity(1, league2);
		draftRound2.setStatus(DraftRoundStatus.OPEN);
		dataService.createTransferWindow(draftRound2);

		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_TRANSFER_DOES_NOT_EXIST_FOR_LEAGUE));
		
		// act
		dataService.getOpenTransferWindow(league.getId());
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testUpdateTransferWindow() throws Exception {
		// arrange
		final TransferWindowEntity draftRound = new TransferWindowEntity(1, league);
		draftRound.setStatus(DraftRoundStatus.OPEN);
		dataService.createTransferWindow(draftRound);
		
		final TransferWindowEntity openTransferWindow = dataService.getOpenTransferWindow(league.getId());
		assertThat(openTransferWindow.getTransfers()).isNull();

		// act
		final TransferEntity transfer = new TransferEntity();
		transfer.setAmount(new BigDecimal("4"));
		openTransferWindow.addTransfer(transfer);
		
		dataService.updateTransferWindow(openTransferWindow);
		
		// assert
		final TransferWindowEntity updatedTransferWindow = dataService.getOpenTransferWindow(league.getId());

		assertThat(updatedTransferWindow.getTransfers()).isNotEmpty();
	}
	
	@Test
	public void testGetTransfers() {
		// arrange
		final TeamEntity team1 = mock(TeamEntity.class);
		final TeamEntity team2 = mock(TeamEntity.class);
		final TeamEntity team3 = mock(TeamEntity.class);
		
		when(team1.getId()).thenReturn(1);
		when(team2.getId()).thenReturn(2);
		when(team3.getId()).thenReturn(3);
		
		final TransferEntity transfer1 = new TransferEntity();
		transfer1.setSellingTeam(team1);
		
		final TransferEntity transfer2 = new TransferEntity();
		transfer2.setSellingTeam(team2);
		transfer2.setBuyingTeam(team1);
		
		final TransferEntity transfer3 = new TransferEntity();
		transfer3.setSellingTeam(team1);
		transfer3.setBuyingTeam(team2);
		
		final TransferEntity transfer4 = new TransferEntity();
		transfer4.setSellingTeam(team3);
		transfer4.setBuyingTeam(team2);
		
		final TransferEntity transfer5 = new TransferEntity();
		transfer5.setSellingTeam(team3);
		
		final TransferWindowEntity transferWindow = new TransferWindowEntity();
		transferWindow.addTransfer(transfer1);
		transferWindow.addTransfer(transfer2);
		transferWindow.addTransfer(transfer3);
		transferWindow.addTransfer(transfer4);
		transferWindow.addTransfer(transfer5);
		
		// act
		final List<TransferEntity> transfersForTeam = dataService.getTransfers(transferWindow, 1);
		
		// assert
		assertThat(transfersForTeam).hasSize(3);
	}
}
