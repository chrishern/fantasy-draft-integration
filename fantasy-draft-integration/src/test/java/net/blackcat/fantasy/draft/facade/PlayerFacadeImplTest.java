/**
 * 
 */
package net.blackcat.fantasy.draft.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.facade.PlayerFacadeImpl;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.player.types.Position;
import net.blackcat.fantasy.draft.test.util.TestDataUtil;

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

	@Mock
	private PlayerDataService playerDataService;
	
	@InjectMocks
	private PlayerFacadeImpl playerFacade = new PlayerFacadeImpl();
	
	@Captor
	private ArgumentCaptor<List<PlayerEntity>> captor;
	
	@Test
	public void testAddPlayers() {
		// arrange
		final Player modelPlayer1 = TestDataUtil.createModelPlayer(1);
		final Player modelPlayer2 = TestDataUtil.createModelPlayer(2);
		final List<Player> modelPlayers = Arrays.asList(modelPlayer1, modelPlayer2);
		
		// act
		playerFacade.addPlayers(modelPlayers);
		
		// assert
		verify(playerDataService).addPlayers(captor.capture());
		
		final List<PlayerEntity> value = captor.getValue();
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
}
