/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import net.blackcat.fantasy.draft.integration.data.service.UserDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.User;
import net.blackcat.fantasy.draft.integration.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring data implementation of the {@link UserDataService}.
 * 
 * @author Chris
 *
 */
public class SpringDataUserDataService implements UserDataService {

	private UserRepository userRepository;
	
	@Autowired
	public SpringDataUserDataService(final UserRepository userRepository) {
		
		this.userRepository = userRepository;
	}
	
	@Override
	public User getUser(final String emailAddress) throws FantasyDraftIntegrationException {
		
		final User user = userRepository.findOne(emailAddress);
		
		if (user == null) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.USER_NOT_FOUND);
		}
		
		return user;
	}

	@Override
	public void addUser(final User user) throws FantasyDraftIntegrationException {

		if (userRepository.exists(user.getEmailAddress())) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.USER_ALREADY_EXISTS);
		}
		
		userRepository.save(user);
	}

	@Override
	public void updateUser(final User user) throws FantasyDraftIntegrationException {
		
	}
}
