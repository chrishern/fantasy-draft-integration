/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO for transferring the result of a bid for a player in an auction.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerAuctionBidResultDto implements Serializable {

    private static final long serialVersionUID = -3334124876810102525L;

    private String forename;
    private String surname;
    private BidResultDetailDto successfulBid;
    private List<BidResultDetailDto> unsuccessfulBids;

    public PlayerAuctionBidResultDto(final String forename, final String surname) {
        this.forename = forename;
        this.surname = surname;
    }

    /**
     * Add a successful bid to the result.
     * 
     * @param teamName
     *            Name of the team with the successful bid.
     * @param amount
     *            Amount of the successful bid.
     */
    public void withSuccessfulBid(final String teamName, final BigDecimal amount) {
        this.successfulBid = new BidResultDetailDto(teamName, amount);
    }

    /**
     * Add an unsuccessful bid to the result.
     * 
     * @param teamName
     *            Name of the team with the unsuccessful bid.
     * @param amount
     *            Amount of the unsuccessful bid.
     */
    public void withUnsuccessfulBid(final String teamName, final BigDecimal amount) {

        if (this.unsuccessfulBids == null) {
            this.unsuccessfulBids = new ArrayList<BidResultDetailDto>();
        }

        this.unsuccessfulBids.add(new BidResultDetailDto(teamName, amount));
        Collections.sort(this.unsuccessfulBids);
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
     * @return the successfulBid
     */
    public BidResultDetailDto getSuccessfulBid() {
        return successfulBid;
    }

    /**
     * @return the unsuccessfulBids
     */
    public List<BidResultDetailDto> getUnsuccessfulBids() {
        return unsuccessfulBids;
    }
}
