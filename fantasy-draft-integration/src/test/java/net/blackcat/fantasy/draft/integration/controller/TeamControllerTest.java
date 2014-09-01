/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.facade.TeamFacade;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.team.Team;
import net.blackcat.fantasy.draft.team.TeamSummary;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link TeamController}
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TeamControllerTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	@Mock
	private TeamFacade teamFacade;
	
	@InjectMocks
	private TeamController teamController = new TeamController();
	
	@Test
	public void testCreateTeam() {
		// arrange
		
		// act
		teamController.createTeam(TestDataUtil.TEST_TEAM_1);
		
		// assert
		verify(teamFacade).createTeam(TestDataUtil.TEST_TEAM_1);
	}
	
	@Test
	public void testGetTeam_Success() throws Exception {
		// arrange
		final Team team = new Team(TestDataUtil.TEST_TEAM_1);
		
		when(teamFacade.getTeam(TestDataUtil.TEST_TEAM_1)).thenReturn(team);
		
		// act
		final Team retrievedTeam = teamController.getTeam(TestDataUtil.TEST_TEAM_1);
		
		// assert
		assertThat(retrievedTeam).isEqualTo(team);
	}
	
	@Test
	public void testGetTeam_TeamNotFound() throws Exception {
		// arrange
		when(teamFacade.getTeam(TestDataUtil.TEST_TEAM_1)).thenThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST));
		
		// act
		teamController.getTeam(TestDataUtil.TEST_TEAM_1);
		
		// assert
		Assert.fail("Exception expected.");
	}

	@Test
	public void testGetTeamSummary_Success() throws Exception {
		// arrange
		final TeamSummary team = new TeamSummary();
		
		when(teamFacade.getTeamSummary(1)).thenReturn(team);
		
		// act
		final TeamSummary retrievedTeam = teamController.getTeamSummary(1);
		
		// assert
		assertThat(retrievedTeam).isEqualTo(team);
	}
	
	@Test
	public void testGetTeamSummary_TeamNotFound() throws Exception {
		// arrange
		when(teamFacade.getTeamSummary(1)).thenThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST));
		
		// act
		teamController.getTeamSummary(1);
		
		// assert
		Assert.fail("Exception expected.");
	}
}
