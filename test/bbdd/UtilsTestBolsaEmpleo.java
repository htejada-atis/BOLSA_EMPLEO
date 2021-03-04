package bbdd;

import java.io.IOException;
import java.sql.SQLException;

import controlador.implementacion.PeticionHttp;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;

/** 
 * Utilidades para los test bolsa de empleo
 */
public class UtilsTestBolsaEmpleo {
	
	private static final String UID_PRUEBAS = "usig";

	private UtilsTestBolsaEmpleo() { }
	
	/** 
	 * Inicializa la bd para BolsaEmpleo.
	 * @throws SQLException si error sql
	 * @throws IOException si error io
	 */
	public static void inicializaBolsaEmpleo() throws SQLException, IOException {
		// BbddRunner.insertMasivo("Documentos/scripts/opc.bolsaempleo/datos_desarrollo/dropTables.sql");
		BbddRunner.insertMasivo("Documentos/scripts/opc.bolsaempleo/datos_desarrollo/cleanUv.sql");
		BbddRunner.insertMasivo("Documentos/scripts/opc.bolsaempleo/uv.sql");
		//BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/creartablasbolsaempleo.sql");
		//BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/creacionsecuenciasytriggers.sql");
		//BbddRunner.insertMasivo("Documentos/scripts/opc.bolsaempleo/datos_desarrollo/creacionregistrosprueba.sql");    	
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
	
}
