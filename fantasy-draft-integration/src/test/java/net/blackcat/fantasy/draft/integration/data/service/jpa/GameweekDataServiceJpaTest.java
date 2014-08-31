/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.jpa;

import static org.fest.assertions.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.entity.GameweekEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit tests for {@link GameweekDataServiceJpa}.
 * 
 * @author Chris
 *
 */
@Transactional		
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"/hsqlDatasourceContext.xml", "/testApplicationContext.xml"})
public class GameweekDataServiceJpaTest {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier(value = "gameweekDataServiceJpa")
	private GameweekDataService gameweekDataService;
	
	@Test
	public void testGetGameweekData() {
		// arrange
		persistGameweekData();
		
		// act
		final GameweekEntity retrievedData = gameweekDataService.getGameweekData();
		
		// assert
		assertThat(retrievedData.getCurrentGameweek()).isEqualTo(2);
		assertThat(retrievedData.getPreviousGameweek()).isEqualTo(1);
	}
	
	@Test
	public void testUpdateGameweekData() {
		// arrange
		persistGameweekData();
		
		final GameweekEntity retrievedData = gameweekDataService.getGameweekData();

		// act
		retrievedData.setCurrentGameweek(3);
		retrievedData.setPreviousGameweek(2);
		
		gameweekDataService.updateGameweekData(retrievedData);
		
		// assert
		final GameweekEntity updatedData = gameweekDataService.getGameweekData();
		
		assertThat(updatedData.getCurrentGameweek()).isEqualTo(3);
		assertThat(updatedData.getPreviousGameweek()).isEqualTo(2);
	}

	private void persistGameweekData() {
		final GameweekEntity gameweek = new GameweekEntity();
		gameweek.setCurrentGameweek(2);
		gameweek.setPreviousGameweek(1);
		
		entityManager.persist(gameweek);
	}
}
