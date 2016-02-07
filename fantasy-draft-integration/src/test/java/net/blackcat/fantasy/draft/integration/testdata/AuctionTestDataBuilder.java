/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.Auction;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionPhaseStatus;

/**
 * @author Chris Hern
 * 
 */
public class AuctionTestDataBuilder {

    private List<AuctionPhase> phases;
    private boolean isOnlyPhaseClosed;

    private AuctionTestDataBuilder() {
        phases = new ArrayList<AuctionPhase>();
    }

    public static AuctionTestDataBuilder anAuction() {

        return new AuctionTestDataBuilder();
    }

    public static AuctionTestDataBuilder anAuctionWithClosedPhase() {

        final AuctionTestDataBuilder builder = new AuctionTestDataBuilder();

        builder.isOnlyPhaseClosed = true;

        return builder;
    }

    public AuctionTestDataBuilder withPhase(final AuctionPhase phase) {

        phases.add(phase);

        return this;
    }

    public Auction build() {

        final Auction auction = new Auction();

        if (isOnlyPhaseClosed) {
            auction.getPhases().get(0).setStatus(AuctionPhaseStatus.CLOSED);
        }

        for (final AuctionPhase phase : phases) {
            auction.createNewPhase();

            if (!phase.isOpen()) {
                auction.getPhases().get(auction.getPhases().size() - 1).setStatus(AuctionPhaseStatus.CLOSED);
            }
        }

        return auction;
    }
}