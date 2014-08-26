package net.blackcat.fantasy.draft.integration.upgrade;

import java.util.List;

import net.blackcat.fantasy.draft.integration.data.service.LeagueDataService;
import net.blackcat.fantasy.draft.integration.data.service.TeamDataService;
import net.blackcat.fantasy.draft.integration.entity.GameweekScoreEntity;
import net.blackcat.fantasy.draft.integration.entity.SelectedPlayerEntity;
import net.blackcat.fantasy.draft.integration.entity.TeamEntity;
import net.blackcat.fantasy.draft.integration.exception.FantasyDraftIntegrationException;
import net.blackcat.fantasy.draft.player.types.SelectedPlayerStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Stand alone class responsible for manually populating the first week draft scores.
 * 
 * @author Chris
 *
 */
@Transactional
@Service(value = "populateFirstWeekScores")
public class PopulateFirstWeekScores {

	@Autowired
	@Qualifier(value = "teamDataServiceJpa")
	private TeamDataService teamDataService;
	
	@Autowired
	@Qualifier(value = "leagueDataServiceJpa")
	private LeagueDataService leagueDataService;
	
	public void populateAllTeams() throws FantasyDraftIntegrationException {
		final List<TeamEntity> teamsInLeague = leagueDataService.getLeague(1).getTeams();
		
		populateTeamOne(teamsInLeague);
		populateTeamTwo(teamsInLeague);
		populateTeamThree(teamsInLeague);
		populateTeamFour(teamsInLeague);
		populateTeamFive(teamsInLeague);
		populateTeamSix(teamsInLeague);
		populateTeamSeven(teamsInLeague);
		populateTeamEight(teamsInLeague);
		populateTeamNine(teamsInLeague);
	}
	
	private void populateTeamOne(final List<TeamEntity> teamsInLeague) {
		final TeamEntity team = getTeam(1, teamsInLeague);
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		final SelectedPlayerEntity cech = getSelectedPlayer(87, selectedPlayers);
		cech.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity cechScore = new GameweekScoreEntity(1, 0);
		cech.addGameweekScore(cechScore);
		
		final SelectedPlayerEntity zaba = getSelectedPlayer(26, selectedPlayers);
		zaba.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity zabaScore = new GameweekScoreEntity(1, 0);
		zaba.addGameweekScore(zabaScore);
		
		final SelectedPlayerEntity vert = getSelectedPlayer(27, selectedPlayers);
		vert.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity vertScore = new GameweekScoreEntity(1, 0);
		vert.addGameweekScore(vertScore);
		
		final SelectedPlayerEntity baines = getSelectedPlayer(88, selectedPlayers);
		baines.setSelectionStatus(SelectedPlayerStatus.PICKED);
		baines.setPointsScored(1);
		final GameweekScoreEntity bainesScore = new GameweekScoreEntity(1, 1);
		baines.addGameweekScore(bainesScore);
		
		final SelectedPlayerEntity sagna = getSelectedPlayer(135, selectedPlayers);
		sagna.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity sagnaScore = new GameweekScoreEntity(1, 0);
		sagna.addGameweekScore(sagnaScore);
		
		final SelectedPlayerEntity herr = getSelectedPlayer(28, selectedPlayers);
		herr.setSelectionStatus(SelectedPlayerStatus.PICKED);
		herr.setPointsScored(2);
		final GameweekScoreEntity herrScore = new GameweekScoreEntity(1, 2);
		herr.addGameweekScore(herrScore);
		
		final SelectedPlayerEntity nolan = getSelectedPlayer(111, selectedPlayers);
		nolan.setSelectionStatus(SelectedPlayerStatus.PICKED);
		nolan.setPointsScored(2);
		final GameweekScoreEntity nolanScore = new GameweekScoreEntity(1, 2);
		nolan.addGameweekScore(nolanScore);
		
		final SelectedPlayerEntity walcott = getSelectedPlayer(89, selectedPlayers);
		walcott.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity walcottScore = new GameweekScoreEntity(1, 0);
		walcott.addGameweekScore(walcottScore);
		
		final SelectedPlayerEntity lall = getSelectedPlayer(90, selectedPlayers);
		lall.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity lallScore = new GameweekScoreEntity(1, 0);
		lall.addGameweekScore(lallScore);
		
		final SelectedPlayerEntity aguero = getSelectedPlayer(29, selectedPlayers);
		aguero.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		aguero.setPointsScored(12);
		final GameweekScoreEntity agueroScore = new GameweekScoreEntity(1, 12);
		aguero.addGameweekScore(agueroScore);
		
		final SelectedPlayerEntity luk = getSelectedPlayer(91, selectedPlayers);
		luk.setSelectionStatus(SelectedPlayerStatus.VICE_CAPTAIN);
		luk.setPointsScored(2);
		final GameweekScoreEntity lukScore = new GameweekScoreEntity(1, 2);
		luk.addGameweekScore(lukScore);
		
		final SelectedPlayerEntity court = getSelectedPlayer(25, selectedPlayers);
		court.setSelectionStatus(SelectedPlayerStatus.SUB_1);
		court.setPointsScored(2);
		final GameweekScoreEntity courtScore = new GameweekScoreEntity(1, 2);
		court.addGameweekScore(courtScore);
		
		final SelectedPlayerEntity drog = getSelectedPlayer(92, selectedPlayers);
		drog.setSelectionStatus(SelectedPlayerStatus.SUB_2);
		drog.setPointsScored(1);
		final GameweekScoreEntity drogScore = new GameweekScoreEntity(1, 1);
		drog.addGameweekScore(drogScore);
		
		final SelectedPlayerEntity hend = getSelectedPlayer(136, selectedPlayers);
		hend.setSelectionStatus(SelectedPlayerStatus.SUB_3);
		hend.setPointsScored(5);
		final GameweekScoreEntity hendScore = new GameweekScoreEntity(1, 5);
		hend.addGameweekScore(hendScore);
		
		final SelectedPlayerEntity camp = getSelectedPlayer(144, selectedPlayers);
		camp.setSelectionStatus(SelectedPlayerStatus.SUB_4);
		final GameweekScoreEntity campScore = new GameweekScoreEntity(1, 0);
		camp.addGameweekScore(campScore);
		
		final SelectedPlayerEntity gibs = getSelectedPlayer(110, selectedPlayers);
		gibs.setSelectionStatus(SelectedPlayerStatus.SUB_5);
		gibs.setPointsScored(1);
		final GameweekScoreEntity gibsScore = new GameweekScoreEntity(1, 1);
		gibs.addGameweekScore(gibsScore);
		
		teamDataService.updateTeam(team);
	}
	
