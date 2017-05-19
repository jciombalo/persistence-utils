package com.jciombalo.utils.persistence.search.test;

import com.jciombalo.utils.persistence.search.literal.LiteralSearchBuilder;

public class LiteralSearchTest {

	public void test() {
		LiteralSearchBuilder builder = new LiteralSearchBuilder();
		builder.select("f1", "f2", "f3");
	}
}
