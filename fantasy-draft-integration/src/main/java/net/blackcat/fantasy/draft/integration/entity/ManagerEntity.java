/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entity representing a manager within the fantasy draft game.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "Manager")
public class ManagerEntity implements Serializable {

	private static final long serialVersionUID = 6417805260056664578L;

	@Id
	private String emailAddress;
	
	@Column
	private String forename;
	
	@Column
	private String surname;
	
	@OneToOne
	private TeamEntity team;
	
	public ManagerEntity() {
	}

	public ManagerEntity(final String emailAddress, final String forename, final String surname, final TeamEntity team) {
		this.emailAddress = emailAddress;
		this.forename = forename;
		this.surname = surname;
		this.team = team;
	}

	/**
	 * @return the forename
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * @param forename the forename to set
	 */
	public void setForename(final String forename) {
		this.forename = forename;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(final String surname) {
		this.surname = surname;
	}

	/**
	 * @return the team
	 */
	public TeamEntity getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(TeamEntity team) {
		this.team = team;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
