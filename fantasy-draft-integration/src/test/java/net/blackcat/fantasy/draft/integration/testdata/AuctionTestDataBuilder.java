/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.Auction;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;

/**
 * @author Chris Hern
 * 
 */
public class AuctionTestDataBuilder {

    private List<AuctionPhase> phases;

    private AuctionTestDataBuilder() {
        phases = new ArrayList<AuctionPhase>();
    }

    public static AuctionTestDataBuilder anAuction() {

        return new AuctionTestDataBuilder();
    }

    public static AuctionTestDataBuilder anAuctionWithOpenPhase() {

        final AuctionTestDataBuilder builder = new AuctionTestDataBuilder();
        final AuctionPhase openAuctionPhase = AuctionPhaseTestDataBuilder.anOpenAuctionPhase().build();

        builder.phases.add(openAuctionPhase);

        return builder;
    }

    public AuctionTestDataBuilder withPhase(final AuctionPhase phase) {

        phases.add(phase);

        return this;
    }

    public Auction build() {

        final Auction auction = new Auction();

        for (final AuctionPhase phase : phases) {
            auction.createNewPhase();
        }

        return auction;
    }
}