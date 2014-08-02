/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;

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

}
