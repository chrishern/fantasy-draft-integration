/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.auction.AuctionPlayerBid;
import net.blackcat.fantasy.draft.auction.AuctionPlayerResult;
import net.blackcat.fantasy.draft.auction.AuctionRoundResults;
import net.blackcat.fantasy.draft.integration.data.service.DraftRoundDataService;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.BidEntity;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.DraftRoundFacade;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.round.Bid;
import net.blackcat.fantasy.draft.round.TeamBids;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;
import net.blackcat.fantasy.draft.team.Team;
import net.blackcat.fantasy.draft.team.types.TeamStatus;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of draft round facade operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "draftRoundFacade")
public class DraftRoundFacadeImpl implements DraftRoundFacade {

	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Autowired
	@Qualifier(value = "draftRoundDataServiceJpa")
	private DraftRoundDataService draftRoundDataService;
	
	@Autowired
	@Qualifier(value = "teamDataServiceJpa")
	private TeamDataService teamDataService;
	
	@Autowired
	@Qualifier(value = "playerDataServiceJpa")
	private PlayerDataService playerDataService;
	
	@Override
	public void startAuctionPhase(final int leagueId, final int phase) throws FantasyDraftIntegrationException {
		// Get the LeagueEntity for the given id
		final LeagueEntity league = leagueDataService.getLeague(leagueId);
		
		// Create the phase
		final DraftRoundEntity draftRound = new DraftRoundEntity(DraftRoundPhase.AUCTION, phase, league);
		draftRoundDataService.createDraftRound(draftRound);
	}

	@Override
	public AuctionRoundResults closeAuctionPhase(final int leagueId) throws FantasyDraftIntegrationException {
		// Get the open auction phase for this league
		final DraftRoundEntity openDraftRound = draftRoundDataService.getOpenDraftRound(leagueId);
		
		final AuctionRoundResults auctionRoundResults = new AuctionRoundResults(openDraftRound.getKey().getSequenceNumber());
		final List<AuctionPlayerResult> playerAuctionResults = new ArrayList<AuctionPlayerResult>();
		auctionRoundResults.setPlayerResults(playerAuctionResults);

		// Create a map of player id to BidEntity
		final Map<Integer, List<BidEntity>> playerBids = createPlayerBidLists(openDraftRound);
		
		// Go through each entity in the map and decide who had the winning bid, creating the AuctionPlayerResults as we go
		processAuctionRoundResults(playerAuctionResults, playerBids);
		
		// Update the draft round entity to show that it is CLOSED and also set the successful status on each BidEntity
		openDraftRound.setStatus(DraftRoundStatus.CLOSED);
		draftRoundDataService.updateDraftRound(openDraftRound);
		
		// Move all the bids of successful players into the appropriate team.
		moveSuccessfulPlayerBidsToTeam(openDraftRound);
		
		return auctionRoundResults;
	}
	
	@Override
	public List<AuctionRoundResults> getAuctionRoundResults(final int leagueId) {
		final List<AuctionRoundResults> allAuctions = new ArrayList<AuctionRoundResults>();
		final List<DraftRoundEntity> draftRounds = draftRoundDataService.getDraftRounds(leagueId);
		
		for (final DraftRoundEntity draftRound : draftRounds) {
			final AuctionRoundResults auctionRoundResults = new AuctionRoundResults(draftRound.getKey().getSequenceNumber());
			final List<AuctionPlayerResult> playerAuctionResults = new ArrayList<AuctionPlayerResult>();
			auctionRoundResults.setPlayerResults(playerAuctionResults);
			
			// Create a map of player id to BidEntity
			final Map<Integer, List<BidEntity>> playerBids = createPlayerBidLists(draftRound);
			
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
			
			allAuctions.add(auctionRoundResults);
		}
		
		return allAuctions;
	}
	
