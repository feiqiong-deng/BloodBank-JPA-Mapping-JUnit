/***************************************************************************
 * File: PojoListener.java Course materials (22W) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 *
 */
package bloodbank.entity;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@SuppressWarnings("unused")

public class PojoListener {

	// TODO - What annotation is used when we want to do something just before object is INSERT'd in the Database?
	public void setCreatedOnDate( PojoBase pojoBase) {
		LocalDateTime now = LocalDateTime.now();
		// TODO - what member field(s) do we wish to alter just before object is INSERT'd in the Database?
	}

	// TODO - What annotation is used when we want to do something just before object is UPDATE'd in the Database?
	public void setUpdatedDate( PojoBase pojoBase) {
		// TODO - what member field(s) do we wish to alter just before object is UPDATE'd in the Database?
	}

}