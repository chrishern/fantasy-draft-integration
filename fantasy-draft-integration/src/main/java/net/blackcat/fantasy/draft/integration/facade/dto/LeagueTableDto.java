/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO for transferring league information.
 * 
 * @author Chris
 *
 */
public class LeagueTableDto {

	private String name;
	private List<TeamPointsDto> teams;
	
	public LeagueTableDto(final String name) {
		this.name = name;
		this.teams = new ArrayList<TeamPointsDto>();
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
	public List<TeamPointsDto> getTeams() {
		return teams;
	}

	/**
	 * Add a team to this league.
	 * 
	 * @param team Team to add.
	 */
	public void addTeam(final TeamPointsDto team) {
		teams.add(team);
		Collections.sort(teams);
	}
}
