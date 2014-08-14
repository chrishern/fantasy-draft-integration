/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.DraftRoundDataService;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.TeamFacade;
import net.blackcat.fantasy.draft.player.SelectedPlayer;
import net.blackcat.fantasy.draft.team.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of team related operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "teamFacade")
public class TeamFacadeImpl implements TeamFacade {

	@Autowired
	@Qualifier(value = "teamDataServiceJpa")
	private TeamDataService teamDataService;
	
	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Autowired
	@Qualifier(value = "draftRoundDataServiceJpa")
	private DraftRoundDataService draftRoundDataService;
	
	@Override
	public void createTeam(final String teamName) {
		teamDataService.createTeam(teamName);
	}

	@Override
	public Team getTeam(final String teamName) throws FantasyDraftIntegrationException {
		final TeamEntity teamEntity = teamDataService.getTeam(teamName);
		
		final Team modelTeam = new Team(teamEntity.getName());
		
		final List<SelectedPlayer> selectedModelPlayers = new ArrayList<SelectedPlayer>();
		for (final SelectedPlayerEntity selectedPlayer : teamEntity.getSelectedPlayers()) {
			selectedModelPlayers.add(selectedPlayer.toModelSelectedPlayer());
		}
		
		modelTeam.setSelectedPlayers(selectedModelPlayers);
		
		final LeagueEntity leagueForTeam = leagueDataService.getLeagueForTeam(teamEntity.getId());
		
		try {
			draftRoundDataService.getOpenDraftRound(leagueForTeam.getId());
			modelTeam.setOpenDraftRound(true);
		} catch (final FantasyDraftIntegrationException e) {
			modelTeam.setOpenDraftRound(false);
		}
		
		return modelTeam;
	}

}
