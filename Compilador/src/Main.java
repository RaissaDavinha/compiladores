import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		try {
			Token token = new Token();
			LexicAnalyser lexicAnalyser = new LexicAnalyser("lexico.txt");
			
			token = lexicAnalyser.getToken();
			System.out.println(token.toString());
			while(token.simbolo   != "sponto") {
				token = lexicAnalyser.getToken();
				System.out.println(token.toString());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LexicException e) {
			e.printStackTrace();
		}
	}
}
