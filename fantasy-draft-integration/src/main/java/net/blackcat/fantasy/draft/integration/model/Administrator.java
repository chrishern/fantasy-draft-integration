/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import net.blackcat.fantasy.draft.integration.model.types.user.UserRole;

/**
 * Domain class representing an Administrator within the Fantasy Draft game.
 * 
 * @author Chris
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Administrator extends User {

	private static final long serialVersionUID = -983853129969196773L;

	private Administrator(final String emailAddress, final String forename,
			final String surname, final UserRole role) {
		super(emailAddress, forename, surname, role);
	}


	/**
	 * Build an instance of a {@link Administrator} user.
	 * 
	 * @param emailAddress Email address of the user.
	 * @param forename Forename of the user.
	 * @param surname Surname of the user.
	 * @return Instance of a {@link Administrator}.
	 */
	public static Administrator buildAdministrator(final String emailAddress, final String forename, final String surname) {
		
		return new Administrator(emailAddress, forename, surname, UserRole.ADMIN);
	}
}
