/**
 * 
 */
package net.blackcat.fantasy.draft.integration.test.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.entity.GameweekScoreEntity;
import net.blackcat.fantasy.draft.integration.entity.ManagerEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.player.GameweekScorePlayer;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.Position;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStartingElevenStatus;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;

/**
 * Utilities for creating test data.
 * 
 * @author Chris
 *
 */
public final class TestDataUtil {

	public static final String SELECTED_PLAYER_COST = "7.0";
	public static final String FPL_COST_AT_PURCHASE = "8.5";

	public static final int POINTS_FOR_PLAYING = 2;
	
	public static final String MANAGER_EMAIL_ADDRESS = "manager@manager.com";
	public static final String MANAGER_SURNAME = "Manager Surname";
	public static final String MANAGER_FORENAME = "Manager Forename";
	
	public static final int PLAYER_1_ID = 1;
	public static final int PLAYER_1_POINTS = 54;
	public static final String PLAYER_1_SURNAME = "Player";
	public static final String PLAYER_1_FORENAME = "Test";
	
	public static final int PLAYER_2_ID = 2;
	public static final int PLAYER_2_POINTS = 89;
	public static final String PLAYER_2_SURNAME = "Player2";
	public static final String PLAYER_2_FORENAME = "Test2";
	
	public static final int PLAYER_3_ID = 3;
	public static final int PLAYER_4_ID = 4;
	public static final int PLAYER_5_ID = 5;
	public static final int PLAYER_6_ID = 6;
	public static final int PLAYER_7_ID = 7;
	public static final int PLAYER_13_ID = 13;
	public static final int PLAYER_14_ID = 14;
	
	public static final String TEST_TEAM_1 = "Test Team 1";
	public static final String TEST_TEAM_2 = "Test Team 2";
	public static final String TEST_TEAM_3 = "Test Team 3";

	public static final int TEST_TEAM_1_SCORE = 134;
	public static final int TEST_TEAM_2_SCORE = 190;
	public static final int TEST_TEAM_3_SCORE = 156;
	
	public static final int TEST_TEAM_1_WEEK_SCORE = 54;
	public static final int TEST_TEAM_2_WEEK_SCORE = 14;
	public static final int TEST_TEAM_3_WEEK_SCORE = 32;
	
	public static final String LEAGUE_NAME = "New League";
	
	/**
	 * Create a {@link Player} object.
	 * 
	 * @param playerNumber Whether you want player 1 or 2.
	 * @return {@link Player} object.
	 */
	public static Player createModelPlayer(final int playerNumber) {
		if (playerNumber == 1) {
			return new Player(PLAYER_1_ID, PLAYER_1_FORENAME, PLAYER_1_SURNAME, TEST_TEAM_1, Position.DEFENDER, PLAYER_1_POINTS);
		}
		
		return new Player(PLAYER_2_ID, PLAYER_2_FORENAME, PLAYER_2_SURNAME, TEST_TEAM_1, Position.STRIKER, PLAYER_2_POINTS);
	}
	
	/**
	 * Create a {@link PlayerEntity} object.
	 * 
	 * @param playerNumber Whether you want player 1 or 2.
	 * @return {@link PlayerEntity} object.
	 */
	public static PlayerEntity createEntityPlayer(final int playerNumber) {
		if (playerNumber == 1) {
			return new PlayerEntity(PLAYER_1_ID, PLAYER_1_FORENAME, PLAYER_1_SURNAME, TEST_TEAM_1, Position.DEFENDER, PLAYER_1_POINTS);
		}
		
		return new PlayerEntity(PLAYER_2_ID, PLAYER_2_FORENAME, PLAYER_2_SURNAME, TEST_TEAM_1, Position.STRIKER, PLAYER_2_POINTS);
	}
	
