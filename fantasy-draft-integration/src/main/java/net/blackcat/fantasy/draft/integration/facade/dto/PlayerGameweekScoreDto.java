/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;

/**
 * Transfer object for transferring gameweek score data for a player.
 * 
 * @author Chris
 *
 */
public class PlayerGameweekScoreDto implements Serializable {

	private static final long serialVersionUID = 5869913161141176263L;

	private int playerId;
	private int minutesPlayed;
	private int score;
	
	public PlayerGameweekScoreDto(final int playerId, final int minutesPlayed, final int score) {
		this.playerId = playerId;
		this.minutesPlayed = minutesPlayed;
		this.score = score;
	}
	
	/**
	 * Return whether this player played in this gameweek or not.
	 *  
	 * @return True if they played in this gameweek, false if not.
	 */
	public boolean hasPlayed() {
		return minutesPlayed > 0;
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @return the minutesPlayed
	 */
	public int getMinutesPlayed() {
		return minutesPlayed;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	
	public void setMinutesPlayed(final int minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}
}
