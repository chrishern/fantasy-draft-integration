/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.types.player.Position;
import net.blackcat.fantasy.draft.integration.testdata.SelectedPlayerTestDataBuilder;

import org.junit.Test;

/**
 * Unit tests for {@link SelectedPlayer}.
 * 
 * @author Chris
 *
 */
public class SelectedPlayerTest {

	private static final BigDecimal PRICE_CHANGE = new BigDecimal("0.5");

	@Test
	public void testCompareTo() {
		// arrange
		final SelectedPlayer goalkeeper = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.GOALKEEPER).build();
		final SelectedPlayer defender = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.DEFENDER).build();
		final SelectedPlayer midfielder = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.MIDFIELDER).build();
		final SelectedPlayer striker = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.STRIKER).build();
		
		final List<SelectedPlayer> selectedPlayers = Arrays.asList(midfielder, striker, goalkeeper, defender);
		
		// act
		Collections.sort(selectedPlayers);
		
		// assert
		assertThat(selectedPlayers).containsExactly(goalkeeper, defender, midfielder, striker);
	}
	
	@Test
	public void testUpdateCurrentSellToPotPrice_PriceIncrease() {
		// arrange
		final SelectedPlayer selectedPlayer = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.GOALKEEPER).build();
		
		final BigDecimal newFplPrice = selectedPlayer.getFplCostAtPurchase().add(PRICE_CHANGE);
		
		// act
		selectedPlayer.updateCurrentSellToPotPrice(newFplPrice);
		
		// assert
		assertThat(selectedPlayer.getCurrentSellToPotPrice()).isEqualTo(new BigDecimal("6.9"));
	}
	
	@Test
	public void testUpdateCurrentSellToPotPrice_PriceDecrease() {
		// arrange
		final SelectedPlayer selectedPlayer = SelectedPlayerTestDataBuilder.aSelectedPlayer(Position.GOALKEEPER).build();
		
		final BigDecimal newFplPrice = selectedPlayer.getFplCostAtPurchase().subtract(PRICE_CHANGE);
		
		// act
		selectedPlayer.updateCurrentSellToPotPrice(newFplPrice);
		
		// assert
		assertThat(selectedPlayer.getCurrentSellToPotPrice()).isEqualTo(new BigDecimal("4.1"));
	}
}
