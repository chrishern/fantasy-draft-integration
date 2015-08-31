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

	/*
	 * Only used for Hibernate database mapping.
	 */
	@SuppressWarnings("unused")
	private TransferWindow() {
		
	}
	
	public TransferWindow(final int leagueSequenceNumber) {
		this.leagueSequenceNumber = leagueSequenceNumber;
		this.potSales = new ArrayList<SaleToPot>();
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
}
