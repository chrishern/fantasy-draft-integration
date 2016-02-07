/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Data transfer object for transferring bids made during an auction.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionBidsDto implements Serializable {

    private static final long serialVersionUID = -9078229895952605108L;

    private int teamId;
    private List<BidDto> bids;

    /*
     * For JSON Serialization
     */
    public AuctionBidsDto() {
    	
    }
    
    public AuctionBidsDto(final int teamId, final List<BidDto> bids) {

        this.teamId = teamId;
        this.bids = bids;
    }

    /**
     * @return the teamId
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * @return the bids
     */
    public List<BidDto> getBids() {
        return bids;
    }
}
