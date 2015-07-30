package net.blackcat.fantasy.draft.integration.model;

import java.math.BigDecimal;

/**
 * Model class representing player statistics.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerStatistics {

    private int totalPoints;
    private int goals;
    private int assists;
    private int cleanSheets;
    private BigDecimal pointsPerGame;

    public PlayerStatistics(final int totalPoints, final int goals, final int assists, final int cleanSheets, final BigDecimal pointsPerGame) {

        this.totalPoints = totalPoints;
        this.goals = goals;
        this.assists = assists;
        this.cleanSheets = cleanSheets;
        this.pointsPerGame = pointsPerGame;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getGoals() {
        return goals;
    }

    public int getAssists() {
        return assists;
    }

    public int getCleanSheets() {
        return cleanSheets;
    }

    public BigDecimal getPointsPerGame() {
        return pointsPerGame;
    }
}