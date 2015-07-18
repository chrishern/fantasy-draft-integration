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

    private static final BigDecimal AMOUNT = new BigDecimal("5.0");

    public static BidTestDataBuilder aBid() {
        return new BidTestDataBuilder();
    }

    public Bid build() {

        final Team team = new Team(TestDataConstants.TEAM_ONE_NAME);
        final Player player = PlayerTestDataBuilder.aPlayer().build();

        return new Bid(team, player, AMOUNT);
    }
}
