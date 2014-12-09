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

-- Get details of all players a team has ever had
SELECT SelectedPlayer.id, Player.forename, Player.surname, SelectedPlayer.stillSelected
FROM SelectedPlayer, Team_SelectedPlayer, Player
WHERE Team_SelectedPlayer.Team_id = 2 
		AND Team_SelectedPlayer.selectedPlayers_id = SelectedPlayer.id 
		AND Player.id = SelectedPlayer.player_id;		

DELETE FROM `Team_SelectedPlayer` WHERE `selectedPlayers_id` = 176;

UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'PICKED' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'CAPTAIN' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'VICE_CAPTAIN' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_1' WHERE id = ;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_2' WHERE id = 146;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_3' WHERE id = 131;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_4' WHERE id = 71;
UPDATE SelectedPlayer SET selectionStatus = 'SUB_5' WHERE id = ;


-- Get the names of the players involved in transfers in the current transfer window
SELECT Player.forename, Player.surname, Transfer.buyingTeam_id, Transfer.sellingTeam_id, Transfer.status
FROM Player, TransferWindow, TransferWindow_Transfer, Transfer, TransferredPlayer
WHERE TransferWindow_Transfer.TransferWindow_sequenceNumber = TransferWindow.sequenceNumber
	AND TransferWindow.status = 'OPEN'
	AND TransferWindow_Transfer.transfers_id = Transfer.id
	AND Transfer.id = TransferredPlayer.id
	AND TransferredPlayer.player_id = Player.id;