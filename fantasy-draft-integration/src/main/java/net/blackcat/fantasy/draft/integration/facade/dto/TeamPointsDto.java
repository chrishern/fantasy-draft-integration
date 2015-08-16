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
	private int points;
	
	public TeamPointsDto(final String teamName, final int points) {
		this.teamName = teamName;
		this.points = points;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	@Override
	public int compareTo(final TeamPointsDto objectToCompare) {
		return Integer.valueOf(objectToCompare.points).compareTo(points);
	}

}
