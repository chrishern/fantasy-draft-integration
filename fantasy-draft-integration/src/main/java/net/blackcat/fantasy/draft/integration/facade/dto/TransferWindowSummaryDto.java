/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for transferring a summary of the activity within a transfer window.
 * 
 * @author Chris
 *
 */
public class TransferWindowSummaryDto implements Serializable {

	private static final long serialVersionUID = 8601040086924884432L;

	private List<AuctionPhaseResultsDto> auctionPhaseResults;
	private List<PotSaleSummaryDto> potSales;
	private List<TransferSummaryDto> transfers;
	
	public TransferWindowSummaryDto() {
		auctionPhaseResults = new ArrayList<AuctionPhaseResultsDto>();
		potSales = new ArrayList<PotSaleSummaryDto>();
		transfers = new ArrayList<TransferSummaryDto>();
	}

	/**
	 * @return the auctionPhaseResults
	 */
	public List<AuctionPhaseResultsDto> getAuctionPhaseResults() {
		return auctionPhaseResults;
	}

	/**
	 * @param auctionPhaseResults the auctionPhaseResults to set
	 */
	public void setAuctionPhaseResults(
			List<AuctionPhaseResultsDto> auctionPhaseResults) {
		this.auctionPhaseResults = auctionPhaseResults;
	}

	/**
	 * @return the potSales
	 */
	public List<PotSaleSummaryDto> getPotSales() {
		return potSales;
	}

	/**
	 * @param potSales the potSales to set
	 */
	public void setPotSales(List<PotSaleSummaryDto> potSales) {
		this.potSales = potSales;
	}

	/**
	 * @return the transfers
	 */
	public List<TransferSummaryDto> getTransfers() {
		return transfers;
	}

	/**
	 * @param transfers the transfers to set
	 */
	public void setTransfers(List<TransferSummaryDto> transfers) {
		this.transfers = transfers;
	}
}
