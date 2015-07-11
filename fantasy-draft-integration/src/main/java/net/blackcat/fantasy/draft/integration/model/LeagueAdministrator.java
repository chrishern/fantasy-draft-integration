/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import net.blackcat.fantasy.draft.integration.model.types.user.UserRole;

/**
 * Domain class representing a League Administrator within the Fantasy Draft game.
 * 
 * @author Chris
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class LeagueAdministrator extends User {

	private static final long serialVersionUID = -983853129969196773L;

	private LeagueAdministrator(final String emailAddress, final String forename,
			final String surname, final UserRole role) {
		super(emailAddress, forename, surname, role);
	}


	/**
	 * Build an instance of a {@link LeagueAdministrator} user.
	 * 
	 * @param emailAddress Email address of the user.
	 * @param forename Forename of the user.
	 * @param surname Surname of the user.
	 * @return Instance of a {@link LeagueAdministrator}.
	 */
	public static LeagueAdministrator buildLeagueAdministrator(final String emailAddress, final String forename, final String surname) {
		
		return new LeagueAdministrator(emailAddress, forename, surname, UserRole.LEAGUE_ADMIN);
	}
}
