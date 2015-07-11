-- Get all bids made in a given transfer window
SELECT Bid.team_id, Player.forename, Player.surname, Bid.amount
FROM TransferWindow_Bid, Bid, Player
WHERE TransferWindow_Bid.auctionRoundBids_id = Bid.id AND TransferWindow_sequenceNumber = 10 AND Bid.player_id = Player.id
ORDER BY Bid.team_id;


-- Get number of STILL_SELECTED players for a team
SELECT count(*) 
FROM SelectedPlayer, Team_SelectedPlayer
WHERE Team_SelectedPlayer.Team_id = 8 AND Team_SelectedPlayer.selectedPlayers_id = SelectedPlayer.id AND SelectedPlayer.stillSelected = 'STILL_SELECTED';

SELECT Team_SelectedPlayer.Team_id, count(*) 
FROM SelectedPlayer, Team_SelectedPlayer
WHERE Team_SelectedPlayer.selectedPlayers_id = SelectedPlayer.id AND SelectedPlayer.stillSelected = 'STILL_SELECTED';
GROUP BY Team_SelectedPlayer.Team_id;


-- Get all details of STILL_SELECTED players for a team
SELECT SelectedPlayer.id, Player.forename, Player.surname, SelectedPlayer.selectionStatus
FROM SelectedPlayer, Team_SelectedPlayer, Player
WHERE Team_SelectedPlayer.Team_id = 5
		AND Team_SelectedPlayer.selectedPlayers_id = SelectedPlayer.id 
		AND SelectedPlayer.stillSelected = 'STILL_SELECTED'
		AND Player.id = SelectedPlayer.player_id;

-- Get details of all players a team has ever had
SELECT SelectedPlayer.id, Player.forename, Player.surname, SelectedPlayer.stillSelected, SelectedPlayer.currentSellToPotPrice, SelectedPlayer.selectionStatus, SelectedPlayer.fplCostAtPurchase
FROM SelectedPlayer, Team_SelectedPlayer, Player
WHERE Team_SelectedPlayer.Team_id = 6 
		AND Team_SelectedPlayer.selectedPlayers_id = SelectedPlayer.id 
		AND Player.id = SelectedPlayer.player_id;

UPDATE SelectedPlayer SET cost = 3.8 WHERE id = 201;
UPDATE SelectedPlayer SET cost = 6 WHERE id = 196;
UPDATE SelectedPlayer SET cost = 5.7 WHERE id = 197;
UPDATE SelectedPlayer SET cost = 2 WHERE id = 200;
UPDATE SelectedPlayer SET selectionStatus = NULL WHERE id = 219;
		
DELETE FROM `Team_SelectedPlayer` WHERE `selectedPlayers_id` = 176;

UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 15;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 20;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 161;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 205;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 222;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 224;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 235;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 236;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 237;
UPDATE SelectedPlayer SET selectionStatus = 'CAPTAIN' WHERE id = 13;
UPDATE SelectedPlayer SET selectionStatus = 'VICE_CAPTAIN' WHERE id = 162;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_1' WHERE id = 22;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_2' WHERE id = 216;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_3' WHERE id = 17;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_4' WHERE id = 204;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_5' WHERE id = 14;


-- Get the names of the players involved in transfers in the current transfer window
SELECT Player.forename, Player.surname, Transfer.buyingTeam_id, Transfer.sellingTeam_id, Transfer.status
FROM Player, TransferWindow, TransferWindow_Transfer, Transfer, TransferredPlayer
WHERE TransferWindow_Transfer.TransferWindow_sequenceNumber = TransferWindow.sequenceNumber
	AND TransferWindow.status = 'OPEN'
	AND TransferWindow_Transfer.transfers_id = Transfer.id
	AND Transfer.id = TransferredPlayer.id
	AND TransferredPlayer.player_id = Player.id;
	
	
-- Remove an erroneously submitted transfer: transfers_id = 90
DELETE FROM TransferWindow_Transfer WHERE transfers_id = 90;
DELETE FROM Transfer_ExchangedPlayer WHERE Transfer_id = 90;
DELETE FROM ExchangedPlayer WHERE id = 6;
DELETE FROM ExchangedPlayer WHERE id = 7;
DELETE FROM Transfer_TransferredPlayer WHERE Transfer_id 90;
DELETE FROM TransferredPlayer WHERE id = 90;
DELETE FROM Transfer WHERE id = 90;



-- Get deals in current transfer window
SELECT *
FROM Transfer, TransferWindow_Transfer
WHERE TransferWindow_Transfer.transferWindow_sequenceNumber = 9
	AND TransferWindow_Transfer.transfers_id = Transfer.id;