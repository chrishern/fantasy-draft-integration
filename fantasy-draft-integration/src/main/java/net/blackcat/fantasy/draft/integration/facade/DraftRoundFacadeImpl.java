/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.integration.data.service.DraftRoundDataService;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of draft round facade operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "draftRoundFacade")
public class DraftRoundFacadeImpl implements DraftRoundFacade {

	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Autowired
	@Qualifier(value = "draftRoundDataServiceJpa")
	private DraftRoundDataService draftRoundDataService;
	
	@Override
	public void startAuctionPhase(final int leagueId, final int phase) throws FantasyDraftIntegrationException {
		// Get the LeagueEntity for the given id
		final LeagueEntity league = leagueDataService.getLeague(leagueId);
		
		// Create the phase
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, phase, league);
		draftRoundDataService.createDraftRound(draftRound);
	}

}
