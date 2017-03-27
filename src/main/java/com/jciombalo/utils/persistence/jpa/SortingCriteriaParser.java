package com.jciombalo.utils.persistence.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.jciombalo.utils.persistence.SortingCriteria;

class SortingCriteriaParser {
	public static Order[] parse(final SortingCriteria[] criterias, final CriteriaBuilder builder,
			final Root<?> fromClause) {
		Order[] sortings = new Order[criterias.length];
		for (int i = 0; i < criterias.length; i++) {
			SortingCriteria criteria = criterias[i];
			if (criteria.isDesc()) {
				sortings[i] = builder.desc(fromClause.get(criteria.getAttributePath()));
			} else {
				sortings[i] = builder.asc(fromClause.get(criteria.getAttributePath()));
			}
		}
		return sortings;
	}
}
