/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;

import org.springframework.stereotype.Component;

/**
 * JPA implementation of the {@link LeagueDataService}.
 * 
 * @author Chris
 *
 */
@Component(value = "leagueDataServiceJpa")
public class LeagueDataServiceJpa implements LeagueDataService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void createLeague(final String leagueName) {
		final LeagueEntity league = new LeagueEntity(leagueName);

		entityManager.persist(league);
	}

}
