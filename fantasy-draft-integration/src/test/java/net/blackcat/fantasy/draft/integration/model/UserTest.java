/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.model.types.user.UserRole;

import org.junit.Test;

/**
 * Unit tests for the {@link User} class.
 * 
 * @author Chris
 * 
 */
public class UserTest {

    private static final String EMAIL_ADDRESS = "test@email.com";
    private static final String FORENAME = "Forename";
    private static final String SURNAME = "Surname";

    @Test
    public void testBuildManager() {
        // arrange

        // act
        final User manager = User.buildManager(EMAIL_ADDRESS, FORENAME, SURNAME);

        // assert
        assertUserData(manager, UserRole.MANAGER);
    }

    @Test
    public void testBuildLeagueAdministrator() {
        // arrange

        // act
        final User manager = User.buildLeagueAdministrator(EMAIL_ADDRESS, FORENAME, SURNAME);

        // assert
        assertUserData(manager, UserRole.LEAGUE_ADMIN);
    }

    @Test
    public void testBuildAdministrator() {
        // arrange

        // act
        final User manager = User.buildAdministrator(EMAIL_ADDRESS, FORENAME, SURNAME);

        // assert
        assertUserData(manager, UserRole.ADMIN);
    }

    @Test
    public void testAddManagedTeam() {
        // arrange
        final User manager = User.buildManager(EMAIL_ADDRESS, FORENAME, SURNAME);
        final Team team = new Team("Team name");

        // act
        manager.addManagedTeam(team);

        // assert
        assertThat(manager.getTeams()).containsOnly(team);
        assertThat(team.getManager()).isEqualTo(manager);
    }

    private void assertUserData(final User manager, final UserRole roleType) {

        assertThat(manager.getEmailAddress()).isEqualTo(EMAIL_ADDRESS);
        assertThat(manager.getForename()).isEqualTo(FORENAME);
        assertThat(manager.getSurname()).isEqualTo(SURNAME);
        assertThat(manager.getRole()).isEqualTo(roleType);
    }

}
