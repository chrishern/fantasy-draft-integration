/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for transferring details on a transfer.
 * 
 * @author Chris
 *
 */
public class TransferDetailDto implements Serializable {

	private static final long serialVersionUID = -8234457023931606631L;

	private int transferId;
	private String forename;
	private String surname;
	private String buyingTeam;
	private String sellingTeam;
	private BigDecimal amount;
	
	public TransferDetailDto(final int transferId, final String forename, final String surname, final String buyingTeam, final String sellingTeam, final BigDecimal amount) {
		this.transferId = transferId;
		this.forename = forename;
		this.surname = surname;
		this.buyingTeam = buyingTeam;
		this.sellingTeam = sellingTeam;
		this.amount = amount;
	}

	/**
	 * @return the transferId
	 */
	public int getTransferId() {
		return transferId;
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
