/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.player.FplCostPlayer;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.player.types.Position;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link PlayerFacadeImpl}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerFacadeImplTest {

	private static final String PLAYER_1_CURRENT_PRICE = "8.0";
	private static final String PLAYER_2_CURRENT_PRICE = "8.7";
	private static final String PLAYER_14_CURRENT_PRICE = "8.5";

	@Mock
	private PlayerDataService playerDataService;
	
	@Mock
	private LeagueDataService leagueDataService;
	
	@Mock
	private TeamDataService teamDataService;
	
	@InjectMocks
	private PlayerFacadeImpl playerFacade = new PlayerFacadeImpl();
	
	@Captor
	private ArgumentCaptor<List<PlayerEntity>> playerListCaptor;
	
	@Captor
	private ArgumentCaptor<PlayerEntity> playerCaptor;
	
	@Captor
	private ArgumentCaptor<TeamEntity> teamCaptor;
	
	@Test
	public void testAddPlayers() {
		// arrange
		final Player modelPlayer1 = TestDataUtil.createModelPlayer(1);
		final Player modelPlayer2 = TestDataUtil.createModelPlayer(2);
		final List<Player> modelPlayers = Arrays.asList(modelPlayer1, modelPlayer2);
		
		// act
		playerFacade.addPlayers(modelPlayers);
		
		// assert
		verify(playerDataService).addPlayers(playerListCaptor.capture());
		
		final List<PlayerEntity> value = playerListCaptor.getValue();
		assertThat(value).hasSize(2);
		
		final PlayerEntity firstPlayer = value.get(0);
		assertThat(firstPlayer.getId()).isEqualTo(modelPlayer1.getId());
		assertThat(firstPlayer.getForename()).isEqualTo(modelPlayer1.getForename());
		assertThat(firstPlayer.getSurname()).isEqualTo(modelPlayer1.getSurname());
		assertThat(firstPlayer.getTeam()).isEqualTo(modelPlayer1.getTeam());
		assertThat(firstPlayer.getPosition()).isEqualTo(modelPlayer1.getPosition());
		assertThat(firstPlayer.getTotalPoints()).isEqualTo(modelPlayer1.getTotalPoints());
		assertThat(firstPlayer.getSelectionStatus()).isEqualTo(PlayerSelectionStatus.NOT_SELECTED);
	}

	@Test
	public void testGetPlayers() {
		// arrange
		final PlayerEntity entityPlayer1 = TestDataUtil.createEntityPlayer(1);
		final PlayerEntity entityPlayer2 = TestDataUtil.createEntityPlayer(2);
		
		when(playerDataService.getPlayers()).thenReturn(Arrays.asList(entityPlayer1, entityPlayer2));
		
		// act
		final List<Player> players = playerFacade.getPlayers();
		
		// assert
		assertThat(players).hasSize(2);
		
		final Player firstPlayer = players.get(0);
		assertThat(firstPlayer.getId()).isEqualTo(entityPlayer1.getId());
		assertThat(firstPlayer.getForename()).isEqualTo(entityPlayer1.getForename());
		assertThat(firstPlayer.getSurname()).isEqualTo(entityPlayer1.getSurname());
		assertThat(firstPlayer.getTeam()).isEqualTo(entityPlayer1.getTeam());
		assertThat(firstPlayer.getPosition()).isEqualTo(entityPlayer1.getPosition());
		assertThat(firstPlayer.getTotalPoints()).isEqualTo(entityPlayer1.getTotalPoints());
	}
	
	@Test
	public void testGetPlayers_ByPosition() {
		// arrange
		final PlayerEntity entityPlayer1 = TestDataUtil.createEntityPlayer(1);
		
		when(playerDataService.getPlayers(Position.DEFENDER)).thenReturn(Arrays.asList(entityPlayer1));
		
		// act
		final List<Player> players = playerFacade.getPlayers(Position.DEFENDER);
		
		// assert
		assertThat(players).hasSize(1);
		
		final Player firstPlayer = players.get(0);
		assertThat(firstPlayer.getId()).isEqualTo(entityPlayer1.getId());
		assertThat(firstPlayer.getForename()).isEqualTo(entityPlayer1.getForename());
		assertThat(firstPlayer.getSurname()).isEqualTo(entityPlayer1.getSurname());
		assertThat(firstPlayer.getTeam()).isEqualTo(entityPlayer1.getTeam());
		assertThat(firstPlayer.getPosition()).isEqualTo(entityPlayer1.getPosition());
		assertThat(firstPlayer.getTotalPoints()).isEqualTo(entityPlayer1.getTotalPoints());
	}
	
	@Test
	public void testUpdatePlayersCurrentPrice() {
		// arrange
		final LeagueEntity leagueEntity = new LeagueEntity(TestDataUtil.LEAGUE_NAME);
		leagueEntity.setTeams(TestDataUtil.createTeamEntitiesWithScore());
		
		final PlayerEntity entityPlayer1 = TestDataUtil.createEntityPlayer(1);
		entityPlayer1.setCurrentPrice(new BigDecimal(TestDataUtil.FPL_COST_AT_PURCHASE));
		
		final PlayerEntity entityPlayer2 = TestDataUtil.createEntityPlayer(2);
		entityPlayer2.setCurrentPrice(new BigDecimal(TestDataUtil.FPL_COST_AT_PURCHASE));
		
		final PlayerEntity entityPlayer14 = new PlayerEntity(TestDataUtil.PLAYER_14_ID, 
				TestDataUtil.PLAYER_1_FORENAME, TestDataUtil.PLAYER_1_SURNAME, TestDataUtil.TEST_TEAM_1, Position.DEFENDER, TestDataUtil.PLAYER_1_POINTS);
		entityPlayer14.setCurrentPrice(new BigDecimal(TestDataUtil.FPL_COST_AT_PURCHASE));
		
		final FplCostPlayer fplCostPlayer1 = new FplCostPlayer(
				TestDataUtil.PLAYER_1_ID, new BigDecimal(TestDataUtil.FPL_COST_AT_PURCHASE), new BigDecimal(PLAYER_1_CURRENT_PRICE));
		
		final FplCostPlayer fplCostPlayer2 = new FplCostPlayer(
				TestDataUtil.PLAYER_2_ID, new BigDecimal(TestDataUtil.FPL_COST_AT_PURCHASE), new BigDecimal(PLAYER_2_CURRENT_PRICE));
		
		final FplCostPlayer fplCostPlayer14 = new FplCostPlayer(
				TestDataUtil.PLAYER_14_ID, new BigDecimal(TestDataUtil.FPL_COST_AT_PURCHASE), new BigDecimal(PLAYER_14_CURRENT_PRICE));
		
		when(playerDataService.getPlayers()).thenReturn(Arrays.asList(entityPlayer1, entityPlayer2, entityPlayer14));
		when(leagueDataService.getLeagues()).thenReturn(Arrays.asList(leagueEntity));
		
		// act
		final Map<Integer, FplCostPlayer> fplPlayerData = new HashMap<Integer, FplCostPlayer>();
		fplPlayerData.put(TestDataUtil.PLAYER_1_ID, fplCostPlayer1);
		fplPlayerData.put(TestDataUtil.PLAYER_2_ID, fplCostPlayer2);
		fplPlayerData.put(TestDataUtil.PLAYER_14_ID, fplCostPlayer14);
		
		playerFacade.updatePlayersCurrentPrice(fplPlayerData);
		
		// assert
		verify(playerDataService, times(3)).updatePlayer(playerCaptor.capture());
		verify(teamDataService, times(3)).updateTeam(teamCaptor.capture());
		
		final List<PlayerEntity> updatedPlayerEntities = playerCaptor.getAllValues();
		
		assertThat(updatedPlayerEntities).hasSize(3);
		assertPlayerUpdatedWithCorrectPrice(TestDataUtil.PLAYER_1_ID, Double.valueOf(PLAYER_1_CURRENT_PRICE), updatedPlayerEntities);
		assertPlayerUpdatedWithCorrectPrice(TestDataUtil.PLAYER_2_ID, Double.valueOf(PLAYER_2_CURRENT_PRICE), updatedPlayerEntities);
		assertPlayerUpdatedWithCorrectPrice(TestDataUtil.PLAYER_14_ID, Double.valueOf(PLAYER_14_CURRENT_PRICE), updatedPlayerEntities);
		
		final List<TeamEntity> updatedTeamEntities = teamCaptor.getAllValues();
		
		assertThat(updatedTeamEntities).hasSize(3);
		assertTeamSelectedPlayerUpdatedWithCorrectResalePrice(TestDataUtil.TEST_TEAM_1, Double.valueOf("5.25"), updatedTeamEntities);
		assertTeamSelectedPlayerUpdatedWithCorrectResalePrice(TestDataUtil.TEST_TEAM_2, Double.valueOf("7.7"), updatedTeamEntities);
		assertTeamSelectedPlayerUpdatedWithCorrectResalePrice(TestDataUtil.TEST_TEAM_3, Double.valueOf("7"), updatedTeamEntities);
	}
	
	private void assertPlayerUpdatedWithCorrectPrice(final int playerId, final double expectedCurrentPrice, final List<PlayerEntity> capturedValues) {
		for (final PlayerEntity player : capturedValues) {
			if (player.getId() == playerId) {
				assertThat(player.getCurrentPrice().doubleValue()).isEqualTo(expectedCurrentPrice);
				return;
			}
		}
		
		Assert.fail(String.format("Player with id %s not found when trying to assert player has been updated with the correct current price", playerId));
	}
	
	private void assertTeamSelectedPlayerUpdatedWithCorrectResalePrice(final String teamName, final double expectedResalePrice, final List<TeamEntity> capturedValues) {
		for (final TeamEntity team : capturedValues) {
			if (team.getName().equals(teamName)) {
				assertThat(team.getSelectedPlayers().get(0).getCurrentSellToPotPrice().doubleValue()).isEqualTo(expectedResalePrice);
				return;
			}
		}
		
		Assert.fail(String.format("Team with name %s not found when trying to assert selected player has been updated with the correct resale price", teamName));
	}
}
