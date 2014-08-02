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

import org.junit.Test;
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

	private static final String LEAGUE_NAME = "New League";

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataServiceJpa leagueDataServiceJpa;

	@Test
	public void testCreateLeague() {
		// arrange
		
		// act
		leagueDataServiceJpa.createLeague(LEAGUE_NAME);
		
		// assert
		final List<LeagueEntity> leagues = entityManager.createQuery("SELECT l FROM LeagueEntity l", LeagueEntity.class).getResultList();
		
		assertThat(leagues).hasSize(1);
		assertThat(leagues.get(0).getName()).isEqualTo(LEAGUE_NAME);
	}

}
