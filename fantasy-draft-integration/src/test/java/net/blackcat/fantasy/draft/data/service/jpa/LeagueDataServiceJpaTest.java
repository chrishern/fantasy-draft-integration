/**
 * 
 */
package net.blackcat.fantasy.draft.data.service.jpa;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.jpa.LeagueDataServiceJpa;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.test.util.TestDataUtil;

import org.junit.Assert;
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
 * Unit tests for {@link LeagueDataServiceJpa}.
 * 
 * @author Chris
 *
 */
@Transactional		
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"/hsqlDatasourceContext.xml", "/testApplicationContext.xml"})
public class LeagueDataServiceJpaTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataServiceJpa leagueDataServiceJpa;

	@Test
	public void testCreateLeague() {
		// arrange
		
		// act
		leagueDataServiceJpa.createLeague(TestDataUtil.LEAGUE_NAME);
		
		// assert
		final List<LeagueEntity> leagues = entityManager.createQuery("SELECT l FROM LeagueEntity l", LeagueEntity.class).getResultList();
		
		assertThat(leagues).hasSize(1);
		assertThat(leagues.get(0).getName()).isEqualTo(TestDataUtil.LEAGUE_NAME);
	}

	@Test
	public void testGetLeague_Success() throws Exception {
		// arrange
		final LeagueEntity league = new LeagueEntity(TestDataUtil.LEAGUE_NAME);
		entityManager.persist(league);
		
		// act
		final LeagueEntity retrievedLeague = leagueDataServiceJpa.getLeague(league.getId());
		
		// assert
		assertThat(retrievedLeague).isNotNull();
		assertThat(retrievedLeague.getName()).isEqualTo(TestDataUtil.LEAGUE_NAME);
	}
	
	@Test
	public void testGetLeague_LeagueNotFound() throws Exception {
		// arrange
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		// act
		leagueDataServiceJpa.getLeague(1);
		
		// assert
		Assert.fail("Exception expected");
	}
}
