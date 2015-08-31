/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import net.blackcat.fantasy.draft.integration.model.types.transfer.TransferStatus;

/**
 * Model object representing a transfer within the game.
 * 
 * @author Chris
 *
 */
@Entity
public class Transfer implements Serializable {

	private static final long serialVersionUID = -9098603855913257822L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	@ManyToOne
	private Team buyingTeam;
	
	@ManyToOne
	private Team sellingTeam;
	
	@ManyToOne
	private SelectedPlayer playerBiddedFor;
	
	@Column
	@Enumerated(EnumType.STRING)
	private TransferStatus status;
	
	@Column
	private BigDecimal amount;
	
	/*
	 * Used for Hibernate database mapping.
	 */
	@SuppressWarnings("unused")
	private Transfer() {
		
	}

	public Transfer(final Team buyingTeam, final Team sellingTeam, final SelectedPlayer playerBiddedFor, final BigDecimal amount) {
		this.buyingTeam = buyingTeam;
		this.sellingTeam = sellingTeam;
		this.playerBiddedFor = playerBiddedFor;
		this.amount = amount;
		this.status = TransferStatus.PENDING;
	}

	/**
	 * Mark this transfer as accepted.
	 */
	public void acceptBid() {
		this.status = TransferStatus.CONFIRMED;
		this.playerBiddedFor.transferBidAccepted();
		this.sellingTeam.agreedToSellPlayer(amount);
		this.buyingTeam.transferBidAccepted(amount);
	}
	
	/**
	 * Mark this transfer as rejected.
	 */
	public void rejectBid() {
		this.status = TransferStatus.REJECTED;
	}
	
	/**
	 * Return whether this transfer has been accepted or not.
	 * 
	 * @return True/false whether this transfer has been accepted or not.
	 */
	public boolean isAccepted() {
		return this.status == TransferStatus.CONFIRMED;
	}
	
	/**
	 * Return whether this transfer has been rejected or not.
	 * 
	 * @return True/false whether this transfer has been rejected or not.
	 */
	public boolean isRejected() {
		return this.status == TransferStatus.REJECTED;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the buyingTeam
	 */
	public Team getBuyingTeam() {
		return buyingTeam;
	}

	/**
	 * @return the sellingTeam
	 */
	public Team getSellingTeam() {
		return sellingTeam;
	}

	/**
	 * @return the playerBiddedFor
	 */
	public SelectedPlayer getPlayerBiddedFor() {
		return playerBiddedFor;
	}

	/**
	 * @return the status
	 */
	public TransferStatus getStatus() {
		return status;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
}
