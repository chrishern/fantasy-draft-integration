/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Gameweek;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.repository.GameweekRepository;
import net.blackcat.fantasy.draft.integration.repository.GameweekScoreRepository;
import net.blackcat.fantasy.draft.integration.repository.PlayerRepository;
import net.blackcat.fantasy.draft.integration.repository.TeamRepository;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.testdata.PlayerTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.SelectedPlayerTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TeamTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * Unit tests for {@link SpringDataGameweekDataService}.
 * 
 * @author Chris
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/hsqlDatasourceContext.xml", "/testApplicationContext.xml" })
public class SpringDataGameweekDataServiceTest {

	private static final int PLAYER_GAMEWEEK_ONE_SCORE = 5;
	private static final int GAMEWEEK_ONE = 1;
	private static final int GAMEWEEK_TWO = 2;
	private static final int TEAM_ONE_GAMEWEEK_ONE_SCORE = 12;
	private static final int TEAM_ONE_GAMEWEEK_TWO_SCORE = 56;
	private static final int TEAM_TWO_GAMEWEEK_ONE_SCORE = 65;
	private static final int TEAM_TWO_GAMEWEEK_TWO_SCORE = 32;
	
	@Rule
    public ExpectedException thrownException = ExpectedException.none();

	@Autowired
	private GameweekRepository gameweekRepository;
	
	@Autowired
	private GameweekScoreRepository gameweekScoreRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	private SpringDataGameweekDataService dataService;

	
	@Before
	public void setup() {
		this.dataService = new SpringDataGameweekDataService(gameweekRepository, gameweekScoreRepository);
		
	}
	
	@Test
	public void testUpdateGameweek() {
		// arrange
		final Gameweek savedGameweek = new Gameweek();
		gameweekRepository.save(savedGameweek);

		final int initialCurrentGameweek = savedGameweek.getCurrentGameweek();
		savedGameweek.moveToNextGameweek();
		
		// act
		dataService.updateGameweek(savedGameweek);
		
		// assert
		final Gameweek updatedGameweek = gameweekRepository.findOne(savedGameweek.getId());
		
		assertThat(updatedGameweek.getCurrentGameweek()).isNotEqualTo(initialCurrentGameweek);
	}

	@Test
	public void testGetGameweek() {
		// arrange
		final Gameweek savedGameweek = new Gameweek();
		gameweekRepository.save(savedGameweek);
		
		// act
		final Gameweek retrievedGameweek = dataService.getGameweek();
		
		// assert
		ReflectionAssert.assertReflectionEquals(savedGameweek, retrievedGameweek);
	}
	
	@Test
	public void testGetGameweekScoreForTeam_Success() throws Exception {
		// arrange
		final Team team1 = TeamTestDataBuilder.aTeam()
				.withGameweekScore(GAMEWEEK_ONE, TEAM_ONE_GAMEWEEK_ONE_SCORE)
				.withGameweekScore(GAMEWEEK_TWO, TEAM_ONE_GAMEWEEK_TWO_SCORE)
				.build();
		
		final Team team2 = TeamTestDataBuilder.aTeam()
				.withName(TestDataConstants.TEAM_TWO_NAME)
				.withGameweekScore(GAMEWEEK_ONE, TEAM_TWO_GAMEWEEK_ONE_SCORE)
				.withGameweekScore(GAMEWEEK_TWO, TEAM_TWO_GAMEWEEK_TWO_SCORE)
				.build();
		
		teamRepository.save(team1);
		teamRepository.save(team2);
		
		// act
		final int gameweekScore = dataService.getGameweekScoreForTeam(team2.getId(), GAMEWEEK_TWO);
		
		// assert
		assertThat(gameweekScore).isEqualTo(TEAM_TWO_GAMEWEEK_TWO_SCORE);
	}
	
	@Test
	public void testGetGameweekScoreForTeam_NoGameweekScore() throws Exception {
		// arrange
		final Team team = TeamTestDataBuilder.aTeam()
				.withName(TestDataConstants.TEAM_THREE_NAME)
				.withGameweekScore(GAMEWEEK_ONE, TEAM_ONE_GAMEWEEK_ONE_SCORE)
				.withGameweekScore(GAMEWEEK_TWO, TEAM_ONE_GAMEWEEK_TWO_SCORE)
				.build();
		
		teamRepository.save(team);
		
		thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.GAMEWEEK_SCORE_NOT_FOUND));
		
		// act
		final int gameweekScore = dataService.getGameweekScoreForTeam(team.getId(), 3);
		
		// assert
		assertThat(gameweekScore).isEqualTo(TEAM_TWO_GAMEWEEK_TWO_SCORE);
	}
	
	@Test
	public void testGetGameweekScoreForPlayer() {
		// arrange
		final Player player = PlayerTestDataBuilder.aPlayer().build();
		final SelectedPlayer selectedPlayer = SelectedPlayerTestDataBuilder.aSelectedPlayer(player)
				.withGameweekScore(GAMEWEEK_ONE, PLAYER_GAMEWEEK_ONE_SCORE)
				.build();
		
		final Team team = TeamTestDataBuilder.aTeam()
				.withName(TestDataConstants.TEAM_FOUR_NAME)
				.build();
		
		team.getSelectedPlayers().add(selectedPlayer);
		
		playerRepository.save(player);
		teamRepository.save(team);
		
		// act
		final Integer gameweekScoreForPlayer = dataService.getGameweekScoreForPlayer(selectedPlayer.getId(), GAMEWEEK_ONE);
		
		// assert
		assertThat(gameweekScoreForPlayer).isEqualTo(PLAYER_GAMEWEEK_ONE_SCORE);
	}
	
	@Test
	public void testGetGameweekScoreForPlayer_NoScore() {
		// arrange
		final Player player = PlayerTestDataBuilder.aPlayer().build();
		final SelectedPlayer selectedPlayer = SelectedPlayerTestDataBuilder.aSelectedPlayer(player)
				.withGameweekScore(GAMEWEEK_ONE, PLAYER_GAMEWEEK_ONE_SCORE)
				.build();
		
		final Team team = TeamTestDataBuilder.aTeam()
				.withName(TestDataConstants.TEAM_FIVE_NAME)
				.build();
		
		team.getSelectedPlayers().add(selectedPlayer);
		
		playerRepository.save(player);
		teamRepository.save(team);
		
		// act
		final Integer gameweekScoreForPlayer = dataService.getGameweekScoreForPlayer(selectedPlayer.getId(), GAMEWEEK_TWO);
		
		// assert
		assertThat(gameweekScoreForPlayer).isNull();
	}
}