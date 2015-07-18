/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;

import net.blackcat.fantasy.draft.integration.testdata.BidTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for the {@link Auction} class.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionTest {

    @Test
    public void testAddBids() {
        // arrange
        final Auction auction = new Auction();
        final Bid bid = BidTestDataBuilder.aBid().build();

        // act
        auction.addBids(Arrays.asList(bid));

        // assert
        assertThat(auction.getBids()).contains(bid);
    }

}
