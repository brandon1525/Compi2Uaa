package Compilador;

import RecursosCompi.DeclarationKind;
import RecursosCompi.DeclarationType;
import RecursosCompi.ExpressionConst;
import RecursosCompi.ExpressionKind;
import RecursosCompi.HashTable;
import RecursosCompi.NodeKind;
import RecursosCompi.StatementKind;
import RecursosCompi.SyntacticTreeNode;
import java.beans.Expression;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import java.math.*;

public class Semantic {
    public static final int SHIFT = 4;
    public static final int SIZE = 211;
    private int location;
    private String errors = "";
    private ArrayList<HashTable> hashTable;
    private SyntacticTreeNode root;
    private DefaultTreeModel dtm;

    public Semantic( SyntacticTreeNode root ) {// Recibe el arbol sintáctico
	this.hashTable = new ArrayList<>();
	this.location = 0;
	this.root = ( SyntacticTreeNode ) root.clone();//clona la raíz del árbol
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
                    //caso de int float bool 
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
	if ( t == null || t.getName()==null) {
            return;
	}
	if ( t.getNodeKind() == NodeKind.STATEMENT ) {
            SyntacticTreeNode nodeExpression = null, nodeId = null;
            switch ( t.getStatementKind() ) {
		        case IF:
                    nodeExpression = ( SyntacticTreeNode ) t.getChildAt( 0 );
                    posEval(nodeExpression);
                    break;
                case WHILE:
                    nodeExpression = ( SyntacticTreeNode ) t.getChildAt( 0 );
                    posEval(nodeExpression);
                    break;
		case DO:
                    nodeExpression = ( SyntacticTreeNode ) t.getChildAt( 0 );
                    SyntacticTreeNode casi = (SyntacticTreeNode) t.getChildAt(1);
                    posEval(casi);
                    posEval(nodeExpression);
                    
                    break;
		case WRITE: case READ:
                    nodeExpression = ( SyntacticTreeNode ) t.getChildAt( 0 );
                    if(nodeExpression.getExpressionKind()==ExpressionKind.ID){
                        insertNewLineHashTable(nodeExpression.getName(),nodeExpression.getLine());
                        posEval(nodeExpression);
                        nodeExpression.setValue(getValueInHashTable(nodeExpression.getName()));
                        nodeExpression.setExpressionConst(getTypeInHashTable(nodeExpression.getName()));
                        
                    }else{
                        posEval(nodeExpression);
                    }
                    //posEval(nodeExpression);
                    break;
                
		        case ASSING:
                    nodeId = ( ( SyntacticTreeNode ) t.getChildAt( 0 ) );
                    nodeExpression = ( (SyntacticTreeNode) t.getChildAt( 1 ) );
                    if(insertNewLineHashTable(nodeId.getName(), nodeId.getLine())){
                        switch(nodeExpression.getExpressionKind()){
                            case OP:
                                posEval(nodeExpression);
                                break;
                            case ID:
                                nodeExpression.setValue(getValueInHashTable(nodeExpression.getName()));
                                nodeExpression.setExpressionConst(getTypeInHashTable(nodeExpression.getName()));
                                break;
                        }
                        
                       if(getTypeInHashTable(nodeId.getName())==nodeExpression.getExpressionConst() ||
                               getTypeInHashTable(nodeId.getName())==ExpressionConst.CONST_FLOAT ||
                               (getTypeInHashTable(nodeId.getName())==ExpressionConst.CONST_BOOL &&
                               nodeExpression.getExpressionConst()==ExpressionConst.CONST_INT && 
                               (nodeExpression.getValue()==0 || nodeExpression.getValue()==1)
                               )
                        ){
    
                           if(getTypeInHashTable(nodeId.getName())== ExpressionConst.CONST_INT && nodeExpression.getExpressionConst()== ExpressionConst.CONST_INT){
                               int temp = (int)nodeExpression.getValue();
                               nodeExpression.setValue(temp);
                          }
                           nodeId.setValue(nodeExpression.getValue());
                           nodeId.setExpressionConst(nodeExpression.getExpressionConst());
                           setValueInHashTable(nodeId.getName(), nodeExpression.getValue());
                           t.setValue(nodeExpression.getValue());
                           t.setExpressionConst(nodeExpression.getExpressionConst());
                           
                       }else{
                           errors=errors+"Error de semántica en la línea: "+nodeId.getLine()+" no se puede realizar"
                                   + "la asignación tipo "+getTypeInHashTable(nodeId.getName())+" diferente a "
                                   + nodeExpression.getExpressionConst()+" \n";
                       } 
                    }
                    break;
                default:
                    System.out.println("algo extraño "+t.getName());
                    break;
            }
            
	}
	buildTree( ( SyntacticTreeNode ) t.getNextNode() );
    }

