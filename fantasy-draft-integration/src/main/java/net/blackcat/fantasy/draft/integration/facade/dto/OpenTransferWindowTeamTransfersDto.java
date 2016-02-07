/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for transferring team transfers within an open transfer window.
 * 
 * @author Chris
 *
 */
public class OpenTransferWindowTeamTransfersDto implements Serializable {

	private static final long serialVersionUID = -2123798780606773394L;

	private int teamId;
	private List<TransferDetailDto> myPendingBids;
	private List<TransferDetailDto> incomingPendingBids;
	private List<TransferDetailDto> myBoughtPlayers;
	private List<TransferDetailDto> mySoldPlayers;
	private List<TransferDetailDto> myRejectedBids;
	private List<TransferDetailDto> incomingRejectedBids;
	
	public OpenTransferWindowTeamTransfersDto(final int teamId) {
		this.teamId = teamId;
		this.myPendingBids = new ArrayList<TransferDetailDto>();
		this.incomingPendingBids = new ArrayList<TransferDetailDto>();
		this.myBoughtPlayers = new ArrayList<TransferDetailDto>();
		this.mySoldPlayers = new ArrayList<TransferDetailDto>();
		this.myRejectedBids = new ArrayList<TransferDetailDto>();
		this.incomingRejectedBids = new ArrayList<TransferDetailDto>();
	}

	/**
	 * Add an outgoing pending transfer
	 * 
	 * @param forename Forename of the player bidded for.
	 * @param surname Surname of the player bidded for.
	 * @param buyingTeam Team making the bid.
	 * @param sellingTeam Team receiving the bid.
	 * @param amount Amount of the bid.
	 */
	public void addPendingBid(final int transferId, final String forename, final String surname, final String buyingTeam, final String sellingTeam, final BigDecimal amount) {
		final TransferDetailDto transfer = new TransferDetailDto(transferId, forename, surname, buyingTeam, sellingTeam, amount);
		this.myPendingBids.add(transfer);
	}
	
	/**
	 * Add an incoming pending transfer
	 * 
	 * @param forename Forename of the player bidded for.
	 * @param surname Surname of the player bidded for.
	 * @param buyingTeam Team making the bid.
	 * @param sellingTeam Team receiving the bid.
	 * @param amount Amount of the bid.
	 */
	public void addIncomingPendingBid(final int transferId, final String forename, final String surname, final String buyingTeam, final String sellingTeam, final BigDecimal amount) {
		final TransferDetailDto transfer = new TransferDetailDto(transferId, forename, surname, buyingTeam, sellingTeam, amount);
		this.incomingPendingBids.add(transfer);
	}
	
	/**
	 * Add a bought player
	 * 
	 * @param forename Forename of the player bidded for.
	 * @param surname Surname of the player bidded for.
	 * @param buyingTeam Team making the bid.
	 * @param sellingTeam Team receiving the bid.
	 * @param amount Amount of the bid.
	 */
	public void addBoughtPlayer(final int transferId, final String forename, final String surname, final String buyingTeam, final String sellingTeam, final BigDecimal amount) {
		final TransferDetailDto transfer = new TransferDetailDto(transferId, forename, surname, buyingTeam, sellingTeam, amount);
		this.myBoughtPlayers.add(transfer);
	}
	
	/**
	 * Add sold player
	 * 
	 * @param forename Forename of the player bidded for.
	 * @param surname Surname of the player bidded for.
	 * @param buyingTeam Team making the bid.
	 * @param sellingTeam Team receiving the bid.
	 * @param amount Amount of the bid.
	 */
	public void addSoldPlayer(final int transferId, final String forename, final String surname, final String buyingTeam, final String sellingTeam, final BigDecimal amount) {
		final TransferDetailDto transfer = new TransferDetailDto(transferId, forename, surname, buyingTeam, sellingTeam, amount);
		this.mySoldPlayers.add(transfer);
	}
	
	/**
	 * Add rejected bid
	 * 
	 * @param forename Forename of the player bidded for.
	 * @param surname Surname of the player bidded for.
	 * @param buyingTeam Team making the bid.
	 * @param sellingTeam Team receiving the bid.
	 * @param amount Amount of the bid.
	 */
	public void addOutgoingRejectedBid(final int transferId, final String forename, final String surname, final String buyingTeam, final String sellingTeam, final BigDecimal amount) {
		final TransferDetailDto transfer = new TransferDetailDto(transferId, forename, surname, buyingTeam, sellingTeam, amount);
		this.myRejectedBids.add(transfer);
	}
	
	/**
	 * Add an incoming rejected transfer
	 * 
	 * @param forename Forename of the player bidded for.
	 * @param surname Surname of the player bidded for.
	 * @param buyingTeam Team making the bid.
	 * @param sellingTeam Team receiving the bid.
	 * @param amount Amount of the bid.
	 */
	public void addIncomingRejectedBid(final int transferId, final String forename, final String surname, final String buyingTeam, final String sellingTeam, final BigDecimal amount) {
		final TransferDetailDto transfer = new TransferDetailDto(transferId, forename, surname, buyingTeam, sellingTeam, amount);
		this.incomingRejectedBids.add(transfer);
	}
	
	/**
	 * @return the teamId
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
	 * @return the myPendingBids
	 */
	public List<TransferDetailDto> getMyPendingBids() {
		return myPendingBids;
	}

	/**
	 * @return the incomingPendingBids
	 */
	public List<TransferDetailDto> getIncomingPendingBids() {
		return incomingPendingBids;
	}

	/**
	 * @return the myBoughtPlayers
	 */
	public List<TransferDetailDto> getMyBoughtPlayers() {
		return myBoughtPlayers;
	}

	/**
	 * @return the mySoldPlayers
	 */
	public List<TransferDetailDto> getMySoldPlayers() {
		return mySoldPlayers;
	}

	/**
	 * @return the myRejectedBids
	 */
	public List<TransferDetailDto> getMyRejectedBids() {
		return myRejectedBids;
	}

	/**
	 * @return the incomingRejectedBids
	 */
	public List<TransferDetailDto> getIncomingRejectedBids() {
		return incomingRejectedBids;
	}
	
}
