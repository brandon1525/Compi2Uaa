/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Class.FileManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Christian Israel López Villalobos
 */
public class TabPanel extends JScrollPane implements ActionListener, KeyListener, CaretListener, UndoableEditListener {

    private final Icon newIcon = new ImageIcon( getClass().getResource( "/Icons/16/new16.png" ) );
    private final Icon closeTabDisable = new ImageIcon( getClass().getResource( "/Icons/16/closeTabDisable16.png" ) );
    private final Icon closeTabEnable = new ImageIcon( getClass().getResource( "/Icons/16/closeTabEnable16.png" ) );

    private static final String[] RESERVED_WORDS = new String[]{
		"if", "then", "else", "fi", "do", "until", "while", "read", "write", "float", "int", "bool", "program"
	};

	private static String PATTERN_RESERVED_WORDS, PATTERN_NUMBERS, PATTERN_SIGNS;

	private final static FileChooser fileChooser = new FileChooser();

	public static final Color NO_SAVE = Color.ORANGE;
	public static final Color SAVE = Color.GRAY;
	private static final Color DEFAULT_RESERVED_WORDS_COLOR = new Color( 102, 217, 239  );
    private static final Color DEFAULT_COMMENTS_COLOR = new Color( 249, 38, 114 );
	private static final Color DEFAULT_NUMBERS_INTEGER_COLOR = new Color( 166, 226, 46 );
	private static final Color DEFAULT_SIGNS_COLOR = new Color( 253, 152, 31 );

	public static JTabbedPane tabbedPane;
	public static JLabel lineColumn;

	public final static boolean OPEN_FILE = true;
	public final static boolean NOT_OPEN_FILE = false;

	private final JTextPane textPane;
	private final StyleContext styleContext;
	private final TextLineNumber textLineNumber;
	private JPanel titleTabPanel;
	private JLabel titleTab;
	private JButton closeTab;
	private FileManager file;
	private UndoManager undoManager;
	private Boolean undo;

