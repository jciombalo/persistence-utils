package com.jciombalo.utils.persistence;

public class SortingCriteria {

	private final String attributePath;
	private final boolean desc;
	
	public static SortingCriteria ASC(String field) {
		return new SortingCriteria(field, false);
	}
	
	public static SortingCriteria DESC(String field) {
		return new SortingCriteria(field, true);
	}

	private SortingCriteria(String attributePath, boolean desc) {
		super();
		this.attributePath = attributePath;
		this.desc = desc;
	}

	public String getAttributePath() {
		return attributePath;
	}

	public boolean isDesc() {
		return desc;
	}
	
}
