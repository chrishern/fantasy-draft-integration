package net.blackcat.fantasy.draft.integration.controller;

import net.blackcat.fantasy.draft.auction.AuctionRoundResults;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.DraftRoundFacade;
import net.blackcat.fantasy.draft.round.TeamBids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Controller class for invoking draft round based operations.  
 * 
 * This is the entry point for all external applications who wish to access/process draft round data.
 * 
 * @author Chris
 *
 */
@Controller(value = "draftRoundIntegrationController")
public class DraftRoundController {

	@Autowired
	@Qualifier(value = "draftRoundFacade")
	private DraftRoundFacade draftRoundFacade;
	
	/**
	 * Start the auction phase with the given number for the given league.
	 * 
	 * @param leagueId Id of the league to start the auction phase for.
	 * @param phase The number of the auction phase to start.
	 * @throws FantasyDraftIntegrationException for the following reasons:
	 * 		<ul>
	 * 			<li><b>LEAGUE_DOES_NOT_EXIST</b> - If the given league does not exist.</li>
	 * 			<li><b>OPEN_DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE</b> - If the given league already has an open draft round.</li>
	 * 			<li><b>DRAFT_ROUND_ALREADY_EXISTS_FOR_LEAGUE</b> - If an action round for the given phase number already exists.</li>
	 * 		</ul>
	 */
	public void startAuctionPhase(int leagueId, final int phase) throws FantasyDraftIntegrationException {
		draftRoundFacade.startAuctionPhase(leagueId, phase);
	}
	
	/**
	 * Make a list of bids for a team.
	 * 
	 * @param teamId Team the bids are being made for.
	 * @param teamBids List of bids the team wants to make.
	 * @throws FantasyDraftIntegrationException for the following reasons:
	 * 		<ul>
	 * 			<li><b>OPEN_DRAFT_ROUND_DOES_NOT_EXIST_FOR_LEAGUE</b> - If the given league does not have any open auction phases.</li>
	 * 			<li><b>TEAM_DOES_NOT_EXIST</b> - If a team with the given ID does not exist.</li>
	 *		</ul>
	 */
	public void makeBids(final TeamBids teamBids) throws FantasyDraftIntegrationException {
		draftRoundFacade.makeBids(teamBids);
	}
	
	/**
	 * Close the currently open auction for a given league and calculate the results of the phase.
	 * 
	 * @param leagueId The Id of the league to close the auction phase for.
	 * @return The results of the auction round.
	 * @throws FantasyDraftIntegrationException for the following reasons:
	 * 		<ul>
	 * 			<li><b>OPEN_DRAFT_ROUND_DOES_NOT_EXIST_FOR_LEAGUE</b> - If the given league does not have any open auction phases.</li>
	 *		</ul>
	 */
	public AuctionRoundResults closeAuctionPhase(int leagueId) throws FantasyDraftIntegrationException {
		return draftRoundFacade.closeAuctionPhase(leagueId);
	}
}
