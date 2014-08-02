/**
 * 
 */
package net.blackcat.fantasy.draft.test.util;

import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Class which allows for specific checks on the {@link FantasyDraftIntegrationException} to be performed.
 * 
 * @author Chris
 *
 */
public class CustomIntegrationExceptionMatcher extends TypeSafeMatcher<FantasyDraftIntegrationException> {

	private FantasyDraftIntegrationExceptionCode actualExceptionCode;
	private final FantasyDraftIntegrationExceptionCode expectedExceptionCode;
	
	public static CustomIntegrationExceptionMatcher hasCode(final FantasyDraftIntegrationExceptionCode item) {
		return new CustomIntegrationExceptionMatcher(item);
	}
	
	public CustomIntegrationExceptionMatcher(final FantasyDraftIntegrationExceptionCode expectedExceptionCode) {
		this.expectedExceptionCode = expectedExceptionCode;
	}
	
	@Override
	public void describeTo(final Description description) {
		description.appendText("Expected: ");
		description.appendValue(expectedExceptionCode);
		description.appendText(".  Actual: ");
		description.appendValue(actualExceptionCode);
	}

	@Override
	protected boolean matchesSafely(final FantasyDraftIntegrationException exception) {
		actualExceptionCode = exception.getCode();
		return expectedExceptionCode == exception.getCode();
	}
}
