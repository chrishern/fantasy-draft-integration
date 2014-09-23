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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entity representing a transfer that has been agreed in a transfer window.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "PendingTransfer")
public class PendingTransferEntity implements Serializable {

	private static final long serialVersionUID = 5955391693448404160L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@OneToOne
	private SelectedPlayerEntity player;
	
	@OneToOne
	private TeamEntity sellingTeam;
	
	@OneToOne
	private TeamEntity buyingTeam;
	
	@Column(nullable = false)
	private BigDecimal amount;

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
	 * @return the player
	 */
	public SelectedPlayerEntity getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(SelectedPlayerEntity player) {
		this.player = player;
	}

	/**
	 * @return the sellingTeam
	 */
	public TeamEntity getSellingTeam() {
		return sellingTeam;
	}

	/**
	 * @param sellingTeam the sellingTeam to set
	 */
	public void setSellingTeam(TeamEntity sellingTeam) {
		this.sellingTeam = sellingTeam;
	}

	/**
	 * @return the buyingTeam
	 */
	public TeamEntity getBuyingTeam() {
		return buyingTeam;
	}

	/**
	 * @param buyingTeam the buyingTeam to set
	 */
	public void setBuyingTeam(TeamEntity buyingTeam) {
		this.buyingTeam = buyingTeam;
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
}
