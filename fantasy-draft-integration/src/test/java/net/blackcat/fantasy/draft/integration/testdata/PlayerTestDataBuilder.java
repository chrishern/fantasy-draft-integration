package net.blackcat.fantasy.draft.integration.testdata;

import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;

/**
 * Builder for creating instances of {@link Player} objects be used in unit tests.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerTestDataBuilder {

    private static final int DEFAULT_ID = 4;
    private static final String DEFAULT_FORNAME = "Kevin";
    private static final String DEFAULT_SURNAME = "Phillips";
    private static final String DEFAULT_TEAM = "Sunderland";
    private static final Position DEFAULT_POSITION = Position.STRIKER;
    private static final BigDecimal DEFAULT_CURRENT_PRICE = new BigDecimal("9.5");

    private int id = DEFAULT_ID;

    public static PlayerTestDataBuilder aPlayer() {

        return new PlayerTestDataBuilder();
    }

    public PlayerTestDataBuilder withId(final int id) {

        this.id = id;
        return this;
    }

    public Player build() {

        return new Player(id, DEFAULT_FORNAME, DEFAULT_SURNAME, DEFAULT_TEAM, DEFAULT_POSITION, DEFAULT_CURRENT_PRICE);
    }
}
