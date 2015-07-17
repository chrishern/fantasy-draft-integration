/**
 * 
 */
package net.blackcat.fantasy.draft.integration.data.service.springdata;

import static org.fest.assertions.Assertions.assertThat;

import javax.transaction.Transactional;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.model.Team;
import net.blackcat.fantasy.draft.integration.model.User;
import net.blackcat.fantasy.draft.integration.repository.UserRepository;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.testdata.UserTestDataBuilder;

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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

/**
 * Unit tests for {@link SpringDataUserDataService}.
 * 
 * @author Chris
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/hsqlDatasourceContext.xml", "/testApplicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("UserData.xml")
public class SpringDataUserDataServiceTest {

	private static final String TEAM_NAME = "Test Team";
	private static final String VALID_EMAIL_ADDRESS = "user1@test.com";
	private static final String INVALID_EMAIL_ADDRESS = "invalid@email.com";

	@Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Autowired
    private UserRepository repository;

    private SpringDataUserDataService userDataService;

    @Before
    public void setup() {

        userDataService = new SpringDataUserDataService(repository);
    }
	
	@Test
	public void testGetUser_Success() throws Exception {
		// arrange
		
		// act
		final User user = userDataService.getUser(VALID_EMAIL_ADDRESS);
		
		// assert
		assertThat(user).isNotNull();
		assertThat(user.getEmailAddress()).isEqualTo(VALID_EMAIL_ADDRESS);
	}

	@Test
	public void testGetUser_NotFound() throws Exception {
		// arrange
		thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.USER_NOT_FOUND));
		
		// act
        userDataService.getUser(INVALID_EMAIL_ADDRESS);
		
		// assert
        Assert.fail("Exception expected");
	}
	
	@Test
	@ExpectedDatabase("UserData-AddedUser.xml")
	public void testAddUser_Success() throws Exception {
		// arrange
		final User newUser = UserTestDataBuilder.aManager().build();
		
		// act
		userDataService.addUser(newUser);
		
		// assert - done in @ExpectedDatabase annotation
	}
	
	@Test
	public void testAddUser_AlreadyExists() throws Exception {
		// arrange
		thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.USER_ALREADY_EXISTS));
		
        final User user = UserTestDataBuilder.aManagerWithEmailAddress(VALID_EMAIL_ADDRESS).build();
        
		// act
        userDataService.addUser(user);
		
		// assert
        Assert.fail("Exception expected");
	}
	
	@Test
	@ExpectedDatabase("UserData-WithTeam.xml")
	@Transactional
	public void testUpdateUser_AddedTeam_Success() throws Exception {
		// arrange
		final Team team = new Team(TEAM_NAME);
		final User user = userDataService.getUser(VALID_EMAIL_ADDRESS);
		
		user.addManagedTeam(team);
		
		// act
		userDataService.updateUser(user);
		
		// assert - done by @ExpectedDatabase annotation
	}
	
	@Test
	public void testUpdateUser_AddedTeam_UserDoesNotExist() throws Exception {
		// arrange
		thrownException.expect(FantasyDraftIntegrationException.class);
        thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.USER_NOT_FOUND));
		
		final Team team = new Team(TEAM_NAME);
		final User user = UserTestDataBuilder.aManagerWithEmailAddress(INVALID_EMAIL_ADDRESS).build();
		
		user.addManagedTeam(team);
		
		// act
		userDataService.updateUser(user);
		
		// assert
		Assert.fail("Exception expected");
	}
}
