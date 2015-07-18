/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Model object representing a league within the draft game.
 * 
 * @author Chris Hern
 * 
 */
@Entity
public class League implements Serializable {

    private static final long serialVersionUID = 4673970749744988343L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String name;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "league")
    private List<Team> teams;

    @OneToOne(cascade = { CascadeType.ALL })
    private Auction auction;

    /*
     * Used only for Hibernate database mapping.
     */
    @SuppressWarnings("unused")
    private League() {

    }

    public League(final String name) {

        this.name = name;
        this.teams = new ArrayList<Team>();
    }

    /**
     * Add a team to this league.
     * 
     * @param team
     */
    public void addTeam(final Team team) {

        teams.add(team);
        team.setLeague(this);
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param auction
     *            the auction to set
     */
    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the teams
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * @return the auction
     */
    public Auction getAuction() {
        return auction;
    }
}
