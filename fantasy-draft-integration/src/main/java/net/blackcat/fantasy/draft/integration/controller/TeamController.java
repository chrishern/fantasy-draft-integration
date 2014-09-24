package net.blackcat.fantasy.draft.integration.controller;

import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.TeamFacade;
import net.blackcat.fantasy.draft.player.PopulateInitialFplCostPlayer;
import net.blackcat.fantasy.draft.team.Team;
import net.blackcat.fantasy.draft.team.TeamSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Controller class for invoking {@link Team} based operations.  
 * 
 * This is the entry point for all external applications who wish to access/process {@link Team} data.
 * 
 * @author Chris
 *
 */
@Controller(value = "teamIntegrationController")
public class TeamController {

	@Autowired
	@Qualifier(value = "teamFacade")
	private TeamFacade teamFacade;
	
	/**
	 * Create a new team within the back end.
	 * 
	 * @param teamName Name of the team to create.
	 */
	public void createTeam(final String teamName) {
		teamFacade.createTeam(teamName);
	}
	
	/**
	 * Get a team based on the name of the team.
	 * 
	 * @param teamName Name of the team to find
	 * @return {@link Team} with the given name. 
	 * @throws FantasyDraftIntegrationException If a team with the given name does not exist.
	 */
	public Team getTeam(final String teamName) throws FantasyDraftIntegrationException {
		return teamFacade.getTeam(teamName);
	}
	
	/**
	 * Get the list of completed (i.e. fully picked) teams for a given league.
	 * 
	 * @param leagueId
	 * @return List of {@link Team} objects in the given league.
	 * @throws FantasyDraftIntegrationException If the league IF does not exist.
	 */
	public List<Team> getCompleteTeams(int leagueId) throws FantasyDraftIntegrationException {
		return teamFacade.getCompleteTeams(leagueId);
	}
	
	/**
	 * Get the summary of a team.  This summary includes the total points for the team and the
	 * squad (in selection order) with the total points for each player.
	 * 
	 * @param teamId ID of the team to get the summary for.
	 * @return Summary of the desired team.
	 * @throws FantasyDraftIntegrationException If a team with the given ID is not found.
	 */
	public TeamSummary getTeamSummary(int teamId) throws FantasyDraftIntegrationException {
		return teamFacade.getTeamSummary(teamId);
	}
	
	/**
	 * Get the team summaries for a specific league.  This summary includes the total points for 
	 * the team and the squad (in selection order) with the total points for each player.
	 * 
	 * @param leagueId ID of the league to get the team summaries for.
	 * @return Team summaries from the desired league.
	 * @throws FantasyDraftIntegrationException If a league with the given ID is not found.
	 */
	public List<TeamSummary> getTeamSummaries(int leagueId) throws FantasyDraftIntegrationException {
		return teamFacade.getTeamSummaries(leagueId);
	}
	
	public void updateSelectedPlayersWithIntialFplCost(final Map<Integer, PopulateInitialFplCostPlayer> initialPlayerCosts) {
		teamFacade.updateSelectedPlayersWithInitialFplCost(initialPlayerCosts);
	}
}
