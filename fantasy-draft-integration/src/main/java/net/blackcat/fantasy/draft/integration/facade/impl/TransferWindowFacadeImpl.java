/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.auction.AuctionPlayerBid;
import net.blackcat.fantasy.draft.auction.AuctionPlayerResult;
import net.blackcat.fantasy.draft.auction.AuctionRoundResults;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.data.service.TransferWindowDataService;
import net.blackcat.fantasy.draft.integration.entity.BidEntity;
import net.blackcat.fantasy.draft.integration.entity.ExchangedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferWindowEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferredPlayerEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.facade.TransferWindowFacade;
import net.blackcat.fantasy.draft.integration.util.TransferUtils;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.round.Bid;
import net.blackcat.fantasy.draft.round.TeamBids;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;
import net.blackcat.fantasy.draft.team.Team;
import net.blackcat.fantasy.draft.team.types.TeamStatus;
import net.blackcat.fantasy.draft.transfer.LeagueTransferSummary;
import net.blackcat.fantasy.draft.transfer.LeagueTransferWindowSummary;
import net.blackcat.fantasy.draft.transfer.Transfer;
import net.blackcat.fantasy.draft.transfer.TransferSummary;
import net.blackcat.fantasy.draft.transfer.types.TransferStatus;
import net.blackcat.fantasy.draft.transfer.types.TransferType;

