package Compiler;

import Class.FileManager;
import CompilerResources.SyntacticTreeNode;
import java.util.Enumeration;

/**
 * @author Christian Israel López Villalobos
 * @author Héctor Daniel Montañez Briano
 */
public class Compiler {
	
	public Lexical lexical;
	public Syntactic syntactic;
	public Semantic semantic;
	public String errors;
	public boolean lexicalReady, syntacticReady, semanticReady;
	
	public Compiler( String path, String name ) {
		lexical = new Lexical( path, name );
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
		FileManager.save( path + "/" + name + ".ezph", errors );
	}

}