	private void populateTeamTwo(final List<TeamEntity> teamsInLeague) {
		final TeamEntity team = getTeam(2, teamsInLeague);
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		final SelectedPlayerEntity cech = getSelectedPlayer(45, selectedPlayers);
		cech.setSelectionStatus(SelectedPlayerStatus.PICKED);
		cech.setPointsScored(2);
		final GameweekScoreEntity cechScore = new GameweekScoreEntity(1, 2);
		cech.addGameweekScore(cechScore);
		
		final SelectedPlayerEntity zaba = getSelectedPlayer(46, selectedPlayers);
		zaba.setPointsScored(2);
		zaba.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity zabaScore = new GameweekScoreEntity(1, 2);
		zaba.addGameweekScore(zabaScore);
		
		final SelectedPlayerEntity vert = getSelectedPlayer(47, selectedPlayers);
		vert.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity vertScore = new GameweekScoreEntity(1, 0);
		vert.addGameweekScore(vertScore);
		
		final SelectedPlayerEntity baines = getSelectedPlayer(48, selectedPlayers);
		baines.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity bainesScore = new GameweekScoreEntity(1, 0);
		baines.addGameweekScore(bainesScore);
		
		final SelectedPlayerEntity sagna = getSelectedPlayer(99, selectedPlayers);
		sagna.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity sagnaScore = new GameweekScoreEntity(1, 0);
		sagna.addGameweekScore(sagnaScore);
		
		final SelectedPlayerEntity herr = getSelectedPlayer(50, selectedPlayers);
		herr.setSelectionStatus(SelectedPlayerStatus.PICKED);
		herr.setPointsScored(5);
		final GameweekScoreEntity herrScore = new GameweekScoreEntity(1, 5);
		herr.addGameweekScore(herrScore);
		
		final SelectedPlayerEntity nolan = getSelectedPlayer(51, selectedPlayers);
		nolan.setSelectionStatus(SelectedPlayerStatus.VICE_CAPTAIN);
		nolan.setPointsScored(2);
		final GameweekScoreEntity nolanScore = new GameweekScoreEntity(1, 2);
		nolan.addGameweekScore(nolanScore);
		
		final SelectedPlayerEntity walcott = getSelectedPlayer(100, selectedPlayers);
		walcott.setSelectionStatus(SelectedPlayerStatus.PICKED);
		walcott.setPointsScored(2);
		final GameweekScoreEntity walcottScore = new GameweekScoreEntity(1, 2);
		walcott.addGameweekScore(walcottScore);
		
		final SelectedPlayerEntity lall = getSelectedPlayer(101, selectedPlayers);
		lall.setSelectionStatus(SelectedPlayerStatus.PICKED);
		lall.setPointsScored(1);
		final GameweekScoreEntity lallScore = new GameweekScoreEntity(1, 1);
		lall.addGameweekScore(lallScore);
		
		final SelectedPlayerEntity aguero = getSelectedPlayer(102, selectedPlayers);
		aguero.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		aguero.setPointsScored(14);
		final GameweekScoreEntity agueroScore = new GameweekScoreEntity(1, 14);
		aguero.addGameweekScore(agueroScore);
		
		final SelectedPlayerEntity luk = getSelectedPlayer(52, selectedPlayers);
		luk.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity lukScore = new GameweekScoreEntity(1, 0);
		luk.addGameweekScore(lukScore);
		
		final SelectedPlayerEntity court = getSelectedPlayer(53, selectedPlayers);
		court.setSelectionStatus(SelectedPlayerStatus.SUB_1);
		final GameweekScoreEntity courtScore = new GameweekScoreEntity(1, 0);
		court.addGameweekScore(courtScore);
		
		final SelectedPlayerEntity drog = getSelectedPlayer(121, selectedPlayers);
		drog.setSelectionStatus(SelectedPlayerStatus.SUB_2);
		final GameweekScoreEntity drogScore = new GameweekScoreEntity(1, 0);
		drog.addGameweekScore(drogScore);
		
		final SelectedPlayerEntity hend = getSelectedPlayer(49, selectedPlayers);
		hend.setSelectionStatus(SelectedPlayerStatus.SUB_3);
		hend.setPointsScored(1);
		final GameweekScoreEntity hendScore = new GameweekScoreEntity(1, 1);
		hend.addGameweekScore(hendScore);
		
		final SelectedPlayerEntity camp = getSelectedPlayer(122, selectedPlayers);
		camp.setSelectionStatus(SelectedPlayerStatus.SUB_4);
		final GameweekScoreEntity campScore = new GameweekScoreEntity(1, 0);
		camp.addGameweekScore(campScore);
		
		final SelectedPlayerEntity gibs = getSelectedPlayer(44, selectedPlayers);
		gibs.setSelectionStatus(SelectedPlayerStatus.SUB_5);
		final GameweekScoreEntity gibsScore = new GameweekScoreEntity(1, 0);
		gibs.addGameweekScore(gibsScore);
		
		teamDataService.updateTeam(team);
	}
	
