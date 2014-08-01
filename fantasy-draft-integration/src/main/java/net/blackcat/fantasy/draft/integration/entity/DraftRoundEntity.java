/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
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

import net.blackcat.fantasy.draft.integration.entity.key.DraftRoundKey;
import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;
import net.blackcat.fantasy.draft.round.types.DraftRoundStatus;

/**
 * Class representing a draft round (i.e. initial auction or transfer window).
 * 
 * @author Chris
 *
 */
@Entity(name = "DraftRound")
public class DraftRoundEntity implements Serializable {

	private static final long serialVersionUID = 1986039206171397038L;

	@EmbeddedId
	private DraftRoundKey key;
	
	@Column
	@Enumerated(EnumType.STRING)
	private DraftRoundStatus status;
	
	@ManyToOne
	@MapsId("leagueId")
	private LeagueEntity league;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<BidEntity> bids;
	
	public DraftRoundEntity() {
		
	}
	
	public DraftRoundEntity(final DraftRoundPhase biddingPhase, final int sequenceNumber,
		final LeagueEntity league, final DraftRoundStatus status) {
		
		final DraftRoundKey key = new DraftRoundKey(biddingPhase, sequenceNumber, league.getId());
		
		this.key = key;
		this.status = status;
	}
	
	/**
	 * Add a new list of bids to this draft.
	 * 
	 * @param newBids New bids to add to the entity.
	 */
	public void addBids(final List<BidEntity> newBids) {
		bids.addAll(newBids);
	}
}
