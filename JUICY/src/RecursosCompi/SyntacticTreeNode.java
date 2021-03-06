package RecursosCompi;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;


public class SyntacticTreeNode extends DefaultMutableTreeNode {
	
	private NodeKind nodeKind;
	
	private StatementKind statementKind;
	
	private DeclarationKind declarationKind;
	private DeclarationType declarationType;
	
	private ExpressionKind expressionKind;
	private ExpressionOp expressionOp;
	private ExpressionType expressionType;
	private ExpressionConst expressionConst;
	
	// Atributos
	private String name;
	private float value;
        private String type;
	private int line;

	public SyntacticTreeNode() {
		this.nodeKind = null;
		// Sentencia
		this.statementKind = StatementKind.NULL;
		//Declaración
		this.declarationKind = DeclarationKind.NULL;
		this.declarationType = DeclarationType.NULL;
		//Expresión
		this.expressionKind = null;
		this.expressionOp = null;
		this.expressionType = null;
		this.expressionConst = ExpressionConst.NULL;
		//Atributos
		this.name = null;
		this.value = 0;
                this.type = null;
		this.line = -1;
	}
	
	
	public static SyntacticTreeNode newStatementNode( String name, StatementKind statementKind, int line ) {
		SyntacticTreeNode temp = new SyntacticTreeNode();
		temp.setName( name );
		temp.setNodeKind( NodeKind.STATEMENT );
		temp.setStatementKind( statementKind );
		temp.setLine( line );
		return temp;
	}
	
	public static SyntacticTreeNode newDeclarationNode( String name, DeclarationKind declarationKind, int line ) {
		SyntacticTreeNode temp = new SyntacticTreeNode();
		temp.setName( name );
		temp.setNodeKind( NodeKind.DECLARATION );
		temp.setDeclarationKind( declarationKind );
		temp.setLine( line );
		return temp;
	}
	
	public static SyntacticTreeNode newDeclarationNode( String name ) {
		SyntacticTreeNode temp = new SyntacticTreeNode();
		temp.setName( name );
		temp.setNodeKind( NodeKind.DECLARATION );
		return temp;
	}
	
	public static SyntacticTreeNode newExpressionNode( String name, ExpressionKind expressionKind, int line ) {
		SyntacticTreeNode temp = new SyntacticTreeNode();
		temp.setName( name );
		temp.setNodeKind( NodeKind.EXPRESSION );
		temp.setExpressionKind( expressionKind );
		temp.setLine( line );
		return temp;
	}

	public NodeKind getNodeKind() {
		return nodeKind;
	}
        
        public ExpressionOp getExpressionOp() {
		return expressionOp;
	}

	public void setNodeKind( NodeKind nodeKind ) {
		this.nodeKind = nodeKind;
	}

	public StatementKind getStatementKind() {
		return statementKind;
	}

	public void setStatementKind( StatementKind statementKind ) {
		this.statementKind = statementKind;
	}

	public DeclarationKind getDeclarationKind() {
		return declarationKind;
	}

	public void setDeclarationKind( DeclarationKind declarationKind ) {
		this.declarationKind = declarationKind;
	}

	public DeclarationType getDeclarationType() {
		return declarationType;
	}

	public void setDeclarationType( DeclarationType declarationType ) {
		this.declarationType = declarationType;
	}

	public ExpressionType getExpressionType() {
		return expressionType;
	}

	public void setExpressionType( ExpressionType expressionType ) {
		this.expressionType = expressionType;
	}
        
        

	public ExpressionKind getExpressionKind() {
		return expressionKind;
	}

	public void setExpressionKind( ExpressionKind expressionKind ) {
		this.expressionKind = expressionKind;
	}

	

	public void setExpressionOp( ExpressionOp opkind ) {
		this.expressionOp = opkind;
	}

	public ExpressionConst getExpressionConst() {
		return expressionConst;
	}

	public void setExpressionConst( ExpressionConst expressionConst ) {
		this.expressionConst = expressionConst;
	}

	public float getValue() {
		return value;
	}

	public void setValue( float value ) {
		this.value = value;
	}
	
	public int getLine() {
		return line;
	}

	public void setLine( int line ) {
		this.line = line;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName( String name ) {
		this.name = name;
		this.userObject = name;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    public DefaultMutableTreeNode getHermano() {
            DefaultMutableTreeNode nextSibling = getNextSibling();
            if (nextSibling == null) {
                DefaultMutableTreeNode aNode = (DefaultMutableTreeNode)getParent();

                do {
                    if (aNode == null) {
                        return null;
                    }

                    nextSibling = aNode.getNextSibling();
                    if (nextSibling != null) {
                        return nextSibling;
                    }

                    aNode = (DefaultMutableTreeNode)aNode.getParent();
                } while(true);
            } else {
                return nextSibling;
            }
    }    
}
