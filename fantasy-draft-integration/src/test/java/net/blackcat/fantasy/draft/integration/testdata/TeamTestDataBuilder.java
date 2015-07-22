/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.Team;

/**
 * Builder for building instances of {@link Team} objects to use in unit tests.
 * 
 * @author Chris Hern
 * 
 */
public class TeamTestDataBuilder {

    private static final String DEFAULT_NAME = TestDataConstants.TEAM_ONE_NAME;

    private int squadSize = 0;

    private TeamTestDataBuilder() {
    }

    public static TeamTestDataBuilder aTeam() {
        return new TeamTestDataBuilder();
    }

    public TeamTestDataBuilder withSquadSize(final int squadSize) {
        this.squadSize = squadSize;
        return this;
    }

    public Team build() {
        final Team team = new Team(DEFAULT_NAME);

        for (int i = 0; i < squadSize; i++) {
            final Bid bid = BidTestDataBuilder.aSuccessfulBid().build();
            team.processSuccessfulBid(bid);
        }

        return team;
    }
}
