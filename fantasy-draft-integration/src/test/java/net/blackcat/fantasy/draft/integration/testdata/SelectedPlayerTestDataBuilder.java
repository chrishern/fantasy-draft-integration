/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.GameweekScore;
import net.blackcat.fantasy.draft.integration.model.Player;
import net.blackcat.fantasy.draft.integration.model.SelectedPlayer;
import net.blackcat.fantasy.draft.integration.model.types.player.Position;

/**
 * Class for building instances of {@link SelectedPlayer} objects to use in unit tests.
 * 
 * @author Chris
 *
 */
public class SelectedPlayerTestDataBuilder {

	private static final BigDecimal DEFAULT_COST = new BigDecimal("5.5");
	public static final BigDecimal DEFAULT_FPL_COST_AT_PURCHASE = new BigDecimal("7.5");
	
	private List<GameweekScore> gameweekScores;
	private Player player;
	
	private SelectedPlayerTestDataBuilder(final Position position) {
		this.gameweekScores = new ArrayList<GameweekScore>();
		this.player = PlayerTestDataBuilder.aPlayer().withPosition(position).build();
	}
	
	private SelectedPlayerTestDataBuilder(final Player player) {
		this.gameweekScores = new ArrayList<GameweekScore>();
		this.player = player;
	}
	
	public static SelectedPlayerTestDataBuilder aSelectedPlayer(final Position position) {
		return new SelectedPlayerTestDataBuilder(position);
	}
	
	public static SelectedPlayerTestDataBuilder aSelectedPlayer(final Player player) {
		return new SelectedPlayerTestDataBuilder(player);
	}
	
	public SelectedPlayerTestDataBuilder withGameweekScore(final int gameweek, final int score) {
    	final GameweekScore gameweekScore = new GameweekScore(gameweek, score);
    	
    	gameweekScores.add(gameweekScore);
    	
    	return this;
    }
	
	public SelectedPlayer build() {
		
		final SelectedPlayer selectedPlayer = new SelectedPlayer(player, DEFAULT_COST, DEFAULT_FPL_COST_AT_PURCHASE);
		
		for (final GameweekScore gameweekScore : gameweekScores) {
        	selectedPlayer.addGameweekScore(gameweekScore.getGameweek(), gameweekScore.getScore());
        }
		
		return selectedPlayer;
	}
}
