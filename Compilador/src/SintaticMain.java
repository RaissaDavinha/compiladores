import java.io.IOException;

public class SintaticMain {
	static Token token = new Token();
	static Token auxToken = new Token();
	static LexicAnalyser analisadorLexico;

	public static void sintaticMain(String file) throws IOException, LexicException, SintaticException {

		try {
			token = new Token();
			analisadorLexico = new LexicAnalyser(file);
			token = analisadorLexico.getToken();

			if (token.simbolo == "sprograma") {
				token = analisadorLexico.getToken();
				if (token.simbolo == "sidentificador") {
					token = analisadorLexico.getToken();
					if (token.simbolo == "sponto_virgula") {
						// comeca analisa_bloco
						analisaBloco();
						if (token.simbolo == "sponto") {
							if (analisadorLexico.getToken().getSimbolo() == "error") {
								System.out.print("Compilado com sucesso");
								Main.sendToConsole("Compilado com sucesso");
							} else {
								throw new SintaticException("Erro Sintatico na linha: " + token.linha + ", coluna:" + token.coluna, token.linha, token.coluna);
							}
						} else {
							throw new SintaticException("Erro Sintatico na linha: " + token.linha + ", esperado ponto", token.linha, token.coluna);
						}
					} else {
						throw new SintaticException("Erro Sintatico na linha: " + token.linha + ", esperado ponto e virgula", token.linha, token.coluna);
					}
				} else {
					throw new SintaticException("Erro Sintatico na linha: " + token.linha + ", identificador invalido", token.linha, token.coluna);
				}
			} else {
				throw new SintaticException("Erro Sintatico na linha: " + token.linha + ", programa nao inicializado", token.linha, token.coluna);
			}

		} catch (IOException ioException) {
			System.out.print("Erro ao abrir o arquivo: " + ioException.getMessage());
		} catch (LexicException lexicoException) {
			System.out.print(lexicoException.getMessage());
		}
	}

	public static void analisaBloco() throws IOException, SintaticException, LexicException {
		token = analisadorLexico.getToken();
		analisaEtVariaveis();
		analisaSubrotina();
		analisaComandos();
	}

