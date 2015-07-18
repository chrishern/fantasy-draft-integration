/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.Auction;
import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Team;

/**
 * @author Chris Hern
 * 
 */
public class AuctionTestDataBuilder {

    private List<Bid> bids;

    private AuctionTestDataBuilder() {
        bids = new ArrayList<Bid>();
    }

    public static AuctionTestDataBuilder anAuction() {

        return new AuctionTestDataBuilder();
    }

    public AuctionTestDataBuilder withBid(final Player player, final Team team, final BigDecimal amount) {

        final Bid bid = new Bid(team, player, amount);

        bids.add(bid);

        return this;
    }

    public Auction build() {

        final Auction auction = new Auction();
        auction.addBids(bids);

        return auction;
    }
}