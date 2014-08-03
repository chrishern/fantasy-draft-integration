/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import static org.mockito.Mockito.verify;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link LeagueFacadeImpl}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LeagueFacadeImplTest {

	@Mock
	private LeagueDataService leagueDataService;
	
	@InjectMocks
	private LeagueFacadeImpl leagueFacade = new LeagueFacadeImpl();
	
	@Test
	public void testCreateLeague() {
		// arrange
		
		// act
		leagueFacade.createLeague(TestDataUtil.LEAGUE_NAME);
		
		// assert
		verify(leagueDataService).createLeague(TestDataUtil.LEAGUE_NAME);
	}

}
