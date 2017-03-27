package com.jciombalo.utils.persistence;

/**
 * <p>
 * Holds the criterias of a search to be performed on a {@link Repository#search
 * repository}.
 * </p>
 * <p>
 * The among these criterias, different kind of conditions can be defined and
 * combined, like {@link SearchCriteria filtering}, {@link SortingCriteria
 * sorting}, pagination details and the list of attributes to be retrieved.
 * </p>
 * <p>
 * New search instances can only be defined using the {@link SearchBuilder}
 * fluent API.
 * </p>
 * 
 * @author jorge.ciombalo
 * 
 * @see Repository
 * @see SearchBuilder
 * @see SearchCriteria
 * @see SortingCriteria
 */
public class Search {

	private final String[] attributes;
	private final SearchCriteria[] searchCriterias;
	private final SortingCriteria[] sortingCriterias;
	private final Integer maxResults;
	private final Integer pageNumber;

	Search(String[] attributes, SearchCriteria[] searchCriterias, SortingCriteria[] sortingCriterias,
			Integer maxResults, Integer pageNumber) {
		super();
		this.attributes = attributes;
		this.searchCriterias = searchCriterias;
		this.sortingCriterias = sortingCriterias;
		this.maxResults = maxResults;
		this.pageNumber = pageNumber;
	}

	/**
	 * @return list of attributes to be retrieved
	 */
	public String[] getAttributes() {
		return attributes;
	}

	/**
	 * @return list of filtering criterias to be applied.
	 */
	public SearchCriteria[] getSearchCriterias() {
		return searchCriterias;
	}

	/**
	 * @return list of sorting criterias to be applied.
	 */
	public SortingCriteria[] getSortingCriterias() {
		return sortingCriterias;
	}

	/**
	 * @return The max number of entities to be retrieved by the search.
	 */
	public Integer getMaxResults() {
		return maxResults;
	}

	/**
	 * @return The number of the page to be retrieved by the search.
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}

}
