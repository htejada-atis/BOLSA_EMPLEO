package es.ujaen.uvirtual.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.uvirtual.modelo.ModeloAdministracion;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Datos de configuración global.
 * @author julopez
 */
public class ConfiguracionGlobal implements Serializable {
	private static final long serialVersionUID = -5180419998742984696L;
	private static Logger logger = Logger.getLogger(ConfiguracionGlobal.class.getName());
	private static String nombreDeEstaClase = ConfiguracionGlobal.class.getName();

	private static final String LECTURA_PARAMETRO_NO_EXISTENTE = "Lectura de un parámetro no existente ";
	private static boolean cargadoDatos = false;
	private static boolean leerConfiguracion = true;
	private static long ultimaLectura = 0; 
	
	@SuppressWarnings("checkstyle:magicnumber")
	private static long periodoLectura = 30L * 60L * 1000L; // 30 minutos
	private static String configuracion = "default"; // configuración por defecto
	private static Map<String, String> datos = null;
	
	private ConfiguracionGlobal() { }
	
	/** Lanza excepcion parametro no existe.
	 * @param metodo metodo que lanza la excepcion
	 * @param parametro nombre del parametro
	 * @throws UVException siempre lanza esta excepcion
	 */
	private static void lanzaExcepcionParametroNoExiste(String metodo, String parametro) throws UVException {
		logger.logp(Level.SEVERE, nombreDeEstaClase, metodo, LECTURA_PARAMETRO_NO_EXISTENTE + parametro);
		throw new UVException("Parámetro '" + parametro + "' no existente");
	}
	
	/**
	 * Lee un parámetro de la configuración como un valor booleano.
	 * @param parametro parametro a leer
	 * @return el valor en modo booleano (debe valer "S" en base de datos para True, cualquier otro valor devuelve False)
	 * @throws UVException si no existe la clave 
	 */
	public static boolean getParametroLogico(String parametro) throws UVException {
		init();
		if (datos != null && datos.get(parametro) != null) { 
			return "S".equals(datos.get(parametro));
		} 
		lanzaExcepcionParametroNoExiste("getParametroLogico", parametro);
		return false;
	}
	
	/**
	 * Lee un parámetro de la configuración como un valor entero.
	 * @param parametro parametro
	 * @return el valor en modo entero
	 * @throws UVException si no existe la clave o no se puede convertir a entero 
	 */
	public static int getParametroEntero(String parametro) throws UVException {
		init();
		if (datos != null && datos.get(parametro) != null) {
			return Integer.parseInt(datos.get(parametro));
		}
		lanzaExcepcionParametroNoExiste("getParametroEntero", parametro);
		return 0;
	}

	/**
	 * Lee un parámetro de la configuración como un valor entero.
	 * @param parametro parametro
	 * @return el valor en modo entero
	 */
	public static Integer getParametroEnteroNE(String parametro) {
		init();
		if (datos != null && datos.get(parametro) != null) {
			return Integer.parseInt(datos.get(parametro));
		}
		return null;
	}
	
	/**
	 * Lee un parámetro de la configuración como un valor entero largo.
	 * @param parametro parametro
	 * @return el valor en modo entero largo
	 * @throws UVException si no existe la clave o no se puede convertir a entero largo 
	 */
	public static long getParametroEnteroLargo(String parametro) throws UVException {
		init();
		if (datos != null && datos.get(parametro) != null) {
			return Long.parseLong(datos.get(parametro));
		}
		lanzaExcepcionParametroNoExiste("getParametroEnteroLargo", parametro);
		return 0;
	}

	/**
	 * Lee un parámetro de la configuración como un valor entero largo.
	 * @param parametro parametro
	 * @return el valor en modo entero largo
	 */
	public static Long getParametroEnteroLargoNE(String parametro) {
		init();
		if (datos != null && datos.get(parametro) != null) {
			return Long.parseLong(datos.get(parametro));
		}
		return null;
	}
	
