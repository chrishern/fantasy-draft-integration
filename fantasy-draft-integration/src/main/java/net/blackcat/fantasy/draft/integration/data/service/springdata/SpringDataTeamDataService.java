/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.data.specification.TeamSpecification;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.repository.TeamRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
    
    @Override
	public List<Team> getTeams() {

    	final List<Team> teams = new ArrayList<Team>();
    	final Iterable<Team> allTeams = repository.findAll();
    	CollectionUtils.addAll(teams, allTeams.iterator());
    	
    	return teams;
	}

    @Override
    public void updateTeam(final Team updatedTeam) throws FantasyDraftIntegrationException {

        if (repository.exists(updatedTeam.getId())) {
            repository.save(updatedTeam);
        } else {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_NOT_FOUND);
        }
    }

    @Override
    public Team getTeamForManager(final String managerEmailAddress) throws FantasyDraftIntegrationException {

        final Specification<Team> managerSpecification = TeamSpecification.managerIsEqualTo(managerEmailAddress);

        final Team team = repository.findOne(managerSpecification);

        if (team == null) {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.TEAM_NOT_FOUND);
        }

        return team;
    }
}
