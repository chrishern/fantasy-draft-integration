/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.facade.dto.transferwindow.TransferWindowDto;

/**
 * DTO for transferring a summary of the activity within a transfer window.
 * 
 * @author Chris
 *
 */
public class TransferWindowSummaryDto implements Serializable {

	private static final long serialVersionUID = 8601040086924884432L;

	private List<TransferWindowDto> transferWindows;
	
	public TransferWindowSummaryDto() {
		transferWindows = new ArrayList<TransferWindowDto>();
	}
	
	public void addTransferWindow(final TransferWindowDto transferWindow) {
		transferWindows.add(transferWindow);
	}

	/**
	 * @return the transferWindows
	 */
	public List<TransferWindowDto> getTransferWindows() {
		return transferWindows;
	}
}
