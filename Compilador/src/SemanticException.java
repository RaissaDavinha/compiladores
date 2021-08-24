public class SemanticException extends Exception {
	public SemanticException(String msg, int line, int colunm) {
		super(msg);
		Main.sendToConsole(msg);
		Main.underlineError(line, colunm);
	}
}