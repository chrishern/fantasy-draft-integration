/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.LoggedInUser;
import net.blackcat.fantasy.draft.integration.data.service.DraftRoundDataService;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.ManagerDataService;
import net.blackcat.fantasy.draft.integration.entity.BidEntity;
import net.blackcat.fantasy.draft.integration.entity.DraftRoundEntity;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.ManagerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.ManagerFacade;
import net.blackcat.fantasy.draft.manager.Manager;
import net.blackcat.fantasy.draft.player.SelectedPlayer;
import net.blackcat.fantasy.draft.team.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of manager facade operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "managerFacade")
public class ManagerFacadeImpl implements ManagerFacade {

	@Autowired
	@Qualifier(value = "managerDataServiceJpa")
	private ManagerDataService managerDataService;
	
	@Autowired
	@Qualifier(value = "draftRoundDataServiceJpa")
	private DraftRoundDataService draftRoundDataService;
	
	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Override
	public Manager getManager(final String emailAddress) throws FantasyDraftIntegrationException {
		final ManagerEntity managerEntity = managerDataService.getManager(emailAddress);
		
		final Manager managerModel = new Manager();
		final Team teamModel = new Team(managerEntity.getTeam().getName());
		teamModel.setId(managerEntity.getTeam().getId());
		
		final List<SelectedPlayer> selectedPlayers = getSelectedPlayers(managerEntity);
		teamModel.setSelectedPlayers(selectedPlayers);
		teamModel.setStatus(managerEntity.getTeam().getStatus());
		
		final LeagueEntity league = leagueDataService.getLeagueForTeam(managerEntity.getTeam().getId());
		
		try {
			final DraftRoundEntity openDraftRound = draftRoundDataService.getOpenDraftRound(league.getId());
			teamModel.setOpenDraftRound(true);
			
			for (final BidEntity bid : openDraftRound.getBids()) {
				if (bid.getTeam().getId() == teamModel.getId()) {
					teamModel.setMadeBidsInOpenDraftRound(true);
					break;
				}
			}
		} catch (final FantasyDraftIntegrationException e) {
			teamModel.setOpenDraftRound(false);
		}
		
		managerModel.setTeam(teamModel);
		managerModel.setEmailAddress(managerEntity.getEmailAddress());
		managerModel.setForename(managerEntity.getForename());
		managerModel.setSurname(managerEntity.getSurname());
		
		return managerModel;
	}
	
	@Override
	public LoggedInUser getLoggedInUser(final String emailAddress) throws FantasyDraftIntegrationException {
		final ManagerEntity managerEntity = managerDataService.getManager(emailAddress);
		final LeagueEntity league = leagueDataService.getLeagueForTeam(managerEntity.getTeam().getId());
		
		return new LoggedInUser(managerEntity.getTeam().getId(), league.getId());
	}

	/**
	 * Create a list of selected players associated with a given manager.
	 * 
	 * @param managerEntity Manager to get the selected players for.
	 * @return List of selected players.
	 */
	private List<SelectedPlayer> getSelectedPlayers(final ManagerEntity managerEntity) {
		final List<SelectedPlayer> selectedPlayers = new ArrayList<SelectedPlayer>();
		
		for (final SelectedPlayerEntity selectedPlayerEntity : managerEntity.getTeam().getSelectedPlayers()) {
			selectedPlayers.add(selectedPlayerEntity.toModelSelectedPlayer());
		}
		return selectedPlayers;
	}
}
