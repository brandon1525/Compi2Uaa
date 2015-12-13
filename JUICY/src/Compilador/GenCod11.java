/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;


import Interfaz.Juicy;
import RecursosCompi.ExpressionConst;
import RecursosCompi.ExpressionKind;
import RecursosCompi.ExpressionType;
import RecursosCompi.NodeKind;
import RecursosCompi.StatementKind;
import RecursosCompi.SyntacticTreeNode;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author Manu
 */
public class GenCod11 {
    //String codigo;
    public ArrayList<String> codeLines;
    StringBuffer code;
    //Semantic semantic;
    
    int cLabel=1;
    boolean TraceCode=true;
    File f;
    FileWriter w;
    BufferedWriter bw;
    PrintWriter wr;
   int indu=0;
    
    public GenCod11(Semantic semantic) {
        codeLines=new ArrayList<String>();
        code = new StringBuffer();
    }
    
    public StringBuffer codeGen(SyntacticTreeNode arbolSemantico, String archivo){
        if(arbolSemantico!=null){
            cGen(arbolSemantico);
            //code.append("STP").append("\n");
            emitCode("STP");
        }
        return code;
    }
    
   public void cGen(SyntacticTreeNode arbol){  
        String code="",label1="",label2="",aux="";
        String[] aux2;
        SyntacticTreeNode nodeExpression,nodeId,node3;
        if(arbol==null||arbol.getName()==null){
            return;
        }
        if(arbol.getNodeKind()==NodeKind.STATEMENT){
            SyntacticTreeNode nodoExpresion=null,nodoId=null, nodo3=null,nodoTrue=null,nodoFalse=null;
            switch(arbol.getStatementKind()){
                case IF:
                    posEval((SyntacticTreeNode) arbol.getChildAt(0));
                    label1=genLabel();
                    emitCode("FJP "+label1);
                    nodoTrue=(SyntacticTreeNode) arbol.getChildAt(1);
                    nodoTrue=(SyntacticTreeNode) nodoTrue.getChildAt(0);
                    cGen(nodoTrue);
                    if(arbol.getChildCount()==3){
                        label2=genLabel();
                        emitCode("JMP "+label2);
                    }
                    emitCode("LAB "+label1);
                    if(arbol.getChildCount()==3){
                        nodoFalse=(SyntacticTreeNode) arbol.getChildAt(2);
                        nodoFalse=(SyntacticTreeNode)nodoFalse.getChildAt(0);
                        cGen(nodoFalse);
                        emitCode("LAB "+label2);
                    }
                    label1="";
                    label2="";
                    break;
                case WHILE:
                    label1=genLabel();
                    emitCode("LAB "+label1);
                    posEval((SyntacticTreeNode)arbol.getChildAt(0));
                    label2=genLabel();
                    emitCode("FJP "+label2);
                    nodoTrue=(SyntacticTreeNode) arbol.getChildAt(1);
                    nodoTrue=(SyntacticTreeNode) nodoTrue.getChildAt(0);
                    cGen(nodoTrue);
                    emitCode("JMP "+label1);
                    emitCode("LAB "+label2);
                    label1="";
                    label2="";
                    break;
                case DO:
                    label1=genLabel();
                    emitCode("LAB "+label1);
                    nodoTrue=(SyntacticTreeNode) arbol.getChildAt(0);
                    nodoTrue=(SyntacticTreeNode) nodoTrue.getChildAt(0);
                    cGen(nodoTrue);
                    posEval((SyntacticTreeNode) arbol.getChildAt(1));
                    emitCode("FJP "+label1);
                    label1="";
                    label2="";
                    break;
                case READ:
                    nodoExpresion=(SyntacticTreeNode)arbol.getChildAt(0);
                    aux=nodoExpresion.getName();
                    aux2=aux.split(" ");
                    code=code.concat("LDA "+aux2[0]);
                    emitCode(code);
                    code="";
                    emitCode("RD ");
                    break;
                case WRITE:
                    posEval((SyntacticTreeNode)arbol.getChildAt(0));
                    emitCode("WRT");
                    code="";
                    break;
                case ASSING:
                    SyntacticTreeNode nodeid = (SyntacticTreeNode)arbol.getChildAt(0);
                    SyntacticTreeNode nodeexp = (SyntacticTreeNode)arbol.getChildAt(1);
                    aux2=(nodeid.getName().split("    "));
                    aux=aux2[0];
                    code=code.concat("LDA "+aux);
                    emitCode(code);
                    posEval(nodeexp);
                    emitCode("STO");
                    code="";
                    break;   
            }
        }
        cGen((SyntacticTreeNode)arbol.getNextSibling());
    }
    
    private void posEval( SyntacticTreeNode arbol ) {
        String aux="";
        String code="";
        String[] aux2;
	if ( arbol == null) {
            return;
	}
        try{
            posEval((SyntacticTreeNode) arbol.getChildAt(0));
            posEval((SyntacticTreeNode) arbol.getChildAt(1));
        }catch(Exception e){
            System.err.println("Cagando el palo"+e+this);
        }
        if(arbol.getExpressionKind()==ExpressionKind.ID){
            aux2=(arbol.getName().split("    "));
            aux=aux2[0];
            code="LOD " + aux;
            emitCode(code);
        }else if(arbol.getExpressionKind()==ExpressionKind.CONSTANT){
            if(arbol.getExpressionConst()==ExpressionConst.CONST_INT){
                code="LDI "+(int)arbol.getValue();
                emitCode(code);
            }else if(arbol.getExpressionConst()==ExpressionConst.CONST_FLOAT){
                code="LDF "+arbol.getValue();
                emitCode(code);
            }else if(arbol.getExpressionConst()==ExpressionConst.CONST_BOOL){
                code="LDB "+(int)arbol.getValue();
                emitCode(code);
            }
        }
        if(arbol.getExpressionOp()==null)
            return;
        switch(arbol.getExpressionOp()){
            case PLUS:
                emitCode("ADD");
                break;
            case MINUS:
                emitCode("SUB");
                break;
            case MULTI:
                emitCode("MUL");
                break;
            case DIV:
                emitCode("DIV");
                break;
            case GREATER:
                emitCode("GTR");
                break;
            case GREATER_EQUAL:
                emitCode("GEQ");
                break;
            case LESS:
                emitCode("LET");
                break;
            case LESS_EQUAL:
                emitCode("LEQ");
                break;
            case EQUAL:
                emitCode("EQU");
                break;
            case DIFFERENT:
                emitCode("NEQ");
                break;
        }
    }
    
    public void emitCode(String codigo){
        //codeLines.add(codigo+"\n");
        code.append(codigo).append("\n");
    }
    
    public ArrayList<String> getCodeLines(){
        return this.codeLines;
    }
    
    public String genLabel(){
        String aux="L"+cLabel;
        cLabel++;
        return aux;
        
    }
}
