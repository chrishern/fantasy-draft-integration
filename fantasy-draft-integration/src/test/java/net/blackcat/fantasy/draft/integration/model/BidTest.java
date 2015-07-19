/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.testdata.BidTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for the {@link Bid} object.
 * 
 * @author Chris Hern
 * 
 */
public class BidTest {

    @Test
    public void testCompareTo() {
        // arrange
        final Bid bid1 = BidTestDataBuilder.aBid().withAmount(new BigDecimal("1.0")).build();
        final Bid bid2 = BidTestDataBuilder.aBid().withAmount(new BigDecimal("2.0")).build();
        final Bid bid3 = BidTestDataBuilder.aBid().withAmount(new BigDecimal("1.1")).build();

        final List<Bid> allBids = Arrays.asList(bid1, bid2, bid3);

        // act
        Collections.sort(allBids);

        // assert
        assertThat(allBids).containsExactly(bid2, bid3, bid1);
    }

    @Test
    public void testHasMatchingAmount_True() {
        // arrange
        final Bid bid1 = BidTestDataBuilder.aBid().withAmount(new BigDecimal("1.0")).build();
        final Bid bid2 = BidTestDataBuilder.aBid().withAmount(new BigDecimal("1.00")).build();

        // act
        final boolean hasMatchingAmount = bid1.hasMatchingAmount(bid2);

        // assert
        assertThat(hasMatchingAmount).isTrue();
    }

    @Test
    public void testHasMatchingAmount_False() {
        // arrange
        final Bid bid1 = BidTestDataBuilder.aBid().withAmount(new BigDecimal("1.0")).build();
        final Bid bid2 = BidTestDataBuilder.aBid().withAmount(new BigDecimal("1.01")).build();

        // act
        final boolean hasMatchingAmount = bid1.hasMatchingAmount(bid2);

        // assert
        assertThat(hasMatchingAmount).isFalse();
    }

    @Test
    public void testMarkBidAsSuccessful() {
        // arrange
        final Bid bid = BidTestDataBuilder.aBid().build();

        // act
        bid.markBidAsSuccessful();

        // assert
        assertThat(bid.isSuccessful()).isTrue();
    }
}
