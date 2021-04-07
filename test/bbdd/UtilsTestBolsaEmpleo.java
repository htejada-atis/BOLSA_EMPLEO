package bbdd;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
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
	private static final int LONGITUD_NAME_SQL = 3;

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
		Arrays.sort(filesList);
		for (int i = filesList.length - 1; i >= 0; i--) {
			UtilsTestBolsaEmpleo.ejecutarFile(filesList[i]);					
	    }
		
		// creación
		directoryPath = new File("Documentos/scripts/opc.bolsaempleo");
		filesList = directoryPath.listFiles();
		Arrays.sort(filesList);
		for (File file : filesList) {
			UtilsTestBolsaEmpleo.ejecutarFile(file);
		}
		
		// datos de prueba
		directoryPath = new File("Documentos/scripts/opc.bolsaempleo/datos_desarrollo/datos_prueba");
		filesList = directoryPath.listFiles();
		Arrays.sort(filesList);
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
    	String[] nameSplit = name.split("-");
    	
    	if (file.isDirectory() || nameSplit.length != LONGITUD_NAME_SQL) {
    		LOGGER.log(Level.WARNING, "El nombre de sql no válido: " + name);
    		return;
    	}
    	    	
    	LOGGER.log(Level.INFO, "Ejecutando sql: " + file.getAbsolutePath());
    	    	
    	String methodKey = nameSplit[1];		
		switch (methodKey) {
		case "arcosim":
			BbddRunner.insertMasivoArcos(file.getAbsolutePath());
			break;
		case "arcoseje":
			BbddRunner.ejecutarArcos(file.getAbsolutePath());
			break;
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
