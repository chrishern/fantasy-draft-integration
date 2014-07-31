/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity.key;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
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
	private LeagueEntity league;

	public DraftRoundKey() {
	}

	public DraftRoundKey(final DraftRoundPhase biddingPhase, final int sequenceNumber,
			final LeagueEntity league) {
		this.biddingPhase = biddingPhase;
		this.sequenceNumber = sequenceNumber;
		this.league = league;
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
	public LeagueEntity getLeague() {
		return league;
	}

	/**
	 * @param league the league to set
	 */
	public void setLeague(LeagueEntity league) {
		this.league = league;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((biddingPhase == null) ? 0 : biddingPhase.hashCode());
		result = prime * result + ((league == null) ? 0 : league.hashCode());
		result = prime * result + sequenceNumber;
		return result;
	}

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
		if (league == null) {
			if (other.league != null) {
				return false;
			}
		} else if (!league.equals(other.league)) {
			return false;
		}
		if (sequenceNumber != other.sequenceNumber) {
			return false;
		}
		return true;
	}
}
