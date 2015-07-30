/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.repository.TeamRepository;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.Assert;
import org.junit.Before;
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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

/**
 * Unit tests for {@link SpringDataTeamDataService}.
 * 
 * @author Chris Hern
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/hsqlDatasourceContext.xml", "/testApplicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("TeamData.xml")
public class SpringDataTeamDataServiceTest {

    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Autowired
    private TeamRepository repository;

    private SpringDataTeamDataService dataService;

    @Before
    public void setup() {

        dataService = new SpringDataTeamDataService(repository);
    }

    @Test
    public void testGetTeam_Success() throws Exception {
        // arrange

        // act
        final Team team = dataService.getTeam(TestDataConstants.TEAM_TWO_ID);

        // assert
        assertThat(team).isNotNull();
        assertThat(team.getId()).isEqualTo(TestDataConstants.TEAM_TWO_ID);
    }

    @Test
    public void testGetTeam_NotFound() throws Exception {
        // arrange
        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.TEAM_NOT_FOUND));

        // act
        dataService.getTeam(3);

        // assert
        Assert.fail("Exception expected");
    }

    @Test
    public void testUpdateTeam_Success() throws Exception {
        // arrange
        final Team team = dataService.getTeam(TestDataConstants.TEAM_TWO_ID);
        team.addToTotalScore(10);

        // act
        dataService.updateTeam(team);

        // assert
        final Team updatedTeam = dataService.getTeam(TestDataConstants.TEAM_TWO_ID);

        assertThat(updatedTeam.getTotalScore()).isEqualTo(10);
    }

    @Test
    public void testUpdateTeam_NotFound() throws Exception {
        // arrange
        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.TEAM_NOT_FOUND));

        final Team team = mock(Team.class);

        when(team.getId()).thenReturn(3);

        // act
        dataService.updateTeam(team);

        // assert
        Assert.fail("Exception expected");
    }

    @Test
    public void testGetTeamForManager_Success() throws Exception {
        // arrange

        // act
        final Team team = dataService.getTeamForManager(TestDataConstants.USER_ONE_EMAIL_ADDRESS);

        // assert
        assertThat(team).isNotNull();
        assertThat(team.getName()).isEqualTo(TestDataConstants.TEAM_TWO_NAME);
    }

    @Test
    public void testGetTeamForManager_NotFound() throws Exception {
        // arrange
        thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.TEAM_NOT_FOUND));

        // act
        dataService.getTeamForManager(TestDataConstants.USER_TWO_EMAIL_ADDRESS);

        // assert
        Assert.fail("Exception expected");
    }
}
