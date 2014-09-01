package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.LoggedInUser;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.manager.Manager;

/**
 * Operations for performing functionality on managers.
 * 
 * @author Chris
 *
 */
public interface ManagerFacade {

	/**
	 * Get a manager based on email address.
	 * 
	 * @param emailAddress Email address of the manager to find.
	 * @return Manager object for the manager with the given email address.
	 * @throws FantasyDraftIntegrationException If a manager with the given email address does not exist.
	 */
	Manager getManager(String emailAddress) throws FantasyDraftIntegrationException;
	
	/**
	 * Get based logged in user information about a manager based on their email address.
	 * 
	 * @param emailAddress
	 * @return Logged in user details.
	 * @throws FantasyDraftIntegrationException If a manager with the given email address does not exist.
	 */
	LoggedInUser getLoggedInUser(String emailAddress) throws FantasyDraftIntegrationException;
}
