/**
 * 
 */
package net.blackcat.fantasy.draft.integration.repository;

import net.blackcat.fantasy.draft.integration.model.League;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository used for storing {@link League} objects.
 * 
 * @author Chris Hern
 * 
 */
public interface LeagueRepository extends CrudRepository<League, Integer> {

}
