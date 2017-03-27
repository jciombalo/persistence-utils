package com.jciombalo.utils.persistence;

import java.io.Serializable;

/**
 * Helper interface that encapsulates CRUD operations for a given entity
 * <code>T</code> on a data repository.
 * 
 * @author jorge.ciombalo
 *
 * @param <T>
 *            The kind of the entity handled by the repository interface.
 */
public interface Repository<T> {

	/**
	 * Inserts a new entity of type T in the repository.
	 * 
	 * @param entity
	 *            The new entity to be persisted.
	 */
	void create(T entity);

	/**
	 * Retrieves an object of type T, identified by <code>id</code>, from the
	 * repository.
	 * 
	 * @param id
	 *            The identifier of the entity to be retrieved.
	 * @return The entity related to the given identifier, or <code>null</code>,
	 *         if there's no entity related.
	 */
	T retrieve(Serializable id);

	/**
	 * Updates an existing entity of type T in the repository.
	 * 
	 * @param entity
	 *            Then changed entity to be persisted.
	 * @return The updated entity.
	 */
	T update(T entity);

	/**
	 * Removes an entity of type T, identified by <code>id</code>, from the
	 * repository.
	 * 
	 * @param id
	 *            The identifier of the entity to be removed from the
	 *            repository.
	 * @return The entity removed from the repository.
	 */
	T delete(Serializable id);

	/**
	 * Removes an entity of type T from the repository.
	 * 
	 * @param entity
	 *            The entity to be removed.
	 */
	void delete(T entity);

	/**
	 * Performs a search in the repository applying the criterias defined by the
	 * given {@link Search search details}. To define these details, the
	 * {@link SearchBuilder} fluent API must be used.
	 * 
	 * @param searchDetails
	 *            The constraints to be used while searching.
	 * @return The entities found, along with additional information about the
	 *         search, according to the given search details.
	 * @see Search
	 * @see SearchBuilder
	 * @see SearchResult
	 */
	SearchResult<T> search(Search searchDetails);
}
