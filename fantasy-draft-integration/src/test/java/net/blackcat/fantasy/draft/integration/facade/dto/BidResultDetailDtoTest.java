/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.Test;

/**
 * Unit tests for {@link BidResultDetailDto}.
 * 
 * @author Chris Hern
 * 
 */
public class BidResultDetailDtoTest {

    private static final BigDecimal THREE_POINT_FIVE_MILLION = new BigDecimal("3.5");
    private static final BigDecimal ONE_POINT_FIVE_MILLION = new BigDecimal("1.5");
    private static final BigDecimal FIVE_MILLION = new BigDecimal("5");

    @Test
    public void testCompareTo() {
        // arrange
        final BidResultDetailDto bidResult1 = new BidResultDetailDto(TestDataConstants.TEAM_ONE_NAME, THREE_POINT_FIVE_MILLION);
        final BidResultDetailDto bidResult2 = new BidResultDetailDto(TestDataConstants.TEAM_TWO_NAME, ONE_POINT_FIVE_MILLION);
        final BidResultDetailDto bidResult3 = new BidResultDetailDto(TestDataConstants.TEAM_THREE_NAME, FIVE_MILLION);

        final List<BidResultDetailDto> bidResultList = Arrays.asList(bidResult1, bidResult2, bidResult3);

        // act
        Collections.sort(bidResultList);

        // assert
        assertThat(bidResultList).containsExactly(bidResult3, bidResult1, bidResult2);
    }

}
