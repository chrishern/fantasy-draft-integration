/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;

import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionPhaseStatus;
import net.blackcat.fantasy.draft.integration.testdata.AuctionPhaseTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.BidTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TeamTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for the {@link AuctionPhase} class.
 * 
 * @author Chris Hern
 * 
 */
public class AuctionPhaseTest {

    private static final String NON_EXISTING_TEAM_NAME = "Non existing team";

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

    @Test
    public void testHasTeamSubmittedBids_True() {
        // arrange
        final Team team = TeamTestDataBuilder.aTeam().build();
        final Player player = PlayerTestDataBuilder.aPlayer().build();
        final AuctionPhase auctionPhase = AuctionPhaseTestDataBuilder.anOpenAuctionPhase().withBid(player, team, new BigDecimal("3.5")).build();

        // act
        final boolean hasTeamSubmittedBids = auctionPhase.hasTeamSubmittedBids(team.getName());

        // assert
        assertThat(hasTeamSubmittedBids).isTrue();
    }

    @Test
    public void testHasTeamSubmittedBids_False() {
        // arrange
        final Team team = TeamTestDataBuilder.aTeam().build();
        final Player player = PlayerTestDataBuilder.aPlayer().build();
        final AuctionPhase auctionPhase = AuctionPhaseTestDataBuilder.anOpenAuctionPhase().withBid(player, team, new BigDecimal("3.5")).build();

        // act
        final boolean hasTeamSubmittedBids = auctionPhase.hasTeamSubmittedBids(NON_EXISTING_TEAM_NAME);

        // assert
        assertThat(hasTeamSubmittedBids).isFalse();
    }
}
