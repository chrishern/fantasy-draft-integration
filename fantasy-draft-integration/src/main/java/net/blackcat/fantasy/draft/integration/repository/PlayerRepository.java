package net.blackcat.fantasy.draft.integration.repository;

import net.blackcat.fantasy.draft.integration.model.Player;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Integer>, JpaSpecificationExecutor<Player> {

}
