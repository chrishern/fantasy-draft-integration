SELECT sp.id, p.forename, p.surname, sp.startingTeamStatus
FROM Player p, SelectedPlayer sp, Team_SelectedPlayer tsp
WHERE p.id = sp.player_id
	AND sp.id = tsp.selectedPlayers_id
	AND tsp.Team_id = 4
ORDER BY sp.id;


UPDATE SelectedPlayer SET startingTeamStatus = 'CAPTAIN' WHERE id = 83;
UPDATE SelectedPlayer SET startingTeamStatus = 'VICE_CAPTAIN' WHERE id = 81;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 84;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 85;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 87;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 123;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 124;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 125;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 145;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 147;
UPDATE SelectedPlayer SET startingTeamStatus = 'PICKED' WHERE id = 174;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_1' WHERE id = 86;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_2' WHERE id = 82;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_3' WHERE id = 146;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_4' WHERE id = 88;
UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_5' WHERE id = 148;

UPDATE SelectedPlayer SET startingTeamStatus = 'SUB_3' WHERE id = 141;