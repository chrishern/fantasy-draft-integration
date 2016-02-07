/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.facade.dto.AuctionBidsDto;
import net.blackcat.fantasy.draft.integration.facade.dto.AuctionPhaseResultsDto;
import net.blackcat.fantasy.draft.integration.facade.dto.BidDto;
import net.blackcat.fantasy.draft.integration.facade.dto.PlayerAuctionBidResultDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TeamAuctionStatusDto;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionPhaseStatus;
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

        // Get league and open auction phase
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
        }

        // Update the league
        openAuctionPhase.setStatus(AuctionPhaseStatus.CLOSED);
        leagueDataService.updateLeague(league);
    }

    /**
     * Opens a new auction phase for a league.
     * 
     * @param leagueId
     *            League ID of the league to open the auction phase for.
     * @throws FantasyDraftIntegrationException
     */
    public void openAuctionPhase(final int leagueId) throws FantasyDraftIntegrationException {

        final League league = leagueDataService.getLeague(leagueId);

        if (league.hasOpenAuction()) {
            openAuctionPhaseForLeagueWithOpenAuction(league);
        } else {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_NOT_FOUND);
        }
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
            closeAuctionForLeagueWithOpenAuction(league);
        } else {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_NOT_FOUND);
        }
    }

    /**
     * Get the auction status of a team managed by a given manager.
     * 
     * @param managerEmailAddress
     *            Email address of the manager to get the
     * @return
     * @throws FantasyDraftIntegrationException
     */
    public TeamAuctionStatusDto getTeamAuctionStatus(final String managerEmailAddress) throws FantasyDraftIntegrationException {

        final Team team = teamDataService.getTeamForManager(managerEmailAddress);
        final League league = team.getLeague();

        final TeamAuctionStatusDto teamAuctionStatus = new TeamAuctionStatusDto(team.getRemainingBudget());

        if (league.hasOpenAuction()) {
        	try {
	            final AuctionPhase openAuctionPhase = leagueDataService.getOpenAuctionPhase(league);
	
	            teamAuctionStatus.withOpenTransferWindow();
	
	            if (openAuctionPhase.hasTeamSubmittedBids(team.getName())) {
	                teamAuctionStatus.withBidsSubmittedInCurrentWindow();
	            }
        	} catch (final FantasyDraftIntegrationException e) {
            	// Indicates that there is no open auction phase TODO change this
            }
        }

        return teamAuctionStatus;
    }

    /**
     * Get a list of the {@link AuctionPhaseResultsDto} representing the auction phase results for a given league.
     * 
     * @param leagueId
     *            ID of the league to get the auction phase results for.
     * @return List of the {@link AuctionPhaseResultsDto} representing the auction phase results for the given league.
     * @throws FantasyDraftIntegrationException
     */
    public List<AuctionPhaseResultsDto> getLeagueAuctionPhaseResults(final int leagueId) throws FantasyDraftIntegrationException {

        final List<AuctionPhaseResultsDto> auctionPhaseResultsList = new ArrayList<AuctionPhaseResultsDto>();

        final League league = leagueDataService.getLeague(leagueId);

        for (final AuctionPhase auctionPhase : league.getAuction().getPhases()) {

            if (!auctionPhase.isOpen()) {
                final AuctionPhaseResultsDto auctionPhaseResults = new AuctionPhaseResultsDto(auctionPhase.getSequenceNumber());

                final Map<Integer, List<Bid>> playerBids = auctionPhaseResultsService.buildUpPlayerBidList(auctionPhase);
                final Map<Integer, List<Bid>> playerBidsWithSuccessMarked = auctionPhaseResultsService.determineSuccessfulBidsWithoutModelUpdates(playerBids);

                createAndAddAuctionPhaseResultsDtos(auctionPhaseResults, playerBidsWithSuccessMarked);

                auctionPhaseResultsList.add(auctionPhaseResults);
            }
        }

        Collections.sort(auctionPhaseResultsList);

        return auctionPhaseResultsList;
    }

    /*
     * Create PlayerAuctionBidResultDto objects for the a map of player ID to the bids made for that player in an
     * auction phase.
     */
    private void createAndAddAuctionPhaseResultsDtos(final AuctionPhaseResultsDto auctionPhaseResults,
            final Map<Integer, List<Bid>> playerBidsWithSuccessMarked) throws FantasyDraftIntegrationException {

        for (final Integer playerId : playerBidsWithSuccessMarked.keySet()) {
            final Player player = playerDataService.getPlayer(playerId);

            final PlayerAuctionBidResultDto playerAuctionPhaseResult = new PlayerAuctionBidResultDto(player.getForename(), player.getSurname());

            for (final Bid bid : playerBidsWithSuccessMarked.get(playerId)) {
                if (bid.isSuccessful()) {
                    playerAuctionPhaseResult.withSuccessfulBid(bid.getTeam().getName(), bid.getAmount());
                } else {
                    playerAuctionPhaseResult.withUnsuccessfulBid(bid.getTeam().getName(), bid.getAmount());
                }
            }

            auctionPhaseResults.withPlayerResult(playerAuctionPhaseResult);
        }
    }

    /*
     * Close an auction for a league which has an open auction.
     */
    private void closeAuctionForLeagueWithOpenAuction(final League league) throws FantasyDraftIntegrationException {

        if (leagueDataService.doesOpenAuctionPhaseExist(league)) {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_PHASE_EXISTS);
        } else {
            league.closeAuction();
            leagueDataService.updateLeague(league);
        }
    }

    /*
     * Open an auction phase for a league which has an open auction.
     */
    private void openAuctionPhaseForLeagueWithOpenAuction(final League league) throws FantasyDraftIntegrationException {

        if (leagueDataService.doesOpenAuctionPhaseExist(league)) {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_PHASE_EXISTS);
        } else {
            league.openAuctionPhase();
            leagueDataService.updateLeague(league);
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
