/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.User;

/**
 * Class for building instances of {@link User} objects to use in unit tests.
 * 
 * @author Chris
 * 
 */
public class UserTestDataBuilder {

    private static final String MANAGER_DEFAULT_EMAIL_ADDRESS = "manager@user.com";
    private static final String MANAGER_DEFAULT_FORENAME = "Manager";
    private static final String MANAGER_DEFAULT_SURNAME = "User";
    private static final String TEAM_NAME = "Test Team";

    private User user;
    private boolean withTeam = false;

    public static UserTestDataBuilder aManager() {

        final UserTestDataBuilder builder = new UserTestDataBuilder();

        builder.user = User.buildManager(MANAGER_DEFAULT_EMAIL_ADDRESS, MANAGER_DEFAULT_FORENAME, MANAGER_DEFAULT_SURNAME);

        return builder;
    }

    public static UserTestDataBuilder aManager(final String emailAddress, final String forename, final String surname) {

        final UserTestDataBuilder builder = new UserTestDataBuilder();

        builder.user = User.buildManager(emailAddress, forename, surname);

        return builder;
    }

    public static UserTestDataBuilder aManagerWithEmailAddress(final String emailAddress) {

        final UserTestDataBuilder builder = new UserTestDataBuilder();

        builder.user = User.buildManager(emailAddress, MANAGER_DEFAULT_FORENAME, MANAGER_DEFAULT_SURNAME);

        return builder;
    }

    public UserTestDataBuilder withTeam() {

        withTeam = true;
        return this;
    }

    public User build() {

        if (withTeam) {
            final Team team = new Team(TEAM_NAME);

            user.addManagedTeam(team);
        }

        return user;
    }
}
