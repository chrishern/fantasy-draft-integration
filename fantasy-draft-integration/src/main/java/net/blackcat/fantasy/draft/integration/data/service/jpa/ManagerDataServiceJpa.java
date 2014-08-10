/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.ManagerDataService;
import net.blackcat.fantasy.draft.integration.entity.ManagerEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;

import org.springframework.stereotype.Component;

/**
 * JPA implementation of the manager data service.
 * 
 * @author Chris
 *
 */
@Component(value = "managerDataServiceJpa")
public class ManagerDataServiceJpa implements ManagerDataService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public ManagerEntity getManager(final String emailAddress) throws FantasyDraftIntegrationException {
		final ManagerEntity entity = entityManager.find(ManagerEntity.class, emailAddress);
		
		if (entity == null) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.MANAGER_DOES_NOT_EXIST);
		}
		
		return entity;
	}

}
