/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;
import net.blackcat.fantasy.draft.integration.testdata.dto.SelectedPlayerDtoTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for {@link SquadDto}.
 * 
 * @author Chris
 *
 */
public class SquadDtoTest {

	@Test
	public void testAddSelectedPlayer() {
		// arrange
		final SquadDto squad = new SquadDto(TestDataConstants.TEAM_ONE_ID, TestDataConstants.TEAM_ONE_NAME, new BigDecimal("44.5"));
		final SelectedPlayerDto striker = SelectedPlayerDtoTestDataBuilder.aStriker().build();
		final SelectedPlayerDto defender = SelectedPlayerDtoTestDataBuilder.aDefender().build();
		
		// act
		squad.addSelectedPlayer(striker);
		squad.addSelectedPlayer(defender);
		
		// assert
		assertThat(squad.getCurrentPlayers()).containsExactly(defender, striker);
	}

}
