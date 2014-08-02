/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.blackcat.fantasy.draft.integration.data.service.DraftRoundDataService;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;

import org.springframework.stereotype.Component;

/**
 * JPA implementation of the {@link DraftRoundDataService}.
 * 
 * @author Chris
 *
 */
@Component(value = "draftRoundDataServiceJpa")
public class DraftRoundDataServiceJpa implements DraftRoundDataService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void createDraftRound(final DraftRoundEntity draftRound) throws FantasyDraftIntegrationException {
		// Check for the presence of open draft rounds for this league
		final TypedQuery<DraftRoundEntity> query = entityManager.createQuery(
				"SELECT d FROM DraftRoundEntity d WHERE d.key.leagueId = :leagueId AND d.status = :status", DraftRoundEntity.class);
		query.setParameter("leagueId", draftRound.getKey().getLeague());
		query.setParameter("status", DraftRoundStatus.OPEN);
		
		if (!query.getResultList().isEmpty()) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE);
		}

		try {
			entityManager.persist(draftRound);
		} catch (final EntityExistsException e) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE);
		}
	}

}
