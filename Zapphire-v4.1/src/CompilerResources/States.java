package CompilerResources;

/**
 * @author Christian Israel López Villalobos
 * @author Héctor Daniel Montañez Briano
 */
public enum States {
	
	IN_IF, IN_THEN, IN_ELSE, IN_FI,	IN_DO,
	IN_UNTIL, IN_WHILE, IN_READ, IN_WRITE, IN_FLOAT,
	IN_INT, IN_BOOL, IN_PROGRAM, IN_ID, IN_NUM,
	IN_START, IN_DONE,
	
	IN_LESS, IN_GREATER, IN_DIFERENT, IN_ASSIGN, IN_LPAREN, IN_RPAREN,
	IN_LBRACE, IN_RBRACE, IN_COMMENT, IN_LCOMMENTM, IN_RCOMMENTM,
	IN_EOF, IN_ERROR, IN_INCREASE, IN_DECREASE
	
}
