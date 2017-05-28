package com.jciombalo.utils.persistence.search;

public class Sorting {

	private boolean desc;
	private Path path;

	public Sorting(boolean desc, Path path) {
		super();
		this.desc = desc;
		this.path = path;
	}

	public boolean isDesc() {
		return desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

}
