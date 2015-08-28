/**
 * 
 */
package net.blackcat.fantasy.draft.integration.converter.team;

import net.blackcat.fantasy.draft.integration.converter.IntegrationConverter;
import net.blackcat.fantasy.draft.integration.data.service.GameweekDataService;
import net.blackcat.fantasy.draft.integration.facade.dto.SelectedPlayerDto;
import net.blackcat.fantasy.draft.integration.facade.dto.SquadDto;
import net.blackcat.fantasy.draft.integration.model.Gameweek;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * Converter to convert {@link Team} objects into a {@link SquadDto} object.
 * 
 * @author Chris Hern
 * 
 */
@IntegrationConverter
public class TeamToSquadDtoConverter implements Converter<Team, SquadDto> {

	private GameweekDataService gameweekDataService;
	
	@Autowired
	public TeamToSquadDtoConverter(final GameweekDataService gameweekDataService) {
		this.gameweekDataService = gameweekDataService;
	}
	
    @Override
    public SquadDto convert(final Team team) {

        final SquadDto squad = new SquadDto(team.getId(), team.getName());

        addSelectedPlayers(team, squad);
        
        return squad;
    }

    /*
     * Add the SelectedPlayerDto objects for the given team.
     */
    private void addSelectedPlayers(final Team team, final SquadDto squad) {

    	final Gameweek gameweek = gameweekDataService.getGameweek();
    	final int previousGameweek = gameweek.getPreviousGameweek();
    	
        for (final SelectedPlayer selectedPlayer : team.getSelectedPlayers()) {

            final Player player = selectedPlayer.getPlayer();
            final SelectedPlayerDto selectedPlayerDto = new SelectedPlayerDto();
			final Integer gameweekScoreForPlayer = gameweekDataService.getGameweekScoreForPlayer(selectedPlayer.getId(), previousGameweek);

            selectedPlayerDto.setCost(selectedPlayer.getCost());
            selectedPlayerDto.setForename(player.getForename());
            selectedPlayerDto.setSurname(player.getSurname());
            selectedPlayerDto.setPointsScored(selectedPlayer.getPointsScored());
            selectedPlayerDto.setPosition(player.getPosition());
            selectedPlayerDto.setSelectedPlayerId(selectedPlayer.getId());
            selectedPlayerDto.setWeeklyPointsScored(gameweekScoreForPlayer);

            squad.addSelectedPlayer(selectedPlayerDto);
        }
    }

}
