package net.blackcat.fantasy.draft.integration.testdata;

import net.blackcat.fantasy.draft.integration.model.Auction;
import net.blackcat.fantasy.draft.integration.model.League;

/**
 * Class used for building instances of {@link League} objects to be used in unit tests.
 * 
 * @author Chris Hern
 * 
 */
public class LeagueTestDataBuilder {

    private static final String DEFAULT_NAME = TestDataConstants.LEAGUE_ONE_NAME;

    private Auction auction;
    private String name = DEFAULT_NAME;

    public static LeagueTestDataBuilder aLeague() {

        return new LeagueTestDataBuilder();
    }

    public LeagueTestDataBuilder withName(final String name) {

        this.name = name;
        return this;
    }

    public LeagueTestDataBuilder withAuction() {

        auction = new Auction();
        return this;
    }

    public LeagueTestDataBuilder withAuction(final Auction auction) {

        this.auction = auction;
        return this;
    }

    public League build() {

        final League league = new League(name);

        if (auction != null) {
            league.setAuction(auction);
        }

        return league;
    }
}