	public static void analisaComandos() throws IOException, SintaticException, LexicException {
		if (token.simbolo == "sinicio") {
			token = analisadorLexico.getToken();
			analisaComandoSimples();
			while (token.simbolo != "sfim") {
				if (token.simbolo == "sponto_virgula") {
					token = analisadorLexico.getToken();
					if (token.simbolo != "sfim") {
						analisaComandoSimples();
					}
				} else {
					throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", esperado ponto e virgula", token.linha, token.coluna);
				}
			}
			token = analisadorLexico.getToken();
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", inicio esperado", token.linha, token.coluna);
		}
	}

	public static void analisaComandoSimples() throws IOException, SintaticException, LexicException {
		switch (token.simbolo) {

		case "sidentificador":
			analisaAtribChProcedimento();
			break;

		case "sse":
			analisaSe();
			break;

		case "senquanto":
			analisaEnquanto();
			break;

		case "sleia":
			analisaLeia();
			break;

		case "sescreva":
			analisaEscreva();
			break;

		default:
			analisaComandos();
		}
	}

	public static void analisaAtribChProcedimento() throws SintaticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		
		if (token.simbolo == "satribuicao") {
			token = analisadorLexico.getToken();
			analisaExpressao();
		} else {
			chamadaProcedimento();
		}
	}

	public static void analisaSe() throws SintaticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		analisaExpressao();
		
		if (token.simbolo == "sentao") {
			token = analisadorLexico.getToken();
			analisaComandoSimples();
			
			if (token.simbolo == "ssenao") {
				token = analisadorLexico.getToken();
				analisaComandoSimples();
			}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", entao nao encontrado", token.linha, token.coluna);
		}
	}

	public static void analisaEnquanto() throws SintaticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		analisaExpressao();
		
		if (token.simbolo == "sfaca") {
			token = analisadorLexico.getToken();
			analisaComandoSimples();
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", comando faca nao encontrado", token.linha, token.coluna);
		}
	}

	public static void analisaLeia() throws SintaticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sabre_parenteses") {
			token = analisadorLexico.getToken();
			if (token.simbolo == "sidentificador") {
				token = analisadorLexico.getToken();
				if (token.simbolo == "sfecha_parenteses") {
					token = analisadorLexico.getToken();
				} else {
					throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses nao fechado", token.linha, token.coluna);
				}
			} else {
				throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", identificador invalido", token.linha, token.coluna);
			}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses esperado", token.linha, token.coluna);
		}
	}

	public static void analisaEscreva() throws SintaticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sabre_parenteses") {
					token = analisadorLexico.getToken();
					analisaExpressao();
					if (token.simbolo == "sfecha_parenteses") {
						token = analisadorLexico.getToken();
					} else {
						throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses nao fechado", token.linha, token.coluna);
					}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses esperado", token.linha, token.coluna);
		}
	}

	public static void analisaFator() throws SintaticException, IOException, LexicException {
		if (token.simbolo == "sidentificador") {
			chamadaFuncao();
		} else {
			if (token.simbolo == "snumero") {
				token = analisadorLexico.getToken();
			} else {
				if (token.simbolo == "snao") {
					token = analisadorLexico.getToken();
					analisaFator();
				} else {
					if (token.simbolo == "sabre_parenteses") {
						token = analisadorLexico.getToken();
						analisaExpressao();
						if (token.simbolo == "sfecha_parenteses") {
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses nao fechado", token.linha, token.coluna);
						}
					} else {
						if (token.lexema.equals("verdadeiro") || token.lexema.equals("falso")) {
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", operador relacional invalido", token.linha, token.coluna);
						}
					}
				}
			}
		}
	}

	public static void analisaExpressao() throws IOException, LexicException, SintaticException {
		analisaExpressaoSimples();
		if (token.simbolo == "smaior" || token.simbolo == "smaiorig" || token.simbolo == "sig"
				|| token.simbolo == "smenor" || token.simbolo == "smenorig" || token.simbolo == "sdif") {
			token = analisadorLexico.getToken();
			analisaExpressaoSimples();
		}
	}

	public static void analisaExpressaoSimples() throws IOException, LexicException, SintaticException {
		if (token.simbolo == "smais" || token.simbolo == "smenos") {
			token = analisadorLexico.getToken();
		}
		analisaTermo();
		while (token.simbolo == "smais" || token.simbolo == "smenos" || token.simbolo == "sou") {
			token = analisadorLexico.getToken();
			analisaTermo();
		}
	}

	public static void analisaTermo() throws SintaticException, IOException, LexicException {
		analisaFator();
		while (token.simbolo == "smult" || token.simbolo == "sdiv" || token.simbolo == "se") {
			token = analisadorLexico.getToken();
			analisaFator();
		}
	}

	public static void analisaEtVariaveis() throws SintaticException, IOException, LexicException {
		if (token.simbolo == "svar") {
			token = analisadorLexico.getToken();
			if (token.simbolo == "sidentificador") {
				while (token.simbolo == "sidentificador") {
					analisaVariaveis();
					if (token.simbolo == "sponto_virgula") {
						token = analisadorLexico.getToken();
					} else {
						throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", esperado ponto e virgula", token.linha, token.coluna);
					}
				}
			} else {
				throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", esperado identificador", token.linha, token.coluna);
			}
		}
	}

	public static void analisaVariaveis() throws SintaticException, IOException, LexicException {
		do {
			if (token.simbolo == "sidentificador") {
				token = analisadorLexico.getToken();
				if (token.simbolo == "svirgula" || token.simbolo == "sdoispontos") {
					if (token.simbolo == "svirgula") {
						token = analisadorLexico.getToken();
						if (token.simbolo == "sdoispontos") {
							throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", caracter invalido", token.linha, token.coluna);
						}
					}
				} else {
					throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", caracter invalido", token.linha, token.coluna);
				}
			} else {
				throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", identificador invalido", token.linha, token.coluna);
			}
		} while (token.simbolo != "sdoispontos");
		token = analisadorLexico.getToken();
		analisaTipo();
	}

	public static void analisaTipo() throws SintaticException, IOException, LexicException {
		if (token.simbolo != "sinteiro" && token.simbolo != "sbooleano") {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", tipo invalido", token.linha, token.coluna);
		}
		token = analisadorLexico.getToken();
	}

	public static void analisaSubrotina() throws SintaticException, IOException, LexicException {
		while (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			if (token.simbolo == "sprocedimento") {
				analisaDeclaracaoProcedimento();
			} else {
				analisaDeclaracaoFuncao();
			}
			if (token.simbolo == "sponto_virgula") {
				token = analisadorLexico.getToken();
			} else {
				throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", esperado ponto e virgula", token.linha, token.coluna);
			}
		}
	}

	public static void analisaDeclaracaoProcedimento() throws SintaticException, IOException, LexicException {
		token = analisadorLexico.getToken();

		if (token.simbolo == "sidentificador") {
			token = analisadorLexico.getToken();

			if (token.simbolo == "sponto_virgula") {
				analisaBloco();
				
			} else {
				throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", esperado ponto e virgula", token.linha, token.coluna);
			}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", identificador invalido", token.linha, token.coluna);
		}
	}

	public static void analisaDeclaracaoFuncao() throws SintaticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		
		if (token.simbolo == "sidentificador") {
			auxToken = token;
			token = analisadorLexico.getToken();

			if (token.simbolo == "sdoispontos") {
				token = analisadorLexico.getToken();

				if (token.simbolo == "sinteiro" || token.simbolo == "sbooleano") {
					token = analisadorLexico.getToken();

					if (token.simbolo == "sponto_virgula") {
						analisaBloco();
					}
				} else {
					throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", tipo invalido", token.linha, token.coluna);
				}
			} else {
				throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", esperado dois pontos", token.linha, token.coluna);
			}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", identificador invalido", token.linha, token.coluna);
		}
	}

	public static void chamadaProcedimento() throws SintaticException, IOException, LexicException {
		
	}
	
	public static void chamadaFuncao() throws SintaticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sponto_virgula" || token.simbolo == "smult" || token.simbolo == "sdiv" || token.simbolo == "smais" 
				|| token.simbolo == "smenos" || token.simbolo == "smaior" || token.simbolo == "smaiorig" 
				|| token.simbolo == "smenor" || token.simbolo == "smenorig" || token.simbolo == "sig" 
				|| token.simbolo == "sdif" ||  token.simbolo == "sou" || token.simbolo == "sentao" 
				|| token.simbolo == "sfaca" || token.simbolo == "sfecha_parenteses" || token.simbolo == "sfim") {
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", caracter invalido", token.linha, token.coluna);
		}
	}
}
