/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for transferring auction results.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionPhaseResultsDto implements Serializable, Comparable<AuctionPhaseResultsDto> {

    private static final long serialVersionUID = 6357636867535675303L;

    private int sequenceNumber;
    private List<PlayerAuctionBidResultDto> playerBidResults;

    public AuctionPhaseResultsDto(final int sequenceNumber) {

        this.sequenceNumber = sequenceNumber;
        this.playerBidResults = new ArrayList<PlayerAuctionBidResultDto>();
    }

    /**
     * Add a player result to this object.
     * 
     * @param playerResult
     *            Player result object to add to this.
     */
    public void withPlayerResult(final PlayerAuctionBidResultDto playerResult) {

        this.playerBidResults.add(playerResult);
    }

    /**
     * @return the leagueId
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @return the playerBidResults
     */
    public List<PlayerAuctionBidResultDto> getPlayerBidResults() {
        return playerBidResults;
    }

    @Override
    public int compareTo(final AuctionPhaseResultsDto phaseToCompareTo) {
        return Integer.valueOf(sequenceNumber).compareTo(Integer.valueOf(phaseToCompareTo.getSequenceNumber()));
    }
}
