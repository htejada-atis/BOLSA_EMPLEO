package es.ujaen.uvirtual.utilidades;

import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Uvexception.
 * Clase para gestionar las excepciones en Universidad Virtual. Incluye muchas mejoras respecto a la versión antigua (UvirtualException), como son:
 *   1) Distingue entre el mensaje de error que se muestra al usuario y la descripción precisa del error para loguear.
 *   2) Añade al log del error un número aleatorio único (código de incidencia) para localizar más fácilmente el error. 
 *   	Este código puede mostrarse si así lo deseamos al usuario, junto con la IP del servidor donde se ha producido el error.
 *   3) Hace uso de los JSP genéricos para mostrar avisos y errores, ahorrando este trabajo a la hora de programar:
 *      AVISOS: "/WEB-INF/jsp/vista/aviso.jsp" -> Muestra una página con el mensaje para el usuario almacenado en UVException
 *      ERRORES: "/WEB-INF/jsp/vista/error.jsp" -> Muestra una página con el mensaje para el usuario, 
 *      y un botón que muestra toda la información de detalle de la excepción (incluyendo el código de incidencia)
 * 
 * @author icasanas
 *
 */
@SuppressWarnings("java:S1165")
public class UVException extends Exception {
	private static final long serialVersionUID = 7078672844238458607L;
	private static final String NOMBRE_ESTA_CLASE = UVException.class.getSimpleName();
	private static final Logger ELOGGER = Logger.getLogger(NOMBRE_ESTA_CLASE);
	
	private Random rand = new Random();  
	
	// URLs de los JSP genéricos para mostrar un mensaje de aviso (sólo mensaje al usuario) o un mensaje de error (incluye información adicional sobre el error).
	public static final String URL_JSP_AVISO = "/WEB-INF/jsp/vista/aviso.jsp";
	public static final String URL_JSP_ERROR = "/WEB-INF/jsp/vista/error.jsp";
	
	private static String cadenaAvisarInformatica = "Contacte con el Servicio de Informática y comuníquele esta incidencia.";
	private static String mensajeUsuarioGenerico = "Se ha producido un error en la aplicación.";
	
	private String mensajeUsuario = null;
	private boolean avisarInformatica = true;
	private Level logLevel = null;
	private String nombreClase = null;
	private String nombreMetodo = null;
	private String error = null;
	private String codigoIncidencia = null;
	
	
	/**
	 * Genera una excepción con un mensaje para el usuario.
	 * Con este tipo de llamada no se loguea el error, por lo que debe utilizarse
	 * cuando es un error "controlado" (por ejemplo, una comprobación previa en base
	 * de datos antes de insertar un registro que encuentra que ya hay un duplicado)
	 * 
	 * @param pmensajeUsuario -> Mensaje que se mostrará al usuario por pantalla
	 */
	public UVException(String pmensajeUsuario) {
		super(pmensajeUsuario);
		cargaValores(pmensajeUsuario, false, null, null, null, null);
	}
	
	/**
	 * Genera una excepción con un mensaje para el usuario.
	 * Con este tipo de llamada no se loguea el error, 
	 * por lo que debe utilizarse cuando es un error "controlado" 
	 * (por ejemplo, si se detecta que hay que hacer una configuración previa de la aplicación)
	 * 
	 * @param pmensajeUsuario -> Mensaje que se mostrará al usuario por pantalla
	 * @param pavisarInformatica -> Indica que se añada después del mensaje una cadena indicando que se contacte con el Servicio de Informática para avisarle del error
	 */
	public UVException(String pmensajeUsuario, boolean pavisarInformatica) {
		super(pmensajeUsuario);
		cargaValores(pmensajeUsuario, pavisarInformatica, null, null, null, null);
	}
	
	/**
	 * Genera una excepción con un mensaje de error genérico para el usuario, logueando además el error para que podamos detectarlo y corregirlo.
	 * 
	 * @param plogLevel -> Nivel de severidad del error
	 * @param pnombreClase -> Nombre de la clase donde se ha generado el error
	 * @param pnombreMetodo -> Nombre del método donde se ha generado del error
	 * @param perror -> Descripción del error (sólo para el fichero de log). 
	 * 		  Puede ser introducida por nosotros manualmente o bien obtenida a través del método getMessage() de la excepción.
	 */
	public UVException(Level plogLevel, String pnombreClase, String pnombreMetodo, String perror) {
		super(perror);
		cargaValores(mensajeUsuarioGenerico, true, plogLevel, pnombreClase, pnombreMetodo, perror);
	}
	
	/**
	 * Genera una excepción con un mensaje para el usuario, logueando además el error para que podamos detectarlo y corregirlo.
	 * 
	 * @param plogLevel -> Nivel de severidad del error
	 * @param pnombreClase -> Nombre de la clase donde se ha generado el error
	 * @param pnombreMetodo -> Nombre del método donde se ha generado del error
	 * @param perror -> Descripción del error (sólo para el fichero de log). 
	 *        Puede ser introducida por nosotros manualmente o bien obtenida a través del método getMessage() de la excepción.
	 * @param pmensajeUsuario -> Mensaje que se mostrará al usuario por pantalla
	 */
	public UVException(Level plogLevel, String pnombreClase, String pnombreMetodo, String perror, String pmensajeUsuario) {
		super(perror);
		cargaValores(pmensajeUsuario, true, plogLevel, pnombreClase, pnombreMetodo, perror);
	}
	
