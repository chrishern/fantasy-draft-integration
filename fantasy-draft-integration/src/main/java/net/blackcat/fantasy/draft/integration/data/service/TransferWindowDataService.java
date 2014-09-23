package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.entity.TransferWindowEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;

/**
 * Defines data operations performed on a transfer window.
 * 
 * @author Chris
 *
 */
public interface TransferWindowDataService {

	/**
	 * Create a given transfer window.
	 * 
	 * The draft round will be created with a {@link DraftRoundStatus} of OPEN.
	 * 
	 * @param transferWindow The transfer window to create.
	 * @throws FantasyDraftIntegrationException If an OPEN transfer window already exists for the given league
	 * or the transfer window trying to be created already exists for the league.
	 */
	void createTransferWindow(TransferWindowEntity transferWindow) throws FantasyDraftIntegrationException;
}
