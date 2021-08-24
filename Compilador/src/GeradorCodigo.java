import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GeradorCodigo {
	ArrayList<Token> postFix;
	ArrayList<Token> postfixStack;
	String codigoGerado = "";
	
	public ArrayList<Token> geraPostFix(ArrayList<Token> infix) throws SemanticException {
		postFix = new ArrayList<Token>();
		postfixStack = new ArrayList<Token>();
		int infixIndex = 0;
		while (infixIndex < infix.size()) {
				if (infix.get(infixIndex).simbolo.equals("smenos") || infix.get(infixIndex).simbolo.equals("smais")) {
					if (infixIndex == 0) {
						infix.get(infixIndex).simbolo += "unitario";
						postfixStack.add(infix.get(infixIndex));
					} else {
						if (!(infix.get(infixIndex - 1).simbolo.equals("variavel inteiro") || infix.get(infixIndex - 1).simbolo.equals("variavel booleano") || infix.get(infixIndex - 1).simbolo.equals("snumero")
							|| infix.get(infixIndex - 1).simbolo.equals("funcao booleano") || infix.get(infixIndex - 1).simbolo.equals("funcao inteiro") || infix.get(infixIndex - 1).simbolo.equals("sverdadeiro")
							|| infix.get(infixIndex - 1).simbolo.equals("sfalso") || infix.get(infixIndex - 1).simbolo.equals("sfecha_parenteses"))) { // se o token antes do operador menos/nao for um operador
							
							infix.get(infixIndex).simbolo += "unitario";
						}
						if (postfixStack.size() <= 0) {
							postfixStack.add(infix.get(infixIndex));
						} else {
							if (postfixStack.get(postfixStack.size() - 1).simbolo.equals("sabre_parenteses")) {
								postfixStack.add(infix.get(infixIndex));
							} else {
								if (postOperand(infix.get(infixIndex)) <= postOperand(postfixStack.get(postfixStack.size() - 1))) {
									postFix.add(postfixStack.get(postfixStack.size() - 1));
									postfixStack.remove(postfixStack.size() - 1);
									while (postfixStack.size() > 0) {
										if (postfixStack.get(postfixStack.size() - 1).simbolo.equals("sabre_parenteses")) {
											break;
										} //  
										if (postOperand(infix.get(infixIndex)) <= postOperand(postfixStack.get(postfixStack.size() - 1))) {
											postFix.add(postfixStack.get(postfixStack.size() - 1));
											postfixStack.remove(postfixStack.size() - 1);
										} else {
											break;
										}
									}
									postfixStack.add(infix.get(infixIndex));
								} else {
									postfixStack.add(infix.get(infixIndex));
								}
							}
						}
					}
				} else  {
						if (infix.get(infixIndex).simbolo.equals("variavel inteiro") || infix.get(infixIndex).simbolo.equals("variavel booleano") || infix.get(infixIndex).simbolo.equals("snumero")
								|| infix.get(infixIndex).simbolo.equals("funcao booleano") || infix.get(infixIndex).simbolo.equals("funcao inteiro") || infix.get(infixIndex).simbolo.equals("sverdadeiro")
								|| infix.get(infixIndex).simbolo.equals("sfalso")) {
								postFix.add(infix.get(infixIndex));
						} else {
							if (infix.get(infixIndex).simbolo.equals("sabre_parenteses")) {
								postfixStack.add(infix.get(infixIndex));
							} else {
								if (infix.get(infixIndex).simbolo.equals("sfecha_parenteses")) {
									while (postfixStack.get(postfixStack.size() - 1).simbolo != "sabre_parenteses") {
										postFix.add(postfixStack.get(postfixStack.size() - 1));
										postfixStack.remove(postfixStack.size() - 1);	
									}
									postfixStack.remove(postfixStack.size() - 1);
								} else {
									if (postfixStack.size() <= 0) {
										postfixStack.add(infix.get(infixIndex));
									} else {
										if (postfixStack.get(postfixStack.size() - 1).simbolo.equals("sabre_parenteses")) {
											postfixStack.add(infix.get(infixIndex));
										} else {
											if (postOperand(infix.get(infixIndex)) <= postOperand(postfixStack.get(postfixStack.size() - 1))) {
												postFix.add(postfixStack.get(postfixStack.size() - 1));
												postfixStack.remove(postfixStack.size() - 1);
												while (postfixStack.size() > 0) {
													if (postfixStack.get(postfixStack.size() - 1).simbolo.equals("sabre_parenteses")) {
														break;
													}
													if (postOperand(infix.get(infixIndex)) <= postOperand(postfixStack.get(postfixStack.size() - 1))) {
														postFix.add(postfixStack.get(postfixStack.size() - 1));
														postfixStack.remove(postfixStack.size() - 1);
													} else {
														break;
													}
												}
												postfixStack.add(infix.get(infixIndex));
											} else {
												postfixStack.add(infix.get(infixIndex));
											}
										}
									}
								}
							}			
					}	
				}
			infixIndex++;
		}
		while (postfixStack.size() > 0) {
			postFix.add(postfixStack.get(postfixStack.size() - 1));
			postfixStack.remove(postfixStack.size() - 1);
		}
		return postFix;
	}
	
	private int postOperand(Token token) {
		if (token.getSimbolo().equals("smenosunitario") || token.getSimbolo().equals("smaisunitario") || token.getSimbolo().equals("snao")) {
			return 6;
		} else {
			if (token.getSimbolo().equals("smult") || token.getSimbolo().equals("sdiv")) {
				return 5;
			} else {
				if (token.getSimbolo().equals("smais") || token.getSimbolo().equals("smenos")) {
					return 4;
				}
				if (token.getSimbolo().equals("smaior") || token.getSimbolo().equals("smenor") || token.getSimbolo().equals("smaiorig") || token.getSimbolo().equals("smenorig") || token.getSimbolo().equals("sig") || token.getSimbolo().equals("sdif")) {
					return 3;
				} else {
					if (token.getSimbolo().equals("se")) {
						return 2;
					} else {
						if (token.getSimbolo().equals("sou")) {
							return 1;
						}
					}
				}
			}
		}
		return -1;
	}

	public boolean validaPostFixBooleano(ArrayList<Token> postFix) {
		int postFixIndex = 0;
		Token auxToken;
		while (postFix.size() > 1) {
			postFixIndex = 0;
			while ((postFix.get(postFixIndex).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex).simbolo.equals("variavel booleano") || postFix.get(postFixIndex).simbolo.equals("snumero")
					|| postFix.get(postFixIndex).simbolo.equals("funcao booleano") || postFix.get(postFixIndex).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex).simbolo.equals("sverdadeiro")
					|| postFix.get(postFixIndex).simbolo.equals("sfalso"))) {
				postFixIndex++;
			}
			if (postFix.get(postFixIndex).simbolo.equals("sou") || postFix.get(postFixIndex).simbolo.equals("se")) {
				if (postFix.get(postFixIndex - 1).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 1).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 1).simbolo.equals("sverdadeiro")
						|| postFix.get(postFixIndex - 1).simbolo.equals("sfalso")) {
					if (postFix.get(postFixIndex - 2).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 2).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 2).simbolo.equals("sverdadeiro")
							|| postFix.get(postFixIndex - 2).simbolo.equals("sfalso")) {
						postFix.remove(postFixIndex);
						postFix.remove(postFixIndex - 1);
						auxToken = postFix.get(postFixIndex - 2);
						auxToken.simbolo = "variavel booleano";
						postFix.set(postFixIndex - 2, auxToken);
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (postFix.get(postFixIndex).simbolo.equals("snao")) {
					if (postFix.get(postFixIndex - 1).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 1).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 1).simbolo.equals("sverdadeiro")
							|| postFix.get(postFixIndex - 1).simbolo.equals("sfalso")) {
						postFix.remove(postFixIndex);
						auxToken = postFix.get(postFixIndex - 1);
						auxToken.simbolo = "variavel booleano";
						postFix.set(postFixIndex - 1, auxToken);
					} else {
						return false;
					}
				} else {
					if (postFix.get(postFixIndex).simbolo.equals("sig") || postFix.get(postFixIndex).simbolo.equals("sdif")) {
						if (postFix.get(postFixIndex - 1).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 1).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 1).simbolo.equals("sverdadeiro")
								|| postFix.get(postFixIndex - 1).simbolo.equals("sfalso")) {
							if (postFix.get(postFixIndex - 2).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 2).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 2).simbolo.equals("sverdadeiro")
									|| postFix.get(postFixIndex - 2).simbolo.equals("sfalso")) {
								postFix.remove(postFixIndex);
								postFix.remove(postFixIndex - 1);
								auxToken = postFix.get(postFixIndex - 2);
								auxToken.simbolo = "variavel booleano";
								postFix.set(postFixIndex - 2, auxToken);
							} else {
								return false;
							}
						} else {
							if (postFix.get(postFixIndex - 1).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("snumero")) {
								if (postFix.get(postFixIndex - 2).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("snumero")) {
									postFix.remove(postFixIndex);
									postFix.remove(postFixIndex - 1);
									auxToken = postFix.get(postFixIndex - 2);
									auxToken.simbolo = "variavel booleano";
									postFix.set(postFixIndex - 2, auxToken);
								} else {
									return false;
								}
							} else {
								return false;
							}
						}
					} else {
						if (postFix.get(postFixIndex).simbolo.equals("smaior") || postFix.get(postFixIndex).simbolo.equals("smenor") || postFix.get(postFixIndex).simbolo.equals("smaiorig")
						|| postFix.get(postFixIndex).simbolo.equals("smenorig")) {
							if (postFix.get(postFixIndex - 1).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("snumero")) {
								if (postFix.get(postFixIndex - 2).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("snumero")) {
									postFix.remove(postFixIndex);
									postFix.remove(postFixIndex - 1);
									auxToken = postFix.get(postFixIndex - 2);
									auxToken.simbolo = "variavel booleano";
									postFix.set(postFixIndex - 2, auxToken);
								} else {
									return false;
								}
							} else {
								return false;
							}
						} else {
							if (postFix.get(postFixIndex).simbolo.equals("smenosunitario")) {
									if (postFix.get(postFixIndex - 1).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("snumero")) {
										postFix.remove(postFixIndex);
										auxToken = postFix.get(postFixIndex - 1);
										auxToken.simbolo = "variavel inteiro";
										postFix.set(postFixIndex - 1, auxToken);
									} else {
										return false;
									}
							} else {
								if (postFix.get(postFixIndex).simbolo.equals("smais") || postFix.get(postFixIndex).simbolo.equals("smenos")
								|| postFix.get(postFixIndex).simbolo.equals("smult") || postFix.get(postFixIndex).simbolo.equals("sdiv")) {
									if (postFix.get(postFixIndex - 1).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("snumero")) {
										if (postFix.get(postFixIndex - 2).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("snumero")) {
											postFix.remove(postFixIndex);
											postFix.remove(postFixIndex - 1);
											auxToken = postFix.get(postFixIndex - 2);
											auxToken.simbolo = "variavel inteiro";
											postFix.set(postFixIndex - 2, auxToken);
										} else {
											return false;
										}
									} else {
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		postFixIndex = 0;
		if (postFix.get(postFixIndex).simbolo.equals("variavel booleano") || postFix.get(postFixIndex).simbolo.equals("funcao booleano") || postFix.get(postFixIndex).simbolo.equals("sverdadeiro")
				|| postFix.get(postFixIndex).simbolo.equals("sfalso")) {
			return true;	
		}
		return false;
	}
	
	public boolean validaPostFixInteiro(ArrayList<Token> postFix) {
		int postFixIndex = 0;
		Token auxToken;
		while (postFix.size() > 1) {
			postFixIndex = 0;
			while ( (postFix.get(postFixIndex).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex).simbolo.equals("variavel booleano") || postFix.get(postFixIndex).simbolo.equals("snumero")
					|| postFix.get(postFixIndex).simbolo.equals("funcao booleano") || postFix.get(postFixIndex).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex).simbolo.equals("sverdadeiro")
					|| postFix.get(postFixIndex).simbolo.equals("sfalso"))) {
				postFixIndex++;
			}
			if (postFix.get(postFixIndex).simbolo.equals("sou") || postFix.get(postFixIndex).simbolo.equals("se")) {
				if (postFix.get(postFixIndex - 1).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 1).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 1).simbolo.equals("sverdadeiro")
						|| postFix.get(postFixIndex - 1).simbolo.equals("sfalso")) {
					if (postFix.get(postFixIndex - 2).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 2).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 2).simbolo.equals("sverdadeiro")
							|| postFix.get(postFixIndex - 2).simbolo.equals("sfalso")) {
						postFix.remove(postFixIndex);
						postFix.remove(postFixIndex - 1);
						auxToken = postFix.get(postFixIndex - 2);
						auxToken.simbolo = "variavel booleano";
						postFix.set(postFixIndex - 2, auxToken);
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (postFix.get(postFixIndex).simbolo.equals("snao")) {
					if (postFix.get(postFixIndex - 1).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 1).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 1).simbolo.equals("sverdadeiro")
							|| postFix.get(postFixIndex - 1).simbolo.equals("sfalso")) {
						postFix.remove(postFixIndex);
						auxToken = postFix.get(postFixIndex - 1);
						auxToken.simbolo = "variavel booleano";
						postFix.set(postFixIndex - 1, auxToken);
					} else {
						return false;
					}
				} else {
					if (postFix.get(postFixIndex).simbolo.equals("sig") || postFix.get(postFixIndex).simbolo.equals("sdif")) {
						if (postFix.get(postFixIndex - 1).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 1).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 1).simbolo.equals("sverdadeiro")
								|| postFix.get(postFixIndex - 1).simbolo.equals("sfalso")) {
							if (postFix.get(postFixIndex - 2).simbolo.equals("variavel booleano") || postFix.get(postFixIndex - 2).simbolo.equals("funcao booleano") || postFix.get(postFixIndex - 2).simbolo.equals("sverdadeiro")
									|| postFix.get(postFixIndex - 2).simbolo.equals("sfalso")) {
								postFix.remove(postFixIndex);
								postFix.remove(postFixIndex - 1);
								auxToken = postFix.get(postFixIndex - 2);
								auxToken.simbolo = "variavel booleano";
								postFix.set(postFixIndex - 2, auxToken);
							} else {
								return false;
							}
						} else {
							if (postFix.get(postFixIndex - 1).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("snumero")) {
								if (postFix.get(postFixIndex - 2).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("snumero")) {
									postFix.remove(postFixIndex);
									postFix.remove(postFixIndex - 1);
									auxToken = postFix.get(postFixIndex - 2);
									auxToken.simbolo = "variavel booleano";
									postFix.set(postFixIndex - 2, auxToken);
								} else {
									return false;
								}
							} else {
								return false;
							}
						}
					} else {
						if (postFix.get(postFixIndex).simbolo.equals("smaior") || postFix.get(postFixIndex).simbolo.equals("smenor") || postFix.get(postFixIndex).simbolo.equals("smaiorig")
						|| postFix.get(postFixIndex).simbolo.equals("smenorig")) {
							if (postFix.get(postFixIndex - 1).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("snumero")) {
								if (postFix.get(postFixIndex - 2).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("snumero")) {
									postFix.remove(postFixIndex);
									postFix.remove(postFixIndex - 1);
									auxToken = postFix.get(postFixIndex - 2);
									auxToken.simbolo = "variavel booleano";
									postFix.set(postFixIndex - 2, auxToken);
								} else {
									return false;
								}
							} else {
								return false;
							}
						} else {
							if (postFix.get(postFixIndex).simbolo.equals("smenosunitario") || postFix.get(postFixIndex).simbolo.equals("smaisunitario")) {
									if (postFix.get(postFixIndex - 1).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("snumero")) {
										postFix.remove(postFixIndex);
										auxToken = postFix.get(postFixIndex - 1);
										auxToken.simbolo = "variavel inteiro";
										postFix.set(postFixIndex - 1, auxToken);
									} else {
										return false;
									}
							} else {
								if (postFix.get(postFixIndex).simbolo.equals("smais") || postFix.get(postFixIndex).simbolo.equals("smenos")
								|| postFix.get(postFixIndex).simbolo.equals("smult") || postFix.get(postFixIndex).simbolo.equals("sdiv")) {
									if (postFix.get(postFixIndex - 1).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 1).simbolo.equals("snumero")) {
										if (postFix.get(postFixIndex - 2).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("funcao inteiro") || postFix.get(postFixIndex - 2).simbolo.equals("snumero")) {
											postFix.remove(postFixIndex);
											postFix.remove(postFixIndex - 1);
											auxToken = postFix.get(postFixIndex - 2);
											auxToken.simbolo = "variavel inteiro";
											postFix.set(postFixIndex - 2, auxToken);
										} else {
											return false;
										}
									} else {
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		postFixIndex = 0;
		if (postFix.get(postFixIndex).simbolo.equals("variavel inteiro") || postFix.get(postFixIndex).simbolo.equals("funcao inteiro")
				|| postFix.get(postFixIndex).simbolo.equals("snumero")) {
			return true;	
		}
		return false;
	}

	
	
	public void geraCodigoDaPosfix(SimbolStack tabelaSimbolos) {
		int postFixIndex = 0;
		while (postFixIndex < postFix.size()) {
			switch (postFix.get(postFixIndex).simbolo) {
				case "funcao booleano":
				case "funcao inteiro":
					this.geraCall(tabelaSimbolos.returnPosicao(postFix.get(postFixIndex).lexema));
					this.geraLdv(0);
					break;
					
				case "variavel inteiro":
				case "variavel booleano":
					this.geraLdv(tabelaSimbolos.returnPosicao(postFix.get(postFixIndex).lexema));
					break;
					
				case "snumero":
					this.geraLdc(postFix.get(postFixIndex).lexema);
					break;
				
				case "sverdadeiro":
					this.geraLdc("1");
					break;
					
				case "sfalso":
					this.geraLdc("0");
					break;
				
				case "smenosunitario":
					this.geraInv();
						break;
						
				case "snao":
					this.geraNeg();
					break;
					
				case "smult":
					this.geraMult();
					break;
					
				case "sdiv":
					this.geraDiv();
					break;
					
				case "smais":
					this.geraAdd();;
					break;
					
				case "smenos":
					this.geraSub();
					break;
					
				case "smaior":
					this.geraCma();
					break;
					
				case "smenor":
					this.geraCme();
					break;
					
				case "smaiorig":
					this.geraCmaq();
					break;
					
				case "smenorig":
					this.geraCmeq();
					break;
					
				case "sig":
					this.geraCeq();
					break;
					
				case "sdif":
					this.geraCdif();
					break;
					
				case "se":
					this.geraAnd();
					break;
					
				case "sou":
					this.geraOr();
					break;
			}
			postFixIndex++;
		}
	}
	
	public void geraArquivo() throws IOException {
		FileWriter fileWriter = new FileWriter("object.txt");
	    fileWriter.write(codigoGerado);
	    fileWriter.close();
	}
	
	public void geraLdc(String k) {
		codigoGerado += "LDC " + k +"\n"; 
	}

	public void geraLdv(int k) {
		codigoGerado += "LDV " + k +"\n"; 
	}

	public void geraAdd() {
		codigoGerado += "ADD" + "\n"; 
	}

	public void geraSub() {
		codigoGerado += "SUB" + "\n"; 
	}

	public void geraMult() {
		codigoGerado += "MULT" + "\n"; 
	}

	public void geraDiv() {
		codigoGerado += "DIVI" + "\n"; 
	}

	public void geraInv() {
		codigoGerado += "INV" + "\n"; 
	}

	public void geraAnd() {
		codigoGerado += "AND" + "\n"; 
	}

	public void geraOr() {
		codigoGerado += "OR" + "\n"; 
	}

	public void geraNeg() {
		codigoGerado += "NEG" + "\n"; 
	}

	public void geraCme() {
		codigoGerado += "CME" + "\n"; 
	}

	public void geraCma() {
		codigoGerado += "CMA" + "\n"; 
	}

	public void geraCeq() {
		codigoGerado += "CEQ" + "\n"; 
	}

	public void geraCdif() {
		codigoGerado += "CDIF" + "\n"; 
	}

	public void geraCmeq() {
		codigoGerado += "CMEQ" + "\n"; 
	}

	public void geraCmaq() {
		codigoGerado += "CMAQ" + "\n"; 
	}

	public void geraStart() {
		codigoGerado += "START" + "\n";
	}

	public void geraHlt() {
		codigoGerado += "HLT" + "\n";
	}

	public void geraStr(int n) {
		codigoGerado += "STR " + n + "\n"; 
	}

	public void geraJmp(int t) {
		codigoGerado += "JMP " +"L" + t + "\n"; 
	}

	public void geraJmpF(int t) {
		codigoGerado += "JMPF " + "L" + t + "\n"; 
	}

	public void geraNull(int t) {
		codigoGerado += "L" + t + " NULL" + "\n"; 
	}

	public void geraRd() {
		codigoGerado += "RD" + "\n"; 
	}

	public void geraPrn() {
		codigoGerado += "PRN" + "\n"; 
	}

	public void geraAlloc(int m, int n) {
		codigoGerado += "ALLOC " + m + " " + n + "\n"; 
	}

	public void geraDalloc(int m, int n) {
		codigoGerado += "DALLOC " + m + " " + n + "\n"; 
	}

	public void geraCall(int t) {
		codigoGerado += "CALL " + "L" + t + "\n";
	}

	public void geraReturn() {
		codigoGerado += "RETURN" + "\n";
	}
}
