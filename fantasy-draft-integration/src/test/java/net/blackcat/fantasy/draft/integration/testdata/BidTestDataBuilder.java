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

    private BigDecimal amount;

    private BidTestDataBuilder() {
        amount = DEFAULT_AMOUNT;
    }

    public static BidTestDataBuilder aBid() {
        return new BidTestDataBuilder();
    }

    public BidTestDataBuilder withAmount(final BigDecimal amount) {
        this.amount = amount;

        return this;
    }

    public Bid build() {

        final Team team = new Team(TestDataConstants.TEAM_ONE_NAME);
        final Player player = PlayerTestDataBuilder.aPlayer().build();

        return new Bid(team, player, amount);
    }
}
