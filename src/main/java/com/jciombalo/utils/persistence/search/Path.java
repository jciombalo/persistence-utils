package com.jciombalo.utils.persistence.search;

import java.util.List;

public class Path {

	public Condition eq(Comparable value) {
		return new SingleCondition(ConditionType.EQ, this, value, null);
	}

	public Condition ge(Comparable value) {
		return new SingleCondition(ConditionType.GE, this, value, null);
	}

	public Condition gt(Comparable value) {
		return new SingleCondition(ConditionType.GT, this, value, null);
	}

	public Condition in(List<Comparable> values) {
		return new SingleCondition(ConditionType.IN, this, null, values);
	}

	public Condition le(Comparable value) {
		return new SingleCondition(ConditionType.LE, this, value, null);
	}

	public Condition lt(Comparable value) {
		return new SingleCondition(ConditionType.LT, this, value, null);
	}

	public Condition isNull() {
		return new SingleCondition(ConditionType.NULL, this, null, null);
	}

	public Sorting asc() {
		return new Sorting(false, this);
	}

	public Sorting desc() {
		return new Sorting(true, this);
	}

}
