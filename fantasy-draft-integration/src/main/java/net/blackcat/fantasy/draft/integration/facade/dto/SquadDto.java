/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.util.List;

/**
 * DTO used for transferring squad information.
 * 
 * @author Chris Hern
 * 
 */
public class SquadDto {

	private int teamId;
    private List<SelectedPlayerDto> currentPlayers;

    public SquadDto(final int teamId, final List<SelectedPlayerDto> currentPlayers) {
    	this.teamId = teamId;
        this.currentPlayers = currentPlayers;
    }

    /**
	 * @return the teamId
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
     * @return the currentPlayers
     */
    public List<SelectedPlayerDto> getCurrentPlayers() {
        return currentPlayers;
    }
}
