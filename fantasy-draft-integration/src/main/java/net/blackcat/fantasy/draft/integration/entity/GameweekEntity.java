/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity representing 
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "Gameweek")
public class GameweekEntity implements Serializable {

	private static final long serialVersionUID = 3924746736277124328L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column
	private int previousGameweek;

	@Column
	private int currentGameweek;
	
	/**
	 * @return the previousGameweek
	 */
	public int getPreviousGameweek() {
		return previousGameweek;
	}
	
	/**
	 * @param previousGameweek the previousGameweek to set
	 */
	public void setPreviousGameweek(int previousGameweek) {
		this.previousGameweek = previousGameweek;
	}
	
	/**
	 * @return the currentGameweek
	 */
	public int getCurrentGameweek() {
		return currentGameweek;
	}
	
	/**
	 * @param currentGameweek the currentGameweek to set
	 */
	public void setCurrentGameweek(int currentGameweek) {
		this.currentGameweek = currentGameweek;
	}
}