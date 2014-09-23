/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import static org.mockito.Mockito.verify;
import net.blackcat.fantasy.draft.integration.facade.TransferWindowFacade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link TransferWindowController}.
 * 
 * @author Chris
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TransferWindowControllerTest {

	@Mock
	private TransferWindowFacade transferWindowFacade;
	
	@InjectMocks
	private TransferWindowController transferWindowController = new TransferWindowController(); 
	
	@Test
	public void testStartTransferWindow() throws Exception {
		// arrange
		
		// act
		transferWindowController.startTransferWindow(1, 1);
		
		// assert
		verify(transferWindowFacade).startTransferWindow(1, 1);
	}

}
