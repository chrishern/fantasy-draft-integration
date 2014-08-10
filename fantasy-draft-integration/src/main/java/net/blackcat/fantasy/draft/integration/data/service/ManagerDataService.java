/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.entity.ManagerEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;

/**
 * Data service for obtaining manager data.
 * 
 * @author Chris
 *
 */
public interface ManagerDataService {

	/**
	 * Get the {@link ManagerEntity} related to the given email address.
	 * 
	 * @param emailAddress Email address of the manager we are interested.
	 * @return {@link ManagerEntity} related to the given email address.
	 * @throws FantasyDraftIntegrationException If a manager with the given email address doesn't exist.
	 */
	ManagerEntity getManager(String emailAddress) throws FantasyDraftIntegrationException;
}
