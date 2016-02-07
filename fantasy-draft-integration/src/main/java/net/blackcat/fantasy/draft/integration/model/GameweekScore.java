/**
 * 
 */
package net.blackcat.fantasy.draft.integration.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Model object storing points scored in a gameweek.
 * 
 * @author Chris
 *
 */
@Entity
public class GameweekScore implements Serializable {

	private static final long serialVersionUID = -6032603140963980878L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column
	private int gameweek;
	
	@Column
	private int score;
	
	/*
	 * For Hibernate database mapping
	 */
	@SuppressWarnings("unused")
	private GameweekScore() {
		
	}

	public GameweekScore(final int gameweek, final int score) {
	
		this.gameweek = gameweek;
		this.score = score;
	}

	/**
	 * @return the gameweek
	 */
	public int getGameweek() {
		return gameweek;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
}
