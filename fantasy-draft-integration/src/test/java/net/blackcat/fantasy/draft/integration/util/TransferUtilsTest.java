/**
 * 
 */
package net.blackcat.fantasy.draft.integration.util;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import net.blackcat.fantasy.draft.integration.entity.ExchangedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.PlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferEntity;
import net.blackcat.fantasy.draft.integration.entity.TransferredPlayerEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationExceptionCode;
import net.blackcat.fantasy.draft.integration.test.util.CustomIntegrationExceptionMatcher;
import net.blackcat.fantasy.draft.integration.test.util.TestDataUtil;
import net.blackcat.fantasy.draft.transfer.Transfer;
import net.blackcat.fantasy.draft.transfer.types.TransferType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for TransferUtils
 * 
 * @author Chris
 *
 */
public class TransferUtilsTest {

	@Rule
	public ExpectedException thrownException = ExpectedException.none();
	
	private static final BigDecimal THREE_MILLION_TRANSFER_AMOUNT = new BigDecimal("3.0");
	private static final BigDecimal FIVE_MILLION_TRANSFER_AMOUNT = new BigDecimal("5.0");
	
	private static final int TEAM_1 = 1;
	private static final int TEAM_2 = 2;

	private TeamEntity buyingTeamEntity;
	private TeamEntity sellingTeamEntity;
	
	private PlayerEntity player1;
	private PlayerEntity player2;
	private PlayerEntity player3;
	
	@Before
	public void setup() {
		buyingTeamEntity = createTeamEntity(TEAM_2);
		sellingTeamEntity = createTeamEntity(TEAM_1);
		
		player1 = TestDataUtil.createEntityPlayer(TestDataUtil.PLAYER_1_ID);
		player2 = TestDataUtil.createEntityPlayer(TestDataUtil.PLAYER_2_ID);
		player3 = TestDataUtil.createEntityPlayer(TestDataUtil.PLAYER_3_ID);
	}
	
	@Test
	public void testGetEquivalentTransfer_Success_SinglePlayer_PriceOnly() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(TEAM_2, TEAM_1, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		
		// act
		final TransferEntity equivalentTransfer = TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		assertThat(equivalentTransfer).isNotNull();
		assertThat(equivalentTransfer).isEqualTo(transferEntity);
	}
	
