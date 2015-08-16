/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model.types.team;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;

/**
 * Enum storing valid formations for a team.
 * 
 * @author Chris
 *
 */
public enum ValidFormation {

	F_3_5_2(3, 5, 2),
	F_3_4_3(3, 4, 3),
	F_4_5_1(4, 5, 1),
	F_4_4_2(4, 4, 2),
	F_4_3_3(4, 3, 3),
	F_5_2_3(5, 2, 3),
	F_5_3_2(5, 3, 2),
	F_5_4_1(5, 4, 1);
	
	private int noOfDefenders;
	private int noOfMidfielders;
	private int noOfStrikers;
	
	private ValidFormation(final int noOfDefenders, final int noOfMidfielders, final int noOfStrikers) {
		this.noOfDefenders = noOfDefenders;
		this.noOfMidfielders = noOfMidfielders;
		this.noOfStrikers = noOfStrikers;
	}

	/**
	 * @return the no of goalkeepers
	 */
	public int getNoOfGoalkeepers() {
		return 1;
	}
	
	/**
	 * @return the noOfDefenders
	 */
	public int getNoOfDefenders() {
		return noOfDefenders;
	}

	/**
	 * @return the noOfMidfielders
	 */
	public int getNoOfMidfielders() {
		return noOfMidfielders;
	}

	/**
	 * @return the noOfStrikers
	 */
	public int getNoOfStrikers() {
		return noOfStrikers;
	}

	/**
	 * Determine if a list of {@link SelectedPlayer} objects correlates to a valid formation.
	 * 
	 * @param selectedPlayers List of {@link SelectedPlayer} objects to check.
	 * @return True if the {@link SelectedPlayer} objects map to a valid formation, false if not.
	 */
	public static boolean isValidFormation(final List<SelectedPlayer> selectedPlayers) {
		
		final List<SelectedPlayer> goalkeepers = getPlayersInPosition(selectedPlayers, Position.GOALKEEPER);
		final List<SelectedPlayer> defenders = getPlayersInPosition(selectedPlayers, Position.DEFENDER);
		final List<SelectedPlayer> midfielders = getPlayersInPosition(selectedPlayers, Position.MIDFIELDER);
		final List<SelectedPlayer> strikers = getPlayersInPosition(selectedPlayers, Position.STRIKER);
		
		for (final ValidFormation validFormation : ValidFormation.values()) {
			if (isValidFormation(goalkeepers, defenders, midfielders, strikers, validFormation)) {
				return true;
			}
		}
		
		return false;
	}

	/*
	 * Determine if we have a valid formation.
	 */
	private static boolean isValidFormation(final List<SelectedPlayer> goalkeepers, final List<SelectedPlayer> defenders,
			final List<SelectedPlayer> midfielders, final List<SelectedPlayer> strikers, final ValidFormation validFormation) {
		
		return goalkeepers.size() <= validFormation.getNoOfGoalkeepers() && defenders.size() <= validFormation.getNoOfDefenders()
				&& midfielders.size() <= validFormation.getNoOfMidfielders() && strikers.size() <= validFormation.getNoOfStrikers();
	}
	
	/*
	 * Get the players of a given position from a list of selected players.
	 */
	private static List<SelectedPlayer> getPlayersInPosition(final List<SelectedPlayer> selectedPlayers, final Position position) {
		
		final List<SelectedPlayer> playersInPosition = new ArrayList<SelectedPlayer>();
		
		for (final SelectedPlayer selectedPlayer : selectedPlayers) {
			if (position == selectedPlayer.getPlayer().getPosition()) {
				playersInPosition.add(selectedPlayer);
			}
		}
		
		return playersInPosition;
	}
}
