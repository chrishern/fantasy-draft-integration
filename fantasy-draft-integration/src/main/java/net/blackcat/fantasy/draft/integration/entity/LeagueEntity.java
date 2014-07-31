/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entity representing a league within the draft.
 * 
 * @author Chris
 *
 */
public class LeagueEntity implements Serializable {

	private static final long serialVersionUID = -128682952066413160L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	private String name;
	
	@OneToMany
	private List<TeamEntity> teams;
	
	public LeagueEntity() {

	}
	
	public LeagueEntity(final String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the teams
	 */
	public List<TeamEntity> getTeams() {
		return teams;
	}

	/**
	 * @param teams the teams to set
	 */
	public void setTeams(final List<TeamEntity> teams) {
		this.teams = teams;
	}
	
	/**
	 * Add a team to the list of teams in this league.
	 * 
	 * @param team Team to add to the list.
	 */
	public void addTeam(final TeamEntity team) {
		teams.add(team);
	}
}
