/**
 * 
 */
package net.blackcat.fantasy.draft.integration.repository;

import java.util.List;

import net.blackcat.fantasy.draft.integration.model.GameweekScore;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GameweekScoreRepository extends CrudRepository<GameweekScore, Integer>, JpaSpecificationExecutor<GameweekScore> {

	@Query(nativeQuery = true, value = "SELECT gs.score FROM GameweekScore gs, Team t, Team_GameweekScore tgs "
			+ "WHERE gs.gameweek=:gameweek AND t.id=:teamId AND tgs.Team_id = t.id AND tgs.gameweekScores_id = gs.id")
	public List<Object> getGameweekScoreForTeam(@Param("teamId") int teamId, @Param("gameweek") int gameweek);
	
	@Query(nativeQuery = true, value = "SELECT gs.score FROM GameweekScore gs, SelectedPlayer sp, SelectedPlayer_GameweekScore spgs "
			+ "WHERE gs.gameweek=:gameweek AND sp.id=:selectedPlayerId AND spgs.SelectedPlayer_id = sp.id AND spgs.gameweekScores_id = gs.id")
	public List<Object> getGameweekScoreForPlayer(@Param("selectedPlayerId") int selectedPlayerId, @Param("gameweek") int gameweek);
}
