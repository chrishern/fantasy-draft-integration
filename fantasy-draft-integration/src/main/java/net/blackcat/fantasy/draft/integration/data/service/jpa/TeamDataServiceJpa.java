/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;

import org.springframework.stereotype.Component;

/**
 * JPA implementation of the {@link TeamDataService}.
 * 
 * @author Chris
 *
 */
@Component(value = "teamDataServiceJpa")
public class TeamDataServiceJpa implements TeamDataService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void createTeam(final String teamName) {
		final TeamEntity team = new TeamEntity(teamName);
		
		entityManager.persist(team);
	}

	@Override
	public void updateTeam(final TeamEntity team) {
		entityManager.merge(team);
	}

	@Override
	public TeamEntity getTeam(final String teamName) throws FantasyDraftIntegrationException {
		final TypedQuery<TeamEntity> query = entityManager.createQuery("SELECT t FROM TeamEntity t WHERE t.name = :name", TeamEntity.class);
		query.setParameter("name", teamName);
		
		try {
			return query.getSingleResult();
		} catch (final NoResultException e) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST);
		}
	}

	@Override
	public TeamEntity getTeam(final int id) throws FantasyDraftIntegrationException {
		final TeamEntity team = entityManager.find(TeamEntity.class, id);
		
		if (team == null) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_DOES_NOT_EXIST);
		}
		
		return team;
	}

}
