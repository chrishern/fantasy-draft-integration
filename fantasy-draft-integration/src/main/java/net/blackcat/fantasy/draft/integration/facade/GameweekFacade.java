/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.facade.dto.PlayerGameweekScoreDto;
import net.blackcat.fantasy.draft.integration.model.Gameweek;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.types.team.ValidFormation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade operations on gameweeks.
 * 
 * @author Chris
 *
 */
@Component
@Transactional
public class GameweekFacade {

	private LeagueDataService leagueDataService;
	private GameweekDataService gameweekDataService;
	
	@Autowired
	public GameweekFacade(final LeagueDataService leagueDataService, final GameweekDataService gameweekDataService) {
		
		this.leagueDataService = leagueDataService;
		this.gameweekDataService = gameweekDataService;
	}
	
	/**
	 * Calculate the gameweek scores for the current gameweek.
	 * 
	 * @param playerGameweekScores
	 * @throws FantasyDraftIntegrationException
	 */
	public void calculateGameweekScores(final Map<Integer, PlayerGameweekScoreDto> playerGameweekScores) throws FantasyDraftIntegrationException {
		
		final Gameweek gameweek = gameweekDataService.getGameweek();
		
		calculateScores(playerGameweekScores, gameweek.getCurrentGameweek());
		
		gameweek.moveToNextGameweek();
		gameweekDataService.updateGameweek(gameweek);
	}
	
	/**
	 * Calculate the gameweek scores for a specific gameweek.
	 *  
	 * @param playerGameweekScores Map of player IDs to the gameweek scores for the given gameweek.
	 * @param gameweek The gameweek number to calculate for.
	 * @throws FantasyDraftIntegrationException
	 */
	public void calculateGameweekScores(final Map<Integer, PlayerGameweekScoreDto> playerGameweekScores, final int gameweek) throws FantasyDraftIntegrationException {
		
		calculateScores(playerGameweekScores, gameweek);
	}

	/*
	 * Actually perform the gameweek score calculation.
	 */
	private void calculateScores(final Map<Integer, PlayerGameweekScoreDto> playerGameweekScores, final int gameweek) throws FantasyDraftIntegrationException {
		
		final League league = leagueDataService.getLeague(1);
		
		for (final Team team : league.getTeams()) {
			
			final List<SelectedPlayer> startingTeam = team.getStartingTeam();
			final List<SelectedPlayer> playedStartingTeam = removePlayersWhoDidntPlay(playerGameweekScores, startingTeam);
			
			if (Team.MAXIMUM_STARTING_TEAM_SIZE != playedStartingTeam.size()) {
				addSubstitutesToStartingTeam(team, playedStartingTeam, playerGameweekScores);
			}
			
			final int totalTeamPoints = addGameweekScoreForSelectedPlayers(playerGameweekScores, playedStartingTeam, gameweek);
			
			team.addGameweekScore(gameweek, totalTeamPoints);
			
			System.out.println(team.getName() + " score: " + totalTeamPoints);
		}
		
		leagueDataService.updateLeague(league);
	}
	
	/*
	 * Add the gameweek score for each selected player in a starting team.
	 */
	private int addGameweekScoreForSelectedPlayers(final Map<Integer, PlayerGameweekScoreDto> playerGameweekScores, 
			final List<SelectedPlayer> startingTeam, final int gameweek) {
		
		int totalTeamPoints = 0;
		final boolean didCaptainPlay = didCaptainPlay(startingTeam);
		
		for (final SelectedPlayer selectedPlayer : startingTeam) {
			final PlayerGameweekScoreDto playerGameweekScoreDto = playerGameweekScores.get(selectedPlayer.getPlayer().getId());
			int playerWeekPoints = playerGameweekScoreDto.getScore();
			
			playerWeekPoints = doublePlayerPointsIfRequired(didCaptainPlay, selectedPlayer, playerWeekPoints);
			
			totalTeamPoints += playerWeekPoints;
			selectedPlayer.addGameweekScore(gameweek, playerWeekPoints);
		}
		return totalTeamPoints;
	}

	/*
	 * Double a player's weekly points if required.
	 */
	private int doublePlayerPointsIfRequired(final boolean didCaptainPlay,
			final SelectedPlayer selectedPlayer, final int playerWeekPoints) {

		int doubledPlayerPoints = playerWeekPoints;
		
		if (selectedPlayer.isCaptain()) {
			doubledPlayerPoints *= 2;
		} else if (selectedPlayer.isViceCaptain() && !didCaptainPlay) {
			doubledPlayerPoints *= 2;
		}
		return doubledPlayerPoints;
	}
	
	/*
	 * Determine if the captain plated in a starting lineup.
	 */
	private boolean didCaptainPlay(final List<SelectedPlayer> startingTeam) {
		
		for (final SelectedPlayer selectedPlayer : startingTeam) {
			if (selectedPlayer.isCaptain()) {
				return true;
			}
		}
		
		return false;
	}

	/*
	 * Remove players from a starting lineup who didn't play in this gameweek.
	 */
	private List<SelectedPlayer> removePlayersWhoDidntPlay(final Map<Integer, PlayerGameweekScoreDto> playerGameweekScores,
			final List<SelectedPlayer> startingTeam) {
		
		final List<SelectedPlayer> startingTeamWithoutNonPlayingPlayers = new ArrayList<SelectedPlayer>();
		
		for (final SelectedPlayer selectedPlayer : startingTeam) {
			final PlayerGameweekScoreDto playerGameweekScoreDto = playerGameweekScores.get(selectedPlayer.getPlayer().getId());
			
			if (playerGameweekScoreDto.hasPlayed()) {
				startingTeamWithoutNonPlayingPlayers.add(selectedPlayer);
			}
		}
		
		return startingTeamWithoutNonPlayingPlayers;
	}

	/*
	 * Add the substitutes to a starting lineup.
	 */
	private void addSubstitutesToStartingTeam(final Team team, final List<SelectedPlayer> startingTeam, final Map<Integer, PlayerGameweekScoreDto> playerGameweekScores) {
		
		final List<SelectedPlayer> substitutes = team.getSubstitutes();
		
		for (final SelectedPlayer substitute : substitutes) {
			
			final PlayerGameweekScoreDto playerGameweekScoreDto = playerGameweekScores.get(substitute.getPlayer().getId());
			
			if (playerGameweekScoreDto.hasPlayed()) {
				startingTeam.add(substitute);
				
				if (ValidFormation.isValidFormation(startingTeam)) {
					if (Team.MAXIMUM_STARTING_TEAM_SIZE == startingTeam.size()) {
						break;
					}
				} else {
					startingTeam.remove(substitute);
				}
			}
		}
	}
}
