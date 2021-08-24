import java.io.IOException;
import java.util.ArrayList;

public class SintaticMain {
	static Token token = new Token();
	static Token auxToken = new Token();
	static LexicAnalyser analisadorLexico;
	static GeradorCodigo geradorCodigo;
	static SimbolStack simbolStack;
	static ArrayList<Integer> nivelList;
	static ArrayList<Token> infixList;
	static ArrayList<Token> postfixList;
	static ArrayList<Integer> procFuncRotuloStack;
	static ArrayList<Integer> allocStack;
	static ArrayList<Integer> allocPerFuncProcStack;
	static ArrayList<String> procFunDeclaPath;
	static ArrayList<String> seEntaoStack;
	
	static int nivelMax = 0;
	static int allocIndex;
	static int posicao;
	static int variaveisDeclaradas = 0;
	static int posicaoVariavel = 0;
	
	static boolean procurandoRetorno = false;

	public static void sintaticMain(String file) throws IOException, LexicException, SintaticException, SemanticException {

		try {
			seEntaoStack = new ArrayList<String>();
			variaveisDeclaradas = 0;
			nivelList = new ArrayList<Integer>();
			simbolStack = new SimbolStack();
			token = new Token();
			infixList = new ArrayList<Token>();
			postfixList = new ArrayList<Token>();
			nivelMax = 0;
			allocIndex = 1;
			posicaoVariavel = 0;
			procurandoRetorno = false;
			analisadorLexico = new LexicAnalyser(file);
			geradorCodigo = new GeradorCodigo();
			procFuncRotuloStack = new ArrayList<Integer>();
			allocStack = new ArrayList<Integer>();
			allocPerFuncProcStack = new ArrayList<Integer>();
			allocPerFuncProcStack.add(0);
			procFunDeclaPath = new ArrayList<String>();
		
			posicao = 0;
			token = analisadorLexico.getToken();
			nivelList.add(0);

			if (token.simbolo == "sprograma") {
				procFunDeclaPath.add("sprograma");
				token = analisadorLexico.getToken();
				if (token.simbolo == "sidentificador") {
					// insere_table(token.lexema, "nomedeprograma","","");
					simbolStack.insereStack(token.lexema, "nomedeprograma",nivelList.get(nivelList.size() - 1), posicao++);
					token = analisadorLexico.getToken();
					if (token.simbolo == "sponto_virgula") {

						geradorCodigo.geraStart();

						procurandoRetorno = false;
						// comeca analisa_bloco
						analisaBloco();
						if (token.simbolo == "sponto") {
							if (analisadorLexico.getToken().getSimbolo() == "error") {
								for (int dallocIndex = allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1); dallocIndex > 0; dallocIndex--) {
									geradorCodigo.geraDalloc(allocStack.get(allocStack.size() - 2), allocStack.get(allocStack.size() - 1));
									allocStack.remove(allocStack.size() - 1);
									allocStack.remove(allocStack.size() - 1);
								}
								allocPerFuncProcStack.remove(allocPerFuncProcStack.size() - 1);
								geradorCodigo.geraHlt();
								System.out.print("Compilado com sucesso");
								Main.sendToConsole("Compilado com sucesso");
								geradorCodigo.geraArquivo();
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

	public static void analisaBloco() throws IOException, SintaticException, LexicException, SemanticException {
		token = analisadorLexico.getToken();
		analisaEtVariaveis();
		analisaSubrotina();
		if (procFunDeclaPath.get(procFunDeclaPath.size() - 1) == "sfuncao") {
			procurandoRetorno = true;
			seEntaoStack = new ArrayList<String>();
			seEntaoStack.add("smainN");
		} else {
			procurandoRetorno = false;
		}
		analisaComandos();
		if (procurandoRetorno == true) {
			if (seEntaoStack.get(seEntaoStack.size() - 1) == "smainN") {
				//todo
			}
		}
	}

	public static void analisaComandos() throws IOException, SintaticException, LexicException, SemanticException {
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

	public static void analisaComandoSimples() throws IOException, SintaticException, LexicException, SemanticException {
		switch (token.simbolo) {

		case "sidentificador":
			analisaAtribChProcedimento();
			break;

		case "sse":
			if (procurandoRetorno == true) {
				seEntaoStack.add("sseN");
			}
			analisaSe();
			break;

		case "senquanto":
			if (procurandoRetorno == true) {
				seEntaoStack.add("sseN");
			}
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

	public static void analisaAtribChProcedimento() throws SintaticException, IOException, LexicException, SemanticException {
		auxToken = token;
		token = analisadorLexico.getToken();
		
		if (token.simbolo == "satribuicao") {
			if (!simbolStack.verificaFuncaoVar(auxToken.lexema)) {
				throw new SemanticException("Erro Semantico na linha: " + token.linha + ", funcao ou variavel nao reconhecida", token.linha, token.coluna);
			}
			
			infixList = new ArrayList<Token>();
			token = analisadorLexico.getToken();
			analisaExpressao(); 
			postfixList = geradorCodigo.geraPostFix(infixList);
		
			for (Token postfix : postfixList) { 		      		
				System.out.print(postfix.lexema);
			}
			
			System.out.println("");
			
			switch (simbolStack.verificaTipoIndentificador(auxToken.lexema)) {
			case 1:
				auxToken.simbolo = "variavel inteiro";
				break;
			case 2:
				auxToken.simbolo = "variavel booleano";
				break;
			case 3:
				auxToken.simbolo = "funcao inteiro";
				break;
			case 4:
				auxToken.simbolo = "funcao booleano";
				break;
			case 0:
				throw new SemanticException("Erro Semantico na linha: " + token.linha + ", funcao ou variavel nao reconhecida", token.linha, token.coluna);
			}

			ArrayList<Token> auxPostfix = new ArrayList<Token>();
			for (Token item : postfixList) auxPostfix.add(new Token(item));
			
			if (auxToken.simbolo == "variavel inteiro") {
				if (!geradorCodigo.validaPostFixInteiro(auxPostfix)) {
					throw new SemanticException("Erro Semantico na linha: " + token.linha + ", atribuicao de booleano invalida", token.linha, token.coluna);
				}
				geradorCodigo.geraCodigoDaPosfix(simbolStack);
				// dar store
				geradorCodigo.geraStr(simbolStack.returnPosicao(auxToken.lexema));
			} else {
				if (auxToken.simbolo == "variavel booleano") {
					if (!geradorCodigo.validaPostFixBooleano(auxPostfix)) {
						throw new SemanticException("Erro Semantico na linha: " + token.linha + ", atribuicao de inteiro invalida", token.linha, token.coluna);
					}
					geradorCodigo.geraCodigoDaPosfix(simbolStack);
					// dar store
					geradorCodigo.geraStr(simbolStack.returnPosicao(auxToken.lexema));
				} else {
					if (auxToken.simbolo == "funcao inteiro") {
						if (procurandoRetorno == true) {
							// substitui ultimo caracter por S caso achar retorno
							seEntaoStack.set(seEntaoStack.size() - 1, seEntaoStack.get(seEntaoStack.size() - 1).substring(0,seEntaoStack.get(seEntaoStack.size() - 1).length() - 1) + "S");
						}
						if (!geradorCodigo.validaPostFixInteiro(auxPostfix)) {
							throw new SemanticException("Erro Semantico na linha: " + token.linha + ", atribuicao de booleano invalida", token.linha, token.coluna);
						}
						geradorCodigo.geraCodigoDaPosfix(simbolStack);
						
						// valida se esta na funcao para dar retorno
						
						// dar return de funcao
						int allocQtd = 0;
						int allocStart = 0;
						if (allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) > 0) {
							for (int dallocIndex = 0; dallocIndex  < allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1); dallocIndex++) {
								allocQtd += allocStack.get(allocStack.size() - 1 - dallocIndex * 2);
								allocStart = allocStack.get(allocStack.size() - 2 - dallocIndex * 2);
							}
						}
						geradorCodigo.geraStr(0);
						geradorCodigo.geraDalloc(allocStart, allocQtd);
						geradorCodigo.geraReturn();
					} else {
						if (auxToken.simbolo == "funcao booleano") {
							if (procurandoRetorno == true) {
								// substitui ultimo caracter por S caso achar retorno
								seEntaoStack.set(seEntaoStack.size() - 1, seEntaoStack.get(seEntaoStack.size() - 1).substring(0,seEntaoStack.get(seEntaoStack.size() - 1).length() - 1) + "S");
							}
							if (!geradorCodigo.validaPostFixBooleano(auxPostfix)) {
								throw new SemanticException("Erro Semantico na linha: " + token.linha + ", atribuicao de inteiro invalida", token.linha, token.coluna);
							}
							geradorCodigo.geraCodigoDaPosfix(simbolStack);
							
							// valida se esta na funcao para dar retorno
							
							// dar return de funcao
							int allocQtd = 0;
							int allocStart = 0;
							if (allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) > 0) {
								for (int dallocIndex = 0; dallocIndex  < allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1); dallocIndex++) {
									allocQtd += allocStack.get(allocStack.size() - 1 - dallocIndex * 2);
									allocStart = allocStack.get(allocStack.size() - 2 - dallocIndex * 2);
								}
							}
							geradorCodigo.geraStr(0);
							geradorCodigo.geraDalloc(allocStart, allocQtd);
							geradorCodigo.geraReturn();
						} else {
							throw new SemanticException("Erro Semantico na linha: " + token.linha + ", atribuicao nao reconhecida", token.linha, token.coluna);
						}
					}
				}
				
			}
			
		} else {
			chamadaProcedimento();
		}
	}

	public static void analisaSe() throws SintaticException, IOException, LexicException, SemanticException {
		infixList = new ArrayList<Token>();
		token = analisadorLexico.getToken();
		int auxRot, auxRot2;
		auxRot2 = 0;
		analisaExpressao();
		
		// gera posfix para comando se
		postfixList = geradorCodigo.geraPostFix(infixList);
		
		for (Token postfix : postfixList) { 
			System.out.print(postfix.lexema);
		}
		System.out.println("");
		
		ArrayList<Token> auxPostfix = new ArrayList<Token>();
		for (Token item : postfixList) auxPostfix.add(new Token(item));
		
		
		if (!geradorCodigo.validaPostFixBooleano(auxPostfix)) {
			throw new SemanticException("Erro Semantico na linha: " + token.linha + ", tipo invalido", token.linha, token.coluna);
		}
		
		geradorCodigo.geraCodigoDaPosfix(simbolStack);
		
		
		
		if (token.simbolo == "sentao") {
			auxRot = posicao;
			posicao++;
			geradorCodigo.geraJmpF(auxRot);
			
			token = analisadorLexico.getToken();
			analisaComandoSimples();
			
			
			if (token.simbolo == "ssenao") {
				auxRot2 = posicao;
				posicao++;
				
				geradorCodigo.geraJmp(auxRot2);
				geradorCodigo.geraNull(auxRot);
				
				token = analisadorLexico.getToken();
				analisaComandoSimples();
				
				geradorCodigo.geraNull(auxRot2);
			}else {
				geradorCodigo.geraNull(auxRot);
			}
			
			
			
			if (token.simbolo == "ssenao") {
				if (procurandoRetorno == true) {
					seEntaoStack.add("ssenaoN");
				}
				token = analisadorLexico.getToken();
				analisaComandoSimples();
				geradorCodigo.geraNull(auxRot2);
				if (procurandoRetorno == true) {
					if (seEntaoStack.get(seEntaoStack.size() - 1).equals("ssenaoS") && seEntaoStack.get(seEntaoStack.size() - 2).equals("sseS") ) {
						seEntaoStack.remove(seEntaoStack.size() - 1);
						seEntaoStack.remove(seEntaoStack.size() - 1);
						seEntaoStack.set(seEntaoStack.size() - 1, seEntaoStack.get(seEntaoStack.size() - 1).substring(0,seEntaoStack.get(seEntaoStack.size() - 1).length() - 1) + "S");
					} else {
						seEntaoStack.remove(seEntaoStack.size() - 1);
						seEntaoStack.remove(seEntaoStack.size() - 1);
					}
				}
			} else {
				if (procurandoRetorno == true) {
					// caso n tiver retorno desempilha ultimo por n garantir retorno
					seEntaoStack.remove(seEntaoStack.size() - 1);
				}
			}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", entao nao encontrado", token.linha, token.coluna);
		}
	}

	public static void analisaEnquanto() throws SintaticException, IOException, LexicException, SemanticException {
		infixList = new ArrayList<Token>();
		int auxrot1, auxrot2;

		auxrot1 = posicao;
		geradorCodigo.geraNull(posicao);
		posicao++;
		
		token = analisadorLexico.getToken();
		analisaExpressao();
		
		postfixList = geradorCodigo.geraPostFix(infixList);
		
		for (Token postfix : postfixList) { 		      		
			System.out.print(postfix.lexema);
		}
		System.out.println("");
		
		@SuppressWarnings("unchecked")
		ArrayList<Token> auxPostfix =(ArrayList<Token>) postfixList.clone();
		
		if (!geradorCodigo.validaPostFixBooleano(auxPostfix)) {
			throw new SemanticException("Erro Semantico na linha: " + token.linha + ", tipo invalido", token.linha, token.coluna);
		}
		
		geradorCodigo.geraCodigoDaPosfix(simbolStack);
		
		if (token.simbolo == "sfaca") {
			if (procurandoRetorno == true) {
				seEntaoStack.add("senquantoN");
			}
			
			auxrot2 = posicao;
			geradorCodigo.geraJmpF(posicao);
			posicao++;
			token = analisadorLexico.getToken();
			analisaComandoSimples();
			
			if (procurandoRetorno == true) {
				if (seEntaoStack.get(seEntaoStack.size() - 1).equals("senquantoS")) {
					seEntaoStack.remove(seEntaoStack.size() - 1);
					seEntaoStack.set(seEntaoStack.size() - 1, seEntaoStack.get(seEntaoStack.size() - 1).substring(0,seEntaoStack.get(seEntaoStack.size() - 1).length() - 1) + "S");
				}
			}
			
			geradorCodigo.geraJmp(auxrot1);
			geradorCodigo.geraNull(auxrot2);
			
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", comando faca nao encontrado", token.linha, token.coluna);
		}
	}

	public static void analisaLeia() throws SintaticException, SemanticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sabre_parenteses") {
			token = analisadorLexico.getToken();
			if (token.simbolo == "sidentificador") {
					if (simbolStack.verificaVarDeclarada(token.lexema)) {
						geradorCodigo.geraRd();
						geradorCodigo.geraStr(simbolStack.returnPosicao(token.lexema));
						token = analisadorLexico.getToken();
						if (token.simbolo == "sfecha_parenteses") {
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses nao fechado", token.linha, token.coluna);
						}
					} else {
						throw new SemanticException("Erro Semantico na linha:" + token.linha + ", variavel nao declarada", token.linha, token.coluna);
					}
			} else {
				throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", identificador invalido", token.linha, token.coluna);
			}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses esperado", token.linha, token.coluna);
		}

	}

