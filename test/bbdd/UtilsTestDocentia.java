package bbdd;

import java.io.IOException;
import java.sql.SQLException;

import controlador.implementacion.PeticionHttp;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.utilidades.Memcache;
import es.ujaen.uvirtual.utilidades.UVException;

/** utilidades para los test docentia.
 *
 */
public class UtilsTestDocentia {
	
	private static final String UID_PRUEBAS = "usig";
	
	private static boolean inicializado = false;

	private UtilsTestDocentia() { }
	
	/** Inicializa la bd para docentia.
	 * @throws SQLException si error sql
	 * @throws IOException si error io
	 * @throws UVException si error uv en memcache
	 */
	public static void inicializaDocentia() throws SQLException, IOException, UVException {
		if (!inicializado) {
			BbddRunner.conectarBd();
			BbddRunner.inicializaBd();
			BbddRunner.ejecutar("Documentos/scripts/opc.docentia/datos_desarrollo/dropTables.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.docentia/creartablasdocentia.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.docentia/creacionsecuenciasytriggers.sql");
			BbddRunner.insertMasivo("Documentos/scripts/opc.docentia/datos_desarrollo/creacionregistrosprueba.sql");
			BbddRunner.insertMasivo("Documentos/scripts/opc.docentia/uv.sql");
			Memcache mc = Memcache.getInstance();
			mc.flushAll();
			inicializado = true;
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
