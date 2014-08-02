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

/**
 * Entity class representing a bid that is made during a fantasy draft auction.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "Bid")
public class BidEntity implements Serializable {

	private static final long serialVersionUID = 5307258911998818485L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private TeamEntity team;
	
	@ManyToOne
	private PlayerEntity player;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Column(nullable = true)
	private boolean successful;

	public BidEntity() {
	}

	public BidEntity(final TeamEntity team, final PlayerEntity player, final BigDecimal amount) {
		this.team = team;
		this.player = player;
		this.amount = amount;
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
	 * @return the team
	 */
	public TeamEntity getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(TeamEntity team) {
		this.team = team;
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
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the successful
	 */
	public boolean isSuccessful() {
		return successful;
	}

	/**
	 * @param successful the successful to set
	 */
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
}
