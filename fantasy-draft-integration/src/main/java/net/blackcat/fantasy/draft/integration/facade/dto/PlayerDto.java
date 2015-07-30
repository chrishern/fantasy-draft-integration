/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;

/**
 * DTO to transfer player information.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerDto implements Serializable {

    private static final long serialVersionUID = 724870752909138375L;

    private int id;
    private String forename;
    private String surname;
    private String team;
    private Position position;
    private PlayerSelectionStatus selectionStatus;
    private int totalPoints;
    private BigDecimal currentPrice;
    private int goals;
    private int assists;
    private int cleanSheets;
    private BigDecimal pointsPerGame;

    public PlayerDto(final int id) {

        this.id = id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param forename
     *            the forename to set
     */
    public void setForename(String forename) {
        this.forename = forename;
    }

    /**
     * @param surname
     *            the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @param team
     *            the team to set
     */
    public void setTeam(String team) {
        this.team = team;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @param selectionStatus
     *            the selectionStatus to set
     */
    public void setSelectionStatus(PlayerSelectionStatus selectionStatus) {
        this.selectionStatus = selectionStatus;
    }

    /**
     * @param totalPoints
     *            the totalPoints to set
     */
    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    /**
     * @param currentPrice
     *            the currentPrice to set
     */
    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * @param goals
     *            the goals to set
     */
    public void setGoals(int goals) {
        this.goals = goals;
    }

    /**
     * @param assists
     *            the assists to set
     */
    public void setAssists(int assists) {
        this.assists = assists;
    }

    /**
     * @param cleanSheets
     *            the cleanSheets to set
     */
    public void setCleanSheets(int cleanSheets) {
        this.cleanSheets = cleanSheets;
    }

    /**
     * @param pointsPerGame
     *            the pointsPerGame to set
     */
    public void setPointsPerGame(BigDecimal pointsPerGame) {
        this.pointsPerGame = pointsPerGame;
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
