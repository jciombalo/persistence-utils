package com.jciombalo.utils.persistence.search;

public interface Condition {

	public Condition negated();
	public Condition and(Condition c);
	public Condition or(Condition c);
}
