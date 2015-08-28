/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;

/**
 * DTO for transferring team points.
 * 
 * @author Chris
 *
 */
public class TeamPointsDto implements Serializable, Comparable<TeamPointsDto> {

	private static final long serialVersionUID = -2834997113081208665L;

	private String teamName;
	private int weekPoints;
	private int totalPoints;
	
	public TeamPointsDto(final String teamName, final int weekPoints, final int totalPoints) {
		this.teamName = teamName;
		this.weekPoints = weekPoints;
		this.totalPoints = totalPoints;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * @return the weekPoints
	 */
	public int getWeekPoints() {
		return weekPoints;
	}

	/**
	 * @return the points
	 */
	public int getTotalPoints() {
		return totalPoints;
	}

	@Override
	public int compareTo(final TeamPointsDto objectToCompare) {
		return Integer.valueOf(objectToCompare.totalPoints).compareTo(totalPoints);
	}

}
