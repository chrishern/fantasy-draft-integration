/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import net.blackcat.fantasy.draft.integration.model.types.user.UserRole;

/**
 * Domain class representing a Manager within the Fantasy Draft game.
 * 
 * @author Chris
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Manager extends User {

	private static final long serialVersionUID = 2477060174670217179L;

	private Manager(final String emailAddress, final String forename, final String surname,
			final UserRole role) {
		
		super(emailAddress, forename, surname, role);
	}

	/**
	 * Build an instance of a {@link Manager} user.
	 * 
	 * @param emailAddress Email address of the user.
	 * @param forename Forename of the user.
	 * @param surname Surname of the user.
	 * @return Instance of a {@link Manager}.
	 */
	public static Manager buildManager(final String emailAddress, final String forename, final String surname) {
		
		return new Manager(emailAddress, forename, surname, UserRole.MANAGER);
	}
}
