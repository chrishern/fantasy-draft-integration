/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import static org.fest.assertions.Assertions.assertThat;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.repository.TeamRepository;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.testdata.TestDataConstants;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
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
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
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

    @After
    public void tearDown() {

        repository.deleteAll();
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
}
