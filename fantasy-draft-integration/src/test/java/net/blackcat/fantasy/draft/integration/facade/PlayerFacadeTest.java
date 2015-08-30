/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blackcat.fantasy.draft.integration.converter.ConverterService;
import net.blackcat.fantasy.draft.integration.data.service.PlayerDataService;
import net.blackcat.fantasy.draft.integration.facade.dto.PlayerDto;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.dto.PlayerDtoTestDataBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link PlayerFacade}.
 * 
 * @author Chris Hern
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerFacadeTest {

    @Captor
    private ArgumentCaptor<List<Player>> playerListCaptor;

    @Mock
    private ConverterService converterService;

    @Mock
    private PlayerDataService playerDataService;

    private PlayerFacade playerFacade;

    private PlayerDto playerDto1;
    private PlayerDto playerDto2;
    private Player player1;
    private Player player2;

    @Before
    public void setup() {
        playerFacade = new PlayerFacade(playerDataService, converterService);

        playerDto1 = PlayerDtoTestDataBuilder.aPlayer().build();
        playerDto2 = PlayerDtoTestDataBuilder.aPlayer().build();
        player1 = PlayerTestDataBuilder.aPlayer().build();
        player2 = PlayerTestDataBuilder.aPlayer().build();
    }

    @Test
    public void testAddPlayers() {
        // arrange
        when(converterService.convert(playerDto1, Player.class)).thenReturn(player1);
        when(converterService.convert(playerDto2, Player.class)).thenReturn(player2);

        // act
        playerFacade.addPlayers(Arrays.asList(playerDto1, playerDto2));

        // assert
        verify(playerDataService).addPlayers(playerListCaptor.capture());

        assertThat(playerListCaptor.getValue()).containsOnly(player1, player2);
    }

    @Test
    public void testGetPlayers() {
        // arrange
        when(converterService.convert(player1, PlayerDto.class)).thenReturn(playerDto1);
        when(converterService.convert(player2, PlayerDto.class)).thenReturn(playerDto2);

        when(playerDataService.getPlayers(Position.DEFENDER, PlayerSelectionStatus.SELECTED)).thenReturn(Arrays.asList(player1, player2));

        // act
        final List<PlayerDto> actualPlayerDtos = playerFacade.getPlayers(Position.DEFENDER, PlayerSelectionStatus.SELECTED);

        // assert
        assertThat(actualPlayerDtos).containsOnly(playerDto1, playerDto2);
    }
    
    @Test
    public void testUpdatePlayersStatistics() throws Exception {
    	// arrange
    	final Player player = PlayerTestDataBuilder.aPlayer().build();
    	final PlayerDto playerDto = PlayerDtoTestDataBuilder.aPlayer().build();
    	
    	when(playerDataService.getPlayer(playerDto.getId())).thenReturn(player);
    	
    	// act
    	playerFacade.updatePlayersStatistics(Arrays.asList(playerDto));
    	
    	// assert
    	verify(playerDataService).updatePlayers(playerListCaptor.capture());
    	
    	final List<Player> updatedPlayerList = playerListCaptor.getValue();
    	assertThat(updatedPlayerList).hasSize(1);
    	
    	final Player updatedPlayer = updatedPlayerList.get(0);
    	
    	assertThat(updatedPlayer.getAssists()).isEqualTo(playerDto.getAssists());
    	assertThat(updatedPlayer.getCleanSheets()).isEqualTo(playerDto.getCleanSheets());
    	assertThat(updatedPlayer.getGoals()).isEqualTo(playerDto.getGoals());
    	assertThat(updatedPlayer.getPointsPerGame()).isEqualTo(playerDto.getPointsPerGame());
    	assertThat(updatedPlayer.getTotalPoints()).isEqualTo(playerDto.getTotalPoints());
    }
    
    @Test
    public void testUpdatePlayersCurrentPrice() throws Exception {
    	// arrange
    	final Player player = PlayerTestDataBuilder.aPlayer().build();
    	final PlayerDto playerDto = PlayerDtoTestDataBuilder.aPlayer().build();
    	
    	final Map<Integer, PlayerDto> playerDtoMap = new HashMap<Integer, PlayerDto>();
    	playerDtoMap.put(playerDto.getId(), playerDto);
    	
    	when(playerDataService.getPlayer(playerDto.getId())).thenReturn(player);
    	
    	// act
    	playerFacade.updatePlayersCurrentPrice(playerDtoMap);
    	
    	// assert
    	verify(playerDataService).updatePlayers(playerListCaptor.capture());
    	
    	final List<Player> updatedPlayerList = playerListCaptor.getValue();
    	assertThat(updatedPlayerList).hasSize(1);
    	
    	final Player updatedPlayer = updatedPlayerList.get(0);
    	
    	assertThat(updatedPlayer.getCurrentPrice()).isEqualTo(playerDto.getCurrentPrice());
    }
}
