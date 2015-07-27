package net.blackcat.fantasy.draft.integration.testdata.dto;

import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.facade.dto.PlayerDto;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;

/**
 * Class for building instances of the {@link PlayerDto} class to be used in unit tests.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerDtoTestDataBuilder {

    private static final int DEFAULT_ID = 4;
    private static final String DEFAULT_FORNAME = "Kevin";
    private static final String DEFAULT_SURNAME = "Phillips";
    private static final String DEFAULT_TEAM = "Sunderland";
    private static final Position DEFAULT_POSITION = Position.STRIKER;
    private static final BigDecimal DEFAULT_CURRENT_PRICE = new BigDecimal("9.5");

    public static PlayerDtoTestDataBuilder aPlayer() {

        return new PlayerDtoTestDataBuilder();
    }

    public PlayerDto build() {

        return new PlayerDto(DEFAULT_ID, DEFAULT_FORNAME, DEFAULT_SURNAME, DEFAULT_TEAM, DEFAULT_POSITION, DEFAULT_CURRENT_PRICE);
    }
}
