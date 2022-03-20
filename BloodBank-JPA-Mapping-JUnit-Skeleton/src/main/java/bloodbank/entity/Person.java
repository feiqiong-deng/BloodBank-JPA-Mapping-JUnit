/***************************************************************************
 * File: Person.java Course materials (22W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * 
 */
package bloodbank.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * The persistent class for the PERSON database table in the bloodbank schema
 * </br></br>
 * 
 * Note: this is <b>NOT</b> the same Person entity from Lab 1/Assignment 1/Assignment 2.
 * </br>
 * This entiy does <b>NOT</b> have member fields email, phoneNumber, or city -
 * </br>
 * those properties are in related objects {@link bloodbank.entity.Address} and {@link bloodbank.entity.Phone}
 * 
 */
@SuppressWarnings("unused")
//TODO PR01 - add the missing annotations.
//TODO PR02 - do we need a mapped super class? which one?
@Entity
@Table( name = "person")
public class Person extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO PR03 - add annotation
	private String firstName;

	// TODO PR04 - add annotation
	private String lastName;

	// TODO PR08 - add annotations for 1:M relation. insertable, updatable are false. remove should not cascade.
	private Set< DonationRecord> donations = new HashSet<>();

	// TODO PR09 - add annotations for 1:M relation. insertable, updatable are false. remove should not cascade.
	private Set< Contact> contacts = new HashSet<>();

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set< DonationRecord> getDonations() {
		return donations;
	}

	public void setDonations( Set< DonationRecord> donations) {
		this.donations = donations;
	}

	public Set< Contact> getContacts() {
		return contacts;
	}

	public void setContacts( Set< Contact> contacts) {
		this.contacts = contacts;
	}

	public void setFullName( String firstName, String lastName) {
		setFirstName( firstName);
		setLastName( lastName);
	}
	
	//Inherited hashCode/equals is sufficient for this Entity class

}