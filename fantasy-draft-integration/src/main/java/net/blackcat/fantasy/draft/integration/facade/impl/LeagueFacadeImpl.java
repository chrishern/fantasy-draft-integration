/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.entity.GameweekScoreEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.LeagueFacade;
import net.blackcat.fantasy.draft.league.League;
import net.blackcat.fantasy.draft.team.LeagueTableTeam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of league based operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "leagueFacade")
public class LeagueFacadeImpl implements LeagueFacade {

	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Autowired
	@Qualifier(value = "gameweekDataServiceJpa")
	private GameweekDataService gameweekDataService;
	
	@Override
	public void createLeague(final String leagueName) {
		leagueDataService.createLeague(leagueName);
	}

	@Override
	public League getLeagueTable(final int leagueId) throws FantasyDraftIntegrationException {
		final LeagueEntity leagueEntity = leagueDataService.getLeague(leagueId);
		final int previousGameweek = gameweekDataService.getGameweekData().getPreviousGameweek();
		
		final League modelLeague = new League(leagueEntity.getName());

		final List<LeagueTableTeam> modelTeams = new ArrayList<LeagueTableTeam>();
		
		for (final TeamEntity entityTeam : leagueEntity.getTeams()) {
			final LeagueTableTeam modelTeam = new LeagueTableTeam(entityTeam.getName());
			
			modelTeam.setId(entityTeam.getId());
			modelTeam.setTotalScore(entityTeam.getTotalScore());
			modelTeam.setWeekScore(getPreviousGameweekScore(entityTeam, previousGameweek));
			
			modelTeams.add(modelTeam);
		}
		
		Collections.sort(modelTeams);
		
		modelLeague.setTeams(modelTeams);
		
		return modelLeague;
	}

	/**
	 * Get the previous gameweek score for a given team.
	 * 
	 * @param team Team to get the previous score for.
	 * @param currentGameweek Previous gameweek number.
	 * @return Score achieved in the previous gameweek by the given team.
	 */
	private int getPreviousGameweekScore(final TeamEntity team, final int currentGameweek) {
		int score = 0;
		
		for (final GameweekScoreEntity gameweekScore : team.getGameweekScores()) {
			if (gameweekScore.getGameweek() == currentGameweek) {
				score = gameweekScore.getScore();
				break;
			}
		}
			
		return score;
	}
}
