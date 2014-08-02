/**
 * 
 */
package net.blackcat.fantasy.draft.integration.exception;

/**
 * Class representing known exceptions that are thrown within the fantasy draft integration.
 * 
 * @author Chris
 *
 */
public class FantasyDraftIntegrationException extends Exception {

	private static final long serialVersionUID = 1358700723366211771L;

	private FantasyDraftIntegrationExceptionCode code;
	
	public FantasyDraftIntegrationException(final FantasyDraftIntegrationExceptionCode code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public FantasyDraftIntegrationExceptionCode getCode() {
		return code;
	}
}
