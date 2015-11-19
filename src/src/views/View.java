package src.views;

public abstract class View {
	
	/**
	 * Call this whenever something happens that should update the display!
	 */
	public abstract void updateDisplay();
	
	/**
	 * Call this whenever a message should be passed to the log
	 * @param message the message to pass
	 */
	public abstract void updateLog(String message);
	
}
