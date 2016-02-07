/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for {@link AuctionPhaseResultsDto}.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionPhaseResultsDtoTest {

    @Test
    public void testCompareTo() {
        // arrange
        final AuctionPhaseResultsDto phase1 = new AuctionPhaseResultsDto(1);
        final AuctionPhaseResultsDto phase2 = new AuctionPhaseResultsDto(2);
        final AuctionPhaseResultsDto phase3 = new AuctionPhaseResultsDto(3);

        final List<AuctionPhaseResultsDto> phases = Arrays.asList(phase2, phase3, phase1);

        // act
        Collections.sort(phases);

        // assert
        assertThat(phases).containsExactly(phase1, phase2, phase3);
    }

}