	/**
	 * Create a {@link ManagerEntity} object.
	 * 
	 * @param team Team to associate the manager with,
	 * @return {@link ManagerEntity} object.
	 */
	public static ManagerEntity createManager(final TeamEntity team) {
		return new ManagerEntity(MANAGER_EMAIL_ADDRESS, MANAGER_FORENAME, MANAGER_SURNAME, team);
	}
	
	/**
	 * Create a list of {@link TeamEntity} objects with the total score attribute populated.
	 * @return List of {@link TeamEntity} objects with the total score attribute populated.
	 */
	public static List<TeamEntity> createTeamEntitiesWithScore() {
		final TeamEntity team1 = new TeamEntity(TEST_TEAM_1);
		team1.setTotalScore(TEST_TEAM_1_SCORE);
		team1.addSelectedPlayers(Arrays.asList(buildSelectedPlayer(PLAYER_1_ID, Position.DEFENDER, SelectedPlayerStartingElevenStatus.PICKED)));
		team1.addGameweekScore(new GameweekScoreEntity(1, TEST_TEAM_1_WEEK_SCORE));
		
		final TeamEntity team2 = new TeamEntity(TEST_TEAM_2);
		team2.setTotalScore(TEST_TEAM_2_SCORE);
		team2.addSelectedPlayers(Arrays.asList(buildSelectedPlayer(PLAYER_2_ID, Position.DEFENDER, SelectedPlayerStartingElevenStatus.PICKED)));
		team2.addGameweekScore(new GameweekScoreEntity(1, TEST_TEAM_2_WEEK_SCORE));
		
		final TeamEntity team3 = new TeamEntity(TEST_TEAM_3);
		team3.setTotalScore(TEST_TEAM_3_SCORE);
		team3.addSelectedPlayers(Arrays.asList(buildSelectedPlayer(PLAYER_14_ID, Position.DEFENDER, SelectedPlayerStartingElevenStatus.PICKED)));
		team3.addGameweekScore(new GameweekScoreEntity(1, TEST_TEAM_3_WEEK_SCORE));
		
		return Arrays.asList(team1, team2, team3);
	}
	
	/**
	 * Create a {@link SelectedPlayerEntity} object.
	 * 
	 * @param playerId ID of the player to create.
	 * @param position Position of the player.
	 * @param selectionStatus Selection status of the player.
	 * @return The created {@link SelectedPlayerEntity}.
	 */
	public static SelectedPlayerEntity buildSelectedPlayer(final int playerId, final Position position, final SelectedPlayerStartingElevenStatus selectionStatus) {
		final PlayerEntity player = new PlayerEntity();
		player.setId(playerId);
		player.setPosition(position);
		
		final SelectedPlayerEntity selectedPlayer = new SelectedPlayerEntity();
		selectedPlayer.setPlayer(player);
		selectedPlayer.setSelectionStatus(selectionStatus);
		selectedPlayer.setFplCostAtPurchase(new BigDecimal(FPL_COST_AT_PURCHASE));
		selectedPlayer.setCost(new BigDecimal(SELECTED_PLAYER_COST));
		selectedPlayer.setCurrentSellToPotPrice(new BigDecimal(SELECTED_PLAYER_COST));
		selectedPlayer.setStillSelected(SelectedPlayerStatus.STILL_SELECTED);
		
		return selectedPlayer;
	}
	
	public static Map<Integer, GameweekScorePlayer> buildFullGameweekScores() {
		final Map<Integer, GameweekScorePlayer> gameweekScores = new HashMap<Integer, GameweekScorePlayer>();
		
		gameweekScores.put(PLAYER_1_ID, buildGameweekScorePlayer(PLAYER_1_ID, true));
		gameweekScores.put(PLAYER_2_ID, buildGameweekScorePlayer(PLAYER_2_ID, true));
		gameweekScores.put(PLAYER_7_ID, buildGameweekScorePlayer(PLAYER_7_ID, true));
		gameweekScores.put(PLAYER_13_ID, buildGameweekScorePlayer(PLAYER_13_ID, true));
		gameweekScores.put(PLAYER_14_ID, buildGameweekScorePlayer(PLAYER_14_ID, false));
		
		return gameweekScores;
	}
	