	private void populateTeamThree(final List<TeamEntity> teamsInLeague) {
		final TeamEntity team = getTeam(3, teamsInLeague);
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		final SelectedPlayerEntity cech = getSelectedPlayer(38, selectedPlayers);
		cech.setSelectionStatus(SelectedPlayerStatus.PICKED);
		cech.setPointsScored(1);
		final GameweekScoreEntity cechScore = new GameweekScoreEntity(1, 1);
		cech.addGameweekScore(cechScore);
		
		final SelectedPlayerEntity zaba = getSelectedPlayer(94, selectedPlayers);
		zaba.setSelectionStatus(SelectedPlayerStatus.PICKED);
		zaba.setPointsScored(1);
		final GameweekScoreEntity zabaScore = new GameweekScoreEntity(1, 1);
		zaba.addGameweekScore(zabaScore);
		
		final SelectedPlayerEntity vert = getSelectedPlayer(117, selectedPlayers);
		vert.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		vert.setPointsScored(4);
		final GameweekScoreEntity vertScore = new GameweekScoreEntity(1, 4);
		vert.addGameweekScore(vertScore);
		
		final SelectedPlayerEntity baines = getSelectedPlayer(39, selectedPlayers);
		baines.setSelectionStatus(SelectedPlayerStatus.PICKED);
		baines.setPointsScored(10);
		final GameweekScoreEntity bainesScore = new GameweekScoreEntity(1, 10);
		baines.addGameweekScore(bainesScore);
		
		final SelectedPlayerEntity sagna = getSelectedPlayer(137, selectedPlayers);
		sagna.setSelectionStatus(SelectedPlayerStatus.PICKED);
		sagna.setPointsScored(9);
		final GameweekScoreEntity sagnaScore = new GameweekScoreEntity(1, 9);
		sagna.addGameweekScore(sagnaScore);
		
		final SelectedPlayerEntity herr = getSelectedPlayer(40, selectedPlayers);
		herr.setSelectionStatus(SelectedPlayerStatus.PICKED);
		herr.setPointsScored(9);
		final GameweekScoreEntity herrScore = new GameweekScoreEntity(1, 9);
		herr.addGameweekScore(herrScore);
		
		final SelectedPlayerEntity nolan = getSelectedPlayer(41, selectedPlayers);
		nolan.setSelectionStatus(SelectedPlayerStatus.PICKED);
		nolan.setPointsScored(13);
		final GameweekScoreEntity nolanScore = new GameweekScoreEntity(1, 13);
		nolan.addGameweekScore(nolanScore);
		
		final SelectedPlayerEntity walcott = getSelectedPlayer(118, selectedPlayers);
		walcott.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity walcottScore = new GameweekScoreEntity(1, 0);
		walcott.addGameweekScore(walcottScore);
		
		final SelectedPlayerEntity lall = getSelectedPlayer(119, selectedPlayers);
		lall.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity lallScore = new GameweekScoreEntity(1, 0);
		lall.addGameweekScore(lallScore);
		
		final SelectedPlayerEntity aguero = getSelectedPlayer(42, selectedPlayers);
		aguero.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		aguero.setPointsScored(12);
		final GameweekScoreEntity agueroScore = new GameweekScoreEntity(1, 12);
		aguero.addGameweekScore(agueroScore);
		
		final SelectedPlayerEntity luk = getSelectedPlayer(43, selectedPlayers);
		luk.setSelectionStatus(SelectedPlayerStatus.PICKED);
		luk.setPointsScored(1);
		final GameweekScoreEntity lukScore = new GameweekScoreEntity(1, 1);
		luk.addGameweekScore(lukScore);
		
		final SelectedPlayerEntity court = getSelectedPlayer(95, selectedPlayers);
		court.setSelectionStatus(SelectedPlayerStatus.SUB_1);
		court.setPointsScored(5);
		final GameweekScoreEntity courtScore = new GameweekScoreEntity(1, 5);
		court.addGameweekScore(courtScore);
		
		final SelectedPlayerEntity drog = getSelectedPlayer(120, selectedPlayers);
		drog.setSelectionStatus(SelectedPlayerStatus.SUB_2);
		final GameweekScoreEntity drogScore = new GameweekScoreEntity(1, 0);
		drog.addGameweekScore(drogScore);
		
		final SelectedPlayerEntity hend = getSelectedPlayer(138, selectedPlayers);
		hend.setSelectionStatus(SelectedPlayerStatus.SUB_3);
		final GameweekScoreEntity hendScore = new GameweekScoreEntity(1, 0);
		hend.addGameweekScore(hendScore);
		
		final SelectedPlayerEntity camp = getSelectedPlayer(96, selectedPlayers);
		camp.setSelectionStatus(SelectedPlayerStatus.SUB_4);
		final GameweekScoreEntity campScore = new GameweekScoreEntity(1, 0);
		camp.addGameweekScore(campScore);
		
		final SelectedPlayerEntity gibs = getSelectedPlayer(93, selectedPlayers);
		gibs.setSelectionStatus(SelectedPlayerStatus.SUB_5);
		final GameweekScoreEntity gibsScore = new GameweekScoreEntity(1, 0);
		gibs.addGameweekScore(gibsScore);
		
		teamDataService.updateTeam(team);
	}
	
