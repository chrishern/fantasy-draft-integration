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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import net.blackcat.fantasy.draft.integration.model.types.team.TeamStatus;

/**
 * Model object representing a team in a league within the fantasy draft game.
 * 
 * @author Chris
 * 
 */
@Entity
public class Team implements Serializable {

    private static final long serialVersionUID = -3734641391628154428L;

    private static final BigDecimal STARTING_BUDGET = new BigDecimal(100);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String name;

    @Column
    private int totalScore;

    @Column
    @Enumerated(EnumType.STRING)
    private TeamStatus status;

    @Column
    private BigDecimal remainingBudget;

    @ManyToOne
    @JoinColumn(name = "manager", referencedColumnName = "emailAddress")
    private User manager;

    /*
     * Only used for Hibernate database mapping.
     */
    @SuppressWarnings("unused")
    private Team() {

    }

    public Team(final String name) {

        this.name = name;
        this.totalScore = 0;
        this.status = TeamStatus.INCOMPLETE;
        this.remainingBudget = STARTING_BUDGET;
    }

    /**
     * Set the {@link User} representing the manager of this Team.
     * 
     * @param manager
     *            {@link User} representing the manager of this team.
     */
    public void setManager(final User manager) {

        this.manager = manager;
    }
}
