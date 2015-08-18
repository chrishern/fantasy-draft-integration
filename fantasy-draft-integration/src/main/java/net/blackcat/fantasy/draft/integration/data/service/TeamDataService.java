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

    /**
     * Get the {@link Team} associated with the email address of a manager.
     * 
     * @param managerEmailAddress Email address of the manager to the get the team for.
     * @return {@link Team} for the manager.
     * @throws FantasyDraftIntegrationException If a {@link Team} can't be found for the given email address.
     */
    Team getTeamForManager(String managerEmailAddress) throws FantasyDraftIntegrationException;

    /**
     * Update the given {@link Team} in the backend.
     * 
     * @param updatedTeam
     *            {@link Team} to update with updated data.
     */
    void updateTeam(Team updatedTeam) throws FantasyDraftIntegrationException;
}
