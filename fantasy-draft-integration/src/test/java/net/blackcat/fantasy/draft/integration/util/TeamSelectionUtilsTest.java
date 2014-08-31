/**
 * 
 */
package net.blackcat.fantasy.draft.integration.util;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
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

	@Test
	public void testIsValidFormation_Valid_451() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = TestDataUtil.buildStartingTeam(1, 4, 5, 1);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Valid_442() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = TestDataUtil.buildStartingTeam(1, 4, 4, 2);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Valid_432() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = TestDataUtil.buildStartingTeam(1, 4, 3, 2);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Valid_430() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = TestDataUtil.buildStartingTeam(1, 4, 3, 0);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Valid_NoGoalkeepers() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = TestDataUtil.buildStartingTeam(0, 4, 4, 2);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isTrue();
	}
	
	@Test
	public void testIsValidFormation_Invalid_TooManyGoalkeepers() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = TestDataUtil.buildStartingTeam(2, 3, 4, 2);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isFalse();
	}
	
	@Test
	public void testIsValidFormation_Invalid_640() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = TestDataUtil.buildStartingTeam(1, 6, 4, 0);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isFalse();
	}
	
	@Test
	public void testIsValidFormation_Invalid_460() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = TestDataUtil.buildStartingTeam(1, 4, 6, 1);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isFalse();
	}
	
	@Test
	public void testIsValidFormation_Invalid_334() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> team = TestDataUtil.buildStartingTeam(1, 3, 3, 4);
		
		// act
		final boolean validFormation = TeamSelectionUtils.isValidFormation(team);
		
		// assert
		assertThat(validFormation).isFalse();
	}
	
	@Test
	public void testBuildTeamFromPickedPlayersForWeek() {
		// arrange
		final Map<Integer, GameweekScorePlayer> gameweekScores = new HashMap<Integer, GameweekScorePlayer>();

		gameweekScores.put(TestDataUtil.PLAYER_1_ID, TestDataUtil.buildGameweekScorePlayer(TestDataUtil.PLAYER_1_ID, true));
		gameweekScores.put(TestDataUtil.PLAYER_2_ID, TestDataUtil.buildGameweekScorePlayer(TestDataUtil.PLAYER_2_ID, true));
		gameweekScores.put(TestDataUtil.PLAYER_3_ID, TestDataUtil.buildGameweekScorePlayer(TestDataUtil.PLAYER_3_ID, false));
		gameweekScores.put(TestDataUtil.PLAYER_4_ID, TestDataUtil.buildGameweekScorePlayer(TestDataUtil.PLAYER_4_ID, true));
		gameweekScores.put(TestDataUtil.PLAYER_5_ID, TestDataUtil.buildGameweekScorePlayer(TestDataUtil.PLAYER_5_ID, true));
		gameweekScores.put(TestDataUtil.PLAYER_6_ID, TestDataUtil.buildGameweekScorePlayer(TestDataUtil.PLAYER_6_ID, false));
		
		final SelectedPlayerEntity selectedPlayer1 = TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_1_ID, Position.GOALKEEPER, SelectedPlayerStatus.CAPTAIN);
		final SelectedPlayerEntity selectedPlayer2 = TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_2_ID, Position.DEFENDER, SelectedPlayerStatus.SUB_1);
		final SelectedPlayerEntity selectedPlayer3 = TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_3_ID, Position.MIDFIEDER, SelectedPlayerStatus.PICKED);
		final SelectedPlayerEntity selectedPlayer4 = TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_4_ID, Position.MIDFIEDER, SelectedPlayerStatus.PICKED);
		final SelectedPlayerEntity selectedPlayer6 = TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_6_ID, Position.STRIKER, SelectedPlayerStatus.PICKED);
		
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
		final SelectedPlayerEntity selectedPlayer = TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_1_ID, Position.DEFENDER, SelectedPlayerStatus.SUB_1);
		
		// act
		final boolean isPlayerASubstitute = TeamSelectionUtils.isPlayerASubstitute(selectedPlayer);
		
		// assert
		assertThat(isPlayerASubstitute).isTrue();
	}
	
	@Test
	public void testIsPlayerASubstitute_False() {
		// arrange
		final SelectedPlayerEntity selectedPlayer = TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_1_ID, Position.DEFENDER, SelectedPlayerStatus.CAPTAIN);
		
		// act
		final boolean isPlayerASubstitute = TeamSelectionUtils.isPlayerASubstitute(selectedPlayer);
		
		// assert
		assertThat(isPlayerASubstitute).isFalse();
	}
	
	@Test
	public void testAddPlayerToStartingTeam() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek = new HashMap<Position, List<SelectedPlayerEntity>>();
		final SelectedPlayerEntity selectedPlayer1 = TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_1_ID, Position.DEFENDER, SelectedPlayerStatus.CAPTAIN);
		final SelectedPlayerEntity selectedPlayer2 = TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_2_ID, Position.DEFENDER, SelectedPlayerStatus.PICKED);
		
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
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = TestDataUtil.buildStartingTeam(1, 4, 4, 2);
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 2);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 11, TestDataUtil.buildSquadList(), TestDataUtil.buildFullGameweekScores());
		
		// assert
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 2);
	}

	@Test
	public void testFillTeamUpWithSubstitutes_AddGoalkeeper() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = TestDataUtil.buildStartingTeam(0, 4, 4, 2);
		
		assertFullSquadDetails(startingTeam, 4, 0, 4, 4, 2);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 10, TestDataUtil.buildSquadList(), TestDataUtil.buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 2);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub442() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = TestDataUtil.buildStartingTeam(1, 4, 4, 1);
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 1);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 10, TestDataUtil.buildSquadList(), TestDataUtil.buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 2);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub1_433() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = TestDataUtil.buildStartingTeam(1, 4, 3, 2);
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 3, 2);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 10, TestDataUtil.buildSquadList(), TestDataUtil.buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 3, 3);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub1_343() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = TestDataUtil.buildStartingTeam(1, 3, 4, 2);
		
		assertFullSquadDetails(startingTeam, 4, 1, 3, 4, 2);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 10, TestDataUtil.buildSquadList(), TestDataUtil.buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 3, 4, 3);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub1And2() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = TestDataUtil.buildStartingTeam(1, 4, 4, 0);
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 4, 0);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 9, TestDataUtil.buildSquadList(), TestDataUtil.buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 4, 5, 1);
	}
	
	@Test
	public void testFillTeamUpWithSubstitutes_AddSub1And2And4() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeam = TestDataUtil.buildStartingTeam(1, 2, 4, 0);
		
		assertFullSquadDetails(startingTeam, 4, 1, 2, 4, 0);
		
		// act
		TeamSelectionUtils.fillTeamUpWithSubstitutes(startingTeam, 7, TestDataUtil.buildSquadList(), TestDataUtil.buildFullGameweekScores());
		
		assertFullSquadDetails(startingTeam, 4, 1, 3, 5, 1);
	}
	
	@Test
	public void testCalculateTeamGameweekScore_WithCaptain() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek = new HashMap<Position, List<SelectedPlayerEntity>>();
		
		final List<SelectedPlayerEntity> goalkeeperList = Arrays.asList(TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_1_ID, Position.GOALKEEPER, SelectedPlayerStatus.CAPTAIN));
		final List<SelectedPlayerEntity> defenderList = Arrays.asList(TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_2_ID, Position.DEFENDER, SelectedPlayerStatus.VICE_CAPTAIN));
		final List<SelectedPlayerEntity> midfielderList = Arrays.asList(TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_7_ID, Position.DEFENDER, SelectedPlayerStatus.PICKED));
		final List<SelectedPlayerEntity> strikerList = Arrays.asList(TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_13_ID, Position.DEFENDER, SelectedPlayerStatus.PICKED));
		
		startingTeamForWeek.put(Position.GOALKEEPER, goalkeeperList);
		startingTeamForWeek.put(Position.DEFENDER, defenderList);
		startingTeamForWeek.put(Position.MIDFIEDER, midfielderList);
		startingTeamForWeek.put(Position.STRIKER, strikerList);
		
		// act
		final int gameweekScore = TeamSelectionUtils.calculateTeamGameweekScore(startingTeamForWeek, 1, TestDataUtil.buildFullGameweekScores());
		
		// assert
		assertThat(gameweekScore).isEqualTo(10);
	}
	
	@Test
	public void testCalculateTeamGameweekScore_WithoutCaptain() {
		// arrange
		final Map<Position, List<SelectedPlayerEntity>> startingTeamForWeek = new HashMap<Position, List<SelectedPlayerEntity>>();
		
		final List<SelectedPlayerEntity> goalkeeperList = Arrays.asList(TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_1_ID, Position.GOALKEEPER, SelectedPlayerStatus.PICKED));
		final List<SelectedPlayerEntity> defenderList = Arrays.asList(TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_2_ID, Position.DEFENDER, SelectedPlayerStatus.VICE_CAPTAIN));
		final List<SelectedPlayerEntity> midfielderList = Arrays.asList(TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_7_ID, Position.DEFENDER, SelectedPlayerStatus.PICKED));
		final List<SelectedPlayerEntity> strikerList = Arrays.asList(TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_13_ID, Position.DEFENDER, SelectedPlayerStatus.PICKED));
		
		startingTeamForWeek.put(Position.GOALKEEPER, goalkeeperList);
		startingTeamForWeek.put(Position.DEFENDER, defenderList);
		startingTeamForWeek.put(Position.MIDFIEDER, midfielderList);
		startingTeamForWeek.put(Position.STRIKER, strikerList);
		
		// act
		final int gameweekScore = TeamSelectionUtils.calculateTeamGameweekScore(startingTeamForWeek, 1, TestDataUtil.buildFullGameweekScores());
		
		// assert
		assertThat(gameweekScore).isEqualTo(10);
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
}
