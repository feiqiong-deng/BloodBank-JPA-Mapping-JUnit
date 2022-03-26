/***************************************************************************
 * File: DonationRecord.java Course materials (22W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * 
 */
package bloodbank.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@SuppressWarnings("unused")

/**
 * The persistent class for the donation_record database table.
 */
//TODO DR01 - add the missing annotations.
//TODO DR02 - do we need a mapped super class? which one?
@Entity
@Table(name = "donation_record")
@NamedQuery( name = "DonationRecord.findAll", query = "SELECT a FROM DonationRecord a")
@AttributeOverride( name = "id", column = @Column( name = "record_id"))
public class DonationRecord extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO DR03 - add annotations for 1:1 mapping. changes here should cascade.
	@OneToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="donation_id", referencedColumnName = "donation_id", nullable = false)
	private BloodDonation donation;

	// TODO DR04 - add annotations for M:1 mapping. changes here should not cascade.
	@ManyToOne
	@JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
	private Person owner;

	// TODO DR05 - add annotations.
	@Basic( optional = false)
	@Column(name="tested", nullable = false)
	private byte tested;

	public DonationRecord() {
		super();
	}
	
	public DonationRecord(BloodDonation donation, Person owner, byte tested) {
		this();
		this.donation = donation;
		this.owner = owner;
		this.tested = tested;
	}

	public BloodDonation getDonation() {
		return donation;
	}

	public void setDonation( BloodDonation donation) {
		this.donation = donation;
		//we must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (donation != null) {
			donation.setRecord(this);
		}
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner( Person owner) {
		this.owner = owner;
		//we must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (owner != null) {
			owner.getDonations().add(this);
		}
	}

	public byte getTested() {
		return tested;
	}

	public void setTested(byte tested) {
		this.tested = tested;
	}

	public void setTested( boolean tested) {
		this.tested = (byte) ( tested ? 0b0001 : 0b0000);
	}
	
	//Inherited hashCode/equals is sufficient for this Entity class

}