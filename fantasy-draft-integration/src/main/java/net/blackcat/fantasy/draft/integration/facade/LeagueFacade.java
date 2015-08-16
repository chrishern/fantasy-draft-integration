/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.dto.LeagueTableDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TeamPointsDto;
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
	
	@Autowired
	public LeagueFacade(final TeamDataService teamDataService) {
		this.teamDataService = teamDataService;
	}
	
	/**
	 * Get the league table for the league that a manager belongs to.
	 * 
	 * @param managerEmailAddress Email address of the manager.
	 * @return League table for the manager's league.
	 * @throws FantasyDraftIntegrationException
	 */
	public LeagueTableDto getLeagueTable(final String managerEmailAddress) throws FantasyDraftIntegrationException {
		
		final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);
    	final League league = teamForManager.getLeague();
    	
    	final LeagueTableDto leagueDto = new LeagueTableDto(league.getName());
    	
    	for (final Team team : league.getTeams()) {
    		final TeamPointsDto teamPointsDto = new TeamPointsDto(team.getName(), team.getTotalScore());
    		
    		leagueDto.addTeam(teamPointsDto);
    	}
    	
    	return leagueDto;
	}
}
