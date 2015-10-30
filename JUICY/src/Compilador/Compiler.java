package Compilador;

import Interfaz.FileManager;
import RecursosCompi.SyntacticTreeNode;
import java.util.Enumeration;

/**
 * @author Brandon Rodriguez
 * @author José Antonio
 */
public class Compiler {
	
	public Lexical lexical;
	public Syntactic syntactic;
	public Semantic semantic;
	public String errors;
	public boolean lexicalReady, syntacticReady, semanticReady;
	
	public Compiler( String path, String name ) {
		lexical = new Lexical( path, name );
                System.out.println("path "+path+" name "+name);
		errors = lexical.getErrors();
		if( errors.isEmpty() ) {						// Sin errores en el léxico
			lexicalReady = true;
			syntactic = new Syntactic( path, name );
			errors = syntactic.getErrors();
			if( errors.isEmpty() ) {					// Sin errores en el sintáctico
				syntacticReady = true;
				semantic = new Semantic( syntactic.getTree() );
				errors = semantic.getErrors();
				if( errors.isEmpty() ) {				// Sin errores en el semántico
					semanticReady = true;
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

}
