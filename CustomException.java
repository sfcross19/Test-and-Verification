package laneChanger;

public class CustomException extends Exception{
	private String message;
	public CustomException(String message) {
		super();
		this.message = message;
	}
	// For chaining exceptions
	public CustomException(Throwable thr) {
		super();
	}
	public CustomException(String message, Throwable thr) {
		super();
		this.message = message;
	}
	@Override
	public String getMessage() {
		return message;
	}
}
