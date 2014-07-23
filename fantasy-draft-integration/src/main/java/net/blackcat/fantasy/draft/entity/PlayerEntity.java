/**
 * 
 */
package net.blackcat.fantasy.draft.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import net.blackcat.fantasy.draft.player.types.Position;

/**
 * Entity class representing a fantasy draft player.
 * 
 * @author Chris
 *
 */
@Entity(name = "Player")
public class PlayerEntity implements Serializable {	

	private static final long serialVersionUID = 6584040856373261900L;

	@Id
	private long id;
	
	@Column
	private String forename;
	
	@Column
	private String surname;
	
	@Column
	private String team;

	@Column
	@Enumerated(EnumType.STRING)
	private Position position;

	@Column
	private boolean selected;
	
	@Column
	private int totalPoints;

	public PlayerEntity() {
	}

	public PlayerEntity(final long id, final String forename, final String surname, final String team,
			final Position position, final boolean selected, final int totalPoints) {
		this.id = id;
		this.forename = forename;
		this.surname = surname;
		this.team = team;
		this.position = position;
		this.selected = selected;
		this.totalPoints = totalPoints;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the forename
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * @param forename the forename to set
	 */
	public void setForename(String forename) {
		this.forename = forename;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the totalPoints
	 */
	public int getTotalPoints() {
		return totalPoints;
	}

	/**
	 * @param totalPoints the totalPoints to set
	 */
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	
}
