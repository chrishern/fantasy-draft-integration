/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;

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

	@Override
	public LeagueEntity getLeague(final int leagueId) throws FantasyDraftIntegrationException {
		final LeagueEntity league = entityManager.find(LeagueEntity.class, leagueId);
		
		if (league == null) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST);
		}
		
		return league;
	}

}