import org.springframework.beans.BeanUtils;
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
	
	@Autowired
	@Qualifier(value = "teamDataServiceJpa")
	private TeamDataService teamDataService;
	
	@Autowired
	@Qualifier(value = "playerDataServiceJpa")
	private PlayerDataService playerDataService;
	
	@Override
	public void startTransferWindow(final int leagueId, final int phase) throws FantasyDraftIntegrationException {
		final LeagueEntity league = leagueDataService.getLeague(leagueId);
		
		final TransferWindowEntity draftRound = new TransferWindowEntity(phase, league);
		transferWindowDataService.createTransferWindow(draftRound);
	}
	
	@Override
	public AuctionRoundResults closeTransferWindow(final int leagueId) throws FantasyDraftIntegrationException {
		final TransferWindowEntity openWindow = transferWindowDataService.getOpenTransferWindow(leagueId);
		
		final AuctionRoundResults auctionRoundResults = new AuctionRoundResults(openWindow.getKey().getSequenceNumber());
		final List<AuctionPlayerResult> playerAuctionResults = new ArrayList<AuctionPlayerResult>();
		auctionRoundResults.setPlayerResults(playerAuctionResults);

		// Create a map of player id to BidEntity
		final Map<Integer, List<BidEntity>> playerBids = createPlayerBidLists(openWindow);
		
		// Go through each entity in the map and decide who had the winning bid, creating the AuctionPlayerResults as we go
		processAuctionRoundResults(playerAuctionResults, playerBids);
		
		// Update the draft round entity to show that it is CLOSED and also set the successful status on each BidEntity
		openWindow.setStatus(DraftRoundStatus.CLOSED);
		transferWindowDataService.updateTransferWindow(openWindow);
		
		// Move all the bids of successful players into the appropriate team.
		moveSuccessfulPlayerBidsToTeam(openWindow);
		
		return auctionRoundResults;
		
	}
	
	/**
	 * Build up a map of the teams who had successful auction bids and their successful bids.
	 * 
	 * @param draftRound Draft round results.
	 * @param selectedPlayersMap Map of teams who had successful auction bids and their successful bids.
	 */
	private void buildSuccessfulBidsForTeams(final TransferWindowEntity draftRound, final Map<TeamEntity, List<BidEntity>> selectedPlayersMap) {
		for (final BidEntity bid : draftRound.getAuctionRoundBids()) {
			if (bid.isSuccessful()) {
				
				List<BidEntity> teamBidList = selectedPlayersMap.get(bid.getTeam());
				
				if (teamBidList == null) {
					teamBidList = new ArrayList<BidEntity>();
					selectedPlayersMap.put(bid.getTeam(), teamBidList);
				}
				
				teamBidList.add(bid);
			}
		}
	}

	/**
	 * Update each team entity with the successful player bids.
	 * 
	 * @param selectedPlayersMap Map of teams and their successful bids.
	 */
	private void updateTeamsWithSuccessfulBids(final Map<TeamEntity, List<BidEntity>> selectedPlayersMap) {
		for (final TeamEntity teamToUpdate : selectedPlayersMap.keySet()) {
			final List<SelectedPlayerEntity> selectedPlayers = new ArrayList<SelectedPlayerEntity>();
			BigDecimal costOfSuccessfulBids = BigDecimal.ZERO;
			
			for (final BidEntity successfulBid : selectedPlayersMap.get(teamToUpdate)) {
				final SelectedPlayerEntity selectedPlayer = new SelectedPlayerEntity(successfulBid.getPlayer());
				selectedPlayer.setCost(successfulBid.getAmount());
				selectedPlayers.add(selectedPlayer);
				
				costOfSuccessfulBids = costOfSuccessfulBids.add(successfulBid.getAmount());
			}
			
			teamToUpdate.setRemainingBudget(teamToUpdate.getRemainingBudget().subtract(costOfSuccessfulBids));
			teamToUpdate.addSelectedPlayers(selectedPlayers);
			
			if (getNumberOfSelectedPlayers(teamToUpdate.getSelectedPlayers()) == 16) {
				teamToUpdate.setStatus(TeamStatus.COMPLETE);
			}
			
			teamDataService.updateTeam(teamToUpdate);
		}
	}
	
	private int getNumberOfSelectedPlayers(final List<SelectedPlayerEntity> selectedPlayers) {
		int noOfSelectedPlayers = 0;
		
		for (final SelectedPlayerEntity selectedPlayer : selectedPlayers) {
			if (selectedPlayer.getSelectedPlayerStatus() == SelectedPlayerStatus.STILL_SELECTED) {
				noOfSelectedPlayers++;
			}
		}
		
		return noOfSelectedPlayers;
	}
	
	/**
	 * Move all of the successful bids for players to be selected players within the appropriate team.
	 * 
	 * @param draftRound Draft round containing all bid results.
	 */
	private void moveSuccessfulPlayerBidsToTeam(final TransferWindowEntity draftRound) {
		final Map<TeamEntity, List<BidEntity>> selectedPlayersMap = new HashMap<TeamEntity, List<BidEntity>>();
		
		buildSuccessfulBidsForTeams(draftRound, selectedPlayersMap);
		updateTeamsWithSuccessfulBids(selectedPlayersMap);
	}
	
	/**
	 * Process the auction round updates, updating any entities and building up the {@link AuctionPlayerResult} list as we go.
	 * 
	 * @param playerAuctionResults List of {@link AuctionPlayerResult} object to record the results of the player bids on.
	 * @param playerBids List of player bids made in this auction round.
	 */
	private void processAuctionRoundResults(final List<AuctionPlayerResult> playerAuctionResults, final Map<Integer, List<BidEntity>> playerBids) {
		for (final List<BidEntity> bids : playerBids.values()) {
			final PlayerEntity playerEntity = bids.get(0).getPlayer();
			final Player modelPlayer = new Player();
			BeanUtils.copyProperties(playerEntity, modelPlayer);
			
			final AuctionPlayerResult playerResults = new AuctionPlayerResult(modelPlayer);
			playerAuctionResults.add(playerResults);
			
			Collections.sort(bids);
			
			final BidEntity firstBid = bids.get(0);
			
			if (bids.size() == 1) {
				processSuccessfulBid(playerResults, firstBid);
			} else {
				processMultipleBidPlayer(bids, playerResults, firstBid);
			}
		}
	}
	
	/**
	 * Processes the auction bids for a player who has had multiple bids against them.
	 * 
	 * @param bids The bids for the player.
	 * @param playerResults Auction results object to update with the processed results.
	 * @param firstBid The first bid that was made on the player.
	 */
	private void processMultipleBidPlayer(final List<BidEntity> bids, final AuctionPlayerResult playerResults, final BidEntity firstBid) {
		// If there are any matching bids, update the player selection status to restricted and make a note of the teams who can bid
		if (isMatchingHighestBid(bids, firstBid)) {
			processPlayerWithMatchingHighestBid(bids, playerResults, firstBid);
		} else {
			processSuccessfulBid(playerResults, firstBid);
			
			final List<AuctionPlayerBid> unsuccessfulBids = new ArrayList<AuctionPlayerBid>();
			for (int i = 1; i < bids.size(); i++) {
				bids.get(i).setSuccessful(false);
				
				final Team unsuccessfulModelTeam = new Team(bids.get(i).getTeam().getName());
				final AuctionPlayerBid unsuccesfulAuctionBid = new AuctionPlayerBid(unsuccessfulModelTeam, bids.get(i).getAmount());
				unsuccessfulBids.add(unsuccesfulAuctionBid);
			}
			
			playerResults.setUnsuccessfulBids(unsuccessfulBids);
		}
	}

	/**
	 * Determine if there is a matching highest bid for a player.
	 * 
	 * @param bids Bids for a certain player.
	 * @param firstBid The first bid for the player.
	 * @return True if there are matching highest bids, false otherwise.
	 */
	private boolean isMatchingHighestBid(final List<BidEntity> bids, final BidEntity firstBid) {
		return firstBid.getAmount().equals(bids.get(1).getAmount());
	}

	/**
	 * Process the bids for a player who has joint highest bids.
	 * 
	 * @param bids The bids for the player.
	 * @param playerResults Auction results object to update with the processed results.
	 * @param firstBid The first bid that was made on the player.
	 */
	private void processPlayerWithMatchingHighestBid(final List<BidEntity> bids, final AuctionPlayerResult playerResults, final BidEntity firstBid) {
		final BigDecimal highestBid = firstBid.getAmount();
		
		final List<AuctionPlayerBid> unsuccessfulBids = new ArrayList<AuctionPlayerBid>();
		final List<AuctionPlayerBid> matchingHighBids = new ArrayList<AuctionPlayerBid>();
		
		for (final BidEntity playerBid : bids) {
			playerBid.setSuccessful(false);

			final Team modelTeam = new Team(playerBid.getTeam().getName());
			final AuctionPlayerBid auctionBid = new AuctionPlayerBid(modelTeam, playerBid.getAmount());

			if (playerBid.getAmount().equals(highestBid)) {
				matchingHighBids.add(auctionBid);
			} else {
				unsuccessfulBids.add(auctionBid);
			}
		}
		
		playerResults.setUnsuccessfulBids(unsuccessfulBids);
		playerResults.setMatchingHighBids(matchingHighBids);
	}

	/**
	 * Process the successful bid for a player.
	 * 
	 * @param playerResults Auction results object to update with the processed results.
	 * @param successfulBid The successful bid.
	 */
	private void processSuccessfulBid(final AuctionPlayerResult playerResults, final BidEntity successfulBid) {
		successfulBid.setSuccessful(true);
		successfulBid.getPlayer().setSelectionStatus(PlayerSelectionStatus.SELECTED);
		
		final Team modelTeam = new Team(successfulBid.getTeam().getName());
		final AuctionPlayerBid auctionBid = new AuctionPlayerBid(modelTeam, successfulBid.getAmount());
		playerResults.setSuccessfulBid(auctionBid);
	}
	
	/**
	 * Create a map containing a player id mapped to the list of bids for that player in a given draft round.
	 * 
	 * @param draftRound Draft round to create the player bid list for.
	 * @return Map containing a player id mapped to the list of bids for that player.
	 */
	private Map<Integer, List<BidEntity>> createPlayerBidLists(final TransferWindowEntity draftRound) {
		final Map<Integer, List<BidEntity>> playerBids = new HashMap<Integer, List<BidEntity>>();
		
		for (final BidEntity bid : draftRound.getAuctionRoundBids()) {
			List<BidEntity> bidList = playerBids.get(bid.getPlayer().getId());
			
			if (bidList == null) {
				bidList = new ArrayList<BidEntity>();
			} 
			
			bidList.add(bid);
			playerBids.put(bid.getPlayer().getId(), bidList);
		}
		
		return playerBids;
	}

	@Override
	public void addTransfer(final Transfer transfer) throws FantasyDraftIntegrationException {
		final TeamEntity buyingTeam = teamDataService.getTeam(transfer.getBuyingTeam());
		final TeamEntity sellingTeam = teamDataService.getTeam(transfer.getSellingTeam());
		
		final List<TransferredPlayerEntity> players = getPlayerEntitiesInvolvedInTransfer(transfer);
		final List<ExchangedPlayerEntity> exchangedPlayers = getPlayersBeingExchangedInTransfer(transfer);
		
		final LeagueEntity league = leagueDataService.getLeagueForTeam(transfer.getBuyingTeam());
		final TransferWindowEntity openWindow = transferWindowDataService.getOpenTransferWindow(league.getId());
		
		final TransferEntity newTransfer = new TransferEntity(players, sellingTeam, buyingTeam, exchangedPlayers, transfer.getAmount());
		
		final TransferStatus transferStatus = setTransferStatus(transfer, openWindow, newTransfer);
		
		updateSellingTeamData(transfer, sellingTeam, players, transferStatus);
		updateBuyingTeamData(transfer, buyingTeam, exchangedPlayers, transferStatus);
		transferWindowDataService.updateTransferWindow(openWindow);
	}
	
	@Override
	public void sellPlayerToPot(final int teamId, final int playerId) throws FantasyDraftIntegrationException {
		final TeamEntity sellingTeam = teamDataService.getTeam(teamId);
		
		for (final SelectedPlayerEntity selectedPlayer : sellingTeam.getSelectedPlayers()) {
			if (selectedPlayer.getPlayer().getId() == playerId) {
				
				updateTeamDataForSaleToPot(sellingTeam, selectedPlayer);
				updateTransferWindowDataForSaleToPot(sellingTeam, selectedPlayer);
				
				break;
			}
		}
	}
	
	@Override
	public List<TransferSummary> getTransfersForTeamInOpenWindow(final int teamId) throws FantasyDraftIntegrationException {
		final List<TransferSummary> transfers = new ArrayList<TransferSummary>();
		
		final LeagueEntity league = leagueDataService.getLeagueForTeam(teamId);
		final TransferWindowEntity openWindow = transferWindowDataService.getOpenTransferWindow(league.getId());
		
		for (final TransferEntity transferEntity : transferWindowDataService.getTransfers(openWindow, teamId)) {
			final TransferSummary modelTransfer = new TransferSummary();
			
			modelTransfer.setStatus(transferEntity.getStatus());
			
			if (isTeamSellingTeam(teamId, transferEntity)) {
				modelTransfer.setType(TransferType.SELL);
				modelTransfer.setCost(transferEntity.getAmount());

				setOpposingTeamForTransfer(transferEntity, modelTransfer);
				setTransferSummaryPlayers(transferEntity, modelTransfer);
				setTransferSummaryExchangedPlayers(transferEntity, modelTransfer);
			} else if (isTeamBuyingTeam(teamId, transferEntity)) {
				modelTransfer.setType(TransferType.BUY);
				modelTransfer.setCost(transferEntity.getAmount());
				modelTransfer.setOpposingTeam(transferEntity.getSellingTeam().getName());

				setTransferSummaryPlayers(transferEntity, modelTransfer);
				setTransferSummaryExchangedPlayers(transferEntity, modelTransfer);
			}
			
			transfers.add(modelTransfer);
		}
		
		return transfers;
	}
	
	@Override
	public void moveTransferWindowOntoAuction(final int leagueId) throws FantasyDraftIntegrationException {
		final TransferWindowEntity openWindow = transferWindowDataService.getOpenTransferWindow(leagueId);
		
		for (final TransferEntity transferEntity : openWindow.getTransfers()) {
			if (transferEntity.getStatus() == TransferStatus.CONFIRMED) {
				processTransferredPlayers(transferEntity);
				processExchangedPlayers(transferEntity);
			} else {
				processNonConfirmedTransfer(openWindow, transferEntity);
			}
		}
		
		transferWindowDataService.updateTransferWindow(openWindow);
	}

	@Override
	public LeagueTransferWindowSummary getLeagueTransferWindowSummary(final int leagueId) throws FantasyDraftIntegrationException {
		final LeagueTransferWindowSummary windowSummary = new LeagueTransferWindowSummary();
		final List<LeagueTransferSummary> transferSummaries = new ArrayList<LeagueTransferSummary>();
		final List<AuctionRoundResults> auctionSummaries = new ArrayList<AuctionRoundResults>();
		
		for (final TransferWindowEntity window : transferWindowDataService.getTransferWindows(leagueId)) {
			createTransfersSummary(transferSummaries, window);
			createAuctionsSummary(auctionSummaries, window);
		}
		
		windowSummary.setTransfers(transferSummaries);
		windowSummary.setAuctions(auctionSummaries);
		
		return windowSummary;
	}

	/**
	 * @param auctionSummaries
	 * @param window
	 */
	private void createAuctionsSummary(final List<AuctionRoundResults> auctionSummaries, final TransferWindowEntity window) {
		if (window.getStatus() == DraftRoundStatus.CLOSED) {
			final AuctionRoundResults auctionRoundResults = new AuctionRoundResults(window.getKey().getSequenceNumber());
			final List<AuctionPlayerResult> playerAuctionResults = new ArrayList<AuctionPlayerResult>();
			auctionRoundResults.setPlayerResults(playerAuctionResults);
			
			// Create a map of player id to BidEntity
			final Map<Integer, List<BidEntity>> playerBids = createPlayerBidLists(window);
			
			for (final List<BidEntity> bids : playerBids.values()) {
				final PlayerEntity playerEntity = bids.get(0).getPlayer();
				final Player modelPlayer = new Player();
				BeanUtils.copyProperties(playerEntity, modelPlayer);
				
				final AuctionPlayerResult playerResults = new AuctionPlayerResult(modelPlayer);
				playerAuctionResults.add(playerResults);
				
				Collections.sort(bids);
				
				final BidEntity firstBid = bids.get(0);
				
				if (bids.size() == 1) {
					final Team modelTeam = new Team(firstBid.getTeam().getName());
					final AuctionPlayerBid auctionBid = new AuctionPlayerBid(modelTeam, firstBid.getAmount());
					playerResults.setSuccessfulBid(auctionBid);
				} else {
					if (isMatchingHighestBid(bids, firstBid)) {
						processPlayerWithMatchingHighestBid(bids, playerResults, firstBid);
					} else {
						final Team modelTeam = new Team(firstBid.getTeam().getName());
						final AuctionPlayerBid auctionBid = new AuctionPlayerBid(modelTeam, firstBid.getAmount());
						playerResults.setSuccessfulBid(auctionBid);
						
						final List<AuctionPlayerBid> unsuccessfulBids = new ArrayList<AuctionPlayerBid>();
						for (int i = 1; i < bids.size(); i++) {
							final Team unsuccessfulModelTeam = new Team(bids.get(i).getTeam().getName());
							final AuctionPlayerBid unsuccesfulAuctionBid = new AuctionPlayerBid(unsuccessfulModelTeam, bids.get(i).getAmount());
							unsuccessfulBids.add(unsuccesfulAuctionBid);
						}
						
						playerResults.setUnsuccessfulBids(unsuccessfulBids);
					}
				}
			}
			
			auctionSummaries.add(auctionRoundResults);
		}
	}

	/**
	 * @param windowSummary
	 * @param transferSummaries
	 * @param openWindow
	 */
	private void createTransfersSummary(final List<LeagueTransferSummary> transferSummaries, final TransferWindowEntity openWindow) {
		for (final TransferEntity transferEntity : openWindow.getTransfers()) {
			final LeagueTransferSummary transferSummary = new LeagueTransferSummary();
			
			if (transferEntity.getBuyingTeam() == null) {
				transferSummary.setBuyingTeam("Pot");
			} else {
				transferSummary.setBuyingTeam(transferEntity.getBuyingTeam().getName());
			}
			
			transferSummary.setSellingTeam(transferEntity.getSellingTeam().getName());
			transferSummary.setCost(transferEntity.getAmount());
			
			final List<Player> transferredPlayers = new ArrayList<Player>();
			for (final TransferredPlayerEntity transferredPlayer : transferEntity.getPlayers()) {
				final Player player = new Player();
				
				player.setForename(transferredPlayer.getPlayer().getForename());
				player.setSurname(transferredPlayer.getPlayer().getSurname());
				
				transferredPlayers.add(player);
			}
			transferSummary.setPlayers(transferredPlayers);
			
			final List<Player> exchangedPlayers = new ArrayList<Player>();
			for (final ExchangedPlayerEntity exchangedPlayer : transferEntity.getExchangedPlayers()) {
				final Player player = new Player();
				
				player.setForename(exchangedPlayer.getPlayer().getForename());
				player.setSurname(exchangedPlayer.getPlayer().getSurname());
				
				exchangedPlayers.add(player);
			}
			transferSummary.setExchangedPlayers(exchangedPlayers);
			
			transferSummaries.add(transferSummary);
		}
	}
	
	@Override
	public void makeBids(final TeamBids teamBids) throws FantasyDraftIntegrationException {
		final TeamEntity teamEntity = teamDataService.getTeam(teamBids.getTeamId());
		final LeagueEntity leagueEntity = leagueDataService.getLeagueForTeam(teamBids.getTeamId());
		final TransferWindowEntity openTransferWindow = transferWindowDataService.getOpenTransferWindow(leagueEntity.getId());
		
		// Add the bids to the draft round
		final List<BidEntity> bidEntities = new ArrayList<BidEntity>();
		for (final Bid modelBid : teamBids.getBids()) {
			final PlayerEntity playerBiddedFor = playerDataService.getPlayer(modelBid.getPlayerId());
			final BidEntity entityBid = new BidEntity(teamEntity, playerBiddedFor, modelBid.getAmount());
			
			bidEntities.add(entityBid);
		}
		
		openTransferWindow.addBids(bidEntities);
		
		transferWindowDataService.updateTransferWindow(openTransferWindow);
	}
	
	/**
	 * @param openWindow
	 * @param transferEntity
	 * @throws FantasyDraftIntegrationException
	 */
	private void processNonConfirmedTransfer(final TransferWindowEntity openWindow, final TransferEntity transferEntity) throws FantasyDraftIntegrationException {
		openWindow.removeTransfer(transferEntity);
		
		// Set the selected player status' back to 'STILL_SELECTED'
		final TeamEntity sellingTeam = transferEntity.getSellingTeam();
		final TeamEntity buyingTeam = transferEntity.getBuyingTeam();
		
		for (final TransferredPlayerEntity transferredPlayer : transferEntity.getPlayers()) {
			final SelectedPlayerEntity selectedPlayer = getSelectedPlayerFromTeam(sellingTeam, transferredPlayer.getPlayer().getId());
			
			selectedPlayer.setStillSelected(SelectedPlayerStatus.STILL_SELECTED);
			
		}
		
		for (final ExchangedPlayerEntity exchangedPlayer : transferEntity.getExchangedPlayers()) {
			final SelectedPlayerEntity selectedPlayer = getSelectedPlayerFromTeam(buyingTeam, exchangedPlayer.getPlayer().getId());
			
			selectedPlayer.setStillSelected(SelectedPlayerStatus.STILL_SELECTED);
			
		}
		
		teamDataService.updateTeam(sellingTeam);
		teamDataService.updateTeam(buyingTeam);
	}

	/**
	 * @param transferEntity
	 * @throws FantasyDraftIntegrationException
	 */
	private void processTransferredPlayers(final TransferEntity transferEntity) throws FantasyDraftIntegrationException {
		for (final TransferredPlayerEntity transferredPlayer : transferEntity.getPlayers()) {
			final TeamEntity sellingTeam = transferEntity.getSellingTeam();
			
			final SelectedPlayerEntity selectedPlayer = getSelectedPlayerFromTeam(sellingTeam, transferredPlayer.getPlayer().getId());
			final PlayerEntity soldPlayer = selectedPlayer.getPlayer();
			
			setNewPlayerSelectionStatusAfterTransferOut(selectedPlayer);
			
			teamDataService.updateTeam(sellingTeam);
			
			if (transferEntity.getBuyingTeam() != null) {
				final TeamEntity buyingTeam = transferEntity.getBuyingTeam();
				final SelectedPlayerEntity boughtSelectedPlayer = new SelectedPlayerEntity(soldPlayer, transferEntity.getAmount());
				
				buyingTeam.addSelectedPlayers(Arrays.asList(boughtSelectedPlayer));
				
				teamDataService.updateTeam(buyingTeam);
			} else {
				// update player
				soldPlayer.setSelectionStatus(PlayerSelectionStatus.NOT_SELECTED);
				
				playerDataService.updatePlayer(soldPlayer);
			}
			
		}
	}
	
	/**
	 * @param transferEntity
	 * @throws FantasyDraftIntegrationException
	 */
	private void processExchangedPlayers(final TransferEntity transferEntity) throws FantasyDraftIntegrationException {
		for (final ExchangedPlayerEntity transferredPlayer : transferEntity.getExchangedPlayers()) {
			final TeamEntity sellingTeam = transferEntity.getSellingTeam();
			
			final SelectedPlayerEntity selectedPlayer = getSelectedPlayerFromTeam(sellingTeam, transferredPlayer.getPlayer().getId());
			final PlayerEntity soldPlayer = selectedPlayer.getPlayer();
			
			setNewPlayerSelectionStatusAfterTransferOut(selectedPlayer);
			
			teamDataService.updateTeam(sellingTeam);
			
			if (transferEntity.getBuyingTeam() != null) {
				final TeamEntity buyingTeam = transferEntity.getBuyingTeam();
				final SelectedPlayerEntity boughtSelectedPlayer = new SelectedPlayerEntity(soldPlayer, transferEntity.getAmount());
				
				buyingTeam.addSelectedPlayers(Arrays.asList(boughtSelectedPlayer));
				
				teamDataService.updateTeam(buyingTeam);
			} else {
				// update player
				soldPlayer.setSelectionStatus(PlayerSelectionStatus.NOT_SELECTED);
				
				playerDataService.updatePlayer(soldPlayer);
			}
			
		}
	}
	
	private void setNewPlayerSelectionStatusAfterTransferOut(final SelectedPlayerEntity selectedPlayer) {
		if (selectedPlayer.getSelectedPlayerStatus() == SelectedPlayerStatus.PENDING_SALE_TO_POT) {
			selectedPlayer.setStillSelected(SelectedPlayerStatus.SOLD_TO_POT);
		} else if (selectedPlayer.getSelectedPlayerStatus() == SelectedPlayerStatus.PENDING_TRANSFER_OUT) {
			selectedPlayer.setStillSelected(SelectedPlayerStatus.TRANSFERRED_OUT);
		}
		
		selectedPlayer.setSelectionStatus(null);
	}
	
	private SelectedPlayerEntity getSelectedPlayerFromTeam(final TeamEntity team, final int playerId) throws FantasyDraftIntegrationException {
		for (final SelectedPlayerEntity selectedPlayer : team.getSelectedPlayers()) {
			if (selectedPlayer.getPlayer().getId() == playerId) {
				return selectedPlayer;
			}
		}
		
		throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.PLAYER_DOES_NOT_EXIST);
	}
	
	/**
	 * @param teamId
	 * @param transferEntity
	 * @return
	 */
	private boolean isTeamSellingTeam(final int teamId, final TransferEntity transferEntity) {
		return transferEntity.getSellingTeam().getId() == teamId;
	}

	/**
	 * @param teamId
	 * @param transferEntity
	 * @return
	 */
	private boolean isTeamBuyingTeam(final int teamId, final TransferEntity transferEntity) {
		return transferEntity.getBuyingTeam() != null && transferEntity.getBuyingTeam().getId() == teamId;
	}

	/**
	 * @param transferEntity
	 * @param modelTransfer
	 */
	private void setTransferSummaryExchangedPlayers(final TransferEntity transferEntity, final TransferSummary modelTransfer) {
		final List<Player> exchangedPlayers = new ArrayList<Player>();
		for (final ExchangedPlayerEntity entity : transferEntity.getExchangedPlayers()) {
			final Player player = new Player();
			
			player.setForename(entity.getPlayer().getForename());
			player.setSurname(entity.getPlayer().getSurname());
			
			exchangedPlayers.add(player);
		}
		
		modelTransfer.setExchangedPlayers(exchangedPlayers);
	}

	/**
	 * @param transferEntity
	 * @param modelTransfer
	 */
	private void setTransferSummaryPlayers(final TransferEntity transferEntity, final TransferSummary modelTransfer) {
		final List<Player> players = new ArrayList<Player>();
		for (final TransferredPlayerEntity entity : transferEntity.getPlayers()) {
			final Player player = new Player();
			
			player.setForename(entity.getPlayer().getForename());
			player.setSurname(entity.getPlayer().getSurname());
			
			players.add(player);
		}
		
		modelTransfer.setPlayers(players);
	}

	/**
	 * @param transferEntity
	 * @param modelTransfer
	 */
	private void setOpposingTeamForTransfer(final TransferEntity transferEntity, final TransferSummary modelTransfer) {
		if (transferEntity.getBuyingTeam() == null) {
			modelTransfer.setOpposingTeam("Pot");
		} else {
			modelTransfer.setOpposingTeam(transferEntity.getBuyingTeam().getName());
		}
	}

	/**
	 * Update transfer window data to reflect a player has been sold to the pot.
	 * 
	 * @param sellingTeam Team selling the player to the pot.
	 * @param selectedPlayer Player who has been sold to the pot.
	 * @throws FantasyDraftIntegrationException
	 */
	private void updateTransferWindowDataForSaleToPot(final TeamEntity sellingTeam, final SelectedPlayerEntity selectedPlayer) throws FantasyDraftIntegrationException {
		final TransferredPlayerEntity transferredPlayer = new TransferredPlayerEntity(selectedPlayer.getPlayer());
		final TransferEntity transfer = new TransferEntity(Arrays.asList(transferredPlayer), sellingTeam, null, null, selectedPlayer.getCurrentSellToPotPrice());
		transfer.setStatus(TransferStatus.CONFIRMED);
		
		final LeagueEntity league = leagueDataService.getLeagueForTeam(sellingTeam.getId());
		final TransferWindowEntity openWindow = transferWindowDataService.getOpenTransferWindow(league.getId());
		
		openWindow.addTransfer(transfer);
		
		transferWindowDataService.updateTransferWindow(openWindow);
	}

	/**
	 * Update team data for a player that has been sold to the pot.
	 * 
	 * @param sellingTeam Team selling the player.
	 * @param selectedPlayer Player who has been sold to the pot.
	 */
	private void updateTeamDataForSaleToPot(final TeamEntity sellingTeam, final SelectedPlayerEntity selectedPlayer) {
		selectedPlayer.setStillSelected(SelectedPlayerStatus.PENDING_SALE_TO_POT);
		
		final BigDecimal newTeamRemainingBudget = sellingTeam.getRemainingBudget().add(selectedPlayer.getCurrentSellToPotPrice());
		sellingTeam.setRemainingBudget(newTeamRemainingBudget);
		
		teamDataService.updateTeam(sellingTeam);
	}

	/**
	 * Update the details for the team who has bought the players.
	 * 
	 * @param transfer Transfer details.
	 * @param buyingTeam Team buying the players.
	 * @param exchangedPlayers Any players the buying team is exchanging.
	 */
	private void updateBuyingTeamData(final Transfer transfer, final TeamEntity buyingTeam, final List<ExchangedPlayerEntity> exchangedPlayers, final TransferStatus transferStatus) {
		if (transferStatus == TransferStatus.CONFIRMED) {
			final BigDecimal newRemainingBudget = buyingTeam.getRemainingBudget().subtract(transfer.getAmount());
			buyingTeam.setRemainingBudget(newRemainingBudget);
		}
		
		updateExchangedPlayerSelectionStatus(buyingTeam, exchangedPlayers);
		
		teamDataService.updateTeam(buyingTeam);
	}
	
	/**
	 * Update the details for the team who has bought the players.
	 * 
	 * @param transfer Transfer details.
	 * @param sellingTeam Team buying the players.
	 * @param soldPlayers Any players the buying team is exchanging.
	 */
	private void updateSellingTeamData(final Transfer transfer, final TeamEntity sellingTeam, final List<TransferredPlayerEntity> soldPlayers, final TransferStatus transferStatus) {
		if (transferStatus == TransferStatus.CONFIRMED) {
			final BigDecimal newRemainingBudget = sellingTeam.getRemainingBudget().add(transfer.getAmount());
			sellingTeam.setRemainingBudget(newRemainingBudget);
		}
		
		updateSoldPlayerSelectionStatus(sellingTeam, soldPlayers);
		
		teamDataService.updateTeam(sellingTeam);
	}
	
	/**
	 * Update the selection status of players who are transferred out to record the pending transfer out.
	 * 
	 * @param sellingTeam The team selling the players.
	 * @param players The players being sold.
	 */
	private void updateSoldPlayerSelectionStatus(final TeamEntity sellingTeam, final List<TransferredPlayerEntity> players) {
		for (final TransferredPlayerEntity transferredOutPlayer : players) {
			for (final SelectedPlayerEntity selectedPlayer : sellingTeam.getSelectedPlayers()) {
				if (selectedPlayer.getPlayer().getId() == transferredOutPlayer.getPlayer().getId()) {
					selectedPlayer.setStillSelected(SelectedPlayerStatus.PENDING_TRANSFER_OUT);
				}
			}
		}
	}
	
	/**
	 * Update the selection status of players who are transferred out to record the pending transfer out.
	 * 
	 * @param sellingTeam The team selling the players.
	 * @param players The players being sold.
	 */
	private void updateExchangedPlayerSelectionStatus(final TeamEntity sellingTeam, final List<ExchangedPlayerEntity> players) {
		for (final ExchangedPlayerEntity transferredOutPlayer : players) {
			for (final SelectedPlayerEntity selectedPlayer : sellingTeam.getSelectedPlayers()) {
				if (selectedPlayer.getPlayer().getId() == transferredOutPlayer.getPlayer().getId()) {
					selectedPlayer.setStillSelected(SelectedPlayerStatus.PENDING_TRANSFER_OUT);
				}
			}
		}
	}
	
	/**
	 * Set the status of a new transfer and any equivalent transfers to that one.
	 * 
	 * @param transfer New transfer model object containing all data on the transfer.
	 * @param openWindow The open transfer window object of the league in question.
	 * @param newTransfer New transfer entity object to set the status of. 
	 */
	private TransferStatus setTransferStatus(final Transfer transfer, final TransferWindowEntity openWindow, final TransferEntity newTransfer) {
		try {
			final TransferEntity equivalentTransfer = TransferUtils.getEquivalentTransfer(transfer, openWindow.getTransfers());
			equivalentTransfer.setStatus(TransferStatus.CONFIRMED);
			return TransferStatus.CONFIRMED;
			
		} catch (final FantasyDraftIntegrationException e) {
			newTransfer.setStatus(TransferStatus.PENDING);
			openWindow.addTransfer(newTransfer);
			return TransferStatus.PENDING;
		}
	}

	/**
	 * Create a list of the players who are being exchanged in a transfer.
	 * 
	 * @param transfer {@link Transfer} object containing transfer details.
	 * @return List of {@link PlayerEntity} objects who are who are being exchanged in a transfer.
	 * @throws FantasyDraftIntegrationException
	 */
	private List<ExchangedPlayerEntity> getPlayersBeingExchangedInTransfer(final Transfer transfer) throws FantasyDraftIntegrationException {
		final List<ExchangedPlayerEntity> exchangedPlayers = new ArrayList<ExchangedPlayerEntity>();
		
		if (transfer.getExchangedPlayers() != null) {
			for (final int playerId : transfer.getExchangedPlayers()) {
				exchangedPlayers.add(new ExchangedPlayerEntity(playerDataService.getPlayer(playerId)));
			}
		}
		
		return exchangedPlayers;
	}

	/**
	 * Create a list of the players who are the subject of a transfer.
	 * 
	 * @param transfer {@link Transfer} object containing transfer details.
	 * @return List of {@link PlayerEntity} objects who are part of the transfer.
	 * @throws FantasyDraftIntegrationException
	 */
	private List<TransferredPlayerEntity> getPlayerEntitiesInvolvedInTransfer(final Transfer transfer) throws FantasyDraftIntegrationException {
		final List<TransferredPlayerEntity> players = new ArrayList<TransferredPlayerEntity>();
		
		for (final int playerId : transfer.getPlayers()) {
			players.add(new TransferredPlayerEntity(playerDataService.getPlayer(playerId)));
		}
		return players;
	}
}
