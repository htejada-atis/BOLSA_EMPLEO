package basicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import bbdd.UtilsTestDocentia;
import controlador.implementacion.CadenaFiltros;
import controlador.implementacion.CadenaFiltrosEnlazada;
import controlador.implementacion.ConfiguracionFiltro;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.vistas.VistaUVirtual;
import es.ujaen.uvirtual.filtros.GeneradorCabecera;
import es.ujaen.uvirtual.filtros.GeneradorContenido;
import es.ujaen.uvirtual.filtros.GeneradorCuerpoPostServlet;
import es.ujaen.uvirtual.filtros.GeneradorCuerpoPreServlet;
import es.ujaen.uvirtual.filtros.GeneradorLog;
import es.ujaen.uvirtual.filtros.GeneradorPDF;
import es.ujaen.uvirtual.filtros.GeneradorPie;
import es.ujaen.uvirtual.filtros.ValidaAcceso;
import es.ujaen.uvirtual.filtros.ValidaAutenticacion;
import es.ujaen.uvirtual.filtros.ValidaEstadoSistema;
import es.ujaen.uvirtual.filtros.ValidaIdioma;
import es.ujaen.uvirtual.filtros.ValidaSAML;

/** test de filtros.
 * @author jmoral
 */
public class TestFiltro {
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() {
    	BbddRunner.conectarBd();
    }
	
