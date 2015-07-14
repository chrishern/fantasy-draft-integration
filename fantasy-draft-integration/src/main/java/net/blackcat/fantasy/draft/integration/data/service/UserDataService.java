/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.model.User;

/**
 * Defines operations for accessing User data from the back end data store.
 * 
 * @author Chris
 *
 */
public interface UserDataService {

	/**
	 * Get a {@link User} object based upon their email address.
	 * 
	 * @param emailAddress The email address of the {@link User} to get.
	 * @return Corresponding {@link User} object.
	 * @throws FantasyDraftIntegrationException If a user with the given email address is not found.
	 */
	User getUser(String emailAddress) throws FantasyDraftIntegrationException;
	
	/**
	 * Add a new {@link User} to the back end.
	 * 
	 * @param user {@link User} to add.
	 * @throws FantasyDraftIntegrationException If a {@link User} with the same email address already exists.
	 */
	void addUser(User user) throws FantasyDraftIntegrationException;
}