	public TabPanel( boolean openFile ) {

		int indexTab = tabbedPane.getTabCount();
		String title = "Nuevo documento", tip = null, path = null;

		textPane = new JTextPane();
		styleContext = StyleContext.getDefaultStyleContext();
		textPane.setFont( new Font( "Monospaced", Font.TRUETYPE_FONT, 18 ) );
		textPane.setBackground(new Color(73, 72, 62));
		textLineNumber = new TextLineNumber( textPane );
		setViewportView( textPane );
		setRowHeaderView( textLineNumber );
		textPane.addKeyListener( this );
		textPane.addCaretListener( this );
		
		getPatterns();

		if ( openFile ) {		// Vemos si tenemos que abrir un archivo
			path = fileChooser.openFileChooser();
			if ( path != null ) {		// En caso en que la ruta sea nula, es decir, dio en el boton cancelar en el JFileChooser
				file = new FileManager( path );
				title = FileManager.getNameWithExtension( path );		// Obtenemos el nombre del archivo para colocarlo como nombre de la pestaña
				tip = path;
				textPane.setText( FileManager.open( path, false ) );		// Colocamos el contenido del archivo
				textPane.setCaretPosition( 0 );
				findPattern();
				findCommentsAndNumbers();
			}
		} else {				// Si no, es un nuevo archivo
			file = new FileManager();
		}

		if ( openFile && path == null ) {		// En caso de que sea para abrir un archivo y si la ruta este nula, no agregamos la pestaña
			return;
		}
		
		undoManager = new UndoManager();
		textPane.getDocument().addUndoableEditListener( this );
		textPane.getInputMap( JComponent.WHEN_FOCUSED ).put( KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo" );
		textPane.getInputMap( JComponent.WHEN_FOCUSED ).put( KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo" );
		undo = true;
		
		ActionMap actionMap = textPane.getActionMap();
		actionMap.put( "Undo", new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					if ( undoManager.canUndo() ) {
						undoManager.undo();
					}
				} catch ( CannotUndoException | ArrayIndexOutOfBoundsException exp  ) {}
			}
		} );
		actionMap.put( "Redo", new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					if ( undoManager.canRedo() ) {
						undoManager.redo();
					}
				} catch ( CannotUndoException | ArrayIndexOutOfBoundsException exp ) {}
			}
		} );

		titleTabPanel = new JPanel();
		titleTab = new JLabel( title, newIcon, SwingConstants.CENTER );
		closeTab = new JButton( closeTabDisable );
		closeTab.setActionCommand( indexTab + "" );
		closeTab.setPreferredSize( new Dimension( 16, 16 ) );
		closeTab.setSize( new Dimension( 16, 16 ) );
		closeTab.setBorderPainted( false );
		closeTab.setContentAreaFilled( false );
		closeTab.setRolloverIcon( closeTabEnable );
		closeTab.addActionListener( this );

		titleTabPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
		titleTabPanel.add( titleTab );
		titleTabPanel.add( closeTab );
		titleTabPanel.setOpaque( false );

		tabbedPane.insertTab( null, null, this, tip, indexTab );
		tabbedPane.setTabComponentAt( indexTab, this.titleTabPanel );
		tabbedPane.setSelectedIndex( indexTab );
		textPane.requestFocus();
		textPane.getDocument().putProperty( DefaultEditorKit.EndOfLineStringProperty, "\n" );
	}

	public TabPanel( String path ) {

		int indexTab = tabbedPane.getTabCount();
		String title = "Nuevo documento", tip = null;

		textPane = new JTextPane();
		styleContext = StyleContext.getDefaultStyleContext();
                
		textPane.setFont( new Font( "Monospaced", Font.TRUETYPE_FONT, 24 ) );
		textPane.setBackground(new Color(73, 72, 62));
		textLineNumber = new TextLineNumber( textPane );
		setViewportView( textPane );
		setRowHeaderView( textLineNumber );
		textPane.addKeyListener( this );
		textPane.addCaretListener( this );
		
		getPatterns();
		
		file = new FileManager( path );
                System.out.println(file.getPath());
		title = FileManager.getNameWithExtension( path );		// Obtenemos el nombre del archivo para colocarlo como nombre de la pestaña
		tip = path;
		textPane.setText( FileManager.open( path, false ) );		// Colocamos el contenido del archivo
		textPane.setCaretPosition( 0 );
		findPattern();
		findCommentsAndNumbers();
		
		undoManager = new UndoManager();
		textPane.getDocument().addUndoableEditListener( this );
		textPane.getInputMap( JComponent.WHEN_FOCUSED ).put( KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo" );
		textPane.getInputMap( JComponent.WHEN_FOCUSED ).put( KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo" );
		undo = true;
		
		ActionMap actionMap = textPane.getActionMap();
		actionMap.put( "Undo", new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					if ( undoManager.canUndo() ) {
						undoManager.undo();
					}
				} catch ( CannotUndoException | ArrayIndexOutOfBoundsException exp  ) {}
			}
		} );
		actionMap.put( "Redo", new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					if ( undoManager.canRedo() ) {
						undoManager.redo();
					}
				} catch ( CannotUndoException | ArrayIndexOutOfBoundsException exp ) {}
			}
		} );

		titleTabPanel = new JPanel();
		titleTab = new JLabel( title, newIcon, SwingConstants.CENTER );
		closeTab = new JButton( closeTabDisable );
		closeTab.setActionCommand( indexTab + "" );
		closeTab.setPreferredSize( new Dimension( 16, 16 ) );
		closeTab.setSize( new Dimension( 16, 16 ) );
		closeTab.setBorderPainted( false );
		closeTab.setContentAreaFilled( false );
		closeTab.setRolloverIcon( closeTabEnable );
		closeTab.addActionListener( this );

		titleTabPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
		titleTabPanel.add( titleTab );
		titleTabPanel.add( closeTab );
		titleTabPanel.setOpaque( false );

		tabbedPane.insertTab( null, null, this, tip, indexTab );
		tabbedPane.setTabComponentAt( indexTab, this.titleTabPanel );
		tabbedPane.setSelectedIndex( indexTab );
		textPane.requestFocus();
		textPane.getDocument().putProperty( DefaultEditorKit.EndOfLineStringProperty, "\n" );
	}
	
	private void saveFile() {
		if ( file.isPathNull() ) {		// Significa que el archivo no ha sido guardado
			String path = fileChooser.saveFileChooser();
			if ( path != null ) {
				file = new FileManager( path );
				// Colocamos el nombre a la pestaña
				titleTab.setText( file.getName() );
				setStyle( Font.PLAIN, SAVE );
				FileManager.save( path, textPane.getText() );
			}
		} else {
			file.setAlter( false );
			FileManager.save( file.getPath(), textPane.getText() );
			setStyle( Font.PLAIN, SAVE );
		}
	}

	/**
	 * Cambia el estilo del titulo de la pestaña en negritas o normal
	 *
	 * <ul>
	 * <li>Font.BOLD
	 * <li>Font.PLAIN
	 * </ul>
	 *
	 * @param style estilo de la fuente
	 * @param color color de la fuente
	 */
	public void setStyle( int style, Color color ) {
		titleTab.setForeground( color );
		titleTab.setFont( tabbedPane.getFont().deriveFont( style ) );
	}

	/**
	 * Coloca el nombre de la pestaña
	 *
	 */
	public void setTitleTab() {
		titleTab.setText( file.getName() );
	}

	/**
	 * Obtiene el contenido del componente JTextPane
	 *
	 * @return Cadena con texto de JTextPane
	 */
	public String getText() {
		return textPane.getText();
	}

	/**
	 * Coloca el titulo y estilo a la pestaña
	 *
	 */
	public void setTitleTabAndStyle() {
		titleTab.setText( file.getName() );
		setStyle( Font.PLAIN, SAVE );
	}

	/**
	 * Obtiene el archivo afiliado a la pestaña
	 *
	 * @return FileZapphire
	 */
	public FileManager getFile() {
		return file;
	}

	/**
	 * Coloca el nuevo archivo afiliado a la pestaña
	 *
	 * @param file Nuevo archivo para la pestaña
	 */
	public void setFile( FileManager file ) {
		this.file = file;
	}

	/**
	 * Devuelve si la ruta del archivo es nula
	 *
	 * @return booleano
	 */
	public boolean isPathNullFile() {
		return file.isPathNull();
	}

	/**
	 * Coloca si el archivo a sido modificado
	 *
	 * @param alter a sido modificado el archivo
	 */
	public void setAlterFile( boolean alter ) {
		file.setAlter( alter );
	}

	/**
	 * Devuelve si el archivo fue modificado
	 *
	 * @return booleano
	 */
	public boolean isAlterFile() {
		return file.isAlter();
	}

	/**
	 * Obtiene la ruta del archivo
	 *
	 * @return Ruta del archivo
	 */
	public String getPathFile() {
		return file.getPath();
	}

	/**
	 * Obtiene el nombre del archivo
	 *
	 * @return nombre del archivo
	 */
	public String getNameFile() {
		if ( file.isPathNull() ) {
			return "Nuevo documento";
		}
		return file.getName();
	}
	
	public JTextPane getTextPane() {
		return textPane;
	}
	
	public UndoManager getUndoManager() {
		return undoManager;
	}

	private void getPatterns() {
		StringBuilder buff = new StringBuilder( "" );
		buff.append( "(" );
		for ( String reservedWord : RESERVED_WORDS ) {
			buff.append( "\\b" ).append( reservedWord ).append( "\\b" ).append( "|" );
		}
		buff.deleteCharAt( buff.length() - 1 );
		buff.append( ")" );
		PATTERN_RESERVED_WORDS = buff.toString();
		
		PATTERN_NUMBERS = "\\b\\d+(\\.\\d*)?\\b";
		
		PATTERN_SIGNS = "\\+|\\-|\\*|\\/";
	}

	private void findPattern() {
		clearTextColors();
		Pattern pattern = Pattern.compile( PATTERN_RESERVED_WORDS );
		Matcher match = pattern.matcher( textPane.getText() );
		while ( match.find() ) {
			paintText( match.start(), match.end() - match.start(), DEFAULT_RESERVED_WORDS_COLOR );
		}
		
		pattern = Pattern.compile( PATTERN_NUMBERS );
		match = pattern.matcher( textPane.getText() );
		while ( match.find() ) {
			paintText( match.start(), match.end() - match.start(), DEFAULT_NUMBERS_INTEGER_COLOR );
		}
		
		pattern = Pattern.compile( PATTERN_SIGNS );
		match = pattern.matcher( textPane.getText() );
		while ( match.find() ) {
			paintText( match.start(), match.end() - match.start(), DEFAULT_SIGNS_COLOR );
		}
	}

	private void findCommentsAndNumbers() {
		String text = getText();
		int length = text.length();
		boolean bandera = false, punto = false;
		int i = 0, indexStart;
		char c;
		
		// Numeros enteros y flotantes
		
		/*while( i < length ) {
			c = text.charAt( i );
			// Si el caracter 'c' es un dígito
			if( Character.isDigit( c ) ) {
				indexStart = i++;
				// Mientras el caracter sea un digito o sea un punto y la bandera punto no este activada
				while( i < length && ( Character.isDigit( text.charAt( i ) ) || ( text.charAt( i ) == '.' && !punto ) ) ) {
					if( text.charAt( i ) == '.' )
						punto = true;
					i++;
				}
				// Verificamos si el caracter con el que rompio el ciclo no es una letra
				if( i < length && !Character.isLetter( text.charAt( i ) ) ) {
					/* 
					Checamos que el caracter con el que rompio el ciclo
					es un punto y si la bandera punto este activado, si es asi
					tenemos que aumentar el indice para no tomar en cuenta el
					segundo punto para el pintado
					
					if( text.charAt( i ) == '.' && punto ) {
						bandera = true;
						paintText( indexStart, i - indexStart, DEFAULT_NUMBERS_FLOAT_COLOR ); 
						i++;
					}
				}
				if( !bandera ) {
					if( punto )
						paintText( indexStart, i - indexStart, DEFAULT_NUMBERS_FLOAT_COLOR ); 
					else
						paintText( indexStart, i - indexStart, DEFAULT_NUMBERS_INTEGER_COLOR );
				}
				punto = bandera = false;
				// Cuando no existen espacios entre expresiones
			} else if( c == '=' || c == '+' || c == '-' || c == '*' || c == '/' || c == '=' ) {
				paintText( i, 1, DEFAULT_SIGNS_COLOR );
				i++;
			} else if( c == ' ' || c == '\n' || c == '\t' || c == '(' || c == '<' || c == '>' ) {
				i++;
			} else {
				// Si llega una letra (identificador o palabra reservada) eliminamos todos los caracteres hasta encontrar...
				while( c != ' ' && c != '\n' && c != '\t' && c != '+' && c != '-' && c != '*' && c != '/' && c != '=' ) {
					if( ++i == length )
						break;
					c = text.charAt( i );
				}
			}
		}*/
		
		// Buscar comentarios sencillos y multiples
		i = 0;
		bandera = false;
		while ( i < length - 1 ) {
			if ( text.charAt( i ) == '/' && text.charAt( i +1 ) == '*' ) {
				indexStart = i;
				i += 2;
				while ( i < length ) {
					if ( text.charAt( i++ ) == '*' ) {
						if ( i < length ) {
							if ( text.charAt( i++ ) == '/' ) {
								bandera = true;
								paintText( indexStart, i - indexStart, DEFAULT_COMMENTS_COLOR );
								break;
							} else
								i--;
						}
					}
				}
				if ( !bandera )
					paintText( indexStart, length, DEFAULT_COMMENTS_COLOR );
				bandera = false;
			} else if( text.charAt( i ) == '/' && text.charAt( i +1 ) == '/' ) {
				indexStart = i;
				i += 2;
				while( i < length && text.charAt( i ) != '\n' )
					i++;
				paintText( indexStart, i - indexStart, DEFAULT_COMMENTS_COLOR );
			} else
				i++;
		}
		
		
	}

	public void clearTextColors() {
		AttributeSet aset = styleContext.addAttribute( SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(248, 248, 242) );
		textPane.getStyledDocument().setCharacterAttributes( 0, textPane.getText().length(), aset, true );
	}

	public void paintText( int offset, int length, Color type ) {
		AttributeSet aset = styleContext.addAttribute( SimpleAttributeSet.EMPTY, StyleConstants.Foreground, type );
		if( type == DEFAULT_COMMENTS_COLOR )
			aset = styleContext.addAttribute( aset, StyleConstants.Italic, true );
		else if ( type == DEFAULT_RESERVED_WORDS_COLOR )
			aset = styleContext.addAttribute( aset, StyleConstants.Bold, true );
		textPane.getStyledDocument().setCharacterAttributes( offset, length, aset, true );
	}
	
	public void updateLineColumn() {
		int pos = textPane.getCaretPosition();
		Element map = textPane.getDocument().getDefaultRootElement();
		int row = map.getElementIndex( pos );
		Element lineElem = map.getElement( row );
		int col = pos - lineElem.getStartOffset();
		lineColumn.setText( "linea " + ( row + 1 ) + ", columna " + ( col + 1 ) );
	}

	private void setActionCommandTabs() {
		for ( int i = 0; i < tabbedPane.getTabCount(); i++ ) {
			( ( TabPanel ) tabbedPane.getComponentAt( i ) ).closeTab.setActionCommand( i + "" );
		}
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		String actionCommand = ( ( JButton ) e.getSource() ).getActionCommand();
		int index = Integer.parseInt( actionCommand );			// Obtenemos el indice de la pestaña que se va a cerrar
		int respuesta = -1;
		if ( file.isAlter() ) {		// Si fue modificado el archivo
			respuesta = JOptionPane.showConfirmDialog( null, "El archivo \"" + getNameFile() + "\" ha sido modificado, ¿Desea guardarlo?", "Guardar archivo...", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE );
			if ( respuesta == JOptionPane.YES_OPTION ) {
				saveFile();
			}
		}
		if ( respuesta != JOptionPane.CANCEL_OPTION ) {
			tabbedPane.remove( index );
			setActionCommandTabs();
		}
	}

	@Override
	public void keyTyped( KeyEvent e ) {
		if ( !e.isControlDown() ) {
			file.setAlter( true );
			setStyle( Font.BOLD, NO_SAVE );
			undo = true;
		}
	}

	@Override
	public void keyPressed( KeyEvent e ) {
		if ( !e.isControlDown() ) {
			undo = false;
			findPattern();
			findCommentsAndNumbers();
		}
	}

	@Override
	public void keyReleased( KeyEvent e ) {
		if ( !e.isControlDown() ) {
			undo = false;
			findPattern();
			findCommentsAndNumbers();
		}
	}

	@Override
	public void caretUpdate( CaretEvent e ) {
		updateLineColumn();
	}

	@Override
	public void undoableEditHappened( UndoableEditEvent e ) {
		if( undo )
			undoManager.addEdit( e.getEdit() );
	}

}
