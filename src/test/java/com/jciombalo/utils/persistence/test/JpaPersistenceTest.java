package com.jciombalo.utils.persistence.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jciombalo.utils.persistence.Repository;
import com.jciombalo.utils.persistence.Search;
import com.jciombalo.utils.persistence.SearchBuilder;
import com.jciombalo.utils.persistence.SearchCriteria;
import com.jciombalo.utils.persistence.SearchResult;
import com.jciombalo.utils.persistence.SortingCriteria;
import com.jciombalo.utils.persistence.jpa.JpaRepository;
import com.jciombalo.utils.persistence.test.entity.Person;
import com.jciombalo.utils.persistence.test.entity.Skill;

public class JpaPersistenceTest extends JpaEMInjector {
	
	private static final Skill scientist = new Skill(1, "Scientist");
	private static final Skill worldLeader = new Skill(2, "World Leader");
	
	private static final Integer maxAge = 100;
	private static final Person[] initialPersons = {
			new Person(1, "Charles Darwin", ThreadLocalRandom.current().nextInt(0, maxAge + 1), scientist),
			new Person(2, "Albert Einstein", ThreadLocalRandom.current().nextInt(0, maxAge + 1), scientist),
			new Person(3, "Mahatma Gandhi", ThreadLocalRandom.current().nextInt(0, maxAge + 1), worldLeader),
			new Person(4, "Nelson Mandela", ThreadLocalRandom.current().nextInt(0, maxAge + 1), worldLeader)
	};
	private static final String containingName = "AND";
	
	private static void assertSamePersonInfo(final Person expected, final Person actual) {
		Assert.assertNotNull(actual);
    	Assert.assertEquals(expected.getId(), actual.getId());
    	Assert.assertEquals(expected.getName(), actual.getName());
    	Assert.assertEquals(expected.getAge(), actual.getAge());
    	Assert.assertEquals(expected.getSkill().getId(), actual.getSkill().getId());
    	Assert.assertEquals(expected.getSkill().getName(), actual.getSkill().getName());
	}
	
	private static void assertOriginalPersonList(final Collection<Person> persons) {
		List<Person> initialPersons = Arrays.asList(JpaPersistenceTest.initialPersons);
		Assert.assertTrue(initialPersons.containsAll(persons));
		
		for (Person person : persons) {
			Person original = initialPersons.get(initialPersons.indexOf(person));
			assertSamePersonInfo(original, person);
		}
	}
	
	private Repository<Person> repo;
	
    @Before
    public void preparePersistenceTest() throws Exception {
    	em.clear();
    	em.getTransaction().begin();
    	em.createQuery("Delete from Person p").executeUpdate();
    	em.createQuery("Delete from Skill s").executeUpdate();
    	em.persist(scientist);
    	em.persist(worldLeader);
    	for (Person person : initialPersons) {
			em.persist(person);
		}
    	em.getTransaction().commit();
    	
    	this.repo = new JpaRepository<Person>(em, Person.class);
    }
    
    @Test
    public void givenNewEntity_whenCreating_thenShouldPersistIt() {
    	// given
		final Person p = new Person(initialPersons.length + 1, "Napoleon Bonaparte",
				ThreadLocalRandom.current().nextInt(0, maxAge + 1), worldLeader);
    	// when
    	em.getTransaction().begin();
    	this.repo.create(p);
    	em.getTransaction().commit();
    	// then
    	assertSamePersonInfo(p, em.find(Person.class, p.getId()));
    }
    
