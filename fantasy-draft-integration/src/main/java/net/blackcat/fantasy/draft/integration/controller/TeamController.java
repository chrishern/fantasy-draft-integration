package net.blackcat.fantasy.draft.integration.controller;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.TeamFacade;
import net.blackcat.fantasy.draft.team.Team;

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
}
