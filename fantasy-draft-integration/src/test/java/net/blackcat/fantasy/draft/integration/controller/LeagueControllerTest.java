/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.facade.LeagueFacade;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.league.League;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
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
	
	@Test
	public void testGetLeagueTable_Success() throws Exception {
		// arrange
		final League league  = new League(TestDataUtil.LEAGUE_NAME);
		
		when(leagueFacade.getLeagueTable(1)).thenReturn(league);
		
		// act
		final League leagueTable = leagueController.getLeagueTable(1);
		
		// assert
		assertThat(leagueTable).isEqualTo(league);
	}
	
	@Test
	public void testGetLeagueTable_LeagueNotFound() throws Exception {
		// arrange
		when(leagueFacade.getLeagueTable(1)).thenThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		// act
		leagueController.getLeagueTable(1);
		
		// assert
		Assert.fail("Exception expected.");
	}

}
