/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.List;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.team.Team;
import net.blackcat.fantasy.draft.team.TeamSummary;

/**
 * Operations for manipulating team related data.
 * 
 * @author Chris
 *
 */
public interface TeamFacade {

	/**
	 * Create a new team within the back end.
	 * 
	 * @param teamName Name of the team to create.
	 */
	void createTeam(String teamName);
	
	/**
	 * Get a team based on the name of the team.
	 * 
	 * @param teamName Name of the team to find
	 * @return {@link Team} with the given name. 
	 * @throws FantasyDraftIntegrationException If a team with the given name does not exist.
	 */
	Team getTeam(String teamName) throws FantasyDraftIntegrationException;
	
	
	/**
	 * Get the list of completed (i.e. fully picked) teams for a given league.
	 * 
	 * This was used to display the list of squads straight after the auction finished.
	 * I.e. without any points or selected starting eleven information.
	 * 
	 * @param leagueId ID of the league to get the teams for.
	 * @return List of {@link Team} objects in the given league.
	 * @throws FantasyDraftIntegrationException If the league IF does not exist.
	 */
	List<Team> getCompleteTeams(int leagueId) throws FantasyDraftIntegrationException;
	
	/**
	 * Get the summary of a team.  This summary includes the total points for the team and the
	 * squad (in selection order) with the total points for each player.
	 * 
	 * @param teamId ID of the team to get the summary for.
	 * @return Summary of the desired team.
	 * @throws FantasyDraftIntegrationException If a team with the given ID is not found.
	 */
	TeamSummary getTeamSummary(int teamId) throws FantasyDraftIntegrationException;
	
	/**
	 * Get the team summaries for a specific league.  This summary includes the total points for 
	 * the team and the squad (in selection order) with the total points for each player.
	 * 
	 * @param leagueId ID of the league to get the team summaries for.
	 * @return Team summaries from the desired league.
	 * @throws FantasyDraftIntegrationException If a league with the given ID is not found.
	 */
	List<TeamSummary> getTeamSummaries(int leagueId) throws FantasyDraftIntegrationException;
}