	/**
	 * Genera una excepción con un mensaje para el usuario, logueando además el error para que podamos detectarlo y corregirlo.
	 * 
	 * @param plogLevel -> Nivel de severidad del error
	 * @param pnombreClase -> Nombre de la clase donde se ha generado el error
	 * @param pnombreMetodo -> Nombre del método donde se ha generado del error
	 * @param perror -> Descripción del error (sólo para el fichero de log). 
	 *        Puede ser introducida por nosotros manualmente o bien obtenida a través del método getMessage() de la excepción.
	 * @param pmensajeUsuario -> Mensaje que se mostrará al usuario por pantalla
	 * @param pavisarInformatica -> Indica que se añada después del mensaje una cadena indicando que se contacte con el Servicio de Informática para avisarle del error
	 */
	public UVException(Level plogLevel, String pnombreClase, String pnombreMetodo, String perror, String pmensajeUsuario, boolean pavisarInformatica) {
		super(perror);
		cargaValores(pmensajeUsuario, pavisarInformatica, plogLevel, pnombreClase, pnombreMetodo, perror);
	}
	
	
	/**
	 * Método privado al que llaman todos los métodos públicos de UVException para cargar los valores de la clase.
	 * 
	 * @param plogLevel -> Nivel de severidad del error
	 * @param pnombreClase -> Nombre de la clase donde se ha generado el error
	 * @param pnombreMetodo -> Nombre del método donde se ha generado del error
	 * @param perror -> Descripción del error (sólo para el fichero de log). 
	 *        Puede ser introducida por nosotros manualmente o bien obtenida a través del método getMessage() de la excepción.
	 * @param pmensajeUsuario -> Mensaje que se mostrará al usuario por pantalla
	 * @param pavisarInformatica -> Indica que se añada después del mensaje una cadena indicando que se contacte con el Servicio de Informática para avisarle del error
	 */
	private void cargaValores(String pmensajeUsuario, boolean pavisarInformatica, Level plogLevel, String pnombreClase, String pnombreMetodo, String perror) {
		this.mensajeUsuario = pmensajeUsuario;
		this.avisarInformatica = pavisarInformatica;
		this.logLevel = plogLevel;
		this.nombreClase = pnombreClase;
		this.nombreMetodo = pnombreMetodo;
		this.error = perror;
		this.codigoIncidencia = null;
		String servidor = null;
		
		// Obtenemos el servidor al que estamos conectados
		try {
			String ipServidor = java.net.InetAddress.getLocalHost().getHostAddress();
			servidor = "srv-" + ipServidor.substring(ipServidor.lastIndexOf(".") + 1);
		} catch (UnknownHostException e) {
			servidor = "srv-xxx";
		}
		
		// Si no se especifica un mensaje para el usuario, ponemos el mensaje de error por defecto
		if (mensajeUsuario == null) {
			this.mensajeUsuario = mensajeUsuarioGenerico;
		}
		
		if (nombreClase == null) {
			this.nombreClase = "[Clase no especificada]";
		}
		
		if (nombreMetodo == null) {
			this.nombreMetodo = "[Método no especificado]";
		}
		
		// Comprobamos si hemos de añadir al final la recomendación de comunicar al Servicio de Informática el error
		if (avisarInformatica) {
			if (".".equals(this.mensajeUsuario.substring(this.mensajeUsuario.length() - 1))) {
				this.mensajeUsuario += " " + cadenaAvisarInformatica;
			} else {
				this.mensajeUsuario += ". " + cadenaAvisarInformatica;
			}
		}
		// Sólo se loguea el error si está definido el logLevel
		if (logLevel != null) {
			this.codigoIncidencia = servidor + ":" + rand.nextInt();
			this.error = "[" + this.codigoIncidencia + "] " + this.error;
			ELOGGER.logp(this.logLevel, this.nombreClase, this.nombreMetodo, this.error);
		}
	}
	
	public boolean isAvisarInformatica() {
		return avisarInformatica;
	}

	/** get mensaje usuario.
	 * @param mostrarCodigoIncidencia mostrar codigo incidencia
	 * @return mensaje usuario
	 */
	public String getMensajeUsuario(boolean mostrarCodigoIncidencia) {
		if (codigoIncidencia == null || !mostrarCodigoIncidencia) {
			return mensajeUsuario;
		} else {
			return mensajeUsuario + " (Código de error: " + codigoIncidencia + ")";
		}
	}
	
	/** get mensaje usuario.
	 * @return mensaje usuario
	 */
	public String getMensajeUsuario() {
		if (codigoIncidencia == null) {
			return mensajeUsuario;
		} else {
			return mensajeUsuario + " (Código de error: " + codigoIncidencia + ")";
		}
	}
	
	/** get descripcion excepcion.
	 * @return descripcion excepcion
	 */
	public String getDescripcionExcepcion() {
		String salida = "";
		if (error == null) {
			salida = "";
		} else if (nombreClase != null && nombreMetodo != null) {
			salida = nombreClase + "." + nombreMetodo + " -> " + error;
		} else {
			salida = error;
		}
		return salida;
	}
	
	public String getCodigoIncidencia() {
		return codigoIncidencia;
	}
	
	public Level getLogLevel() {
		return logLevel;
	}
}
