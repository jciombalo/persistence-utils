package com.jciombalo.utils.persistence.search;

import java.util.List;

public interface Path {

	public Condition eq(Object value);

	public Condition ge(Object value);

	public Condition gt(Object value);

	public Condition in(List<Object> values);

	public Condition le(Object value);

	public Condition lt(Object value);

	public Condition isNull();

	public Sorting asc();

	public Sorting desc();

}
