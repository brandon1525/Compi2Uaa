package CompilerResources;

/**
 * @author Christian Israel López Villalobos
 * @author Héctor Daniel Montañez Briano
 */
public class Token {
	
	private TokenType type;
	private String lexema;

	public Token() {
		lexema = "";
	}

	public Token( TokenType type, String lexema ) {
		this.type = type;
		this.lexema = lexema;
	}
	
	public TokenType getType() {
		return type;
	}

	public void setType( TokenType type ) {
		this.type = type;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLexema( String lexema ) {
		this.lexema = lexema;
	}
	
	public void addCharacter( char c ) {
		this.lexema += c;
	}
		
}
