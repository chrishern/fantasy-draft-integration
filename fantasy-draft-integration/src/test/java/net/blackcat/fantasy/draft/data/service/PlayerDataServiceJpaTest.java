/**
 * 
 */
package net.blackcat.fantasy.draft.data.service;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.PlayerDataServiceJpa;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.test.util.TestDataUtil;

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

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "playerDataServiceJpa")
	private PlayerDataServiceJpa playerDataServiceJpa;
	
	@Test
	public void testGetPlayers() {
		// arrange
		final PlayerEntity player = TestDataUtil.createEntityPlayer(1);
		entityManager.persist(player);
		
		PlayerEntity find = entityManager.find(PlayerEntity.class, TestDataUtil.PLAYER_1_ID);
		assertThat(find).isNotNull();
		assertThat(find.getForename()).isEqualTo(TestDataUtil.PLAYER_1_FORENAME);
		
		// act
		final List<Player> players = playerDataServiceJpa.getPlayers();
		
		// assert
		assertThat(players).hasSize(1);
		
		final Player retrievedPlayer = players.get(0);
		assertThat(retrievedPlayer.getId()).isEqualTo(TestDataUtil.PLAYER_1_ID);
		assertThat(retrievedPlayer.getForename()).isEqualTo(TestDataUtil.PLAYER_1_FORENAME);
		assertThat(retrievedPlayer.getSurname()).isEqualTo(TestDataUtil.PLAYER_1_SURNAME);
		assertThat(retrievedPlayer.getTeam()).isEqualTo(TestDataUtil.TEST_TEAM);
		assertThat(retrievedPlayer.getTotalPoints()).isEqualTo(TestDataUtil.PLAYER_1_POINTS);
	}

	@Test
	public void testAddPlayers() {
		// arrange
		final Player player1 = TestDataUtil.createModelPlayer(1);
		final Player player2 = TestDataUtil.createModelPlayer(2);
		
		// act
		playerDataServiceJpa.addPlayers(Arrays.asList(player1, player2));
		
		// assert
		final List<Player> players = playerDataServiceJpa.getPlayers();
		
		assertThat(players).hasSize(2);
		
		final Player retrievedPlayer = players.get(0);
		assertThat(retrievedPlayer.getId()).isEqualTo(TestDataUtil.PLAYER_1_ID);
		assertThat(retrievedPlayer.getForename()).isEqualTo(TestDataUtil.PLAYER_1_FORENAME);
		assertThat(retrievedPlayer.getSurname()).isEqualTo(TestDataUtil.PLAYER_1_SURNAME);
		assertThat(retrievedPlayer.getTeam()).isEqualTo(TestDataUtil.TEST_TEAM);
		assertThat(retrievedPlayer.getTotalPoints()).isEqualTo(TestDataUtil.PLAYER_1_POINTS);
	}
}
