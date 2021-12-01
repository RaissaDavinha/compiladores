/**
 * Procedimento que manda o erro Sintatico encontrado para a console da interface
 * Usado em:
 * 	Main.java, SintaticMain.java
 * Parametros:
 * Strin msg = mensagem de erro para imprimir na console
 * int str = linha onde ocorreu o erro
 * int colunm = coluna onde ocorreu o erro
 */

public class SintaticException extends Exception{
	public SintaticException(String msg, int line, int colunm) {
		super(msg);
		Main.sendToConsole(msg);
		Main.underlineError(line, colunm);
	}
}