	/**
	 * Lee un parámetro de la configuración como un valor numérico decimal.
	 * @param parametro parametro
	 * @return el valor en modo numérico decimal
	 * @throws UVException si no existe la clave o no se puede convertir a numérico decimal 
	 */
	public static float getParametroNumeroDecimal(String parametro) throws UVException {
		init();
		if (datos != null && datos.get(parametro) != null) {
			return Float.parseFloat(datos.get(parametro));
		}
		lanzaExcepcionParametroNoExiste("getParametroNumeroDecimal", parametro);
		return 0;
	}

	/**
	 * Lee un parámetro de la configuración como un valor de tipo cadena.
	 * @param parametro parametro
	 * @return el valor en modo cadena
	 * @throws UVException si no existe la clave 
	 */
	public static String getParametroCadena(String parametro) throws UVException {
		init(); 
		if (datos != null && datos.get(parametro) != null) { 
			return datos.get(parametro);
		}
		lanzaExcepcionParametroNoExiste("getParametroCadena", parametro);
		return null;
	}

	/**
	 * Lee un parámetro de la configuración como un valor de tipo cadena.
	 * @param parametro parametro
	 * @return el valor en modo cadena
	 */
	public static String getParametroCadenaNE(String parametro) {
		init();
		if (datos != null && datos.get(parametro) != null) {
			return datos.get(parametro);
		}
		return null;
	}

	/** cargar datos.
	 * @param nombreDeLaConfiguracion nombre configuracion
	 */
	public static void cargarDatos(String nombreDeLaConfiguracion) {
		configuracion = nombreDeLaConfiguracion;
		leerConfiguracion = false;
		cargarDatos(true);
		leerConfiguracion = true;
	}
	
	public static boolean isDatosCargados() {
		return datos != null;
	}
	
	/** cargar datos.
	 * @param forzarCarga forzar carga
	 */
	public static void cargarDatos(boolean forzarCarga) {
		if (forzarCarga) {
			cargadoDatos = false;
		}
		init();
	}
	
	/** init.
	 */
	@SuppressWarnings("checkstyle:CyclomaticComplexity")
	private static synchronized void init() {
		if (cargadoDatos && ((new java.util.Date()).getTime() - ultimaLectura) > periodoLectura) {
			cargadoDatos = false;
		}
		if (!cargadoDatos) {
    		logger.logp(Level.FINE, nombreDeEstaClase, "init", "Cargando parámetros del fichero configuracion.properties");
			ModeloAdministracion modelo = ModeloAdministracion.obtenerInstancia();
			
			if (leerConfiguracion) {
				configuracion = "desarrollo";
			}

			try {
				datos = modelo.listaConfiguracion(configuracion);

				if (datos == null) {
					throw new UVException("error al listar configuracion");
				}
				// Cuando volvemos a leer los datos?
				if (datos.get("administracion.refrescoconfiguracion") != null) {
					periodoLectura = Long.parseLong(datos.get("administracion.refrescoconfiguracion"));
				}
				
				// Para evitar poner dobles barras
				String nombreParametroUrlBase = "administracion.urlbase"; 
				String urlBase = datos.get(nombreParametroUrlBase);
				if (urlBase != null && urlBase.endsWith("/")) {
					datos.put(nombreParametroUrlBase, urlBase.substring(0, urlBase.length() - 1));
				}
				ultimaLectura = (new java.util.Date()).getTime();
				cargadoDatos = true;
			} catch (Exception e) {
	    		logger.logp(Level.SEVERE, nombreDeEstaClase, "init", "No se pudieron cargar los datos de la configuración de la base de datos");
	    		logger.logp(Level.SEVERE, nombreDeEstaClase, "init", Formateador.getStackTrace(e));
			}
		}
	}
	
	/** get paramatro cadena sin excepcion.
	 * @param cadena nombre del parametro
	 * @return valor del parametro
	 */
	public static String getParametroCadenaSinExcepcion(String cadena) {
		try { 
			return getParametroCadena(cadena); 
		} catch (UVException e) {
			return null;
		}
	}

