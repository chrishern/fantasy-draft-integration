/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import net.blackcat.fantasy.draft.integration.facade.LeagueFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Controller class for invoking league based operations.  
 * 
 * This is the entry point for all external applications who wish to access/process league data.
 * 
 * @author Chris
 *
 */
@Controller(value = "leagueIntegrationController")
public class LeagueController {

	@Autowired
	@Qualifier(value = "leagueFacade")
	private LeagueFacade leagueFacade;
	
	/**
	 * Create a new league.
	 * 
	 * @param leagueName Name of the league to create.
	 */
	public void createLeague(final String leagueName) {
		leagueFacade.createLeague(leagueName);
	}
}