	@Test
	public void testGetEquivalentTransfer_Success_MultiplePlayers_PriceOnly() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(
				TEAM_2, TEAM_1, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID, TestDataUtil.PLAYER_2_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1), new TransferredPlayerEntity(player2)));
		
		// act
		final TransferEntity equivalentTransfer = TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		assertThat(equivalentTransfer).isNotNull();
		assertThat(equivalentTransfer).isEqualTo(transferEntity);
	}

	@Test
	public void testGetEquivalentTransfer_Success_SinglePlayer_ExchangedPlayer() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(
				TEAM_2, TEAM_1, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), Arrays.asList(TestDataUtil.PLAYER_2_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		transferEntity.setExchangedPlayers(Arrays.asList(new ExchangedPlayerEntity(player2)));
		
		// act
		final TransferEntity equivalentTransfer = TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		assertThat(equivalentTransfer).isNotNull();
		assertThat(equivalentTransfer).isEqualTo(transferEntity);
	}
	
	@Test
	public void testGetEquivalentTransfer_Success_SinglePlayer_ExchangedPlayers() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(TEAM_2, TEAM_1, TransferType.BUY, 
				Arrays.asList(TestDataUtil.PLAYER_1_ID), Arrays.asList(TestDataUtil.PLAYER_2_ID, TestDataUtil.PLAYER_3_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		transferEntity.setExchangedPlayers(Arrays.asList(new ExchangedPlayerEntity(player2), new ExchangedPlayerEntity(player3)));
		
		// act
		final TransferEntity equivalentTransfer = TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		assertThat(equivalentTransfer).isNotNull();
		assertThat(equivalentTransfer).isEqualTo(transferEntity);
	}
	
	@Test
	public void testGetEquivalentTransfer_Success_AlsoANonMatchingTransferBetweenTeams() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(TEAM_2, TEAM_1, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity nonMatchingTransferEntity = new TransferEntity();
		nonMatchingTransferEntity.setBuyingTeam(buyingTeamEntity);
		nonMatchingTransferEntity.setSellingTeam(sellingTeamEntity);
		nonMatchingTransferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		nonMatchingTransferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player2)));
		
		final TransferEntity matchingTransferEntity = new TransferEntity();
		matchingTransferEntity.setBuyingTeam(buyingTeamEntity);
		matchingTransferEntity.setSellingTeam(sellingTeamEntity);
		matchingTransferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		matchingTransferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		
		// act
		final TransferEntity equivalentTransfer = TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(matchingTransferEntity));
		
		// assert
		assertThat(equivalentTransfer).isNotNull();
		assertThat(equivalentTransfer).isEqualTo(matchingTransferEntity);
	}
	
	@Test
	public void testGetEquivalentTransfer_NotFound_TeamsDontMatch() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(TEAM_1, TEAM_2, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MATCHING_TRANSFER_NOT_FOUND));
		
		// act
		TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetEquivalentTransfer_NotFound_PlayersDontMatchSinglePlayer() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(TEAM_2, TEAM_1, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_2_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MATCHING_TRANSFER_NOT_FOUND));
		
		// act
		TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetEquivalentTransfer_NotFound_PlayerDoesntExistInSeller() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(
				TEAM_2, TEAM_1, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID, TestDataUtil.PLAYER_2_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MATCHING_TRANSFER_NOT_FOUND));
		
		// act
		TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetEquivalentTransfer_NotFound_PlayerDoesntExistInBuyer() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(
				TEAM_2, TEAM_1, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(THREE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1), new TransferredPlayerEntity(player2)));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MATCHING_TRANSFER_NOT_FOUND));
		
		// act
		TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetEquivalentTransfer_NotFound_PriceDoesntMatch() throws Exception{
		// arrange
		final Transfer transfer = new Transfer(
				TEAM_2, TEAM_1, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(FIVE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MATCHING_TRANSFER_NOT_FOUND));
		
		// act
		TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetEquivalentTransfer_NotFound_ExchangedPlayersDontMatchSinglePlayer() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(
				TEAM_2, TEAM_1, TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), Arrays.asList(TestDataUtil.PLAYER_2_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(FIVE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		transferEntity.setExchangedPlayers(Arrays.asList(new ExchangedPlayerEntity(player3)));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MATCHING_TRANSFER_NOT_FOUND));
		
		// act
		TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetEquivalentTransfer_NotFound_ExchangedPlayerDoesntExistInSeller() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(TEAM_2, TEAM_1, 
				TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), Arrays.asList(TestDataUtil.PLAYER_2_ID, TestDataUtil.PLAYER_3_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(FIVE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		transferEntity.setExchangedPlayers(Arrays.asList(new ExchangedPlayerEntity(player3)));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MATCHING_TRANSFER_NOT_FOUND));
		
		// act
		TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		Assert.fail("Exception expected");
	}
	
	@Test
	public void testGetEquivalentTransfer_NotFound_ExchangedPlayerDoesntExistInBuyer() throws Exception {
		// arrange
		final Transfer transfer = new Transfer(TEAM_2, TEAM_1, 
				TransferType.BUY, Arrays.asList(TestDataUtil.PLAYER_1_ID), Arrays.asList(TestDataUtil.PLAYER_2_ID), THREE_MILLION_TRANSFER_AMOUNT);

		final TransferEntity transferEntity = new TransferEntity();
		transferEntity.setBuyingTeam(buyingTeamEntity);
		transferEntity.setSellingTeam(sellingTeamEntity);
		transferEntity.setAmount(FIVE_MILLION_TRANSFER_AMOUNT);
		transferEntity.setPlayers(Arrays.asList(new TransferredPlayerEntity(player1)));
		transferEntity.setExchangedPlayers(Arrays.asList(new ExchangedPlayerEntity(player2), new ExchangedPlayerEntity(player3)));
		
		thrownException.expect(FantasyDraftIntegrationException.class);
		thrownException.expect(CustomIntegrationExceptionMatcher.hasCode(FantasyDraftIntegrationExceptionCode.MATCHING_TRANSFER_NOT_FOUND));
		
		// act
		TransferUtils.getEquivalentTransfer(transfer, Arrays.asList(transferEntity));
		
		// assert
		Assert.fail("Exception expected");
	}
	
	private TeamEntity createTeamEntity(final int teamId) {
		final TeamEntity team = mock(TeamEntity.class);
		
		when(team.getId()).thenReturn(teamId);
		
		return team;
	}
}
