package Compiler;

import CompilerResources.*;
import Class.FileManager;
import com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author Christian Israel López Villalobos
 * @author Héctor Daniel Montañez Briano
 */
public class Syntactic {

	private int line;
	private String path, name, errors, tokens[][];
	private TokenType token;
	private DefaultTreeModel dtm;
	private SyntacticTreeNode root;

	public Syntactic( String path, String name ) {
		this.path = path;
		this.name = name;
		this.line = -1;
		this.tokens = FileManager.getTokens( this.path + "/" + this.name + ".lzph" );
		this.errors = "";
		getToken();
		this.root = program();
		this.dtm = new DefaultTreeModel( root );
	}
	
	// program → program { lista-declaración lista-sentencias }
	private SyntacticTreeNode program() {
		SyntacticTreeNode t = null;
		if( token == TokenType.PROGRAM ) {
			t = SyntacticTreeNode.newStatementNode( "programa", StatementKind.PROGRAM, getLineToken() );
			match( TokenType.PROGRAM );
			match( TokenType.LBRACE );
			while( token != TokenType.EOF && token != TokenType.RBRACE ) {
				declarationList( t );
				statementList( t );
			}
			match( TokenType.RBRACE );
		}
		return t;
	}
	
	// lista-declaración → declaración ; lista-declaración | vació
	private SyntacticTreeNode declarationList( SyntacticTreeNode root ) {
		SyntacticTreeNode t = declaration();
		if( t != null ) {
			root.add( t );
			match( TokenType.SEMICOLON );
			declarationList( root ); //Recursivo para ver si hay mas declaraciones
		}
		return t;
	}
	
	// declaración → tipo lista-variables
	// tipo → int | float | bool
	private SyntacticTreeNode declaration() {
		SyntacticTreeNode t = null, p = null;
		if( token == TokenType.INT || token == TokenType.FLOAT || token == TokenType.BOOL ) {
			t = SyntacticTreeNode.newDeclarationNode( "declaración" );
			p = SyntacticTreeNode.newDeclarationNode( getComponetToken(), DeclarationKind.TYPE, getLineToken() );
			p.setDeclarationType( getDeclarationType() );
			t.add( p );
			//t = new SyntacticTreeNode( "declaración", NodeKind.DECLARATION_NODE );
			//q = new SyntacticTreeNode( getComponetToken(), NodeKind.DECLARATION_NODE, getTypeKind() );
			//t.add( q );
			match( token );
			variableList( p ); //Ver las variables de esta declaracion y se envia como raíz el INT FLOAT O BOOL
		}
		return t;
	}
	
	// lista-variables → identificador, lista-variables | identificador
	private void variableList( SyntacticTreeNode root ) {
		SyntacticTreeNode t = null;
		if( token == TokenType.ID ) {
			t = SyntacticTreeNode.newDeclarationNode( getComponetToken(), DeclarationKind.ID, getLineToken() );
			root.add( t );
			match( TokenType.ID );
			if( token == TokenType.COMMA ) {
				match( TokenType.COMMA );
				variableList( root ); //recursivo para ver si hay mas variables de este tipo
			}
		}
	}
	
	// lista-sentencias → sentencia lista-sentencias | sentencia | vació
	private void statementList( SyntacticTreeNode root ) {
		SyntacticTreeNode t = statement();
		if( t != null ) {
			root.add( t );
			statementList( root );//Recursivo para ver la lista de sentencias
		}
	}
	
	// sentencia → selección | iteración | repetición | sent-read | sent-write | bloque | asignación
	private SyntacticTreeNode statement() {
		SyntacticTreeNode t = null;
		switch ( token ) {
			case IF:				// selección
				t = ifStmt();
				break;
			case WHILE:				// iteración
				t = whileStmt();
				break;
			case DO:				// repetición
				t = doStmt();
				break;
			case READ:				// sent-read
				t = readStmt();
				break;
			case WRITE:				// sent-write
				t = writeStmt();
				break;
			case LBRACE:			// bloque
				t = block( "bloque" );
				break;
			case ID:				// asignación	
				t = assignStmt();
				break;
			case INC: case DEC:
				t = incDecStmt();
				break;
			default:
				if( token == TokenType.ASSIGN  ) {
					error( TokenType.ID );
					consumeAllLine();
				} else if( token == TokenType.UNTIL ) {
					error( TokenType.DO );
					consumeAllLine();
				} else if( token == TokenType.MULTI || 
						token == TokenType.DIV ||
						token == TokenType.PLUS || 
						token == TokenType.MINUS || 
						token == TokenType.NUM_FLOAT || 
						token == TokenType.NUM_INT || 
						token == TokenType.LPARENT || 
						token == TokenType.RPARENT 
					) {
					error( TokenType.ID );
					consumeAllLine2();
				} else if( token == TokenType.SEMICOLON ) {
					error( TokenType.LBRACE );
					consumeAllLine();
				}
		}
		return t;
	}
	
