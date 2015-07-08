/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.types.player.PlayerSelectionStatus;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;
import net.blackcat.fantasy.draft.integration.repository.PlayerRepository;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

/**
 * Unit tests for {@link SpringDataPlayerDataService}
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/hsqlDatasourceContext.xml", "/testApplicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("PlayerData.xml")
public class SpringDataPlayerDataServiceTest {

    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Autowired
    private PlayerRepository repository;

    private SpringDataPlayerDataService playerDataService;

    @Before
    public void setup() {

        playerDataService = new SpringDataPlayerDataService(repository);
    }

    @Test
    @ExpectedDatabase("PlayerData-AddedPlayer.xml")
    public void testAddPlayers() {
        // arrange
        final Player newPlayer = PlayerTestDataBuilder.aPlayer().build();

        // act
        playerDataService.addPlayers(Arrays.asList(newPlayer));

        // assert - done in @ExpectedDatabase annotation
    }

    @Test
    public void testGetPlayers_ByPosition() {
        // arrange

        // act
        final List<Player> players = playerDataService.getPlayers(Position.DEFENDER);

        // assert
        assertThat(players).hasSize(1);
        assertThat(players.get(0).getForename()).isEqualTo("Chris");
        assertThat(players.get(0).getPosition()).isEqualTo(Position.DEFENDER);
    }

    @Test
    public void testGetPlayers_ByPositionAndSelectionStatus() {
        // arrange

        // act
        final List<Player> players = playerDataService.getPlayers(Position.MIDFIELDER, PlayerSelectionStatus.NOT_SELECTED);

        // assert
        assertThat(players).hasSize(1);
        assertThat(players.get(0).getForename()).isEqualTo("Alex");
        assertThat(players.get(0).getPosition()).isEqualTo(Position.MIDFIELDER);
    }

    @Test
    public void testGetPlayer_ById() throws Exception {
        // arrange

        // act
        final Player player = playerDataService.getPlayer(2);

        // assert
        assertThat(player.getForename()).isEqualTo("Nicky");
        assertThat(player.getPosition()).isEqualTo(Position.MIDFIELDER);
    }

    @Test
    public void testGetPlayer_ById_DoesNotExist() throws Exception {
        // arrange
        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.PLAYER_NOT_FOUND));

        // act
        playerDataService.getPlayer(7);

        // assert
        Assert.fail("Exception expected.");
    }

    @Test
    @ExpectedDatabase("PlayerData-UpdatedPlayer.xml")
    public void testUpdatePlayer() {
        // arrange
        final Player newPlayer = PlayerTestDataBuilder.aPlayer().build();

        playerDataService.addPlayers(Arrays.asList(newPlayer));

        final Player updatedPlayer = PlayerTestDataBuilder.aPlayer().build();
        updatedPlayer.setTotalPoints(12);

        // act
        playerDataService.updatePlayer(updatedPlayer);

        // assert - done in @ExpectedDatabase annotation
    }
}
