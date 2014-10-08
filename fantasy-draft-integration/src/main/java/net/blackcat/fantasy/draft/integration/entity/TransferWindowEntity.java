/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.blackcat.fantasy.draft.integration.entity.key.DraftRoundKey;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;

/**
 * Entity representing a transfer window within the game.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "TransferWindow")
public class TransferWindowEntity implements Serializable {

	private static final long serialVersionUID = -153821465936710219L;

	@EmbeddedId
	private DraftRoundKey key;
	
	@Column
	@Enumerated(EnumType.STRING)
	private DraftRoundStatus status;
	
	@ManyToOne
	@MapsId("leagueId")
	private LeagueEntity league;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<BidEntity> auctionRoundBids;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<TransferEntity> transfers;
	
	public TransferWindowEntity() {
		
	}
	
	public TransferWindowEntity(final int sequenceNumber, final LeagueEntity league) {
		
		final DraftRoundKey key = new DraftRoundKey(DraftRoundPhase.TRANSFER_WINDOW, sequenceNumber, league.getId());
		
		this.key = key;
		this.league = league;
		this.status = DraftRoundStatus.OPEN;
	}

	/**
	 * @return the key
	 */
	public DraftRoundKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(DraftRoundKey key) {
		this.key = key;
	}

	/**
	 * @return the status
	 */
	public DraftRoundStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(DraftRoundStatus status) {
		this.status = status;
	}

	/**
	 * @return the league
	 */
	public LeagueEntity getLeague() {
		return league;
	}

	/**
	 * @param league the league to set
	 */
	public void setLeague(LeagueEntity league) {
		this.league = league;
	}

	/**
	 * @return the auctionRoundBids
	 */
	public List<BidEntity> getAuctionRoundBids() {
		return auctionRoundBids;
	}

	/**
	 * @return the pendingTransfers
	 */
	public List<TransferEntity> getTransfers() {
		return transfers;
	}
	
	/**
	 * Add a new list of bids to this transfer window.
	 * 
	 * @param newBids New bids to add to the entity.
	 */
	public void addBids(final List<BidEntity> newBids) {
		if (auctionRoundBids == null) {
			auctionRoundBids = new ArrayList<BidEntity>();
		}
		
		auctionRoundBids.addAll(newBids);
	}
	
	/**
	 * Add a new transfer to the draft round.
	 * 
	 * @param newTransfer Transfer to add.
	 */
	public void addTransfer(final TransferEntity newTransfer) {
		if (transfers == null) {
			transfers = new ArrayList<TransferEntity>();
		}
		
		transfers.add(newTransfer);
	}
	
	public void removeTransfer(final TransferEntity transfer) {
		transfers.remove(transfer);
	}
}
