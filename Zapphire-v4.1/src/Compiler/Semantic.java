package Compiler;

import CompilerResources.DeclarationKind;
import CompilerResources.DeclarationType;
import CompilerResources.ExpressionConst;
import CompilerResources.ExpressionKind;
import CompilerResources.HashTable;
import CompilerResources.NodeKind;
import CompilerResources.StatementKind;
import CompilerResources.SyntacticTreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author Christian Israel López Villalobos
 * @author Héctor Daniel Montañez Briano
 */
public class Semantic {
    public static final int SHIFT = 4;
    public static final int SIZE = 211;
    private int location;
    private String errors = "";
    private ArrayList<HashTable> hashTable;
    private SyntacticTreeNode root;
    private DefaultTreeModel dtm;

	public Semantic( SyntacticTreeNode root ) {
		this.hashTable = new ArrayList<>();
		this.location = 0;
		this.root = ( SyntacticTreeNode ) root.clone();
		evalType( root );
		getHashTable( root );
		buildTree( root );
		setAnnotationsTree( root );
		this.dtm = new DefaultTreeModel( root );
	}

	private void evalType( SyntacticTreeNode t ) {
		if ( t == null ) {
			return;
		}
		switch ( t.getNodeKind() ) {
			case DECLARATION:
				switch ( t.getDeclarationKind() ) {
					case TYPE:
						// De izq a der
						( ( SyntacticTreeNode ) t.getNextNode() ).setDeclarationType( t.getDeclarationType() );
						evalType( ( SyntacticTreeNode ) t.getNextNode() );
						break;
					case ID:
						SyntacticTreeNode x = ( SyntacticTreeNode ) t.getNextNode();
						if ( x != null ) {
							// De hermano a hermano
							( ( SyntacticTreeNode ) t.getNextNode() ).setDeclarationType( ( ( SyntacticTreeNode ) t ).getDeclarationType() );
							evalType( ( SyntacticTreeNode ) t.getNextNode() );
						}
						break;
					case NULL:
						t.setDeclarationType( ( ( SyntacticTreeNode ) t.getChildAt( 0 ) ).getDeclarationType() );
						break;
				}
				break;
		}
		evalType( ( SyntacticTreeNode ) t.getNextNode() );
	}

	private void getHashTable( SyntacticTreeNode t ) {
		if ( t == null ) {
			return;
		}
		switch ( t.getNodeKind() ) {
			case DECLARATION:
				if ( t.getDeclarationKind() == DeclarationKind.ID ) {
					insertNewIdHashTable( t.getName(), t.getLine(), 0, getType( t.getDeclarationType() ) );
				}
				break;
		}
		getHashTable( ( SyntacticTreeNode ) t.getNextNode() );
	}

	private void buildTree( SyntacticTreeNode t ) {
		if ( t == null ) {
			return;
		}
		if ( t.getNodeKind() == NodeKind.STATEMENT ) {
			SyntacticTreeNode nodeExpression = null, nodeId = null;
			switch ( t.getStatementKind() ) {
				case IF:
					nodeExpression = ( SyntacticTreeNode ) t.getChildAt( 0 );
					break;
				case WHILE:
					nodeExpression = ( SyntacticTreeNode ) t.getChildAt( 0 );
					break;
				case DO:
					nodeExpression = ( SyntacticTreeNode ) t.getChildAt( 1 );
					break;
				case WRITE:
					nodeExpression = ( SyntacticTreeNode ) t.getChildAt( 0 );
					break;
				case ASSING:
					nodeId = ( ( SyntacticTreeNode ) t.getChildAt( 0 ) );
					if ( insertNewLineHashTable( nodeId.getName(), nodeId.getLine() ) ) {
						nodeExpression = ( SyntacticTreeNode ) t.getChildAt( 1 );
					} else {
						nodeExpression = null;
						nodeId = null;
					}
					break;
			}
			posEval( nodeExpression );
			// Verificación del tipo del id y del tipo del valor obtenido de la expresión
			if ( t.getStatementKind() == StatementKind.ASSING ) {
				if ( nodeId != null && nodeId.getExpressionType() == nodeExpression.getExpressionType() ) {
					setValueInHashTable( nodeId.getName(), nodeExpression.getValue() );
					t.setValue( nodeExpression.getValue() );
					t.setExpressionConst( nodeExpression.getExpressionConst() );
				}

			}
		}
		buildTree( ( SyntacticTreeNode ) t.getNextNode() );
	}

