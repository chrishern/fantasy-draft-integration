package net.blackcat.fantasy.draft.integration.facade.dto.transferwindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.facade.dto.AuctionPhaseResultsDto;
import net.blackcat.fantasy.draft.integration.facade.dto.PotSaleSummaryDto;
import net.blackcat.fantasy.draft.integration.facade.dto.TransferSummaryDto;

public class TransferWindowDto implements Serializable {

	private static final long serialVersionUID = -8044211877324593375L;

	private int sequenceNumber;
	private List<AuctionPhaseResultsDto> auctionPhaseResults;
	private List<PotSaleSummaryDto> potSales;
	private List<TransferSummaryDto> transfers;
	
	public TransferWindowDto(final int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
		this.auctionPhaseResults = new ArrayList<AuctionPhaseResultsDto>();
		this.potSales = new ArrayList<PotSaleSummaryDto>();
		this.transfers = new ArrayList<TransferSummaryDto>();
	}

	/**
	 * @return the sequenceNumber
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
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
	public void setAuctionPhaseResults(List<AuctionPhaseResultsDto> auctionPhaseResults) {
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