	public static String getServletFotosEstudiante() {
		return getParametroCadenaSinExcepcion("administracion.servletfotosestudiante"); 
	}

	/** getNivelesFotosEstudiante.
	 * @return nivelesFotosEstudiante
	 */
	public static int getNivelesFotosEstudiante() {
		try { 
			return getParametroEntero("administracion.nivelesfotosestudiante"); 
		} catch (UVException e) {
			return 0;
		} 
	}

	public static String getFotoUsuarioNoExiste() {
		return getParametroCadenaSinExcepcion("administracion.fotousuarionoexiste");
	}

	public static String getDirectorioFotosEstudiantes() {
		return getParametroCadenaSinExcepcion("administracion.directoriofotosestudiantes");
	}

	public static String getAtributoPDF() {
		return getParametroCadenaSinExcepcion("administracion.atributopdf");
	}

	/** get validez urls.
	 * @return valides urls
	 */
	public static long getValidezUrls() {
		try { 
			return getParametroEnteroLargo("administracion.validezUrls"); 
		} catch (UVException e) {
			return 0;
		} 
	}

	public static String getUrlVolverDeUsuario() {
		return getParametroCadenaSinExcepcion("administracion.urlvolverdeusuario");
	}

	public static String getAtributoUsuarioReal() {
		return getParametroCadenaSinExcepcion("administracion.atributousuarioreal"); 
	}

	public static String getUrlRaiz() {
		return getParametroCadenaSinExcepcion("administracion.urlraiz");
	}

	/** getCodigoMenuRaiz.
	 * @return codigoMenuRaiz
	 */
	public static int getCodigoMenuRaiz() {
		try {
			return getParametroEntero("administracion.codigomenuraiz"); 
		} catch (UVException e) {
			return 0;
		} 
	}

	public static String getAtributoUsuario() {
		return getParametroCadenaSinExcepcion("administracion.atributousuario"); 
	}

	public static String getUrlError() {
		return getParametroCadenaSinExcepcion("administracion.urlerror"); 
	}

	public static String getAtributoError() {
		return getParametroCadenaSinExcepcion("administracion.atributoerror"); 
	}

	public static String getRolAdministradorPortal() {
		return getParametroCadenaSinExcepcion("administracion.roladministradores"); 
	}
	
	public static String getRolDesarrolloPortal() {
		return getParametroCadenaSinExcepcion("administracion.roldesarrollo"); 
	}
	
	public static String getAtributoVistas() {
		return getParametroCadenaSinExcepcion("administracion.atributovistas");
	}

	public static String getAtributoJavascript() {
		return getParametroCadenaSinExcepcion("administracion.atributojavascript"); 
	}

	public static String getAtributoCSS() {
		return getParametroCadenaSinExcepcion("administracion.atributocss");
	}

	@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
	public static String getAtributoXSLFO() {
		return getParametroCadenaSinExcepcion("administracion.atributoxsl"); 
	}

	public static String getAtributoRespuestaEnviada() {
		return getParametroCadenaSinExcepcion("administracion.respuestaenviada");
	}

	public static String getAtributoContentType() {
		return getParametroCadenaSinExcepcion("administracion.atributocontenttype");
	}

	public static String getMemcacheNamespace() {
		return getParametroCadenaSinExcepcion("memcache.namespace"); 
	}

	/** getMemcachePoolsize.
	 * @return memcachePoolsize
	 */
	public static int getMemcachePoolsize() {
		try {
			return getParametroEntero("memcache.poolsize"); 
		} catch (UVException e) {
			return 0;
		}
	}
	
	/** getMemcacheServers.
	 * @return memcacheServers
	 */
	public static String getMemcacheServers() {
		String memcacheUrl = System.getProperty("memcacheUrl"); 
		return memcacheUrl + ":11211";
	}
	
	/** getMemcacheTTL.
	 * @return memcacheTTL
	 */
	public static int getMemcacheTTL() {
		try {
			return getParametroEntero("memcache.ttl"); 
		} catch (UVException e) {
			return 0;
		} 
	}
	
