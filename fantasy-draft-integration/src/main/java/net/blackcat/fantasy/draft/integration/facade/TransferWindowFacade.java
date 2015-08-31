package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.dto.SellPlayerToPotDto;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.TransferWindow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TransferWindowFacade {

	private TeamDataService teamDataService;
	private LeagueDataService leagueDataService;
	
	@Autowired
	public TransferWindowFacade(final TeamDataService teamDataService, final LeagueDataService leagueDataService) {
		this.teamDataService = teamDataService;
		this.leagueDataService = leagueDataService;
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
}
