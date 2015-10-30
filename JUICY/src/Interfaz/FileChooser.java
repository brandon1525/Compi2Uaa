package Interfaz;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Brandon Alan
 */
public class FileChooser extends JFrame {

	private JFileChooser fileChooser;
	private int respuesta;
	private String ruta;

	public FileChooser() {
		fileChooser = new JFileChooser( FileSystemView.getFileSystemView() );
		fileChooser.setFileFilter( new FileNameExtensionFilter( "Archivos de JUICY", "java" ) );
		fileChooser.setMultiSelectionEnabled( false );
	}

	public String saveFileChooser() {
		fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
		fileChooser.setDialogTitle( "Seleccionar directorio de destino para el archivo" );
		respuesta = fileChooser.showSaveDialog( this );
		if ( respuesta == JFileChooser.APPROVE_OPTION ) {
			ruta = fileChooser.getSelectedFile().getPath();
			if( !ruta.endsWith( ".java" ) )
				ruta += ".java";
			return ruta;
		}
		return null;
	}

	public String openFileChooser() {
		fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
		fileChooser.setDialogTitle( "Seleccionar el archivo a abrir" );
		respuesta = fileChooser.showOpenDialog( this );
		if ( respuesta == JFileChooser.APPROVE_OPTION ) {
			if( fileChooser.getSelectedFile().getPath().endsWith( ".java" ) )
				return fileChooser.getSelectedFile().getPath();
			else
				JOptionPane.showMessageDialog( null, "La extensión del archivo no es correcta, favor de seleccionar un archivo con extensión .java", "Extensión del archivo incorrecta", JOptionPane.ERROR_MESSAGE );
		}
		return null;
	}
}
