/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity class representing a managed team within the fantasy draft game.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "Team")
public class TeamEntity implements Serializable {

	private static final long serialVersionUID = 3011246980899172711L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(unique = true)
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private List<SelectedPlayerEntity> selectedPlayers;
	
	public TeamEntity() {
		
	}
	
	public TeamEntity(final String name) {
		this.name = name;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Add a new selected player to the list of selected players for this team.
	 * 
	 * @param selectedPlayer New selected player to add to this team.
	 */
	public void addSelectedPlayer(final SelectedPlayerEntity selectedPlayer) {
		if (selectedPlayers == null) {
			selectedPlayers = new ArrayList<SelectedPlayerEntity>();
		}
		
		selectedPlayers.add(selectedPlayer);
 	}
	
	/**
	 * @return the selectedPlayers
	 */
	public List<SelectedPlayerEntity> getSelectedPlayers() {
		return selectedPlayers;
	}
}
