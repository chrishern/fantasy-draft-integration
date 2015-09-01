/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO summarising a player who was sold to the pot.
 * 
 * @author Chris
 *
 */
public class PotSaleSummaryDto implements Serializable {

	private static final long serialVersionUID = 754735445426501538L;

	private String forename;
	private String surname;
	private String teamName;
	private BigDecimal amount;
	
	public PotSaleSummaryDto(final String forename, final String surname, final String teamName, final BigDecimal amount) {
		
		this.forename = forename;
		this.surname = surname;
		this.teamName = teamName;
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
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
}
