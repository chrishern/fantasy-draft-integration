/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link GameweekScoreFacadeImpl}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameweekScoreFacadeImplTest {

	@Mock
	private LeagueDataService leagueDataService;
	
	@Mock
	private TeamDataService teamDataService;
	
	@InjectMocks
	private GameweekScoreFacadeImpl gameweekScoreFacade = new GameweekScoreFacadeImpl();
	
	@Captor
	private ArgumentCaptor<TeamEntity> teamCaptor;
	
	@Test
	public void testStoreGameweekScores() {
		// arrange
		final TeamEntity team = new TeamEntity();
		final List<SelectedPlayerEntity> squadList = TestDataUtil.buildSquadList();
		team.addSelectedPlayers(removeNonSubstitutes(squadList));
		
		final LeagueEntity league = new LeagueEntity();
		league.setTeams(Arrays.asList(team));
		
		when(leagueDataService.getLeagues()).thenReturn(Arrays.asList(league));
		
		final int orginalScore = team.getTotalScore();
		
		// act
		gameweekScoreFacade.storeGameweekScores(1, TestDataUtil.buildFullGameweekScores());
		
		// assert
		verify(teamDataService).updateTeam(teamCaptor.capture());
		
		assertThat(teamCaptor.getAllValues()).hasSize(1);
		assertThat(teamCaptor.getValue().getTotalScore()).isGreaterThan(orginalScore);
	}

	private List<SelectedPlayerEntity> removeNonSubstitutes(final List<SelectedPlayerEntity> squadList) {
		final List<SelectedPlayerEntity> squadWithOnlySubs = new ArrayList<SelectedPlayerEntity>();
		
		for (final SelectedPlayerEntity selectedPlayer : squadList) {
			if (SelectedPlayerStatus.SUBSTITUTE_POSITIONS.contains(selectedPlayer.getSelectionStatus())) {
				squadWithOnlySubs.add(selectedPlayer);
			}
 		}
		
		return squadWithOnlySubs;
	}
}
