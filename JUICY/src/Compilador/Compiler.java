package Compilador;

import Interfaz.FileManager;
import RecursosCompi.HashTable;
import RecursosCompi.SyntacticTreeNode;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * @author Brandon Rodriguez
 * @author José Antonio
 */
public class Compiler {
	public static String midCod;
        private static ArrayList<HashTable> hashTable;
        public static StringBuffer code;
	public Lexical lexical;
	public Syntactic syntactic;
        public SyntacticTreeNode sAux;
	public Semantic semantic;
	public String errors;
        public GenCod11 generador2;
	public boolean lexicalReady, syntacticReady, semanticReady;
	
	public  Compiler( String path, String name ) {
            midCod="";
		lexical = new Lexical( path, name );
                System.out.println("path "+path+" name "+name);
		errors = lexical.getErrors();
		if( errors.isEmpty() ) {						// Sin errores en el léxico
			lexicalReady = true;
			syntactic = new Syntactic( path, name );
                        //sAuzx=(SyntacticTreeNode)syntactic.getDefaultTreeModel();
			errors = syntactic.getErrors();
			if( errors.isEmpty() ) {					// Sin errores en el sintáctico
				syntacticReady = true;
				semantic = new Semantic( syntactic.getTree() );
				errors = semantic.getErrors();
				if( errors.isEmpty() ) {				// Sin errores en el semántico
					semanticReady = true;
                                        hashTable=semantic.getHashTable();
                                        generador2=new GenCod11(semantic);
                                        code=generador2.codeGen((SyntacticTreeNode) syntactic.getTree().getChildAt(1), "CodigoIntermedio");
                                        midCod=code.toString();
				} else {								// Con errores en el semantico
					semanticReady = false;
				}
			} else {									// Con errores en el sintáctico
				syntacticReady = false;
			}
		} else {										// Con errores en el léxico
			lexicalReady = false;
		}
		// Anexamos al archivo de errores, los errores del análisis léxico y sintáctico
		FileManager.save( path + "/" + name + ".ejava", errors );
	}
        
        public static String getMidCode(){
            return Compiler.midCod;
        }

    public static ArrayList<HashTable> getHashTable() {
        return hashTable;
    }

    public static StringBuffer getCode() {
        return code;
    }
        
        

}
