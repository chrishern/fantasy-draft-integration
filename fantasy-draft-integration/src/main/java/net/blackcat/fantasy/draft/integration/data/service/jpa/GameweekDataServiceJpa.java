/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.entity.GameweekEntity;

import org.springframework.stereotype.Component;

/**
 * JPA implementation of the {@link GameweekDataService}.
 * 
 * @author Chris
 *
 */
@Component(value = "gameweekDataServiceJpa")
public class GameweekDataServiceJpa implements GameweekDataService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public GameweekEntity getGameweekData() {
		final TypedQuery<GameweekEntity> query = entityManager.createQuery("select g from GameweekEntity g", GameweekEntity.class);
		
		return query.getSingleResult();
	}

	@Override
	public void updateGameweekData(final GameweekEntity gameweekData) {
		entityManager.merge(gameweekData);
	}

}
