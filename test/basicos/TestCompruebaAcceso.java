package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import bbdd.UtilsTestDocentia;
import es.ujaen.uvirtual.adm.CompruebaAcceso;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.ErroresPersonalizados;
import es.ujaen.uvirtual.beans.Menu;
import es.ujaen.uvirtual.beans.MenuSubred;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;
import es.ujaen.uvirtual.utilidades.AyudaURL;
import es.ujaen.uvirtual.utilidades.UVException;

/** test compruebaAcceso.
 *
 */
public class TestCompruebaAcceso {
	
	private static final String MENSAJE_CON_ACCESO = "Debe tener acceso";
	private static final String MENSAJE_SIN_ACCESO = "NO debe tener acceso";
	private static final String IP_EXTERNA = "140.214.3.51";
	private static final String URL_INFORMACION_GENERAL = "/pub/es/informaciongeneral";
	private static final String URL_CONVOCATORIA_CRUD = "/srv/es/informacionadministrativa/docentia/convocatoriacrud";
			
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     * @throws UVException si error uv
     */
    @BeforeClass
    public static void preparaBd() throws SQLException, IOException, UVException {
    	BbddRunner.conectarBd();
    	UtilsTestDocentia.inicializaDocentia();
    }

	/** obtener comprueba acceso valido por usuario.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testA01() throws SQLException {
		Usuario usuarioUsig = CrearUsuario.usuario("usig");
		String controladorInformacionGeneral = AyudaURL.obtenerControlador(URL_INFORMACION_GENERAL);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuInformacionGeneral = modeloAdministracion.listaMenuDeControlador(controladorInformacionGeneral);
		String menuAsString = menuInformacionGeneral.toString();

		boolean tieneAcceso = CompruebaAcceso.acceso(usuarioUsig, menuInformacionGeneral, IP_EXTERNA);
		assertNotNull(menuAsString);
		assertTrue(MENSAJE_CON_ACCESO, tieneAcceso);
	}

	/** obtener comprueba acceso valido por url.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testA02() throws SQLException {
		String controladorInformacionGeneral = AyudaURL.obtenerControlador(URL_INFORMACION_GENERAL);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuInformacionGeneral = modeloAdministracion.listaMenuDeControlador(controladorInformacionGeneral);

		String errorAcceso = CompruebaAcceso.errorAcceso(null, menuInformacionGeneral, IP_EXTERNA, URL_INFORMACION_GENERAL, true);

		assertNull(MENSAJE_CON_ACCESO, errorAcceso);
	}
	
	/** obtener comprueba acceso valido sin url.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testA03() throws SQLException {
		String controladorInformacionGeneral = AyudaURL.obtenerControlador(URL_INFORMACION_GENERAL);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuInformacionGeneral = modeloAdministracion.listaMenuDeControlador(controladorInformacionGeneral);

		String errorAcceso = CompruebaAcceso.errorAcceso(null, menuInformacionGeneral, IP_EXTERNA, null, true);

		assertNull(MENSAJE_CON_ACCESO, errorAcceso);
	}

	/** obtener comprueba acceso erroneo.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testA04() throws SQLException {
		Usuario usuarioUsig = CrearUsuario.usuario("usig");
		String controladorConvocatoriaCrud = AyudaURL.obtenerControlador(URL_CONVOCATORIA_CRUD);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuConvocatoriaCrud = modeloAdministracion.listaMenuDeControlador(controladorConvocatoriaCrud);

		boolean tieneAcceso = CompruebaAcceso.acceso(usuarioUsig, menuConvocatoriaCrud, IP_EXTERNA);

		assertTrue(MENSAJE_CON_ACCESO, tieneAcceso);
	}
	
	/** obtener comprueba acceso erroneo.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE01() throws SQLException {
		Usuario usuarioEstudiante = CrearUsuario.usuario("estudiante1");
		String controladorConvocatoriaCrud = AyudaURL.obtenerControlador(URL_CONVOCATORIA_CRUD);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuConvocatoriaCrud = modeloAdministracion.listaMenuDeControlador(controladorConvocatoriaCrud);

		boolean tieneAcceso = CompruebaAcceso.acceso(usuarioEstudiante, menuConvocatoriaCrud, IP_EXTERNA);

		assertTrue(MENSAJE_SIN_ACCESO, !tieneAcceso);
	}

	/** obtener comprueba error acceso menu no dispinible.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE02() throws SQLException {
		Usuario usuarioEstudiante = CrearUsuario.usuario("usig");
		String controladorConvocatoriaCrud = AyudaURL.obtenerControlador(URL_CONVOCATORIA_CRUD);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuConvocatoriaCrud = modeloAdministracion.listaMenuDeControlador(controladorConvocatoriaCrud);
		menuConvocatoriaCrud.setDisponible(false);

		String errorAcceso = CompruebaAcceso.errorAcceso(usuarioEstudiante, menuConvocatoriaCrud, IP_EXTERNA, true);

		assertEquals(MENSAJE_SIN_ACCESO, ErroresPersonalizados.ERROR_PRIVILEGIOS, errorAcceso);
	}

	/** obtener comprueba acceso no valido sin url.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE03() throws SQLException {
		String controladorInformacionGeneral = AyudaURL.obtenerControlador(URL_CONVOCATORIA_CRUD);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuInformacionGeneral = modeloAdministracion.listaMenuDeControlador(controladorInformacionGeneral);

		String errorAcceso = CompruebaAcceso.errorAcceso(null, menuInformacionGeneral, IP_EXTERNA, null, true);

		assertEquals(MENSAJE_SIN_ACCESO, ErroresPersonalizados.ERROR_PRIVILEGIOS, errorAcceso);
	}
	
	/** obtener comprueba error acceso por url publica, no disponible.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE04() throws SQLException {
		String controlador = AyudaURL.obtenerControlador(URL_INFORMACION_GENERAL);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menu = modeloAdministracion.listaMenuDeControlador(controlador);
		menu.setDisponible(false);

		String errorAcceso = CompruebaAcceso.errorAcceso(null, menu, IP_EXTERNA, URL_INFORMACION_GENERAL, true);

		assertEquals(MENSAJE_SIN_ACCESO, ErroresPersonalizados.ERROR_PRIVILEGIOS, errorAcceso);
	}
	
	/** obtener comprueba acceso no valido sin url, menu no disponible.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE05() throws SQLException {
		String controlador = AyudaURL.obtenerControlador(URL_INFORMACION_GENERAL);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menu = modeloAdministracion.listaMenuDeControlador(controlador);
		menu.setDisponible(false);

		String errorAcceso = CompruebaAcceso.errorAcceso(null, menu, IP_EXTERNA, null, true);

		assertEquals(MENSAJE_SIN_ACCESO, ErroresPersonalizados.ERROR_PRIVILEGIOS, errorAcceso);
	}

	/** obtener comprueba error acceso por url publica, no anonimo.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE06() throws SQLException {
		String controlador = AyudaURL.obtenerControlador(URL_INFORMACION_GENERAL);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menu = modeloAdministracion.listaMenuDeControlador(controlador);
		menu.setAccesoAnonimo(false);

		String errorAcceso = CompruebaAcceso.errorAcceso(null, menu, IP_EXTERNA, URL_INFORMACION_GENERAL, true);

		assertEquals(MENSAJE_SIN_ACCESO, ErroresPersonalizados.ERROR_PRIVILEGIOS, errorAcceso);
	}
	
	/** obtener comprueba acceso no valido sin url, menu no anonimo.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE07() throws SQLException {
		String controlador = AyudaURL.obtenerControlador(URL_INFORMACION_GENERAL);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menu = modeloAdministracion.listaMenuDeControlador(controlador);
		menu.setAccesoAnonimo(false);

		String errorAcceso = CompruebaAcceso.errorAcceso(null, menu, IP_EXTERNA, null, true);

		assertEquals(MENSAJE_SIN_ACCESO, ErroresPersonalizados.ERROR_PRIVILEGIOS, errorAcceso);
	}
	
	/** obtener comprueba acceso no valido por mascara de red.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE08() throws SQLException {
		Usuario usuarioUsig = CrearUsuario.usuario("usig");
		String controladorConvocatoriaCrud = AyudaURL.obtenerControlador(URL_CONVOCATORIA_CRUD);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuConvocatoriaCrud = modeloAdministracion.listaMenuDeControlador(controladorConvocatoriaCrud);
		MenuSubred menuSubred = new MenuSubred(menuConvocatoriaCrud.getCodigo(), false, "solo interno", "127.0.0.1/32");
		ArrayList<MenuSubred> accesoSubredes = new ArrayList<>();
		accesoSubredes.add(menuSubred);
		menuConvocatoriaCrud.setAccesoSubredes(accesoSubredes);

		boolean tieneAcceso = CompruebaAcceso.acceso(usuarioUsig, menuConvocatoriaCrud, IP_EXTERNA);

		assertTrue(MENSAJE_SIN_ACCESO, !tieneAcceso);
	}

	/** obtener comprueba acceso no valido por mascara de red, desactivado.
	 * @throws SQLException si error bd
	 */
	@Test
	public void testE09() throws SQLException {
		Usuario usuarioUsig = CrearUsuario.usuario("usig");
		String controladorConvocatoriaCrud = AyudaURL.obtenerControlador(URL_CONVOCATORIA_CRUD);
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		Menu menuConvocatoriaCrud = modeloAdministracion.listaMenuDeControlador(controladorConvocatoriaCrud);
		MenuSubred menuSubredInterna = new MenuSubred(menuConvocatoriaCrud.getCodigo(), false, "solo interno", "127.0.0.1/32");
		MenuSubred menuSubredExterna = new MenuSubred(menuConvocatoriaCrud.getCodigo(), true, "externa", IP_EXTERNA + "/32");
		ArrayList<MenuSubred> accesoSubredes = new ArrayList<>();
		accesoSubredes.add(menuSubredInterna);
		accesoSubredes.add(menuSubredExterna);
		menuConvocatoriaCrud.setAccesoSubredes(accesoSubredes);

		boolean tieneAcceso = CompruebaAcceso.acceso(usuarioUsig, menuConvocatoriaCrud, IP_EXTERNA);

		assertTrue(MENSAJE_SIN_ACCESO, !tieneAcceso);
	}

}
