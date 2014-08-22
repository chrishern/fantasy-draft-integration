/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.LeagueFacade;
import net.blackcat.fantasy.draft.league.League;
import net.blackcat.fantasy.draft.team.Team;

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
	
	@Override
	public void createLeague(final String leagueName) {
		leagueDataService.createLeague(leagueName);
	}

	@Override
	public League getLeagueTable(final int leagueId) throws FantasyDraftIntegrationException {
		final LeagueEntity leagueEntity = leagueDataService.getLeague(leagueId);
		
		final League modelLeague = new League(leagueEntity.getName());

		final List<Team> modelTeams = new ArrayList<Team>();
		
		for (final TeamEntity entityTeam : leagueEntity.getTeams()) {
			final Team modelTeam = new Team(entityTeam.getName());
			modelTeam.setId(entityTeam.getId());
			modelTeam.setTotalScore(entityTeam.getTotalScore());
			modelTeams.add(modelTeam);
		}
		
		Collections.sort(modelTeams);
		
		modelLeague.setTeams(modelTeams);
		
		return modelLeague;
	}

}
