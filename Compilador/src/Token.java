
public class Token {
	String lexema;
	String simbolo;
	int linha;
	int coluna;
	
	public Token(Token token) {
		this.lexema = token.lexema;
		this.simbolo = token.simbolo;
		this.linha = token.linha;
		this.coluna = token.coluna;
	}
	public Token() {
	}
	
	public int getColuna() {
		return coluna;
	}

	public boolean teste(int a) {
		if (a == 1) {
			return true;
		} else {
			return false;
		}
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	
	public int getLinha() {
		return linha;
	}



	public void setLinha(int linha) {
		this.linha = linha;
	}



	public String getLexema() {
		return lexema;
	}



	public void setLexema(String lexema) {
		this.lexema = lexema;
	}



	public String getSimbolo() {
		return simbolo;
	}



	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	@Override
	public String toString() {
		return "Token [lexema=" + lexema + ", simbolo=" + simbolo + ", linha=" + linha + "]";
	}

}
