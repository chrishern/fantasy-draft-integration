/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.testdata.dto.SelectedPlayerDtoTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for {@link SelectedPlayerDto}.
 * 
 * @author Chris
 *
 */
public class SelectedPlayerDtoTest {

	@Test
	public void testCompareTo() {
		// arrange
		final SelectedPlayerDto goalkeeper = SelectedPlayerDtoTestDataBuilder.aGoalkeeper().build();
		final SelectedPlayerDto defender = SelectedPlayerDtoTestDataBuilder.aDefender().build();
		final SelectedPlayerDto midfielder = SelectedPlayerDtoTestDataBuilder.aMidfielder().build();
		final SelectedPlayerDto striker = SelectedPlayerDtoTestDataBuilder.aStriker().build();
		
		final List<SelectedPlayerDto> players = Arrays.asList(midfielder, defender, striker, goalkeeper);
		
		// act
		Collections.sort(players);
		
		// assert
		assertThat(players).containsExactly(goalkeeper, defender, midfielder, striker);
	}

}