	/** is memcahce enabled.
	 * @return memcache enabled
	 */
	public static boolean isMemcacheEnabled() {
		try {
			return getParametroLogico("memcache.enabled"); 
		} catch (UVException e) {
			return false;
		} 
	}

	/** set memcache enabled.
	 * @param enabled enabled
	 */
	public static void setMemcacheEnabled(boolean enabled) {
		if (datos != null) {
			datos.put("memcache.enabled", enabled ? "S" : "N");
		}
	}
	
	/** getAyudaUrlPosicionInicial.
	 * @return ayudaUrlPosicionInicial
	 */
	public static int getAyudaUrlPosicionInicial() {
		try {
			return getParametroEntero("ayudaurl.posicioninicial"); 
		} catch (UVException e) {
			return 0;
		} 
	}
	
	public static String getAyudaUrlSeparadorParametros() {
		return getParametroCadenaSinExcepcion("ayudaurl.separadorparametros"); 
	}
	
	public static String getAyudaUrlIdiomaPorDefecto() {
		return getParametroCadenaSinExcepcion("ayudaurl.idiomapordefecto"); 
	}
	
	public static String getAyudaUrlControladorPorDefecto() {
		return getParametroCadenaSinExcepcion("ayudaurl.controladorpordefecto"); 
	}
	
	public static String getAyudaUrlPrefijoUrlProtegida() {
		return getParametroCadenaSinExcepcion("ayudaurl.prefijourlprotegida"); 
	}

	public static String getAyudaUrlFormatosSoportados() {
		return getParametroCadenaSinExcepcion("ayudaurl.formatossoportados");
	}

	public static String getAyudaUrlFormatoPorDefecto() {
		return getParametroCadenaSinExcepcion("ayudaurl.formatopordefecto"); 
	}

	public static String getAyudaUrlFormatoHTML() {
		return getParametroCadenaSinExcepcion("ayudaurl.formatohtml"); 
	}

	public static String getAyudaUrlFormatoPDF() {
		return getParametroCadenaSinExcepcion("ayudaurl.formatopdf"); 
	}

	public static String getAyudaUrlFormatoAJAX() {
		return getParametroCadenaSinExcepcion("ayudaurl.formatoajax"); 
	}

	public static String getAyudaUrlFormatoWord() {
		return getParametroCadenaSinExcepcion("ayudaurl.formatoword"); 
	}

	public static String getAyudaUrlFormatoExcel() {
		return getParametroCadenaSinExcepcion("ayudaurl.formatoexcel"); 
	}

	public static String getAyudaUrlFormatoCsv() {
		return getParametroCadenaSinExcepcion("ayudaurl.formatocsv"); 
	}

	public static String getAyudaUrlPrefijoUrlAccesoAnonimo() {
		return getParametroCadenaSinExcepcion("ayudaurl.prefijourlaccesoanonimo"); 
	}

	public static String getAtributoLog() {
		return getParametroCadenaSinExcepcion("administracion.atributolog"); 
	}

	public static String getDirectorioFicherosTemporales() {
		return getParametroCadenaSinExcepcion("administracion.directorioficherostemporales");
	}

	public static String getUrlBase() {
		return getParametroCadenaSinExcepcion("administracion.urlbase"); 
	}

	public static String getSamlUrlAutenticacion() {
		return getParametroCadenaSinExcepcion("saml.idp.urlautenticacion");
	}

	public static String getSamlUrlDestino() {
		return getParametroCadenaSinExcepcion("saml.idp.urldestino"); 
	}

	public static String getAtributoSistemas() {
		return getParametroCadenaSinExcepcion("administracion.atributosistemas"); 
	}

	public static String getUrlSistemaNoDisponible() {
		return getParametroCadenaSinExcepcion("administracion.sistemanodisponible");
	}
	
	public static String getConfiguracion() {
		return configuracion;
	}
}