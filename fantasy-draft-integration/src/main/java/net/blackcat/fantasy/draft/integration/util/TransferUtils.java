/**
 * 
 */
package net.blackcat.fantasy.draft.integration.util;

import java.util.List;

import net.blackcat.fantasy.draft.integration.entity.ExchangedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferredPlayerEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.transfer.Transfer;

/**
 * Utilities to help transfer processing.
 * 
 * @author Chris
 *
 */
public final class TransferUtils {

	private TransferUtils() {}
	
	/**
	 * Get the equivalent transfer for a new transfer from a list of existing transfers.
	 * 
	 * @param newTransfer New transfer to find the match for.
	 * @param existingTransfers List of existing transfers to find the equivalent from.
	 * @return The equivalent existing transfer.
	 * @throws FantasyDraftIntegrationException If an equivalent is not found.
	 */
	public static TransferEntity getEquivalentTransfer(final Transfer newTransfer, final List<TransferEntity> existingTransfers) throws FantasyDraftIntegrationException {
		
		if (existingTransfers != null) {
			for (final TransferEntity existingTransfer : existingTransfers) {
				if (transfersAreEquivalent(newTransfer, existingTransfer)) {
					return existingTransfer;
				}
			}
		}
		
		throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.MATCHING_TRANSFER_NOT_FOUND);
	}

	/**
	 * Determine if a new transfer and an existing transfer are equivalent.
	 * 
	 * @param newTransfer New transfer.
	 * @param existingTransfer Existing transfer.
	 * @return True if the two are equivalent, false otherwise.
	 */
	private static boolean transfersAreEquivalent(final Transfer newTransfer, final TransferEntity existingTransfer) {
		return transferIsBetweenSameTeams(newTransfer, existingTransfer) && transferIsForSameAmount(newTransfer, existingTransfer)
				&& playersMatch(newTransfer, existingTransfer);
	}

	/**
	 * Determine if a new transfer and an existing transfer are between the same teams.
	 * 
	 * @param newTransfer New transfer.
	 * @param existingTransfer Existing transfer.
	 * @return True if the two are between the same teams, false otherwise.
	 */
	private static boolean transferIsBetweenSameTeams(final Transfer newTransfer, final TransferEntity existingTransfer) {
		return newTransfer.getBuyingTeam() == existingTransfer.getBuyingTeam().getId() &&
				newTransfer.getSellingTeam() == existingTransfer.getSellingTeam().getId();
	}
	
	/**
	 * Determine if a new transfer and an existing transfer are for the same amount.
	 * 
	 * @param newTransfer New transfer.
	 * @param existingTransfer Existing transfer.
	 * @return True if the two are for the same amount, false otherwise.
	 */
	private static boolean transferIsForSameAmount(final Transfer newTransfer, final TransferEntity existingTransfer) {
		return newTransfer.getAmount().doubleValue() == existingTransfer.getAmount().doubleValue();
	}
	
	/**
	 * Determine if the players that are part of the transfer match between a new transfer and an existing transfer.
	 * 
	 * @param newTransfer New transfer.
	 * @param existingTransfer Existing transfer.
	 * @return True if the players are equivalent, false otherwise.
	 */
	private static boolean playersMatch(final Transfer newTransfer, final TransferEntity existingTransfer) {
		return mainPlayersMatch(newTransfer, existingTransfer) && exchangedPlayersMatch(newTransfer, existingTransfer);
	}
	
	/**
	 * Determine if the players that the are subject of the transfer match between a new transfer and an existing transfer.
	 * 
	 * @param newTransfer New transfer.
	 * @param existingTransfer Existing transfer.
	 * @return True if the subject players are equivalent, false otherwise.
	 */
	private static boolean mainPlayersMatch(final Transfer newTransfer, final TransferEntity existingTransfer) {
		if (sameAmountOfPlayersInBothTransfers(newTransfer, existingTransfer)) {
			for (final int newPlayerId : newTransfer.getPlayers()) {
				if (!equivalentPlayerExistsInExistingTransfer(newPlayerId, existingTransfer)) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}

	/**
	 * @param newTransfer
	 * @param existingTransfer
	 * @return
	 */
	private static boolean sameAmountOfPlayersInBothTransfers(final Transfer newTransfer, final TransferEntity existingTransfer) {
		if (newTransfer.getPlayers() == null) {
			return existingTransfer.getPlayers() == null;
		}
		
		return newTransfer.getPlayers().size() == existingTransfer.getPlayers().size();
	}

	/**
	 * Check whether a player with a given id exists in the list of players in an existing transfer.
	 * 
	 * @param newPlayerId New player ID.
	 * @param existingTransfer Existing transfer.
	 * @return True if the player existing in the existing transfer, false otherwise.
	 */
	private static boolean equivalentPlayerExistsInExistingTransfer(final int newPlayerId, final TransferEntity existingTransfer) {
		for (final TransferredPlayerEntity existingPlayer : existingTransfer.getPlayers()) {
			if (existingPlayer.getPlayer().getId() == newPlayerId) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Determine if the players that are being exchanged as part of the transfer match between a new transfer and an existing transfer.
	 * 
	 * @param newTransfer New transfer.
	 * @param existingTransfer Existing transfer.
	 * @return True if the exchanged players are equivalent, false otherwise.
	 */
	private static boolean exchangedPlayersMatch(final Transfer newTransfer, final TransferEntity existingTransfer) {
		if (!arePlayersBeingExchanged(newTransfer)) {
			return true;
		}
		
		if (sameNumberOfExchangedPlayersInBothTransfers(newTransfer, existingTransfer)) {
			for (final int newPlayerId : newTransfer.getExchangedPlayers()) {
				if (!equivalentExchangedPlayerExistsInExistingTransfer(newPlayerId, existingTransfer)) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}

	/**
	 * Determine if players are being exchanged as part of the transfer.
	 * 
	 * @param newTransfer The new transfer that has been submitted.
	 * @return True if players are being exchanged as part of the transfer, false otherwise.
	 */
	private static boolean arePlayersBeingExchanged(final Transfer newTransfer) {
		return newTransfer.getExchangedPlayers() != null;
	}
	
	/**
	 * @param newTransfer
	 * @param existingTransfer
	 * @return
	 */
	private static boolean sameNumberOfExchangedPlayersInBothTransfers(final Transfer newTransfer, final TransferEntity existingTransfer) {
		if (newTransfer.getExchangedPlayers() == null) {
			return existingTransfer.getExchangedPlayers() == null;
		}
		
		return newTransfer.getExchangedPlayers().size() == existingTransfer.getExchangedPlayers().size();
	}

	/**
	 * Check whether a player with a given id exists in the list of exchanged players in an existing transfer.
	 * 
	 * @param newPlayerId New player ID.
	 * @param existingTransfer Existing transfer.
	 * @return True if the player exist as an exchanged player in the existing transfer, false otherwise.
	 */
	private static boolean equivalentExchangedPlayerExistsInExistingTransfer(final int newPlayerId, final TransferEntity existingTransfer) {
		for (final ExchangedPlayerEntity existingPlayer : existingTransfer.getExchangedPlayers()) {
			if (existingPlayer.getPlayer().getId() == newPlayerId) {
				return true;
			}
		}
		
		return false;
	}
}
