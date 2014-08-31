/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.GameweekEntity;
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
	
	@Autowired
	@Qualifier(value = "gameweekDataServiceJpa")
	private GameweekDataService gameweekDataService;
	
	@Override
	public void storeCurrentGameweekScores(final Map<Integer, GameweekScorePlayer> currentGameweekScores) {
		final GameweekEntity gameweekData = gameweekDataService.getGameweekData();
		final int currentGameweek = gameweekData.getCurrentGameweek();
		
		for (final LeagueEntity league : leagueDataService.getLeagues()) {
			for (final TeamEntity team : league.getTeams()) {
				
				System.out.println("**** Starting processing of scores for " + team.getName() + " ****");
				
				final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek = new HashMap<Position, List<SelectedPlayerEntity>>();
				
				int numberOfStartingPlayersForWeek = TeamSelectionUtils.buildTeamFromPickedPlayersForWeek(currentGameweekScores, team, startingTeamForWeek);
				
				final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
				
				TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeamForWeek, numberOfStartingPlayersForWeek, selectedPlayers, currentGameweekScores);
				
				final int teamWeekScore = TeamSelectionUtils.calculateTeamGameweekScore(startingTeamForWeek, currentGameweek, currentGameweekScores);
				
				final GameweekScoreEntity teamWeekScoreEntity = new GameweekScoreEntity(currentGameweek, teamWeekScore);
				team.addGameweekScore(teamWeekScoreEntity);
				team.setTotalScore(team.getTotalScore() + teamWeekScore);
				
				teamDataService.updateTeam(team);
				
				System.out.println("**** End processing of scores for " + team.getName() + ", total score: " + teamWeekScore  + " ****");
			}
		}
		
		updateGameweek(gameweekData);
	}

	/**
	 * Update the gameweek data to reflect the changing of the week.
	 *  
	 * @param gameweekData Gameweek data in the form of a {@link GameweekEntity}.
	 */
	private void updateGameweek(final GameweekEntity gameweekData) {
		gameweekData.setPreviousGameweek(gameweekData.getCurrentGameweek());
		gameweekData.setCurrentGameweek(gameweekData.getCurrentGameweek() + 1);
		
		gameweekDataService.updateGameweekData(gameweekData);
	}
}
