/**
 * 
 */
package net.blackcat.fantasy.draft.integration.repository;

import net.blackcat.fantasy.draft.integration.model.Gameweek;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository for storing {@link Gameweek} entities.
 * 
 * @author Chris
 *
 */
public interface GameweekRepository extends CrudRepository<Gameweek, Integer> {

}