	private void posEval( SyntacticTreeNode t ) {
		if ( t == null || t.getExpressionKind() != ExpressionKind.OP ) {
			return;
		}
		posEval( ( SyntacticTreeNode ) t.getChildAt( 0 ) );			// Izq
		posEval( ( SyntacticTreeNode ) t.getChildAt( 1 ) );			// Der

		/*
		 Verificamos los hijos del operador
		 si hay una variable entonces
		 verificamos que exista en la tabla hash y si existe sacamos el valor y el tipo
		 si no existe
		 marcar error de variable no declarada
		
		 si es una constante 
		 verficamos que tipo es y sacamos el valor
		 */
		SyntacticTreeNode leftChild = ( ( SyntacticTreeNode ) t.getChildAt( 0 ) );
		SyntacticTreeNode rightChild = ( ( SyntacticTreeNode ) t.getChildAt( 1 ) );
		float leftChildValue = 0;	// Valor del hijo izquierdo
		float rightChildValue = 0;	// Valor del hijo derecho
		ExpressionConst leftChildConst = ExpressionConst.NULL;
		ExpressionConst rightChildConst = ExpressionConst.NULL;

		// Verificamos el hijo izquierdo
		switch ( leftChild.getExpressionKind() ) {
			case ID:
				// Insertamos el valor en la tabla hash
				if ( insertNewLineHashTable( leftChild.getName(), leftChild.getLine() ) ) {
					leftChildValue = getValueInHashTable( leftChild.getName() );
					leftChild.setValue( leftChildValue );
					leftChildConst = getTypeInHashTable( leftChild.getName() );
					leftChild.setExpressionConst( leftChildConst );
				}
				break;
			case CONSTANT: case OP:
				leftChildValue = leftChild.getValue();
				leftChildConst = leftChild.getExpressionConst();
				break;
		}

		// Verificamos el hijo derecho
		switch ( rightChild.getExpressionKind() ) {
			case ID:
				// Insertamos el valor en la tabla hash
				if ( insertNewLineHashTable( rightChild.getName(), rightChild.getLine() ) ) {
					rightChildValue = getValueInHashTable( rightChild.getName() );
					rightChild.setValue( rightChildValue );
					rightChildConst = getTypeInHashTable( rightChild.getName() );
					rightChild.setExpressionConst( rightChildConst );
				}
				break;
			case CONSTANT: case OP:
				rightChildValue = rightChild.getValue();
				rightChildConst = rightChild.getExpressionConst();
				break;
		}
		
		// Si es un variable y no se encuentra en la tabla hash, el tipo de constante esta nulo
		if ( leftChildConst == ExpressionConst.NULL || rightChildConst == ExpressionConst.NULL ) {
			return;
		}

		// Se le asigna el tipo al nodo de operacion
		if ( leftChildConst == ExpressionConst.CONST_FLOAT || rightChildConst == ExpressionConst.CONST_FLOAT ) {
			t.setExpressionConst( ExpressionConst.CONST_FLOAT );
		} else {
			t.setExpressionConst( ExpressionConst.CONST_INT );
		}

		// Asignación de valor
		switch ( t.getExpressionOp() ) {
			case PLUS:
				t.setValue( leftChildValue + rightChildValue );
				break;
			case MINUS:
				t.setValue( leftChildValue - rightChildValue );
				break;
			case MULTI:
				t.setValue( leftChildValue * rightChildValue );
				break;
			case DIV:
				t.setValue( leftChildValue / rightChildValue );
				break;
			case LESS:
				if ( leftChildValue < rightChildValue ) {
					t.setValue( 1 );
				} else {
					t.setValue( 0 );
				}
				t.setExpressionConst( ExpressionConst.CONST_BOOL );
				break;
			case LESS_EQUAL:
				if ( leftChildValue <= rightChildValue ) {
					t.setValue( 1 );
				} else {
					t.setValue( 0 );
				}
				t.setExpressionConst( ExpressionConst.CONST_BOOL );
				break;
			case GREATER:
				if ( leftChildValue > rightChildValue ) {
					t.setValue( 1 );
				} else {
					t.setValue( 0 );
				}
				t.setExpressionConst( ExpressionConst.CONST_BOOL );
				break;
			case GREATER_EQUAL:
				if ( leftChildValue >= rightChildValue ) {
					t.setValue( 1 );
				} else {
					t.setValue( 0 );
				}
				t.setExpressionConst( ExpressionConst.CONST_BOOL );
				break;
			case EQUAL:
				if ( leftChildValue == rightChildValue ) {
					t.setValue( 1 );
				} else {
					t.setValue( 0 );
				}
				t.setExpressionConst( ExpressionConst.CONST_BOOL );
				break;
			case DIFFERENT:
				if ( leftChildValue != rightChildValue ) {
					t.setValue( 1 );
				} else {
					t.setValue( 0 );
				}
				t.setExpressionConst( ExpressionConst.CONST_BOOL );
				break;
		}
	}

