package com.jciombalo.utils.persistence.search;

import java.util.Arrays;
import java.util.List;

public class SearchBuilder<T> {

	private Integer firstResult;
	private Integer maxResults;
	private Path[] selectClause;
	private Condition whereClause;
	private Sorting[] sortingClause;

	public static <T> SearchBuilder<T> selectAll() {
		return new SearchBuilder<T>(null, null, null);
	}

	public static <T> SearchBuilder<T> selectFirst(Integer firstResult) {
		return new SearchBuilder<T>(firstResult, null, null);
	}

	public static <T> SearchBuilder<T> selectMax(Integer maxResults) {
		return new SearchBuilder<T>(null, maxResults, null);
	}

	public static <T> SearchBuilder<T> selectPage(Integer firstResult, Integer maxResults) {
		return new SearchBuilder<T>(firstResult, maxResults, null);
	}

	public static <T> SearchBuilder<T> select(Path... selections) {
		return new SearchBuilder<T>(null, null, selections);
	}

	public static <T> SearchBuilder<T> select(Integer firstResult, Integer maxResults, Path... selections) {
		return new SearchBuilder<T>(firstResult, maxResults, selections);
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

	protected SearchBuilder(Integer firstResult, Integer maxResults, Path[] selectClause) {
		super();
		this.firstResult = firstResult;
		this.maxResults = maxResults;
		this.selectClause = selectClause;
	}

}
