package com.jciombalo.utils.persistence.search;

import java.util.List;

public class SingleCondition extends Condition {

	private final Path path;
	private final Comparable value;
	private final List<Comparable> values;

	protected SingleCondition(ConditionType type, Path path, Comparable value, List<Comparable> values) {
		super(type);
		this.path = path;
		this.value = value;
		this.values = values;
	}

	public Path getPath() {
		return path;
	}

	public Comparable getValue() {
		return value;
	}

	public List<Comparable> getValues() {
		return values;
	}

}
