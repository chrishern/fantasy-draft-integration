/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.model.PlayerStatistics;

/**
 * Class for building instances of {@link PlayerStatistics} to use in unit tests.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerStatisticsTestDataBuilder {

    private static final int TOTAL_POINTS = 12;
    private static final int GOALS = 3;
    private static final int ASSISTS = 4;
    private static final int CLEAN_SHEETS = 1;
    private static final BigDecimal POINTS_PER_GAME = new BigDecimal(3.4);

    private PlayerStatisticsTestDataBuilder() {

    }

    public static PlayerStatisticsTestDataBuilder aPlayerStatistics() {
        return new PlayerStatisticsTestDataBuilder();
    }

    public PlayerStatistics build() {

        return new PlayerStatistics(TOTAL_POINTS, GOALS, ASSISTS, CLEAN_SHEETS, POINTS_PER_GAME);
    }
}
