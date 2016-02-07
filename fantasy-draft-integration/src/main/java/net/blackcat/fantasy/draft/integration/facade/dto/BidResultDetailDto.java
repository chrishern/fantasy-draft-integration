/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.math.BigDecimal;

/**
 * Capture bid result detail.
 * 
 * @author Chris Hern
 * 
 */
public class BidResultDetailDto implements Comparable<BidResultDetailDto> {

    private String teamName;
    private BigDecimal amount;

    public BidResultDetailDto(final String teamName, final BigDecimal amount) {
        this.teamName = teamName;
        this.amount = amount;
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

    @Override
    public int compareTo(final BidResultDetailDto bidResultToCompare) {
        return bidResultToCompare.getAmount().compareTo(this.amount);
    }
}
