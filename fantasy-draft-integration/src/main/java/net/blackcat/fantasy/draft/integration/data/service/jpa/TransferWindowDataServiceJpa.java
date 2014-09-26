/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.blackcat.fantasy.draft.integration.data.service.TransferWindowDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferWindowEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;

import org.springframework.stereotype.Component;

/**
 * JPA implementation of transfer window data operations.
 * 
 * @author Chris
 *
 */
@Component(value = "transferWindowDataServiceJpa")
public class TransferWindowDataServiceJpa implements TransferWindowDataService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void createTransferWindow(final TransferWindowEntity transferWindow) throws FantasyDraftIntegrationException {
		
		if (doesLeagueHaveOpenTransferWindow(transferWindow.getLeague())) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE);
		}

		try {
			entityManager.persist(transferWindow);
		} catch (final EntityExistsException e) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TRANSFER_WINDOW_ALREADY_EXISTS_FOR_LEAGUE);
		}
	}

	/**
	 * Determine if a given league has an open draft round or not.
	 * 
	 * @param league League to check.
	 * @return True if the league has an open draft round, false otherwise.
	 */
	private boolean doesLeagueHaveOpenTransferWindow(final LeagueEntity league) {
		final TypedQuery<TransferWindowEntity> query = entityManager.createQuery(
				"SELECT d FROM TransferWindowEntity d WHERE d.key.leagueId = :leagueId AND d.status = :status", TransferWindowEntity.class);
		query.setParameter("leagueId", league.getId());
		query.setParameter("status", DraftRoundStatus.OPEN);
		
		try {
			query.getSingleResult();
			return true;
		} catch (final NoResultException e) {
			return false;
		}
	}
}