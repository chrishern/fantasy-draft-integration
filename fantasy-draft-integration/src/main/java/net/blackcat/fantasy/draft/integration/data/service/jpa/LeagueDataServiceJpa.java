/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

	@Override
	public LeagueEntity getLeagueForTeam(final int teamId) throws FantasyDraftIntegrationException {
		final TypedQuery<LeagueEntity> query = entityManager.createQuery("select l from LeagueEntity l inner join l.teams team where team.id = :teamId", LeagueEntity.class);
		query.setParameter("teamId", teamId);
		
		try {
			return query.getSingleResult();
		} catch (final NoResultException e) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST);
		}
	}

	@Override
	public List<LeagueEntity> getLeagues() {
		final TypedQuery<LeagueEntity> query = entityManager.createQuery("select l from LeagueEntity l", LeagueEntity.class);
		
		return query.getResultList();
	}

}
