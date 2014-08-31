/**
 * 
 */
package net.blackcat.fantasy.draft.integration.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.player.GameweekScorePlayer;
import net.blackcat.fantasy.draft.player.types.Position;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.team.ValidFormations;

/**
 * Utility class to help with team selections.
 * 
 * @author Chris
 *
 */
public final class TeamSelectionUtils {

	private static final int FULL_TEAM_SIZE = 11;
	
	public TeamSelectionUtils () {}
	
	/**
	 * Determine if a given starting team is a valid formation or not.
	 * 
	 * @param startingTeamForWeek Team to check if it is a valid team.
	 * @return True if the given team contains a valid formation and false otherwise.
	 */
	public static boolean isValidFormation(final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek) {
		final int numberOfGoalkeepers = getNumberOfPlayersInPosition(startingTeamForWeek, Position.GOALKEEPER);
		final int numberOfDefenders = getNumberOfPlayersInPosition(startingTeamForWeek, Position.DEFENDER);
		final int numberOfMidfielders = getNumberOfPlayersInPosition(startingTeamForWeek, Position.MIDFIEDER);
		final int numberOfStrikers = getNumberOfPlayersInPosition(startingTeamForWeek, Position.STRIKER);
		
		for (final ValidFormations validFormation : ValidFormations.values()) {
			if (validFormation.getNoOfGoalkeepers() >= numberOfGoalkeepers && validFormation.getNoOfDefenders() >= numberOfDefenders
					&& validFormation.getNoOfMidfielders() >= numberOfMidfielders && validFormation.getNoOfStrikers() >= numberOfStrikers) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Utility which builds the starting lineup for a given {@link TeamEntity} based on their PICKED players and the 
	 * players who played in a given gameweek.
	 * 
	 * @param gameweekScores Scores for a gameweek.
	 * @param team {@link TeamEntity} to build the picked starting team from.
	 * @param startingTeamForWeek Map to build up the starting players into.
	 * @return The number of players that were added to the team.
	 */
	public static int buildTeamFromPickedPlayersForWeek(final Map<Integer, GameweekScorePlayer> gameweekScores, final TeamEntity team,
			final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek) {
		int numberOfStartingPlayersForWeek = 0;
		
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		for (final SelectedPlayerEntity selectedPlayer : selectedPlayers) {
			if (isPlayerInSelectedTeam(selectedPlayer)) {
				
				final PlayerEntity player = selectedPlayer.getPlayer();
				
				if (didPlayerPlay(gameweekScores, player)) {
					numberOfStartingPlayersForWeek++;
					addPlayerToStartingTeam(startingTeamForWeek, selectedPlayer, player.getPosition());
				}
			}
		}
		
		return numberOfStartingPlayersForWeek;
	}
	
	/**
	 * Utility to take a starting team and fill it up (if necessary) with substitutes from a team's squad.
	 * 
	 * @param startingTeamForWeek Current starting team.
	 * @param currentNumberOfPlayersInTeam Current number of players in the starting team.
	 * @param teamSquad The {@link TeamEntity} selected players containing the substitutes.
	 * @param gameweekScores Player scores from a gameweek.
	 */
	public static void fillTeamUpWithSubstitutes(final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek, 
			final int currentNumberOfPlayersInTeam, final List<SelectedPlayerEntity> teamSquad,
			final Map<Integer, GameweekScorePlayer> gameweekScores) {
		int numberOfStartingPlayersForWeek = currentNumberOfPlayersInTeam;
		
		if (!isTeamComplete(numberOfStartingPlayersForWeek)) {
			int substitutePosition = 1;
			final List<SelectedPlayerEntity> substitutes = getOrderedSubtituteList(teamSquad);
			
			while (!isTeamComplete(numberOfStartingPlayersForWeek) && substitutePosition <= 5) {
				final SelectedPlayerEntity substitute = substitutes.get(substitutePosition - 1);
				
				if (didPlayerPlay(gameweekScores, substitute.getPlayer())) {
					addPlayerToStartingTeam(startingTeamForWeek, substitute, substitute.getPlayer().getPosition());
					
					if (isValidFormation(startingTeamForWeek)) {
						numberOfStartingPlayersForWeek++;
					} else {
						removePlayerFromTeam(startingTeamForWeek, substitute);
					}
				}
				
				substitutePosition++;
			}
		}
	}
	
	/**
	 * Remove a selected {@link SelectedPlayerEntity} from a starting team.
	 * 
	 * @param startingTeamForWeek Starting team to remove the player from.
	 * @param player Player to remove.
	 */
	private static void removePlayerFromTeam(final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek, final SelectedPlayerEntity player) {
		final List<SelectedPlayerEntity> positionList = startingTeamForWeek.get(player.getPlayer().getPosition());
		positionList.remove(player);
	}
	
	/**
	 * Get a list of the substitutes in a given team in the priority order.
	 * 
	 * @param selectedPlayers List of selected players to get the ordered substitutes from.
	 * @return List of priority ordered substitutes.
	 */
	private static List<SelectedPlayerEntity> getOrderedSubtituteList(final List<SelectedPlayerEntity> selectedPlayers) {
		final List<SelectedPlayerEntity> substitutes = new ArrayList<SelectedPlayerEntity>();
		
		for (final SelectedPlayerEntity selectedPlayer : selectedPlayers) {
			if (TeamSelectionUtils.isPlayerASubstitute(selectedPlayer)) {
				substitutes.add(selectedPlayer);
			}
		}
		
		Collections.sort(substitutes);
		
		return substitutes;
	}


	/**
	 * Determine if a team is complete based on the number of players in the team.
	 *  
	 * @param currentTeamSize Number of players in a team.
	 * @return True if the team is complete, false otherwise.
	 */
	private static boolean isTeamComplete(final int currentTeamSize) {
		return currentTeamSize == FULL_TEAM_SIZE;
	}
	
	/**
	 * Determine if a player is a substitute or not.
	 * 
	 * @param selectedPlayer Player to check the selection status for.
	 * @return True if the given player is a substitute, false otherwise.
	 */
	public static boolean isPlayerASubstitute(final SelectedPlayerEntity selectedPlayer) {
		return SelectedPlayerStatus.SUBSTITUTE_POSITIONS.contains(selectedPlayer.getSelectionStatus());
	}
	
	/**
	 * Add a player to the the starting team.
	 * 
	 * @param startingTeamForWeek Starting eleven for a certain team.
	 * @param selectedPlayer Player to add to the starting team.
	 * @param position The player's position. 
	 */
	public static void addPlayerToStartingTeam(final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek,
			final SelectedPlayerEntity selectedPlayer, final Position position) {
		List<SelectedPlayerEntity> playersInPosition = startingTeamForWeek.get(position);
		
		if (playersInPosition == null) {
			playersInPosition = new ArrayList<SelectedPlayerEntity>();
		}
		
		playersInPosition.add(selectedPlayer);
		startingTeamForWeek.put(position, playersInPosition);
	}
	
	/**
	 * Determine if a player is part of the selected 11 for a team.
	 * 
	 * @param selectedPlayer The player to determine the status of.
	 * @return True if the player is part of the starting 11, false if not.
	 */
	private static boolean isPlayerInSelectedTeam(final SelectedPlayerEntity selectedPlayer) {
		return !isPlayerASubstitute(selectedPlayer);
	}
	
	/**
	 * Determine if a given player has played in a given gameweek.
	 * 
	 * @param gameweekScores Player scores in the given gameweek.
	 * @param player Player to check if they played.
	 * @return True if the player played in the gameweek, false otherwise.
	 */
	private static boolean didPlayerPlay(final Map<Integer, GameweekScorePlayer> gameweekScores, final PlayerEntity player) {
		return gameweekScores.get(player.getId()).getMinutesPlayed() > 0;
	}
	
	/**
	 * Get the number of players in a starting team from a given position.
	 * 
	 * @param startingTeamForWeek Starting team to check.
	 * @param position Position to get the number of players from.
	 * @return Number of players in the given position.
	 */
	private static int getNumberOfPlayersInPosition(final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek, final Position position) {
		final List<SelectedPlayerEntity> playersInPosition = startingTeamForWeek.get(position);
		
		if (playersInPosition == null) {
			return 0;
		}
		
		return playersInPosition.size();
	}
}
