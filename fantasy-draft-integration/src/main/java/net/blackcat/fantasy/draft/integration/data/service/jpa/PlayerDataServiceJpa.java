/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;

import org.springframework.stereotype.Component;

/**
 * JPA implementation of the {@link PlayerDataService}.
 * 
 * @author Chris
 *
 */
@Component(value = "playerDataServiceJpa")
public class PlayerDataServiceJpa implements PlayerDataService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void addPlayers(final List<PlayerEntity> players) {
		for (final PlayerEntity player : players) {
			entityManager.persist(player);
		}
	}

	@Override
	public List<PlayerEntity> getPlayers() {
		return entityManager.createQuery("select tc from PlayerEntity tc", PlayerEntity.class).getResultList();
	}

}
