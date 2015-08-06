/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.converter.ConverterService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.dto.SquadDto;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade providing team information.
 * 
 * @author Chris Hern
 * 
 */
@Component
@Transactional
public class TeamFacade {

    private TeamDataService teamDataService;
    private ConverterService converterService;

    @Autowired
    public TeamFacade(final TeamDataService teamDataService, final ConverterService converterService) {

        this.teamDataService = teamDataService;
        this.converterService = converterService;
    }

    /**
     * Get squad details for the team owned by a given manager.
     * 
     * @param managerEmailAddress
     *            Email address of the manager to get the squad for.
     * @return Squad for the given manager.
     * @throws FantasyDraftIntegrationException
     */
    public SquadDto getSquadDetails(final String managerEmailAddress) throws FantasyDraftIntegrationException {

        final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);

        return converterService.convert(teamForManager, SquadDto.class);
    }
    
    /**
     * Get details of all squads in a manager's league.
     * 
     * @param managerEmailAddress Email address of the manager to get the squads for.
     * @return List of squads in the manager's league.
     * @throws FantasyDraftIntegrationException
     */
    public List<SquadDto> getSquadDetailsForLeague(final String managerEmailAddress) throws FantasyDraftIntegrationException {
    	
    	final Team teamForManager = teamDataService.getTeamForManager(managerEmailAddress);
    	final League league = teamForManager.getLeague();
    	
    	final List<SquadDto> squads = new ArrayList<SquadDto>();
    	
    	for (final Team team : league.getTeams()) {
    		squads.add(converterService.convert(team, SquadDto.class));
    	}
    	
    	return squads;
    }
}
