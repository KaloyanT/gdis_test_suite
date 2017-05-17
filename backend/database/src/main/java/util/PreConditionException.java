package util;

public class PreConditionException extends IllegalArgumentException {
	private static final long serialVersionUID = -5513891348829648436L;
	
	public PreConditionException(String reason) {
		super(reason);
	}
}
