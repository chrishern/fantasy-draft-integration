package net.blackcat.fantasy.draft.integration.data.service;

import java.util.List;

import net.blackcat.fantasy.draft.integration.entity.TransferEntity;
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
	
	/**
	 * Get the open transfer window for a league.
	 * 
	 * @param leagueId ID of the league to get the open transfer window for.
	 * @return The open {@link TransferWindowEntity}.
	 * @throws FantasyDraftIntegrationException If an open transfer window for the given league does not exist,
	 */
	TransferWindowEntity getOpenTransferWindow(int leagueId) throws FantasyDraftIntegrationException;
	
	/**
	 * Update the given transfer window entity.
	 *
	 * @param transferWindow The {@link TransferWindowEntity} to update.
	 */
	void updateTransferWindow(TransferWindowEntity transferWindow);
	
	/**
	 * Get all transfers that a given team is involved in the given window.
	 * 
	 * @param transferWindow The transfer window to look for transfers in.
	 * @param teamId ID of the team to get the transfers for.
	 * @return List of all transfers the team is involved in.
	 */
	List<TransferEntity> getTransfers(TransferWindowEntity transferWindow, int teamId);
}
