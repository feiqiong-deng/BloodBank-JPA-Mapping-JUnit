package bloodbank.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2022-03-21T15:03:13.346-0400")
@StaticMetamodel(Phone.class)
public class Phone_ extends PojoBase_ {
	public static volatile SingularAttribute<Phone, String> areaCode;
	public static volatile SingularAttribute<Phone, String> countryCode;
	public static volatile SingularAttribute<Phone, String> number;
	public static volatile SetAttribute<Phone, Contact> contacts;
}
