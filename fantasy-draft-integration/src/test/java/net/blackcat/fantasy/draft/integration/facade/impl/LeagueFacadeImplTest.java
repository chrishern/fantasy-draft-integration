/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.entity.LeagueEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
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
 * Unit tests for {@link LeagueFacadeImpl}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LeagueFacadeImplTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
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

	@Test
	public void testGetLeagueTable_Success() throws Exception {
		// arrange
		final LeagueEntity leagueEntity = new LeagueEntity();
		leagueEntity.setTeams(TestDataUtil.createTeamEntitiesWithScore());
		
		when(leagueDataService.getLeague(1)).thenReturn(leagueEntity);
		
		// act
		final League leagueTable = leagueFacade.getLeagueTable(1);
		
		// assert
		final int firstScore = leagueTable.getTeams().get(0).getTotalScore();
		final int secondScore = leagueTable.getTeams().get(1).getTotalScore();
		final int thirdScore = leagueTable.getTeams().get(2).getTotalScore();
		
		assertThat(firstScore).isGreaterThan(secondScore);
		assertThat(secondScore).isGreaterThan(thirdScore);
	}
	
	@Test
	public void testGetLeagueTable_LeagueNotFound() throws Exception {
		// arrange
		when(leagueDataService.getLeague(1)).thenThrow(new FantasyDraftIntegrationException(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_DOES_NOT_EXIST));
		
		// act
		leagueFacade.getLeagueTable(1);
		
		// assert
		Assert.fail("Exception expected.");
	}
}
