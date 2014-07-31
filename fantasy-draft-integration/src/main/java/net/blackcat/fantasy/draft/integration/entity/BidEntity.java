/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity class representing a bid that is made during a fantasy draft auction.
 * 
 * @author Chris
 *
 */
public class BidEntity implements Serializable {

	private static final long serialVersionUID = 5307258911998818485L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	private TeamEntity team;
	
	@Column
	private PlayerEntity player;
	
	@Column
	private BigDecimal amount;
	
	@Column
	private boolean successful;
}
