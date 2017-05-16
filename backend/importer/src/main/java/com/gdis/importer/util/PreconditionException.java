package com.gdis.importer.util;

public class PreconditionException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7730162433418498116L;
	
	public PreconditionException(String reason) {
		super(reason);
	}
}