	@Override
	public void makeBids(final TeamBids bids) throws FantasyDraftIntegrationException {

		final TeamEntity teamEntity = teamDataService.getTeam(bids.getTeamId());
		final LeagueEntity leagueEntity = leagueDataService.getLeagueForTeam(bids.getTeamId());
		final DraftRoundEntity openDraftRound = draftRoundDataService.getOpenDraftRound(leagueEntity.getId());
		
		// Add the bids to the draft round
		final List<BidEntity> bidEntities = new ArrayList<BidEntity>();
		for (final Bid modelBid : bids.getBids()) {
			final PlayerEntity playerBiddedFor = playerDataService.getPlayer(modelBid.getPlayerId());
			final BidEntity entityBid = new BidEntity(teamEntity, playerBiddedFor, modelBid.getAmount());
			
			bidEntities.add(entityBid);
		}
		
		draftRoundDataService.addBids(openDraftRound, bidEntities);
	}

	/**
	 * Move all of the successful bids for players to be selected players within the appropriate team.
	 * 
	 * @param draftRound Draft round containing all bid results.
	 */
	private void moveSuccessfulPlayerBidsToTeam(final DraftRoundEntity draftRound) {
		final Map<TeamEntity, List<BidEntity>> selectedPlayersMap = new HashMap<TeamEntity, List<BidEntity>>();
		
		buildSuccessfulBidsForTeams(draftRound, selectedPlayersMap);
		updateTeamsWithSuccessfulBids(selectedPlayersMap);
	}
	
	/**
	 * Build up a map of the teams who had successful auction bids and their successful bids.
	 * 
	 * @param draftRound Draft round results.
	 * @param selectedPlayersMap Map of teams who had successful auction bids and their successful bids.
	 */
	private void buildSuccessfulBidsForTeams(final DraftRoundEntity draftRound, final Map<TeamEntity, List<BidEntity>> selectedPlayersMap) {
		for (final BidEntity bid : draftRound.getBids()) {
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
			
			for (final BidEntity successfulBid : selectedPlayersMap.get(teamToUpdate)) {
				final SelectedPlayerEntity selectedPlayer = new SelectedPlayerEntity(successfulBid.getPlayer());
				selectedPlayer.setCost(successfulBid.getAmount());
				selectedPlayers.add(selectedPlayer);
			}
			
			teamToUpdate.addSelectedPlayers(selectedPlayers);
			
			if (teamToUpdate.getSelectedPlayers().size() == 16) {
				teamToUpdate.setStatus(TeamStatus.COMPLETE);
			}
			
			teamDataService.updateTeam(teamToUpdate);
		}
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
		firstBid.getPlayer().setTeamsWhoCanBid(null);
		
		final List<AuctionPlayerBid> unsuccessfulBids = new ArrayList<AuctionPlayerBid>();
		final List<AuctionPlayerBid> matchingHighBids = new ArrayList<AuctionPlayerBid>();
		
		for (final BidEntity playerBid : bids) {
			playerBid.setSuccessful(false);

			final Team modelTeam = new Team(playerBid.getTeam().getName());
			final AuctionPlayerBid auctionBid = new AuctionPlayerBid(modelTeam, playerBid.getAmount());

			if (playerBid.getAmount().equals(highestBid)) {
				// TODO Add back in??
				//playerBid.getPlayer().setSelectionStatus(PlayerSelectionStatus.RESTRICTED_SELECTION);
				playerBid.getPlayer().addTeamWhoCanBid(playerBid.getTeam());
				
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
		successfulBid.getPlayer().setTeamsWhoCanBid(null);
		
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
	private Map<Integer, List<BidEntity>> createPlayerBidLists(final DraftRoundEntity draftRound) {
		final Map<Integer, List<BidEntity>> playerBids = new HashMap<Integer, List<BidEntity>>();
		
		for (final BidEntity bid : draftRound.getBids()) {
			List<BidEntity> bidList = playerBids.get(bid.getPlayer().getId());
			
			if (bidList == null) {
				bidList = new ArrayList<BidEntity>();
			} 
			
			bidList.add(bid);
			playerBids.put(bid.getPlayer().getId(), bidList);
		}
		
		return playerBids;
	}
}
