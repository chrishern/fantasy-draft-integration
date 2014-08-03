/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.facade.PlayerFacade;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.player.types.Position;

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

	@Autowired
	@Qualifier(value = "playerDataServiceJpa")
	private PlayerDataService playerDataService;
	
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
}
