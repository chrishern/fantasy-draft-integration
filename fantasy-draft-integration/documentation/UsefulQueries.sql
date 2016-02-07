SELECT sp.id, p.forename, p.surname, sp.startingTeamStatus
FROM Player p, SelectedPlayer sp, Team_SelectedPlayer tsp
WHERE p.id = sp.player_id
	AND sp.id = tsp.selectedPlayers_id
	AND tsp.Team_id = 11
	AND sp.selectedStatus = 'STILL_SELECTED'
ORDER BY sp.id;


UPDATE SelectedPlayer SET startingTeamStatus = 'CAPTAIN' WHERE id = 77;
UPDATE SelectedPlayer SET startingTeamStatus = 'VICE_CAPTAIN' WHERE id = 251;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 69;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 71;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 221;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 222;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 223;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 253;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 263;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 287;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 310;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_1' WHERE id = 314;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_2' WHERE id = 308;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_3' WHERE id = 224;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_4' WHERE id = 313;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_5' WHERE id = 309;

SELECT DISTINCT b.team_id
FROM Bid b, AuctionPhase_Bid a
WHERE b.id = a.bids_id
	AND a.AuctionPhase_id = 14
ORDER BY b.team_id;
