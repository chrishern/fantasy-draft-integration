/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.Test;

/**
 * Unit tests for {@link TeamPointsDto}.
 * 
 * @author Chris
 *
 */
public class TeamPointsDtoTest {

	@Test
	public void testCompareTo() {
		// arrange
		final TeamPointsDto team1 = new TeamPointsDto(TestDataConstants.TEAM_ONE_NAME, 2, 13);
		final TeamPointsDto team2 = new TeamPointsDto(TestDataConstants.TEAM_TWO_NAME, 1, 11);
		final TeamPointsDto team3 = new TeamPointsDto(TestDataConstants.TEAM_THREE_NAME, 14, 45);
		
		final List<TeamPointsDto> teams = Arrays.asList(team1, team2, team3);
		
		// act
		Collections.sort(teams);
		
		// assert
		assertThat(teams).containsExactly(team3, team1, team2);
	}

}
