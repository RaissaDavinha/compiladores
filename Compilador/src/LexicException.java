@SuppressWarnings("serial")
public class LexicException extends Exception{
	public LexicException(String msg, int line, int colunm) {
		super(msg);
		System.out.println(msg + " linha " + line);
	}
}