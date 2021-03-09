package bbdd;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.implementacion.PeticionHttp;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;

/** Utilidades para los test bolsa de empleo.
 */
public class UtilsTestBolsaEmpleo {
	private static final String NOMBREDEESTACLASE = UtilsTestBolsaEmpleo.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	private static final String UID_PRUEBAS = "usig";

	private UtilsTestBolsaEmpleo() { }
	
	/** 
	 * Inicializa la bd para BolsaEmpleo. 
	 */
	public static void inicializaBolsaEmpleo() throws SQLException, IOException {
		File directoryPath;
		File filesList[];
		
		// limpieza
		directoryPath = new File("Documentos/scripts/opc.bolsaempleo/datos_desarrollo/clean");
		filesList = directoryPath.listFiles();		
		for (int i = filesList.length - 1; i >= 0; i--) {
			UtilsTestBolsaEmpleo.ejecutarFile(filesList[i]);					
	    }
				
		// creación
		directoryPath = new File("Documentos/scripts/opc.bolsaempleo");
		filesList = directoryPath.listFiles();
		for (File file : filesList) {
			UtilsTestBolsaEmpleo.ejecutarFile(file);
		}
	}
	
    /** obtiene una peticion autenticada con el usuario de pruebas de BolsaEmpleo.
     * @return peticion autenticada
     */
    public static PeticionHttp peticionAutenticada() {
		UVDatos datos = new UVDatos();
		datos.setIdentificadorUsuario(UID_PRUEBAS);
		
		Usuario usuario = CrearUsuario.usuario(UID_PRUEBAS);
		datos.setUsuario(usuario);
		
		PeticionHttp peticion = new PeticionHttp();
		peticion.setUVDatos(datos);
		
		return peticion;
    }
    
    private static void ejecutarFile(File file) throws SQLException, IOException {
    	String name = file.getName();
    	
    	if (file.isDirectory()) {
    		return;
    	}
    	    	
    	LOGGER.log(Level.INFO, "Ejecutando: " + file.getAbsolutePath());
    	    	
    	String methodKey = name.split("-")[1];		
		switch (methodKey) {
		case "im":
			BbddRunner.insertMasivo(file.getAbsolutePath());
			break;
		case "eje":
			BbddRunner.ejecutar(file.getAbsolutePath());
			break;
		default:
			LOGGER.log(Level.WARNING, "Error ejecutando fichero. No entiendo el tipo: " + name);						
		}
    }
	
}
