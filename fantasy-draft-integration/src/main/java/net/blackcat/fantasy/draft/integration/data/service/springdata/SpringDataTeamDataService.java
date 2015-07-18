/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Spring Data implementation of the {@link SpringDataTeamDataService}.
 * 
 * @author Chris Hern
 * 
 */
@Repository
public class SpringDataTeamDataService implements TeamDataService {

    private TeamRepository repository;

    @Autowired
    public SpringDataTeamDataService(final TeamRepository repository) {

        this.repository = repository;
    }

    @Override
    public Team getTeam(final int teamId) throws FantasyDraftIntegrationException {

        final Team team = repository.findOne(teamId);

        if (team == null) {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_NOT_FOUND);
        }

        return team;
    }

}