	private void setAnnotationsTree( SyntacticTreeNode t ) {
		Enumeration en = t.breadthFirstEnumeration();
		SyntacticTreeNode node = null;
		while ( en.hasMoreElements() ) {
			node = ( SyntacticTreeNode ) en.nextElement();
			switch ( node.getNodeKind() ) {
				case EXPRESSION:
					if ( node.getExpressionKind() == ExpressionKind.OP || node.getExpressionKind() == ExpressionKind.ID ) {
						if ( node.getExpressionConst() != ExpressionConst.CONST_FLOAT ) {
							node.setName( node.getName() + "    [ " + ( int ) node.getValue() + " ]" );
						} else {
							node.setName( node.getName() + "    [ " + node.getValue() + " ]" );
						}
					}
					break;
				case DECLARATION:
					if ( node.getDeclarationKind() == DeclarationKind.NULL ) {
						node.setName( node.getName() + "    [ " + node.getDeclarationType().name().toLowerCase() + " ]" );
					}
				case STATEMENT:
					if( node.getStatementKind() == StatementKind.ASSING ) {
						if ( node.getExpressionConst() != ExpressionConst.CONST_FLOAT ) {
							node.setName( node.getName() + "    [ " + ( int ) node.getValue() + " ]" );
						} else {
							node.setName( node.getName() + "    [ " + node.getValue() + " ]" );
						}
					}
			}
		}
	}

	// Métodos de la tabla hash
	
	private void insertNewIdHashTable( String name, int line, int value, String type ) {
		int hash = getHash( name ), index = 0;
		boolean exist = false;
		HashTable temp = serchHash( hash );
		if ( temp == null ) {				// no existe
			for ( HashTable i : hashTable ) {
				if ( hash > i.getHash() ) {
					index++;
				} else {
					break;
				}
			}
			hashTable.add( index, new HashTable( hash, name, this.location++, line, value, type ) );
		} else {						// existe
			for ( HashTable i : hashTable ) {
				if ( i.getHash() == hash && i.getName().equals( name ) ) {
					exist = true;
					errors += "Error de semántica en la linea: " + line + " variable " + name + " ya declarada\n";
					break;
				}
				if ( i.getHash() <= hash ) {
					index++;
				}
			}
			if ( !exist ) {
				hashTable.add( index, new HashTable( hash, name, this.location++, line, value, type ) );
			}
		}
	}

	private boolean insertNewLineHashTable( String name, int line ) {
		int hash = getHash( name );
		boolean exist = false;
		HashTable temp = serchHash( hash );
		if ( temp == null ) {				// no existe
			errors += "Error de semántica en la linea: " + line + " variable '" + name + "' no declarada\n";
		} else {						// existe
			for ( HashTable i : hashTable ) {
				if ( i.getHash() == hash && i.getName().equals( name ) ) {
					exist = true;
					i.addLine( line );
					break;
				}
			}
			if ( !exist ) {
				errors += "Error de semántica en la linea: " + line + " variable '" + name + "' no declarada\n";
			}
		}
		return exist;
	}

	private void setValueInHashTable( String name, float value ) {
		int hash = getHash( name );
		for ( HashTable i : hashTable ) {
			if ( i.getHash() == hash && i.getName().equals( name ) ) {
				i.setValue( value );
				break;
			}
		}
	}
	
	private float getValueInHashTable( String name ) {
		int hash = getHash( name );
		for ( HashTable i : hashTable ) {
			if ( i.getHash() == hash && i.getName().equals( name ) ) {
				return i.getValue();
			}
		}
		return 0;
	}
	
	private ExpressionConst getTypeInHashTable( String name ) {
		int hash = getHash( name );
		for ( HashTable i : hashTable ) {
			if ( i.getHash() == hash && i.getName().equals( name ) ) {
				switch( i.getType() ) {
					case "int":
						return ExpressionConst.CONST_INT;
					case "float":
						return ExpressionConst.CONST_FLOAT;
					case "bool":
						return ExpressionConst.CONST_BOOL;
				}
			}
		}
		return ExpressionConst.NULL;
	}

	private HashTable serchHash( int hash ) {
		for ( HashTable i : this.hashTable ) {
			if ( i.getHash() == hash ) {
				return i;
			}
		}
		return null;
	}
	
	

	private String getType( DeclarationType type ) {
		switch ( type ) {
			case INT:
				return "int";
			case FLOAT:
				return "float";
			case BOOL:
				return "bool";
		}
		return null;
	}

	public String getErrors() {
		return errors;
	}

	public DefaultTableModel getDefaultTableModel() {
		Object data[][] = new Object[hashTable.size()][6];
		int index = 0;
		for ( HashTable row : hashTable ) {
			data[index][0] = row.getHash();
			data[index][1] = row.getName();
			data[index][2] = row.getLocation();
			data[index][3] = row.getLineList();
			data[index][4] = row.getValue();
			data[index++][5] = row.getType();
		}
		DefaultTableModel dtm = new DefaultTableModel( data, new Object[]{ "Hash", "Nombre", "Localidad", "Lineas", "Valor", "Tipo" } ) {

			@Override
			public boolean isCellEditable( int row, int column ) {
				return false;
			}

		};
		return dtm;
	}

	public DefaultTreeModel getDefaultTreeModel() {
		return dtm;
	}

	public SyntacticTreeNode getSemanticTree() {
		return root;
	}

	public static int getHash( String id ) {
		int temp = 0;
		int i = 0;
		char[] key = id.toCharArray();
		while ( i != id.length() ) {
			temp = ( ( temp << SHIFT ) + key[i] ) % SIZE;
			++i;
		}
		return temp;
	}

}
