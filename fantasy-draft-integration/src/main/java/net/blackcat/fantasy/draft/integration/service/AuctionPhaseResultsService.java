/**
 * 
 */
package net.blackcat.fantasy.draft.integration.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.Team;

import org.springframework.stereotype.Service;

/**
 * Facade for performing auction phase results functionality.s
 * 
 * @author Chris Hern
 * 
 */
@Service
public class AuctionPhaseResultsService {

    /**
     * Build up a map of a list of the bids made for each player.
     * 
     * @param openAuctionPhase
     *            An open {@link AuctionPhase} from a league.
     * @return Map of player IDs mapped to a list of bids made for that player.
     */
    public Map<Integer, List<Bid>> buildUpPlayerBidList(final AuctionPhase openAuctionPhase) {

        final Map<Integer, List<Bid>> playerBids = new HashMap<Integer, List<Bid>>();

        for (final Bid bid : openAuctionPhase.getBids()) {
            List<Bid> bidList = playerBids.get(bid.getPlayer().getId());

            if (bidList == null) {
                bidList = new ArrayList<Bid>();
            }

            bidList.add(bid);
            playerBids.put(bid.getPlayer().getId(), bidList);
        }
        return playerBids;
    }

    /**
     * Go through a map of the list of bids for each player and determine the successful bid (if any) for each player.
     * 
     * @param playerBids
     *            Map of player ID to the list of bids of that player.
     * @return Updated map of Player ID to list of bids for that player with any successful bids marked as so.
     */
    public Map<Integer, List<Bid>> determineSuccessfulBids(final Map<Integer, List<Bid>> playerBids) {

        final Map<Integer, List<Bid>> updatedPlayerBids = new HashMap<Integer, List<Bid>>();

        for (final Integer playerId : playerBids.keySet()) {

            final List<Bid> bids = playerBids.get(playerId);
            Collections.sort(bids);

            if (playerHasOnlyOneBid(bids)) {
                markSuccessfulBid(bids);
            } else {
                processMultipleBidPlayer(bids);
            }

            updatedPlayerBids.put(playerId, bids);
        }

        return updatedPlayerBids;
    }
    
    /*
     * TODO Look at this
     */
    public Map<Integer, List<Bid>> determineSuccessfulBidsWithoutModelUpdates(final Map<Integer, List<Bid>> playerBids) {

        final Map<Integer, List<Bid>> updatedPlayerBids = new HashMap<Integer, List<Bid>>();

        for (final Integer playerId : playerBids.keySet()) {

            final List<Bid> bids = playerBids.get(playerId);
            Collections.sort(bids);

            if (playerHasOnlyOneBid(bids)) {
            	markSuccessfulBidWithoutModelUpdates(bids);
            } else {
            	processMultipleBidPlayerWithoutModelUpdates(bids);
            }

            updatedPlayerBids.put(playerId, bids);
        }

        return updatedPlayerBids;
    }

    /**
     * Get a map of Team objects mapped to a list of successful bids that team has made.
     * 
     * @param bids
     *            Map of player ID to list of bids made for that player with any successful bids marked as so.
     * @return Map of Team objects mapped to a list of successful bids for that team.
     */
    public Map<Team, List<Bid>> getSuccessfulTeamBids(final Map<Integer, List<Bid>> bids) {

        final Map<Team, List<Bid>> teamBids = new HashMap<Team, List<Bid>>();

        for (final Integer playerId : bids.keySet()) {

            if (playerHasSuccessfulBid(bids, playerId)) {
                addSuccessfulBidToTeamBids(bids, teamBids, playerId);
            }
        }

        return teamBids;
    }

    /*
     * Add a successful bid for a team to their list of successful bids.
     */
    private void addSuccessfulBidToTeamBids(final Map<Integer, List<Bid>> bids, final Map<Team, List<Bid>> teamBids, final Integer playerId) {

        final Bid successfulBid = bids.get(playerId).get(0);

        List<Bid> successfulTeamBids = teamBids.get(successfulBid.getTeam());

        if (successfulTeamBids == null) {
            successfulTeamBids = new ArrayList<Bid>();
            teamBids.put(successfulBid.getTeam(), successfulTeamBids);
        }

        successfulTeamBids.add(successfulBid);
    }

    /*
     * Determine if a player has a successful bid or not.
     */
    private boolean playerHasSuccessfulBid(final Map<Integer, List<Bid>> bids, final Integer playerId) {
        return bids.get(playerId).get(0).isSuccessful();
    }

    /*
     * Process a player which has a number of bids.
     */
    private void processMultipleBidPlayer(final List<Bid> bids) {
        if (!isMatchingHighestBids(bids)) {
            markSuccessfulBid(bids);
        }
    }
    
    /*
     * TODO Look at this.
     */
    private void processMultipleBidPlayerWithoutModelUpdates(final List<Bid> bids) {
        if (!isMatchingHighestBids(bids)) {
            markSuccessfulBidWithoutModelUpdates(bids);
        }
    }

    /*
     * Determine if a player only has one bid.
     */
    private boolean playerHasOnlyOneBid(final List<Bid> bids) {
        return bids.size() == 1;
    }

    /*
     * Determine if we have matching highest bids.
     */
    private boolean isMatchingHighestBids(final List<Bid> bids) {
        return bids.get(0).hasMatchingAmount(bids.get(1));
    }

    /*
     * Mark the successful bid from a list of bids.
     */
    private void markSuccessfulBid(final List<Bid> bids) {

        final Bid successfulBid = bids.get(0);

        successfulBid.markBidAsSuccessful();
        successfulBid.getPlayer().markPlayerAsSelected();
    }
    
    /*
     * TODO Look at this.
     */
    private void markSuccessfulBidWithoutModelUpdates(final List<Bid> bids) {

        final Bid successfulBid = bids.get(0);

        successfulBid.markBidAsSuccessful();
    }
}