	private void populateTeamFour(final List<TeamEntity> teamsInLeague) {
		final TeamEntity team = getTeam(4, teamsInLeague);
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		final SelectedPlayerEntity cech = getSelectedPlayer(1, selectedPlayers);
		cech.setSelectionStatus(SelectedPlayerStatus.PICKED);
		cech.setPointsScored(3);
		final GameweekScoreEntity cechScore = new GameweekScoreEntity(1, 3);
		cech.addGameweekScore(cechScore);
		
		final SelectedPlayerEntity zaba = getSelectedPlayer(104, selectedPlayers);
		zaba.setSelectionStatus(SelectedPlayerStatus.PICKED);
		zaba.setPointsScored(4);
		final GameweekScoreEntity zabaScore = new GameweekScoreEntity(1, 4);
		zaba.addGameweekScore(zabaScore);
		
		final SelectedPlayerEntity vert = getSelectedPlayer(2, selectedPlayers);
		vert.setSelectionStatus(SelectedPlayerStatus.PICKED);
		vert.setPointsScored(1);
		final GameweekScoreEntity vertScore = new GameweekScoreEntity(1, 1);
		vert.addGameweekScore(vertScore);
		
		final SelectedPlayerEntity baines = getSelectedPlayer(73, selectedPlayers);
		baines.setSelectionStatus(SelectedPlayerStatus.PICKED);
		baines.setPointsScored(2);
		final GameweekScoreEntity bainesScore = new GameweekScoreEntity(1, 2);
		baines.addGameweekScore(bainesScore);
		
		final SelectedPlayerEntity sagna = getSelectedPlayer(3, selectedPlayers);
		sagna.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		sagna.setPointsScored(4);
		final GameweekScoreEntity sagnaScore = new GameweekScoreEntity(1, 4);
		sagna.addGameweekScore(sagnaScore);
		
		final SelectedPlayerEntity herr = getSelectedPlayer(4, selectedPlayers);
		herr.setSelectionStatus(SelectedPlayerStatus.VICE_CAPTAIN);
		herr.setPointsScored(3);
		final GameweekScoreEntity herrScore = new GameweekScoreEntity(1, 3);
		herr.addGameweekScore(herrScore);
		
		final SelectedPlayerEntity nolan = getSelectedPlayer(74, selectedPlayers);
		nolan.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity nolanScore = new GameweekScoreEntity(1, 0);
		nolan.addGameweekScore(nolanScore);
		
		final SelectedPlayerEntity walcott = getSelectedPlayer(75, selectedPlayers);
		walcott.setSelectionStatus(SelectedPlayerStatus.PICKED);
		walcott.setPointsScored(2);
		final GameweekScoreEntity walcottScore = new GameweekScoreEntity(1, 2);
		walcott.addGameweekScore(walcottScore);
		
		final SelectedPlayerEntity lall = getSelectedPlayer(5, selectedPlayers);
		lall.setSelectionStatus(SelectedPlayerStatus.PICKED);
		lall.setPointsScored(2);
		final GameweekScoreEntity lallScore = new GameweekScoreEntity(1, 2);
		lall.addGameweekScore(lallScore);
		
		final SelectedPlayerEntity aguero = getSelectedPlayer(105, selectedPlayers);
		aguero.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		final GameweekScoreEntity agueroScore = new GameweekScoreEntity(1, 0);
		aguero.addGameweekScore(agueroScore);
		
		final SelectedPlayerEntity luk = getSelectedPlayer(106, selectedPlayers);
		luk.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity lukScore = new GameweekScoreEntity(1, 0);
		luk.addGameweekScore(lukScore);
		
		final SelectedPlayerEntity court = getSelectedPlayer(76, selectedPlayers);
		court.setSelectionStatus(SelectedPlayerStatus.SUB_1);
		court.setPointsScored(13);
		final GameweekScoreEntity courtScore = new GameweekScoreEntity(1, 13);
		court.addGameweekScore(courtScore);
		
		final SelectedPlayerEntity drog = getSelectedPlayer(133, selectedPlayers);
		drog.setSelectionStatus(SelectedPlayerStatus.SUB_2);
		final GameweekScoreEntity drogScore = new GameweekScoreEntity(1, 0);
		drog.addGameweekScore(drogScore);
		
		final SelectedPlayerEntity hend = getSelectedPlayer(143, selectedPlayers);
		hend.setSelectionStatus(SelectedPlayerStatus.SUB_3);
		hend.setPointsScored(2);
		final GameweekScoreEntity hendScore = new GameweekScoreEntity(1, 2);
		hend.addGameweekScore(hendScore);
		
		final SelectedPlayerEntity camp = getSelectedPlayer(77, selectedPlayers);
		camp.setSelectionStatus(SelectedPlayerStatus.SUB_4);
		camp.setPointsScored(2);
		final GameweekScoreEntity campScore = new GameweekScoreEntity(1, 2);
		camp.addGameweekScore(campScore);
		
		final SelectedPlayerEntity gibs = getSelectedPlayer(72, selectedPlayers);
		gibs.setSelectionStatus(SelectedPlayerStatus.SUB_5);
		final GameweekScoreEntity gibsScore = new GameweekScoreEntity(1, 0);
		gibs.addGameweekScore(gibsScore);
		
		teamDataService.updateTeam(team);
	}
	
