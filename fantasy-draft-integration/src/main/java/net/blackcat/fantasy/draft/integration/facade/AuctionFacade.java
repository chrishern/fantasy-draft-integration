/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.facade.dto.AuctionBidsDto;
import net.blackcat.fantasy.draft.integration.facade.dto.BidDto;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade containing operations needed to perform an initial auction.
 * 
 * @author Chris Hern
 * 
 */
@Component
@Transactional
public class AuctionFacade {

    private LeagueDataService leagueDataService;
    private TeamDataService teamDataService;
    private PlayerDataService playerDataService;

    @Autowired
    public AuctionFacade(final LeagueDataService leagueDataService, final TeamDataService teamDataService, final PlayerDataService playerDataService) {

        this.leagueDataService = leagueDataService;
        this.teamDataService = teamDataService;
        this.playerDataService = playerDataService;
    }

    /**
     * Start the auction phase for a given league.
     * 
     * @param leagueId
     *            ID of the league to start the auction for.
     */
    public void startAuction(final int leagueId) throws FantasyDraftIntegrationException {

        final League league = leagueDataService.getLeague(leagueId);

        league.openAuction();

        leagueDataService.updateLeague(league);
    }

    /**
     * Make bids for players within the auction.
     * 
     * @param bids
     *            Bids made by a team.
     */
    public void makeBids(final AuctionBidsDto bids) throws FantasyDraftIntegrationException {

        // Get team associated with bids, league associated with team and open auction phase for league
        final Team team = teamDataService.getTeam(bids.getTeamId());
        final League league = team.getLeague();
        final AuctionPhase openAuctionPhase = leagueDataService.getOpenAuctionPhase(league);

        // Convert dtos to domain bids
        final List<Bid> domainBids = convertBidDtos(bids, team);

        // Add domain bids to Auction
        openAuctionPhase.addBids(domainBids);

        // Update league
        leagueDataService.updateLeague(league);
    }

    public void closeAuctionPhase(final int leagueId) throws FantasyDraftIntegrationException {

        // Get league and open auction phase. - TODO combine into a single query
        final League league = leagueDataService.getLeague(leagueId);
        final AuctionPhase openAuctionPhase = leagueDataService.getOpenAuctionPhase(league);

        // Build up a map of PlayerId to List of Bids for that player
        final Map<Integer, List<Bid>> playerBids = new HashMap<Integer, List<Bid>>();

        for (final Bid bid : openAuctionPhase.getBids()) {
            List<Bid> bidList = playerBids.get(bid.getPlayer().getId());

            if (bidList == null) {
                bidList = new ArrayList<Bid>();
            }

            bidList.add(bid);
            playerBids.put(bid.getPlayer().getId(), bidList);
        }

        // Work out the highest bidder for each player
        for (final Integer playerId : playerBids.keySet()) {

            final List<Bid> bids = playerBids.get(playerId);

            if (bids.size() == 1) {
                // If we only have one bid, we have a successful bid
                final Bid successfulBid = bids.get(0);

                successfulBid.markBidAsSuccessful();
                successfulBid.getPlayer().markPlayerAsSelected();
            } else {
                Collections.sort(bids);

                if (bids.get(0).hasMatchingAmount(bids.get(1))) {
                    // We have matching bids
                } else {
                    final Bid successfulBid = bids.get(0);

                    successfulBid.markBidAsSuccessful();
                    successfulBid.getPlayer().markPlayerAsSelected();
                }
            }
        }

        // Move each 'won' player to their Team as a SelectedPlayer
        // Also set the selection status of each Player to show they are picked and update the player

        // Update the league
    }

    /**
     * Close the auction phase for a given league.
     * 
     * @param leagueId
     *            ID of the league to start the auction for.
     */
    public void closeAuction(final int leagueId) throws FantasyDraftIntegrationException {

        final League league = leagueDataService.getLeague(leagueId);

        if (league.hasOpenAuction()) {
            league.closeAuction();
            leagueDataService.updateLeague(league);
        } else {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_NOT_FOUND);
        }
    }

    /*
     * Convert the bids within an AuctionBidsDto into a list of domain Bid objects.
     */
    private List<Bid> convertBidDtos(final AuctionBidsDto bids, final Team team) throws FantasyDraftIntegrationException {

        final List<Bid> domainBids = new ArrayList<Bid>();

        for (final BidDto bidDto : bids.getBids()) {

            final Player player = playerDataService.getPlayer(bidDto.getPlayerId());

            final Bid bid = new Bid(team, player, bidDto.getAmount());
            domainBids.add(bid);
        }
        return domainBids;
    }
}
