/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity.key;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import net.blackcat.fantasy.draft.bidround.types.BiddingPhase;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;

/**
 * Key for the draft round entity.
 * 
 * @author Chris
 *
 */
@Embeddable
public class DraftRoundKey implements Serializable {

	private static final long serialVersionUID = 53683889670938216L;

	@Column
	@Enumerated(EnumType.STRING)
	private BiddingPhase biddingPhase;
	
	@Column
	private int sequenceNumber;
	
	@Column
	private LeagueEntity league;

	public DraftRoundKey() {
	}

	public DraftRoundKey(final BiddingPhase biddingPhase, final int sequenceNumber,
			final LeagueEntity league) {
		this.biddingPhase = biddingPhase;
		this.sequenceNumber = sequenceNumber;
		this.league = league;
	}

	/**
	 * @return the biddingPhase
	 */
	public BiddingPhase getBiddingPhase() {
		return biddingPhase;
	}

	/**
	 * @param biddingPhase the biddingPhase to set
	 */
	public void setBiddingPhase(BiddingPhase biddingPhase) {
		this.biddingPhase = biddingPhase;
	}

	/**
	 * @return the sequenceNumber
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
}
