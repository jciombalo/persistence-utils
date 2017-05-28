package com.jciombalo.utils.persistence.search;

import java.util.List;

public class Search {

	private final List<Path> selections;
	private final Condition conditions;
	private final List<Sorting> sortings;
	private final Integer fromIndex;
	private final Integer maxLength;

	public Search(List<Path> selections, Condition condition, List<Sorting> sortings,
			Integer fromIndex, Integer maxLength) {
		super();
		this.selections = selections;
		this.conditions = condition;
		this.sortings = sortings;
		this.fromIndex = fromIndex;
		this.maxLength = maxLength;
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

	public Integer getFromIndex() {
		return fromIndex;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

}
