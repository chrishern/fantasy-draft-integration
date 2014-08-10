/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.player.types.Position;

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

	@Override
	public List<PlayerEntity> getPlayers(final Position position) {
		final TypedQuery<PlayerEntity> query = entityManager.createQuery("select tc from PlayerEntity tc WHERE tc.position = :position", PlayerEntity.class);
		
		query.setParameter("position", position);
		
		return query.getResultList();
	}

	@Override
	public PlayerEntity getPlayer(final int id) throws FantasyDraftIntegrationException {
		final PlayerEntity entity = entityManager.find(PlayerEntity.class, id);
		
		if (entity == null) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.PLAYER_DOES_NOT_EXIST);
		}
		
		return entity;
	}
}
