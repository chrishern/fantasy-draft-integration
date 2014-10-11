-- Get all bids made in a given transfer window
SELECT Bid.team_id, Player.forname, Player.surname, Bid.amount
FROM TransferWindow_Bid, Bid, Player
WHERE TransferWindow_Bid.auctionRoundBids_id = Bid.id AND TransferWindow_sequenceNumber = 3 AND Bid.player_id = Player.id
ORDER BY Bid.team_id;


-- Get number of STILL_SELECTED players for a team
SELECT count(*) 
FROM SelectedPlayer, Team_SelectedPlayer
WHERE Team_SelectedPlayer.Team_id = 8 AND Team_SelectedPlayer.selectedPlayers_id = SelectedPlayer.id AND SelectedPlayer.stillSelected = 'STILL_SELECTED';


-- Get all details of STILL_SELECTED players for a team
SELECT SelectedPlayer.id, Player.forename, Player.surname, SelectedPlayer.selectionStatus
FROM SelectedPlayer, Team_SelectedPlayer, Player
WHERE Team_SelectedPlayer.Team_id = 2 
		AND Team_SelectedPlayer.selectedPlayers_id = SelectedPlayer.id 
		AND SelectedPlayer.stillSelected = 'STILL_SELECTED'
		AND Player.id = SelectedPlayer.player_id;
		

DELETE FROM `Team_SelectedPlayer` WHERE `selectedPlayers_id` = 176;

UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 7;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 8;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 9;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 10;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 80;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 81;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 82;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 83;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = 175;
UPDATE SelectedPlayer SET selectionStatus = 'CAPTAIN' WHERE id = 12;
UPDATE SelectedPlayer SET selectionStatus = 'VICE_CAPTAIN' WHERE id = 6;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_1' WHERE id = 160;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_2' WHERE id = 79;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_3' WHERE id = 78;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_4' WHERE id = 85;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_5' WHERE id = 107;