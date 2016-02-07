package net.blackcat.fantasy.draft.integration.data.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.Team_;
import net.blackcat.fantasy.draft.integration.model.User_;

import org.springframework.data.jpa.domain.Specification;

/**
 * Class used to create Specification objects used to create JPA criteria queries for {@link Team} objects.
 * 
 * @author Chris Hern
 * 
 */
public final class TeamSpecification {

    private TeamSpecification() {
    }

    /**
     * Specification to obtain {@link Team} objects for a specific manager email address.
     * 
     * @param managerEmailAddress
     *            Manager email address to find out the team for.
     * @return Specification which will get the team for the given manager email address.
     */
    public static Specification<Team> managerIsEqualTo(final String managerEmailAddress) {

        return new Specification<Team>() {

            @Override
            public Predicate toPredicate(final Root<Team> teamRoot, final CriteriaQuery<?> query, final CriteriaBuilder cb) {

                return cb.equal(teamRoot.get(Team_.manager).get(User_.emailAddress), managerEmailAddress);
            }

        };
    }
}
