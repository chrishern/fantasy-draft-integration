/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter.team;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.facade.dto.SelectedPlayerDto;
import net.blackcat.fantasy.draft.integration.facade.dto.SquadDto;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.testdata.TeamTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for {@link TeamToSquadDtoConverter}.
 * 
 * @author Chris
 *
 */
public class TeamToSquadDtoConverterTest {

	private TeamToSquadDtoConverter converter = new TeamToSquadDtoConverter();
	
	@Test
	public void test() {
		// arrange
		final Team team = TeamTestDataBuilder.aTeam().withSquadSize(3).build();
		
		// act
		final SquadDto squadDto = converter.convert(team);
		
		// assert
		assertThat(squadDto.getTeamId()).isEqualTo(team.getId());
		assertThat(squadDto.getTeamName()).isEqualTo(team.getName());
		assertThat(squadDto.getCurrentPlayers()).hasSize(team.getSelectedPlayers().size());
		
		final SelectedPlayerDto selectedPlayerDto = squadDto.getCurrentPlayers().get(0);
		final SelectedPlayer selectedPlayer = team.getSelectedPlayers().get(0);
		
		assertThat(selectedPlayerDto.getBigDecimal()).isEqualTo(selectedPlayer.getCost());
		assertThat(selectedPlayerDto.getForename()).isEqualTo(selectedPlayer.getPlayer().getForename());
		assertThat(selectedPlayerDto.getSurname()).isEqualTo(selectedPlayer.getPlayer().getSurname());
		assertThat(selectedPlayerDto.getPointsScored()).isEqualTo(selectedPlayer.getPointsScored());
		assertThat(selectedPlayerDto.getPosition()).isEqualTo(selectedPlayer.getPlayer().getPosition());
		assertThat(selectedPlayerDto.getSelectedPlayerId()).isEqualTo(selectedPlayer.getId());
	}

}