	private void populateTeamFive(final List<TeamEntity> teamsInLeague) {
		final TeamEntity team = getTeam(6, teamsInLeague);
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		final SelectedPlayerEntity cech = getSelectedPlayer(30, selectedPlayers);
		cech.setSelectionStatus(SelectedPlayerStatus.PICKED);
		cech.setPointsScored(2);
		final GameweekScoreEntity cechScore = new GameweekScoreEntity(1, 2);
		cech.addGameweekScore(cechScore);
		
		final SelectedPlayerEntity zaba = getSelectedPlayer(112, selectedPlayers);
		zaba.setSelectionStatus(SelectedPlayerStatus.PICKED);
		zaba.setPointsScored(5);
		final GameweekScoreEntity zabaScore = new GameweekScoreEntity(1, 5);
		zaba.addGameweekScore(zabaScore);
		
		final SelectedPlayerEntity vert = getSelectedPlayer(34, selectedPlayers);
		vert.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity vertScore = new GameweekScoreEntity(1, 0);
		vert.addGameweekScore(vertScore);
		
		final SelectedPlayerEntity baines = getSelectedPlayer(33, selectedPlayers);
		baines.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity bainesScore = new GameweekScoreEntity(1, 0);
		baines.addGameweekScore(bainesScore);
		
		final SelectedPlayerEntity sagna = getSelectedPlayer(35, selectedPlayers);
		sagna.setSelectionStatus(SelectedPlayerStatus.PICKED);
		sagna.setPointsScored(1);
		final GameweekScoreEntity sagnaScore = new GameweekScoreEntity(1, 1);
		sagna.addGameweekScore(sagnaScore);
		
		final SelectedPlayerEntity herr = getSelectedPlayer(36, selectedPlayers);
		herr.setSelectionStatus(SelectedPlayerStatus.VICE_CAPTAIN);
		herr.setPointsScored(3);
		final GameweekScoreEntity herrScore = new GameweekScoreEntity(1, 3);
		herr.addGameweekScore(herrScore);
		
		final SelectedPlayerEntity nolan = getSelectedPlayer(114, selectedPlayers);
		nolan.setSelectionStatus(SelectedPlayerStatus.PICKED);
		nolan.setPointsScored(3);
		final GameweekScoreEntity nolanScore = new GameweekScoreEntity(1, 3);
		nolan.addGameweekScore(nolanScore);
		
		final SelectedPlayerEntity walcott = getSelectedPlayer(115, selectedPlayers);
		walcott.setSelectionStatus(SelectedPlayerStatus.PICKED);
		walcott.setPointsScored(2);
		final GameweekScoreEntity walcottScore = new GameweekScoreEntity(1, 2);
		walcott.addGameweekScore(walcottScore);
		
		final SelectedPlayerEntity lall = getSelectedPlayer(139, selectedPlayers);
		lall.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity lallScore = new GameweekScoreEntity(1, 0);
		lall.addGameweekScore(lallScore);
		
		final SelectedPlayerEntity aguero = getSelectedPlayer(37, selectedPlayers);
		aguero.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		aguero.setPointsScored(2);
		final GameweekScoreEntity agueroScore = new GameweekScoreEntity(1, 2);
		aguero.addGameweekScore(agueroScore);
		
		final SelectedPlayerEntity luk = getSelectedPlayer(97, selectedPlayers);
		luk.setSelectionStatus(SelectedPlayerStatus.PICKED);
		luk.setPointsScored(2);
		final GameweekScoreEntity lukScore = new GameweekScoreEntity(1, 2);
		luk.addGameweekScore(lukScore);
		
		final SelectedPlayerEntity court = getSelectedPlayer(31, selectedPlayers);
		court.setSelectionStatus(SelectedPlayerStatus.SUB_1);
		final GameweekScoreEntity courtScore = new GameweekScoreEntity(1, 0);
		court.addGameweekScore(courtScore);
		
		final SelectedPlayerEntity drog = getSelectedPlayer(98, selectedPlayers);
		drog.setSelectionStatus(SelectedPlayerStatus.SUB_2);
		drog.setPointsScored(1);
		final GameweekScoreEntity drogScore = new GameweekScoreEntity(1, 1);
		drog.addGameweekScore(drogScore);
		
		final SelectedPlayerEntity hend = getSelectedPlayer(113, selectedPlayers);
		hend.setSelectionStatus(SelectedPlayerStatus.SUB_3);
		hend.setPointsScored(1);
		final GameweekScoreEntity hendScore = new GameweekScoreEntity(1, 1);
		hend.addGameweekScore(hendScore);
		
		final SelectedPlayerEntity camp = getSelectedPlayer(32, selectedPlayers);
		camp.setSelectionStatus(SelectedPlayerStatus.SUB_4);
		camp.setPointsScored(1);
		final GameweekScoreEntity campScore = new GameweekScoreEntity(1, 1);
		camp.addGameweekScore(campScore);
		
		final SelectedPlayerEntity gibs = getSelectedPlayer(116, selectedPlayers);
		gibs.setSelectionStatus(SelectedPlayerStatus.SUB_5);
		final GameweekScoreEntity gibsScore = new GameweekScoreEntity(1, 0);
		gibs.addGameweekScore(gibsScore);
		
		teamDataService.updateTeam(team);
	}
	
	private void populateTeamSix(final List<TeamEntity> teamsInLeague) {
		final TeamEntity team = getTeam(7, teamsInLeague);
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		final SelectedPlayerEntity cech = getSelectedPlayer(103, selectedPlayers);
		cech.setSelectionStatus(SelectedPlayerStatus.PICKED);
		cech.setPointsScored(2);
		final GameweekScoreEntity cechScore = new GameweekScoreEntity(1, 2);
		cech.addGameweekScore(cechScore);
		
		final SelectedPlayerEntity zaba = getSelectedPlayer(55, selectedPlayers);
		zaba.setSelectionStatus(SelectedPlayerStatus.PICKED);
		zaba.setPointsScored(2);
		final GameweekScoreEntity zabaScore = new GameweekScoreEntity(1, 2);
		zaba.addGameweekScore(zabaScore);
		
		final SelectedPlayerEntity vert = getSelectedPlayer(56, selectedPlayers);
		vert.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity vertScore = new GameweekScoreEntity(1, 0);
		vert.addGameweekScore(vertScore);
		
		final SelectedPlayerEntity baines = getSelectedPlayer(57, selectedPlayers);
		baines.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity bainesScore = new GameweekScoreEntity(1, 0);
		baines.addGameweekScore(bainesScore);
		
		final SelectedPlayerEntity sagna = getSelectedPlayer(60, selectedPlayers);
		sagna.setSelectionStatus(SelectedPlayerStatus.VICE_CAPTAIN);
		sagna.setPointsScored(4);
		final GameweekScoreEntity sagnaScore = new GameweekScoreEntity(1, 4);
		sagna.addGameweekScore(sagnaScore);
		
		final SelectedPlayerEntity herr = getSelectedPlayer(61, selectedPlayers);
		herr.setSelectionStatus(SelectedPlayerStatus.PICKED);
		herr.setPointsScored(3);
		final GameweekScoreEntity herrScore = new GameweekScoreEntity(1, 3);
		herr.addGameweekScore(herrScore);
		
		final SelectedPlayerEntity nolan = getSelectedPlayer(62, selectedPlayers);
		nolan.setSelectionStatus(SelectedPlayerStatus.PICKED);
		nolan.setPointsScored(2);
		final GameweekScoreEntity nolanScore = new GameweekScoreEntity(1, 2);
		nolan.addGameweekScore(nolanScore);
		
		final SelectedPlayerEntity walcott = getSelectedPlayer(63, selectedPlayers);
		walcott.setSelectionStatus(SelectedPlayerStatus.PICKED);
		walcott.setPointsScored(1);
		final GameweekScoreEntity walcottScore = new GameweekScoreEntity(1, 1);
		walcott.addGameweekScore(walcottScore);
		
		final SelectedPlayerEntity lall = getSelectedPlayer(64, selectedPlayers);
		lall.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		final GameweekScoreEntity lallScore = new GameweekScoreEntity(1, 0);
		lall.addGameweekScore(lallScore);
		
		final SelectedPlayerEntity aguero = getSelectedPlayer(65, selectedPlayers);
		aguero.setSelectionStatus(SelectedPlayerStatus.PICKED);
		aguero.setPointsScored(5);
		final GameweekScoreEntity agueroScore = new GameweekScoreEntity(1, 5);
		aguero.addGameweekScore(agueroScore);
		
		final SelectedPlayerEntity luk = getSelectedPlayer(66, selectedPlayers);
		luk.setSelectionStatus(SelectedPlayerStatus.PICKED);
		luk.setPointsScored(2);
		final GameweekScoreEntity lukScore = new GameweekScoreEntity(1, 2);
		luk.addGameweekScore(lukScore);
		
		final SelectedPlayerEntity court = getSelectedPlayer(67, selectedPlayers);
		court.setSelectionStatus(SelectedPlayerStatus.SUB_1);
		court.setPointsScored(2);
		final GameweekScoreEntity courtScore = new GameweekScoreEntity(1, 2);
		court.addGameweekScore(courtScore);
		
		final SelectedPlayerEntity drog = getSelectedPlayer(59, selectedPlayers);
		drog.setSelectionStatus(SelectedPlayerStatus.SUB_2);
		drog.setPointsScored(4);
		final GameweekScoreEntity drogScore = new GameweekScoreEntity(1, 4);
		drog.addGameweekScore(drogScore);
		
		final SelectedPlayerEntity hend = getSelectedPlayer(140, selectedPlayers);
		hend.setSelectionStatus(SelectedPlayerStatus.SUB_3);
		final GameweekScoreEntity hendScore = new GameweekScoreEntity(1, 0);
		hend.addGameweekScore(hendScore);
		
		final SelectedPlayerEntity camp = getSelectedPlayer(58, selectedPlayers);
		camp.setSelectionStatus(SelectedPlayerStatus.SUB_4);
		final GameweekScoreEntity campScore = new GameweekScoreEntity(1, 0);
		camp.addGameweekScore(campScore);
		
		final SelectedPlayerEntity gibs = getSelectedPlayer(54, selectedPlayers);
		gibs.setSelectionStatus(SelectedPlayerStatus.SUB_5);
		final GameweekScoreEntity gibsScore = new GameweekScoreEntity(1, 0);
		gibs.addGameweekScore(gibsScore);
		
		teamDataService.updateTeam(team);
	}
	
