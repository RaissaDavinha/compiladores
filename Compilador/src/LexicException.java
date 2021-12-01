/**
 * Procedimento que manda o erro Léxico encontrado para a console da interface
 * Usado em:
 * 	Main.java, SintaticMain.java, LexicAnalyzer.java
 * Parametros:
 * Strin msg = mensagem de erro para imprimir na console
 * int str = linha onde ocorreu o erro
 * int colunm = coluna onde ocorreu o erro
 */

@SuppressWarnings("serial")
public class LexicException extends Exception{
	public LexicException(String msg, int line, int colunm) {
		super(msg);
		Main.sendToConsole(msg);
		Main.underlineError(line, colunm);
	}
}