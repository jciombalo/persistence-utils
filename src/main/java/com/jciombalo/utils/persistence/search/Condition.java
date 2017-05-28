package com.jciombalo.utils.persistence.search;

public class Condition {
	private final ConditionType type;

	protected Condition(ConditionType type) {
		super();
		this.type = type;
	}

	public Condition negated() {
		return new CompoundCondition(ConditionType.NOT, this, null);
	}

	public Condition and(Condition anotherCondition) {
		return new CompoundCondition(ConditionType.AND, this, anotherCondition);
	}

	public Condition or(Condition anotherCondition) {
		return new CompoundCondition(ConditionType.OR, this, anotherCondition);
	}

	public ConditionType getType() {
		return type;
	}

}
