/**
 * 
 */
package net.blackcat.fantasy.draft.integration.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	private TeamSummaryFactory() {}
	
	public static TeamSummary createTeamSummary(final TeamEntity teamEntity) {
		final TeamSummary teamSummary = new TeamSummary();
		
		teamSummary.setId(teamEntity.getId());
		teamSummary.setTeamName(teamEntity.getName());
		teamSummary.setTotalPoints(teamEntity.getTotalScore());
		teamSummary.setRemainingBudget(teamEntity.getRemainingBudget());
		
		final List<SelectedPlayer> teamList = new ArrayList<SelectedPlayer>();
		final List<SelectedPlayer> soldPlayerList = new ArrayList<SelectedPlayer>();
		
		for (final SelectedPlayerEntity selectedPlayerEntity : teamEntity.getSelectedPlayers()) {
			final SelectedPlayer selectedPlayerModel = createSelectedPlayerFromEntity(selectedPlayerEntity);
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
	 * @return Create {@link SelectedPlayer}.
	 */
	private static SelectedPlayer createSelectedPlayerFromEntity(final SelectedPlayerEntity selectedPlayerEntity) {
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
		
		return selectedPlayerModel;
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
		if (selectedPlayerEntity.getSelectedPlayerStatus() == SelectedPlayerStatus.STILL_SELECTED) {
			teamList.add(selectedPlayerModel);
		} else {
			soldPlayerList.add(selectedPlayerModel);
		}
	}
}
