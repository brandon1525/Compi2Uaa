package Class;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Christian Israel Lopez Villalobos
 */
public class FileManager {
	
	private String path, name;
	private boolean alter;
	
	public FileManager() {
		this.path = null;
		this.name = null;
		this.alter = false;
	}
	
	public FileManager( String path ) {
		this.path = path;
		this.name = getName( path );
		this.alter = false;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath( String path ) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public boolean isAlter() {
		return alter;
	}

	public void setAlter( boolean alter ) {
		this.alter = alter;
	}
	
	public boolean isPathNull() {
		return path == null;
	}

	/**
	 * Obtiene el contenido del archivo especificado en la ruta
	 *
	 * @param path ruta del archivo
	 * @param openCode bandera que indica si es necesario agregar un caracter extra a la cadena, esto para el lexico
	 */
	
	public static String open( String path, boolean openCode ) {
		try {
			FileReader fr = new FileReader( path );
			BufferedReader archivo = new BufferedReader( fr );
			String texto = archivo.readLine();
			while ( archivo.ready() ) {
				texto += "\n" + archivo.readLine();
			}
			archivo.close();
				if( openCode )
					return texto + ( char ) 3;
				else
					return texto;
		} catch ( Exception e ) {
		}
		return null;
	}
	
	public static DefaultTableModel getDefaultTableModel( String path ) {
		String code = open( path, false );
		String rows[] = code.split( "\n" );
		Object data[][] = new Object[rows.length][4];
		String colums[];
		for ( int i = 0; i < rows.length; i++ ) {
			colums = rows[i].split( " " );
			for ( int j = 0; j < 4; j++ ) {
				data[i][j]	= colums[j];		
			}
		}
		DefaultTableModel dtm = new DefaultTableModel( data, new Object[]{ "Token", "Componente", "Linea", "Columna" } ) {

			@Override
			public boolean isCellEditable( int row, int column ) {
				return false;
			}
			
		};
		return dtm;
	}
	
	public static String[][] getTokens( String path ) {
		String tokensLexico = open( path, false );
		String rows[] = tokensLexico.split( "\n" );
		String tokens[][] = new String[rows.length][4];
		String colums[];
		for ( int i = 0; i < rows.length; i++ ) {
			colums = rows[i].split( " " );
			for ( int j = 0; j < 4; j++ ) {
				tokens[i][j] = colums[j];		
			}
		}
		return tokens;
	}

	public static void save( String path, String texto ) {
		try {
			FileWriter fw = new FileWriter( path );
			BufferedWriter bw = new BufferedWriter( fw );
			PrintWriter archivo = new PrintWriter( bw );
			archivo.write( texto );
			archivo.close();
		} catch ( Exception e ) {
			
		}
	}
	
	public static String getName( String path ) {
		String name =  new File( path ).getName();
		return name.substring( 0, name.length() -4 );
	}
	
	public static String getNameWithExtension( String path ) {
		return new File( path ).getName();
	}
	
	public static String getPath( String path ) {
		return new File( path ).getParent();
	}
}