	// selección → if ( expresión ) then bloque fi | if ( expresión ) then bloque else bloque fi
	private SyntacticTreeNode ifStmt() {		// sentencia-if
		SyntacticTreeNode t = SyntacticTreeNode.newStatementNode( "if", StatementKind.IF, getLineToken() );
		match( TokenType.IF );
		match( TokenType.LPARENT );
		t.add( exp() );
		match( TokenType.RPARENT );
		
		match( TokenType.THEN );
		t.add( block( "Caso Verdadero" ) );
		if ( token == TokenType.ELSE ) {
			match( TokenType.ELSE );
			t.add( block( "Caso Falso" ) );
		}
		match( TokenType.FI );//Debe haber un fi ya sea el caso verdadero o falso
		return t;
	}
	
	// iteración → while ( expresión ) bloque
	private SyntacticTreeNode whileStmt() {
		SyntacticTreeNode t = SyntacticTreeNode.newStatementNode( "while", StatementKind.WHILE, getLineToken() );
		match( TokenType.WHILE );
		match( TokenType.LPARENT );
		t.add( exp() );
		match( TokenType.RPARENT );
		t.add( block( "Bloque while" ) );
		return t;
	}
	
	// repetición → do bloque until ( expresión ) ;
	private SyntacticTreeNode doStmt() {
		SyntacticTreeNode t = SyntacticTreeNode.newStatementNode( "do", StatementKind.DO, getLineToken() );
		match( TokenType.DO );
		t.add( block( "Bloque do" ) );
		match( TokenType.UNTIL );
		match( TokenType.LPARENT );
		t.add( exp() );
		match( TokenType.RPARENT );
		match( TokenType.SEMICOLON );
		return t;
	}
	
	// sent-read → read identificador ;
	private SyntacticTreeNode readStmt() {
		SyntacticTreeNode t = SyntacticTreeNode.newStatementNode( "read", StatementKind.READ, getLineToken() );
		match( TokenType.READ );
		if( token == TokenType.ID ){
			t.add( SyntacticTreeNode.newExpressionNode( getComponetToken(), ExpressionKind.ID, getLineToken() ) ); 
		}
		match( TokenType.ID );
		match( TokenType.SEMICOLON );
		return t;
	}
	
	// sent-write → write expresión ;
	private SyntacticTreeNode writeStmt() {
		SyntacticTreeNode t = SyntacticTreeNode.newStatementNode( "write", StatementKind.WRITE, getLineToken() );
		match( TokenType.WRITE );
		t.add( exp() );
		match( TokenType.SEMICOLON );
		return t;
	}
	
	// asignación → identificador := expresión ;
	private SyntacticTreeNode assignStmt() {
		SyntacticTreeNode expressionId = null;
		if ( token == TokenType.ID ) {
			expressionId = SyntacticTreeNode.newExpressionNode( getComponetToken(), ExpressionKind.ID, getLineToken() );
			String id = expressionId.getName();		// nombre del id
			match( TokenType.ID );
			SyntacticTreeNode statementAssing, expressionOperator, expressionConst;
			switch( token ) {
				case ASSIGN:
					statementAssing = SyntacticTreeNode.newStatementNode( getComponetToken(), StatementKind.ASSING, getLineToken() );
					statementAssing.add( expressionId );
					expressionId = statementAssing;
					match( TokenType.ASSIGN );
					expressionId.add( exp() );
					break;
				case INC:
					match( TokenType.INC );
					statementAssing = SyntacticTreeNode.newStatementNode( ":=", StatementKind.ASSING, getLineToken() );
					statementAssing.add( expressionId );
					expressionId = statementAssing;
					expressionOperator = SyntacticTreeNode.newExpressionNode( "+", ExpressionKind.OP, getLineToken() );
					expressionOperator.setExpressionOp( ExpressionOp.PLUS );
					expressionOperator.add( SyntacticTreeNode.newExpressionNode( id, ExpressionKind.ID, getLineToken() ) );
					expressionConst = SyntacticTreeNode.newExpressionNode( "1", ExpressionKind.CONSTANT, getLineToken() );
					expressionConst.setValue( 1 );
					expressionOperator.add( expressionConst );
					expressionId.add( expressionOperator );
					break;
				case DEC:
					match( TokenType.DEC );
					statementAssing = SyntacticTreeNode.newStatementNode( ":=", StatementKind.ASSING, getLineToken() );
					statementAssing.add( expressionId );
					expressionId = statementAssing;
					expressionOperator = SyntacticTreeNode.newExpressionNode( "-", ExpressionKind.OP, getLineToken() );
					expressionOperator.setExpressionOp( ExpressionOp.MINUS );
					expressionOperator.add( SyntacticTreeNode.newExpressionNode( id, ExpressionKind.ID, getLineToken() ) );
					expressionConst = SyntacticTreeNode.newExpressionNode( "1", ExpressionKind.CONSTANT, getLineToken() );
					expressionConst.setValue( 1 );
					expressionOperator.add( expressionConst );
					expressionId.add( expressionOperator );
					break;
			}
			match( TokenType.SEMICOLON );
		}
		return expressionId;
	}
	
