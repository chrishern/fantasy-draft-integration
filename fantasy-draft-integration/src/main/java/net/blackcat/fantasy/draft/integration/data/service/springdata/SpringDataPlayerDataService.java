package net.blackcat.fantasy.draft.integration.data.service.springdata;

import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.data.specification.PlayerSpecification;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;
import net.blackcat.fantasy.draft.integration.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

/**
 * Spring data implementation of the {@link PlayerDataService}.
 * 
 * @author Chris Hern
 * 
 */
@Repository(value = "springDataPlayerDataService")
public class SpringDataPlayerDataService implements PlayerDataService {

    private PlayerRepository repository;

    @Autowired
    public SpringDataPlayerDataService(final PlayerRepository repository) {

        this.repository = repository;
    }

    @Override
    public void addPlayers(final List<Player> players) {

        repository.save(players);
    }

    @Override
    public List<Player> getPlayers(final Position position) {

        final Specification<Player> positionSpecification = PlayerSpecification.positionIsEqualTo(position);

        return repository.findAll(positionSpecification);
    }

    @Override
    public List<Player> getPlayers(final Position position, final PlayerSelectionStatus selectionStatus) {

        final Specification<Player> positionAndSelectionStatusSpecification =
                PlayerSpecification.positionAndSelectionStatusAreEqualTo(position, selectionStatus);

        return repository.findAll(positionAndSelectionStatusSpecification);
    }

    @Override
    public Player getPlayer(final int id) throws FantasyDraftIntegrationException {

        final Player foundPlayer = repository.findOne(id);

        if (foundPlayer == null) {
            throw new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.PLAYER_NOT_FOUND);
        }

        return foundPlayer;
    }

    @Override
    public void updatePlayer(final Player updatedPlayer) {

        repository.save(updatedPlayer);
    }

}
