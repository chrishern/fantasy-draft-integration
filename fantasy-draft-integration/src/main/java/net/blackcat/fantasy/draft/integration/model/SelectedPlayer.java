/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import net.blackcat.fantasy.draft.integration.model.types.player.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.StartingTeamStatus;

/**
 * Model object representing a selected player in a team.
 * 
 * @author Chris Hern
 * 
 */
@Entity
public class SelectedPlayer implements Serializable, Comparable<SelectedPlayer> {

    private static final long serialVersionUID = -9196777599538702713L;
    
    private static final String PRICE_CHANGE_MULTIPLIER = "0.05";
	private static final String FPL_PRICE_CHANGE_FACTOR = "0.1";

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
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<GameweekScore> gameweekScores;

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
     * Return true/false depending on whether this player is still selected for this team.
     * 
     * TODO Unit test for this once we can set the selected player status to something other than 
     * 'STILL_SELECTED' and also set the starting position.
     * 
     * @return True/false depending on whether this player is still selected for this team.
     */
    public boolean isStillSelected() {
    	return this.selectedStatus == SelectedPlayerStatus.STILL_SELECTED;
    }

    /**
     * Return true/false depending on whether this player is in the starting lineup for this team.
     * 
     * TODO Unit test for this once we can set the selected player status to something other than 
     * 'STILL_SELECTED' and also set the starting position.
     * 
     * @return True/false depending on whether this player is part of the starting lineup.
     */
    public boolean isInStartingLineup() {
    	return this.isStillSelected() && this.startingTeamStatus.isStartingPosition();
    }
    
    /**
     * Return true/false depending on whether this player is a substitute for this team.
     * 
     * TODO Unit test for this once we can set the selected player status to something other than 
     * 'STILL_SELECTED' and also set the starting position.
     * 
     * @return True/false depending on whether this player is a substitute.
     */
    public boolean isSubstitute() {
    	return this.isStillSelected() && this.startingTeamStatus.isSubstitutePosition();
    }
    
    /**
     * Return true/false depending on whether this player is the captain for this team.
     * 
     * TODO Unit test for this once we can set the selected player status to something other than 
     * 'STILL_SELECTED' and also set the starting position.
     * 
     * @return True/false depending on whether this player is the captain.
     */
    public boolean isCaptain() {
    	return this.isStillSelected() && this.startingTeamStatus == StartingTeamStatus.CAPTAIN;
    }
    
    /**
     * Return true/false depending on whether this player is the vice captain for this team.
     * 
     * TODO Unit test for this once we can set the selected player status to something other than 
     * 'STILL_SELECTED' and also set the starting position.
     * 
     * @return True/false depending on whether this player is the vice captain.
     */
    public boolean isViceCaptain() {
    	return this.isStillSelected() && this.startingTeamStatus == StartingTeamStatus.VICE_CAPTAIN;
    }
    
    /**
     * Add a gameweek score to this selected player.
     * 
     * @param gameweek The gameweek number of the week the points were scored in.
     * @param score The points scored.
     */
    public void addGameweekScore(final int gameweek, final int score) {
    	
    	if (gameweekScores == null) {
    		gameweekScores = new ArrayList<GameweekScore>();
    	}
    	
    	pointsScored += score;
    	
    	final GameweekScore gameweekScore = new GameweekScore(gameweek, score);
    	gameweekScores.add(gameweekScore);
    }
    
    /**
     * Update the current sell to pot price for this selected player.
     * 
     * @param currentFplPrice The current FPL price for this player.
     */
    public void updateCurrentSellToPotPrice(final BigDecimal currentFplPrice) {
    	
    	final BigDecimal purchasePrice = this.getCost();
		final BigDecimal priceAdjustmentFactor = purchasePrice.multiply(new BigDecimal(PRICE_CHANGE_MULTIPLIER));
		
		final BigDecimal fplPriceChange = currentFplPrice.subtract(this.getFplCostAtPurchase());
		final BigDecimal amountPriceHasChanged = fplPriceChange.divide(new BigDecimal(FPL_PRICE_CHANGE_FACTOR));
		
		final BigDecimal draftPriceChange = priceAdjustmentFactor.multiply(amountPriceHasChanged);
		
		final BigDecimal currentResalePrice = purchasePrice.add(draftPriceChange);
		
		this.currentSellToPotPrice = currentResalePrice.setScale(1, RoundingMode.HALF_UP);
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

	@Override
	public int compareTo(final SelectedPlayer objectToCompare) {
		
		if (this.selectedStatus == null) {
			return this.player.getPosition().compareTo(objectToCompare.player.getPosition());
		}
		
		if (this.startingTeamStatus.isSubstitutePosition() && objectToCompare.startingTeamStatus.isStartingPosition()) {
			return 1;
		}
		
		if (this.startingTeamStatus.isStartingPosition() && objectToCompare.startingTeamStatus.isSubstitutePosition()) {
			return -1;
		}
		
		if (this.startingTeamStatus.isSubstitutePosition() && objectToCompare.startingTeamStatus.isSubstitutePosition()) {
			return this.startingTeamStatus.compareTo(objectToCompare.startingTeamStatus);
		}
		
		return this.player.getPosition().compareTo(objectToCompare.player.getPosition());
	}
}
