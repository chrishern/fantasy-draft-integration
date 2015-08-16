/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import java.math.BigDecimal;

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
	private static final BigDecimal DEFAULT_FPL_COST_AT_PURCHASE = new BigDecimal("7.5");
	
	private Position position;
	
	private SelectedPlayerTestDataBuilder(final Position position) {
		this.position = position;
	}
	
	public static SelectedPlayerTestDataBuilder aSelectedPlayer(final Position position) {
		return new SelectedPlayerTestDataBuilder(position);
	}
	
	public SelectedPlayer build() {
		
		final Player player = PlayerTestDataBuilder.aPlayer().withPosition(position).build();
		final SelectedPlayer selectedPlayer = new SelectedPlayer(player, DEFAULT_COST, DEFAULT_FPL_COST_AT_PURCHASE);
		
		return selectedPlayer;
	}
}
