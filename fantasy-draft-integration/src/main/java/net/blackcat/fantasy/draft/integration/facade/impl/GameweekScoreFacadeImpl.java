/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.GameweekScoreEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.facade.GameweekScoreFacade;
import net.blackcat.fantasy.draft.integration.util.TeamSelectionUtils;
import net.blackcat.fantasy.draft.player.GameweekScorePlayer;
import net.blackcat.fantasy.draft.player.types.Position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of gameweek score operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "gameweekScoreFacade")
public class GameweekScoreFacadeImpl implements GameweekScoreFacade {

	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Autowired
	@Qualifier(value = "teamDataServiceJpa")
	private TeamDataService teamDataService;
	
	@Override
	public void storeGameweekScores(final int gameweek, final Map<Integer, GameweekScorePlayer> gameweekScores) {
		
		for (final LeagueEntity league : leagueDataService.getLeagues()) {
			for (final TeamEntity team : league.getTeams()) {
				
				System.out.println("**** Starting processing of scores for " + team.getName() + " ****");
				
				final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek = new HashMap<Position, List<SelectedPlayerEntity>>();
				
				int numberOfStartingPlayersForWeek = TeamSelectionUtils.buildTeamFromPickedPlayersForWeek(gameweekScores, team, startingTeamForWeek);
				
				final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
				
				TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeamForWeek, numberOfStartingPlayersForWeek, selectedPlayers, gameweekScores);
				
				final int teamWeekScore = TeamSelectionUtils.calculateTeamGameweekScore(startingTeamForWeek, gameweek, gameweekScores);
				
				final GameweekScoreEntity teamWeekScoreEntity = new GameweekScoreEntity(gameweek, teamWeekScore);
				team.addGameweekScore(teamWeekScoreEntity);
				team.setTotalScore(team.getTotalScore() + teamWeekScore);
				
				teamDataService.updateTeam(team);
				
				System.out.println("**** End processing of scores for " + team.getName() + ", total score: " + teamWeekScore  + " ****");
			}
		}
	}
}
