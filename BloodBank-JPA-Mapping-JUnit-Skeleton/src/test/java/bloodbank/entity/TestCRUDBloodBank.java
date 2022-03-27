package bloodbank.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
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
public class TestCRUDBloodBank extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	
	private static BloodBank privateBank;
	private static BloodBank publicBank;
	private static final String PRIVATENAME = "Health";
	private static final String PUBLICNAME = "Luck";

	@BeforeAll
	static void setupAllInit() {
		privateBank = new PrivateBloodBank();
		publicBank = new PublicBloodBank();
		privateBank.setName(PRIVATENAME);
		publicBank.setName(PUBLICNAME);
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
		Long result = getTotalCount(em, BloodBank.class);
		assertThat( result, is( comparesEqualTo( 0L)));
	}
	
	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		em.persist(privateBank);
		em.persist(publicBank);
		et.commit();

		Long privateResult = getCountWithId(em, BloodBank.class, Integer.class, Person_.id, privateBank.getId());
		Long publicResult = getCountWithId(em, BloodBank.class, Integer.class, Person_.id, publicBank.getId());

		assertThat( privateResult, is( greaterThanOrEqualTo( 1L)));
		assertThat( publicResult, is( greaterThanOrEqualTo( 1L)));

	}
	
	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		BloodBank bankTest = new PrivateBloodBank();
	
		assertThrows( PersistenceException.class, () -> em.persist(bankTest));
		et.commit();
	}
	
	@Test
	@Order(4)
	void test04_Read() {		
		List<BloodBank> banks = getAll(em, BloodBank.class);
		List<BloodBank> expect = Arrays.asList(privateBank, publicBank);
		assertThat( banks, equalTo(expect));
	}
	
	@Test
	@Order(5)
	void test05_ReadAllFields() {
		BloodBank returnedPrivate = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, privateBank.getId());
		BloodBank returnedPublic = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, publicBank.getId());

		assertThat( returnedPrivate.getName(), equalTo(PRIVATENAME));
		assertThat( returnedPublic.getName(), equalTo(PUBLICNAME));

	}
	
	@Test
	@Order(6)
	void test06_Update() {
		BloodBank returnedPrivate = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, privateBank.getId());
		BloodBank returnedPublic = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, publicBank.getId());

		String newPrivateName = "Hope";
		String newPublicName = "Fortune";

		et.begin();
		
		returnedPrivate.setName(newPrivateName);
		returnedPublic.setName(newPublicName);
		em.merge( returnedPrivate);
		em.merge( returnedPublic);

		et.commit();

		returnedPrivate = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, privateBank.getId());
		returnedPublic = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, publicBank.getId());
		
		assertThat(returnedPrivate.getName(), equalTo( newPrivateName));
		assertThat(returnedPublic.getName(), equalTo( newPublicName));
	}
	
	@Test
	@Order(7)
	void test07_Delete() {
		BloodBank returnedPrivate = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, privateBank.getId());
		BloodBank returnedPublic = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, publicBank.getId());

		et.begin();

		BloodBank newPrivateBank = new PrivateBloodBank();
		BloodBank newPublicBank = new PublicBloodBank();
		
		newPrivateBank.setName(PRIVATENAME);
		newPublicBank.setName(PUBLICNAME);
		
		em.persist( newPrivateBank);
		em.persist( newPublicBank);

		et.commit();

		et.begin();
		em.remove(returnedPrivate);
		em.remove(returnedPublic);
		et.commit();

		long resultPrivate = getCountWithId(em, BloodBank.class, Integer.class, BloodBank_.id, privateBank.getId());
		assertThat( resultPrivate, is( equalTo( 0L)));
		
		long resultPublic = getCountWithId(em, BloodBank.class, Integer.class, BloodBank_.id, publicBank.getId());
		assertThat( resultPublic, is( equalTo( 0L)));

		resultPrivate = getCountWithId(em, BloodBank.class, Integer.class, BloodBank_.id, newPrivateBank.getId());
		resultPublic = getCountWithId(em, BloodBank.class, Integer.class, BloodBank_.id, newPublicBank.getId());
		
		assertThat( resultPrivate, is( equalTo( 1L)));
		assertThat( resultPublic, is( equalTo( 1L)));

	}

}
