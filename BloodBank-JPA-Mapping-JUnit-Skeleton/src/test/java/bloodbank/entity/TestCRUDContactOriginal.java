package bloodbank.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCRUDContactOriginal extends JUnitBase {

	private EntityManager em;
	private EntityTransaction et;

	private static Phone phone;
	private static Address address;
	private static Person person;
	private static Contact contact;
	private static final String EMIAL = "test@test.com";
	private static final String CONTACT_TYPE = "Home";

	@BeforeAll
	static void setupAllInit() {
		phone = new Phone();
		phone.setNumber( "0", "234", "5678900");

		address = new Address();
		address.setAddress( "123", "abcd Dr.W", "ottawa", "ON", "CA", "A1B2C3");

		person = new Person();
		person.setFullName( "Teddy", "Yap");
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
	@Order(1)
	void test01_Empty() {
		long result = getTotalCount(em, Contact.class);
		assertThat( result, is( comparesEqualTo( 0L)));
	}

	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		contact = new Contact();
		contact.setAddress( address);
		contact.setPhone( phone);
		contact.setEmail( EMIAL);
		contact.setContactType( CONTACT_TYPE);
		contact.setOwner( person);
		em.persist( contact);
		et.commit();

		long result = getCountWithId(em, Contact.class, ContactPK.class, Contact_.id, contact.getId());
		// there should only be one row in the DB
		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}

	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		Contact contactHome = new Contact();
		contactHome.setAddress( address);
//		contactHome.setPhone( phone);
		contactHome.setEmail( "test@test.com");
		contactHome.setContactType( "Home");
		contactHome.setOwner( person);
		// we expect a failure because phone is part of the composite key
		assertThrows( PersistenceException.class, () -> em.persist( contactHome));
		et.commit();
	}

	@Test
	@Order(4)
	void test04_Read() {
		List< Contact> contacts = getAll(em, Contact.class);
		assertThat( contacts, contains( equalTo( contact)));
	}

	@Test
	@Order(5)
	void test05_ReadDependencies() {
		Contact returnedContact = getWithId(em, Contact.class, ContactPK.class, Contact_.id, contact.getId());

		assertThat( returnedContact.getOwner(), equalTo( person));
		assertThat( returnedContact.getEmail(), equalTo( EMIAL));
		assertThat( returnedContact.getContactType(), equalTo( CONTACT_TYPE));
		assertThat( returnedContact.getPhone(), equalTo( phone));
		assertThat( returnedContact.getAddress(), equalTo( address));
	}

	@Test
	@Order(6)
	void test06_Update() {
		Contact returnedContact = getWithId(em, Contact.class, ContactPK.class, Contact_.id, contact.getId());

		String newEmail = "test3@test3.com";
		String newContactType = "Work";

		et.begin();
		returnedContact.setEmail( newEmail);
		returnedContact.setContactType( newContactType);
		em.merge( returnedContact);
		et.commit();

		returnedContact = getWithId(em, Contact.class, ContactPK.class, Contact_.id, contact.getId());
		assertThat( returnedContact.getEmail(), equalTo( newEmail));
		assertThat( returnedContact.getContactType(), equalTo( newContactType));
	}

	@Test
	@Order(7)
	void test07_UpdateDependencies() {
		Contact returnedContact = getWithId(em, Contact.class, ContactPK.class, Contact_.id, contact.getId());

		phone = returnedContact.getPhone();
		phone.setNumber( "9", "876", "5432100");

		address = returnedContact.getAddress();
		address.setAddress( "7654", "zxcv Dr.E", "Vancouver", "BS", "CA", "Z9Y8X7W");

		person = returnedContact.getOwner();
		person.setFullName( "Jack", "Jackson");

		et.begin();
		returnedContact.setAddress( address);
		returnedContact.setPhone( phone);
		returnedContact.setOwner( person);
		em.merge( returnedContact);
		et.commit();

		returnedContact = getWithId(em, Contact.class, ContactPK.class, Contact_.id, contact.getId());

		assertThat( returnedContact.getOwner(), equalTo( person));
		assertThat( returnedContact.getPhone(), equalTo( phone));
		assertThat( returnedContact.getAddress(), equalTo( address));
	}

	
	@Test
	@Order(8)
	void test08_DeleteDependecy() {
		Contact returnedContact = getWithId(em, Contact.class, ContactPK.class, Contact_.id, contact.getId());

		int addressId = returnedContact.getAddress().getId();

		et.begin();
		returnedContact.setAddress( null);
		em.merge( returnedContact);
		et.commit();

		returnedContact = getWithId(em, Contact.class, ContactPK.class, Contact_.id, contact.getId());

		assertThat( returnedContact.getAddress(), is( nullValue()));

		long result = getCountWithId(em, Address.class, Integer.class, Address_.id, addressId);
		// because it can be null so it is not removed
		assertThat( result, is( equalTo( 1L)));
	}

	@Test
	@Order(9)
	void test09_Delete() {
		Contact returnedContact = getWithId(em, Contact.class, ContactPK.class, Contact_.id, contact.getId());

		et.begin();
		// add another row to db to make sure only the correct row is deleted
		Contact contactHome = new Contact();
		contactHome.setPhone( new Phone().setNumber( "2", "673", "9845385"));
		contactHome.setContactType( "Work");
		contactHome.setOwner( returnedContact.getOwner());
		em.persist( contactHome);
		et.commit();

		et.begin();
		em.remove(returnedContact);
		et.commit();

		long result = getCountWithId(em, Contact.class, ContactPK.class, Contact_.id, returnedContact.getId());
		assertThat( result, is( equalTo( 0L)));

		result = getCountWithId(em, Contact.class, ContactPK.class, Contact_.id, contactHome.getId());
		assertThat( result, is( equalTo( 1L)));
	}
}
