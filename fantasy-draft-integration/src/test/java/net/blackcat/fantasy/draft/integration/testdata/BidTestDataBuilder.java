/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Team;

/**
 * Builder for building instances of the {@link Bid} class to use in unit tests.
 * 
 * @author Chris Hern
 * 
 */
public class BidTestDataBuilder {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal("5.0");
    private static final String DEFAULT_TEAM_NAME = TestDataConstants.TEAM_ONE_NAME;

    private BigDecimal amount;
    private String teamName;
    private boolean isSuccessful;

    private BidTestDataBuilder(final boolean isSuccessful) {
        this.amount = DEFAULT_AMOUNT;
        this.teamName = DEFAULT_TEAM_NAME;
        this.isSuccessful = isSuccessful;
    }

    public static BidTestDataBuilder aBid() {
        return new BidTestDataBuilder(false);
    }

    public static BidTestDataBuilder aSuccessfulBid() {
        return new BidTestDataBuilder(true);
    }

    public BidTestDataBuilder withAmount(final BigDecimal amount) {
        this.amount = amount;

        return this;
    }

    public BidTestDataBuilder withTeam(final String teamName) {
        this.teamName = teamName;

        return this;
    }

    public Bid build() {

        final Team team = new Team(teamName);
        final Player player = PlayerTestDataBuilder.aPlayer().build();

        final Bid bid = new Bid(team, player, amount);

        if (isSuccessful) {
            bid.markBidAsSuccessful();
        }

        return bid;
    }
}
