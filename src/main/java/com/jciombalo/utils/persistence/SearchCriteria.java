package com.jciombalo.utils.persistence;

import java.util.List;

public class SearchCriteria {
	
	public static SearchCriteria EQ(final String attributePath, final Object value) {
		return new SearchCriteria(SearchCriteriaType.EQ, attributePath, value);
	}
	
	public static SearchCriteria EQ(final String attributePath, final String value, boolean startingWith, boolean endingWith) {
		return new SearchCriteria(SearchCriteriaType.EQ, attributePath, value, startingWith, endingWith);
	}
	
	public static SearchCriteria GE(final String attributePath, final Object value) {
		return new SearchCriteria(SearchCriteriaType.GE, attributePath, value);
	}
	
	public static SearchCriteria GT(final String attributePath, final Object value) {
		return new SearchCriteria(SearchCriteriaType.GT, attributePath, value);
	}

	public static SearchCriteria IN(final String attributePath, final Object... values) {
		return new SearchCriteria(SearchCriteriaType.IN, attributePath, values);
	}

	public static SearchCriteria IN(final String attributePath, final List<Object> values) {
		return new SearchCriteria(SearchCriteriaType.IN, attributePath, values.toArray(new Object[values.size()]));
	}

	public static SearchCriteria LE(final String attributePath, final Object value) {
		return new SearchCriteria(SearchCriteriaType.LE, attributePath, value);
	}
	
	public static SearchCriteria LT(final String attributePath, final Object value) {
		return new SearchCriteria(SearchCriteriaType.LT, attributePath, value);
	}

	public static SearchCriteria NO(final SearchCriteria innerCriteria) {
		return new SearchCriteria(SearchCriteriaType.NO, innerCriteria);
	}

	public static SearchCriteria AND(final SearchCriteria innerCriteria1, final SearchCriteria innerCriteria2,
			final SearchCriteria... optionalInnerCriterias) {
		if (optionalInnerCriterias == null) {
			return new SearchCriteria(SearchCriteriaType.AND, innerCriteria1, innerCriteria2);
		} else {
			SearchCriteria[] innerCriterias = new SearchCriteria[optionalInnerCriterias.length + 2];
			innerCriterias[0] = innerCriteria1;
			innerCriterias[1] = innerCriteria2;
			System.arraycopy(optionalInnerCriterias, 0, innerCriterias, 2, optionalInnerCriterias.length);
			return new SearchCriteria(SearchCriteriaType.AND, innerCriterias);
		}
	}

	public static SearchCriteria OR(final SearchCriteria innerCriteria1, final SearchCriteria innerCriteria2,
			final SearchCriteria... optionalInnerCriterias) {
		if (optionalInnerCriterias == null) {
			return new SearchCriteria(SearchCriteriaType.OR, innerCriteria1, innerCriteria2);
		} else {
			SearchCriteria[] innerCriterias = new SearchCriteria[optionalInnerCriterias.length + 2];
			innerCriterias[0] = innerCriteria1;
			innerCriterias[1] = innerCriteria2;
			System.arraycopy(optionalInnerCriterias, 0, innerCriterias, 2, optionalInnerCriterias.length);
			return new SearchCriteria(SearchCriteriaType.OR, innerCriterias);
		}
	}
	
	private final SearchCriteriaType operation;
	private final Object[] args;
	
	private SearchCriteria(final SearchCriteriaType operation, final Object... args) {
		super();
		this.operation = operation;
		this.args = args;
	}
	
	private SearchCriteria(final SearchCriteriaType operation, final SearchCriteria[] args) {
		super();
		this.operation = operation;
		this.args = args;
	}

	public SearchCriteriaType getOperation() {
		return operation;
	}

	public Object[] getArgs() {
		return args;
	}
	
}