    private void posEval( SyntacticTreeNode t ) {
	if ( t == null || t.getExpressionKind() != ExpressionKind.OP ) {
            return;
	}
	posEval( ( SyntacticTreeNode ) t.getChildAt( 0 ) );	// Izq
	posEval( ( SyntacticTreeNode ) t.getChildAt( 1 ) );	// Der

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
                    leftChild.setExpressionConst( leftChildConst );		}
		break;
            case CONSTANT: case OP:
                
                leftChildValue = leftChild.getValue();
		leftChildConst = leftChild.getExpressionConst();
                if(leftChildConst==ExpressionConst.CONST_INT){
                    leftChildValue=(int)leftChildValue;
                }
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
                if(rightChildConst==ExpressionConst.CONST_INT){
                    rightChildValue=(int)rightChildValue;
                }
		break;
	}
		
	// Si es un variable y no se encuentra en la tabla hash, el tipo de constante esta nulo
	if ( leftChildConst == ExpressionConst.NULL || rightChildConst == ExpressionConst.NULL ) {
            return;
	}

	// Se le asigna el tipo al nodo de operacion
	if ( leftChildConst == ExpressionConst.CONST_FLOAT || rightChildConst == ExpressionConst.CONST_FLOAT) {
            t.setExpressionConst( ExpressionConst.CONST_FLOAT );
	} else {
            t.setExpressionConst( ExpressionConst.CONST_INT );
            
	}
        
	// Asignación de valor
	switch ( t.getExpressionOp() ) {
            case PLUS:
		//
                if(t.getExpressionConst()==ExpressionConst.CONST_INT){
                    t.setValue((int)(leftChildValue+rightChildValue));
                }else{
                    t.setValue( leftChildValue + rightChildValue );
                }
		break;
            case MINUS:
		
                if(t.getExpressionConst()==ExpressionConst.CONST_INT){
                    t.setValue((int)(leftChildValue-rightChildValue));
                }else{
                    t.setValue( leftChildValue - rightChildValue );
                }
		break;
            case MULTI:
		
                if(t.getExpressionConst()==ExpressionConst.CONST_INT){
                    t.setValue((int)(leftChildValue*rightChildValue));
                }else{
                    t.setValue( leftChildValue * rightChildValue );
                }
		break;
            case DIV:
		
                if(t.getExpressionConst()==ExpressionConst.CONST_INT){
                    t.setValue((int)(leftChildValue/rightChildValue));
                }else{
                    t.setValue( leftChildValue / rightChildValue );
                }
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
                            node.setName( node.getName() + "    [ " + (int)node.getValue() + " ] [ "+node.getExpressionConst()+" ]" );
			} else {
                            node.setName( node.getName() + "    [ " + node.getValue() + " ] [ "+node.getExpressionConst()+" ]" );
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
                            node.setName( node.getName() + "    [ " + (int)node.getValue() + " ] [ "+node.getExpressionConst()+" ]" );
			} else {
                            node.setName( node.getName() + "    [ " + node.getValue() + " ] [ "+node.getExpressionConst()+" ]" );
                            
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