    @Test
    public void givenId_whenRetrieving_thenShouldReturnEntity() {
    	// given
    	final Person person = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length)];
    	// when
    	final Person retrieved = this.repo.retrieve(person.getId());
    	// then
    	assertSamePersonInfo(person, retrieved);
    }
    
    @Test
    public void givenChangedEntity_whenUpdating_shouldPersistNewInfo() {
    	// given
    	final Integer newAge = ThreadLocalRandom.current().nextInt(0, maxAge + 1);
    	final Person p = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length)];
    	p.setAge(newAge);
    	// when
    	em.getTransaction().begin();
    	this.repo.update(p);
    	em.getTransaction().commit();
    	// then
    	Assert.assertEquals(newAge, em.find(Person.class, p.getId()).getAge());
    	@SuppressWarnings("unchecked")
		final List<Person> remaining = em.createQuery("Select p from Person p Where id <>"+p.getId()).getResultList();
    	assertOriginalPersonList(remaining);
    }
    
    @Test
    public void givenExistingId_whenDeleting_shouldRemoveEntity() {
    	// given
    	final Person p = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length)];
    	// when
    	em.getTransaction().begin();
    	this.repo.delete(p.getId());
    	em.getTransaction().commit();
    	// then
    	@SuppressWarnings("unchecked")
		final List<Person> remaining = em.createQuery("Select p from Person p").getResultList();
    	Assert.assertFalse(remaining.contains(p));
    	assertOriginalPersonList(remaining);
    }
    
    @Test
    public void givenExistingEntity_whenDeleting_shouldRemoveIt() {
    	// given
    	final Person p = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length)];
    	// when
    	em.getTransaction().begin();
    	this.repo.delete(p);
    	em.getTransaction().commit();
    	// then
    	@SuppressWarnings("unchecked")
		final List<Person> remaining = em.createQuery("Select p from Person p").getResultList();
    	Assert.assertFalse(remaining.contains(p));
    	Assert.assertEquals(initialPersons.length - 1, remaining.size());
    	assertOriginalPersonList(remaining);
    }
    
    @Test
    public void givenNoSearchDetails_whenSearching_shouldFindAll() {
    	// given
    	final Search noSearchDetails = null;
    	// when
    	final SearchResult<Person> result = this.repo.search(noSearchDetails);
    	// then
    	Assert.assertEquals(initialPersons.length, result.getItemsFound());
    	assertOriginalPersonList(result.getData());
    }
    
    @Test
    public void givenListOfAttributes_whenSearching_shouldReturnOnlyItsValues() {
    	// given
    	final Search attributeSelection = SearchBuilder.selectAttributes("id", "name").build();
    	// when
    	final SearchResult<Person> result = this.repo.search(attributeSelection);
    	// then
    	Assert.assertEquals(initialPersons.length, result.getItemsFound());
    	for (Person person : result.getData()) {
			Assert.assertNotNull(person.getId());
			Assert.assertNotNull(person.getName());
			Assert.assertNull(person.getAge());
			Assert.assertNull(person.getSkill());
		}
    }
    
    @Test 
    public void givenPageNumberAndSize_whenSearching_shouldPaginateTheResult() {
    	// given
    	final int pageSize = ThreadLocalRandom.current().nextInt(1, initialPersons.length);
    	final int lastPageSize = initialPersons.length % pageSize;
    	final int maxPageNumber = (initialPersons.length / pageSize) + (lastPageSize > 0 ? 1 : 0);
    	final int pageNumber = ThreadLocalRandom.current().nextInt(1, maxPageNumber + 1);
    	final int expectedPageSize =  pageNumber <= initialPersons.length / pageSize ? pageSize : lastPageSize;
    	
    	// when
    	final Search paginatedSearch = SearchBuilder.selectPage(pageNumber, pageSize).build();
    	final SearchResult<Person> result = this.repo.search(paginatedSearch);
    	// then
    	Assert.assertEquals(initialPersons.length, result.getItemsFound());
    	Assert.assertEquals(pageNumber, result.getCurrentPage());
    	Assert.assertEquals(maxPageNumber, result.getPagesFound());
    	Assert.assertEquals(expectedPageSize, result.getData().size());
    }
   
    @Test
    public void givenSortingAndMaxResults_whenSearching_shouldRetrieveTopRankedEntities() {
    	// given
    	final int maxResults = 1;
    	Person oldestPerson = null;
    	for (Person person : initialPersons) {
			if (oldestPerson == null || oldestPerson.getAge().compareTo(person.getAge()) < 0) {
				oldestPerson = person;
			}
		}
    	// when
    	final Search topRankedSearch = SearchBuilder.selectFirst(maxResults).sortBy(SortingCriteria.DESC("age")).build();
    	final SearchResult<Person> result = this.repo.search(topRankedSearch);
    	// then
    	Assert.assertEquals(initialPersons.length, result.getItemsFound());
    	Assert.assertEquals(maxResults, result.getData().size());
    	assertSamePersonInfo(oldestPerson, result.getData().iterator().next());
    }
    
    @Test 
    public void givenValue_whenSearchingEquals_shouldFindEntitiesExactlyMatching() {
    	// given
    	final Skill skill = scientist;
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getSkill().equals(skill)) {
				expectedResult.add(person);
			}
		}
    	// when
    	final Search skilledPeopleSearch = SearchBuilder
    			.selectAll()
    			.where(SearchCriteria.EQ("skill.name", skill.getName()))
    			.build();
    	final SearchResult<Person> result = this.repo.search(skilledPeopleSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    }
    
    @Test 
    public void givenPartialValue_whenSearchingEquals_shouldFindEntitiesPartialMatching() {
    	// given
    	final String partialName = containingName;
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getName().contains(partialName)) {
				expectedResult.add(person);
			}
		}
    	
    	// when
    	final Search partialEqualsSearch = SearchBuilder
    			.selectAll()
    			.where(SearchCriteria.EQ("name", partialName, false, false))
    			.build();
    	final SearchResult<Person> result = this.repo.search(partialEqualsSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    }
    
    @Test
    public void givenValue_whenSearchingGreaterOrEqual_shouldFindEntitiesMatchingTheCondition() {
    	// given
    	final Person p = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length)];
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getAge().compareTo(p.getAge()) >= 0) {
				expectedResult.add(person);
			}
		}
    	// when
    	final Search greaterOrEqualSearch = SearchBuilder.selectAll().where(SearchCriteria.GE("age", p.getAge())).build();
    	final SearchResult<Person> result = this.repo.search(greaterOrEqualSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(result.getData().contains(p));
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    }
    
    @Test
    public void givenValue_whenSearchingGreater_shouldFindEntitiesMatchingTheCondition() {
    	// given
    	final Person p = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length)];
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getAge().compareTo(p.getAge()) > 0) {
				expectedResult.add(person);
			}
		}
    	// when
    	final Search greaterThanSearch = SearchBuilder.selectAll().where(SearchCriteria.GT("age", p.getAge())).build();
    	final SearchResult<Person> result = this.repo.search(greaterThanSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    }
    
    @Test
    public void givenListOfValues_whenSearchingIn_shouldFindEntitiesMatchingTheCondition() {
    	// given
    	final Person p1 = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length / 2)];
    	final Person p2 = initialPersons[ThreadLocalRandom.current().nextInt(initialPersons.length / 2, initialPersons.length)];
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getAge().compareTo(p1.getAge()) == 0 || person.getAge().compareTo(p2.getAge()) == 0) {
				expectedResult.add(person);
			}
		}
    	// when
    	final Search inSearch = SearchBuilder.selectAll().where(SearchCriteria.IN("age", p1.getAge(), p2.getAge())).build();
    	final SearchResult<Person> result = this.repo.search(inSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    }
    
    @Test
    public void givenValue_whenSearchingLowerOrEqual_shouldFindEntitiesMatchingTheCondition() {
    	// given
    	final Person p = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length)];
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getAge().compareTo(p.getAge()) <= 0) {
				expectedResult.add(person);
			}
		}
    	// when
    	final Search lowerOrEqualSearch = SearchBuilder.selectAll().where(SearchCriteria.LE("age", p.getAge())).build();
    	final SearchResult<Person> result = this.repo.search(lowerOrEqualSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(result.getData().contains(p));
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    }
    
    @Test
    public void givenValue_whenSearchingLower_shouldFindEntitiesMatchingTheCondition() {
    	// given
    	final Person p = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length)];
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getAge().compareTo(p.getAge()) < 0) {
				expectedResult.add(person);
			}
		}
    	// when
    	final Search lowerSearch = SearchBuilder.selectAll().where(SearchCriteria.LT("age", p.getAge())).build();
    	final SearchResult<Person> result = this.repo.search(lowerSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    }
    
    @Test
    public void givenValue_whenSearchingNotEquals_shouldFindEntitiesMatchingTheCondition() {
    	// given
    	final Person p = initialPersons[ThreadLocalRandom.current().nextInt(0, initialPersons.length)];
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getAge().compareTo(p.getAge()) != 0) {
				expectedResult.add(person);
			}
		}
    	// when
    	final Search negatedSearch = SearchBuilder
    			.selectAll()
    			.where(SearchCriteria.NO(SearchCriteria.EQ("age", p.getAge())))
    			.build();
    	final SearchResult<Person> result = this.repo.search(negatedSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    	Assert.assertFalse(result.getData().contains(p));
    }
    
    @Test
    public void givenTwoConditions_whenSearchingBoth_shouldFindEntitiesMatchingTheCondition() {
    	// given
    	final Skill skill = scientist;
    	final Integer minAge = ThreadLocalRandom.current().nextInt(0, maxAge + 1);
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getAge().compareTo(minAge) >= 0 && person.getSkill().equals(skill)) {
				expectedResult.add(person);
			}
		}
    	
    	// The AND conjunction can be implemented in two different ways. 
    	// We'll test both to ensure scenario is valid, no matter the method used.
    	
    	// when
    	final Search usualAndSearch = SearchBuilder
    			.selectAll()
    			.where(SearchCriteria.GE("age", minAge))
    			.and(SearchCriteria.EQ("skill", skill))
    			.build();
    	final SearchResult<Person> result = this.repo.search(usualAndSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    	
    	// when (alternative way)
    	final Search alternativeAndSearch = SearchBuilder
    			.selectAll()
    			.where(SearchCriteria.AND(
    					SearchCriteria.GE("age", minAge), 
    					SearchCriteria.EQ("skill", skill)))
    			.build();
    	final SearchResult<Person> alternativeResult = this.repo.search(alternativeAndSearch);
    	// then (alternative way)
    	Assert.assertEquals(expectedResult.size(), alternativeResult.getItemsFound());
    	Assert.assertTrue(expectedResult.containsAll(alternativeResult.getData()));
    }
    
    @Test
    public void givenTwoConditions_whenSearchingLeastOne_shouldFindEntitiesMatchingTheCondition() {
    	// given
    	final Skill skill = scientist;
    	final Integer maxAge = ThreadLocalRandom.current().nextInt(0, JpaPersistenceTest.maxAge + 1);
    	
    	List<Person> expectedResult = new ArrayList<Person>();
    	for (Person person : initialPersons) {
			if (person.getAge().compareTo(maxAge) < 0 || person.getSkill().equals(skill)) {
				expectedResult.add(person);
			}
		}
    	
    	// when
    	final Search orSearch = SearchBuilder
    			.selectAll()
    			.where(SearchCriteria.OR(
    					SearchCriteria.LT("age", maxAge), 
    					SearchCriteria.EQ("skill", skill)))
    			.build();
    	final SearchResult<Person> result = this.repo.search(orSearch);
    	// then
    	Assert.assertEquals(expectedResult.size(), result.getItemsFound());
    	Assert.assertTrue(expectedResult.containsAll(result.getData()));
    }
    
}
