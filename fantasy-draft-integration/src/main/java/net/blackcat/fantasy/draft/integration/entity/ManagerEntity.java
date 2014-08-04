/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	
	@Column
	private String password;

	public ManagerEntity() {
	}

	public ManagerEntity(final String emailAddress, final String forename, final String surname, final String password) {
		this.emailAddress = emailAddress;
		this.forename = forename;
		this.surname = surname;
		this.password = password;
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
	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}
}
