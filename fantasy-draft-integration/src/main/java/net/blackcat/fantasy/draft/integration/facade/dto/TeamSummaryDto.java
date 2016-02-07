/**
 * 
 */
package net.blackcat.fantasy.draft.integration.facade.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.types.player.Position;

/**
 * @author Chris
 *
 */
public class TeamSummaryDto implements Serializable {

	private static final long serialVersionUID = 7987483747892501056L;

	private int teamId;
	private String teamName;
	private List<PlayerSummaryDto> players;
	
	public TeamSummaryDto(final int teamId, final String teamName) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.players = new ArrayList<PlayerSummaryDto>();
	}
	
	public void addPlayer(final int selectedPlayerId, final String forename, final String surname, final Position position) {
		final PlayerSummaryDto player = new PlayerSummaryDto(selectedPlayerId, forename, surname, position);
		players.add(player);
		Collections.sort(players);
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
	 * @return the players
	 */
	public List<PlayerSummaryDto> getPlayers() {
		return players;
	}
}
