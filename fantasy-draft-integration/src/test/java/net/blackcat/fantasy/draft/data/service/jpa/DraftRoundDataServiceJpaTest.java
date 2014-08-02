/**
 * 
 */
package net.blackcat.fantasy.draft.data.service.jpa;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.jpa.DraftRoundDataServiceJpa;
import net.blackcat.fantasy.draft.integration.entity.BidEntity;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.entity.key.DraftRoundKey;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;
import net.blackcat.fantasy.draft.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.test.util.TestDataUtil;

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
	private TeamEntity team;
	private PlayerEntity player1;
	private PlayerEntity player2;
	private BidEntity bidPlayer1;
	private BidEntity bidPlayer2;
	
	@Before
	public void setup() {
		league = new LeagueEntity(TestDataUtil.LEAGUE_NAME);
		entityManager.persist(league);
		
		team = new TeamEntity(TestDataUtil.TEST_TEAM_1);
		entityManager.persist(team);
		
		player1 = TestDataUtil.createEntityPlayer(1);
		entityManager.persist(player1);
		
		player2 = TestDataUtil.createEntityPlayer(2);
		entityManager.persist(player2);
		
		bidPlayer1 = new BidEntity(team, player1, new BigDecimal("3"));
		bidPlayer2 = new BidEntity(team, player2, new BigDecimal("6"));

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
		Assert.fail("Exception expected");
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
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testAddBids_Success_FirstSetOfBids() throws Exception {
		// arrange
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		dataService.createDraftRound(draftRound);
		
		// act
		dataService.addBids(draftRound, Arrays.asList(bidPlayer1, bidPlayer2));
		
		// assert
		final DraftRoundKey draftRoundKey = new DraftRoundKey(DraftRoundPhase.AUCTION, 1, league.getId());
		final DraftRoundEntity retrievedObject = entityManager.find(DraftRoundEntity.class, draftRoundKey);
		assertThat(retrievedObject.getBids()).hasSize(2);
	}
	
	@Test
	public void testAddBids_Success_SecondSetOfBids() throws Exception {
		// arrange
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		dataService.createDraftRound(draftRound);
		
		dataService.addBids(draftRound, Arrays.asList(bidPlayer1));
		
		final DraftRoundKey draftRoundKey = new DraftRoundKey(DraftRoundPhase.AUCTION, 1, league.getId());
		DraftRoundEntity retrievedObject = entityManager.find(DraftRoundEntity.class, draftRoundKey);
		assertThat(retrievedObject.getBids()).hasSize(1);
		
		// act
		dataService.addBids(draftRound, Arrays.asList(bidPlayer2));
		
		// assert
		retrievedObject = entityManager.find(DraftRoundEntity.class, draftRoundKey);
		assertThat(retrievedObject.getBids()).hasSize(2);
	}
	
	@Test
	public void testGetOpenDraftRound_Success() throws Exception {
		// arrange
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		dataService.createDraftRound(draftRound);
		
		// act
		final DraftRoundEntity openDraftRound = dataService.getOpenDraftRound(league.getId());
		
		// assert
		assertThat(openDraftRound).isNotNull();
	}
	
	@Test
	public void testGetOpenDraftRound_SuccessWithClosedPhase() throws Exception {
		// arrange
		final DraftRoundEntity draftRound1 = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		draftRound1.setStatus(DraftRoundStatus.CLOSED);
		dataService.createDraftRound(draftRound1);
		
		final DraftRoundEntity draftRound2 = new DraftRoundEntity(DraftRoundPhase.AUCTION, 2, league);
		dataService.createDraftRound(draftRound2);
		
		// act
		final DraftRoundEntity openDraftRound = dataService.getOpenDraftRound(league.getId());
		
		// assert
		assertThat(openDraftRound).isNotNull();
		assertThat(openDraftRound.getKey().getSequenceNumber()).isEqualTo(2);
	}
	
	@Test
	public void testGetOpenDraftRound_NoLeagueExists() throws Exception {
		// arrange
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_DOES_NOT_EXIST_FOR_LEAGUE));
		
		// act
		dataService.getOpenDraftRound(league.getId());
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetOpenDraftRound_NoOpenDraftRoundForLeague() throws Exception {
		// arrange
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league);
		draftRound.setStatus(DraftRoundStatus.CLOSED);
		dataService.createDraftRound(draftRound);
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_DOES_NOT_EXIST_FOR_LEAGUE));
		
		// act
		dataService.getOpenDraftRound(league.getId());
		
		// assert
		Assert.fail("Exception expected");
	}
}
