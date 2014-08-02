/**
 * 
 */
package net.blackcat.fantasy.draft.data.service.jpa;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.bid.Bid;
import net.blackcat.fantasy.draft.integration.data.service.jpa.DraftRoundDataServiceJpa;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.player.types.Position;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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
@Ignore
@Transactional		
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"/hsqlDatasourceContext.xml", "/testApplicationContext.xml"})
public class DraftRoundDataServiceJpaTest {

	private static final String LEAGUE_NAME = "League Name";
	private static final String PLAYER_1 = "Player1";
	private static final String PLAYER_2 = "Player2";

	private static final String TEAM_1 = "Team1";

	private static final int PLAYER_1_ID = 1;
	private static final int PLAYER_2_ID = 2;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "draftRoundDataServiceJpa")
	private DraftRoundDataServiceJpa dataService;
	private LeagueEntity league;
	
	@Before
	public void setup() {
		setupPlayerData();
		setupTeamData();
		setupLeagueData();
		setupDraftRoundData();
	}


	@Test
	public void testAddBids() {
		// arrange
		final Bid bid = new Bid(PLAYER_1_ID, 1, new BigDecimal("35"));
		
		// act
		dataService.addBids(1, Arrays.asList(bid));
		
		// assert
	}

	private void setupTeamData() {
		final TeamEntity team1 = new TeamEntity(TEAM_1);
		
		entityManager.persist(team1);
	}
	
	private void setupPlayerData() {
		final PlayerEntity player1 = new PlayerEntity(PLAYER_1_ID, PLAYER_1, PLAYER_1, TEAM_1, Position.DEFENDER, false, 0);
		final PlayerEntity player2 = new PlayerEntity(PLAYER_2_ID, PLAYER_2, PLAYER_2, TEAM_1, Position.DEFENDER, false, 0);
		
		entityManager.persist(player1);
		entityManager.persist(player2);
	}
	
	private void setupDraftRoundData() {
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, 1, league, DraftRoundStatus.OPEN);
		entityManager.persist(draftRound);
	}
	
	private void setupLeagueData() {
		league = new LeagueEntity(LEAGUE_NAME);
		entityManager.persist(league);
	}
}
