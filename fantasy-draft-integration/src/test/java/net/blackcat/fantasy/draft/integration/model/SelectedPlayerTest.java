/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.types.player.Position;
import net.blackcat.fantasy.draft.integration.testdata.SelectedPlayerTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for {@link SelectedPlayer}.
 * 
 * @author Chris
 *
 */
public class SelectedPlayerTest {

	@Test
	public void testCompareTo() {
		// arrange
		final SelectedPlayer goalkeeper = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.GOALKEEPER).build();
		final SelectedPlayer defender = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.DEFENDER).build();
		final SelectedPlayer midfielder = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.MIDFIELDER).build();
		final SelectedPlayer striker = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.STRIKER).build();
		
		final List<SelectedPlayer> selectedPlayers = Arrays.asList(midfielder, striker, goalkeeper, defender);
		
		// act
		Collections.sort(selectedPlayers);
		
		// assert
		assertThat(selectedPlayers).containsExactly(goalkeeper, defender, midfielder, striker);
	}
}