	private void populateTeamSeven(final List<TeamEntity> teamsInLeague) {
		final TeamEntity team = getTeam(5, teamsInLeague);
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		final SelectedPlayerEntity cech = getSelectedPlayer(14, selectedPlayers);
		cech.setSelectionStatus(SelectedPlayerStatus.PICKED);
		cech.setPointsScored(6);
		final GameweekScoreEntity cechScore = new GameweekScoreEntity(1, 6);
		cech.addGameweekScore(cechScore);
		
		final SelectedPlayerEntity zaba = getSelectedPlayer(15, selectedPlayers);
		zaba.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity zabaScore = new GameweekScoreEntity(1, 0);
		zaba.addGameweekScore(zabaScore);
		
		final SelectedPlayerEntity vert = getSelectedPlayer(16, selectedPlayers);
		vert.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity vertScore = new GameweekScoreEntity(1, 0);
		vert.addGameweekScore(vertScore);
		
		final SelectedPlayerEntity baines = getSelectedPlayer(17, selectedPlayers);
		baines.setSelectionStatus(SelectedPlayerStatus.PICKED);
		baines.setPointsScored(1);
		final GameweekScoreEntity bainesScore = new GameweekScoreEntity(1, 1);
		baines.addGameweekScore(bainesScore);
		
		final SelectedPlayerEntity sagna = getSelectedPlayer(134, selectedPlayers);
		sagna.setSelectionStatus(SelectedPlayerStatus.PICKED);
		sagna.setPointsScored(5);
		final GameweekScoreEntity sagnaScore = new GameweekScoreEntity(1, 5);
		sagna.addGameweekScore(sagnaScore);
		
		final SelectedPlayerEntity herr = getSelectedPlayer(19, selectedPlayers);
		herr.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity herrScore = new GameweekScoreEntity(1, 0);
		herr.addGameweekScore(herrScore);
		
		final SelectedPlayerEntity nolan = getSelectedPlayer(20, selectedPlayers);
		nolan.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		nolan.setPointsScored(20);
		final GameweekScoreEntity nolanScore = new GameweekScoreEntity(1, 20);
		nolan.addGameweekScore(nolanScore);
		
		final SelectedPlayerEntity walcott = getSelectedPlayer(21, selectedPlayers);
		walcott.setSelectionStatus(SelectedPlayerStatus.VICE_CAPTAIN);
		walcott.setPointsScored(11);
		final GameweekScoreEntity walcottScore = new GameweekScoreEntity(1, 11);
		walcott.addGameweekScore(walcottScore);
		
		final SelectedPlayerEntity lall = getSelectedPlayer(22, selectedPlayers);
		lall.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity lallScore = new GameweekScoreEntity(1, 0);
		lall.addGameweekScore(lallScore);
		
		final SelectedPlayerEntity aguero = getSelectedPlayer(23, selectedPlayers);
		aguero.setSelectionStatus(SelectedPlayerStatus.PICKED);
		aguero.setPointsScored(2);
		final GameweekScoreEntity agueroScore = new GameweekScoreEntity(1, 2);
		aguero.addGameweekScore(agueroScore);
		
		final SelectedPlayerEntity luk = getSelectedPlayer(109, selectedPlayers);
		luk.setSelectionStatus(SelectedPlayerStatus.PICKED);
		luk.setPointsScored(1);
		final GameweekScoreEntity lukScore = new GameweekScoreEntity(1, 1);
		luk.addGameweekScore(lukScore);
		
		final SelectedPlayerEntity court = getSelectedPlayer(13, selectedPlayers);
		court.setSelectionStatus(SelectedPlayerStatus.SUB_1);
		final GameweekScoreEntity courtScore = new GameweekScoreEntity(1, 0);
		court.addGameweekScore(courtScore);
		
		final SelectedPlayerEntity drog = getSelectedPlayer(86, selectedPlayers);
		drog.setSelectionStatus(SelectedPlayerStatus.SUB_2);
		final GameweekScoreEntity drogScore = new GameweekScoreEntity(1, 0);
		drog.addGameweekScore(drogScore);
		
		final SelectedPlayerEntity hend = getSelectedPlayer(108, selectedPlayers);
		hend.setSelectionStatus(SelectedPlayerStatus.SUB_3);
		hend.setPointsScored(2);
		final GameweekScoreEntity hendScore = new GameweekScoreEntity(1, 2);
		hend.addGameweekScore(hendScore);
		
		final SelectedPlayerEntity camp = getSelectedPlayer(24, selectedPlayers);
		camp.setSelectionStatus(SelectedPlayerStatus.SUB_4);
		camp.setPointsScored(2);
		final GameweekScoreEntity campScore = new GameweekScoreEntity(1, 2);
		camp.addGameweekScore(campScore);
		
		final SelectedPlayerEntity gibs = getSelectedPlayer(18, selectedPlayers);
		gibs.setSelectionStatus(SelectedPlayerStatus.SUB_5);
		gibs.setPointsScored(2);
		final GameweekScoreEntity gibsScore = new GameweekScoreEntity(1, 2);
		gibs.addGameweekScore(gibsScore);
		
		teamDataService.updateTeam(team);
	}
	
