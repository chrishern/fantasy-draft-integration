/**
 * 
 */
package net.blackcat.fantasy.draft.data.service.jpa;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.jpa.TeamDataServiceJpa;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.test.util.TestDataUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit tests for {@link TeamDataServiceJpa}.
 * 
 * @author Chris
 *
 */
@Transactional		
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"/hsqlDatasourceContext.xml", "/testApplicationContext.xml"})
public class TeamDataServiceJpaTest {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "teamDataServiceJpa")
	private TeamDataServiceJpa teamDataServiceJpa;

	@Test
	public void testCreateTeam() {
		// arrange
		
		// act
		teamDataServiceJpa.createTeam(TestDataUtil.TEST_TEAM);
		
		// assert
		final List<TeamEntity> teams = entityManager.createQuery("SELECT t FROM TeamEntity t", TeamEntity.class).getResultList();
		
		assertThat(teams).hasSize(1);
		assertThat(teams.get(0).getName()).isEqualTo(TestDataUtil.TEST_TEAM);
	}

}
