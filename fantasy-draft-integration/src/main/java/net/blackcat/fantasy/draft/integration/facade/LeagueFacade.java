/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.dto.LeagueTableDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TeamPointsDto;
import net.blackcat.fantasy.draft.integration.model.Gameweek;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade for league operations.
 * 
 * @author Chris
 *
 */
@Component
@Transactional
public class LeagueFacade {

	private TeamDataService teamDataService;
	private GameweekDataService gameweekDataService;
	
	@Autowired
	public LeagueFacade(final TeamDataService teamDataService, final GameweekDataService gameweekDataService) {
		this.teamDataService = teamDataService;
		this.gameweekDataService = gameweekDataService;
	}
	
	/**
	 * Get the league table for the league that a manager belongs to.
	 * 
	 * @param managerEmailAddress Email address of the manager.
	 * @return League table for the manager's league.
	 * @throws FantasyDraftIntegrationException
	 */
	public LeagueTableDto getLeagueTable(final String managerEmailAddress) throws FantasyDraftIntegrationException {
		
		final Gameweek gameweek = gameweekDataService.getGameweek();
		final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);
		
		final int previousGameweek = gameweek.getPreviousGameweek();
    	final League league = teamForManager.getLeague();
    	
    	final LeagueTableDto leagueDto = new LeagueTableDto(league.getName());
    	
    	for (final Team team : league.getTeams()) {
    		final int weekPoints = gameweekDataService.getGameweekScoreForTeam(team.getId(), previousGameweek);
    		
    		final TeamPointsDto teamPointsDto = new TeamPointsDto(team.getName(), weekPoints, team.getTotalScore());
    		
    		leagueDto.addTeam(teamPointsDto);
    	}
    	
    	return leagueDto;
	}
}
