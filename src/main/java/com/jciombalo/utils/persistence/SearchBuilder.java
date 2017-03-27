package com.jciombalo.utils.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Fluent API that aims to helps on the creation of new {@link Search search}
 * criterias.
 * </p>
 * <p>
 * New search criterias are defined by firstly choosing one of the static
 * <code>select(...)</code> methods available. After that, the newly
 * instantiated builder can be used for the definition of filtering and sorting
 * conditions, being these last steps optional.
 * </p>
 * <p>
 * Once specified all the conditions, the search can be built using the
 * {@link #build() build()} method, it will create a new {@link Search} object
 * that can then be submitted to the {@link Repository#search(Search) search}
 * operation of a {@link Repository}.
 * </p>
 * <p>
 * Usage example:
 * 
 * <pre>
 * Repository&lt;Person&gt; repo = ...;
 * Search top10ByAgeAndSkill = SearchBuilder.selectFirst(10)
 *         .where(SearchCriteria.GE("age", 21))
 *         .and(SearchCriteria.EQ("skill.name", "scientist"))
 *         .sortBy(SortingCriteria.DESC("name"))
 *         .build();
 * SearchResult&lt;Person&gt; result = repo.search(top10ByAgeAndSkill);
 * </pre>
 * </p>
 * 
 * @author jorge.ciombalo
 *
 * @see Search
 * @see Repository
 */
public class SearchBuilder {

	private String[] attributes;
	private List<SearchCriteria> searchCriterias;
	private List<SortingCriteria> sortingCriterias;
	private Integer maxResults;
	private Integer pageNumber;

	/**
	 * Creates a new <code>SearchBuilder</code> object for a search that should
	 * retrieve all the attributes from all the entities matching the search.
	 * 
	 * @return the newly created <code>SearchBuilder</code> object.
	 */
	public static SearchBuilder selectAll() {
		return new SearchBuilder();
	}

	/**
	 * Creates a new <code>SearchBuilder</code> object for a search that should
	 * retrieve only the specified attributes, from all the entities matching
	 * the search. At least one attribute must be specified for this kind of
	 * search.
	 * 
	 * @param attribute
	 *            Not-null, not-empty name of the attribute to be retrieved.
	 * @param additionalAttributes
	 *            Additional attributes to be retrieved (optional).
	 * @return the newly created <code>SearchBuilder</code> object.
	 * @throws IllegalArgumentException
	 *             If <code>attribute</code> is null or empty.
	 */
	public static SearchBuilder selectAttributes(final String attribute, final String... additionalAttributes)
			throws IllegalArgumentException {
		SearchBuilder builder = new SearchBuilder();
		builder.attributes = validateAndConcatAttributes(attribute, additionalAttributes);
		return builder;
	}

	/**
	 * Creates a new <code>SearchBuilder</code> object for a search that should
	 * retrieve all the attributes from the first <code>maxResults</code>
	 * entities matching the search.
	 * 
	 * @param maxResults
	 *            Not-null, positive number of the max results that should be
	 *            retrieved.
	 * @return the newly created <code>SearchBuilder</code> object.
	 * @throws IllegalArgumentException
	 *             If <code>maxResults</code> is null or not positive.
	 */
	public static SearchBuilder selectFirst(final Integer maxResults) throws IllegalArgumentException {
		validatePositiveNumber(maxResults, "Max results");

		SearchBuilder builder = new SearchBuilder();
		builder.pageNumber = 1;
		builder.maxResults = maxResults;
		return builder;
	}

	/**
	 * Creates a new <code>SearchBuilder</code> object for a paginated search
	 * that should retrieve all the attributes from the <code>pageNumber</code>
	 * page of entities matching the search. Having up to
	 * <code>maxPageSize</code> entities each page.
	 * 
	 * @param pageNumber
	 *            Not-null, positive index of the page to be retrieved, being 1
	 *            the index of the first page.
	 * @param maxPageSize
	 *            Not-null, positive number of the max per page that should be
	 *            retrieved.
	 * @return The newly created <code>SearchBuilder</code> object.
	 * @throws IllegalArgumentException
	 *             If <code>pageNumber</code> or <code>maxPageSize</code> are
	 *             null or not positive.
	 */
	public static SearchBuilder selectPage(final Integer pageNumber, final Integer maxPageSize)
			throws IllegalArgumentException {
		validatePositiveNumber(pageNumber, "Page number");
		validatePositiveNumber(maxPageSize, "Max page size");

		SearchBuilder builder = new SearchBuilder();
		builder.pageNumber = pageNumber;
		builder.maxResults = maxPageSize;
		return builder;
	}

	/**
	 * Creates a new <code>SearchBuilder</code> object for a search that should
	 * retrieve only the specified attributes from the first
	 * <code>maxResults</code> entities matching the search.
	 * 
	 * @param maxResults
	 *            Not-null, positive number of the max results that should be
	 *            retrieved.
	 * @param attribute
	 *            Not-null, not-empty name of the attribute to be retrieved.
	 * @param additionalAttributes
	 *            Additional attributes to be retrieved (optional).
	 * @return the newly created <code>SearchBuilder</code> object.
	 * @throws IllegalArgumentException
	 *             If <code>maxResults</code> is null or not positive, or if
	 *             <code>attribute</code> is null or empty.
	 */
	public static SearchBuilder selectFistAttributes(final Integer maxResults, final String attribute,
			final String... additionalAttributes) throws IllegalArgumentException {
		validatePositiveNumber(maxResults, "Max results");

		SearchBuilder builder = new SearchBuilder();
		builder.attributes = validateAndConcatAttributes(attribute, additionalAttributes);
		builder.pageNumber = 1;
		builder.maxResults = maxResults;
		return builder;
	}

	/**
	 * Creates a new <code>SearchBuilder</code> object for a paginated search
	 * that should retrieve only the specified attributes from the
	 * <code>pageNumber</code> page of entities matching the search. Having up
	 * to <code>maxPageSize</code> entities each page.
	 * 
	 * @param pageNumber
	 *            Not-null, positive index of the page to be retrieved, being 1
	 *            the index of the first page.
	 * @param maxPageSize
	 *            Not-null, positive number of the max per page that should be
	 *            retrieved.
	 * @param attribute
	 *            Not-null, not-empty name of the attribute to be retrieved.
	 * @param additionalAttributes
	 *            Additional attributes to be retrieved (optional).
	 * @return the newly created <code>SearchBuilder</code> object.
	 * @throws IllegalArgumentException
	 *             If <code>pageNumber</code> or <code>maxPageSize</code> are
	 *             null or not positive, or if <code>attribute</code> is null or
	 *             empty.
	 */
	public static SearchBuilder selectAttributesFromPage(final Integer pageNumber, final Integer maxPageSize,
			final String attribute, final String... additionalAttributes) throws IllegalArgumentException {
		validatePositiveNumber(pageNumber, "Page number");
		validatePositiveNumber(maxPageSize, "Max page size");

		SearchBuilder builder = new SearchBuilder();
		builder.attributes = validateAndConcatAttributes(attribute, additionalAttributes);
		builder.pageNumber = pageNumber;
		builder.maxResults = maxPageSize;
		return builder;
	}

	private static void validatePositiveNumber(Integer number, String numberLabel) throws IllegalArgumentException {
		if (number == null || number.compareTo(0) <= 0) {
			throw new IllegalArgumentException(numberLabel + " must be a not null positive number");
		}
	}

	private static String[] validateAndConcatAttributes(String attribute, String[] additionalAttributes)
			throws IllegalArgumentException {
		if (attribute == null || attribute.isEmpty()) {
			throw new IllegalArgumentException(
					"At least one attribute must be a not null, not empty, valid attribute path");
		}
		String[] attributes = null;
		if (additionalAttributes == null) {
			attributes = new String[] { attribute };
		} else {
			attributes = new String[additionalAttributes.length + 1];
			attributes[0] = attribute;
			System.arraycopy(additionalAttributes, 0, attributes, 1, additionalAttributes.length);
		}
		return attributes;
	}

	private SearchBuilder() {
		super();
	}

	public SearchBuilder where(SearchCriteria searchCriteria) {
		this.searchCriterias = new ArrayList<SearchCriteria>();
		this.searchCriterias.add(searchCriteria);
		return this;
	}

	public SearchBuilder and(SearchCriteria searchCriteria) {
		this.searchCriterias.add(searchCriteria);
		return this;
	}

	public SearchBuilder sortBy(SortingCriteria sortingCriteria) {
		if (this.sortingCriterias == null) {
			this.sortingCriterias = new ArrayList<SortingCriteria>();
		}
		this.sortingCriterias.add(sortingCriteria);
		return this;
	}

	/**
	 * Creates a new {@link Search} instance with all the criterias previously
	 * defined. This instance is ready to be submitted to the
	 * {@link Repository#search}.
	 * 
	 * @return
	 */
	public Search build() {
		SearchCriteria[] searchCriterias = null;
		if (this.searchCriterias != null) {
			searchCriterias = this.searchCriterias.toArray(new SearchCriteria[this.searchCriterias.size()]);
		}
		SortingCriteria[] sortingCriterias = null;
		if (this.sortingCriterias != null) {
			sortingCriterias = this.sortingCriterias.toArray(new SortingCriteria[this.sortingCriterias.size()]);
		}
		return new Search(attributes, searchCriterias, sortingCriterias, maxResults, pageNumber);
	}

}
