/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;

import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionPhaseStatus;
import net.blackcat.fantasy.draft.integration.testdata.BidTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for the {@link AuctionPhase} class.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionPhaseTest {

    @Test
    public void testAddBids() {
        // arrange
        final AuctionPhase auctionPhase = new AuctionPhase();
        final Bid bid = BidTestDataBuilder.aBid().build();

        // act
        auctionPhase.addBids(Arrays.asList(bid));

        // assert
        assertThat(auctionPhase.getBids()).contains(bid);
    }

    @Test
    public void testIsOpen_True() {
        // arrange
        final AuctionPhase auctionPhase = new AuctionPhase();

        // act
        final boolean isOpen = auctionPhase.isOpen();

        // assert
        assertThat(isOpen).isTrue();
    }

    @Test
    public void testIsOpen_False() {
        // arrange
        final AuctionPhase auctionPhase = new AuctionPhase();
        auctionPhase.setStatus(AuctionPhaseStatus.CLOSED);

        // act
        final boolean isOpen = auctionPhase.isOpen();

        // assert
        assertThat(isOpen).isFalse();
    }
}
