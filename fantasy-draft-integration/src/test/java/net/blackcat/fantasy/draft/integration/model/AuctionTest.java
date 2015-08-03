/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionStatus;

import org.junit.Test;

/**
 * Unit tests for the {@link Auction} class.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionTest {

    @Test
    public void testCreatNewInstance() {
        // arrange

        // act
        final Auction auction = new Auction();

        // assert
        assertThat(auction.getStatus()).isEqualTo(AuctionStatus.OPEN);
        assertThat(auction.getPhases()).hasSize(1);
    }

    @Test
    public void testCreateNewPhase() {
        // arrange
        final Auction auction = new Auction();

        // act
        auction.createNewPhase();

        // assert
        assertThat(auction.getPhases()).hasSize(2);
        assertThat(auction.getPhases().get(1).getSequenceNumber()).isEqualTo(2);
    }

}
