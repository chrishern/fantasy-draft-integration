/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.specification;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.blackcat.fantasy.draft.integration.data.specification.PlayerSpecification;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.Player_;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

/**
 * Unit tests for {@link PlayerSpecification}.
 * 
 * @author Chris Hern
 * 
 */
public class PlayerSpecificationTest {

    private static final Position PLAYER_POSITION = Position.MIDFIELDER;

    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<?> criteriaQuery;
    private Root<Player> playerRoot;
    private Predicate positionPredicate;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Before
    public void setup() {

        criteriaBuilder = mock(CriteriaBuilder.class);
        criteriaQuery = mock(CriteriaQuery.class);
        playerRoot = mock(Root.class);

        final Path positionPath = mock(Path.class);
        positionPredicate = mock(Predicate.class);

        when(playerRoot.get(Player_.position)).thenReturn(positionPath);
        when(criteriaBuilder.equal(positionPath, PLAYER_POSITION)).thenReturn(positionPredicate);
    }

    @Test
    public void testPositionIsEqualTo() {
        // arrange

        // act
        final Specification<Player> actualSpecification = PlayerSpecification.positionIsEqualTo(PLAYER_POSITION);
        final Predicate actualPredicate = actualSpecification.toPredicate(playerRoot, criteriaQuery, criteriaBuilder);

        // assert
        assertThat(actualPredicate).isEqualTo(positionPredicate);
    }

}
