package com.jciombalo.utils.persistence.search;

import com.jciombalo.utils.persistence.search.typed.TypedPath;

public class SearchBuilder<T> {
	
	Class<T> entityType;

	public static <T> SearchBuilder<T> from(Class<T> entityType) {
		return new SearchBuilder<>(entityType);
	}

	public SearchBuilder<T> select(Path... paths) {
		return this;
	}
	
	public SearchBuilder<T> where(Condition c) {
		return this;
	}
	
	public SearchBuilder<T> sort(Sorting... s) {
		return this;
	}
	
	public static void main(String[] args) {
		SearchBuilder.from(Search.class)
				.select(new TypedPath(Search::getSelections).path(String::chars))
				.where(new TypedPath(Search::getFromIndex).eq("1").or(new TypedPath(Search::getMaxLength).ge(10).negated()))
				.sort(new TypedPath(Search::getSelections).asc())
				;
	}
	
	protected SearchBuilder(Class<T> entityType) {
		super();
		this.entityType = entityType;
	}
}
