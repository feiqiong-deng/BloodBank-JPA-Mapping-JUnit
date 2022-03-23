/***************************************************************************
 * File: PojoBaseCompositeKey.java Course materials (22W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * @author Mike Norman
 * @date 2020 04
 */
package bloodbank.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@SuppressWarnings("unused")

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 * 
 * @param <ID> - type of composite key used
 */
//TODO PC01 - add annotation to define this class as superclass of all entities. Week 9 slides.
//TODO PC02 - add annotation to place all JPA annotations on fields.
//TODO PC03 - add annotation to for listener.
@MappedSuperclass
@Access(AccessType.FIELD) 
@EntityListeners({PojoCompositeListener.class})
public abstract class PojoBaseCompositeKey<ID extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO PC04 - add missing annotations.
	@Version
	protected int version;

	// TODO PC05 - add missing annotations (hint, is this column on DB).
    @Column(name = "created")
	protected LocalDateTime created;

	// TODO PC06 - add missing annotations (hint, is this column on DB).
    @Column(name = "updated")
    protected LocalDateTime updated;

	public abstract ID getId();

	public abstract void setId( ID id);
    
	public int getVersion() {
		return version;
	}

	public void setVersion( int version) {
		this.version = version;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated( LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated( LocalDateTime updated) {
		this.updated = updated;
	}

	/**
	 * Very important: use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation
		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof PojoBaseCompositeKey otherPojoBaseComposite) {
			// see comment (above) in hashCode(): compare using only member variables that are
			// truely part of an object's identity
			return Objects.equals(this.getId(), otherPojoBaseComposite.getId());
		}
		return false;
	}
}