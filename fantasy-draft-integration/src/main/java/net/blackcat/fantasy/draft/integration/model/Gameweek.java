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
 * Model object storing gameweek information.
 * 
 * @author Chris
 *
 */
@Entity
public class Gameweek implements Serializable {

	private static final long serialVersionUID = 8145028726211876889L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column
	private int previousGameweek;

	@Column
	private int currentGameweek;

	/**
	 * Move to the next gameweek.
	 */
	public void moveToNextGameweek() {
		previousGameweek++;
		currentGameweek++;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the previousGameweek
	 */
	public int getPreviousGameweek() {
		return previousGameweek;
	}

	/**
	 * @return the currentGameweek
	 */
	public int getCurrentGameweek() {
		return currentGameweek;
	}
}
