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
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.types.auction.AuctionPhaseStatus;
import net.blackcat.fantasy.draft.integration.repository.LeagueRepository;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.testdata.AuctionPhaseTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.AuctionTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.LeagueTestDataBuilder;
import net.blackcat.fantasy.draft.integration.testdata.TeamTestDataBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit tests for {@link SpringDataLeagueDataService}.
 * 
 * @author Chris Hern
 * 
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/hsqlDatasourceContext.xml", "/testApplicationContext.xml" })
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
    public void testAddLeague() {
        // arrange
        final League league = LeagueTestDataBuilder.aLeague().build();

        // act
        leagueDataService.addLeague(league);

        // assert
        assertThat(repository.exists(league.getId())).isTrue();
    }

    @Test
    public void testGetLeague_Success() throws Exception {
        // arrange
        final String newLeagueName = "New league name";
        final League league = LeagueTestDataBuilder.aLeague().withName(newLeagueName).build();

        repository.save(league);

        // act
        final League actualLeague = leagueDataService.getLeague(league.getId());

        // assert
        assertThat(actualLeague).isNotNull();
        assertThat(actualLeague.getName()).isEqualTo(newLeagueName);
    }

    @Test
    public void testGetLeague_NotFound() throws Exception {
        // arrange
        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_NOT_FOUND));

        // act
        leagueDataService.getLeague(99999);

        // assert
        Assert.fail("Exception expected.");
    }

    @Test
    public void testUpdateLeague_Success() throws Exception {
        // arrange
        final String newLeagueName = "Update league name";
        final League league = LeagueTestDataBuilder.aLeague().withName(newLeagueName).build();

        repository.save(league);

        final League initialLeague = repository.findOne(league.getId());

        assertThat(initialLeague.getTeams()).isEmpty();

        // act
        final Team team = TeamTestDataBuilder.aTeam().build();
        league.addTeam(team);

        leagueDataService.updateLeague(league);

        // assert
        final League actualLeague = repository.findOne(league.getId());

        assertThat(actualLeague.getTeams()).contains(team);
    }

    @Test
    public void testUpdateLeague_LeagueNotFound() throws Exception {
        // arrange
        final League league = mock(League.class);

        when(league.getId()).thenReturn(9999999);

        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.LEAGUE_NOT_FOUND));

        // act
        leagueDataService.updateLeague(league);

        // assert
        Assert.fail("Exception expected.");
    }

    @Test
    public void testGetOpenAuctionPhase_Success() throws Exception {
        // arrange
        final AuctionPhase closedPhase = AuctionPhaseTestDataBuilder.aClosedAuctionPhase().build();
        final Auction auction = AuctionTestDataBuilder.anAuction().withPhase(closedPhase).build();
        final League league = LeagueTestDataBuilder.aLeague().withAuction(auction).build();

        repository.save(league);

        // act
        final AuctionPhase openAuctionPhase = leagueDataService.getOpenAuctionPhase(league);

        // assert
        assertThat(openAuctionPhase).isNotNull();
        assertThat(openAuctionPhase.getStatus()).isEqualTo(AuctionPhaseStatus.OPEN);
    }

    @Test
    public void testGetOpenAuctionPhase_DoesNotExist() throws Exception {
        // arrange
        final Auction auction = AuctionTestDataBuilder.anAuctionWithClosedPhase().build();
        final League league = LeagueTestDataBuilder.aLeague().withAuction(auction).build();

        repository.save(league);

        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.OPEN_AUCTION_PHASE_NOT_FOUND));

        // act
        leagueDataService.getOpenAuctionPhase(league);

        // assert
        Assert.fail("Exception expected.");
    }
}
