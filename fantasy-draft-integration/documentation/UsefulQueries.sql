SELECT sp.id, p.forename, p.surname, sp.startingTeamStatus
FROM Player p, SelectedPlayer sp, Team_SelectedPlayer tsp
WHERE p.id = sp.player_id
	AND sp.id = tsp.selectedPlayers_id
	AND tsp.Team_id = 3
	AND sp.selectedStatus = 'STILL_SELECTED'
ORDER BY sp.id;


UPDATE SelectedPlayer SET startingTeamStatus = 'CAPTAIN' WHERE id = 106;
UPDATE SelectedPlayer SET startingTeamStatus = 'VICE_CAPTAIN' WHERE id = 103;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 1;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 3;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 104;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 161;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 203;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 206;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 229;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 254;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 262;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_1' WHERE id = 273;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_2' WHERE id = 274;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_3' WHERE id = 283;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_4' WHERE id = 157;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_5' WHERE id = 202;