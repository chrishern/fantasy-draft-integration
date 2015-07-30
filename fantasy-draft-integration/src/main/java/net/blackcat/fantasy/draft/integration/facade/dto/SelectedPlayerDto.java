/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.model.types.player.Position;

/**
 * DTO used to transfer selected player information.
 * 
 * @author Chris Hern
 * 
 */
public class SelectedPlayerDto implements Serializable {

    private static final long serialVersionUID = -6472944042325975290L;

    private int selectedPlayerId;
    private String forename;
    private String surname;
    private Position position;
    private BigDecimal cost;
    private int pointsScored;

    /**
     * @return the selectedPlayerId
     */
    public int getSelectedPlayerId() {
        return selectedPlayerId;
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
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return the bigDecimal
     */
    public BigDecimal getBigDecimal() {
        return cost;
    }

    /**
     * @return the pointsScored
     */
    public int getPointsScored() {
        return pointsScored;
    }

    /**
     * @param selectedPlayerId
     *            the selectedPlayerId to set
     */
    public void setSelectedPlayerId(int selectedPlayerId) {
        this.selectedPlayerId = selectedPlayerId;
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
     * @param position
     *            the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @param cost
     *            the bigDecimal to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @param pointsScored
     *            the pointsScored to set
     */
    public void setPointsScored(int pointsScored) {
        this.pointsScored = pointsScored;
    }
}
