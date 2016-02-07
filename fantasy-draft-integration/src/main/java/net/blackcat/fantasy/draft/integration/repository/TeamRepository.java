/**
 * 
 */
package net.blackcat.fantasy.draft.integration.repository;

import net.blackcat.fantasy.draft.integration.model.Team;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository used to store {@link Team} objects.
 * 
 * @author Chris Hern
 * 
 */
public interface TeamRepository extends CrudRepository<Team, Integer>, JpaSpecificationExecutor<Team> {

}
