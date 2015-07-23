/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.League;

/**
 * Defines operations for accessing {@link League} data from the back end data store.
 * 
 * @author Chris Hern
 * 
 */
public interface LeagueDataService {

    /**
     * Add a new league to the system.
     * 
     * @param league
     *            League to add.
     */
    void addLeague(League league);

    /**
     * Get a {@link League} with the given ID.
     * 
     * @param leagueId
     *            ID of the league to retrieve.
     * @return Corresponding {@link League} object.
     * @throws FantasyDraftIntegrationException
     *             If a {@link League} with the given ID does not exist.
     */
    League getLeague(int leagueId) throws FantasyDraftIntegrationException;

    /**
     * Update an existing {@link League} within the system.
     * 
     * @param league
     *            Updated {@link League} object to save.
     * @throws FantasyDraftIntegrationException
     *             If a {@link League} with the matching ID does not exist in the system.
     */
    void updateLeague(League league) throws FantasyDraftIntegrationException;

    /**
     * Determine if an open auction phase exists for a given {@link League}.
     * 
     * @param league
     *            League to get the check the open auction phase for.
     * @return True if an open auction phase exists. False otherwise.
     */
    boolean doesOpenAuctionPhaseExist(League league);

    /**
     * Get the currently open auction phase for a given {@link League}.
     * 
     * @param league
     *            League to get the open auction phase for.
     * @return Open {@link AuctionPhase}.
     * @throws FantasyDraftIntegrationException
     *             If the given league does not have an open auction phase.
     */
    AuctionPhase getOpenAuctionPhase(League league) throws FantasyDraftIntegrationException;
}
