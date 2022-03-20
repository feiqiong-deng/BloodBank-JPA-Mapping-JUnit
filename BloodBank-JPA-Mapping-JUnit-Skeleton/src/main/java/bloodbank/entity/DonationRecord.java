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

@SuppressWarnings("unused")

/**
 * The persistent class for the donation_record database table.
 */
//TODO DR01 - add the missing annotations.
//TODO DR02 - do we need a mapped super class? which one?
public class DonationRecord extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO DR03 - add annotations for 1:1 mapping. changes here should cascade.
	private BloodDonation donation;

	// TODO DR04 - add annotations for M:1 mapping. changes here should not cascade.
	private Person owner;

	// TODO DR05 - add annotations.
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