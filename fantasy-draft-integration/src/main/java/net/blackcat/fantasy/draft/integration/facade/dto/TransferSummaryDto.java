/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for summarising a transfer.
 * 
 * @author Chris
 *
 */
public class TransferSummaryDto implements Serializable {

	private static final long serialVersionUID = 7474450570210534378L;

	private String forename;
	private String surname;
	private String buyingTeam;
	private String sellingTeam;
	private BigDecimal amount;
	
	public TransferSummaryDto(final String forename, final String surname, final String buyingTeam, final String sellingTeam, final BigDecimal amount) {
		this.forename = forename;
		this.surname = surname;
		this.buyingTeam = buyingTeam;
		this.sellingTeam = sellingTeam;
		this.amount = amount;
	}

	/**
	 * @return the forename
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @return the buyingTeam
	 */
	public String getBuyingTeam() {
		return buyingTeam;
	}

	/**
	 * @return the sellingTeam
	 */
	public String getSellingTeam() {
		return sellingTeam;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
}
