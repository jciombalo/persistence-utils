package com.jciombalo.utils.persistence.search;

import java.util.List;

public class Search {

	private final List<Selection> selections;
	private final List<Condition> conditions;
	private final List<Sorting> sortings;
	private final List<Grouping> groupings;
	private final Integer fromIndex;
	private final Integer maxLength;

	public Search(List<Selection> selections, List<Condition> conditions, List<Sorting> sortings,
			List<Grouping> groupings, Integer fromIndex, Integer maxLength) {
		super();
		this.selections = selections;
		this.conditions = conditions;
		this.sortings = sortings;
		this.groupings = groupings;
		this.fromIndex = fromIndex;
		this.maxLength = maxLength;
	}

	public List<Selection> getSelections() {
		return selections;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public List<Sorting> getSortings() {
		return sortings;
	}

	public List<Grouping> getGroupings() {
		return groupings;
	}

	public Integer getFromIndex() {
		return fromIndex;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

}
