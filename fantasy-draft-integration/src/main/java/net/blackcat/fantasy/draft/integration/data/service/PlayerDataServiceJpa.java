/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.player.Player;

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
	
	public void addPlayers(final List<PlayerEntity> players) {
		for (final PlayerEntity player : players) {
			entityManager.persist(player);
		}
	}

	public List<PlayerEntity> getPlayers() {
		final List<Player> domainPlayers = new ArrayList<Player>();
		
		return entityManager.createQuery("select tc from PlayerEntity tc", PlayerEntity.class).getResultList();
	}

}
