/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.model.Gameweek;
import net.blackcat.fantasy.draft.integration.repository.GameweekRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit tests for {@link SpringDataGameweekDataService}.
 * 
 * @author Chris
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/hsqlDatasourceContext.xml", "/testApplicationContext.xml" })
public class SpringDataGameweekDataServiceTest {

	@Autowired
	private GameweekRepository repository;
	
	private SpringDataGameweekDataService dataService;

	private Gameweek savedGameweek;
	
	@Before
	public void setup() {
		this.dataService = new SpringDataGameweekDataService(repository);
		
		savedGameweek = new Gameweek();
		
		repository.save(savedGameweek);
	}
	
	@Test
	public void testUpdateGameweek() {
		// arrange
		final int initialCurrentGameweek = savedGameweek.getCurrentGameweek();
		savedGameweek.moveToNextGameweek();
		
		// act
		dataService.updateGameweek(savedGameweek);
		
		// assert
		final Gameweek updatedGameweek = repository.findOne(savedGameweek.getId());
		
		assertThat(updatedGameweek.getCurrentGameweek()).isNotEqualTo(initialCurrentGameweek);
	}

}
