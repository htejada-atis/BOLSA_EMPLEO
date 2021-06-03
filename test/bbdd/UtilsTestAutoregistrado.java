package bbdd;

import java.io.IOException;
import java.sql.SQLException;

import controlador.implementacion.PeticionHttp;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;

/** utilidades para los test docentia.
 *
 */
public class UtilsTestAutoregistrado {
	
	private static final String UID_PRUEBAS = "usig";

	private UtilsTestAutoregistrado() { }
	
	private static boolean cargado = false;
	
	/** Inicializa la bd autoaprovisionado.
	 * @throws SQLException si error sql
	 * @throws IOException si error io
	 */
	public static void inicializaDb() throws SQLException, IOException {
		if (!cargado) {
			BbddRunner.ejecutarArcos("Documentos/scripts/opc.autoaprovisionado/Desarrollo/dropTables.sql");
			BbddRunner.ejecutarArcos("Documentos/scripts/opc.autoaprovisionado/01-createTable.sql");
			BbddRunner.ejecutarArcos("Documentos/scripts/opc.autoaprovisionado/02-createTrigger.sql");
			BbddRunner.insertMasivo("Documentos/scripts/opc.autoaprovisionado/uv.sql");
			cargado = true;
		}
	}
	
    /** obtiene una peticion autenticada con el usuario de pruebas de docentia.
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
