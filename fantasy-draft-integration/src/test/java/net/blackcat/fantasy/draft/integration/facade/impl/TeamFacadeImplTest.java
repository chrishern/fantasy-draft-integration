/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.player.types.Position;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStartingElevenStatus;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.team.Team;
import net.blackcat.fantasy.draft.team.TeamSummary;

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
	
	@Mock
	private LeagueDataService leagueDataService;
	
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
	@Ignore
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
	
	@Test
	public void getGetTeamSummary_Success() throws Exception {
		// arrange
		final TeamEntity team = TestDataUtil.createTeamEntitiesWithScore().get(0);
		
		when(teamDataService.getTeam(1)).thenReturn(team);
		
		// act
		final TeamSummary teamSummary = teamFacade.getTeamSummary(1);
		
		// assert
		assertThat(teamSummary.getId()).isEqualTo(0);
		assertThat(teamSummary.getTeamName()).isEqualTo(TestDataUtil.TEST_TEAM_1);
		assertThat(teamSummary.getTotalPoints()).isEqualTo(TestDataUtil.TEST_TEAM_1_SCORE);
		assertThat(teamSummary.getRemainingBudget().doubleValue()).isEqualTo(Double.valueOf(TestDataUtil.TEST_TEAM_1_REMAINING_BUDGET));
		assertThat(teamSummary.getTeam()).hasSize(1);
		assertThat(teamSummary.getTeam().get(0).getPosition()).isEqualTo(Position.DEFENDER);
		assertThat(teamSummary.getTeam().get(0).getSelectionStatus()).isEqualTo(SelectedPlayerStartingElevenStatus.PICKED);
		assertThat(teamSummary.getTeam().get(0).getCurrentSellToPotPrice().doubleValue()).isEqualTo(Double.valueOf(TestDataUtil.SELECTED_PLAYER_COST));
		assertThat(teamSummary.getTeam().get(0).getSquadStatus()).isEqualTo(SelectedPlayerStatus.STILL_SELECTED);
	}
	
	@Test
	public void getGetTeamSummary_TeamNotFound() throws Exception {
		// arrange
		when(teamDataService.getTeam(1)).thenThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST));
		
		// act
		teamFacade.getTeamSummary(1);
		
		// assert
		Assert.fail("Exception expected.");
	}
	
	@Test
	public void getGetTeamSummaries_Success() throws Exception {
		// arrange
		final LeagueEntity league = new LeagueEntity();
		
		league.setTeams(TestDataUtil.createTeamEntitiesWithScore());
		
		when(leagueDataService.getLeague(1)).thenReturn(league);
		
		// act
		final List<TeamSummary> teamSummaries = teamFacade.getTeamSummaries(1);
		
		// assert
		assertThat(teamSummaries).hasSize(3);
		assertThat(teamSummaries.get(0).getTeamName()).isEqualTo(TestDataUtil.TEST_TEAM_1);
		assertThat(teamSummaries.get(1).getTeamName()).isEqualTo(TestDataUtil.TEST_TEAM_2);
		assertThat(teamSummaries.get(2).getTeamName()).isEqualTo(TestDataUtil.TEST_TEAM_3);
	}
	
	@Test
	public void getGetTeamSummaries_LeagueNotFound() throws Exception {
		// arrange
		when(leagueDataService.getLeague(1)).thenThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		// act
		teamFacade.getTeamSummaries(1);
		
		// assert
		Assert.fail("Exception expected.");
	}
}
