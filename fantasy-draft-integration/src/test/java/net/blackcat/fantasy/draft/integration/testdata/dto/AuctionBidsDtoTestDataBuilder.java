package net.blackcat.fantasy.draft.integration.testdata.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.facade.dto.AuctionBidsDto;
import net.blackcat.fantasy.draft.integration.facade.dto.BidDto;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

/**
 * Builder for building instances of {@link AuctionBidsDto} to use in unit tests.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionBidsDtoTestDataBuilder {

    private static final int TEAM_ID = TestDataConstants.TEAM_ONE_ID;
    private final List<BidDto> bids;

    private AuctionBidsDtoTestDataBuilder() {
        bids = new ArrayList<BidDto>();
    }

    public static AuctionBidsDtoTestDataBuilder anAuctionBidsDto() {

        return new AuctionBidsDtoTestDataBuilder();
    }

    public AuctionBidsDtoTestDataBuilder withBid(final int playerId, final BigDecimal bidAmount) {

        final BidDto bid = new BidDto(playerId, bidAmount);
        bids.add(bid);

        return this;
    }

    public AuctionBidsDto build() {

        return new AuctionBidsDto(TEAM_ID, bids);
    }
}
