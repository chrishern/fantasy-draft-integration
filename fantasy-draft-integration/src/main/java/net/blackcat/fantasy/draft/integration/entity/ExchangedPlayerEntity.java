package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ExchangedPlayer")
public class ExchangedPlayerEntity implements Serializable {

	private static final long serialVersionUID = -2349230686738045583L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@OneToOne
	private PlayerEntity player;

	public ExchangedPlayerEntity() {
	}
	
	public ExchangedPlayerEntity(final PlayerEntity player) {
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public PlayerEntity getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(PlayerEntity player) {
		this.player = player;
	}
}
