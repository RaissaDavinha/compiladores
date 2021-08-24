@SuppressWarnings("serial")
public class LexicException extends Exception{
	public LexicException(String msg, int line, int colunm) {
		super(msg);
		Main.sendToConsole(msg);
		Main.underlineError(line, colunm);
	}
}