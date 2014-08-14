/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import net.blackcat.fantasy.draft.integration.data.service.ManagerDataService;
import net.blackcat.fantasy.draft.integration.entity.ManagerEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.manager.Manager;
import net.blackcat.fantasy.draft.team.types.TeamStatus;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link ManagerFacadeImpl}.
 * 
 * @author Chris
 *
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ManagerFacadeImplTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@Mock
	private ManagerDataService managerDataService;
	
	@InjectMocks
	private ManagerFacadeImpl managerFacade = new ManagerFacadeImpl();
	
	@Test
	public void testGetManager_Success() throws Exception {
		// arrange
		final PlayerEntity player1 = TestDataUtil.createEntityPlayer(1);
		final SelectedPlayerEntity selectedPlayer = new SelectedPlayerEntity(player1);
		final TeamEntity team = new TeamEntity(TestDataUtil.TEST_TEAM_1);
		team.addSelectedPlayers(Arrays.asList(selectedPlayer));
		team.setStatus(TeamStatus.COMPLETE);
		final ManagerEntity managerEntity = TestDataUtil.createManager(team);
		
		when(managerDataService.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS)).thenReturn(managerEntity);
		
		// act
		final Manager manager = managerFacade.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		
		// assert
		assertThat(manager.getEmailAddress()).isEqualTo(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		assertThat(manager.getForename()).isEqualTo(TestDataUtil.MANAGER_FORENAME);
		assertThat(manager.getSurname()).isEqualTo(TestDataUtil.MANAGER_SURNAME);
		assertThat(manager.getTeam().getTeamName()).isEqualTo(TestDataUtil.TEST_TEAM_1);
		assertThat(manager.getTeam().getSelectedPlayers()).hasSize(1);
		assertThat(manager.getTeam().getSelectedPlayers().get(0).getForename()).isEqualTo(TestDataUtil.PLAYER_1_FORENAME);
		assertThat(manager.getTeam().getSelectedPlayers().get(0).getSurname()).isEqualTo(TestDataUtil.PLAYER_1_SURNAME);
		assertThat(manager.getTeam().getStatus()).isEqualTo(TeamStatus.COMPLETE);
	}

	@Test
	public void testGetManager_ManagerNotFound() throws Exception {
		// arrange
		when(managerDataService.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS)).thenThrow(
				new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.MANAGER_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MANAGER_DOES_NOT_EXIST));
		
		// act
		managerFacade.getManager(TestDataUtil.MANAGER_EMAIL_ADDRESS);
		
		// assert
		Assert.fail("Exception expected");
	}
}
