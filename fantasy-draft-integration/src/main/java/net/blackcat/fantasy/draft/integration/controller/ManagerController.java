package net.blackcat.fantasy.draft.integration.controller;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.ManagerFacade;
import net.blackcat.fantasy.draft.manager.Manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Controller class for invoking {@link Manager} based operations.  
 * 
 * This is the entry point for all external applications who wish to access/process {@link Manager} data.
 * 
 * @author Chris
 *
 */
@Controller(value = "managerIntegrationController")
public class ManagerController {

	@Autowired
	@Qualifier(value = "managerFacade")
	private ManagerFacade managerFacade;
	
	/**
	 * Get a manager based on email address.
	 * 
	 * @param emailAddress Email address of the manager to find.
	 * @return Manager object for the manager with the given email address.
	 * @throws FantasyDraftIntegrationException If a manager with the given email address does not exist.
	 */
	public Manager getManager(final String emailAddress) throws FantasyDraftIntegrationException {
		return managerFacade.getManager(emailAddress);
	}
}
