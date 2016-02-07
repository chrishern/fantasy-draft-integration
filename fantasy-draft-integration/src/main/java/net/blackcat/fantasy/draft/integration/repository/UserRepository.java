/**
 * 
 */
package net.blackcat.fantasy.draft.integration.repository;

import net.blackcat.fantasy.draft.integration.model.User;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository used for storing {@link User} objects.
 * 
 * @author Chris
 *
 */
public interface UserRepository extends CrudRepository<User, String> {

}
