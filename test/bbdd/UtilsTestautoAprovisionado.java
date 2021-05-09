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
public class UtilsTestautoAprovisionado {
	
	private static final String UID_PRUEBAS = "usig";

	private UtilsTestautoAprovisionado() { }
	
	/** Inicializa la bd autoaprovisionado.
	 * @throws SQLException si error sql
	 * @throws IOException si error io
	 */
	public static void inicializaDb() throws SQLException, IOException {
		BbddRunner.ejecutarArcos("Documentos/scripts/opc.autoaprovisionado/Desarrollo/dropTables.sql");
		BbddRunner.ejecutarArcos("Documentos/scripts/opc.autoaprovisionado/01-createTable.sql");
		BbddRunner.ejecutar("Documentos/scripts/opc.autoaprovisionado/uv.sql");
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
