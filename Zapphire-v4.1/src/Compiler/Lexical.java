package Compiler;

import CompilerResources.States;
import CompilerResources.Token;
import CompilerResources.TokenType;
import Class.FileManager;
import java.util.ArrayList;

/**
 * @author Christian Israel López Villalobos
 * @author Héctor Daniel Montañez Briano
 */
public class Lexical {
	private ArrayList<Token> reservedWords;
	private String code;
	private int index, column, line, colAnt;
	private String path, name, errors;
	private Token tokenBefore = new Token( TokenType.DO, "" );
	
	public Lexical( String path, String name ) {
		this.path = path;
		this.name = name;
		this.errors = "";
		reservedWords();
		code = FileManager.open( this.path + "/" + this.name + ".zph", true );
		start();
	}
	
	public void start() {
		index = 0;
		column = 0;
		line = 1;
		Token token = new Token();
		String lexico = "";
		while ( token.getType() != TokenType.EOF ) {
			token = getToken();
			if ( token.getType() == TokenType.ERROR ) {
				errors += "Error en la linea: " + line + ", columna: " + column + " el caracter \'" + token.getLexema() + "\' no concuerda\n";
			} else if ( token.getType() != TokenType.COMMENT && token.getType() != TokenType.COMMENT_MUL ) {
				lexico += token.getType().name() + " " + token.getLexema() + " " + line + " " + ( column - token.getLexema().length() +1 ) + "\n";
			}
		}
		FileManager.save( path + "/" + name + ".lzph", lexico );
	}
	
	private boolean isDelimiter( char c ) {
		char delimiters[] = { ' ', '\t', '\n' };
		for ( int i = 0; i < 3; i++ ) {
			if ( c == delimiters[i] ) {
				return true;
			}
		}
		return false;
	}

	private char getChar() {
		colAnt = column;
		char c = code.charAt( index++ );
		column++;
		if ( c == '\n' ) {
			line++;
			column = 0;
		}
		return c;
	}

	private TokenType checkReservedWords( String lexema ) {
		for ( Token i : reservedWords ) {
			if ( i.getLexema().equals( lexema ) ) {
				return i.getType();
			}
		}
		return TokenType.ID;
	}

