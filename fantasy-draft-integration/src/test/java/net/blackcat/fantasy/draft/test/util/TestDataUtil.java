/**
 * 
 */
package net.blackcat.fantasy.draft.test.util;

import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.player.Player;
import net.blackcat.fantasy.draft.player.types.Position;

/**
 * Utilities for creating test data.
 * 
 * @author Chris
 *
 */
public final class TestDataUtil {

	public static final long PLAYER_1_ID = 1;
	public static final int PLAYER_1_POINTS = 54;
	public static final String PLAYER_1_SURNAME = "Player";
	public static final String PLAYER_1_FORENAME = "Test";
	public static final long PLAYER_2_ID = 2;
	public static final int PLAYER_2_POINTS = 89;
	public static final String PLAYER_2_SURNAME = "Player2";
	public static final String PLAYER_2_FORENAME = "Test2";
	public static final String TEST_TEAM = "Test Team";
	
	/**
	 * Create a {@link Player} object.
	 * 
	 * @param playerNumber Whether you want player 1 or 2.
	 * @return {@link Player} object.
	 */
	public static Player createModelPlayer(final int playerNumber) {
		if (playerNumber == 1) {
			return new Player(PLAYER_1_ID, PLAYER_1_FORENAME, PLAYER_1_SURNAME, TEST_TEAM, Position.DEFENDER, PLAYER_1_POINTS);
		}
		
		return new Player(PLAYER_2_ID, PLAYER_2_FORENAME, PLAYER_2_SURNAME, TEST_TEAM, Position.STRIKER, PLAYER_2_POINTS);
	}
	
	/**
	 * Create a {@link PlayerEntity} object.
	 * 
	 * @param playerNumber Whether you want player 1 or 2.
	 * @return {@link PlayerEntity} object.
	 */
	public static PlayerEntity createEntityPlayer(final int playerNumber) {
		if (playerNumber == 1) {
			return new PlayerEntity(PLAYER_1_ID, PLAYER_1_FORENAME, PLAYER_1_SURNAME, TEST_TEAM, Position.DEFENDER, true, PLAYER_1_POINTS);
		}
		
		return new PlayerEntity(PLAYER_2_ID, PLAYER_2_FORENAME, PLAYER_2_SURNAME, TEST_TEAM, Position.STRIKER, true, PLAYER_2_POINTS);
	}
}
