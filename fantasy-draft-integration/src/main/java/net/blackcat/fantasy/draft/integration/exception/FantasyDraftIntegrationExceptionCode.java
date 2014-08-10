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

	/**
	 * Draft round codes.
	 */
	OPEN_DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE,
	OPEN_DRAFT_ROUND_DOES_NOT_EXIST_FOR_LEAGUE,
	DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE,
	
	/**
	 * League codes.
	 */
	LEAGUE_DOES_NOT_EXIST,
	
	/**
	 * Team codes.
	 */
	TEAM_DOES_NOT_EXIST,
	
	/**
	 * Manager codes.
	 */
	MANAGER_DOES_NOT_EXIST,
	
	/**
	 * Player codes.
	 */
	PLAYER_DOES_NOT_EXIST;
}