	private void populateTeamEight(final List<TeamEntity> teamsInLeague) {
		final TeamEntity team = getTeam(8, teamsInLeague);
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		final SelectedPlayerEntity cech = getSelectedPlayer(6, selectedPlayers);
		cech.setSelectionStatus(SelectedPlayerStatus.PICKED);
		cech.setPointsScored(7);
		final GameweekScoreEntity cechScore = new GameweekScoreEntity(1, 7);
		cech.addGameweekScore(cechScore);
		
		final SelectedPlayerEntity zaba = getSelectedPlayer(7, selectedPlayers);
		zaba.setSelectionStatus(SelectedPlayerStatus.PICKED);
		zaba.setPointsScored(1);
		final GameweekScoreEntity zabaScore = new GameweekScoreEntity(1, 1);
		zaba.addGameweekScore(zabaScore);
		
		final SelectedPlayerEntity vert = getSelectedPlayer(78, selectedPlayers);
		vert.setSelectionStatus(SelectedPlayerStatus.PICKED);
		vert.setPointsScored(2);
		final GameweekScoreEntity vertScore = new GameweekScoreEntity(1, 2);
		vert.addGameweekScore(vertScore);
		
		final SelectedPlayerEntity baines = getSelectedPlayer(8, selectedPlayers);
		baines.setSelectionStatus(SelectedPlayerStatus.PICKED);
		baines.setPointsScored(2);
		final GameweekScoreEntity bainesScore = new GameweekScoreEntity(1, 2);
		baines.addGameweekScore(bainesScore);
		
		final SelectedPlayerEntity sagna = getSelectedPlayer(79, selectedPlayers);
		sagna.setSelectionStatus(SelectedPlayerStatus.PICKED);
		sagna.setPointsScored(4);
		final GameweekScoreEntity sagnaScore = new GameweekScoreEntity(1, 4);
		sagna.addGameweekScore(sagnaScore);
		
		final SelectedPlayerEntity herr = getSelectedPlayer(9, selectedPlayers);
		herr.setSelectionStatus(SelectedPlayerStatus.VICE_CAPTAIN);
		herr.setPointsScored(1);
		final GameweekScoreEntity herrScore = new GameweekScoreEntity(1, 1);
		herr.addGameweekScore(herrScore);
		
		final SelectedPlayerEntity nolan = getSelectedPlayer(10, selectedPlayers);
		nolan.setSelectionStatus(SelectedPlayerStatus.PICKED);
		nolan.setPointsScored(8);
		final GameweekScoreEntity nolanScore = new GameweekScoreEntity(1, 8);
		nolan.addGameweekScore(nolanScore);
		
		final SelectedPlayerEntity walcott = getSelectedPlayer(11, selectedPlayers);
		walcott.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity walcottScore = new GameweekScoreEntity(1, 0);
		walcott.addGameweekScore(walcottScore);
		
		final SelectedPlayerEntity lall = getSelectedPlayer(81, selectedPlayers);
		lall.setSelectionStatus(SelectedPlayerStatus.PICKED);
		lall.setPointsScored(2);
		final GameweekScoreEntity lallScore = new GameweekScoreEntity(1, 2);
		lall.addGameweekScore(lallScore);
		
		final SelectedPlayerEntity aguero = getSelectedPlayer(82, selectedPlayers);
		aguero.setSelectionStatus(SelectedPlayerStatus.PICKED);
		aguero.setPointsScored(3);
		final GameweekScoreEntity agueroScore = new GameweekScoreEntity(1, 3);
		aguero.addGameweekScore(agueroScore);
		
		final SelectedPlayerEntity luk = getSelectedPlayer(12, selectedPlayers);
		luk.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		luk.setPointsScored(12);
		final GameweekScoreEntity lukScore = new GameweekScoreEntity(1, 12);
		luk.addGameweekScore(lukScore);
		
		final SelectedPlayerEntity court = getSelectedPlayer(107, selectedPlayers);
		court.setSelectionStatus(SelectedPlayerStatus.SUB_1);
		final GameweekScoreEntity courtScore = new GameweekScoreEntity(1, 0);
		court.addGameweekScore(courtScore);
		
		final SelectedPlayerEntity drog = getSelectedPlayer(84, selectedPlayers);
		drog.setSelectionStatus(SelectedPlayerStatus.SUB_2);
		final GameweekScoreEntity drogScore = new GameweekScoreEntity(1, 0);
		drog.addGameweekScore(drogScore);
		
		final SelectedPlayerEntity hend = getSelectedPlayer(80, selectedPlayers);
		hend.setSelectionStatus(SelectedPlayerStatus.SUB_3);
		hend.setPointsScored(2);
		final GameweekScoreEntity hendScore = new GameweekScoreEntity(1, 2);
		hend.addGameweekScore(hendScore);
		
		final SelectedPlayerEntity camp = getSelectedPlayer(83, selectedPlayers);
		camp.setSelectionStatus(SelectedPlayerStatus.SUB_4);
		final GameweekScoreEntity campScore = new GameweekScoreEntity(1, 0);
		camp.addGameweekScore(campScore);
		
		final SelectedPlayerEntity gibs = getSelectedPlayer(85, selectedPlayers);
		gibs.setSelectionStatus(SelectedPlayerStatus.SUB_5);
		final GameweekScoreEntity gibsScore = new GameweekScoreEntity(1, 0);
		gibs.addGameweekScore(gibsScore);
		
		teamDataService.updateTeam(team);
	}
	
