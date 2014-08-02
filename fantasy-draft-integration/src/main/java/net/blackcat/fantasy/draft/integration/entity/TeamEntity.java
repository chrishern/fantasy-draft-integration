/**
 * 
 */
package net.blackcat.fantasy.draft.integration.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class representing a managed team within the fantasy draft game.
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "Team")
public class TeamEntity implements Serializable {

	private static final long serialVersionUID = 3011246980899172711L;

	@Id
	private String name;
	
	public TeamEntity() {
		
	}
	
	public TeamEntity(final String name) {
		this.name = name;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
