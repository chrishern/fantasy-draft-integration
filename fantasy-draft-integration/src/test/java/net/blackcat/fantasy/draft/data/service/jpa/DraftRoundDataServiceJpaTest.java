/**
 * 
 */
package net.blackcat.fantasy.draft.data.service.jpa;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.jpa.DraftRoundDataServiceJpa;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;
import net.blackcat.fantasy.draft.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.test.util.TestDataUtil;

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
 * Unit tests for {@link DraftRoundDataServiceJpa}.
 * 
 * @author Chris
 *
 */
@Transactional		
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"/hsqlDatasourceContext.xml", "/testApplicationContext.xml"})
public class DraftRoundDataServiceJpaTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "draftRoundDataServiceJpa")
	private DraftRoundDataServiceJpa dataService;

	private LeagueEntity league;
	
	@Before
	public void setup() {
		league = new LeagueEntity(TestDataUtil.LEAGUE_NAME);
		
		entityManager.persist(league);
	}

	@Test
	public void testCreateDraftRound_Success() throws Exception {
		// arrange
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		
		// act
		dataService.createDraftRound(draftRound);
		
		// assert
		final List<DraftRoundEntity> draftRounds = entityManager.createQuery("SELECT d FROM DraftRoundEntity d", DraftRoundEntity.class).getResultList();
		
		assertThat(draftRounds).hasSize(1);
		
		final DraftRoundEntity retrievedDraftRound = draftRounds.get(0);
		
		assertThat(retrievedDraftRound.getStatus()).isEqualTo(DraftRoundStatus.OPEN);
		assertThat(retrievedDraftRound.getKey().getBiddingPhase()).isEqualTo(DraftRoundPhase.AUCTION);
		assertThat(retrievedDraftRound.getKey().getSequenceNumber()).isEqualTo(1);
		assertThat(retrievedDraftRound.getKey().getLeague()).isEqualTo(league.getId());
	}
	
	@Test
	public void testCreateDraftRound_OpenDraftRoundAlreadyExists() throws Exception {
		// arrange
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		dataService.createDraftRound(draftRound);
		
		final LeagueEntity secondLeague = new LeagueEntity("Second League");
		entityManager.persist(secondLeague);
		
		final DraftRoundEntity draftRoundForSecondLeague = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, secondLeague);
		draftRoundForSecondLeague.setStatus(DraftRoundStatus.CLOSED);
		dataService.createDraftRound(draftRoundForSecondLeague);
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE));
		
		// act
		final DraftRoundEntity draftRound2 = new DraftRoundEntity(DraftRoundPhase.AUCTION, 2, league);
		dataService.createDraftRound(draftRound2);
		
		// assert
	}
	
	@Test
	public void testCreateDraftRound_DraftRoundAlreadyExists() throws Exception {
		// arrange
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		draftRound.setStatus(DraftRoundStatus.CLOSED);
		dataService.createDraftRound(draftRound);
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE));
		
		// act
		final DraftRoundEntity duplicateDraftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		dataService.createDraftRound(duplicateDraftRound);
		
		// assert
	}
}
