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
    private static final int DEFAULT_TOTAL_POINTS = 12;
    private static final int DEFAULT_GOALS = 3;
    private static final int DEFAULT_ASSISTS = 4;
    private static final int DEFAULT_CLEAN_SHEETS = 1;
    private static final BigDecimal DEFAULT_POINTS_PER_GAME = new BigDecimal(3.4);

    private boolean setStatistics = false;

    public static PlayerDtoTestDataBuilder aPlayer() {

        return new PlayerDtoTestDataBuilder();
    }

    public PlayerDtoTestDataBuilder withStatistics() {
        setStatistics = true;
        return this;
    }

    public PlayerDto build() {

        final PlayerDto dto = new PlayerDto(DEFAULT_ID);

        dto.setForename(DEFAULT_FORNAME);
        dto.setSurname(DEFAULT_SURNAME);
        dto.setTeam(DEFAULT_TEAM);
        dto.setPosition(DEFAULT_POSITION);
        dto.setCurrentPrice(DEFAULT_CURRENT_PRICE);

        if (setStatistics) {
            dto.setTotalPoints(DEFAULT_TOTAL_POINTS);
            dto.setGoals(DEFAULT_GOALS);
            dto.setAssists(DEFAULT_ASSISTS);
            dto.setCleanSheets(DEFAULT_CLEAN_SHEETS);
            dto.setPointsPerGame(DEFAULT_POINTS_PER_GAME);
        }

        return dto;
    }
}
