/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import net.blackcat.fantasy.draft.bidround.types.BiddingPhaseStatus;
import net.blackcat.fantasy.draft.integration.entity.key.DraftRoundKey;

/**
 * Class representing a draft round (i.e. initial auction or transfer window).
 * 
 * @author Chris
 *
 */
public class DraftRoundEntity implements Serializable {

	private static final long serialVersionUID = 1986039206171397038L;

	@EmbeddedId
	private DraftRoundKey key;
	
	@Column
	@Enumerated(EnumType.STRING)
	private BiddingPhaseStatus status;
	
	@OneToMany
	private List<BidEntity> bids;
}
