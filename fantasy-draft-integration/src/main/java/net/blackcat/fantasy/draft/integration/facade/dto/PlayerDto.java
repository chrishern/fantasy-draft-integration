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

    public PlayerDto(final int id, final String forename, final String surname, final String team, final Position position,
            final BigDecimal currentPrice) {

        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.team = team;
        this.position = position;
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
}
