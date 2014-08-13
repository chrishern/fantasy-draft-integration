/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.blackcat.fantasy.draft.player.SelectedPlayer;

/**
 * Entity representing a player who has been picked by a team within the game.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "SelectedPlayer")
public class SelectedPlayerEntity implements Serializable {

	private static final long serialVersionUID = 3472489202239807734L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	private PlayerEntity player;
	
	@Column
	private int pointsScored;
	
	@Column(nullable = false)
	private boolean stillSelected;
	
	@Column
	private BigDecimal cost;

	public SelectedPlayerEntity() {
	}

	public SelectedPlayerEntity(final PlayerEntity player) {
		this.player = player;
		this.stillSelected = true;
	}

	/**
	 * @return the player
	 */
	public PlayerEntity getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(PlayerEntity player) {
		this.player = player;
	}

	/**
	 * @return the pointsScored
	 */
	public int getPointsScored() {
		return pointsScored;
	}

	/**
	 * @param pointsScored the pointsScored to set
	 */
	public void setPointsScored(int pointsScored) {
		this.pointsScored = pointsScored;
	}

	/**
	 * @return the stillSelected
	 */
	public boolean isStillSelected() {
		return stillSelected;
	}

	/**
	 * @param stillSelected the stillSelected to set
	 */
	public void setStillSelected(boolean stillSelected) {
		this.stillSelected = stillSelected;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the cost
	 */
	public BigDecimal getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	/**
	 * Convert this object into an equivalent model {@link SelectedPlayer}. 
	 * 
	 * @return Converted {@link SelectedPlayer}.
	 */
	public SelectedPlayer toModelSelectedPlayer() {
		final SelectedPlayer selectedPlayerModel = new SelectedPlayer();
		
		selectedPlayerModel.setCost(this.cost);
		selectedPlayerModel.setForename(this.player.getForename());
		selectedPlayerModel.setId(this.id);
		selectedPlayerModel.setPointsScored(this.pointsScored);
		selectedPlayerModel.setSurname(this.player.getSurname());
		selectedPlayerModel.setTeam(this.player.getTeam());
		selectedPlayerModel.setPosition(this.player.getPosition());
		
		return selectedPlayerModel;
	}
}
