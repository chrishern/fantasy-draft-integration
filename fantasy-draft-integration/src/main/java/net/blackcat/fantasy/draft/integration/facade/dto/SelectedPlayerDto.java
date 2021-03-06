/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import net.blackcat.fantasy.draft.integration.model.types.player.Position;
import net.blackcat.fantasy.draft.integration.model.types.player.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.StartingTeamStatus;

/**
 * DTO used to transfer selected player information.
 * 
 * @author Chris Hern
 * 
 */
public class SelectedPlayerDto implements Serializable, Comparable<SelectedPlayerDto> {

    private static final long serialVersionUID = -6472944042325975290L;

    private int selectedPlayerId;
    private String forename;
    private String surname;
    private Position position;
    private StartingTeamStatus startingTeamStatus;
    private BigDecimal cost;
    private Integer weeklyPointsScored;
    private int pointsScored;
    private BigDecimal sellToPotPrice;
    private SelectedPlayerStatus selectedStatus;

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
	 * @return the weeklyPointsScored
	 */
	public Integer getWeeklyPointsScored() {
		return weeklyPointsScored;
	}

	/**
	 * @return the startingTeamStatus
	 */
	public StartingTeamStatus getStartingTeamStatus() {
		return startingTeamStatus;
	}

	/**
	 * @param startingTeamStatus the startingTeamStatus to set
	 */
	public void setStartingTeamStatus(StartingTeamStatus startingTeamStatus) {
		this.startingTeamStatus = startingTeamStatus;
	}

	/**
	 * @param weeklyPointsScored the weeklyPointsScored to set
	 */
	public void setWeeklyPointsScored(Integer weeklyPointsScored) {
		this.weeklyPointsScored = weeklyPointsScored;
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

	/**
	 * @return the sellToPotPrice
	 */
	public BigDecimal getSellToPotPrice() {
		return sellToPotPrice;
	}

	/**
	 * @param sellToPotPrice the sellToPotPrice to set
	 */
	public void setSellToPotPrice(BigDecimal sellToPotPrice) {
		this.sellToPotPrice = sellToPotPrice;
	}

	/**
	 * @return the selectedStatus
	 */
	public SelectedPlayerStatus getSelectedStatus() {
		return selectedStatus;
	}

	/**
	 * @param selectedStatus the selectedStatus to set
	 */
	public void setSelectedStatus(SelectedPlayerStatus selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	/**
	 * @return the cost
	 */
	public BigDecimal getCost() {
		return cost;
	}

	@Override
	public int compareTo(final SelectedPlayerDto playerToCompare) {
		
		if (this.startingTeamStatus == null || playerToCompare.startingTeamStatus == null) {
			return this.position.compareTo(playerToCompare.position);
		}
		
		if (this.startingTeamStatus.isSubstitutePosition() && playerToCompare.startingTeamStatus.isStartingPosition()) {
			return 1;
		}
		
		if (this.startingTeamStatus.isStartingPosition() && playerToCompare.startingTeamStatus.isSubstitutePosition()) {
			return -1;
		}
		
		if (this.startingTeamStatus.isSubstitutePosition() && playerToCompare.startingTeamStatus.isSubstitutePosition()) {
			return this.startingTeamStatus.compareTo(playerToCompare.startingTeamStatus);
		}
		
		return this.position.compareTo(playerToCompare.position);
	}
}
