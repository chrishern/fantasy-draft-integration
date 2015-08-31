/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;

import net.blackcat.fantasy.draft.integration.model.types.player.Position;

/**
 * @author Chris
 *
 */
public class PlayerSummaryDto implements Serializable, Comparable<PlayerSummaryDto> {

	private static final long serialVersionUID = -2552035257248025842L;

	private int selectedPlayerId;
	private String forename;
	private String surname;
	private Position position;
	
	public PlayerSummaryDto(final int selectedPlayerId, final String forename, final String surname, final Position position) {
		this.selectedPlayerId = selectedPlayerId;
		this.forename = forename;
		this.surname = surname;
		this.position = position;
	}

	/**
	 * @return the selectedPlayerId
	 */
	public int getSelectedPlayerId() {
		return selectedPlayerId;
	}

	/**
	 * @return the forename
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public int compareTo(PlayerSummaryDto objectToCompareTo) {
		return this.position.compareTo(objectToCompareTo.position);
	}
}
