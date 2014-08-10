/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import static org.fest.assertions.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.entity.ManagerEntity;
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
 * Unit tests for {@link ManagerDataServiceJpa}.
 * 
 * @author Chris
 *
 */
@Transactional		
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"/hsqlDatasourceContext.xml", "/testApplicationContext.xml"})
public class ManagerDataServiceJpaTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "managerDataServiceJpa")
	private ManagerDataServiceJpa dataService;
	
	@Test
	public void testGetManager_Success() throws Exception {
		// arrange
		final TeamEntity team = new TeamEntity(TestDataUtil.TEST_TEAM_1);
		final ManagerEntity manager = TestDataUtil.createManager(team);
		entityManager.persist(manager);
		
		// act
		final ManagerEntity retrievedManager = dataService.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		
		// assert
		assertThat(retrievedManager.getEmailAddress()).isEqualTo(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		assertThat(retrievedManager.getForename()).isEqualTo(TestDataUtil.MANAGER_FORENAME);
		assertThat(retrievedManager.getSurname()).isEqualTo(TestDataUtil.MANAGER_SURNAME);
	}

	@Test
	public void testGetManager_NotFound() throws Exception {
		// arrange
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MANAGER_DOES_NOT_EXIST));
		
		// act
		dataService.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		
		// assert
		Assert.fail("Exception expected");
	}
}
