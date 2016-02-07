/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for passing information on team transfer status.
 * 
 * @author Chris Hern
 * 
 */
public class TeamAuctionStatusDto implements Serializable {

    private static final long serialVersionUID = -4277177344401229262L;

    private boolean bidsSubmittedInCurrentWindow;
    private boolean openTransferWindowForTeam;
    private BigDecimal remainingBudget;

    public TeamAuctionStatusDto(final BigDecimal remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public void withBidsSubmittedInCurrentWindow() {
        this.bidsSubmittedInCurrentWindow = true;
    }

    public void withOpenTransferWindow() {
        this.openTransferWindowForTeam = true;
    }

    /**
     * @return the bidsSubmittedInCurrentWindow
     */
    public boolean isBidsSubmittedInCurrentWindow() {
        return bidsSubmittedInCurrentWindow;
    }

    /**
     * @return the openTransferWindowForTeam
     */
    public boolean isOpenTransferWindowForTeam() {
        return openTransferWindowForTeam;
    }

    /**
     * @return the remainingBudget
     */
    public BigDecimal getRemainingBudget() {
        return remainingBudget;
    }
}
