/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import java.util.List;

import net.blackcat.fantasy.draft.bid.Bid;

/**
 * Defines operations which allow operations on draft rounds to be performed.
 * 
 * @author Chris
 *
 */
public interface DraftRoundDataService {

	/**
	 * Add a list of bids to the currently open bidding phase of the given league.
	 * 
	 * @param leagueId The ID of the league in which the bids are being made.
	 * @param bids List of {@link Bid} objects to add to the back end.
	 */
	void addBids(int leagueId, List<Bid> bids);
}