	/** prueba filtro valida acceso.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA01ValidaAcceso() throws IOException, ServletException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	peticion.setRequestURI("/srv/es/informacionadministrativa/docentia/convocatoriacrud");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

	    ValidaAcceso validaAcceso = new ValidaAcceso();
	    validaAcceso.doFilter(peticion, respuesta, cadenaFiltros);
	    assertNotNull("existe menu", peticion.getUVDatos().getMenu());
	    assertNull("no redirige", respuesta.getSendRedirect());
	}

	/** prueba filtro ValidaEstadoSistema.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA02ValidaEstadoSistema() throws IOException, ServletException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	peticion.setRequestURI("/srv/es/informacionadministrativa/docentia/convocatoriacrud");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

    	ValidaEstadoSistema filtro = new ValidaEstadoSistema();
    	filtro.doFilter(peticion, respuesta, cadenaFiltros);
	    assertNull("no redirige", respuesta.getSendRedirect());
	}
	
	/** prueba filtro GeneradorLog.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA03GeneradorLog() throws IOException, ServletException {
    	PeticionHttp peticion = new PeticionHttp();
    	peticion.setRequestURI("/pub/es/index");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

    	GeneradorLog filtro = new GeneradorLog();
    	filtro.doFilter(peticion, respuesta, cadenaFiltros);
	    assertNull("no redirige", respuesta.getSendRedirect());
	    assertNotNull("debe poner UVDatos", peticion.getUVDatos());
	    assertNotNull("debe poner Acceso", peticion.getUVDatos().getAcceso());
	}

	/** prueba filtro ValidaSAML.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA04ValidaSAML() throws IOException, ServletException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	peticion.setRequestURI("/sso");
    	peticion.setParameter("usuario", "usig");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

    	ValidaSAML filtro = new ValidaSAML();
    	ConfiguracionFiltro configuracionFiltro = new ConfiguracionFiltro();
    	configuracionFiltro.setInitParameter("UserAttribute", "uid");
    	configuracionFiltro.setInitParameter("UserSessionAttribute", "uid");
    	configuracionFiltro.setInitParameter("ValidSessionKey", "esValidaLaSesion");
    	
    	filtro.init(configuracionFiltro);
    	filtro.doFilter(peticion, respuesta, cadenaFiltros);
    	filtro.destroy();
	    assertNull("no redirige", respuesta.getSendRedirect());
	    assertNotNull("debe poner en session UserSessionAttribute", peticion.getSession().getAttribute("uid"));
	}

	/** prueba filtro ValidaIdioma.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA05ValidaIdioma() throws IOException, ServletException {
    	PeticionHttp peticion = new PeticionHttp();
    	peticion.setRequestURI("/pub/es/index");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

    	ValidaIdioma filtro = new ValidaIdioma();
    	filtro.doFilter(peticion, respuesta, cadenaFiltros);
	    assertNull("no redirige", respuesta.getSendRedirect());
	}

	/** prueba filtro GeneradorContenido.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA06GeneradorContenido() throws IOException, ServletException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	peticion.setRequestURI("/srv/es/index");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

    	GeneradorContenido filtro = new GeneradorContenido();
    	filtro.doFilter(peticion, respuesta, cadenaFiltros);
	    assertNull("no redirige", respuesta.getSendRedirect());
	    assertEquals("tiene un javascript", 1, peticion.getUVDatos().getFicherosJS().size());
	}
	
	/** prueba filtro GeneradorCabecera.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA07GeneradorCabecera() throws IOException, ServletException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	peticion.setRequestURI("/srv/es/index");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

    	GeneradorContenido filtroContenido = new GeneradorContenido();
    	filtroContenido.doFilter(peticion, respuesta, cadenaFiltros);
    	GeneradorCabecera filtroCabecera = new GeneradorCabecera();
    	filtroCabecera.doFilter(peticion, respuesta, cadenaFiltros);
	    assertNull("no redirige", respuesta.getSendRedirect());
	    assertEquals("tiene un jsp", 1, peticion.getUVDatos().getFicherosJSP().size());
	    VistaUVirtual vista = (VistaUVirtual) peticion.getUVDatos().getVistas().get(VistaUVirtual.class.getName());
	    assertEquals("tiene pagina de inicio", ConfiguracionGlobal.getUrlRaiz(), vista.getPaginaInicio());
	}
	
	/** prueba filtro GeneradorCuerpoPreServlet.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA08GeneradorCuerpoPreServlet() throws IOException, ServletException {
    	PeticionHttp peticion = new PeticionHttp();
    	peticion.setRequestURI("/srv/es/index");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

    	GeneradorLog filtroLog = new GeneradorLog();
    	filtroLog.doFilter(peticion, respuesta, cadenaFiltros);
    	GeneradorContenido filtroContenido = new GeneradorContenido();
    	filtroContenido.doFilter(peticion, respuesta, cadenaFiltros);
    	GeneradorCabecera filtroCabecera = new GeneradorCabecera();
    	filtroCabecera.doFilter(peticion, respuesta, cadenaFiltros);
    	GeneradorCuerpoPreServlet filtroCuerpoPre = new GeneradorCuerpoPreServlet();
    	peticion.getUVDatos().setFormatoSalida(ConfiguracionGlobal.getParametroCadenaNE("ayudaurl.formatohtml"));
    	filtroCuerpoPre.doFilter(peticion, respuesta, cadenaFiltros);
	    assertNull("no redirige", respuesta.getSendRedirect());
	    assertEquals("tiene dos jsp", 2, peticion.getUVDatos().getFicherosJSP().size());
	    VistaUVirtual vista = (VistaUVirtual) peticion.getUVDatos().getVistas().get(VistaUVirtual.class.getName());
	    assertNotNull("tiene miga de pan", vista.getMigaDePan());
	    assertNotNull("tiene menu hijos", vista.getMenusHijos());
	    assertNotNull("tiene menu mismo nivel", vista.getMenusMismoNivel());
	}
	
	/** prueba filtro GeneradorCuerpoPostServlet.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA09GeneradorCuerpoPostServlet() throws IOException, ServletException {
		PeticionHttp peticion = new PeticionHttp();
    	peticion.setRequestURI("/pub/es/index");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	ValidaEstadoSistema filtroValidaEstadoSistema = new ValidaEstadoSistema();
    	GeneradorLog filtroLog = new GeneradorLog();
	    ValidaIdioma filtroIdioma = new ValidaIdioma();
	    GeneradorPDF filtroPdf = new GeneradorPDF();
    	GeneradorContenido filtroContenido = new GeneradorContenido();
    	GeneradorCabecera filtroCabecera = new GeneradorCabecera();
    	GeneradorCuerpoPreServlet filtroCuerpoPre = new GeneradorCuerpoPreServlet();
    	GeneradorCuerpoPostServlet filtroCuerpoPost = new GeneradorCuerpoPostServlet();
    	GeneradorPie filtroPie = new GeneradorPie();
    	CadenaFiltrosEnlazada cadenaFiltros = new CadenaFiltrosEnlazada();
    	cadenaFiltros.addFilter(filtroValidaEstadoSistema);
    	cadenaFiltros.addFilter(filtroLog);
    	cadenaFiltros.addFilter(filtroIdioma);
    	cadenaFiltros.addFilter(filtroPdf);
    	cadenaFiltros.addFilter(filtroContenido);
    	cadenaFiltros.addFilter(filtroCabecera);
    	cadenaFiltros.addFilter(filtroCuerpoPre);
    	cadenaFiltros.addFilter(filtroCuerpoPost);
    	cadenaFiltros.addFilter(filtroPie);
    	cadenaFiltros.doFilter(peticion, respuesta);

	    assertNull("no redirige", respuesta.getSendRedirect());
	    final int numeroJspPostServlet = 2;
	    assertEquals("tiene 2 jsp", numeroJspPostServlet, peticion.getUVDatos().getFicherosJSP().size());
	    VistaUVirtual vista = (VistaUVirtual) peticion.getUVDatos().getVistas().get(VistaUVirtual.class.getName());
	    assertNotNull("tiene miga de pan", vista.getMigaDePan());
	    assertNotNull("tiene menu hijos", vista.getMenusHijos());
	    assertNotNull("tiene menu mismo nivel", vista.getMenusMismoNivel());
	}

	/** prueba filtro GeneradorCuerpoPostServlet.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testA10ValidaAutenticacion() throws IOException, ServletException {
		PeticionHttp peticion = new PeticionHttp();
    	peticion.setRequestURI("/pub/es/index");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	ValidaAutenticacion filtroAutenticacion = new ValidaAutenticacion();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();
    	ConfiguracionFiltro fConfig = new ConfiguracionFiltro();
    	fConfig.setInitParameter("ClaveSesionValida", "esValidaLaSesion");
    	fConfig.setInitParameter("UrlDeEntrada", "urlDeEntrada");
    	filtroAutenticacion.init(fConfig);
    	filtroAutenticacion.doFilter(peticion, respuesta, cadenaFiltros);
    	
	    assertEquals("redirige a autenticacion", ConfiguracionGlobal.getSamlUrlAutenticacion(), respuesta.getSendRedirect());
	}

	/** prueba filtro valida acceso a una url que no existe.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testE01UrlNoExiste() throws IOException, ServletException {
    	PeticionHttp peticion = UtilsTestDocentia.peticionAutenticada();
    	peticion.setRequestURI("/srv/es/informacionadministrativa/docentia/convocatoriacrudXXXXMAL");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

	    ValidaAcceso validaAcceso = new ValidaAcceso();
	    validaAcceso.doFilter(peticion, respuesta, cadenaFiltros);
	    assertEquals("redirige a error", ConfiguracionGlobal.getParametroCadenaNE("administracion.urlerror"), respuesta.getSendRedirect());
	}
	
	/** prueba filtro ValidaIdioma invalido.
	 * @throws IOException si error io
	 * @throws ServletException si error servlet
	 */
	@Test
	public void testE02IdiomaInvalido() throws IOException, ServletException {
    	PeticionHttp peticion = new PeticionHttp();
    	peticion.setRequestURI("/pub/kk/index");
    	RespuestaHttp respuesta = new RespuestaHttp();
    	CadenaFiltros cadenaFiltros = new CadenaFiltros();

    	ValidaIdioma filtro = new ValidaIdioma();
    	filtro.doFilter(peticion, respuesta, cadenaFiltros);
	    assertNotNull("redirige a error", respuesta.getSendRedirect());
	}
}