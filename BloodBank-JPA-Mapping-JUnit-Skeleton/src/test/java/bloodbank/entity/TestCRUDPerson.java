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
public class TestCRUDPerson extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	
	private static Person person;
	private static final String FIRSTNAME = "Amy";
	private static final String LASTNAME = "Young";
	
	@BeforeAll
	static void setupAllInit() {
		person = new Person();
		person.setFirstName(FIRSTNAME);
		person.setLastName(LASTNAME);
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
		Long result = getTotalCount(em, Person.class);
		assertThat( result, is( comparesEqualTo( 0L)));
	}
	
	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		em.persist(person);
		et.commit();

		Long result = getCountWithId(em, Person.class, Integer.class, Person_.id, person.getId());

		assertThat( result, is( greaterThanOrEqualTo( 1L)));
	}
	
	
	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		Person personTest = new Person();
		personTest.setFirstName(FIRSTNAME);
		assertThrows( PersistenceException.class, () -> em.persist(personTest));
		et.commit();
	}
	
	@Test
	@Order(4)
	void test04_Read() {		
		List<Person> persons = getAll(em, Person.class);
		assertThat(persons, contains( equalTo(person)));
	}
	
	@Test
	@Order(5)
	void test05_ReadAllFields() {
		Person returnedPerson = getWithId(em, Person.class, Integer.class, Person_.id, person.getId());

		assertThat( returnedPerson.getFirstName(), equalTo(FIRSTNAME));
		assertThat( returnedPerson.getLastName(), equalTo(LASTNAME));

	}
	
	@Test
	@Order(6)
	void test06_Update() {
		Person returnedPerson = getWithId(em, Person.class, Integer.class, Person_.id, person.getId());

		String newFirstName = "Lily";
		String newLastName = "Anderson";

		et.begin();
		
		returnedPerson.setFirstName(newFirstName);
		returnedPerson.setLastName(newLastName);
		em.merge( returnedPerson);
		et.commit();

		returnedPerson = getWithId(em, Person.class, Integer.class, Person_.id, person.getId());

		assertThat(returnedPerson.getFirstName(), equalTo( newFirstName));
		assertThat(returnedPerson.getLastName(), equalTo( newLastName));
	}
	
	@Test
	@Order(7)
	void test07_Delete() {
		Person returnedPerson = getWithId(em, Person.class, Integer.class, Person_.id, person.getId());

		et.begin();

		Person personTest = new Person();
		
		String newFirstName = "Alice";
		String newLastName = "Wong";
		
		personTest.setFirstName(newFirstName);
		personTest.setLastName(newLastName);
		
		em.persist( personTest);
		et.commit();

		et.begin();
		em.remove(returnedPerson);
		et.commit();

		long result = getCountWithId(em, Person.class, Integer.class, Person_.id, person.getId());
		assertThat( result, is( equalTo( 0L)));

		result = getCountWithId(em, Person.class, Integer.class, Person_.id, personTest.getId());
		assertThat( result, is( equalTo( 1L)));
	}

}
