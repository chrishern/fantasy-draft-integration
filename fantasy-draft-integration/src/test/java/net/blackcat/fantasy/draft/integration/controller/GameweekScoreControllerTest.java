/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import static org.mockito.Mockito.verify;

import java.util.Map;

import net.blackcat.fantasy.draft.integration.facade.GameweekScoreFacade;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.player.GameweekScorePlayer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link GameweekScoreController}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameweekScoreControllerTest {

	@Mock
	private GameweekScoreFacade gameweekScoreFacade;
	
	@InjectMocks
	private GameweekScoreController gameweekScoreController = new GameweekScoreController();
	
	@Test
	public void testStoreGameweekScores() {
		// arrange
		final Map<Integer, GameweekScorePlayer> gameweekScores = TestDataUtil.buildFullGameweekScores(true);
		
		// act
		gameweekScoreController.storeGameweekScores(gameweekScores);
		
		// assert
		verify(gameweekScoreFacade).storeCurrentGameweekScores(gameweekScores);
	}

}
