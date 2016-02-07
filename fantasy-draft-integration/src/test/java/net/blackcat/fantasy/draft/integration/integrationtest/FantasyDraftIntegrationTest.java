package net.blackcat.fantasy.draft.integration.integrationtest;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.integration.facade.AuctionFacade;
import net.blackcat.fantasy.draft.integration.facade.TeamFacade;
import net.blackcat.fantasy.draft.integration.facade.dto.AuctionBidsDto;
import net.blackcat.fantasy.draft.integration.facade.dto.AuctionPhaseResultsDto;
import net.blackcat.fantasy.draft.integration.facade.dto.BidDto;
import net.blackcat.fantasy.draft.integration.facade.dto.SquadDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TeamAuctionStatusDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests for the Fantasy Draft integration functionality.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/integrationTestContext.xml" })
public class FantasyDraftIntegrationTest {

    @Autowired
    private AuctionFacade auctionFacade;

    @Autowired
    private TeamFacade teamFacade;

    @Test
    public void testStartAuction() throws Exception {
        auctionFacade.startAuction(1);
    }

    @Test
    public void testOpenAuctionPhase() throws Exception {
        auctionFacade.openAuctionPhase(1);
    }

    @Test
    public void testMakeBids() throws Exception {
        // Team 1 bids
        final BidDto t1p1Bid = new BidDto(1, new BigDecimal("3"));
        final BidDto t1p2Bid = new BidDto(2, new BigDecimal("1"));
        final BidDto t1p3Bid = new BidDto(3, new BigDecimal("5"));

        final AuctionBidsDto team1Bids = new AuctionBidsDto(1, Arrays.asList(t1p1Bid, t1p2Bid, t1p3Bid));

        auctionFacade.makeBids(team1Bids);

        // Team 2 bids
        final BidDto t2p1Bid = new BidDto(1, new BigDecimal("3.1"));
        final BidDto t2p2Bid = new BidDto(2, new BigDecimal("1"));

        final AuctionBidsDto team2Bids = new AuctionBidsDto(2, Arrays.asList(t2p1Bid, t2p2Bid));

        auctionFacade.makeBids(team2Bids);
    }

    @Test
    public void testGetTeamAuctionStatus() throws Exception {
        final TeamAuctionStatusDto teamAuctionStatus = auctionFacade.getTeamAuctionStatus("a@a.com");

        assertThat(teamAuctionStatus.getRemainingBudget()).isNotNull();
        assertThat(teamAuctionStatus.isBidsSubmittedInCurrentWindow()).isTrue();
        assertThat(teamAuctionStatus.isOpenTransferWindowForTeam()).isTrue();
    }

    @Test
    public void testCloseAuctionPhase() throws Exception {
        auctionFacade.closeAuctionPhase(1);
    }

    @Test
    public void testGetLeagueAuctionPhaseResults() throws Exception {
        final List<AuctionPhaseResultsDto> leagueAuctionPhaseResults = auctionFacade.getLeagueAuctionPhaseResults(1);

        assertThat(leagueAuctionPhaseResults).hasSize(1);
    }

    @Test
    public void testGetSquadDetails() throws Exception {
        final SquadDto squadDetails = teamFacade.getSquadDetails("a@a.com");

        System.out.println("Squad size: " + squadDetails.getCurrentPlayers().size());
    }

    @Test
    public void testCloseAuction() throws Exception {
        auctionFacade.closeAuction(1);
    }
}
