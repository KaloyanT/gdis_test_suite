package com.gdis.importer.util;

public class PreCondition {

	public static void require(boolean predicate, String reason) {
		if (!predicate) {
			throw new PreConditionException(reason);
		}
	}

	public static <T> void notNull(final T obj, String name) {
		require(obj != null, name + " is not present (null)");
	}
}
