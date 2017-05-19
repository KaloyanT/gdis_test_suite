package com.gdis.database.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ProductType {
	
	PROFIT_INSURANCE(0, "PROFIT_INSURANCE", "PROFIT_INSURANCE"){
		@Override
		public boolean isPROFIT_INSURANCE() {
			return true;
		}
	},
	
	CONTROL_INSURANCE(1, "CONTROL_INSURANCE", "CONTROL_INSURANCE"){
		@Override
		public boolean isCONTROL_INSURANCE() {
			return true;
		}
	},
	
	CAPITAL_INSURANCE(2, "CAPITAL_INSURANCE", "CAPITAL_INSURANCE"){
		@Override
		public boolean isCAPITAL_INSURANCE() {
			return true;
		}
	};
	
	private static final ProductType[] VALUES_ARRAY = new ProductType[] { PROFIT_INSURANCE,	
											CONTROL_INSURANCE, CAPITAL_INSURANCE};
	
	public static final List<ProductType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	public static ProductType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ProductType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	public static ProductType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ProductType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	public static ProductType get(int value) {
		for (ProductType enumInstance : VALUES_ARRAY) {
			if (enumInstance.getValue() == value) {
				return enumInstance;
			}
		}
		return null;
	}

	private final int value;
	
	private final String name;

	private final String literal;

	
	private ProductType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	public boolean isPROFIT_INSURANCE() {
		return false;
	}

	public boolean isCONTROL_INSURANCE() {
		return false;
	}
	
	public boolean isCAPITAL_INSURANCE() {
		return false;
	}
	
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String getLiteral() {
		return literal;
	}

	@Override
	public String toString() {
		return literal;
	}
}
