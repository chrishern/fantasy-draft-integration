/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.integration.facade.DraftRoundFacade;
import net.blackcat.fantasy.draft.round.Bid;
import net.blackcat.fantasy.draft.round.TeamBids;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit test cases for {@link DraftRoundController}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DraftRoundControllerTest {

	@Mock
	private DraftRoundFacade draftRoundFacade;
	
	@InjectMocks
	private DraftRoundController draftRoundController = new DraftRoundController(); 
	
	@Test
	public void testMakeBids() throws Exception {
		// arrange
		final Bid bid = new Bid(1, new BigDecimal("1"));
		final List<Bid> bids = Arrays.asList(bid);
		final TeamBids teamBids = new TeamBids(2, bids);
		
		// act
		draftRoundController.makeBids(teamBids);
		
		// assert
		verify(draftRoundFacade).makeBids(teamBids);
	}

	@Test
	public void testStartAuctionPhase() throws Exception {
		// arrange
		
		// act
		draftRoundController.startAuctionPhase(1, 1);
		
		// assert
		verify(draftRoundFacade).startAuctionPhase(1, 1);
	}
}
