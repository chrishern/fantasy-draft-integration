/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata.dto;

import net.blackcat.fantasy.draft.integration.facade.dto.SelectedPlayerDto;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;
import net.blackcat.fantasy.draft.integration.model.types.player.StartingTeamStatus;

/**
 * Class for building instances of {@link SelectedPlayerDto} objects to be used in unit tests.
 * 
 * @author Chris
 *
 */
public class SelectedPlayerDtoTestDataBuilder {

	private Position position;
	private StartingTeamStatus startingTeamStatus;
	
	private SelectedPlayerDtoTestDataBuilder(final Position position) {
		this.position = position;
	}
	
	public static SelectedPlayerDtoTestDataBuilder aGoalkeeper() {
		return new SelectedPlayerDtoTestDataBuilder(Position.GOALKEEPER);
	}
	
	public static SelectedPlayerDtoTestDataBuilder aDefender() {
		return new SelectedPlayerDtoTestDataBuilder(Position.DEFENDER);
	}
	
	public static SelectedPlayerDtoTestDataBuilder aMidfielder() {
		return new SelectedPlayerDtoTestDataBuilder(Position.MIDFIELDER);
	}
	
	public static SelectedPlayerDtoTestDataBuilder aStriker() {
		return new SelectedPlayerDtoTestDataBuilder(Position.STRIKER);
	}
	
	public SelectedPlayerDtoTestDataBuilder withStartingTeamStatus(final StartingTeamStatus startingTeamStatus) {
		this.startingTeamStatus = startingTeamStatus;
		return this;
	}
	
	public SelectedPlayerDto build() {
		final SelectedPlayerDto dto = new SelectedPlayerDto();
		
		dto.setPosition(position);
		dto.setStartingTeamStatus(startingTeamStatus);
		
		return dto;
	}
}
