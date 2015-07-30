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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import net.blackcat.fantasy.draft.integration.model.types.player.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.StartingTeamStatus;

/**
 * Model object representing a selected player in a team.
 * 
 * @author Chris Hern
 * 
 */
@Entity
public class SelectedPlayer implements Serializable {

    private static final long serialVersionUID = -9196777599538702713L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Player player;

    @Column
    private int pointsScored;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SelectedPlayerStatus selectedStatus;

    @Column
    private BigDecimal cost;

    @Column
    @Enumerated(EnumType.STRING)
    private StartingTeamStatus startingTeamStatus;

    @Column
    private BigDecimal fplCostAtPurchase;

    @Column
    private BigDecimal currentSellToPotPrice;

    /*
     * Used only for Hibernate mapping
     */
    @SuppressWarnings("unused")
    private SelectedPlayer() {

    }

    public SelectedPlayer(final Player player, final BigDecimal cost, final BigDecimal fplCostAtPurchase) {

        this.player = player;
        this.cost = cost;
        this.fplCostAtPurchase = fplCostAtPurchase;
        this.selectedStatus = SelectedPlayerStatus.STILL_SELECTED;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the pointsScored
     */
    public int getPointsScored() {
        return pointsScored;
    }

    /**
     * @return the selectedStatus
     */
    public SelectedPlayerStatus getSelectedStatus() {
        return selectedStatus;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @return the startingTeamStatus
     */
    public StartingTeamStatus getStartingTeamStatus() {
        return startingTeamStatus;
    }

    /**
     * @return the fplCostAtPurchase
     */
    public BigDecimal getFplCostAtPurchase() {
        return fplCostAtPurchase;
    }

    /**
     * @return the currentSellToPotPrice
     */
    public BigDecimal getCurrentSellToPotPrice() {
        return currentSellToPotPrice;
    }

}