	public static void analisaEscreva() throws SintaticException, SemanticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sabre_parenteses") {
			
					
					infixList = new ArrayList<Token>();
					token = analisadorLexico.getToken();
					analisaExpressao();
			
					// gera posfix para comando se
					postfixList = geradorCodigo.geraPostFix(infixList);
					
					ArrayList<Token> auxPostfix = new ArrayList<Token>();
					for (Token item : postfixList) auxPostfix.add(new Token(item));
					
					if (!geradorCodigo.validaPostFixInteiro(auxPostfix)) {
						throw new SemanticException("Erro Semantico na linha: " + token.linha + ", tipo invalido", token.linha, token.coluna);
					}
					geradorCodigo.geraCodigoDaPosfix(simbolStack);
					
					geradorCodigo.geraPrn();
					if (token.simbolo == "sfecha_parenteses") {
						token = analisadorLexico.getToken();
					} else {
						throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses nao fechado", token.linha, token.coluna);
					}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses esperado", token.linha, token.coluna);
		}
	}

	public static void analisaFator() throws SintaticException, IOException, LexicException, SemanticException {
		if (token.simbolo == "sidentificador") {
			if (simbolStack.verificaFuncaoVar(token.getLexema())) {
				if (simbolStack.verificaFuncDeclarada(token.getLexema())) {
					switch (simbolStack.verificaTipoIndentificador(token.lexema)) {
						case 3:
							token.simbolo = "funcao inteiro";
							break;
						case 4:
							token.simbolo = "funcao booleano";
							break;
					}
					infixList.add(token);
					chamadaFuncao();
				
				} else {
					switch (simbolStack.verificaTipoIndentificador(token.getLexema())) {
						case 1:
							token.simbolo = "variavel inteiro";
							break;
						case 2:
							token.simbolo = "variavel booleano";
							break;
					}
					infixList.add(token);
					token = analisadorLexico.getToken();	
				}
			} else {
				throw new SemanticException("Erro Semantico na linha:" + token.linha + ", identificador nao reconhecido", token.linha, token.coluna);
			}
		} else {
			if (token.simbolo == "snumero") {
				infixList.add(token);
				token = analisadorLexico.getToken();
			} else {
				if (token.simbolo == "snao") {
					infixList.add(token);
					token = analisadorLexico.getToken();
					analisaFator();
				} else {
					if (token.simbolo == "sabre_parenteses") {
						infixList.add(token);
						token = analisadorLexico.getToken();
						analisaExpressao();
						if (token.simbolo == "sfecha_parenteses") {
							infixList.add(token);
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", parenteses nao fechado", token.linha, token.coluna);
						}
					} else {
						if (token.lexema.equals("verdadeiro") || token.lexema.equals("falso")) {
							infixList.add(token);
							token = analisadorLexico.getToken();
						} else {
							throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", operador relacional invalido", token.linha, token.coluna);
						}
					}
				}
			}
		}
	}

	public static void analisaExpressao() throws IOException, LexicException, SintaticException, SemanticException {
		analisaExpressaoSimples();
		if (token.simbolo == "smaior" || token.simbolo == "smaiorig" || token.simbolo == "sig"
				|| token.simbolo == "smenor" || token.simbolo == "smenorig" || token.simbolo == "sdif") {
			infixList.add(token);
			token = analisadorLexico.getToken();
			analisaExpressaoSimples();
		}
	}

	public static void analisaExpressaoSimples() throws IOException, LexicException, SintaticException, SemanticException {
		if (token.simbolo == "smais" || token.simbolo == "smenos") { // caso der erro colocar token.simbolo == "smais" || de volta
			infixList.add(token);
			token = analisadorLexico.getToken();
		}
		analisaTermo();
		while (token.simbolo == "smais" || token.simbolo == "smenos" || token.simbolo == "sou") {
			infixList.add(token);
			token = analisadorLexico.getToken();
			analisaTermo();
		}
	}

	public static void analisaTermo() throws SintaticException, IOException, LexicException, SemanticException {
		analisaFator();
		while (token.simbolo == "smult" || token.simbolo == "sdiv" || token.simbolo == "se") {
			infixList.add(token);
			token = analisadorLexico.getToken();
			analisaFator();
		}
	}

	public static void analisaEtVariaveis() throws SintaticException, SemanticException, IOException, LexicException {
		if (token.simbolo == "svar") {
			variaveisDeclaradas = 0;
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

	public static void analisaVariaveis() throws SintaticException, SemanticException, IOException, LexicException {
		do {
			if (token.simbolo == "sidentificador") {
				if (!simbolStack.verificaDeclaDuplicVar(token.lexema)) {
					simbolStack.insereStack(token.lexema, "variavel", nivelList.get(nivelList.size() - 1), posicaoVariavel++);
					variaveisDeclaradas++;
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
					throw new SemanticException("Erro Semantico na linha:" + token.linha + ", variavel ja declarada", token.linha, token.coluna);
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
		} else {
			simbolStack.colocaTipo("variavel " + token.lexema, variaveisDeclaradas);
			
			geradorCodigo.geraAlloc(allocIndex, variaveisDeclaradas);
			allocStack.add(allocIndex);
			allocStack.add(variaveisDeclaradas);
			allocPerFuncProcStack.set(allocPerFuncProcStack.size() - 1, allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) + 1);
			allocIndex += variaveisDeclaradas;
			
			variaveisDeclaradas = 0;
		}
		token = analisadorLexico.getToken();
	}

	public static void analisaSubrotina() throws SintaticException, IOException, LexicException, SemanticException {
		int auxrot = 0;
		int flag = 0;
		
		if (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			auxrot = posicao;
			geradorCodigo.geraJmp(posicao);
			posicao++;
			flag = 1;
		}
		while (token.simbolo == "sprocedimento" || token.simbolo == "sfuncao") {
			allocPerFuncProcStack.add(0);
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
		if (flag == 1) {
			geradorCodigo.geraNull(auxrot);
		}
	}

	public static void analisaDeclaracaoProcedimento() throws SintaticException, IOException, LexicException, SemanticException {
		int auxVar;
		
		token = analisadorLexico.getToken();
		nivelMax++;
		nivelList.add(nivelMax);
		

		if (token.simbolo == "sidentificador") {
			if (!simbolStack.verificaDeclaDuplic(token.lexema)) {
				procFunDeclaPath.add("sprocedimento");
				
				simbolStack.insereStack(token.getLexema(), "procedimento", nivelList.get(nivelList.size() - 2), posicao);
				System.out.println(posicao);
				
				geradorCodigo.geraNull(posicao);
				posicao++;
				
				token = analisadorLexico.getToken();

				if (token.simbolo == "sponto_virgula") {
					analisaBloco();
					procFunDeclaPath.remove(procFunDeclaPath.size() - 1);
					if (allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) > 0) {
						for (int dallocIndex = allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1); dallocIndex > 0; dallocIndex--) {
							geradorCodigo.geraDalloc(allocStack.get(allocStack.size() - 2), allocStack.get(allocStack.size() - 1));
							auxVar = allocStack.remove(allocStack.size() - 1);
							posicaoVariavel = allocStack.remove(allocStack.size() - 1);
							allocIndex = allocIndex - auxVar;
						}
					}
					allocPerFuncProcStack.remove(allocPerFuncProcStack.size() - 1);
					geradorCodigo.geraReturn();
				} else {
					throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", esperado ponto e virgula", token.linha, token.coluna);
				}
			} else {
				throw new SemanticException("Erro Semantico na linha:" + token.linha + ", item ja existente", token.linha, token.coluna);
			}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", identificador invalido", token.linha, token.coluna);
		}
		
		simbolStack.limpaNivel(nivelList.get(nivelList.size() - 1));
		nivelList.remove(nivelList.size() - 1);
	}

	public static void analisaDeclaracaoFuncao() throws SintaticException, SemanticException, IOException, LexicException, SemanticException {
		int auxVar;
		token = analisadorLexico.getToken();
		nivelMax++;
		nivelList.add(nivelMax);
		
		if (token.simbolo == "sidentificador") {
			if (!simbolStack.verificaDeclaDuplic(token.simbolo)) {
				procFunDeclaPath.add("sfuncao");
				auxToken = token;
				token = analisadorLexico.getToken();

				if (token.simbolo == "sdoispontos") {
					token = analisadorLexico.getToken();

					if (token.simbolo == "sinteiro" || token.simbolo == "sbooleano") {
						if (token.simbolo == "sinteiro") {
							simbolStack.insereStack(auxToken.getLexema(), "funcao inteiro", nivelList.get(nivelList.size() - 2), posicao);
						}
						// senao
						if (token.simbolo == "sbooleano") {
							simbolStack.insereStack(auxToken.getLexema(), "funcao booleano", nivelList.get(nivelList.size() - 2), posicao);
						}
						
						geradorCodigo.geraNull(posicao);
						posicao++;
						token = analisadorLexico.getToken();

						if (token.simbolo == "sponto_virgula") {
							analisaBloco();
							
							procFunDeclaPath.remove(procFunDeclaPath.size() - 1);
							
							if (allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1) > 0) {
								for (int dallocIndex = allocPerFuncProcStack.get(allocPerFuncProcStack.size() - 1); dallocIndex > 0; dallocIndex--) {
									auxVar = allocStack.remove(allocStack.size() - 1);
									posicaoVariavel = allocStack.remove(allocStack.size() - 1);
									allocIndex = allocIndex - auxVar;
								}
							}
							allocPerFuncProcStack.remove(allocPerFuncProcStack.size() - 1);
						}
					} else {
						throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", tipo invalido", token.linha, token.coluna);
					}
				} else {
					throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", esperado dois poontos", token.linha, token.coluna);
				}
			} else {
				throw new SemanticException("Erro Semantico na linha:" + token.linha + ", item ja existente", token.linha, token.coluna);
			}
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", identificador invalido", token.linha, token.coluna);
		}
		simbolStack.limpaNivel(nivelList.get(nivelList.size() - 1));
		nivelList.remove(nivelList.size() - 1);
	}

	public static void chamadaProcedimento() throws SintaticException, IOException, LexicException {
		if (simbolStack.verificaProcDeclarada(auxToken.getLexema())) {
			geradorCodigo.geraCall(simbolStack.returnPosicao(auxToken.lexema));
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", esperado ponto e virgula apos chamada de procedimento", token.linha, token.coluna);
		}
	}
	
	public static void chamadaFuncao() throws SintaticException, IOException, LexicException {
		token = analisadorLexico.getToken();
		if (token.simbolo == "sponto_virgula" || token.simbolo == "smult" || token.simbolo == "sdiv" || token.simbolo == "smais" 
				|| token.simbolo == "smenos" || token.simbolo == "smaior" || token.simbolo == "smaiorig" 
				|| token.simbolo == "smenor" || token.simbolo == "smenorig" || token.simbolo == "sig" 
				|| token.simbolo == "sdif" || token.simbolo == "se" || token.simbolo == "sou" || token.simbolo == "sentao" 
				|| token.simbolo == "sfaca" || token.simbolo == "sfecha_parenteses" || token.simbolo == "sfim") {
		} else {
			throw new SintaticException("Erro Sintatico na linha:" + token.linha + ", caracter invalido", token.linha, token.coluna);
		}
	}
}
