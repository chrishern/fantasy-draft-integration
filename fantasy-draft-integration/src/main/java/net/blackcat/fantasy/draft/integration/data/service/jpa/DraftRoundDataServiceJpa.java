/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.blackcat.fantasy.draft.integration.data.service.DraftRoundDataService;
import net.blackcat.fantasy.draft.integration.entity.BidEntity;
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
		final List<DraftRoundEntity> openRound = getOpenRound(draftRound.getKey().getLeague());
		
		if (!openRound.isEmpty()) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE);
		}

		try {
			entityManager.persist(draftRound);
		} catch (final EntityExistsException e) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE);
		}
	}

	@Override
	public void addBids(final DraftRoundEntity draftRound, final List<BidEntity> bids) {
		draftRound.addBids(bids);
		entityManager.merge(draftRound);
	}

	@Override
	public DraftRoundEntity getOpenDraftRound(final int leagueId) throws FantasyDraftIntegrationException {
		final List<DraftRoundEntity> openRounds = getOpenRound(leagueId);

		if (openRounds.isEmpty()) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_DRAFT_ROUND_DOES_NOT_EXIST_FOR_LEAGUE);
		}
		
		return openRounds.get(0);
	}
	
	/**
	 * Get the open draft rounds for the given league Id.
	 * 
	 * @param leagueId Id of the league to get the open rounds for.
	 * @return List of open draft rounds.
	 */
	private List<DraftRoundEntity> getOpenRound(final int leagueId) {
		final TypedQuery<DraftRoundEntity> query = entityManager.createQuery(
				"SELECT d FROM DraftRoundEntity d WHERE d.key.leagueId = :leagueId AND d.status = :status", DraftRoundEntity.class);
		query.setParameter("leagueId", leagueId);
		query.setParameter("status", DraftRoundStatus.OPEN);
		return query.getResultList();
	}
}
