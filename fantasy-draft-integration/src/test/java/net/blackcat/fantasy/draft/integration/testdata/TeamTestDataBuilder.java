/**
 * 
 */
package net.blackcat.fantasy.draft.integration.testdata;

import java.util.ArrayList;
import java.util.List;

import net.blackcat.fantasy.draft.integration.model.Bid;
import net.blackcat.fantasy.draft.integration.model.GameweekScore;
import net.blackcat.fantasy.draft.integration.model.Team;

/**
 * Builder for building instances of {@link Team} objects to use in unit tests.
 * 
 * @author Chris Hern
 * 
 */
public class TeamTestDataBuilder {

    private static final String DEFAULT_NAME = TestDataConstants.TEAM_ONE_NAME;
    
    private String teamName;

    private int squadSize = 0;
    private List<GameweekScore> gameweekScores;

    private TeamTestDataBuilder() {
    	this.teamName = DEFAULT_NAME;
    	this.gameweekScores = new ArrayList<GameweekScore>();
    }

    public static TeamTestDataBuilder aTeam() {
        return new TeamTestDataBuilder();
    }

    public TeamTestDataBuilder withSquadSize(final int squadSize) {
        this.squadSize = squadSize;
        return this;
    }
    
    public TeamTestDataBuilder withName(final String teamName) {
    	this.teamName = teamName;
    	return this;
    }
    
    public TeamTestDataBuilder withGameweekScore(final int gameweek, final int score) {
    	final GameweekScore gameweekScore = new GameweekScore(gameweek, score);
    	
    	gameweekScores.add(gameweekScore);
    	
    	return this;
    }

    public Team build() {
        final Team team = new Team(teamName);

        for (int i = 0; i < squadSize; i++) {
            final Bid bid = BidTestDataBuilder.aSuccessfulBid().build();
            team.processSuccessfulBid(bid);
        }
        
        for (final GameweekScore gameweekScore : gameweekScores) {
        	team.addGameweekScore(gameweekScore.getGameweek(), gameweekScore.getScore());
        }

        return team;
    }
}
