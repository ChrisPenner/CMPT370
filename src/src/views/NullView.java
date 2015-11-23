package views;

public class NullView extends View {
	private static final long serialVersionUID = 1L;

	public NullView() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Call this whenever something happens that should update the display!
	 */
	public void updateDisplay() {}
	
	/**
	 * Call this whenever a message should be passed to the log
	 * @param message the message to pass
	 */
	public void updateLog(String message) {}
}
