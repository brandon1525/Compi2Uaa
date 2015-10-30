package RecursosCompi;

/**
 * @author Christian Israel López Villalobos
 * @author Héctor Daniel Montañez Briano
 */
public enum TokenType {
	
	IF, THEN, ELSE, FI,	DO,
	UNTIL, WHILE, READ, WRITE, FLOAT,
	INT, BOOL, PROGRAM, ID, NUM_INT, NUM_FLOAT,
	
	PLUS, MINUS, MULTI, DIV, LESS, 
	INC, DEC,LESS_EQUAL, GREATER, GREATER_EQUAL, 
	EQUAL, DIFFERENT, ASSIGN, SEMICOLON, COMMA, 
	LPARENT, RPARENT, LBRACE, RBRACE, COMMENT, 
	COMMENT_MUL, EOF, ERROR
	
}
