package com.jciombalo.utils.persistence.search;

import java.util.Arrays;
import java.util.List;

import com.jciombalo.utils.persistence.search.typed.TypedPath;

public class SearchBuilder<T> {

	private final Class<T> entityType;
	private Integer firstResult;
	private Integer maxResults;
	private Path[] selectClause;
	private Condition whereClause;
	private Sorting[] sortingClause;

	public static <T> SearchBuilder<T> from(Class<T> entityType) {
		return new SearchBuilder<>(entityType);
	}

	public SearchBuilder<T> select(Path... selections) {
		this.selectClause = selections;
		return this;
	}

	public SearchBuilder<T> where(Condition c) {
		this.whereClause = c;
		return this;
	}

	public SearchBuilder<T> sort(Sorting... s) {
		this.sortingClause = s;
		return this;
	}

	public Search build() {
		List<Path> selections = null;
		if (this.selectClause != null && this.selectClause.length > 0) {
			selections = Arrays.asList(this.selectClause);
		}
		List<Sorting> sortings = null;
		if (this.sortingClause != null && this.sortingClause.length > 0) {
			sortings = Arrays.asList(this.sortingClause);
		}
		return new Search(selections, this.whereClause, sortings, this.firstResult, this.maxResults);
	}

	public static void main(String[] args) {
		SearchBuilder.from(Search.class).select(TypedPath.path(Search::getSelections))
				.where(TypedPath.path(Search::getFromIndex).eq("1").negated())
				.sort(TypedPath.path(Search::getSelections).asc()).build();
	}

	protected SearchBuilder(Class<T> entityType) {
		super();
		this.entityType = entityType;
	}
}
