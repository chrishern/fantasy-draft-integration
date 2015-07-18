/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Data transfer object used for transferring a bid made during an auction.
 * 
 * @author Chris Hern
 * 
 */
public class BidDto implements Serializable {

    private static final long serialVersionUID = 8495379494439782673L;

    private int playerId;
    private BigDecimal amount;

    public BidDto(final int playerId, final BigDecimal amount) {

        this.playerId = playerId;
        this.amount = amount;
    }

    /**
     * @return the playerId
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }
}
