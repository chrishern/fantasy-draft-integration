/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Gameweek;
import net.blackcat.fantasy.draft.integration.repository.GameweekRepository;
import net.blackcat.fantasy.draft.integration.repository.GameweekScoreRepository;

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

	private GameweekRepository gameweekRepository;
	private GameweekScoreRepository gameweekScoreRepository;
	
	@Autowired
	public SpringDataGameweekDataService(final GameweekRepository gameweekRepository, final GameweekScoreRepository gameweekScoreRepository) {
		
		this.gameweekRepository = gameweekRepository;
		this.gameweekScoreRepository = gameweekScoreRepository;
	}
	
	@Override
	public void updateGameweek(final Gameweek gameweek) {

		gameweekRepository.save(gameweek);
	}

	@Override
	public Gameweek getGameweek() {
		
		// There will only be ever one Gameweek record.
		return gameweekRepository.findAll().iterator().next();
	}

	@Override
	public int getGameweekScoreForTeam(int teamId, int gameweek) throws FantasyDraftIntegrationException {

		final List<Object> gameweekScoreForTeam = gameweekScoreRepository.getGameweekScoreForTeam(teamId, gameweek);
		
		if (gameweekScoreForTeam.isEmpty()) {
			throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.GAMEWEEK_SCORE_NOT_FOUND);
		}
		
		return (Integer) gameweekScoreForTeam.get(0);
	}

}
