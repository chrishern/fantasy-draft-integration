/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.blackcat.fantasy.draft.integration.data.service.TransferWindowDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferEntity;
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

	@Override
	public TransferWindowEntity getOpenTransferWindow(final int leagueId) throws FantasyDraftIntegrationException {
		return getTransferWindow(leagueId, DraftRoundStatus.OPEN);
	}

	@Override
	public void updateTransferWindow(final TransferWindowEntity transferWindow) {
		entityManager.merge(transferWindow);
	}
	
	@Override
	public List<TransferEntity> getTransfers(final TransferWindowEntity transferWindow, final int teamId) {
		final List<TransferEntity> teamTransfers = new ArrayList<TransferEntity>();
		
		for (final TransferEntity transfer : transferWindow.getTransfers()) {
			if (teamIsBuyingTeam(teamId, transfer) || teamIsSellingTeam(teamId, transfer)) {
				teamTransfers.add(transfer);
			}
		}
		
		return teamTransfers;
	}

	/**
	 * @param teamId
	 * @param transfer
	 * @return
	 */
	private boolean teamIsSellingTeam(final int teamId, final TransferEntity transfer) {
		return transfer.getSellingTeam().getId() == teamId;
	}

	/**
	 * @param teamId
	 * @param transfer
	 * @return
	 */
	private boolean teamIsBuyingTeam(final int teamId, final TransferEntity transfer) {
		return transfer.getBuyingTeam() != null && transfer.getBuyingTeam().getId() == teamId;
	}
	
	private TransferWindowEntity getTransferWindow(final int leagueId, final DraftRoundStatus status) throws FantasyDraftIntegrationException {
		final TypedQuery<TransferWindowEntity> query = entityManager.createQuery(
				"SELECT d FROM TransferWindowEntity d WHERE d.key.leagueId = :leagueId AND d.status = :status", TransferWindowEntity.class);
		query.setParameter("leagueId", leagueId);
		query.setParameter("status", status);
		
		try {
			return query.getSingleResult();
		} catch (final NoResultException e) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_TRANSFER_DOES_NOT_EXIST_FOR_LEAGUE);
		}
	}
	
	/**
	 * Determine if a given league has an open draft round or not.
	 * 
	 * @param league League to check.
	 * @return True if the league has an open draft round, false otherwise.
	 */
	private boolean doesLeagueHaveOpenTransferWindow(final LeagueEntity league) {
		try {
			getTransferWindow(league.getId(), DraftRoundStatus.OPEN);
			return true;
		} catch (final FantasyDraftIntegrationException e) {
			return false;
		}
	}
}
