/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.blackcat.fantasy.draft.transfer.types.TransferStatus;

/**
 * Entity representing a transfer that has been agreed in a transfer window.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "Transfer")
public class TransferEntity implements Serializable {

	private static final long serialVersionUID = 5955391693448404160L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<TransferredPlayerEntity> players;
	
	@OneToOne
	private TeamEntity sellingTeam;
	
	@OneToOne
	private TeamEntity buyingTeam;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<ExchangedPlayerEntity> exchangedPlayers;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Column
	@Enumerated(EnumType.STRING)
	private TransferStatus status;

	public TransferEntity() {
	}

	public TransferEntity(final List<TransferredPlayerEntity> players, final TeamEntity sellingTeam, final TeamEntity buyingTeam, 
			final List<ExchangedPlayerEntity> exchangedPlayers, final BigDecimal amount) {
		this.players = players;
		this.sellingTeam = sellingTeam;
		this.buyingTeam = buyingTeam;
		this.exchangedPlayers = exchangedPlayers;
		this.amount = amount;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the players
	 */
	public List<TransferredPlayerEntity> getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(final List<TransferredPlayerEntity> players) {
		this.players = players;
	}

	/**
	 * @return the sellingTeam
	 */
	public TeamEntity getSellingTeam() {
		return sellingTeam;
	}

	/**
	 * @param sellingTeam the sellingTeam to set
	 */
	public void setSellingTeam(TeamEntity sellingTeam) {
		this.sellingTeam = sellingTeam;
	}

	/**
	 * @return the buyingTeam
	 */
	public TeamEntity getBuyingTeam() {
		return buyingTeam;
	}

	/**
	 * @param buyingTeam the buyingTeam to set
	 */
	public void setBuyingTeam(TeamEntity buyingTeam) {
		this.buyingTeam = buyingTeam;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the exchangedPlayers
	 */
	public List<ExchangedPlayerEntity> getExchangedPlayers() {
		return exchangedPlayers;
	}

	/**
	 * @param exchangedPlayers the exchangedPlayers to set
	 */
	public void setExchangedPlayers(List<ExchangedPlayerEntity> exchangedPlayers) {
		this.exchangedPlayers = exchangedPlayers;
	}

	/**
	 * @return the status
	 */
	public TransferStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TransferStatus status) {
		this.status = status;
	}
}