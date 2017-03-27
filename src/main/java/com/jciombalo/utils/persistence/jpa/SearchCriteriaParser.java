package com.jciombalo.utils.persistence.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.jciombalo.utils.persistence.SearchCriteria;
import com.jciombalo.utils.persistence.SearchCriteriaType;

class SearchCriteriaParser {

	public static Predicate[] parse(final SearchCriteria[] criterias, final CriteriaBuilder builder, final Root<?> fromClause) {
		Predicate[] predicates = new Predicate[criterias.length];
		for (int i = 0; i < criterias.length; i++) {
			SearchCriteria searchCriteria = criterias[i];
			CriteriaTypeParser parser = CriteriaTypeParser.getParser(searchCriteria.getOperation());
			predicates[i] = parser.parse(searchCriteria, builder, fromClause);
		}
		return predicates;
	}

	enum CriteriaTypeParser {
		EQ(SearchCriteriaType.EQ) {
			@Override
			public Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause) {
				String attributePath = (String) criteria.getArgs()[0];
				if (criteria.getArgs().length == 2) {
					return builder.equal(this.resolvePath(attributePath, fromClause), criteria.getArgs()[1]);
				} else {
					final String value = (String) criteria.getArgs()[1];
					final Boolean startingWith = (Boolean) criteria.getArgs()[2];
					final Boolean endingWith = (Boolean) criteria.getArgs()[3];
					
					StringBuilder valueBuilder = new StringBuilder();
					if (!startingWith) {
						valueBuilder.append("%");
					}
					valueBuilder.append(value);
					if (!endingWith) {
						valueBuilder.append("%");
					}
					return builder.like(this.<String>resolvePath(attributePath, fromClause), valueBuilder.toString());
				}
			}
		},
		GE(SearchCriteriaType.GE) {
			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause) {
				String attributePath = (String) criteria.getArgs()[0];
				Comparable value = (Comparable) criteria.getArgs()[1];
				return builder.greaterThanOrEqualTo(this.<Comparable>resolvePath(attributePath, fromClause), value);
			}
		},
		GT(SearchCriteriaType.GT) {
			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause) {
				String attributePath = (String) criteria.getArgs()[0];
				Comparable value = (Comparable) criteria.getArgs()[1];
				return builder.greaterThan(this.<Comparable>resolvePath(attributePath, fromClause), value);
			}
		},
		IN(SearchCriteriaType.IN) {
			@Override
			public Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause) {
				final String attributePath = (String) criteria.getArgs()[0];
				final Object[] values = (Object[]) criteria.getArgs()[1];
				return this.resolvePath(attributePath, fromClause).in(values);
			}
		},
		LE(SearchCriteriaType.LE) {
			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause) {
				String attributePath = (String) criteria.getArgs()[0];
				Comparable value = (Comparable) criteria.getArgs()[1];
				return builder.lessThanOrEqualTo(this.<Comparable>resolvePath(attributePath, fromClause), value);
			}
		},
		LT(SearchCriteriaType.LT) {
			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause) {
				String attributePath = (String) criteria.getArgs()[0];
				Comparable value = (Comparable) criteria.getArgs()[1];
				return builder.lessThan(this.<Comparable>resolvePath(attributePath, fromClause), value);
			}
		},
		NO(SearchCriteriaType.NO) {
			@Override
			public Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause) {
				SearchCriteria innerCriteria = (SearchCriteria) criteria.getArgs()[0];
				CriteriaTypeParser innerParser = CriteriaTypeParser.getParser(innerCriteria.getOperation());
				return builder.not(innerParser.parse(innerCriteria, builder, fromClause));
			}
		},
		AND(SearchCriteriaType.AND) {
			@Override
			public Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause) {
				SearchCriteria[] innerCriterias = (SearchCriteria[]) criteria.getArgs();
				Predicate[] predicates = SearchCriteriaParser.parse(innerCriterias, builder, fromClause);
				return builder.and(predicates);
			}
		},
		OR(SearchCriteriaType.OR) {
			@Override
			public Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause) {
				SearchCriteria[] innerCriterias = (SearchCriteria[]) criteria.getArgs();
				Predicate[] predicates = SearchCriteriaParser.parse(innerCriterias, builder, fromClause);
				return builder.or(predicates);
			}
		};

		public static CriteriaTypeParser getParser(final SearchCriteriaType type)
				throws IllegalArgumentException, IllegalStateException {
			if (type == null) {
				throw new IllegalArgumentException("The criteria type must be a not null value");
			}
			for (CriteriaTypeParser parser : CriteriaTypeParser.values()) {
				if (parser.parsedType.equals(type)) {
					return parser;
				}
			}
			throw new IllegalStateException("Cannot find a implementation for type " + type);
		}

		private final SearchCriteriaType parsedType;

		private CriteriaTypeParser(SearchCriteriaType parsedType) {
			this.parsedType = parsedType;
		}

		public abstract Predicate parse(final SearchCriteria criteria, final CriteriaBuilder builder, final Root<?> fromClause);
		
		@SuppressWarnings("unchecked")
		protected <T> Path<T> resolvePath(String attributePath, Root<?> fromClause) {
			String[] pathParts = attributePath.split("\\.");
			@SuppressWarnings("rawtypes")
			Path p = fromClause;
			for (String pathPart : pathParts) {
				p = p.get(pathPart);
			}
			return p;
		}
	}
}