	public static GameweekScorePlayer buildGameweekScorePlayer(final int playerId, final boolean played) {
		final GameweekScorePlayer player = new GameweekScorePlayer();
		
		player.setId(playerId);
		
		if (played) {
			player.setMinutesPlayed(10);
			player.setScore(POINTS_FOR_PLAYING);
		}
		
		return player;
	}
	
	public static List<SelectedPlayerEntity> buildSquadList() {
		final List<SelectedPlayerEntity> fullSquad = new ArrayList<SelectedPlayerEntity>();
		
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = buildStartingTeam(2, 5, 5, 4);
		
		fullSquad.addAll(startingTeam.get(Position.GOALKEEPER));
		fullSquad.addAll(startingTeam.get(Position.DEFENDER));
		fullSquad.addAll(startingTeam.get(Position.MIDFIEDER));
		fullSquad.addAll(startingTeam.get(Position.STRIKER));
		
		for (final SelectedPlayerEntity player : fullSquad) {
			player.setSelectionStatus(SelectedPlayerStartingElevenStatus.PICKED);
		}

		updateSquadSubstitute(fullSquad, TestDataUtil.PLAYER_1_ID, Position.GOALKEEPER, SelectedPlayerStartingElevenStatus.SUB_5);
		updateSquadSubstitute(fullSquad, TestDataUtil.PLAYER_2_ID, Position.DEFENDER, SelectedPlayerStartingElevenStatus.SUB_4);
		updateSquadSubstitute(fullSquad, TestDataUtil.PLAYER_7_ID, Position.MIDFIEDER, SelectedPlayerStartingElevenStatus.SUB_2);
		updateSquadSubstitute(fullSquad, TestDataUtil.PLAYER_13_ID, Position.STRIKER, SelectedPlayerStartingElevenStatus.SUB_1);
		updateSquadSubstitute(fullSquad, TestDataUtil.PLAYER_14_ID, Position.STRIKER, SelectedPlayerStartingElevenStatus.SUB_3);

		return fullSquad;
	}
	
	public static Map<Position, List<SelectedPlayerEntity>> buildStartingTeam(final int noOfGoalkeepers, final int noOfDefenders, 
			final int noOfMidfielders, final int noOfStrikers) {
		final Map<Position, List<SelectedPlayerEntity>> team = new HashMap<Position, List<SelectedPlayerEntity>>();
		
		team.put(Position.GOALKEEPER, buildSelectedPlayerList(noOfGoalkeepers));
		team.put(Position.DEFENDER, buildSelectedPlayerList(noOfDefenders));
		team.put(Position.MIDFIEDER, buildSelectedPlayerList(noOfMidfielders));
		team.put(Position.STRIKER, buildSelectedPlayerList(noOfStrikers));
		
		return team;
	}
	
	private static void updateSquadSubstitute(final List<SelectedPlayerEntity> fullSquad, final int subListPosition, 
			final Position subPosition, final SelectedPlayerStartingElevenStatus subStatus) {
		
		final SelectedPlayerEntity selectedPlayer = fullSquad.get(subListPosition); 
		
		final PlayerEntity player = new PlayerEntity();
		player.setPosition(subPosition);
		player.setId(subListPosition);
		
		selectedPlayer.setSelectionStatus(subStatus);
		selectedPlayer.setPlayer(player);
		
		fullSquad.set(subListPosition, selectedPlayer);
	}
	
	private static List<SelectedPlayerEntity> buildSelectedPlayerList(final int noOfPlayers) {
		final List<SelectedPlayerEntity> players = new ArrayList<SelectedPlayerEntity>();
		
		for (int i = 0; i < noOfPlayers; i++) {
			players.add(new SelectedPlayerEntity());
		}
		
		return players;
	}
}
