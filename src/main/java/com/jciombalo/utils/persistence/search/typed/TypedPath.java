package com.jciombalo.utils.persistence.search.typed;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.jciombalo.utils.persistence.search.Path;

public class TypedPath extends Path {

	private List<Function> pathParts;
	
	public static <T, R> TypedPath path(Function<T, R> path) {
		return new TypedPath(path);
	}

	protected <T,R> TypedPath(Function<T,R> path) {
		super();
		this.pathParts = new ArrayList<>();
		this.pathParts.add(path);
	}
	
	public <T, R> TypedPath subpath(Function<T, R> path) {
		this.pathParts.add(path);
		return this;
	}
	
}
