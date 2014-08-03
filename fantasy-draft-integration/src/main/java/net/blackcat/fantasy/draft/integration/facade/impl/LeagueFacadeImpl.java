/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.facade.LeagueFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of league based operations.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "leagueFacade")
public class LeagueFacadeImpl implements LeagueFacade {

	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	@Override
	public void createLeague(final String leagueName) {
		leagueDataService.createLeague(leagueName);
	}

}