	//++id --id
	private SyntacticTreeNode incDecStmt() {
		SyntacticTreeNode t = new SyntacticTreeNode(), statementAssing, expressionOperator, expressionId, expressionConst;
		if( token == TokenType.INC || token == TokenType.DEC ) {
			String operator=getComponetToken();
			//operator = getComponetToken().equals( "++" ) ? "+" : "-";
			if("++".equals(operator)){
				operator="+";
			}else{
				operator="-";
			}
			match( token );
			statementAssing = SyntacticTreeNode.newStatementNode( ":=", StatementKind.ASSING, getLineToken() );
			expressionId = SyntacticTreeNode.newExpressionNode( getComponetToken(), ExpressionKind.ID, getLineToken() );
			statementAssing.add( expressionId );
			t = statementAssing;
			expressionOperator = SyntacticTreeNode.newExpressionNode( operator, ExpressionKind.OP, getLineToken() );
			expressionOperator.setExpressionOp( operator.equals( "+" ) ? ExpressionOp.PLUS : ExpressionOp.MINUS );
			expressionOperator.add( SyntacticTreeNode.newExpressionNode(expressionId.getName(), ExpressionKind.ID, getLineToken()) );
			expressionConst = SyntacticTreeNode.newExpressionNode( "1", ExpressionKind.CONSTANT, getLineToken() );
			expressionConst.setValue( 1 );
			expressionOperator.add( expressionConst );
			t.add( expressionOperator );
			match( TokenType.ID );
			match( TokenType.SEMICOLON );
		}
		return t;
	}
	
	// bloque → { lista-sentencia }
	private SyntacticTreeNode block( String name ) {
		SyntacticTreeNode t = SyntacticTreeNode.newStatementNode( name, StatementKind.BLOCK, getLineToken() );
		match( TokenType.LBRACE );
		statementList( t );
		match( TokenType.RBRACE );
		return t;
	}

	// expresión → expresión-simple relación expresión-simple | expresión-simple
	// relacion → <= | < | > | >= | = | !=
	private SyntacticTreeNode exp() {
		SyntacticTreeNode t = simpleExp();
		if ( token == TokenType.EQUAL || token == TokenType.DIFFERENT || 
			token == TokenType.LESS || token == TokenType.LESS_EQUAL ||
			token == TokenType.GREATER || token == TokenType.GREATER_EQUAL ) {
			SyntacticTreeNode p = SyntacticTreeNode.newExpressionNode( getComponetToken(), ExpressionKind.OP, getLineToken() );
			p.setExpressionOp( getExpressionOp() );
			p.add( t );
			t = p;
			match( token );
			t.add( simpleExp() );
		} else if( token != TokenType.SEMICOLON && token != TokenType.RPARENT ){
			error( TokenType.PLUS );
		}
		return t;
	}

	// expresión-simple → expresión-simple suma-op termino | termino
	// suma-op → + | -
	private SyntacticTreeNode simpleExp() {
		SyntacticTreeNode t = term();
		while ( token == TokenType.PLUS || token == TokenType.MINUS ) {
			SyntacticTreeNode p = SyntacticTreeNode.newExpressionNode( getComponetToken(), ExpressionKind.OP, getLineToken() );
			p.setExpressionOp( getExpressionOp() );
			p.add( t );
			t = p;
			match( token );
			t.add( term() );
		}
		return t;
	}

	// termino → termino mult-op factor | factor
	// mult-op → * | /
	private SyntacticTreeNode term() {
		SyntacticTreeNode t = factor();
		while ( ( token == TokenType.MULTI ) || ( token == TokenType.DIV ) ) {
			SyntacticTreeNode p = SyntacticTreeNode.newExpressionNode( getComponetToken(), ExpressionKind.OP, getLineToken() );
			p.setExpressionOp( getExpressionOp() );
			p.add( t );
			t = p;
			match( token );
			t.add( factor() );
		}
		return t;
	}

