/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;

import javax.persistence.Column;
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
public abstract class User implements Serializable {

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
	
	protected User(final String emailAddress, final String forename,
			final String surname, final UserRole role) {
		
		this.emailAddress = emailAddress;
		this.forename = forename;
		this.surname = surname;
		this.role = role;
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
