package com.jciombalo.utils.persistence;

import java.io.Serializable;
import java.util.Collection;

public class SearchResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Collection<T> content;
	private final long itemsFound;
	private final long maxPageSize;
	private final long currentPage;

	public SearchResult(final Collection<T> content) {
		super();
		this.content = content;
		if (content == null) {
			this.itemsFound = 0l;
			this.maxPageSize = 0l;
			this.currentPage = 0l;
		} else {
			this.itemsFound = Long.valueOf(content.size());
			this.maxPageSize = this.itemsFound;
			this.currentPage = 1l;
		}
	}
	
	public SearchResult(final Collection<T> content, final long itemsFound, 
			final long maxPageSize,
			final long currentPage) {
		super();
		this.content = content;
		this.itemsFound = itemsFound;
		this.maxPageSize = maxPageSize;
		this.currentPage = currentPage;
	}

	public Collection<T> getContent() {
		return content;
	}

	public long getItemsFound() {
		return this.itemsFound;
	}

	public long getPagesFound() {
		if (this.itemsFound == 0) {
			return 0;
		} else {
			return this.itemsFound / this.maxPageSize + (this.itemsFound % this.maxPageSize > 0 ? 1 : 0);
		}
	}

	public long getCurrentPage() {
		return currentPage;
	}

}
