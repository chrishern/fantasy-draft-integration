/**
 * 
 */
package net.blackcat.fantasy.draft.data.service;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.entity.PlayerEntity;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.Position;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit tests for {@link PlayerDataServiceJpa}.
 * 
 * @author Chris
 *
 */
@Ignore		
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"/hsqlDatasourceContext.xml", "/testApplicationContext.xml"})
public class PlayerDataServiceJpaTest {

	private static final long PLAYER_1_ID = 1;
	private static final int PLAYER_1_POINTS = 54;
	private static final String PLAYER_1_SURNAME = "Player";
	private static final String PLAYER_1_FORENAME = "Test";
	private static final long PLAYER_2_ID = 2;
	private static final int PLAYER_2_POINTS = 89;
	private static final String PLAYER_2_SURNAME = "Player2";
	private static final String PLAYER_2_FORENAME = "Test2";
	private static final String TEST_TEAM = "Test Team";

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "playerDataServiceJpa")
	private PlayerDataServiceJpa playerDataServiceJpa;
	
	@Test
	public void testGetPlayers() {
		// arrange
		final PlayerEntity player = new PlayerEntity(PLAYER_1_ID, PLAYER_1_FORENAME, PLAYER_1_SURNAME, TEST_TEAM, Position.DEFENDER, true, PLAYER_1_POINTS);
		entityManager.persist(player);
		
		PlayerEntity find = entityManager.find(PlayerEntity.class, PLAYER_1_ID);
		assertThat(find).isNotNull();
		assertThat(find.getForename()).isEqualTo(PLAYER_1_FORENAME);
		
		// act
		final List<Player> players = playerDataServiceJpa.getPlayers();
		
		// assert
		assertThat(players).hasSize(1);
		
		final Player retrievedPlayer = players.get(0);
		assertThat(retrievedPlayer.getId()).isEqualTo(PLAYER_1_ID);
		assertThat(retrievedPlayer.getForename()).isEqualTo(PLAYER_1_FORENAME);
		assertThat(retrievedPlayer.getSurname()).isEqualTo(PLAYER_1_SURNAME);
		assertThat(retrievedPlayer.getTeam()).isEqualTo(TEST_TEAM);
		assertThat(retrievedPlayer.getTotalPoints()).isEqualTo(PLAYER_1_POINTS);
	}

	@Test
	public void testAddPlayers() {
		// arrange
		final Player player1 = new Player(PLAYER_1_ID, PLAYER_1_FORENAME, PLAYER_1_SURNAME, TEST_TEAM, Position.DEFENDER, PLAYER_1_POINTS);
		final Player player2 = new Player(PLAYER_2_ID, PLAYER_2_FORENAME, PLAYER_2_SURNAME, TEST_TEAM, Position.STRIKER, PLAYER_2_POINTS);
		
		// act
		playerDataServiceJpa.addPlayers(Arrays.asList(player1, player2));
		
		// assert
		final List<Player> players = playerDataServiceJpa.getPlayers();
		
		assertThat(players).hasSize(2);
		
		final Player retrievedPlayer = players.get(0);
		assertThat(retrievedPlayer.getId()).isEqualTo(PLAYER_1_ID);
		assertThat(retrievedPlayer.getForename()).isEqualTo(PLAYER_1_FORENAME);
		assertThat(retrievedPlayer.getSurname()).isEqualTo(PLAYER_1_SURNAME);
		assertThat(retrievedPlayer.getTeam()).isEqualTo(TEST_TEAM);
		assertThat(retrievedPlayer.getTotalPoints()).isEqualTo(PLAYER_1_POINTS);
	}
}
