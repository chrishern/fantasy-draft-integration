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

    private List<SelectedPlayerDto> currentPlayers;

    public SquadDto(final List<SelectedPlayerDto> currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    /**
     * @return the currentPlayers
     */
    public List<SelectedPlayerDto> getCurrentPlayers() {
        return currentPlayers;
    }
}
