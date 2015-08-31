package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SellPlayerToPotDto implements Serializable {

	private static final long serialVersionUID = -489934370530981763L;

	private String managerEmailAddress;
	private int selectedPlayerId;
	private BigDecimal amount;
	
	public SellPlayerToPotDto() {
	}
	
	public SellPlayerToPotDto(final int selectedPlayerId, final BigDecimal amount) {
		this.selectedPlayerId = selectedPlayerId;
		this.amount = amount;
	}

	/**
	 * @return the managerEmailAddress
	 */
	public String getManagerEmailAddress() {
		return managerEmailAddress;
	}

	/**
	 * @param managerEmailAddress the managerEmailAddress to set
	 */
	public void setManagerEmailAddress(String managerEmailAddress) {
		this.managerEmailAddress = managerEmailAddress;
	}

	/**
	 * @return the selectedPlayerId
	 */
	public int getSelectedPlayerId() {
		return selectedPlayerId;
	}
	
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
}
