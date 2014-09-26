/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.player.types.Position;

/**
 * Entity class representing a fantasy draft player.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "Player")
public class PlayerEntity implements Serializable {	

	private static final long serialVersionUID = 6584040856373261900L;

	@Id
	private int id;
	
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
	@Enumerated(EnumType.STRING)
	private PlayerSelectionStatus selectionStatus;
	
	@Column
	private int totalPoints;
	
	@Column
	private BigDecimal currentPrice;

	public PlayerEntity() {
	}

	public PlayerEntity(final int id, final String forename, final String surname, final String team,
			final Position position, final int totalPoints) {
		this.id = id;
		this.forename = forename;
		this.surname = surname;
		this.team = team;
		this.position = position;
		this.totalPoints = totalPoints;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
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
	 * @return the selectionStatus
	 */
	public PlayerSelectionStatus getSelectionStatus() {
		return selectionStatus;
	}

	/**
	 * @param selectionStatus the selectionStatus to set
	 */
	public void setSelectionStatus(final PlayerSelectionStatus selectionStatus) {
		this.selectionStatus = selectionStatus;
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

	/**
	 * @return the currentPrice
	 */
	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param currentPrice the currentPrice to set
	 */
	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}
	
}
