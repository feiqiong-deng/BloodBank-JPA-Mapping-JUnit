package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2022-03-21T23:14:23.162-0400")
@StaticMetamodel(DonationRecord.class)
public class DonationRecord_ extends PojoBase_ {
	public static volatile SingularAttribute<DonationRecord, BloodDonation> donation;
	public static volatile SingularAttribute<DonationRecord, Byte> tested;
	public static volatile SingularAttribute<DonationRecord, Person> owner;
}
