/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import java.util.List;

import net.blackcat.fantasy.draft.integration.entity.BidEntity;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;

/**
 * Defines operations which allow operations on draft rounds to be performed.
 * 
 * @author Chris
 *
 */
public interface DraftRoundDataService {

	/**
	 * Create a new draft round for the given league.
	 * 
	 * The draft round will be created with a {@link DraftRoundStatus} of OPEN.
	 * 
	 * @param draftRound The draft round to create.
	 * @throws FantasyDraftIntegrationException If an OPEN draft round already exists for the given league
	 * or the draft round trying to be created already exists for the league.
	 */
	void createDraftRound(DraftRoundEntity draftRound) throws FantasyDraftIntegrationException;
	
	/**
	 * Add a list of bids to a draft round.
	 * 
	 * @param draftRound Draft round to add the bids to.
	 * @param bids List of bids to add.
	 */
	void addBids(DraftRoundEntity draftRound, List<BidEntity> bids);
	
	/**
	 * Get the open draft round for the given league.
	 * 
	 * @param leagueId Id of the league we want to get the open draft round for.
	 * @throws FantasyDraftIntegrationException If no open draft round exists for the given league.
	 */
	DraftRoundEntity getOpenDraftRound(int leagueId) throws FantasyDraftIntegrationException;
}
