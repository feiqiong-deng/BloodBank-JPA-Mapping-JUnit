/***************************************************************************
 * File: Phone.java Course materials (22W) CST 8277
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

@SuppressWarnings("unused")

/**
 * The persistent class for the phone database table.
 */
//TODO PH01 - add the missing annotations.
//TODO PH02 - do we need a mapped super class? which one?
public class Phone extends PojoBase implements Serializable{
	private static final long serialVersionUID = 1L;

	// TODO PH03 - add missing annotations.
	private String areaCode;

	// TODO PH04 - add missing annotations.
	private String countryCode;

	// TODO PH05 - add missing annotations.
	private String number;

	// TODO PH06 - add annotations for 1:M relation. insertable, updatable are false. remove should not cascade.
	private Set< Contact> contacts = new HashSet<>();

	public Phone() {
		super();
	}

	public Phone(String areaCode, String countryCode, String number) {
		this();
		this.areaCode = areaCode;
		this.countryCode = countryCode;
		this.number = number;
	}

	public Phone setNumber( String countryCode, String areaCode, String number) {
		setCountryCode( countryCode);
		setAreaCode( areaCode);
		setNumber( number);
		return this;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode( String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode( String countryCode) {
		this.countryCode = countryCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber( String number) {
		this.number = number;
	}

	public Set< Contact> getContacts() {
		return contacts;
	}

	public void setContacts( Set< Contact> contacts) {
		this.contacts = contacts;
	}

	//Inherited hashCode/equals is sufficient for this Entity class

}