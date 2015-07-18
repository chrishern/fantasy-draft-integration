/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.Test;

/**
 * Unit tests for the {@link League} class.
 * 
 * @author Chris Hern
 * 
 */
public class LeagueTest {

    @Test
    public void testAddTeam() {
        // arrange
        final League league = new League(TestDataConstants.LEAGUE_ONE_NAME);
        final Team team = new Team(TestDataConstants.TEAM_ONE_NAME);

        // act
        league.addTeam(team);

        // assert
        assertThat(league.getTeams()).containsOnly(team);
        assertThat(team.getLeague()).isEqualTo(league);
    }
}
