/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import net.blackcat.fantasy.draft.integration.model.types.user.UserRole;

/**
 * Abstract class represents a user within the Fantasy Draft game.
 * 
 * @author Chris
 *
 */
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = -5501812656863255674L;

	@Id
	private String emailAddress;

	@Column
	private String forename;

	@Column
	private String surname;

	@Column
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	/*
	 * Used only for Hibernate database mapping.
	 */
	private User() {
		
	}
	
	private User(final String emailAddress, final String forename,
			final String surname, final UserRole role) {
		
		this.emailAddress = emailAddress;
		this.forename = forename;
		this.surname = surname;
		this.role = role;
	}
	
	/**
	 * Build an instance of a {@link User} representing someone who only manages a team.
	 * 
	 * @param emailAddress Email address of the user.
	 * @param forename Forename of the user.
	 * @param surname Surname of the user.
	 * @return Instance of a {@link User} representing the manager.
	 */
	public static User buildManager(final String emailAddress, final String forename, final String surname) {
		
		return new User(emailAddress, forename, surname, UserRole.MANAGER);
	}
	
	/**
	 * Build an instance of a {@link User} representing someone who administers a league.
	 * 
	 * @param emailAddress Email address of the user.
	 * @param forename Forename of the user.
	 * @param surname Surname of the user.
	 * @return Instance of a {@link User} representing the league administrator.
	 */
	public static User buildLeagueAdministrator(final String emailAddress, final String forename, final String surname) {
		
		return new User(emailAddress, forename, surname, UserRole.LEAGUE_ADMIN);
	}
	
	/**
	 * Build an instance of a {@link User} representing someone who administers the entire game.
	 * 
	 * @param emailAddress Email address of the user.
	 * @param forename Forename of the user.
	 * @param surname Surname of the user.
	 * @return Instance of a {@link User} representing the administrator.
	 */
	public static User buildAdministrator(final String emailAddress, final String forename, final String surname) {
		
		return new User(emailAddress, forename, surname, UserRole.ADMIN);
	}
	
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
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
	 * @return the role
	 */
	public UserRole getRole() {
		return role;
	}
}
