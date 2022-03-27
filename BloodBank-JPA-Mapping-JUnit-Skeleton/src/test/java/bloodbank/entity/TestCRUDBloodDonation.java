package bloodbank.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
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
public class TestCRUDBloodDonation extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;

	private static BloodDonation donation;
	private static BloodBank bank;
	private static int MILLILITERS = 50;
	private static BloodType type;

	@BeforeAll
	static void setupAllInit() {
		donation = new BloodDonation();
		bank = new PrivateBloodBank();
		type = new BloodType();

		bank.setName("Health");
		type.setType( "A", "+");
		
		donation.setBank(bank);
		donation.setMilliliters(MILLILITERS);
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
		long result = getTotalCount(em, BloodDonation.class);
		assertThat( result, is( comparesEqualTo( 0L)));
	}

	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		
		em.persist( donation);
		et.commit();

		long result = getCountWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, donation.getId());
		// there should only be one row in the DB
		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}

	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		BloodDonation donationTest = new BloodDonation();
		donationTest.setMilliliters(MILLILITERS);
		donationTest.setBloodType(type);
		// we expect a failure because phone is part of the composite key
		assertThrows( PersistenceException.class, () -> em.persist( donationTest));
		et.commit();
	}

	@Test
	@Order(4)
	void test04_Read() {
		List< BloodDonation> donations = getAll(em, BloodDonation.class);
		assertThat( donations, contains( equalTo( donation)));
	}

	@Test
	@Order(5)
	void test05_ReadDependencies() {
		BloodDonation returnedDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, donation.getId());

		assertThat( returnedDonation.getBank(), equalTo( bank));
		assertThat( returnedDonation.getMilliliters(), equalTo(MILLILITERS));
		assertThat( returnedDonation.getBloodType(), equalTo( type));
	}

	@Test
	@Order(6)
	void test06_Update() {
		BloodDonation returnedDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, donation.getId());

		int newMilliliters = 80;
		
		BloodType newType = new BloodType();
		newType.setType( "O", "-");

		et.begin();
		returnedDonation.setMilliliters(newMilliliters);
		returnedDonation.setBloodType(newType);
		
		em.merge( returnedDonation);
		et.commit();

		returnedDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, donation.getId());
		assertThat( returnedDonation.getMilliliters(), equalTo( newMilliliters));
		assertThat( returnedDonation.getBloodType(), equalTo( newType));

	}

	@Test
	@Order(7)
	void test07_UpdateDependencies() {
		BloodDonation returnedDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, donation.getId());

		bank = returnedDonation.getBank();
		bank.setName("Lucky");

		et.begin();
		returnedDonation.setBank(bank);	
		em.merge( returnedDonation);
		et.commit();

		returnedDonation =getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, donation.getId());

		assertThat( returnedDonation.getBank(), equalTo( bank));
	}

	@Test
	@Order(8)
	void test08_Delete() {
		BloodDonation returnedDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, donation.getId());

		et.begin();
		// add another row to db to make sure only the correct row is deleted		
		BloodDonation newDonation = new BloodDonation();	
		BloodBank newBank = new PublicBloodBank();
		BloodType newType = new BloodType();

		newBank.setName("Wealth");
		newType.setType( "O", "-");
		
		newDonation.setBank(newBank);
		newDonation.setMilliliters(MILLILITERS);
		newDonation.setBloodType(newType);
		
		em.persist(newDonation);
		et.commit();

		et.begin();
		em.remove(returnedDonation);
		et.commit();

		long result = getCountWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, donation.getId());
		assertThat( result, is( equalTo( 0L)));

		result = getCountWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id, newDonation.getId());
		assertThat( result, is( equalTo( 1L)));
	}

}
