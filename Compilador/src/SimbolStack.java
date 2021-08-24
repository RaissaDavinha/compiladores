import java.util.ArrayList;

public class SimbolStack {
	private ArrayList<Simbolo> stack = new ArrayList<Simbolo>();

	public void insereStack(String lexema, String tipo, int nivel, int posicao) {
		Simbolo simbolo = new Simbolo();
		simbolo.lexema = lexema;
		simbolo.tipo = tipo;
		simbolo.nivel = nivel;
		simbolo.posicao = posicao;
		stack.add(simbolo);
	}
	
	public int returnPosicao(String lexema) {
		for(int i = stack.size() - 1; i >= 0; i--) {
			if (stack.get(i).getLexema().equals(lexema)) {
				return stack.get(i).posicao;
			}
		}
		
		return 0;
	}
	
	public void limpaNivel(int nivel) {
		for(int i = stack.size() - 1; i >= 0; i--) {
			if (stack.get(i).getNivel() == nivel) {
				stack.remove(i);
			}
		}
	}

	public boolean verificaDeclaDuplicVar(String lexema) {
		for (int i = stack.size() - 1; i >= 0; i--) {
			if (stack.get(i).tipo.contains("variavel")) {
				if (stack.get(i).lexema.equals(lexema)) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	public boolean verificaVarDeclarada(String lexema) {
		for (int i = stack.size() - 1; i >= 0; i--) {
			if (stack.get(i).tipo.contains("variavel")) {
				if (stack.get(i).lexema.equals(lexema)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean verificaDeclaDuplic(String lexema) {
		for (int i = stack.size() - 1; i >= 0; i--) {
			if (stack.get(i).getLexema().equals(lexema)) {
				return true;
			}
		}
		return false;
	}
	
	

	public boolean verificaProcDeclarada(String lexema) {
		for (int i = stack.size() - 1; i >= 0; i--) {
			if (stack.get(i).tipo.contains("proc")) {
				if (stack.get(i).lexema.equals(lexema)) {
					return true;
				}
			}
		}
		return false;
	}
	

	public boolean verificaFuncDeclarada(String lexema) {
		for (int i = stack.size() - 1; i >= 0; i--) {
			if (stack.get(i).tipo.contains("funcao")) {
				if (stack.get(i).lexema.equals(lexema)) {
					return true;
				}
			}
		}
		return false;
	}

	
	
	public boolean verificaFuncaoVar(String lexema) {
		for (int i = stack.size() - 1; i >= 0 ; i--) {
			if (stack.get(i).getLexema().equals(lexema) && (stack.get(i).getTipo().equals("funcao booleano") || stack.get(i).getTipo().equals("funcao inteiro") || stack.get(i).getTipo().equals("variavel inteiro") || stack.get(i).getTipo().equals("variavel booleano"))) {
				return true;
			}
		}
		return false;
	}
	
	public void colocaTipo(String tipo, int count) {
		int index = stack.size() - 1;
		int i;
		Simbolo auxSimbolo = new Simbolo();
		for (i = 0; i < count; i++, index--) {
			auxSimbolo = stack.get(index);
			auxSimbolo.tipo = tipo;
			stack.set(index, auxSimbolo);
		}
	}
	
	public int verificaTipoIndentificador(String lexema) {
		int j = stack.size() - 1;
		while (j >= 0) {
			if (lexema.equals(stack.get(j).lexema)) {
				switch (stack.get(j).tipo) {
					case "variavel inteiro":
					return 1;
					
					case "variavel booleano":
					return 2;
					
					case "funcao inteiro":
					return 3;
					
					case "funcao booleano":
					return 4;
					
					default:
					return 0;
				}
			} else {
				j--;
			}
		}
		return 0;
	}

}