	// factor → ( expresión ) | numero | identificador
	private SyntacticTreeNode factor() {
		SyntacticTreeNode t = new SyntacticTreeNode();
		switch ( token ) {
			case LPARENT:
				match( TokenType.LPARENT );
				t = exp();
				match( TokenType.RPARENT );
				break;
			case NUM_INT:
				t = SyntacticTreeNode.newExpressionNode( getComponetToken(), ExpressionKind.CONSTANT, getLineToken() );
				t.setExpressionConst( ExpressionConst.CONST_INT );
				t.setValue( Integer.parseInt( getComponetToken() ) );
				match( token );
				break;
			case NUM_FLOAT:
				t = SyntacticTreeNode.newExpressionNode( getComponetToken(), ExpressionKind.CONSTANT, getLineToken() );
				t.setExpressionConst( ExpressionConst.CONST_FLOAT );
				t.setValue( Float.parseFloat( getComponetToken() ) );
				match( token );
				break;
			case ID:
				t = SyntacticTreeNode.newExpressionNode( getComponetToken(), ExpressionKind.ID, getLineToken() );
				match( TokenType.ID );
				break;
			default:
				error( TokenType.NUM_INT );
		}
		return t;
	}

	/*
		Extra functions
	*/
	
	private void match( TokenType expected ) {
		if ( token == expected ) {
			getToken();
		} else {
			error( expected );
		}
	}

	private void error( TokenType expected ) {
		String tokenExpected;
			switch( expected ) {
				case PLUS: case MINUS: case MULTI: case DIV: tokenExpected = "operador"; break;
				case LBRACE: tokenExpected = "{"; break;
				case RBRACE: tokenExpected = "}"; break;
				case LPARENT: tokenExpected = "("; break;
				case RPARENT: tokenExpected = ")"; break;
				case COMMA: tokenExpected = ","; break;
				case SEMICOLON: tokenExpected = ";"; break;
				case ASSIGN: tokenExpected = ":="; break;
				case ID: tokenExpected = "identificador"; break;
				case DO: tokenExpected = "do"; break;
				case FI: tokenExpected = "fi"; break;
				case THEN: tokenExpected = "then"; break;
				case UNTIL: tokenExpected = "until"; break;
				case NUM_INT: case NUM_FLOAT: tokenExpected = "expresión"; break;
				default:
					tokenExpected = expected.name();
					break;
			}
		errors += "Error de sintaxis en la linea: " + getLineToken() + ", columna: " + getColumnToken() + " se esperaba " + tokenExpected + "\n";
		consume();
	}
	
	private void consume() {
		line--;			// token anterior (token que esperaba)
		int lineCurrentToken = getLineToken();
		line++;
		int lineNextToken = getLineToken();
		while( lineCurrentToken == lineNextToken && 
			token != TokenType.EOF && 
			token != TokenType.LBRACE &&
			token != TokenType.SEMICOLON ) {
			getToken();
			lineNextToken = getLineToken();
		}
	}
	
	private void consumeAllLine() {
		int lineA = getLineToken();
		line++;
		int lineB = getLineToken();
		while( lineA == lineB ) {
			getToken();
			lineB = getLineToken();
		}
	}
	
	private void consumeAllLine2() {
		int lineA = getLineToken();
		int lineB = getLineToken();
		while( lineA == lineB ) {
			getToken();
			lineB = getLineToken();
		}
	}
	
	private void getToken() {
		this.token = TokenType.valueOf(tokens[++line][0]);
	}
	
	private String getComponetToken() {
		return tokens[line][1];
	}
	
	private int getLineToken() {
		return Integer.parseInt( tokens[line][2] ) ;
	}
	
	private int getColumnToken() {
		return Integer.parseInt( tokens[line][3] ) ;
	}

	public DefaultTreeModel getDefaultTreeModel() {
		return dtm;
	}
	
	public SyntacticTreeNode getTree() {
		return this.root;
	}
	
	public String getErrors() {
		return errors;
	}
	
	private DeclarationType getDeclarationType() {
		switch( token ) {
			case INT:
				return DeclarationType.INT;
			case FLOAT:
				return DeclarationType.FLOAT;
			case BOOL:
				return DeclarationType.BOOL;
			default:
				return null;
		}
	}
	
	private ExpressionOp getExpressionOp() {
		switch( token ) {
			case LESS:
				return ExpressionOp.LESS; 
			case LESS_EQUAL:
				return ExpressionOp.LESS_EQUAL;
			case GREATER:
				return ExpressionOp.GREATER;
			case GREATER_EQUAL:
				return ExpressionOp.GREATER_EQUAL;
			case EQUAL:
				return ExpressionOp.EQUAL;
			case DIFFERENT:
				return ExpressionOp.DIFFERENT;
			case PLUS:
				return ExpressionOp.PLUS;
			case MINUS:
				return ExpressionOp.MINUS;
			case MULTI:
				return ExpressionOp.MULTI;
			case DIV:
				return ExpressionOp.DIV;
		}
		return null;
	}
	
}
