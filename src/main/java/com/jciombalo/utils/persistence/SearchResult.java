package com.jciombalo.utils.persistence;

import java.io.Serializable;
import java.util.List;

public class SearchResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<T> data;
	private final long itemsFound;
	private final long maxPageSize;
	private final long currentPage;

	public SearchResult(final List<T> data) {
		super();
		this.data = data;
		if (data == null) {
			this.itemsFound = 0l;
			this.maxPageSize = 0l;
			this.currentPage = 0l;
		} else {
			this.itemsFound = Long.valueOf(data.size());
			this.maxPageSize = this.itemsFound;
			this.currentPage = 1l;
		}
	}
	
	public SearchResult(final List<T> data, final long itemsFound, 
			final long maxPageSize,
			final long currentPage) {
		super();
		this.data = data;
		this.itemsFound = itemsFound;
		this.maxPageSize = maxPageSize;
		this.currentPage = currentPage;
	}

	public List<T> getData() {
		return data;
	}

	public long getItemsFound() {
		return this.itemsFound;
	}

	public long getMaxPageSize() {
		return maxPageSize;
	}

	public long getCurrentPage() {
		return currentPage;
	}

	public long getPagesFound() {
		if (this.itemsFound == 0) {
			return 0;
		} else {
			return this.itemsFound / this.maxPageSize + (this.itemsFound % this.maxPageSize > 0 ? 1 : 0);
		}
	}

}
