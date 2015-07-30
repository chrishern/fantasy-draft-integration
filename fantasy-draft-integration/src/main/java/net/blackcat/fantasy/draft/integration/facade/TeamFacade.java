/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import net.blackcat.fantasy.draft.integration.converter.ConverterService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.dto.SquadDto;
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
}
