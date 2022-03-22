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

public class PojoCompositeListener {

	// TODO - What annotation is used when we want to do something just before object is INSERT'd into Database?
	@PrePersist
	public void setCreatedOnDate( PojoBaseCompositeKey< ?> pojoBaseComposite) {
		LocalDateTime now = LocalDateTime.now();
		// TODO - what member field(s) do we wish to alter just before object is INSERT'd in the Database?
		pojoBaseComposite.setCreated(now);
		pojoBaseComposite.setUpdated(now);
	}

	// TODO - What annotation is used when we want to do something just before object is UPDATE'd into Database?
	@PreUpdate
	public void setUpdatedDate( PojoBaseCompositeKey< ?> pojoBaseComposite) {
		// TODO - what member field(s) do we wish to alter just before object is UPDATE'd in the Database?
		pojoBaseComposite.setUpdated(LocalDateTime.now());
	}

}