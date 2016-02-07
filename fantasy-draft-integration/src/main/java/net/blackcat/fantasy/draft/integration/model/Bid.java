/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Model object representing a bid made during an auction in the draft.
 * 
 * @author Chris Hern
 * 
 */
@Entity
public class Bid implements Serializable, Comparable<Bid> {

    private static final long serialVersionUID = -3332994016917899749L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Team team;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Player player;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column
    private boolean successful;

    /*
     * Used by Hibernate database mapping.
     */
    @SuppressWarnings("unused")
    private Bid() {
    }

    public Bid(final Team team, final Player player, final BigDecimal amount) {

        this.team = team;
        this.player = player;
        this.amount = amount;
    }

    /**
     * Determine if this bid has a matching bid amount to another bid passed in as a parameter.
     * 
     * @param bidToCompare
     *            Bid to compare the amount with.
     * @return True if the two bids are for the same amount, false otherwise.
     */
    public boolean hasMatchingAmount(final Bid bidToCompare) {

        return amount.doubleValue() == bidToCompare.getAmount().doubleValue();
    }

    /**
     * Mark this bid as being successful.
     */
    public void markBidAsSuccessful() {

        successful = true;
    }

    @Override
    public int compareTo(final Bid bidToCompare) {

        return bidToCompare.getAmount().compareTo(amount);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @return the successful
     */
    public boolean isSuccessful() {
        return successful;
    }
}
