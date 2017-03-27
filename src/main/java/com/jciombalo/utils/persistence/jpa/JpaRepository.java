package com.jciombalo.utils.persistence.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.jciombalo.utils.persistence.Repository;
import com.jciombalo.utils.persistence.Search;
import com.jciombalo.utils.persistence.SearchResult;

public class JpaRepository<T> implements Repository<T> {
	
	private final Class<T> entityType;
    protected EntityManager em;
    
	public JpaRepository(EntityManager em, Class<T> entityType) {
		super();
		this.em = em;
        this.entityType = entityType;
	}

	@Override
	public void create(T entity) {
		em.persist(entity);
	}

	@Override
	public T retrieve(Serializable id) {
		return em.find(this.entityType, id);
	}

	@Override
	public T update(T t) {
		return em.merge(t);
	}

	@Override
	public T delete(Serializable id) {
		T entity = this.retrieve(id);
		em.remove(entity);
		return entity;
	}

	@Override
	public void delete(T entity) {
		entity = em.merge(entity);
		em.remove(entity);
	}

	@Override
	public SearchResult<T> search(Search searchDetails) {
		CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityType);
		Root<T> fromEntity = criteriaQuery.from(entityType);
		
		// Selection Attributes
		if (searchDetails != null && searchDetails.getAttributes() != null) {
			List<Selection<?>> selectionList = new ArrayList<>(searchDetails.getAttributes().length);
			for (String attributeName : searchDetails.getAttributes()) {
				selectionList.add(fromEntity.get(attributeName));
			}
			criteriaQuery.multiselect(selectionList);
		}
		
		Predicate[] restrictions = null;
		if (searchDetails != null && searchDetails.getSearchCriterias() != null) {
			restrictions = SearchCriteriaParser.parse(searchDetails.getSearchCriterias(), criteriaBuilder, fromEntity);
			criteriaQuery.where(restrictions);
		}
		
		if (searchDetails != null && searchDetails.getSortingCriterias() != null) {
			criteriaQuery.orderBy(SortingCriteriaParser.parse(searchDetails.getSortingCriterias(), criteriaBuilder, fromEntity));
		}
		
		TypedQuery<T> query = this.em.createQuery(criteriaQuery);
		
		// Apply pagination
		if (searchDetails != null && searchDetails.getMaxResults() != null) {
			final long totalItensCount = getItemsCount(restrictions);
			final int firstResult = (searchDetails.getPageNumber() - 1) * searchDetails.getMaxResults();
			query.setFirstResult(firstResult);
			query.setMaxResults(searchDetails.getMaxResults());
			return new SearchResult<T>(query.getResultList(), totalItensCount, searchDetails.getMaxResults(),
					searchDetails.getPageNumber());
		} else {
			return new SearchResult<T>(query.getResultList());
		}
	}
	
	private long getItemsCount(final Predicate[] predicateList) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(entityType)));
		if (predicateList != null) {
			countQuery.where(predicateList);
		}
		return em.createQuery(countQuery).getSingleResult();
	}
	
}
