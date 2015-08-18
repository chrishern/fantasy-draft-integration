/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.model.Gameweek;
import net.blackcat.fantasy.draft.integration.repository.GameweekRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Spring data implementation of the {@link GameweekDataService}
 * 
 * @author Chris
 *
 */
@Repository
public class SpringDataGameweekDataService implements GameweekDataService {

	private GameweekRepository repository;
	
	@Autowired
	public SpringDataGameweekDataService(final GameweekRepository repository) {
		
		this.repository = repository;
	}
	
	@Override
	public void updateGameweek(final Gameweek gameweek) {

		repository.save(gameweek);
	}

	@Override
	public Gameweek getGameweek() {
		
		// There will only be ever one Gameweek record.
		return repository.findAll().iterator().next();
	}

}
