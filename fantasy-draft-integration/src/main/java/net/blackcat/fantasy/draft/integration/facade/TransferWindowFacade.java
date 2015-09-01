package net.blackcat.fantasy.draft.integration.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.dto.AuctionBidsDto;
import net.blackcat.fantasy.draft.integration.facade.dto.AuctionPhaseResultsDto;
import net.blackcat.fantasy.draft.integration.facade.dto.BidDto;
import net.blackcat.fantasy.draft.integration.facade.dto.OpenTransferWindowTeamTransfersDto;
import net.blackcat.fantasy.draft.integration.facade.dto.PlayerAuctionBidResultDto;
import net.blackcat.fantasy.draft.integration.facade.dto.PotSaleSummaryDto;
import net.blackcat.fantasy.draft.integration.facade.dto.SellPlayerToPotDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TeamAuctionStatusDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TransferBidDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TransferSummaryDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TransferWindowSummaryDto;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.SaleToPot;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.Transfer;
import net.blackcat.fantasy.draft.integration.model.TransferWindow;
import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionPhaseStatus;
import net.blackcat.fantasy.draft.integration.service.AuctionPhaseResultsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TransferWindowFacade {

	private TeamDataService teamDataService;
	private LeagueDataService leagueDataService;
	private PlayerDataService playerDataService;
	private AuctionPhaseResultsService auctionPhaseResultsService;
	
	@Autowired
	public TransferWindowFacade(final TeamDataService teamDataService, final LeagueDataService leagueDataService, final PlayerDataService playerDataService, final AuctionPhaseResultsService auctionPhaseResultsService) {
		this.teamDataService = teamDataService;
		this.leagueDataService = leagueDataService;
		this.playerDataService = playerDataService;
		this.auctionPhaseResultsService = auctionPhaseResultsService;
	}

	/**
	 * Sell a player to the pot.
	 * 
	 * @param sellPlayerToPotDto DTO containing details the player being sold to the pot.
	 * @throws FantasyDraftIntegrationException
	 */
	public void sellPlayerToPot(final SellPlayerToPotDto sellPlayerToPotDto) throws FantasyDraftIntegrationException {
		
		final Team teamForManager = teamDataService.getTeamForManager(sellPlayerToPotDto.getManagerEmailAddress());
    	final League league = teamForManager.getLeague();
    	final TransferWindow transferWindow = league.getTransferWindows().get(0);
    	final SelectedPlayer soldToPotPlayer = teamForManager.getSelectedPlayer(sellPlayerToPotDto.getSelectedPlayerId());
    	
    	teamForManager.sellPlayerToPot(sellPlayerToPotDto.getSelectedPlayerId(), sellPlayerToPotDto.getAmount());
    	transferWindow.addPotSale(teamForManager, soldToPotPlayer, sellPlayerToPotDto.getAmount());
    	
    	leagueDataService.updateLeague(league);
	}
	
	/**
	 * Make a transfer bid in the current transfer window.
	 * 
	 * @param transferBid DTO containing transfer bid details.
	 * @throws FantasyDraftIntegrationException
	 */
	public void makeTransferBid(final TransferBidDto transferBid) throws FantasyDraftIntegrationException {
		
		final Team buyingTeam = teamDataService.getTeam(transferBid.getBuyingTeam());
		final Team sellingTeam = teamDataService.getTeam(transferBid.getSellingTeam());
		final SelectedPlayer playerSubjectOfBid = sellingTeam.getSelectedPlayer(transferBid.getSelectedPlayerSubjectOfBid());
		final League league = buyingTeam.getLeague();
		final TransferWindow transferWindow = league.getTransferWindows().get(0);
		
		transferWindow.makeTransferBid(buyingTeam, sellingTeam, playerSubjectOfBid, transferBid.getAmount());
		
		leagueDataService.updateLeague(league);
	}
	
	/**
	 * Accept a transfer bid that has been made in this window.
	 * 
	 * @param managerEmailAddress Email address of the manager who has accepted the bid.
	 * @param transferId ID of the bid to accept.
	 * @throws FantasyDraftIntegrationException
	 */
	public void acceptTransferBid(final String managerEmailAddress, final int transferId) throws FantasyDraftIntegrationException {
		
		final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);
    	final League league = teamForManager.getLeague();
    	final TransferWindow transferWindow = league.getTransferWindows().get(0);
    	
    	transferWindow.acceptTransfer(transferId);
    	
    	leagueDataService.updateLeague(league);
	}
	
	/**
	 * Reject a transfer bid that has been made in this window.
	 * 
	 * @param managerEmailAddress Email address of the manager who has rejected the bid.
	 * @param transferId ID of the bid to reject.
	 * @throws FantasyDraftIntegrationException
	 */
	public void rejectTransferBid(final String managerEmailAddress, final int transferId) throws FantasyDraftIntegrationException {
		
		final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);
    	final League league = teamForManager.getLeague();
    	final TransferWindow transferWindow = league.getTransferWindows().get(0);
    	
    	transferWindow.rejectTransfer(transferId);
    	
    	leagueDataService.updateLeague(league);
	}
	
	public OpenTransferWindowTeamTransfersDto getTeamTransferWindowTransferDetail(final String managerEmailAddress) throws FantasyDraftIntegrationException {
		
		final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);
    	final League league = teamForManager.getLeague();
    	final TransferWindow transferWindow = league.getTransferWindows().get(0);
    	final int teamId = teamForManager.getId();
    	
    	final OpenTransferWindowTeamTransfersDto transferDetail = new OpenTransferWindowTeamTransfersDto(teamId);
    	
    	for (final Transfer transfer : transferWindow.getTransfers()) {
    		
    		final int transferId = transfer.getId();
    		final String playerForename = transfer.getPlayerBiddedFor().getPlayer().getForename();
    		final String playerSurname = transfer.getPlayerBiddedFor().getPlayer().getSurname();
    		final String buyingTeam = transfer.getBuyingTeam().getName();
    		final String sellingTeam = transfer.getSellingTeam().getName();
    		final BigDecimal amount = transfer.getAmount();
    		
    		if (transfer.getBuyingTeam().getId() == teamId) {
    			if (transfer.isAccepted()) {
    				transferDetail.addBoughtPlayer(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			} else if (transfer.isRejected()) {
    				transferDetail.addOutgoingRejectedBid(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			} else {
    				transferDetail.addPendingBid(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			}
    		} else if (transfer.getSellingTeam().getId() == teamId) {
    			if (transfer.isAccepted()) {
    				transferDetail.addSoldPlayer(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			} else if (transfer.isRejected()) {
    				transferDetail.addIncomingRejectedBid(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			} else {
    				transferDetail.addIncomingPendingBid(transferId, playerForename, playerSurname, buyingTeam, sellingTeam, amount);
    			}
    		}
    	}
    	
    	return transferDetail;
	}
	
	/**
	 * Move this transfer window onto the auction phase.
	 * 
	 * @param leagueId ID of the league to move onto the auction phase.
	 * @throws FantasyDraftIntegrationException 
	 */
	public void moveTransferWindowOntoAuction(final int leagueId) throws FantasyDraftIntegrationException {
		
		final League league = leagueDataService.getLeague(leagueId);
        final TransferWindow transferWindow = league.getTransferWindows().get(0);
        
        updatePlayersSoldToPot(transferWindow);
        updateTransfersBetweenTeams(transferWindow);
        
        transferWindow.startAuctionPhase();
        
        leagueDataService.updateLeague(league);
	}

	/**
     * Start an auction phase for a given league.
     * 
     * @param leagueId
     *            ID of the league to start an auction phase for.
     */
    public void startAuctionPhase(final int leagueId) throws FantasyDraftIntegrationException {

        final League league = leagueDataService.getLeague(leagueId);
        final TransferWindow transferWindow = league.getTransferWindows().get(0);

        transferWindow.startAuctionPhase();
        
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
        final TransferWindow transferWindow = league.getTransferWindows().get(0);
        final AuctionPhase openAuctionPhase = transferWindow.getOpenAuctionPhase();

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
        final TransferWindow transferWindow = league.getTransferWindows().get(0);
        final AuctionPhase openAuctionPhase = transferWindow.getOpenAuctionPhase();

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
        final TransferWindow transferWindow = league.getTransferWindows().get(0);
        final AuctionPhase openAuctionPhase = transferWindow.getOpenAuctionPhase();

        final TeamAuctionStatusDto teamAuctionStatus = new TeamAuctionStatusDto(team.getRemainingBudget());

        if (openAuctionPhase != null) {
            teamAuctionStatus.withOpenTransferWindow();

            if (openAuctionPhase.hasTeamSubmittedBids(team.getName())) {
                teamAuctionStatus.withBidsSubmittedInCurrentWindow();
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
    public TransferWindowSummaryDto getTransferWindowSummary(final int leagueId) throws FantasyDraftIntegrationException {

    	final TransferWindowSummaryDto transferWindowSummary = new TransferWindowSummaryDto();
        
        final League league = leagueDataService.getLeague(leagueId);
        final TransferWindow transferWindow = league.getTransferWindows().get(0);

        final List<AuctionPhaseResultsDto> auctionPhaseResultsList = createAuctionPhaseResults(transferWindow);
        final List<PotSaleSummaryDto> potSaleSummaryList = createPotSaleSummary(transferWindow);
        final List<TransferSummaryDto> transferSummaryList = createTransferSummary(transferWindow);
        
        transferWindowSummary.setTransfers(transferSummaryList);
        transferWindowSummary.setPotSales(potSaleSummaryList);
        transferWindowSummary.setAuctionPhaseResults(auctionPhaseResultsList);
        
        return transferWindowSummary;
    }

    /*
	 * Create the summary of transfers in this transfer window.
	 */
    private List<TransferSummaryDto> createTransferSummary(final TransferWindow transferWindow) {
    	
    	final List<TransferSummaryDto> transferSummaryList = new ArrayList<TransferSummaryDto>();
    	
    	for (final Transfer transfer : transferWindow.getTransfers()) {
    		
    		if (transfer.isAccepted()) {
    			final Player player = transfer.getPlayerBiddedFor().getPlayer();
    			
    			final TransferSummaryDto transferSummary = new TransferSummaryDto(player.getForename(), player.getSurname(), 
    					transfer.getBuyingTeam().getName(), transfer.getSellingTeam().getName(), transfer.getAmount());
    			
    			transferSummaryList.add(transferSummary);
    		}
    	}
    	
    	return transferSummaryList;
    }
    
	/*
	 * Create the summary of sales to the pot in this transfer window.
	 */
	private List<PotSaleSummaryDto> createPotSaleSummary(final TransferWindow transferWindow) {
		
		final List<PotSaleSummaryDto> potSaleSummaryList = new ArrayList<PotSaleSummaryDto>();
        
        for (final SaleToPot saleToPot : transferWindow.getPotSales()) {
        	final Player player = saleToPot.getPlayer().getPlayer();
        	
        	final PotSaleSummaryDto potSaleSummary = new PotSaleSummaryDto(player.getForename(), player.getSurname(), saleToPot.getTeam().getName(), saleToPot.getAmount());
        	potSaleSummaryList.add(potSaleSummary);
        }
		return potSaleSummaryList;
	}

	/*
	 * Create the auction phase results for the transfer window.
	 */
	private List<AuctionPhaseResultsDto> createAuctionPhaseResults(final TransferWindow transferWindow) throws FantasyDraftIntegrationException {

        final List<AuctionPhaseResultsDto> auctionPhaseResultsList = new ArrayList<AuctionPhaseResultsDto>();
        
		for (final AuctionPhase auctionPhase : transferWindow.getAuctionPhases()) {

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
	 * Update all of the confirmed transfers between teams.
	 */
	private void updateTransfersBetweenTeams(final TransferWindow transferWindow) {
		
		for (final Transfer transfer : transferWindow.getTransfers()) {
        	if (transfer.isAccepted()) {
        		final SelectedPlayer transferredSelectedPlayer = transfer.getPlayerBiddedFor();
        		final Team buyingTeam = transfer.getBuyingTeam();
        		
        		transferredSelectedPlayer.completePlayerSale();
        		buyingTeam.completePlayerPurchase(transferredSelectedPlayer.getPlayer(), transfer.getAmount());
        	}
        }
	}

	/*
	 * Update all of the players sold to the pot.
	 */
	private void updatePlayersSoldToPot(final TransferWindow transferWindow) {
		
		for (final SaleToPot saleToPot : transferWindow.getPotSales()) {
        	final SelectedPlayer selectedPlayer = saleToPot.getPlayer();
        	
        	selectedPlayer.completeSaleToPot();
        	selectedPlayer.getPlayer().markPlayerAsNotSelected();
        }
	}
}
