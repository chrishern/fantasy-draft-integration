/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.ArrayList;
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
import net.blackcat.fantasy.draft.integration.service.AuctionPhaseResultsService;

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
    private AuctionPhaseResultsService auctionPhaseResultsService;

    @Autowired
    public AuctionFacade(final LeagueDataService leagueDataService, final TeamDataService teamDataService, final PlayerDataService playerDataService,
            final AuctionPhaseResultsService auctionPhaseResultsFacade) {

        this.leagueDataService = leagueDataService;
        this.teamDataService = teamDataService;
        this.playerDataService = playerDataService;
        this.auctionPhaseResultsService = auctionPhaseResultsFacade;
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
        league.getAuction().getPhases();
        final AuctionPhase openAuctionPhase = leagueDataService.getOpenAuctionPhase(league);

        // Convert dtos to domain bids
        final List<Bid> domainBids = convertBidDtos(bids, team);

        // Add domain bids to Auction
        openAuctionPhase.addBids(domainBids);

        // Update league
        leagueDataService.updateLeague(league);
    }

    /**
     * Close an auction phase for a league. This involves calculating winning bids for players and transferring then to
     * the teams with the winning bids.
     * 
     * @param leagueId
     * @throws FantasyDraftIntegrationException
     */
    public void closeAuctionPhase(final int leagueId) throws FantasyDraftIntegrationException {

        // Get league and open auction phase. - TODO combine into a single query
        final League league = leagueDataService.getLeague(leagueId);
        final AuctionPhase openAuctionPhase = leagueDataService.getOpenAuctionPhase(league);

        // Build up a map of PlayerId to List of Bids for that player
        final Map<Integer, List<Bid>> playerBids = auctionPhaseResultsService.buildUpPlayerBidList(openAuctionPhase);

        final Map<Integer, List<Bid>> playerBidsWithSuccessMarked = auctionPhaseResultsService.determineSuccessfulBids(playerBids);

        final Map<Team, List<Bid>> successfulTeamBids = auctionPhaseResultsService.getSuccessfulTeamBids(playerBidsWithSuccessMarked);

        // Move each 'won' player to their Team as a SelectedPlayer
        for (final Team team : successfulTeamBids.keySet()) {

            for (final Bid bid : successfulTeamBids.get(team)) {
                team.processSuccessfulBid(bid);
            }

            teamDataService.updateTeam(team);
        }

        // Also set the selection status of each Player to show they are picked and update the player - do we need to do
        // this?? I didn't last time

        // Update the league
        leagueDataService.updateLeague(league);
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
