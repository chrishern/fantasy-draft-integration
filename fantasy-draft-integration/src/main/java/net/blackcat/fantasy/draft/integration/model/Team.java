/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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

    public static final int MAXIMUM_STARTING_TEAM_SIZE = 11;
    private static final BigDecimal STARTING_BUDGET = new BigDecimal(100);
    private static final int SQUAD_SIZE = 16;

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

    @ManyToOne
    @JoinColumn(name = "league", referencedColumnName = "id")
    private League league;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SelectedPlayer> selectedPlayers;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<GameweekScore> gameweekScores;

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
        this.selectedPlayers = new ArrayList<SelectedPlayer>();
    }

    /**
     * Process a successful player bid made by this team.
     * 
     * @param bid
     *            The successful {@link Bid} made by this team.
     */
    public void processSuccessfulBid(final Bid bid) {

        final Player playerBought = bid.getPlayer();
        final SelectedPlayer selectedPlayer = new SelectedPlayer(playerBought, bid.getAmount(), playerBought.getCurrentPrice());

        this.selectedPlayers.add(selectedPlayer);
        reduceRemaningBudget(bid.getAmount());
        updateTeamStatus();
    }

    /**
     * Add to the total score this team has.
     * 
     * @param amountToAdd
     *            Amount to add to the total score.
     */
    public void addToTotalScore(final int amountToAdd) {
        this.totalScore += amountToAdd;
    }
    
    /**
     * Add a gameweek score to this team.
     * 
     * @param gameweek The gameweek number of the week the points were scored in.
     * @param score The points scored.
     */
    public void addGameweekScore(final int gameweek, final int score) {
    	
    	if (gameweekScores == null) {
    		gameweekScores = new ArrayList<GameweekScore>();
    	}
    	
    	totalScore += score;
    	
    	final GameweekScore gameweekScore = new GameweekScore(gameweek, score);
    	gameweekScores.add(gameweekScore);
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

    /**
     * Set the {@link League} this Team is in.
     * 
     * @param league
     *            {@link League} representing the league this team is in.
     */
    public void setLeague(final League league) {

        this.league = league;
    }

    /**
     * Get the starting line up for this team.
     * 
     * TODO test this once we can add players who are not part of the starting lineup.
     * 
     * @return Starting line up for this team.
     */
    public List<SelectedPlayer> getStartingTeam() {
    	
    	final List<SelectedPlayer> startingLineup = new ArrayList<SelectedPlayer>();
    	
    	for (final SelectedPlayer selectedPlayer : this.selectedPlayers) {
    		if (selectedPlayer.isInStartingLineup()) {
    			startingLineup.add(selectedPlayer);
    		}
    	}
    	
    	Collections.sort(startingLineup);
    	
    	return startingLineup;
    }
    
    /**
     * Get the substitutes for this team.
     * 
     * TODO test this once we can add players who are not part of the starting lineup.
     * 
     * @return Substitutes for this team.
     */
    public List<SelectedPlayer> getSubstitutes() {
    	
    	final List<SelectedPlayer> startingLineup = new ArrayList<SelectedPlayer>();
    	
    	for (final SelectedPlayer selectedPlayer : this.selectedPlayers) {
    		if (selectedPlayer.isSubstitute()) {
    			startingLineup.add(selectedPlayer);
    		}
    	}
    	
    	Collections.sort(startingLineup);
    	
    	return startingLineup;
    }
    
    /**
     * Get the currently selected players for this team.
     * 
     * TODO test this once we can have players who are no longer selected.
     * 
     * @return Currently selected players for this team.
     */
    public List<SelectedPlayer> getCurrentlySelectedPlayers() {
    	
    	final List<SelectedPlayer> currentlySelectedPlayers = new ArrayList<SelectedPlayer>();
    	
    	for (final SelectedPlayer selectedPlayer : this.selectedPlayers) {
    		if (selectedPlayer.isStillSelected()) {
    			currentlySelectedPlayers.add(selectedPlayer);
    		}
    	}
    	
    	return currentlySelectedPlayers;
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
     * @return the totalScore
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * @return the status
     */
    public TeamStatus getStatus() {
        return status;
    }

    /**
     * @return the remainingBudget
     */
    public BigDecimal getRemainingBudget() {
        return remainingBudget;
    }

    /**
     * @return the manager
     */
    public User getManager() {
        return manager;
    }

    /**
     * @return the league
     */
    public League getLeague() {
        return league;
    }

    /**
     * @return the selectedPlayers
     */
    public List<SelectedPlayer> getSelectedPlayers() {
        return selectedPlayers;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(final Object objectToCompare) {

        if (objectToCompare instanceof Team) {
            final Team teamToCompare = (Team) objectToCompare;

            return Objects.equals(this.name, teamToCompare.name);
        }

        return false;
    }

    /*
     * Reduce the remaining budget this team has.
     */
    private void reduceRemaningBudget(final BigDecimal amountToReduce) {
        final BigDecimal newBudget = this.remainingBudget.subtract(amountToReduce);

        this.remainingBudget = newBudget;
    }

    /*
     * Update the selection status of this team.
     */
    private void updateTeamStatus() {

        if (this.selectedPlayers.size() == SQUAD_SIZE) {
            this.status = TeamStatus.COMPLETE;
        }
    }

}
