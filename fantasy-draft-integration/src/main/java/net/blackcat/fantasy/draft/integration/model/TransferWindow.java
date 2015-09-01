/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Model object storing transfer window details.
 * 
 * @author Chris
 *
 */
@Entity
public class TransferWindow implements Serializable {

	private static final long serialVersionUID = 7973855623977384491L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	@Column
	private int leagueSequenceNumber;
	
	@ManyToOne
    @JoinColumn(name = "league", referencedColumnName = "id")
    private League league;
	
	@OneToMany(cascade = { CascadeType.ALL })
	private List<SaleToPot> potSales;
	
	@OneToMany(cascade = { CascadeType.ALL })
	private List<Transfer> transfers;
	
	@OneToMany(cascade = { CascadeType.ALL })
	private List<AuctionPhase> auctionPhases;

	/*
	 * Only used for Hibernate database mapping.
	 */
	@SuppressWarnings("unused")
	private TransferWindow() {
		
	}
	
	public TransferWindow(final int leagueSequenceNumber) {
		this.leagueSequenceNumber = leagueSequenceNumber;
		this.potSales = new ArrayList<SaleToPot>();
		this.transfers = new ArrayList<Transfer>();
		this.auctionPhases = new ArrayList<AuctionPhase>();
	}
	
	/**
	 * Add a pot sale to this transfer window.
	 * 
	 * @param team Team selling the player to the pot.
	 * @param player {@link SelectedPlayer} that is being sold to the pot.
	 * @param amount Amount the player is being sold to the pot for.
	 */
	public void addPotSale(final Team team, final SelectedPlayer player, final BigDecimal amount) {
		final SaleToPot potSale = new SaleToPot(team, player, amount);
		potSales.add(potSale);
	}
	
	/**
	 * Make a transfer bid in the window.
	 * 
	 * @param buyingTeam The team making the offer.
	 * @param sellingTeam The team subject to the offer.
	 * @param playerBiddedFor The player subject to the offer.
	 * @param amount The amount of the offer.
	 */
	public void makeTransferBid(final Team buyingTeam, final Team sellingTeam, final SelectedPlayer playerBiddedFor, final BigDecimal amount) {
		final Transfer transfer = new Transfer(buyingTeam, sellingTeam, playerBiddedFor, amount);
		transfers.add(transfer);
	}
	
	/**
	 * Accept a transfer bid based on the ID of the bid.
	 * 
	 * @param bidId ID of the bis to accept.
	 */
	public void acceptTransfer(final int bidId) {

		final Transfer bid = getTransfer(bidId);
		bid.acceptBid();
	}
	
	/**
	 * Reject a transfer bid based on the ID of the bid.
	 * 
	 * @param bidId ID of the bis to accept.
	 */
	public void rejectTransfer(final int bidId) {
		
		final Transfer bid = getTransfer(bidId);
		bid.rejectBid();
	}
	
	/**
	 * @return the transfers
	 */
	public List<Transfer> getTransfers() {
		return transfers;
	}
	
	/**
	 * Get the open auction phase for this transfer window.
	 * 
	 * @return The open auction phase for this transfer window or null if one does not exist.
	 */
	public AuctionPhase getOpenAuctionPhase() {
		
		for (final AuctionPhase phase : auctionPhases) {
			if (phase.isOpen()) {
				return phase;
			}
		}
		
		return null;
	}
	
	/**
	 * Start a new auction phase for this transfer window.
	 */
	public void startAuctionPhase() {
		
		final AuctionPhase newPhase = new AuctionPhase(auctionPhases.size() + 1);
		auctionPhases.add(newPhase);
	}

	/*
	 * Get a transfer bid based on the bid ID.
	 */
	private Transfer getTransfer(final int bidId) {
		
		for (final Transfer bid : transfers) {
			if (bid.getId() == bidId) {
				return bid;
			}
		}
		
		return null;
	}

	/**
	 * @return the auctionPhases
	 */
	public List<AuctionPhase> getAuctionPhases() {
		return auctionPhases;
	}

	/**
	 * @return the potSales
	 */
	public List<SaleToPot> getPotSales() {
		return potSales;
	}
	
}
