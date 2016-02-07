/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.types.player.StartingTeamStatus;
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
		final SelectedPlayerDto startingGoalkeeper = SelectedPlayerDtoTestDataBuilder.aGoalkeeper()
				.withStartingTeamStatus(StartingTeamStatus.PICKED)
				.build();
		
		final SelectedPlayerDto startingDefender = SelectedPlayerDtoTestDataBuilder.aDefender()
				.withStartingTeamStatus(StartingTeamStatus.CAPTAIN)
				.build();
		
		final SelectedPlayerDto startingMidfielder = SelectedPlayerDtoTestDataBuilder.aMidfielder()
				.withStartingTeamStatus(StartingTeamStatus.VICE_CAPTAIN)
				.build();
		
		final SelectedPlayerDto startingStriker = SelectedPlayerDtoTestDataBuilder.aStriker()
				.withStartingTeamStatus(StartingTeamStatus.PICKED)
				.build();
		
		final SelectedPlayerDto sub1 = SelectedPlayerDtoTestDataBuilder.aDefender()
				.withStartingTeamStatus(StartingTeamStatus.SUB_1)
				.build();
		
		final SelectedPlayerDto sub2 = SelectedPlayerDtoTestDataBuilder.aStriker()
				.withStartingTeamStatus(StartingTeamStatus.SUB_2)
				.build();
		
		final SelectedPlayerDto sub3 = SelectedPlayerDtoTestDataBuilder.aMidfielder()
				.withStartingTeamStatus(StartingTeamStatus.SUB_3)
				.build();
		
		
		final List<SelectedPlayerDto> players = Arrays.asList(sub3, startingMidfielder, sub1, startingDefender, sub2, startingStriker, startingGoalkeeper);
		
		// act
		Collections.sort(players);
		
		// assert
		assertThat(players).containsExactly(startingGoalkeeper, startingDefender, startingMidfielder, startingStriker, sub1, sub2, sub3);
	}
}