	private void populateTeamNine(final List<TeamEntity> teamsInLeague) {
		final TeamEntity team = getTeam(9, teamsInLeague);
		final List<SelectedPlayerEntity> selectedPlayers = team.getSelectedPlayers();
		
		final SelectedPlayerEntity cech = getSelectedPlayer(124, selectedPlayers);
		cech.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity cechScore = new GameweekScoreEntity(1, 0);
		cech.addGameweekScore(cechScore);
		
		final SelectedPlayerEntity zaba = getSelectedPlayer(68, selectedPlayers);
		zaba.setSelectionStatus(SelectedPlayerStatus.PICKED);
		zaba.setPointsScored(10);
		final GameweekScoreEntity zabaScore = new GameweekScoreEntity(1, 10);
		zaba.addGameweekScore(zabaScore);
		
		final SelectedPlayerEntity vert = getSelectedPlayer(69, selectedPlayers);
		vert.setSelectionStatus(SelectedPlayerStatus.CAPTAIN);
		vert.setPointsScored(10);
		final GameweekScoreEntity vertScore = new GameweekScoreEntity(1, 10);
		vert.addGameweekScore(vertScore);
		
		final SelectedPlayerEntity baines = getSelectedPlayer(141, selectedPlayers);
		baines.setSelectionStatus(SelectedPlayerStatus.PICKED);
		baines.setPointsScored(3);
		final GameweekScoreEntity bainesScore = new GameweekScoreEntity(1, 3);
		baines.addGameweekScore(bainesScore);
		
		final SelectedPlayerEntity sagna = getSelectedPlayer(70, selectedPlayers);
		sagna.setSelectionStatus(SelectedPlayerStatus.VICE_CAPTAIN);
		sagna.setPointsScored(2);
		final GameweekScoreEntity sagnaScore = new GameweekScoreEntity(1, 2);
		sagna.addGameweekScore(sagnaScore);
		
		final SelectedPlayerEntity herr = getSelectedPlayer(129, selectedPlayers);
		herr.setSelectionStatus(SelectedPlayerStatus.PICKED);
		herr.setPointsScored(1);
		final GameweekScoreEntity herrScore = new GameweekScoreEntity(1, 1);
		herr.addGameweekScore(herrScore);
		
		final SelectedPlayerEntity nolan = getSelectedPlayer(147, selectedPlayers);
		nolan.setSelectionStatus(SelectedPlayerStatus.PICKED);
		nolan.setPointsScored(3);
		final GameweekScoreEntity nolanScore = new GameweekScoreEntity(1, 3);
		nolan.addGameweekScore(nolanScore);
		
		final SelectedPlayerEntity walcott = getSelectedPlayer(148, selectedPlayers);
		walcott.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity walcottScore = new GameweekScoreEntity(1, 0);
		walcott.addGameweekScore(walcottScore);
		
		final SelectedPlayerEntity lall = getSelectedPlayer(71, selectedPlayers);
		lall.setSelectionStatus(SelectedPlayerStatus.PICKED);
		lall.setPointsScored(2);
		final GameweekScoreEntity lallScore = new GameweekScoreEntity(1, 2);
		lall.addGameweekScore(lallScore);
		
		final SelectedPlayerEntity aguero = getSelectedPlayer(150, selectedPlayers);
		aguero.setSelectionStatus(SelectedPlayerStatus.PICKED);
		final GameweekScoreEntity agueroScore = new GameweekScoreEntity(1, 0);
		aguero.addGameweekScore(agueroScore);
		
		final SelectedPlayerEntity luk = getSelectedPlayer(142, selectedPlayers);
		luk.setSelectionStatus(SelectedPlayerStatus.PICKED);
		luk.setPointsScored(2);
		final GameweekScoreEntity lukScore = new GameweekScoreEntity(1, 2);
		luk.addGameweekScore(lukScore);
		
		final SelectedPlayerEntity court = getSelectedPlayer(145, selectedPlayers);
		court.setSelectionStatus(SelectedPlayerStatus.SUB_1);
		final GameweekScoreEntity courtScore = new GameweekScoreEntity(1, 0);
		court.addGameweekScore(courtScore);
		
		final SelectedPlayerEntity drog = getSelectedPlayer(149, selectedPlayers);
		drog.setSelectionStatus(SelectedPlayerStatus.SUB_2);
		final GameweekScoreEntity drogScore = new GameweekScoreEntity(1, 0);
		drog.addGameweekScore(drogScore);
		
		final SelectedPlayerEntity hend = getSelectedPlayer(131, selectedPlayers);
		hend.setSelectionStatus(SelectedPlayerStatus.SUB_3);
		final GameweekScoreEntity hendScore = new GameweekScoreEntity(1, 0);
		hend.addGameweekScore(hendScore);
		
		final SelectedPlayerEntity camp = getSelectedPlayer(123, selectedPlayers);
		camp.setSelectionStatus(SelectedPlayerStatus.SUB_4);
		camp.setPointsScored(2);
		final GameweekScoreEntity campScore = new GameweekScoreEntity(1, 2);
		camp.addGameweekScore(campScore);
		
		final SelectedPlayerEntity gibs = getSelectedPlayer(146, selectedPlayers);
		gibs.setSelectionStatus(SelectedPlayerStatus.SUB_5);
		gibs.setPointsScored(2);
		final GameweekScoreEntity gibsScore = new GameweekScoreEntity(1, 2);
		gibs.addGameweekScore(gibsScore);
		
		teamDataService.updateTeam(team);
	}
	
	private TeamEntity getTeam(final int id, final List<TeamEntity> teams) {
		for (final TeamEntity team : teams) {
			if (team.getId() == id) {
				return team;
			}
		}
		
		return null;
	}
	
	private SelectedPlayerEntity getSelectedPlayer(final int id, final List<SelectedPlayerEntity> selectedPlayers) {
		for (final SelectedPlayerEntity selectedPlayer : selectedPlayers) {
			if (selectedPlayer.getId() == id) {
				return selectedPlayer;
			}
		}
		
		return null;
	}
}
