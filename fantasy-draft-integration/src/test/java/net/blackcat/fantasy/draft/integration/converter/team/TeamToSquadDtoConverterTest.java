/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter.team;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.facade.dto.SelectedPlayerDto;
import net.blackcat.fantasy.draft.integration.facade.dto.SquadDto;
import net.blackcat.fantasy.draft.integration.model.Gameweek;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.testdata.TeamTestDataBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link TeamToSquadDtoConverter}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TeamToSquadDtoConverterTest {

	private static final int PLAYER_GAMEWEEK_SCORE = 14;

	@Mock
	private GameweekDataService gameweekDataService;
	
	private TeamToSquadDtoConverter converter;
	
	@Before
	public void setup() {
		converter = new TeamToSquadDtoConverter(gameweekDataService);
	}
	
	@Test
	public void testConvert() {
		// arrange
		final Team team = TeamTestDataBuilder.aTeam().withSquadSize(3).build();
		
		when(gameweekDataService.getGameweek()).thenReturn(new Gameweek());
		when(gameweekDataService.getGameweekScoreForPlayer(anyInt(), anyInt())).thenReturn(PLAYER_GAMEWEEK_SCORE);
		
		// act
		final SquadDto squadDto = converter.convert(team);
		
		// assert
		assertThat(squadDto.getTeamId()).isEqualTo(team.getId());
		assertThat(squadDto.getTeamName()).isEqualTo(team.getName());
		assertThat(squadDto.getRemainingBudget()).isEqualTo(team.getRemainingBudget());
		assertThat(squadDto.getCurrentPlayers()).hasSize(team.getSelectedPlayers().size());
		
		final SelectedPlayerDto selectedPlayerDto = squadDto.getCurrentPlayers().get(0);
		final SelectedPlayer selectedPlayer = team.getSelectedPlayers().get(0);
		
		assertThat(selectedPlayerDto.getBigDecimal()).isEqualTo(selectedPlayer.getCost());
		assertThat(selectedPlayerDto.getForename()).isEqualTo(selectedPlayer.getPlayer().getForename());
		assertThat(selectedPlayerDto.getSurname()).isEqualTo(selectedPlayer.getPlayer().getSurname());
		assertThat(selectedPlayerDto.getPointsScored()).isEqualTo(selectedPlayer.getPointsScored());
		assertThat(selectedPlayerDto.getPosition()).isEqualTo(selectedPlayer.getPlayer().getPosition());
		assertThat(selectedPlayerDto.getSelectedPlayerId()).isEqualTo(selectedPlayer.getId());
		assertThat(selectedPlayerDto.getWeeklyPointsScored()).isEqualTo(PLAYER_GAMEWEEK_SCORE);
		assertThat(selectedPlayerDto.getStartingTeamStatus()).isEqualTo(selectedPlayer.getStartingTeamStatus());
		assertThat(selectedPlayerDto.getSelectedStatus()).isEqualTo(selectedPlayer.getSelectedStatus());
		assertThat(selectedPlayerDto.getSellToPotPrice()).isEqualTo(selectedPlayer.getCurrentSellToPotPrice());
	}

}
