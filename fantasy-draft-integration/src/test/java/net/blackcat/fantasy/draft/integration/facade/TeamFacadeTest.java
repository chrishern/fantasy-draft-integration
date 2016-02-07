/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.converter.ConverterService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.facade.dto.PlayerDto;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.testdata.TeamTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.dto.PlayerDtoTestDataBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link TeamFacade}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TeamFacadeTest {

	@Mock
	private TeamDataService teamDataService;
	
	@Mock
	private ConverterService converterService;
	
	private TeamFacade teamFacade;
	
	@Before
	public void setup() {
		this.teamFacade = new TeamFacade(teamDataService, converterService);
	}
	
	@Test
	public void testUpdateSelectedPlayersSellToPotPrice() throws Exception {
		// arrange
		final ArgumentCaptor<Team> teamCaptor = ArgumentCaptor.forClass(Team.class);
		
		final Team team = TeamTestDataBuilder.aTeam().withSquadSize(1).build();
    	final PlayerDto playerDto = PlayerDtoTestDataBuilder.aPlayer().build();
    	
    	final Map<Integer, PlayerDto> playerDtoMap = new HashMap<Integer, PlayerDto>();
    	playerDtoMap.put(playerDto.getId(), playerDto);
    	
    	when(teamDataService.getTeams()).thenReturn(Arrays.asList(team));
		
		// act
    	teamFacade.updateSelectedPlayersSellToPotPrice(playerDtoMap);
		
		// assert
    	verify(teamDataService).updateTeam(teamCaptor.capture());
    	
    	assertThat(teamCaptor.getValue().getSelectedPlayers().get(0).getCurrentSellToPotPrice()).isNotNull();
	}

}