	private Token getToken() {
		Token token = new Token();
		States state = States.IN_START;
		char c;
		boolean punto = false;
		while ( state != States.IN_DONE ) {
			switch ( state ) {
				case IN_START:
					c = getChar();
					while ( isDelimiter( c ) ) {
						c = getChar();
					}
					if ( Character.isLetter( c ) || c == '_' ) {
						state = States.IN_ID;
						token.addCharacter( c );
					} else if ( c == '+' ) {
						state = States.IN_INCREASE;
						token.addCharacter( c );
					} else if ( c == '-' ) {
						state = States.IN_DECREASE;
						token.addCharacter( c );
					} else if ( Character.isDigit( c ) ) {
						state = States.IN_NUM;
						token.addCharacter( c );
					} else if ( c == '<' ) {
						state = States.IN_LESS;
						token.addCharacter( c );
					} else if ( c == '>' ) {
						state = States.IN_GREATER;
						token.addCharacter( c );
					} else if ( c == '!' ) {
						state = States.IN_DIFERENT;
						token.addCharacter( c );
					} else if ( c == ':' ) {
						state = States.IN_ASSIGN;
						token.addCharacter( c );
					} else if ( c == '/' ) {
						state = States.IN_COMMENT;
						token.addCharacter( c );
					} else if ( c == '(' ) {
						state = States.IN_DONE;
						token.addCharacter( c );
						token.setType( TokenType.LPARENT );
					} else if ( c == ')' ) {
						state = States.IN_DONE;
						token.addCharacter( c );
						token.setType( TokenType.RPARENT );
					} else if ( c == '{' ) {
						state = States.IN_DONE;
						token.addCharacter( c );
						token.setType( TokenType.LBRACE );
					} else if ( c == '}' ) {
						state = States.IN_DONE;
						token.addCharacter( c );
						token.setType( TokenType.RBRACE );
					} else if ( c == '*' ) {
						state = States.IN_DONE;
						token.addCharacter( c );
						token.setType( TokenType.MULTI );
					} else if ( c == '=' ) {
						state = States.IN_DONE;
						token.addCharacter( c );
						token.setType( TokenType.EQUAL );
					} else if ( c == ',' ) {
						state = States.IN_DONE;
						token.addCharacter( c );
						token.setType( TokenType.COMMA );
					} else if ( c == ';' ) {
						state = States.IN_DONE;
						token.addCharacter( c );
						token.setType( TokenType.SEMICOLON );
					} else if ( c == ( char ) 3 ) {		// Fin de archivo
						token.setType( TokenType.EOF );
						state = States.IN_DONE;
						token.addCharacter( c );
					} else {
						state = States.IN_DONE;
						token.setType( TokenType.ERROR );
						token.addCharacter( c );
					}
					break;
				case IN_ID:
					c = getChar();
					if ( Character.isLetter( c ) || Character.isDigit( c ) || c == '_' ) {
						token.addCharacter( c );
					} else {
						token.setType( checkReservedWords( token.getLexema() ) );
						state = States.IN_DONE;
						adjust();
					}
					break;
				case IN_NUM:
					c = getChar();
					if ( Character.isDigit( c ) ) {
						token.addCharacter( c );
					} else if ( c == '.' && !punto ) {
						punto = true;
						token.addCharacter( c );
					} else {
						state = States.IN_DONE;
						if ( !punto ) {
							token.setType( TokenType.NUM_INT );
						} else {
							token.setType( TokenType.NUM_FLOAT );
						}
						if ( code.charAt( index - 2 ) == '.' ) {
							token.setType( TokenType.NUM_INT );
							token.setLexema( token.getLexema().substring( 0, token.getLexema().length() - 1 ) );
							adjust();
						}
						adjust();
					}
					break;

				case IN_INCREASE:
					c = getChar();
					state = States.IN_DONE;
					if ( c == '+' ) {
						token.addCharacter( c );
						token.setType( TokenType.INC );
					} else if ( tokenBefore.getType() == TokenType.NUM_INT || tokenBefore.getType() == TokenType.NUM_FLOAT || tokenBefore.getType() == TokenType.ID ) {
						token.setType( TokenType.PLUS );
						adjust();
					} else if ( Character.isDigit( c ) ) {
						state = States.IN_NUM;
						token.addCharacter( c );
					} else {
						token.setType( TokenType.PLUS );
						adjust();
					}
					break;

				case IN_DECREASE:
					c = getChar();
					if ( c == '-' ) {
						state = States.IN_DONE;
						token.addCharacter( c );
						token.setType( TokenType.DEC );
					} else if ( tokenBefore.getType() == TokenType.NUM_INT || tokenBefore.getType() == TokenType.NUM_FLOAT || tokenBefore.getType() == TokenType.ID ) {
						state = States.IN_DONE;
						token.setType( TokenType.MINUS );
						adjust();
					} else if ( Character.isDigit( c ) ) {
						token.addCharacter( c );
						state = States.IN_NUM;
					} else {
						state = States.IN_DONE;
						token.setType( TokenType.MINUS );
						adjust();
					}
					break;

				case IN_LESS:
					c = getChar();
					state = States.IN_DONE;
					if ( c == '=' ) {
						token.setType( TokenType.LESS_EQUAL );
						token.addCharacter( c );
					} else {
						token.setType( TokenType.LESS );
						adjust();
					}
					break;
				case IN_GREATER:
					c = getChar();
					state = States.IN_DONE;
					if ( c == '=' ) {
						token.setType( TokenType.GREATER_EQUAL );
						token.addCharacter( c );
					} else {
						token.setType( TokenType.GREATER );
						adjust();
					}
					break;
				case IN_DIFERENT:
					c = getChar();
					state = States.IN_DONE;
					if ( c == '=' ) {
						token.setType( TokenType.DIFFERENT );
						token.addCharacter( c );
					} else {
						token.setType( TokenType.ERROR );
						adjust();
					}
					break;
				case IN_ASSIGN:
					c = getChar();
					state = States.IN_DONE;
					if ( c == '=' ) {
						token.setType( TokenType.ASSIGN );
						token.addCharacter( c );
					} else {
						token.setType( TokenType.ERROR );
						adjust();
					}
					break;
				case IN_COMMENT:
					c = getChar();
					state = States.IN_DONE;
					if ( c == '/' ) {
						token.setType( TokenType.COMMENT );
						token.addCharacter( c );
						while ( c != '\n' ) {
							c = getChar();
						}
					} else if ( c == '*' ) {
						token.addCharacter( c );
						token.setType( TokenType.COMMENT_MUL );
						c = getChar();
						while ( c != '*' || code.charAt( index ) != '/' ) {
							c = getChar();
							if ( c == ( char ) 3 ) {
								adjust();
								adjust();			//Se entra 2 veces al ajuste 
								break;
							}
						}
						index++;
						column++;
					} else {
						token.setType( TokenType.DIV );
						adjust();
					}
					break;
			}
			if ( token.getType() != null ) {
				tokenBefore.setType( token.getType() );
			}
		}
		return token;
	}

	private void adjust() {
		index--;
		column--;
		if ( code.charAt( index ) == '\n' ) {
			line--;
			column = colAnt;
		}
	}

	private void reservedWords() {
		reservedWords = new ArrayList<>();
		reservedWords.add( new Token( TokenType.IF, "if" ) );
		reservedWords.add( new Token( TokenType.THEN, "then" ) );
		reservedWords.add( new Token( TokenType.ELSE, "else" ) );
		reservedWords.add( new Token( TokenType.FI, "fi" ) );
		reservedWords.add( new Token( TokenType.DO, "do" ) );
		reservedWords.add( new Token( TokenType.UNTIL, "until" ) );
		reservedWords.add( new Token( TokenType.WHILE, "while" ) );
		reservedWords.add( new Token( TokenType.READ, "read" ) );
		reservedWords.add( new Token( TokenType.WRITE, "write" ) );
		reservedWords.add( new Token( TokenType.FLOAT, "float" ) );
		reservedWords.add( new Token( TokenType.INT, "int" ) );
		reservedWords.add( new Token( TokenType.BOOL, "bool" ) );
		reservedWords.add( new Token( TokenType.PROGRAM, "program" ) );
	}

	public String getErrors() {
		return errors;
	}
}
