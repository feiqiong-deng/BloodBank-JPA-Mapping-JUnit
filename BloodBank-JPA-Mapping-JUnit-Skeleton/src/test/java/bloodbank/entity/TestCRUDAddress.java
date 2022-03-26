package bloodbank.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCRUDAddress extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	
	private static Address address;
	private static final String STREETNUM = "123";
	private static final String STREET = "cde ln";
	private static final String CITY = "toronto";
	private static final String PROVINCE = "ON";
	private static final String COUNTRY = "CA";
	private static final String ZIPCODE = "A2A9R4";

	@BeforeAll
	static void setupAllInit() {
		address = new Address();
		address.setStreetNumber( STREETNUM);
		address.setStreet(STREET);
		address.setCity(CITY);
		address.setProvince(PROVINCE);
		address.setCountry(COUNTRY);
		address.setZipcode(ZIPCODE);
	}

	@BeforeEach
	void setup() {
		em = getEntityManager();
		et = em.getTransaction();
	}

	@AfterEach
	void tearDown() {
		em.close();
	}
	
	@Test
	void test01_Empty() {
		Long result = getTotalCount(em, Address.class);
		assertThat( result, is( comparesEqualTo( 0L)));
	}
	
	@Test
	void test02_Create() {
		et.begin();
		em.persist( address);	
		et.commit();
		Long result = getCountWithId(em, Address.class, Integer.class, Address_.id, address.getId());
		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}
	
	
	@Test
	void test03_CreateInvalid() {
		et.begin();
		Address addressTest = new Address();
		addressTest.setStreetNumber(STREETNUM);
		addressTest.setStreet(STREET);
		addressTest.setCity(CITY);
		addressTest.setProvince(PROVINCE);
		addressTest.setCountry(COUNTRY);
		assertThrows( PersistenceException.class, () -> em.persist( addressTest));
		et.commit();
	}
	
	@Test
	void test04_Read() {		
		List<Address> addresses = getAll(em, Address.class);
		assertThat( addresses, contains( equalTo( address)));
	}
	
	@Test
	void test05_ReadAllFields() {
		Address returnedAddress = getWithId(em, Address.class, Integer.class, Address_.id, address.getId());

		assertThat( returnedAddress.getStreetNumber(), equalTo( STREETNUM));
		assertThat( returnedAddress.getStreet(), equalTo( STREET));
		assertThat( returnedAddress.getCity(), equalTo( CITY));
		assertThat( returnedAddress.getProvince(), equalTo( PROVINCE));
		assertThat( returnedAddress.getCountry(), equalTo( COUNTRY));
		assertThat( returnedAddress.getZipcode(), equalTo( ZIPCODE));

	}
	
	@Test
	void test06_Update() {
		Address returnedAddress = getWithId(em, Address.class, Integer.class, Address_.id, address.getId());

		String newStreetNum = "666";
		String newStreet = "Hello Rd";
		String newCity = "boston";
		String newProvinve = "MA";
		String newCountry = "US";
		String newZipcode = "MAB789";

		et.begin();
		returnedAddress.setStreetNumber( newStreetNum);
		returnedAddress.setStreet( newStreet);
		returnedAddress.setCity(newCity);
		returnedAddress.setProvince(newProvinve);
		returnedAddress.setCountry(newCountry);
		returnedAddress.setZipcode(newZipcode);
		em.merge( returnedAddress);
		et.commit();

		returnedAddress = getWithId(em, Address.class, Integer.class, Address_.id, address.getId());

		assertThat( returnedAddress.getStreetNumber(), equalTo( newStreetNum));
		assertThat( returnedAddress.getStreet(), equalTo( newStreet));
		assertThat( returnedAddress.getCity(), equalTo( newCity));
		assertThat( returnedAddress.getProvince(), equalTo( newProvinve));
		assertThat( returnedAddress.getCountry(), equalTo( newCountry));
		assertThat( returnedAddress.getZipcode(), equalTo( newZipcode));
	}
	
	@Test
	void test07_Delete() {
		Address returnedAddress = getWithId(em, Address.class, Integer.class, Address_.id, address.getId());

		et.begin();

		Address addressTest = new Address();
		String newStreetNum = "999";
		String newStreet = "Apple Rd";
		String newCity = "ottawa";
		String newProvinve = "ON";
		String newCountry = "CA";
		String newZipcode = "Y027U8";
		
		addressTest.setStreetNumber( newStreetNum);
		addressTest.setStreet( newStreet);
		addressTest.setCity(newCity);
		addressTest.setProvince(newProvinve);
		addressTest.setCountry(newCountry);
		addressTest.setZipcode(newZipcode);
		
		em.persist( addressTest);
		et.commit();

		et.begin();
		em.remove( returnedAddress);
		et.commit();

		long result = getCountWithId(em, Address.class, Integer.class, Address_.id, returnedAddress.getId());
		assertThat( result, is( equalTo( 0L)));

		result = getCountWithId(em, Address.class, Integer.class, Address_.id, addressTest.getId());
		assertThat( result, is( equalTo( 1L)));
	}

}
