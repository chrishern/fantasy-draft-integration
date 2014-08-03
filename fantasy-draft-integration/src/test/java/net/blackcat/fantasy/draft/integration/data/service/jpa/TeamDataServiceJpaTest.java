/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.jpa.TeamDataServiceJpa;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;

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
 * Unit tests for {@link TeamDataServiceJpa}.
 * 
 * @author Chris
 *
 */
@Transactional		
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"/hsqlDatasourceContext.xml", "/testApplicationContext.xml"})
public class TeamDataServiceJpaTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "teamDataServiceJpa")
	private TeamDataServiceJpa teamDataServiceJpa;

	@Test
	public void testCreateTeam() {
		// arrange
		
		// act
		teamDataServiceJpa.createTeam(TestDataUtil.TEST_TEAM_1);
		
		// assert
		final List<TeamEntity> teams = entityManager.createQuery("SELECT t FROM TeamEntity t", TeamEntity.class).getResultList();
		
		assertThat(teams).hasSize(1);
		assertThat(teams.get(0).getName()).isEqualTo(TestDataUtil.TEST_TEAM_1);
	}
	
	@Test
	public void testGetTeam_Success() throws Exception {
		// arrange
		teamDataServiceJpa.createTeam(TestDataUtil.TEST_TEAM_1);
		teamDataServiceJpa.createTeam(TestDataUtil.TEST_TEAM_2);
		
		// act
		final TeamEntity team = teamDataServiceJpa.getTeam(TestDataUtil.TEST_TEAM_1);
		
		// assert
		assertThat(team.getName()).isEqualTo(TestDataUtil.TEST_TEAM_1);
	}
	
	@Test
	public void testGetTeam_TeamNotFound() throws Exception {
		// arrange
		teamDataServiceJpa.createTeam(TestDataUtil.TEST_TEAM_1);
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST));
		
		// act
		teamDataServiceJpa.getTeam(TestDataUtil.TEST_TEAM_3);
		
		// assert
		Assert.fail("Exception expected.");
	}
	
	@Test
	public void testUpdateTeam() throws Exception {
		// arrange
		teamDataServiceJpa.createTeam(TestDataUtil.TEST_TEAM_1);
		
		final PlayerEntity player = new PlayerEntity();
		player.setId(1);
		entityManager.persist(player);
		
		// act
		final TeamEntity team = teamDataServiceJpa.getTeam(TestDataUtil.TEST_TEAM_1);
		
		final SelectedPlayerEntity selectedPlayer = new SelectedPlayerEntity(player);
		team.addSelectedPlayers(Arrays.asList(selectedPlayer));
		
		teamDataServiceJpa.updateTeam(team);
		
		// assert
		final TeamEntity retrievedTeam = teamDataServiceJpa.getTeam(TestDataUtil.TEST_TEAM_1);
		assertThat(retrievedTeam.getName()).isEqualTo(TestDataUtil.TEST_TEAM_1);
		assertThat(retrievedTeam.getSelectedPlayers()).hasSize(1);
	}

}
