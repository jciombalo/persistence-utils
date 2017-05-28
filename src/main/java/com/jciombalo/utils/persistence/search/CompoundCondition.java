package com.jciombalo.utils.persistence.search;

public class CompoundCondition extends Condition {

	private final Condition condition1;
	private final Condition condition2;

	public CompoundCondition(ConditionType type, Condition condition1, Condition condition2) {
		super(type);
		this.condition1 = condition1;
		this.condition2 = condition2;
	}

	public Condition getCondition1() {
		return condition1;
	}

	public Condition getCondition2() {
		return condition2;
	}

}
