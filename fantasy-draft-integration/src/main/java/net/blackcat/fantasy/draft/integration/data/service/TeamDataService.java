/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.model.Team;

/**
 * Data operations for the {@link Team} object.
 * 
 * @author Chris Hern
 * 
 */
public interface TeamDataService {

    /**
     * Get a {@link Team} with a given ID.
     * 
     * @param teamId
     *            ID of the team to get.
     * @return {@link Team} with corresponding ID.
     * @throws FantasyDraftIntegrationException
     *             If a {@link Team} with the given ID does not exist.
     */
    Team getTeam(int teamId) throws FantasyDraftIntegrationException;
}
