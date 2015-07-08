package net.blackcat.fantasy.draft.integration.data.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Player_;
import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;

import org.springframework.data.jpa.domain.Specification;

/**
 * Class used to create Specification objects used to create JPA criteria queries for {@link Player} objects.
 * 
 * @author Chris Hern
 * 
 */
public final class PlayerSpecification {

    private PlayerSpecification() {
    }

    /**
     * Specification to obtain {@link Player} objects for a specific {@link Position}.
     * 
     * @param position
     *            {@link Position} to get the players for.
     * @return Specification which will get the players for the given position.
     */
    public static Specification<Player> positionIsEqualTo(final Position position) {

        return new Specification<Player>() {

            @Override
            public Predicate toPredicate(final Root<Player> playerRoot, final CriteriaQuery<?> query, final CriteriaBuilder cb) {

                return cb.equal(playerRoot.get(Player_.position), position);
            }

        };
    }

    /**
     * Specification to obtain {@link Player} objects for a specific {@link Position} and {@link PlayerSelectionStatus}.
     * 
     * @param position
     *            {@link Position} to get the players for.
     * @param selectionStatus
     *            {@link PlayerSelectionStatus} to get the players for.
     * @return Specification which will get the players for the given position and selection status.
     */
    public static Specification<Player> positionAndSelectionStatusAreEqualTo(final Position position, final PlayerSelectionStatus selectionStatus) {

        return new Specification<Player>() {

            @Override
            public Predicate toPredicate(final Root<Player> playerRoot, final CriteriaQuery<?> query, final CriteriaBuilder cb) {

                // @formatter:off
                
                return cb.and(
                        cb.equal(playerRoot.get(Player_.position), position),
                        cb.equal(playerRoot.get(Player_.selectionStatus), selectionStatus)
                        );
                
                // @formatter:on
            }
        };
    }

}
