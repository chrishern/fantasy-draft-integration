/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for the {@link BidEntity}.
 * 
 * @author Chris
 *
 */
public class BidEntityTest {

	@Test
	public void testCompareTo() {
		// arrange
		final List<BidEntity> listToSort = new ArrayList<BidEntity>();
		
		final BidEntity bid1 = new BidEntity();
		bid1.setAmount(new BigDecimal("1.5"));
		listToSort.add(bid1);
		
		final BidEntity bid2 = new BidEntity();
		bid2.setAmount(new BigDecimal("0.5"));
		listToSort.add(bid2);
		
		final BidEntity bid3 = new BidEntity();
		bid3.setAmount(new BigDecimal("1"));
		listToSort.add(bid3);
		
		final BidEntity bid4 = new BidEntity();
		bid4.setAmount(new BigDecimal("3.5"));
		listToSort.add(bid4);
		
		// act
		Collections.sort(listToSort);
		
		// assert
		assertThat(listToSort.get(0).getAmount().doubleValue()).isEqualTo(3.5d);
		assertThat(listToSort.get(1).getAmount().doubleValue()).isEqualTo(1.5d);
		assertThat(listToSort.get(2).getAmount().doubleValue()).isEqualTo(1d);
		assertThat(listToSort.get(3).getAmount().doubleValue()).isEqualTo(0.5d);
	}
}
