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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(unique = true)
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
