/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity.key;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import net.blackcat.fantasy.draft.round.types.DraftRoundPhase;

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
	private DraftRoundPhase biddingPhase;
	
	@Column
	private int sequenceNumber;
	
	@Column
	private int leagueId;

	public DraftRoundKey() {
	}

	public DraftRoundKey(final DraftRoundPhase biddingPhase, final int sequenceNumber,
			final int leagueId) {
		this.biddingPhase = biddingPhase;
		this.sequenceNumber = sequenceNumber;
		this.leagueId = leagueId;
	}

	/**
	 * @return the biddingPhase
	 */
	public DraftRoundPhase getBiddingPhase() {
		return biddingPhase;
	}

	/**
	 * @param biddingPhase the biddingPhase to set
	 */
	public void setBiddingPhase(DraftRoundPhase biddingPhase) {
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
	public int getLeague() {
		return leagueId;
	}

	/**
	 * @param leagueId the league to set
	 */
	public void setLeague(int leagueId) {
		this.leagueId = leagueId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((biddingPhase == null) ? 0 : biddingPhase.hashCode());
		result = prime * result + leagueId;
		result = prime * result + sequenceNumber;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DraftRoundKey)) {
			return false;
		}
		DraftRoundKey other = (DraftRoundKey) obj;
		if (biddingPhase != other.biddingPhase) {
			return false;
		}
		if (leagueId != other.leagueId) {
			return false;
		}
		if (sequenceNumber != other.sequenceNumber) {
			return false;
		}
		return true;
	}
}
