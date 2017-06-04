package com.jciombalo.utils.persistence.search.test;

import com.jciombalo.utils.persistence.search.Search;
import com.jciombalo.utils.persistence.search.SearchBuilder;
import com.jciombalo.utils.persistence.search.typed.TypedPath;
import com.jciombalo.utils.persistence.test.entity.Person;

public class TypedSearchTest {

	public void test() {
		Search s = SearchBuilder
				.select(TypedPath.path(Person::getName), TypedPath.path(Person::getAge))
				.where(TypedPath.path(Person::getName).like("an"))
				.sort(TypedPath.path(Person::getName).asc())
				.build();
		
	}
}
