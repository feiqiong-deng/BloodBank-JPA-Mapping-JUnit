/***************************************************************************
 * File: BloodDonation.java Course materials (22W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * 
 */
package bloodbank.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@SuppressWarnings("unused")

/**
 * The persistent class for the blood_donation database table.
 */
//TODO BD01 - add the missing annotations.
//TODO BD02 - do we need a mapped super class? which one?
@Entity
@Table( name = "blood_donation")
@NamedQuery( name = "BloodDonation.findAll", query = "SELECT a FROM BloodDonation a")
@AttributeOverride( name = "id", column = @Column( name = "donation_id"))
public class BloodDonation extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO BD03 - add annotations for M:1. changes to this class should cascade to BloodBank.
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="bank_id", referencedColumnName="bank_id", nullable = false)
	private BloodBank bank;

	// TODO BD04 - add annotations for 1:1. changes to this class should not cascade to DonationRecord.
	// TODO BD05 - this object should not be insertable or updatable;
	@OneToOne(mappedBy="donation")
	private DonationRecord record;

	// TODO BD06 - add annotations
	@Basic( optional = false)
	@Column(name="milliliters", nullable = false)
	private int milliliters;

	// TODO BD07 - this object is embedded
	@Embedded
	private BloodType bloodType;

	public BloodDonation() {
		bloodType = new BloodType();
	}

	public BloodBank getBank() {
		return bank;
	}

	public void setBank( BloodBank bank) {
		this.bank = bank;
		//we must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (bank != null) {
			bank.getDonations().add(this);
		}
	}

	public DonationRecord getRecord() {
		return record;
	}

	public void setRecord( DonationRecord record) {
		this.record = record;
		//we should (as above) set the 'other' side of this relationship, but that causes an infinite-loop
	}

	public int getMilliliters() {
		return milliliters;
	}

	public void setMilliliters( int milliliters) {
		this.milliliters = milliliters;
	}

	public BloodType getBloodType() {
		return bloodType;
	}

	public void setBloodType( BloodType bloodType) {
		this.bloodType = bloodType;
	}
	
	//Inherited hashCode/equals NOT sufficient for this Entity class
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
		
		// include BloodType in identity
		return prime * result + Objects.hash(getId(), getBloodType());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof BloodDonation otherBloodDonation) {
			// see comment (above) in hashCode(): compare using only member variables that are
			// truely part of an object's identity
			return Objects.equals(this.getId(), otherBloodDonation.getId()) &&
				Objects.equals(this.getBloodType(), otherBloodDonation.getBloodType());
		}
		return false;
	}
}