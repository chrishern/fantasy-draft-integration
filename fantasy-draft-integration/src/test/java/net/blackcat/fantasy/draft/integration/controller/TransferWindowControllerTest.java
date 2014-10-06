/**
 * 
 */
package net.blackcat.fantasy.draft.integration.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import net.blackcat.fantasy.draft.integration.facade.TransferWindowFacade;
import net.blackcat.fantasy.draft.transfer.Transfer;

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

	@Test
	public void testAddTransfer() throws Exception {
		// arrange
		final Transfer transfer = mock(Transfer.class);
		
		// act
		transferWindowController.addTransfer(transfer);
		
		// assert
		verify(transferWindowFacade).addTransfer(transfer);
	}
}
