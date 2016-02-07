/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for transferring data on a transfer bid.
 * 
 * @author Chris
 *
 */
public class TransferBidDto implements Serializable {

	private static final long serialVersionUID = -6362879321794110888L;

	private int buyingTeam;
	private int sellingTeam;
	private int selectedPlayerSubjectOfBid;
	private BigDecimal amount;
	
	/**
	 * @return the buyingTeam
	 */
	public int getBuyingTeam() {
		return buyingTeam;
	}
	
	/**
	 * @return the sellingTeam
	 */
	public int getSellingTeam() {
		return sellingTeam;
	}

	/**
	 * @return the selectedPlayerSubjectOfBid
	 */
	public int getSelectedPlayerSubjectOfBid() {
		return selectedPlayerSubjectOfBid;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
}
