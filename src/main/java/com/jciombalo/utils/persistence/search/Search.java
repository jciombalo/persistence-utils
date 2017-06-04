package com.jciombalo.utils.persistence.search;

import java.util.List;

public class Search {

	private final List<Path> selections;
	private final Condition conditions;
	private final List<Sorting> sortings;
	private final Integer firstResult;
	private final Integer maxResults;

	public Search(List<Path> selections, Condition condition, List<Sorting> sortings,
			Integer firstResult, Integer maxResults) {
		super();
		this.selections = selections;
		this.conditions = condition;
		this.sortings = sortings;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public List<Path> getSelections() {
		return selections;
	}

	public Condition getConditions() {
		return conditions;
	}

	public List<Sorting> getSortings() {
		return sortings;
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

}
