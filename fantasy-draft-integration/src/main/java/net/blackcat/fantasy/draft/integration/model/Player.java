/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;

/**
 * Domain entity class representing a fantasy draft player.
 * 
 * @author Chris
 * 
 */
@Entity
public class Player implements Serializable {

    private static final long serialVersionUID = 6584040856373261900L;

    @Id
    private int id;

    @Column
    private String forename;

    @Column
    private String surname;

    @Column
    private String team;

    @Column
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column
    @Enumerated(EnumType.STRING)
    private PlayerSelectionStatus selectionStatus;

    @Column
    private int totalPoints;

    @Column
    private BigDecimal currentPrice;

    @Column
    private int goals;

    @Column
    private int assists;

    @Column
    private int cleanSheets;

    @Column
    private BigDecimal pointsPerGame;

    /*
     * Only for Hibernate database mapping
     */
    @SuppressWarnings("unused")
    private Player() {
    }

    public Player(final int id, final String forename, final String surname, final String team, final Position position, final BigDecimal currentPrice) {

        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.team = team;
        this.position = position;
        this.selectionStatus = PlayerSelectionStatus.NOT_SELECTED;
        this.totalPoints = 0;
        this.currentPrice = currentPrice;
    }

    /**
     * Set the statistics for this player.
     * 
     * @param statistics
     *            Statistics for this player.
     */
    public void withStatistics(final PlayerStatistics statistics) {

        this.pointsPerGame = statistics.getPointsPerGame();
        this.assists = statistics.getAssists();
        this.goals = statistics.getGoals();
        this.totalPoints = statistics.getTotalPoints();
        this.cleanSheets = statistics.getCleanSheets();
    }

    /**
     * Mark this player as being selected by a team.
     */
    public void markPlayerAsSelected() {
        this.selectionStatus = PlayerSelectionStatus.SELECTED;
    }

    /**
     * Update the total points scored for this player.
     * 
     * @param newTotalPoints
     *            New number of total points scored by this player.
     */
    public void setTotalPoints(final int newTotalPoints) {
        this.totalPoints = newTotalPoints;
    }
    
    /**
     * Update the current price for this player.
     * 
     * @param currentPrice
     *            New current price of this player.
     */
    public void setCurrentPrice(final BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the forename
     */
    public String getForename() {
        return forename;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return the team
     */
    public String getTeam() {
        return team;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return the selectionStatus
     */
    public PlayerSelectionStatus getSelectionStatus() {
        return selectionStatus;
    }

    /**
     * @return the totalPoints
     */
    public int getTotalPoints() {
        return totalPoints;
    }

    /**
     * @return the currentPrice
     */
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    /**
     * @return the goals
     */
    public int getGoals() {
        return goals;
    }

    /**
     * @return the assists
     */
    public int getAssists() {
        return assists;
    }

    /**
     * @return the cleanSheets
     */
    public int getCleanSheets() {
        return cleanSheets;
    }

    /**
     * @return the pointsPerGame
     */
    public BigDecimal getPointsPerGame() {
        return pointsPerGame;
    }
}
