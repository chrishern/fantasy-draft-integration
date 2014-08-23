/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.blackcat.fantasy.draft.player.SelectedPlayer;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;

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
	
	@Column
	@Enumerated(EnumType.STRING)
	private SelectedPlayerStatus selectionStatus;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<GameweekScoreEntity> gameweekScores;
	
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
	 * @return the gameweekScores
	 */
	public List<GameweekScoreEntity> getGameweekScores() {
		return gameweekScores;
	}

	/**
	 * @param gameweekScores the gameweekScores to set
	 */
	public void setGameweekScores(List<GameweekScoreEntity> gameweekScores) {
		this.gameweekScores = gameweekScores;
	}

	/**
	 * @return the selectionStatus
	 */
	public SelectedPlayerStatus getSelectionStatus() {
		return selectionStatus;
	}

	/**
	 * @param selectionStatus the selectionStatus to set
	 */
	public void setSelectionStatus(SelectedPlayerStatus selectionStatus) {
		this.selectionStatus = selectionStatus;
	}

	/**
	 * Add a new gameweek score to the selected player.
	 * 
	 * @param gameweekScore Score to add to the player.
	 */
	public void addGameweekScore(final GameweekScoreEntity gameweekScore) {
		if (gameweekScores == null) {
			gameweekScores = new ArrayList<GameweekScoreEntity>();
		}
		
		gameweekScores.add(gameweekScore);
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
