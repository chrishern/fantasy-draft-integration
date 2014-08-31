/**
 * 
 */
package net.blackcat.fantasy.draft.integration.util;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.player.GameweekScorePlayer;
import net.blackcat.fantasy.draft.player.types.Position;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;

import org.junit.Test;

/**
 * Unit tests for {@link TeamSelectionUtils}.
 * 
 * @author Chris
 *
 */
public class TeamSelectionUtilsTest {

	private static final int PLAYER_ID_1 = 1;
	private static final int PLAYER_ID_2 = 2;
	private static final int PLAYER_ID_3 = 3;
	private static final int PLAYER_ID_4 = 4;
	private static final int PLAYER_ID_5 = 5;
	private static final int PLAYER_ID_6 = 6;
	private static final int PLAYER_ID_7 = 7;
	private static final int PLAYER_ID_13 = 13;
	private static final int PLAYER_ID_14 = 14;

	@Test
	public void testIsValidFormation_Valid_451() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = buildStartingTeam(1, 4, 5, 1);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Valid_442() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = buildStartingTeam(1, 4, 4, 2);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Valid_432() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = buildStartingTeam(1, 4, 3, 2);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Valid_430() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = buildStartingTeam(1, 4, 3, 0);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Valid_NoGoalkeepers() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = buildStartingTeam(0, 4, 4, 2);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Invalid_TooManyGoalkeepers() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = buildStartingTeam(2, 3, 4, 2);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isFalse();
	}
	
	@Test
	public void testIsValidFormation_Invalid_640() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = buildStartingTeam(1, 6, 4, 0);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isFalse();
	}
	
	@Test
	public void testIsValidFormation_Invalid_460() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = buildStartingTeam(1, 4, 6, 1);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isFalse();
	}
	
	@Test
	public void testIsValidFormation_Invalid_334() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = buildStartingTeam(1, 3, 3, 4);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isFalse();
	}
	
	@Test
	public void testBuildTeamFromPickedPlayersForWeek() {
		// arrange
		final Map<Integer, GameweekScorePlayer> gameweekScores = new HashMap<Integer, GameweekScorePlayer>();

		gameweekScores.put(PLAYER_ID_1, buildGameweekScorePlayer(PLAYER_ID_1, true));
		gameweekScores.put(PLAYER_ID_2, buildGameweekScorePlayer(PLAYER_ID_2, true));
		gameweekScores.put(PLAYER_ID_3, buildGameweekScorePlayer(PLAYER_ID_3, false));
		gameweekScores.put(PLAYER_ID_4, buildGameweekScorePlayer(PLAYER_ID_4, true));
		gameweekScores.put(PLAYER_ID_5, buildGameweekScorePlayer(PLAYER_ID_5, true));
		gameweekScores.put(PLAYER_ID_6, buildGameweekScorePlayer(PLAYER_ID_6, false));
		
		final SelectedPlayerEntity selectedPlayer1 = buildSelectedPlayer(PLAYER_ID_1, Position.GOALKEEPER, SelectedPlayerStatus.CAPTAIN);
		final SelectedPlayerEntity selectedPlayer2 = buildSelectedPlayer(PLAYER_ID_2, Position.DEFENDER, SelectedPlayerStatus.SUB_1);
		final SelectedPlayerEntity selectedPlayer3 = buildSelectedPlayer(PLAYER_ID_3, Position.MIDFIEDER, SelectedPlayerStatus.PICKED);
		final SelectedPlayerEntity selectedPlayer4 = buildSelectedPlayer(PLAYER_ID_4, Position.MIDFIEDER, SelectedPlayerStatus.PICKED);
		final SelectedPlayerEntity selectedPlayer6 = buildSelectedPlayer(PLAYER_ID_6, Position.STRIKER, SelectedPlayerStatus.PICKED);
		
		final TeamEntity team = new TeamEntity();
		team.addSelectedPlayers(Arrays.asList(selectedPlayer1, selectedPlayer2, selectedPlayer3, selectedPlayer4, selectedPlayer6));

		final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek = new HashMap<Position, List<SelectedPlayerEntity>>();
		
		// act
		final int numberOfPickedPlayersInTeam = TeamSelectionUtils.buildTeamFromPickedPlayersForWeek(gameweekScores, team, startingTeamForWeek);
		
		// assert
		assertThat(numberOfPickedPlayersInTeam).isEqualTo(2);
		assertThat(startingTeamForWeek.keySet()).hasSize(2);
		assertThat(startingTeamForWeek.get(Position.GOALKEEPER)).hasSize(1);
		assertThat(startingTeamForWeek.get(Position.DEFENDER)).isNull();
		assertThat(startingTeamForWeek.get(Position.MIDFIEDER)).hasSize(1);
		assertThat(startingTeamForWeek.get(Position.STRIKER)).isNull();
	}
	
	@Test
	public void testIsPlayerASubstitute_True() {
		// arrange
		final SelectedPlayerEntity selectedPlayer = buildSelectedPlayer(PLAYER_ID_1, Position.DEFENDER, SelectedPlayerStatus.SUB_1);
		
		// act
		final boolean isPlayerASubstitute = TeamSelectionUtils.isPlayerASubstitute(selectedPlayer);
		
		// assert
		assertThat(isPlayerASubstitute).isTrue();
	}
	
	@Test
	public void testIsPlayerASubstitute_False() {
		// arrange
		final SelectedPlayerEntity selectedPlayer = buildSelectedPlayer(PLAYER_ID_1, Position.DEFENDER, SelectedPlayerStatus.CAPTAIN);
		
		// act
		final boolean isPlayerASubstitute = TeamSelectionUtils.isPlayerASubstitute(selectedPlayer);
		
		// assert
		assertThat(isPlayerASubstitute).isFalse();
	}
	
	@Test
	public void testAddPlayerToStartingTeam() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek = new HashMap<Position, List<SelectedPlayerEntity>>();
		final SelectedPlayerEntity selectedPlayer1 = buildSelectedPlayer(PLAYER_ID_1, Position.DEFENDER, SelectedPlayerStatus.CAPTAIN);
		final SelectedPlayerEntity selectedPlayer2 = buildSelectedPlayer(PLAYER_ID_2, Position.DEFENDER, SelectedPlayerStatus.PICKED);
		
		// act
		TeamSelectionUtils.addPlayerToStartingTeam(startingTeamForWeek, selectedPlayer1, Position.DEFENDER);
		TeamSelectionUtils.addPlayerToStartingTeam(startingTeamForWeek, selectedPlayer2, Position.DEFENDER);
		
		// assert
		assertThat(startingTeamForWeek.keySet()).hasSize(1);
		assertThat(startingTeamForWeek.get(Position.GOALKEEPER)).isNull();
		assertThat(startingTeamForWeek.get(Position.DEFENDER)).hasSize(2);
		assertThat(startingTeamForWeek.get(Position.MIDFIEDER)).isNull();
		assertThat(startingTeamForWeek.get(Position.STRIKER)).isNull();
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_TeamAlreadyComplete() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = buildStartingTeam(1, 4, 4, 2);
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 2);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 11, buildSquadList(), buildFullGameweekScores());
		
		// assert
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 2);
	}

	@Test
	public void testFillTeamUpWithSubstitutes_AddGoalkeeper() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = buildStartingTeam(0, 4, 4, 2);
		
		assertFullSquadDetails(startingTeam, 4, 0, 4, 4, 2);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 10, buildSquadList(), buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 2);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub442() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = buildStartingTeam(1, 4, 4, 1);
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 1);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 10, buildSquadList(), buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 2);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub1_433() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = buildStartingTeam(1, 4, 3, 2);
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 3, 2);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 10, buildSquadList(), buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 3, 3);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub1_343() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = buildStartingTeam(1, 3, 4, 2);
		
		assertFullSquadDetails(startingTeam, 4, 1, 3, 4, 2);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 10, buildSquadList(), buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 3, 4, 3);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub1And2() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = buildStartingTeam(1, 4, 4, 0);
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 0);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 9, buildSquadList(), buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 5, 1);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub1And2And4() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = buildStartingTeam(1, 2, 4, 0);
		
		assertFullSquadDetails(startingTeam, 4, 1, 2, 4, 0);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 7, buildSquadList(), buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 3, 5, 1);
	}
	
	private void assertFullSquadDetails(final Map<Position, List<SelectedPlayerEntity>> startingTeam, 
			final int expectedNumberOfPositions, final int expectedNumberOfGoalkeepers, 
			final int expectedNumberOfDefenders, final int expectedNumberOfMidfielders, final int expectedNumberOfStrikers) {
		
		assertThat(startingTeam.keySet()).hasSize(expectedNumberOfPositions);
		assertThat(startingTeam.get(Position.GOALKEEPER)).hasSize(expectedNumberOfGoalkeepers);
		assertThat(startingTeam.get(Position.DEFENDER)).hasSize(expectedNumberOfDefenders);
		assertThat(startingTeam.get(Position.MIDFIEDER)).hasSize(expectedNumberOfMidfielders);
		assertThat(startingTeam.get(Position.STRIKER)).hasSize(expectedNumberOfStrikers);
	}
	
	private SelectedPlayerEntity buildSelectedPlayer(final int playerId, final Position position, final SelectedPlayerStatus selectionStatus) {
		final PlayerEntity player = new PlayerEntity();
		player.setId(playerId);
		player.setPosition(position);
		
		final SelectedPlayerEntity selectedPlayer = new SelectedPlayerEntity();
		selectedPlayer.setPlayer(player);
		selectedPlayer.setSelectionStatus(selectionStatus);
		
		return selectedPlayer;
	}
	
	private Map<Integer, GameweekScorePlayer> buildFullGameweekScores() {
		final Map<Integer, GameweekScorePlayer> gameweekScores = new HashMap<Integer, GameweekScorePlayer>();
		
		gameweekScores.put(PLAYER_ID_1, buildGameweekScorePlayer(PLAYER_ID_1, true));
		gameweekScores.put(PLAYER_ID_2, buildGameweekScorePlayer(PLAYER_ID_2, true));
		gameweekScores.put(PLAYER_ID_7, buildGameweekScorePlayer(PLAYER_ID_7, true));
		gameweekScores.put(PLAYER_ID_13, buildGameweekScorePlayer(PLAYER_ID_13, true));
		gameweekScores.put(PLAYER_ID_14, buildGameweekScorePlayer(PLAYER_ID_14, false));
		
		return gameweekScores;
	}
	
	private GameweekScorePlayer buildGameweekScorePlayer(final int playerId, final boolean played) {
		final GameweekScorePlayer player = new GameweekScorePlayer();
		
		player.setId(playerId);
		
		if (played) {
			player.setMinutesPlayed(10);
			player.setScore(2);
		}
		
		return player;
	}
	
	private Map<Position, List<SelectedPlayerEntity>> buildStartingTeam(final int noOfGoalkeepers, final int noOfDefenders, 
			final int noOfMidfielders, final int noOfStrikers) {
		final Map<Position, List<SelectedPlayerEntity>> team = new HashMap<Position, List<SelectedPlayerEntity>>();
		
		team.put(Position.GOALKEEPER, buildSelectedPlayerList(noOfGoalkeepers));
		team.put(Position.DEFENDER, buildSelectedPlayerList(noOfDefenders));
		team.put(Position.MIDFIEDER, buildSelectedPlayerList(noOfMidfielders));
		team.put(Position.STRIKER, buildSelectedPlayerList(noOfStrikers));
		
		return team;
	}
	
	private List<SelectedPlayerEntity> buildSelectedPlayerList(final int noOfPlayers) {
		final List<SelectedPlayerEntity> players = new ArrayList<SelectedPlayerEntity>();
		
		for (int i = 0; i < noOfPlayers; i++) {
			players.add(new SelectedPlayerEntity());
		}
		
		return players;
	}
	
	private List<SelectedPlayerEntity> buildSquadList() {
		final List<SelectedPlayerEntity> fullSquad = new ArrayList<SelectedPlayerEntity>();
		
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = buildStartingTeam(2, 5, 5, 4);
		
		fullSquad.addAll(startingTeam.get(Position.GOALKEEPER));
		fullSquad.addAll(startingTeam.get(Position.DEFENDER));
		fullSquad.addAll(startingTeam.get(Position.MIDFIEDER));
		fullSquad.addAll(startingTeam.get(Position.STRIKER));
		
		for (final SelectedPlayerEntity player : fullSquad) {
			player.setSelectionStatus(SelectedPlayerStatus.PICKED);
		}

		updateSquadSubstitute(fullSquad, PLAYER_ID_1, Position.GOALKEEPER, SelectedPlayerStatus.SUB_5);
		updateSquadSubstitute(fullSquad, PLAYER_ID_2, Position.DEFENDER, SelectedPlayerStatus.SUB_4);
		updateSquadSubstitute(fullSquad, PLAYER_ID_7, Position.MIDFIEDER, SelectedPlayerStatus.SUB_2);
		updateSquadSubstitute(fullSquad, PLAYER_ID_13, Position.STRIKER, SelectedPlayerStatus.SUB_1);
		updateSquadSubstitute(fullSquad, PLAYER_ID_14, Position.STRIKER, SelectedPlayerStatus.SUB_3);

		return fullSquad;
	}
	
	private void updateSquadSubstitute(final List<SelectedPlayerEntity> fullSquad, final int subListPosition, 
			final Position subPosition, final SelectedPlayerStatus subStatus) {
		
		final SelectedPlayerEntity selectedPlayer = fullSquad.get(subListPosition); 
		
		final PlayerEntity player = new PlayerEntity();
		player.setPosition(subPosition);
		player.setId(subListPosition);
		
		selectedPlayer.setSelectionStatus(subStatus);
		selectedPlayer.setPlayer(player);
		
		fullSquad.set(subListPosition, selectedPlayer);
	}
}
