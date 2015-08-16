/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model.types.player;

/**
 * Enum storing the starting team status of selected players.
 * 
 * @author Chris Hern
 * 
 */
public enum StartingTeamStatus {

	CAPTAIN(true),
	VICE_CAPTAIN(true),
	PICKED(true),
	SUB_1(false),
	SUB_2(false),
	SUB_3(false),
	SUB_4(false),
	SUB_5(false);
	
	private boolean isStartingPosition;
	
	private StartingTeamStatus(final boolean isStartingPosition) {
		this.isStartingPosition = isStartingPosition;
	}

	/**
	 * @return the isStartingPosition
	 */
	public boolean isStartingPosition() {
		return isStartingPosition;
	}
	
	/**
	 * Return whether this position is a substitute position or not.
	 * @return True if this is a substitute, false if not.
	 */
	public boolean isSubstitutePosition() {
		return !isStartingPosition;
	}
}
