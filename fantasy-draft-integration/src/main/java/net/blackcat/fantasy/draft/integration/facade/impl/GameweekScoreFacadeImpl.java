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
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;

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
				
				int teamWeekScore = 0;
				
				final boolean isCaptainInTeam = isCaptainInTeam(startingTeamForWeek);
				
				for (final Position position : Position.values()) {
					final List<SelectedPlayerEntity> playersInPosition = startingTeamForWeek.get(position);
					
					for (final SelectedPlayerEntity selectedPlayer : playersInPosition) {
						final GameweekScorePlayer weekScorePlayer = gameweekScores.get(selectedPlayer.getPlayer().getId());
						final int weekPointsScored = calculatePlayerScore(isCaptainInTeam, selectedPlayer, weekScorePlayer.getScore());
						
						System.out.println("Player: " + selectedPlayer.getPlayer().getForename() + " " + selectedPlayer.getPlayer().getSurname() + ", score: " + weekPointsScored);
						
						final GameweekScoreEntity playerWeekScoreEntity = new GameweekScoreEntity(gameweek, weekPointsScored);
						selectedPlayer.addGameweekScore(playerWeekScoreEntity);
						selectedPlayer.setPointsScored(selectedPlayer.getPointsScored() + teamWeekScore);
						
						teamWeekScore += weekPointsScored;
					}
				}
				
				final GameweekScoreEntity teamWeekScoreEntity = new GameweekScoreEntity(gameweek, teamWeekScore);
				team.addGameweekScore(teamWeekScoreEntity);
				team.setTotalScore(team.getTotalScore() + teamWeekScore);
				
				teamDataService.updateTeam(team);
				
				System.out.println("**** End processing of scores for " + team.getName() + ", total score: " + teamWeekScore  + " ****");
			}
		}
	}
	
	/**
	 * Determine if the team captain is in the starting lineup this week.
	 * 
	 * @param startingTeamForWeek The starting lineup for a given team.
	 * @return True if the captain is in the team, false otherwise.
	 */
	private boolean isCaptainInTeam(final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek) {
		for (final Position position : Position.values()) {
			final List<SelectedPlayerEntity> playersInPosition = startingTeamForWeek.get(position);
			
			for (final SelectedPlayerEntity selectedPlayer : playersInPosition) {
				if (selectedPlayer.getSelectionStatus() == SelectedPlayerStatus.CAPTAIN) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Calculate the weekly score for a given player.
	 * 
	 * @param isCaptainInTeam Boolean which indicates whether the team captain is in the team or not.
	 * @param selectedPlayer The player to calculate the score for.
	 * @param baseScore The base score, before any adjustments are taken into account.
	 * @return The weekly score for the player with any adjustments taken into account.
	 */
	private int calculatePlayerScore(final boolean isCaptainInTeam, final SelectedPlayerEntity selectedPlayer, final int baseScore) {
		if (selectedPlayer.getSelectionStatus() == SelectedPlayerStatus.CAPTAIN) {
			return baseScore * 2;
		} else if (selectedPlayer.getSelectionStatus() == SelectedPlayerStatus.CAPTAIN && !isCaptainInTeam) {
			return baseScore * 2;
		}
		
		return baseScore;
	}
}
