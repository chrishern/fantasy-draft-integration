/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Model object representing a sale to the pot.
 * 
 * @author Chris
 *
 */
@Entity
public class SaleToPot implements Serializable {

	private static final long serialVersionUID = 6873340107996804603L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	@ManyToOne
	private Team team;
	
	@OneToOne
	private SelectedPlayer player;
	
	@Column
	private BigDecimal amount;

	/*
	 * Used for Hibernate database mapping.
	 */
	@SuppressWarnings("unused")
	private SaleToPot() {
		
	}
	
	public SaleToPot(final Team team, final SelectedPlayer player, final BigDecimal amount) {
		this.team = team;
		this.player = player;
		this.amount = amount;
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @return the player
	 */
	public SelectedPlayer getPlayer() {
		return player;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
}
