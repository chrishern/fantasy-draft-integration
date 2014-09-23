/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.TransferWindowFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Controller class for invoking transfer window based operations.  
 * 
 * This is the entry point for all external applications who wish to  access/process transfer window data.
 * 
 * @author Chris
 *
 */
@Controller(value = "transferWindowIntegrationController")
public class TransferWindowController {

	@Autowired
	@Qualifier(value = "transferWindowFacade")
	private TransferWindowFacade transferWindowFacade;
	
	/**
	 * Start the transfer window with the given number for the given league.
	 * 
	 * @param leagueId Id of the league to start the transfer window for.
	 * @param phase The number of the transfer window to start.
	 * @throws FantasyDraftIntegrationException for the following reasons:
	 * 		<ul>
	 * 			<li><b>LEAGUE_DOES_NOT_EXIST</b> - If the given league does not exist.</li>
	 * 			<li><b>OPEN_TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE</b> - If the given league already has an open transfer window.</li>
	 * 			<li><b>TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE</b> - If a transfer window for the given phase number already exists.</li>
	 * 		</ul>
	 */
	public void startTransferWindow(int leagueId, final int phase) throws FantasyDraftIntegrationException {
		transferWindowFacade.startTransferWindow(leagueId, phase);
	}
}
