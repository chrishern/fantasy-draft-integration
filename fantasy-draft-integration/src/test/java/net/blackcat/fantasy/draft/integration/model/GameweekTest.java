/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Unit tests for {@link Gameweek}.
 * 
 * @author Chris
 *
 */
public class GameweekTest {

	@Test
	public void testMoveToNextGameweek() {
		// arrange
		final Gameweek gameweek = new Gameweek();
		
		final int previousPreviousGameweek = gameweek.getPreviousGameweek();
		final int previousCurrentGameweek = gameweek.getCurrentGameweek();
		
		// act
		gameweek.moveToNextGameweek();
		
		// assert
		assertThat(gameweek.getPreviousGameweek()).isEqualTo(previousPreviousGameweek + 1);
		assertThat(gameweek.getCurrentGameweek()).isEqualTo(previousCurrentGameweek + 1);
	}

}
