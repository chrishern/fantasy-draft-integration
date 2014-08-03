/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

/**
 * Operations for performing functionality on leagues.
 * 
 * @author Chris
 *
 */
public interface LeagueFacade {

	/**
	 * Create a new league.
	 * 
	 * @param leagueName Name of the league to create.
	 */
	void createLeague(String leagueName);
}
