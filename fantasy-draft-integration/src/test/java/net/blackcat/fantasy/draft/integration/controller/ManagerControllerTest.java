/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import net.blackcat.fantasy.draft.LoggedInUser;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.facade.ManagerFacade;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.manager.Manager;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link ManagerController}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ManagerControllerTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@Mock
	private ManagerFacade managerFacade;
	
	@InjectMocks
	private ManagerController managerController = new ManagerController();
	
	@Test
	public void testGetManager_Success() throws Exception {
		// arrange
		final Manager manager = new Manager();
		manager.setEmailAddress(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		
		when(managerFacade.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS)).thenReturn(manager);
		
		// act
		final Manager retrievedManager = managerController.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		
		// assert
		assertThat(retrievedManager).isEqualTo(manager);
	}
	
	@Test
	public void testGetManager_ManagerNotFound() throws Exception {
		// arrange
		when(managerFacade.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS)).thenThrow(
				new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.MANAGER_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MANAGER_DOES_NOT_EXIST));
		
		// act
		managerController.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetLoggedInUser_Success() throws Exception {
		// arrange
		final LoggedInUser loggedInUser = new LoggedInUser(1, 2);
		
		when(managerFacade.getLoggedInUser(TestDataUtil.MANAGER_EMAIL_ADDRESS)).thenReturn(loggedInUser);
		
		// act
		final LoggedInUser retrievedLoggedInUser = managerController.getLoggedInUser(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		
		// assert
		assertThat(retrievedLoggedInUser).isEqualTo(loggedInUser);
	}
	
	@Test
	public void testGetLoggedInUser_ManagerNotFound() throws Exception {
		// arrange
		when(managerFacade.getLoggedInUser(TestDataUtil.MANAGER_EMAIL_ADDRESS)).thenThrow(
				new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.MANAGER_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MANAGER_DOES_NOT_EXIST));
		
		// act
		managerController.getLoggedInUser(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		
		// assert
		Assert.fail("Exception expected");
	}
}
