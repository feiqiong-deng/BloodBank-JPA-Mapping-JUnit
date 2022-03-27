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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCRUDDonationRecord extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;

	private static Person person;
	private static BloodDonation donation;
	private static DonationRecord record;
	private static final byte TESTED = 1;

	@BeforeAll
	static void setupAllInit() {
		person = new Person();
		person.setFullName( "Teddy", "Yap");
		
		donation = new BloodDonation();
		PrivateBloodBank bank = new PrivateBloodBank();
		BloodType type = new BloodType();

		bank.setName("Health");
		donation.setBank(bank);
		donation.setMilliliters(50);
		type.setType( "A", "+");
		donation.setBloodType(type);
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
		long result = getTotalCount(em, DonationRecord.class);
		assertThat( result, is( comparesEqualTo( 0L)));
	}

	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		
		record = new DonationRecord();
		record.setDonation(donation);
		record.setOwner(person);
		record.setTested(TESTED);
		
		em.persist( person);
		em.persist( record);
		et.commit();

		long result = getCountWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());
		// there should only be one row in the DB
		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}

	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		DonationRecord recordTest = new DonationRecord();
		recordTest.setDonation(donation);
//		record.setOwner(person);
		recordTest.setTested(TESTED);
		// we expect a failure because phone is part of the composite key
		assertThrows( PersistenceException.class, () -> em.persist( recordTest));
		et.commit();
	}

	@Test
	@Order(4)
	void test04_Read() {
		List< DonationRecord> records = getAll(em, DonationRecord.class);
		assertThat( records, contains( equalTo( record)));
	}

	@Test
	@Order(5)
	void test05_ReadDependencies() {
		DonationRecord returnedRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());

		assertThat( returnedRecord.getOwner(), equalTo( person));
		assertThat( returnedRecord.getDonation(), equalTo( donation));
		assertThat( returnedRecord.getTested(), equalTo( TESTED));
	}

	@Test
	@Order(6)
	void test06_Update() {
		DonationRecord returnedRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());

		byte newTested = 0;

		et.begin();
		returnedRecord.setTested( newTested);
		
		em.merge( returnedRecord);
		et.commit();

		returnedRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());
		assertThat( returnedRecord.getTested(), equalTo( newTested));
	}

	@Test
	@Order(7)
	void test07_UpdateDependencies() {
		DonationRecord returnedRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());

		person = returnedRecord.getOwner();
		person.setFullName( "Jack", "Jackson");

		donation = returnedRecord.getDonation();
	
		BloodBank bank = new PublicBloodBank();
		bank.setName("Lucky");
		
		BloodType type = new BloodType();
		type.setType( "O", "-");
		
		donation.setBank(bank);
		donation.setMilliliters(80);
		donation.setBloodType(type);

		et.begin();
		
		returnedRecord.setOwner( person);
		returnedRecord.setDonation(donation);
		
		em.merge( returnedRecord);
		et.commit();

		returnedRecord =getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());

		assertThat( returnedRecord.getOwner(), equalTo( person));
		assertThat( returnedRecord.getDonation(), equalTo( donation));
	}

	
	@Test
	@Order(8)
	void test08_DeleteDependecy() {
		DonationRecord returnedRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());

		int donationId = returnedRecord.getDonation().getId();

		et.begin();
		returnedRecord.setDonation(null);
		em.merge( returnedRecord);
		et.commit();

		returnedRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());

		assertThat( returnedRecord.getDonation(), is( nullValue()));

		long result = getCountWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, donationId);
		// because it can be null so it is not removed
		assertThat( result, is( equalTo( 1L)));
	}

	@Test
	@Order(9)
	void test09_Delete() {
		DonationRecord returnedRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());

		et.begin();
		// add another row to db to make sure only the correct row is deleted
		
		DonationRecord anotherRecord = new DonationRecord();
		
		Person newPerson = new Person();
		newPerson.setFullName( "Robert", "Green");
		em.persist(newPerson);

		BloodDonation newDonation = new BloodDonation();
		PrivateBloodBank bank = new PrivateBloodBank();
		BloodType type = new BloodType();
		
		bank.setName("Apple");
		newDonation.setBank(bank);
		newDonation.setMilliliters(50);
		type.setType( "B", "-");
		newDonation.setBloodType(type);
		
		anotherRecord.setOwner(newPerson);
		anotherRecord.setDonation(newDonation);
		anotherRecord.setTested(TESTED);
		
		em.persist(anotherRecord);
		et.commit();

		et.begin();
		em.remove(returnedRecord);
		et.commit();

		long result = getCountWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, record.getId());
		assertThat( result, is( equalTo( 0L)));

		result = getCountWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id, anotherRecord.getId());
		assertThat( result, is( equalTo( 1L)));
	}

}
