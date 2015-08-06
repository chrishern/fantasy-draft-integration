/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO used for transferring squad information.
 * 
 * @author Chris Hern
 * 
 */
public class SquadDto {

	private int teamId;
	private String teamName;
    private List<SelectedPlayerDto> currentPlayers;

    public SquadDto(final int teamId, final String teamName) {
    	this.teamId = teamId;
    	this.teamName = teamName;
        this.currentPlayers = new ArrayList<SelectedPlayerDto>();
    }
    
    /**
     * Add a selected player to this squad.
     * 
     * @param selectedPlayer Player to add to this squad.
     */
    public void addSelectedPlayer(final SelectedPlayerDto selectedPlayer) {
    	currentPlayers.add(selectedPlayer);
    	Collections.sort(currentPlayers);
    }

    /**
	 * @return the teamId
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
     * @return the currentPlayers
     */
    public List<SelectedPlayerDto> getCurrentPlayers() {
        return currentPlayers;
    }
}
