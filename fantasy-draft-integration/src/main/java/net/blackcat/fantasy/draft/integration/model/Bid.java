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
public class Bid implements Serializable {

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
