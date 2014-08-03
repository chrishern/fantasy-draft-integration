/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import static org.mockito.Mockito.verify;
import net.blackcat.fantasy.draft.integration.facade.LeagueFacade;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link LeagueController}
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LeagueControllerTest {

	@Mock
	private LeagueFacade leagueFacade;
	
	@InjectMocks
	private LeagueController leagueController = new LeagueController();
	
	@Test
	public void testCreateLeague() {
		// arrange
		
		// act
		leagueController.createLeague(TestDataUtil.LEAGUE_NAME);
		
		// assert
		verify(leagueFacade).createLeague(TestDataUtil.LEAGUE_NAME);
	}

}
