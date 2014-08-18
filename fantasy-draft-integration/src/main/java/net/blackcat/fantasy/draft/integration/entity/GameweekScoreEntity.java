/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity representing a score achieved during a fantasy draft gameweek.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "GameweekScore")
public class GameweekScoreEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column
	private int gameweek;
	
	@Column
	private int score;

	public GameweekScoreEntity() {
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the gameweek
	 */
	public int getGameweek() {
		return gameweek;
	}

	/**
	 * @param gameweek the gameweek to set
	 */
	public void setGameweek(int gameweek) {
		this.gameweek = gameweek;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
}
