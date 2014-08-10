/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.team.Team;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link TeamFacadeImpl}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TeamFacadeImplTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@Mock
	private TeamDataService teamDataService;
	
	@InjectMocks
	private TeamFacadeImpl teamFacade = new TeamFacadeImpl();
	
	@Test
	public void testCreateTeam() {
		// arrange
		
		// act
		teamFacade.createTeam(TestDataUtil.TEST_TEAM_1);
		
		// assert
		verify(teamDataService).createTeam(TestDataUtil.TEST_TEAM_1);
	}
	
	@Test
	public void testGetTeam_Success() throws Exception {
		// arrange
		final TeamEntity teamEntity = new TeamEntity(TestDataUtil.TEST_TEAM_1);
		final PlayerEntity playerEntity = TestDataUtil.createEntityPlayer(1);
		final SelectedPlayerEntity selectedPlayer = new SelectedPlayerEntity(playerEntity);
		selectedPlayer.setPointsScored(15);
		teamEntity.addSelectedPlayers(Arrays.asList(selectedPlayer));
		
		when(teamDataService.getTeam(TestDataUtil.TEST_TEAM_1)).thenReturn(teamEntity);
		
		// act
		final Team team = teamFacade.getTeam(TestDataUtil.TEST_TEAM_1);
		
		// assert
		assertThat(team.getTeamName()).isEqualTo(TestDataUtil.TEST_TEAM_1);
		assertThat(team.getSelectedPlayers()).hasSize(1);
		assertThat(team.getSelectedPlayers().get(0).getForename()).isEqualTo(playerEntity.getForename());
		assertThat(team.getSelectedPlayers().get(0).getSurname()).isEqualTo(playerEntity.getSurname());
		assertThat(team.getSelectedPlayers().get(0).getPointsScored()).isEqualTo(15);
	}

	@Test
	public void testGetTeam_TeamNotFound() throws Exception {
		// arrange
		when(teamDataService.getTeam(TestDataUtil.TEST_TEAM_1)).thenThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST));
		
		// act
		teamFacade.getTeam(TestDataUtil.TEST_TEAM_1);
		
		// assert
		Assert.fail("Exception expected.");
	}
}
