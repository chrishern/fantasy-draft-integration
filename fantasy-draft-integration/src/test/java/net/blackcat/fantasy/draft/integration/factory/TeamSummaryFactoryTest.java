/**
 * 
 */
package net.blackcat.fantasy.draft.integration.factory;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;

import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.player.types.Position;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStartingElevenStatus;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;
import net.blackcat.fantasy.draft.team.TeamSummary;

import org.junit.Test;

/**
 * Unit tests for {@link TeamSummaryFactory}.
 * 
 * @author Chris
 *
 */
public class TeamSummaryFactoryTest {

	@Test
	public void testCreateTeamSummaryFromTeamEntity() {
		// arrange
		final SelectedPlayerEntity secondSelectedPlayer = 
				TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_2_ID, Position.GOALKEEPER, SelectedPlayerStartingElevenStatus.PICKED);
		
		final SelectedPlayerEntity soldToPotPlayer = 
				TestDataUtil.buildSelectedPlayer(TestDataUtil.PLAYER_3_ID, Position.DEFENDER, null);
		soldToPotPlayer.setStillSelected(SelectedPlayerStatus.SOLD_TO_POT);
		
		final TeamEntity teamEntity = TestDataUtil.createTeamEntitiesWithScore().get(0);
		teamEntity.addSelectedPlayers(Arrays.asList(secondSelectedPlayer, soldToPotPlayer));
		
		// act
		final TeamSummary teamSummary = TeamSummaryFactory.createTeamSummary(teamEntity);
		
		// assert
		assertThat(teamSummary.getId()).isEqualTo(0);
		assertThat(teamSummary.getTeamName()).isEqualTo(TestDataUtil.TEST_TEAM_1);
		assertThat(teamSummary.getTotalPoints()).isEqualTo(TestDataUtil.TEST_TEAM_1_SCORE);
		assertThat(teamSummary.getRemainingBudget().doubleValue()).isEqualTo(Double.valueOf(TestDataUtil.TEST_TEAM_1_REMAINING_BUDGET));
		
		assertThat(teamSummary.getTeam()).hasSize(2);
		assertThat(teamSummary.getTeam().get(0).getPosition()).isEqualTo(Position.GOALKEEPER);
		assertThat(teamSummary.getTeam().get(1).getCost().doubleValue()).isEqualTo(Double.valueOf(TestDataUtil.SELECTED_PLAYER_COST));
		assertThat(teamSummary.getTeam().get(1).getForename()).isEqualTo(TestDataUtil.PLAYER_1_FORENAME);
		assertThat(teamSummary.getTeam().get(1).getId()).isEqualTo(TestDataUtil.PLAYER_1_ID);
		assertThat(teamSummary.getTeam().get(1).getPointsScored()).isEqualTo(TestDataUtil.PLAYER_1_POINTS);
		assertThat(teamSummary.getTeam().get(1).getPosition()).isEqualTo(Position.DEFENDER);
		assertThat(teamSummary.getTeam().get(1).getSelectionStatus()).isEqualTo(SelectedPlayerStartingElevenStatus.PICKED);
		assertThat(teamSummary.getTeam().get(1).getSurname()).isEqualTo(TestDataUtil.PLAYER_1_SURNAME);
		assertThat(teamSummary.getTeam().get(1).getCurrentSellToPotPrice().doubleValue()).isEqualTo(Double.valueOf(TestDataUtil.SELECTED_PLAYER_COST));
		assertThat(teamSummary.getTeam().get(1).getSquadStatus()).isEqualTo(SelectedPlayerStatus.STILL_SELECTED);
		
		assertThat(teamSummary.getSoldPlayers()).hasSize(1);
		assertThat(teamSummary.getSoldPlayers().get(0).getId()).isEqualTo(TestDataUtil.PLAYER_3_ID);
	}

}
