/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionPhaseStatus;

/**
 * Class for creating instances of {@link AuctionPhase} objects to use in unit tests.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionPhaseTestDataBuilder {

    private final static int DEFAULT_SEQUENCE_NUMBER = 1;

    private AuctionPhaseStatus status;
    private List<Bid> bids;

    private AuctionPhaseTestDataBuilder(final AuctionPhaseStatus status) {
        this.status = status;
        this.bids = new ArrayList<Bid>();
    }

    public static AuctionPhaseTestDataBuilder anOpenAuctionPhase() {

        return new AuctionPhaseTestDataBuilder(AuctionPhaseStatus.OPEN);
    }

    public static AuctionPhaseTestDataBuilder aClosedAuctionPhase() {

        return new AuctionPhaseTestDataBuilder(AuctionPhaseStatus.CLOSED);
    }

    public AuctionPhaseTestDataBuilder withBid(final Player player, final Team team, final BigDecimal amount) {

        final Bid bid = new Bid(team, player, amount);

        bids.add(bid);

        return this;
    }

    public AuctionPhase build() {

        final AuctionPhase auctionPhase = new AuctionPhase(DEFAULT_SEQUENCE_NUMBER);

        auctionPhase.setStatus(status);
        auctionPhase.addBids(bids);

        return auctionPhase;
    }
}
