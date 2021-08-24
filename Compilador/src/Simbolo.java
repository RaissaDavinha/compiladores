public class Simbolo {
	String lexema;
	String tipo;
	int posicao;
	int nivel;
	
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	@Override
	public String toString() {
		return "Simbolo [lexema=" + lexema + ", tipo=" + tipo + "]";
	}
	
}