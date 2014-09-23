/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TransferWindowDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferWindowEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.TransferWindowFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of transfer window operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "transferWindowFacade")
public class TransferWindowFacadeImpl implements TransferWindowFacade {

	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Autowired
	@Qualifier(value = "transferWindowDataServiceJpa")
	private TransferWindowDataService transferWindowDataService;
	
	@Override
	public void startTransferWindow(final int leagueId, final int phase) throws FantasyDraftIntegrationException {
		final LeagueEntity league = leagueDataService.getLeague(leagueId);
		
		final TransferWindowEntity draftRound = new TransferWindowEntity(phase, league);
		transferWindowDataService.createTransferWindow(draftRound);

	}

}
