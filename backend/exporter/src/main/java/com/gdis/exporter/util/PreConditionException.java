package com.gdis.exporter.util;

public class PreConditionException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7730162433418498116L;
	
	public PreConditionException(String reason) {
		super(reason);
	}
}
