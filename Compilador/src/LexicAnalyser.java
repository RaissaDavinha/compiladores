import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LexicAnalyser {
	//Variaveis do analisador lexico	
	private FileReader fileReader;
	private BufferedReader reader;
	private String auxContent;
	private String fileContent;
	private Token token;
	private int fileContentIndex;
	private char controlCharacter;
	private String tokenBuilder;
	
	/**
	 * Procedimento que le o arquivo a ser analisado pelo lexico.
	 * Chamado no sintaticMain() para definir o lexico
	 * Coloca o conteudo do arquivo na String fileContent, cada linha separada por um /n
	 * Parametros:
	 * String file = caminho do arquivo que vai ser analisado 
	 */
	public LexicAnalyser(String fileName) throws IOException {
		fileReader = new FileReader(fileName);
		reader = new BufferedReader(fileReader);
		fileContent = "";
		fileContentIndex = 0;
		auxContent = reader.readLine();
		
		while (auxContent != null) {
			fileContent += auxContent;
			fileContent += '\n';
			auxContent = reader.readLine();
		}

		fileReader.close();

		auxContent = fileContent;

		fileContent = auxContent.replace("\t", "");
	}

	/**
	 * Funcao que retorna o proximo token. 
	 * Usado no sintaticMain() e seus diversos procedimentos seguintes.
	 */
	public Token getToken() throws IOException, LexicException {
		token = new Token();

		// Le primeiro caracter
		
		if (fileContentIndex < fileContent.length()) {
			controlCharacter = fileContent.charAt(fileContentIndex);
		} else {
			return null;
		}
		
		fileContentIndex++;

		while (controlCharacter == ' ' || controlCharacter == '\n' || controlCharacter == '{') {
			// Le todos os espacos
			if (controlCharacter == ' ') {
				while (controlCharacter == ' ') {
					if (fileContentIndex < fileContent.length()) {
						controlCharacter = fileContent.charAt(fileContentIndex);
						fileContentIndex++;
					} else {
						token.setSimbolo("error");
						token.setLexema("");
						token.setLinha(0);
						token.setColuna(0);
						return token;
					}
				}
			}
			if (controlCharacter == '\n') {
				while (controlCharacter == '\n') {
					if (fileContentIndex < fileContent.length()) {
						controlCharacter = fileContent.charAt(fileContentIndex);
						fileContentIndex++;
					} else {
						token.setSimbolo("error");
						token.setLexema("");
						token.setLinha(0);
						token.setColuna(0);
						return token;
					}
				}
			}
			//Eat comment type 1
			if (controlCharacter == '{') {
				while (controlCharacter != '}') {
					if (fileContentIndex < fileContent.length()) {
						controlCharacter = fileContent.charAt(fileContentIndex);
						fileContentIndex++;
					} else {
						throw new LexicException("Erro Lexico na linha: " + returnLineOfToken(fileContentIndex - 1, fileContent) + ", comentario sem final", 
								returnLineOfToken(fileContentIndex - 1, fileContent), 
								returnEndColumnOfToken(fileContentIndex - 1, fileContent));
					}

				}
				controlCharacter = fileContent.charAt(fileContentIndex);
				fileContentIndex++;
			}
		
		}
		
		//Se o primeiro caracter for um numero
		if (Character.isDigit(controlCharacter)) {
			tokenBuilder = "";
			while (Character.isDigit(controlCharacter)) {
				tokenBuilder = tokenBuilder + controlCharacter;
				if (fileContentIndex < fileContent.length()) {
					controlCharacter = fileContent.charAt(fileContentIndex);
					fileContentIndex++;
				}
			}
			fileContentIndex--;
			token.setSimbolo("snumero"); //Sempre que comeca com numero o token é um snumero
			token.setLexema(tokenBuilder);
			token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
			token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());

		} else {
			//Se o primeiro caracter for uma letra
			if (Character.isLetter(controlCharacter) && controlCharacter != ';'
					&& controlCharacter != ':') {
				tokenBuilder = "";
				while ((controlCharacter == '_' || Character.isDigit(controlCharacter)
						|| Character.isLetter(controlCharacter)) && controlCharacter != ';'
						&& controlCharacter != ':' && controlCharacter != '\n') {
					tokenBuilder += controlCharacter;
					if (fileContentIndex < fileContent.length()) {
						controlCharacter = fileContent.charAt(fileContentIndex);
						if ((controlCharacter == '_' || Character.isDigit(controlCharacter) || Character.isLetter(controlCharacter)) && controlCharacter != ';'
								&& controlCharacter != ':' && controlCharacter != '\n')
							fileContentIndex++;
					}
				}
				token.setLexema(tokenBuilder);
				token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
				token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
				
				switch (tokenBuilder) {
				
				//Possiveis palavras reservadas
				case "programa":
					token.setSimbolo("sprograma");
					break;

				case "se":
					token.setSimbolo("sse");
					break;

				case "entao":
					token.setSimbolo("sentao");
					break;

				case "senao":
					token.setSimbolo("ssenao");
					break;

				case "enquanto":
					token.setSimbolo("senquanto");
					break;

				case "faca":
					token.setSimbolo("sfaca");
					break;

				case "inicio":
					token.setSimbolo("sinicio");
					break;

				case "fim":
					token.setSimbolo("sfim");
					break;

				case "escreva":
					token.setSimbolo("sescreva");
					break;

				case "leia":
					token.setSimbolo("sleia");
					break;

				case "var":
					token.setSimbolo("svar");
					break;

				case "inteiro":
					token.setSimbolo("sinteiro");
					break;

				case "booleano":
					token.setSimbolo("sbooleano");
					break;

				case "verdadeiro":
					token.setSimbolo("sverdadeiro");
					break;

				case "falso":
					token.setSimbolo("sfalso");
					break;

				case "procedimento":
					token.setSimbolo("sprocedimento");
					break;

				case "funcao":
					token.setSimbolo("sfuncao");
					break;

				case "div":
					token.setSimbolo("sdiv");
					break;

				case "e":
					token.setSimbolo("se");
					break;
					
				case "ou":
					token.setSimbolo("sou");
					break;
					
				case "nao":
					token.setSimbolo("snao");
					break;
				
				//Se nao é uma palavra reservada, é um identificador
				default:
					token.setSimbolo("sidentificador");
				}
			} else {
				//Se o caracter for :
				if (controlCharacter == ':') {
					tokenBuilder = "";
					if (fileContentIndex < fileContent.length()) {
						//e se o proximo for =, atribuicao de valor
						if (fileContent.charAt(fileContentIndex) == '=') {
							fileContentIndex++;
							tokenBuilder = ":=";
							token.setLexema(tokenBuilder);
							token.setSimbolo("satribuicao");
							token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
							token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
						} else {
							//se nao, : normal
							tokenBuilder = ":";
							token.setLexema(tokenBuilder);
							token.setSimbolo("sdoispontos");
							token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
							token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
						}
					} else {
						tokenBuilder = ":";
						token.setLexema(tokenBuilder);
						token.setSimbolo("sdoispontos");
						token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
						token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
					}
				} else {
					// Tratar Operador Aritmetico
					if (controlCharacter == '+' || controlCharacter == '-' || controlCharacter == '*') {
						tokenBuilder = "";
						if (controlCharacter == '+') {
							token.setSimbolo("smais");
						} else {
							if (controlCharacter == '-') {
								token.setSimbolo("smenos");
							} else {
								if (controlCharacter == '*') {
									token.setSimbolo("smult");
								}
							}
						}
						tokenBuilder = Character.toString(controlCharacter);
						token.setLexema(tokenBuilder);
						token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
						token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
					} else {
						// Trata Operador Relacional
						if (controlCharacter == '<' || controlCharacter == '>'
							|| controlCharacter == '=' || controlCharacter == '!') {
							tokenBuilder = "";
							//Comparador igual
							if (controlCharacter == '=') {
								token.setSimbolo("sig");
							} else {
								if (controlCharacter == '!') {
									if (fileContentIndex < fileContent.length()) {
										if (fileContent.charAt(fileContentIndex) == '=') {
											tokenBuilder += Character.toString(controlCharacter);
											controlCharacter = fileContent.charAt(fileContentIndex);
											fileContentIndex++;
											token.setSimbolo("sdif");
										} else {
											//Erro por ter um !
											throw new LexicException("Erro Lexico na linha:" + returnLineOfToken(fileContentIndex - 1, fileContent) + ", caractere invalido", 
													returnLineOfToken(fileContentIndex - 1, fileContent), 
													returnEndColumnOfToken(fileContentIndex - 1, fileContent));
										}
									} else {
										//Erro ao chegar no fim do arquivo de forma inesperada
										throw new LexicException("Erro Lexico na linha:" + returnLineOfToken(fileContentIndex - 1, fileContent) + ", fim de arquivo", 
												returnLineOfToken(fileContentIndex - 1, fileContent), 
												returnEndColumnOfToken(fileContentIndex - 1, fileContent));
									}
								} else {
									//Token comparativo maior / maior ou igual
									if (controlCharacter == '>') {
										if (fileContentIndex < fileContent.length()) {
											if (fileContent.charAt(fileContentIndex) == '=') {
												tokenBuilder += Character.toString(controlCharacter);
												controlCharacter = fileContent.charAt(fileContentIndex);
												fileContentIndex++;
												token.setSimbolo("smaiorig");
											} else {
												token.setSimbolo("smaior");
											}
										} else {
											token.setSimbolo("smaior");
										}
									} else {
										//Token comparativo menor / menor ou igual
										if (fileContentIndex < fileContent.length()) {
											if (fileContent.charAt(fileContentIndex) == '=') {
												tokenBuilder += Character.toString(controlCharacter);
												controlCharacter = fileContent.charAt(fileContentIndex);
												fileContentIndex++;
												token.setSimbolo("smenorig");
											} else {
												token.setSimbolo("smenor");
											}
										} else {
											token.setSimbolo("smenor");
										}
										
									}
								}
							}
							tokenBuilder += Character.toString(controlCharacter);
							token.setLexema(tokenBuilder);
							token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
							token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
						} else {
							// Trata Pontuacao
							if (controlCharacter == ';' || controlCharacter == ','
									|| controlCharacter == '(' || controlCharacter == ')'
									|| controlCharacter == '.') {
								if (controlCharacter == ';') {
									tokenBuilder = ";";
									token.setLexema(tokenBuilder);
									token.setSimbolo("sponto_virgula");
									token.setLinha(
											returnLineOfToken(fileContentIndex - 1, fileContent));
									token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
								}
								if (controlCharacter == ',') {
									tokenBuilder = ",";
									token.setLexema(tokenBuilder);
									token.setSimbolo("svirgula");
									token.setLinha(
											returnLineOfToken(fileContentIndex - 1, fileContent));
									token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
								}
								if (controlCharacter == '(') {
									tokenBuilder = "(";
									token.setLexema(tokenBuilder);
									token.setSimbolo("sabre_parenteses");
									token.setLinha(returnLineOfToken(fileContentIndex - 1, fileContent));
									token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
								}
								if (controlCharacter == ')') {
									tokenBuilder = ")";
									token.setLexema(tokenBuilder);
									token.setSimbolo("sfecha_parenteses");
									token.setLinha(
											returnLineOfToken(fileContentIndex - 1, fileContent));
									token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
								}
								if (controlCharacter == '.') {
									tokenBuilder = ".";
									token.setLexema(tokenBuilder);
									token.setSimbolo("sponto");
									token.setLinha(
											returnLineOfToken(fileContentIndex - 1, fileContent));
									token.setColuna(returnEndColumnOfToken(fileContentIndex - 1, fileContent) - token.lexema.length());
								}
							} else {
								//Caso nenhum seja verdade, caracter nao é valido
								throw new LexicException("Erro Lexico na linha: " + returnLineOfToken(fileContentIndex - 1, fileContent) + ", caractere valido nao encontrado", 
										returnLineOfToken(fileContentIndex - 1, fileContent), 
										returnEndColumnOfToken(fileContentIndex - 1, fileContent));
							}
						}
					}
				}
			}
		}
		
		return token;
	}

	/**
	 * Funcao que retorna a linha que o token encontrado esta.
	 * Usada na funcao getToken()
	 * Parametros:
	 * int fileContentIndex = indice do token, posicao relativa ao primeiro token do codigo
	 * String fileContent = conteudo do arquivo sendo anlisado
	 */
	private int returnLineOfToken(int fileContentIndex, String fileContent) {
		int lineNumber;
		int i;
		
		//Anda do token 0 ate o token encontrado e ve o numero da linha pelo numero de \n que encontra no caminho.
		for (lineNumber = 0, i = 0; i < fileContentIndex; i++) {
			if (fileContent.charAt(i) == '\n') {
				lineNumber++;
			}
		}
		return lineNumber + 1;
	}
	
	/**
	 * Funcao que retorna a coluna que o token encontrado esta.
	 * Usada na funcao getToken()
	 * Parametros:
	 * int fileContentIndex = indice do token, posicao relativa ao primeiro token do codigo
	 * String fileContent = conteudo do arquivo sendo anlisado
	 */
	private int returnEndColumnOfToken(int fileContentIndex, String fileContent) {
		int auxLinha = 0;
		int i = 0;
		while (token.getLinha() - 1 > auxLinha) {
			if (fileContent.charAt(i) == '\n') {
				auxLinha++;
			}
			i++;
		}
		return fileContentIndex - i - 1;
	}
}