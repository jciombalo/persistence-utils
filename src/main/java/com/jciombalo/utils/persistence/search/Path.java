package com.jciombalo.utils.persistence.search;

import java.util.List;

public class Path {

	public Sorting asc() {
		return new Sorting(false, this);
	}

	public Sorting desc() {
		return new Sorting(true, this);
	}
	
	public Condition endingWith(String value) {
		return this.like(value, false, true);
	}
	
	public Condition equals(Comparable value) {
		return new SingleCondition(ConditionType.EQ, this, value, null);
	}

	public Condition greaterOrEqual(Comparable value) {
		return new SingleCondition(ConditionType.GE, this, value, null);
	}

	public Condition greaterThan(Comparable value) {
		return new SingleCondition(ConditionType.GT, this, value, null);
	}

	public Condition in(List<Comparable> values) {
		return new SingleCondition(ConditionType.IN, this, null, values);
	}
	
	public Condition like(String value) {
		return this.like(value, true, true);
	}
	
	public Condition like(String value, boolean startingWith, boolean endingWith) {
		StringBuilder builder = new StringBuilder();
		if (endingWith) {
			builder.append("%");
		}
		builder.append(value);
		if (startingWith) {
			builder.append("%");
		}
		return new SingleCondition(ConditionType.LIKE, this, value, null);
	}

	public Condition lowerOrEqual(Comparable value) {
		return new SingleCondition(ConditionType.LE, this, value, null);
	}

	public Condition lowerThan(Comparable value) {
		return new SingleCondition(ConditionType.LT, this, value, null);
	}

	public Condition isNull() {
		return new SingleCondition(ConditionType.NULL, this, null, null);
	}

	public Condition startingWith(String value) {
		return this.like(value, true, false);
	}
}
