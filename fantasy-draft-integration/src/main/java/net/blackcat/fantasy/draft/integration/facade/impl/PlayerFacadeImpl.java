/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.PlayerFacade;
import net.blackcat.fantasy.draft.player.FplCostPlayer;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.player.types.Position;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of player facade operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "playerFacade")
public class PlayerFacadeImpl implements PlayerFacade {

	private static final String PRICE_CHANGE_MULTIPLIER = "0.05";
	private static final String FPL_PRICE_CHANGE_FACTOR = "0.1";

	@Autowired
	@Qualifier(value = "playerDataServiceJpa")
	private PlayerDataService playerDataService;
	
	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Autowired
	@Qualifier(value = "teamDataServiceJpa")
	private TeamDataService teamDataService;
	
	@Override
	public void addPlayers(final List<Player> players) {
		final List<PlayerEntity> entityPlayers = new ArrayList<PlayerEntity>();
		
		for (final Player modelPlayer : players) {
			final PlayerEntity entityPlayer = new PlayerEntity();
			
			BeanUtils.copyProperties(modelPlayer, entityPlayer);
			entityPlayer.setSelectionStatus(PlayerSelectionStatus.NOT_SELECTED);
			
			entityPlayers.add(entityPlayer);
		}
		
		playerDataService.addPlayers(entityPlayers);
	}

	@Override
	public List<Player> getPlayers() {
		final List<Player> modelPlayers = new ArrayList<Player>();
		
		for (final PlayerEntity entityPlayer : playerDataService.getPlayers()) {
			final Player modelPlayer = new Player();
			BeanUtils.copyProperties(entityPlayer, modelPlayer);
			modelPlayers.add(modelPlayer);
		}
		
		return modelPlayers;
	}

	@Override
	public List<Player> getPlayers(final Position position) {
		final List<Player> modelPlayers = new ArrayList<Player>();
		
		for (final PlayerEntity entityPlayer : playerDataService.getPlayers(position)) {
			final Player modelPlayer = new Player();
			BeanUtils.copyProperties(entityPlayer, modelPlayer);
			modelPlayers.add(modelPlayer);
		}
		
		return modelPlayers;
	}
	
	@Override
	public List<Player> getPlayers(final Position position, final PlayerSelectionStatus selectionStatus) {
		final List<Player> modelPlayers = new ArrayList<Player>();
		
		for (final PlayerEntity entityPlayer : playerDataService.getPlayers(position, selectionStatus)) {
			final Player modelPlayer = new Player();
			BeanUtils.copyProperties(entityPlayer, modelPlayer);
			modelPlayers.add(modelPlayer);
		}
		
		return modelPlayers;
	}
	
	@Override
	public List<Player> getPlayers(final Position position, final PlayerSelectionStatus selectionStatus, final int teamId) throws FantasyDraftIntegrationException {
		final TeamEntity team = teamDataService.getTeam(teamId);
		final List<Integer> previouslySelectedPlayers = new ArrayList<Integer>();
		
		for (final SelectedPlayerEntity selectedPlayer : team.getSelectedPlayers()) {
			if (selectedPlayer.getSelectedPlayerStatus() != SelectedPlayerStatus.STILL_SELECTED) {
				previouslySelectedPlayers.add(selectedPlayer.getPlayer().getId());
			}
		}
		
		final List<Player> modelPlayers = new ArrayList<Player>();
		
		for (final PlayerEntity entityPlayer : playerDataService.getPlayers(position, selectionStatus)) {
			if (!previouslySelectedPlayers.contains(entityPlayer.getId())) {
				final Player modelPlayer = new Player();
				BeanUtils.copyProperties(entityPlayer, modelPlayer);
				modelPlayers.add(modelPlayer);
			}
		}
		
		return modelPlayers;
	}

	@Override
	public void updatePlayersCurrentPrice(final Map<Integer, FplCostPlayer> fplPlayerData) {
		
		updatePlayerCurrentPriceData(fplPlayerData);
		updateSelectedPlayerResalePriceData(fplPlayerData);
	}

	/**
	 * Update the current resale price of all selected players in teams.
	 * 
	 * @param fplPlayerData Player data obtained from the FPL.
	 */
	private void updateSelectedPlayerResalePriceData(final Map<Integer, FplCostPlayer> fplPlayerData) {
		for (final LeagueEntity league : leagueDataService.getLeagues()) {
			for (final TeamEntity team : league.getTeams()) {
				for (final SelectedPlayerEntity selectedPlayer : team.getSelectedPlayers()) {
					if (selectedPlayer.getSelectedPlayerStatus() == SelectedPlayerStatus.STILL_SELECTED) {
						final BigDecimal purchasePrice = selectedPlayer.getCost();
						final BigDecimal priceAdjustmentFactor = purchasePrice.multiply(new BigDecimal(PRICE_CHANGE_MULTIPLIER));
						
						final BigDecimal currentPlayerFplPrice = fplPlayerData.get(selectedPlayer.getPlayer().getId()).getCurrentCost();
						
						final BigDecimal fplPriceChange = currentPlayerFplPrice.subtract(selectedPlayer.getFplCostAtPurchase());
						final BigDecimal amountPriceHasChanged = fplPriceChange.divide(new BigDecimal(FPL_PRICE_CHANGE_FACTOR));
						
						final BigDecimal draftPriceChange = priceAdjustmentFactor.multiply(amountPriceHasChanged);
						
						final BigDecimal currentResalePrice = purchasePrice.add(draftPriceChange);
						
						selectedPlayer.setCurrentSellToPotPrice(currentResalePrice);
					}
				}
				
				teamDataService.updateTeam(team);
			}
		}
	}

	/**
	 * Update the current player of each Player entity.
	 * 
	 * @param fplPlayerData Player data obtained from the FPL.
	 */
	private void updatePlayerCurrentPriceData(final Map<Integer, FplCostPlayer> fplPlayerData) {
		for (final PlayerEntity player : playerDataService.getPlayers()) {
			final FplCostPlayer fplCostPlayer = fplPlayerData.get(player.getId());
			
			player.setCurrentPrice(fplCostPlayer.getCurrentCost());
			
			playerDataService.updatePlayer(player);
		}
	}
}
