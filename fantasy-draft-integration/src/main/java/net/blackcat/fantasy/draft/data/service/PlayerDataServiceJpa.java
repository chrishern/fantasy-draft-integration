/**
 * 
 */
package net.blackcat.fantasy.draft.data.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.entity.PlayerEntity;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.util.BeanUtils;

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
	
	public void addPlayers(final List<Player> players) {
		for (final Player domainPlayer : players) {
			final PlayerEntity entityPlayer = new PlayerEntity();
			
			BeanUtils.copyProperties(domainPlayer, entityPlayer);
			
			entityManager.persist(entityPlayer);
		}
	}

	public List<Player> getPlayers() {
		final List<Player> domainPlayers = new ArrayList<Player>();
		
		final List<PlayerEntity> entityPlayers = entityManager.createQuery("select tc from PlayerEntity tc", PlayerEntity.class).getResultList();
		
		for (final PlayerEntity entityPlayer : entityPlayers) {
			final Player domainPlayer = new Player();
			
			BeanUtils.copyProperties(entityPlayer, domainPlayer);
			
			domainPlayers.add(domainPlayer);
		}
		
		return domainPlayers;
	}

}
