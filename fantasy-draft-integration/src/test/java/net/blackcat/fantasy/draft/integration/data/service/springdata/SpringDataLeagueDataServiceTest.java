/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Auction;
import net.blackcat.fantasy.draft.integration.model.AuctionPhase;
import net.blackcat.fantasy.draft.integration.model.League;
import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionPhaseStatus;
import net.blackcat.fantasy.draft.integration.repository.LeagueRepository;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

/**
 * Unit tests for {@link SpringDataLeagueDataService}.
 * 
 * These tests are currently ignored because Spring DBUnit seems unable to properly clear the DB down because of foreign
 * key constraints. I need to refactor these tests or find out exactly what Spring DB unit is doing.
 * 
 * @author Chris Hern
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/hsqlDatasourceContext.xml", "/testApplicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("LeagueData.xml")
@Ignore
public class SpringDataLeagueDataServiceTest {

    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Autowired
    private LeagueRepository repository;

    private SpringDataLeagueDataService leagueDataService;

    @Before
    public void setup() {

        leagueDataService = new SpringDataLeagueDataService(repository);
    }

    @Test
    public void testGetLeague_Success() throws Exception {
        // arrange

        // act
        final League league = leagueDataService.getLeague(TestDataConstants.LEAGUE_ONE_ID);

        // assert
        assertThat(league).isNotNull();
        assertThat(league.getId()).isEqualTo(TestDataConstants.LEAGUE_ONE_ID);
        assertThat(league.getName()).isEqualTo(TestDataConstants.LEAGUE_ONE_NAME);
    }

    @Test
    public void testGetLeague_NotFound() throws Exception {
        // arrange
        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_NOT_FOUND));

        // act
        leagueDataService.getLeague(100);

        // assert
        Assert.fail("Exception expected");
    }

    @Test
    @ExpectedDatabase("LeagueData-AddedLeague.xml")
    public void testAddLeague() {
        // arrange
        final League newLeague = new League(TestDataConstants.LEAGUE_FOUR_NAME);

        // act
        leagueDataService.addLeague(newLeague);

        // assert - done via @ExpectedDatabase annotation
    }

    @Test
    @ExpectedDatabase("LeagueData-UpdatedLeague.xml")
    public void testUpdateLeague_Success() throws Exception {
        // arrange
        final League league = leagueDataService.getLeague(TestDataConstants.LEAGUE_ONE_ID);

        league.setName("New Name");

        // act
        leagueDataService.updateLeague(league);

        // assert
    }

    @Test
    public void testUpdateLeague_LeagueDoesNotExist() throws Exception {
        // arrange
        final League league = mock(League.class);
        final Auction auction = new Auction();

        when(league.getId()).thenReturn(14);
        when(league.getName()).thenReturn(TestDataConstants.LEAGUE_ONE_NAME);

        league.setAuction(auction);

        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_NOT_FOUND));

        // act
        leagueDataService.updateLeague(league);

        // assert
        Assert.fail("Exception expected");
    }

    @Test
    @Transactional
    public void testGetOpenAuctionPhase_Success() throws Exception {
        // arrange
        final League league = leagueDataService.getLeague(TestDataConstants.LEAGUE_ONE_ID);

        // act
        final AuctionPhase openAuctionPhase = leagueDataService.getOpenAuctionPhase(league);

        // assert
        assertThat(openAuctionPhase).isNotNull();
        assertThat(openAuctionPhase.getStatus()).isEqualTo(AuctionPhaseStatus.OPEN);
        assertThat(openAuctionPhase.getId()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void testGetOpenAuctionPhase_NoOpenPhase() throws Exception {
        // arrange
        final League league = leagueDataService.getLeague(TestDataConstants.LEAGUE_TWO_ID);

        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_PHASE_NOT_FOUND));

        // act
        leagueDataService.getOpenAuctionPhase(league);

        // assert
        Assert.fail("Exception expected");
    }

    @Test
    public void testGetOpenAuctionPhase_NoAuction() throws Exception {
        // arrange
        final League league = leagueDataService.getLeague(TestDataConstants.LEAGUE_THREE_ID);

        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_PHASE_NOT_FOUND));

        // act
        leagueDataService.getOpenAuctionPhase(league);

        // assert
        Assert.fail("Exception expected");
    }
}
