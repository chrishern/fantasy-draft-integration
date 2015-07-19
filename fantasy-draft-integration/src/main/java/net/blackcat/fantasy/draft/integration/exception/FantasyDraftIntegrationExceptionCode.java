/**
 * 
 */
package net.blackcat.fantasy.draft.integration.exception;

/**
 * Enum storing the different exception codes that can be thrown within the fantasy draft integration.
 * 
 * @author Chris
 * 
 */
public enum FantasyDraftIntegrationExceptionCode {

    // @formatter:off
    
    /**
     * Player codes.
     */
    PLAYER_NOT_FOUND,

    /**
     * User codes.
     */
    USER_NOT_FOUND, 
    USER_ALREADY_EXISTS,

    /**
     * League codes.
     */
    LEAGUE_NOT_FOUND,
    OPEN_AUCTION_NOT_FOUND,
    OPEN_AUCTION_PHASE_NOT_FOUND,

    /**
     * Team codes.
     */
    TEAM_NOT_FOUND;
    
    // @formatter:on
}
