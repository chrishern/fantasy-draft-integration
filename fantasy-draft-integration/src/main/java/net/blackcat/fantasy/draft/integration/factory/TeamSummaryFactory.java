/**
 * 
 */
package net.blackcat.fantasy.draft.integration.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.entity.GameweekScoreEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.player.SelectedPlayer;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.team.TeamSummary;

/**
 * Factory for dealing with {@link TeamSummary} objects.
 * 
 * @author Chris
 *
 */
public final class TeamSummaryFactory {

	private final static List<SelectedPlayerStatus> SOLD_PLAYER_STATUSES = Arrays.asList(SelectedPlayerStatus.SOLD_TO_POT, SelectedPlayerStatus.TRANSFERRED_OUT);
	
	private TeamSummaryFactory() {}
	
	/**
	 * Create the team summary for a given team.
	 * 
	 * @param teamEntity {@link TeamEntity} to create the summary for.
	 * @param weeklyScoreWeekNumber The number of the gameweek to get the player weekly score for.
	 * @return {@link TeamSummary} for the given team.
	 */
	public static TeamSummary createTeamSummary(final TeamEntity teamEntity, final int weeklyScoreWeekNumber) {
		final TeamSummary teamSummary = new TeamSummary();
		
		teamSummary.setId(teamEntity.getId());
		teamSummary.setTeamName(teamEntity.getName());
		teamSummary.setTotalPoints(teamEntity.getTotalScore());
		teamSummary.setRemainingBudget(teamEntity.getRemainingBudget());
		
		final List<SelectedPlayer> teamList = new ArrayList<SelectedPlayer>();
		final List<SelectedPlayer> soldPlayerList = new ArrayList<SelectedPlayer>();
		
		for (final SelectedPlayerEntity selectedPlayerEntity : teamEntity.getSelectedPlayers()) {
			final SelectedPlayer selectedPlayerModel = createSelectedPlayerFromEntity(selectedPlayerEntity, weeklyScoreWeekNumber);
			addSelectedPlayerToAppropriateTeamList(teamList, soldPlayerList, selectedPlayerEntity, selectedPlayerModel);
		}
		
		Collections.sort(teamList);
		teamSummary.setTeam(teamList);
		teamSummary.setSoldPlayers(soldPlayerList);
		
		return teamSummary;
	}

	/**
	 * Create a {@link SelectedPlayer} object from a {@link SelectedPlayerEntity}.
	 * 
	 * @param selectedPlayerEntity Entity to create the {@link SelectedPlayer} from.
	 * @param weeklyScoreWeekNumber The number of the gameweek to get the player weekly score for.
	 * @return Create {@link SelectedPlayer}.
	 */
	private static SelectedPlayer createSelectedPlayerFromEntity(final SelectedPlayerEntity selectedPlayerEntity, final int weeklyScoreWeekNumber) {
		final SelectedPlayer selectedPlayerModel = new SelectedPlayer();
		final PlayerEntity playerEntity = selectedPlayerEntity.getPlayer();

		selectedPlayerModel.setCost(selectedPlayerEntity.getCost());
		selectedPlayerModel.setForename(playerEntity.getForename());
		selectedPlayerModel.setId(playerEntity.getId());
		selectedPlayerModel.setPointsScored(selectedPlayerEntity.getPointsScored());
		selectedPlayerModel.setPosition(playerEntity.getPosition());
		selectedPlayerModel.setSelectionStatus(selectedPlayerEntity.getSelectionStatus());
		selectedPlayerModel.setSurname(playerEntity.getSurname());
		selectedPlayerModel.setTeam(playerEntity.getTeam());
		selectedPlayerModel.setCurrentSellToPotPrice(selectedPlayerEntity.getCurrentSellToPotPrice());
		selectedPlayerModel.setSquadStatus(selectedPlayerEntity.getSelectedPlayerStatus());
		setGameweekScore(selectedPlayerEntity, weeklyScoreWeekNumber, selectedPlayerModel);
		
		return selectedPlayerModel;
	}

	/**
	 * Set the gameweek score for a given gameweek into the model player.
	 * 
	 * @param selectedPlayerEntity Entity containing all selected player data.
	 * @param gameweekNumber The number of the gameweek to get the score for. 
	 * @param selectedPlayerModel Model object to set the score in.
	 */
	private static void setGameweekScore(final SelectedPlayerEntity selectedPlayerEntity, final int gameweekNumber, final SelectedPlayer selectedPlayerModel) {
		if (playerHasGameweekScores(selectedPlayerEntity)) {
			for (final GameweekScoreEntity gameweekScoreEntity : selectedPlayerEntity.getGameweekScores()) {
				if (gameweekScoreEntity.getGameweek() == gameweekNumber) {
					selectedPlayerModel.setCurrentWeeklyPoints(gameweekScoreEntity.getScore());
					break;
				}
			}
		}
	}

	/**
	 * Determine if the given player has some gameweek scores.
	 * 
	 * @param selectedPlayerEntity Selected player to check if it has gameweek scores associated with it.
	 * @return True if the player has gameweek scores, false otherwise.
	 */
	private static boolean playerHasGameweekScores(final SelectedPlayerEntity selectedPlayerEntity) {
		return selectedPlayerEntity.getGameweekScores() != null;
	}
	
	/**
	 * Add a {@link SelectedPlayer} to the appropriate team list, either the team or the list of sold players depending on
	 * whether they are still part of the team or not.
	 * 
	 * @param teamList The list of {@link SelectedPlayer} objects representing the players still selected.
	 * @param soldPlayerList The list of {@link SelectedPlayer} objects representing the players who have been sold.
	 * @param selectedPlayerEntity The entity representation of the selected player.
	 * @param selectedPlayerModel The model representation of the selected player.
	 */
	private static void addSelectedPlayerToAppropriateTeamList(final List<SelectedPlayer> teamList, final List<SelectedPlayer> soldPlayerList,
			final SelectedPlayerEntity selectedPlayerEntity, final SelectedPlayer selectedPlayerModel) {
		if (SOLD_PLAYER_STATUSES.contains(selectedPlayerEntity.getSelectedPlayerStatus())) {
			soldPlayerList.add(selectedPlayerModel);
		} else {
			teamList.add(selectedPlayerModel);
		}
	}
}
