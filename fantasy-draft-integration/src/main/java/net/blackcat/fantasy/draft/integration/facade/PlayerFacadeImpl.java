/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.player.Player;

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
		playerDataService.addPlayers(players);
	}

	@Override
	public List<Player> getPlayers() {
		return playerDataService.getPlayers();
	}

}