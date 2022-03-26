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

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCRUDPhone extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	
	private static Phone phone;
	private static final String COUTRYCODE = "01";
	private static final String AREACODE = "613";
	private static final String NUMBER = "1234567";
	
	@BeforeAll
	static void setupAllInit() {
		phone = new Phone();
		phone.setCountryCode(COUTRYCODE);
		phone.setAreaCode(AREACODE);
		phone.setNumber(NUMBER);
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
		Long result = getTotalCount(em, Phone.class);
		assertThat( result, is( comparesEqualTo( 0L)));
	}
	
	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		em.persist(phone);
		et.commit();

		Long result = getCountWithId(em, Phone.class, Integer.class, Phone_.id, phone.getId());

		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}
	
	
	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		Phone phoneTest = new Phone();
		phoneTest.setCountryCode(COUTRYCODE);
		phoneTest.setAreaCode(AREACODE);
		assertThrows( PersistenceException.class, () -> em.persist(phoneTest));
		et.commit();
	}
	
	@Test
	@Order(4)
	void test04_Read() {		
		List<Phone> phones = getAll(em, Phone.class);
		assertThat( phones, contains( equalTo( phone)));
	}
	
	@Test
	@Order(5)
	void test05_ReadAllFields() {
		Phone returnedPhone= getWithId(em, Phone.class, Integer.class, Phone_.id, phone.getId());

		assertThat( returnedPhone.getCountryCode(), equalTo( COUTRYCODE));
		assertThat( returnedPhone.getAreaCode(), equalTo( AREACODE));
		assertThat( returnedPhone.getNumber(), equalTo( NUMBER));

	}
	
	@Test
	@Order(6)
	void test06_Update() {
		Phone returnedPhone= getWithId(em, Phone.class, Integer.class, Phone_.id, phone.getId());

		String newCountryCode = "123";
		String newAreaCode = "321";
		String newNumber = "9876543";

		et.begin();
		
		returnedPhone.setCountryCode(newCountryCode);
		returnedPhone.setAreaCode(newAreaCode);
		returnedPhone.setNumber(newNumber);
		em.merge( returnedPhone);
		et.commit();

		returnedPhone = getWithId(em, Phone.class, Integer.class, Phone_.id, phone.getId());

		assertThat(returnedPhone.getCountryCode(), equalTo( newCountryCode));
		assertThat(returnedPhone.getAreaCode(), equalTo( newAreaCode));
		assertThat(returnedPhone.getNumber(), equalTo( newNumber));
	}
	
	@Test
	@Order(7)
	void test07_Delete() {
		Phone returnedPhone= getWithId(em, Phone.class, Integer.class, Phone_.id, phone.getId());

		et.begin();

		Phone phoneTest = new Phone();
		
		String newCountryCode = "666";
		String newAreaCode = "999";
		String newNumber = "1234567";
		
		phoneTest.setCountryCode(newCountryCode);
		phoneTest.setAreaCode(newAreaCode);
		phoneTest.setNumber(newNumber);
		
		em.persist( phoneTest);
		et.commit();

		et.begin();
		em.remove(returnedPhone);
		et.commit();

		long result = getCountWithId(em, Phone.class, Integer.class, Phone_.id, phone.getId());
		assertThat( result, is( equalTo( 0L)));

		result = getCountWithId(em, Phone.class, Integer.class, Phone_.id, phoneTest.getId());
		assertThat( result, is( equalTo( 1L)));
	}
}
