/**
 * 
 */
package net.blackcat.fantasy.draft.controller;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.facade.PlayerFacade;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.test.util.TestDataUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link PlayerController}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerControllerTest {

	@Mock
	private PlayerFacade playerFacade;
	
	@InjectMocks
	private PlayerController playerController = new PlayerController();
	
	@Test
	public void testAddPlayers() {
		// arrange
		final List<Player> modelPlayers = Arrays.asList(TestDataUtil.createModelPlayer(1), TestDataUtil.createModelPlayer(2));
		
		// act
		playerController.addPlayers(modelPlayers);
		
		// assert
		verify(playerFacade).addPlayers(modelPlayers);
	}

	@Test
	public void testGetPlayers() {
		// arrange
		final Player player1 = TestDataUtil.createModelPlayer(1);
		final Player player2 = TestDataUtil.createModelPlayer(2);
		
		when(playerFacade.getPlayers()).thenReturn(Arrays.asList(player1, player2));
		
		// act
		final List<Player> players = playerController.getPlayers();
		
		// assert
		assertThat(players).hasSize(2);
		assertThat(players).containsOnly(player1, player2);
	}

}
