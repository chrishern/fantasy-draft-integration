/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.repository.LeagueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Spring Data implementation of the {@link LeagueDataService}.
 * 
 * @author Chris Hern
 * 
 */
@Repository
public class SpringDataLeagueDataService implements LeagueDataService {

    private LeagueRepository repository;

    @Autowired
    public SpringDataLeagueDataService(final LeagueRepository repository) {

        this.repository = repository;
    }

    @Override
    public void addLeague(final League league) {

        repository.save(league);
    }

    @Override
    public League getLeague(final int leagueId) throws FantasyDraftIntegrationException {

        final League league = repository.findOne(leagueId);

        if (league == null) {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.LEAGUE_NOT_FOUND);
        }

        return league;
    }

    @Override
    public void updateLeague(final League league) throws FantasyDraftIntegrationException {

        if (repository.exists(league.getId())) {
            repository.save(league);
        } else {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.LEAGUE_NOT_FOUND);
        }
    }

    @Override
    public AuctionPhase getOpenAuctionPhase(final League league) throws FantasyDraftIntegrationException {

        final Object[] queryResult = repository.getOpenAuctionPhase(league.getId());

        if (queryResult.length == 0) {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_PHASE_NOT_FOUND);
        }

        final int phaseId = (int) queryResult[0];

        return repository.getAuctionPhase(phaseId);
    }

    @Override
    public boolean doesOpenAuctionPhaseExist(final League league) {

        try {
            getOpenAuctionPhase(league);
            return true;
        } catch (final FantasyDraftIntegrationException e) {
            return false;
        }
    }
}
