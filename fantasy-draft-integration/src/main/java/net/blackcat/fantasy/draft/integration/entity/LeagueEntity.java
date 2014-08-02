/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity representing a league within the draft.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "League")
public class LeagueEntity implements Serializable {

	private static final long serialVersionUID = -128682952066413160L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column
	private String name;
	
	@OneToMany
	private List<TeamEntity> teams;
	
	@OneToMany
	private List<DraftRoundEntity> draftRounds;
	
	public LeagueEntity() {

	}
	
	public LeagueEntity(final String name) {
		this.name = name;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof LeagueEntity)) {
			return false;
		}
		LeagueEntity other = (LeagueEntity) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
	
}
