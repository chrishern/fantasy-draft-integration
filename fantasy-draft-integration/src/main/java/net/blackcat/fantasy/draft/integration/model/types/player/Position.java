package net.blackcat.fantasy.draft.integration.model.types.player;

/**
 * Type representing all available positions for a player within the fantasy draft.
 * 
 * @author Chris
 * 
 */
public enum Position {

    // @formatter:off

    GOALKEEPER(1, "goalkeepers"), 
    DEFENDER(2, "defenders"), 
    MIDFIELDER(3, "midfielders"),
    STRIKER(4, "strikers");

    // @formatter:on

    private int fantasyPremierLeagueValue;
    private String restApiValue;

    private Position(final int fantasyPremiumLeagueValue, final String restApiValue) {
        this.fantasyPremierLeagueValue = fantasyPremiumLeagueValue;
        this.restApiValue = restApiValue;
    }

    /**
     * @return the fantasyPremierLeagueValue
     */
    public int getFantasyPremierLeagueValue() {
        return fantasyPremierLeagueValue;
    }

    /**
     * @return the restApiValue
     */
    public String getRestApiValue() {
        return restApiValue;
    }

    /**
     * Convert the string FPL position value to the enum value.
     * 
     * @param fantasyPremierLeaguePosition
     *            int value of the position from the FPL.
     * @return Equivalent Position enum.
     */
    public static Position fromFantasyPremierLeaguePosition(final int fantasyPremierLeaguePosition) {
        for (final Position position : Position.values()) {
            if (position.getFantasyPremierLeagueValue() == fantasyPremierLeaguePosition) {
                return position;
            }
        }

        return null;
    }

    /**
     * Convert the string REST API position value to the enum value.
     * 
     * @param restApiValue
     *            String value of the position from the REST API parameter.
     * @return Equivalent Position enum.
     */
    public static Position fromRestApiValue(final String restApiValue) {
        for (final Position position : Position.values()) {
            if (position.getRestApiValue().equals(restApiValue)) {
                return position;
            }
        }

        return null;
    }
}
