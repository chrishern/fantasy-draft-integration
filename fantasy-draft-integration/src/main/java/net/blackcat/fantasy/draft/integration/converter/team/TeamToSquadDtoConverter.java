/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter.team;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.converter.IntegrationConverter;
import net.blackcat.fantasy.draft.integration.facade.dto.SelectedPlayerDto;
import net.blackcat.fantasy.draft.integration.facade.dto.SquadDto;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.Team;

import org.springframework.core.convert.converter.Converter;

/**
 * Converter to convert {@link Team} objects into a {@link SquadDto} object.
 * 
 * @author Chris Hern
 * 
 */
@IntegrationConverter
public class TeamToSquadDtoConverter implements Converter<Team, SquadDto> {

    @Override
    public SquadDto convert(final Team team) {

        final SquadDto squad = new SquadDto(team.getId(), createSelectedPlayers(team));

        return squad;
    }

    /*
     * Create the SelectedPlayerDto objects for the given team.
     */
    private List<SelectedPlayerDto> createSelectedPlayers(final Team team) {

        final List<SelectedPlayerDto> selectedPlayers = new ArrayList<SelectedPlayerDto>();

        for (final SelectedPlayer selectedPlayer : team.getSelectedPlayers()) {

            final Player player = selectedPlayer.getPlayer();

            final SelectedPlayerDto selectedPlayerDto = new SelectedPlayerDto();

            selectedPlayerDto.setCost(selectedPlayer.getCost());
            selectedPlayerDto.setForename(player.getForename());
            selectedPlayerDto.setSurname(player.getSurname());
            selectedPlayerDto.setPointsScored(selectedPlayer.getPointsScored());
            selectedPlayerDto.setPosition(player.getPosition());
            selectedPlayerDto.setSelectedPlayerId(selectedPlayer.getId());

            selectedPlayers.add(selectedPlayerDto);
        }
        return selectedPlayers;
    }

}
