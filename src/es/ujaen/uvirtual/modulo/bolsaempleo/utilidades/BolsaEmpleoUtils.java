package es.ujaen.uvirtual.modulo.bolsaempleo.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Métodos para la bolsa de empleo.
 * @author jlopez
 *
 */
public final class BolsaEmpleoUtils {
	private static final String MENSAJE_REDIRECT_SESSION_EXITO = "BEP_MENSAJE_REDIRECT_SESSION_EXITO";	
	private static final String MENSAJE_REDIRECT_SESSION_ERROR = "BEP_MENSAJE_REDIRECT_SESSION_ERROR";
	private static final int TIPO_MENSAJE_ERROR = 0;
	private static final int TIPO_MENSAJE_EXITO = 1;	
	private static final double ROUNDER = 100.0;
	private static final int NUMBER_2 = 2;
	
	private BolsaEmpleoUtils() { }
	
	/** Utility method to get file name from HTTP header content-disposition .
	 * @param part archivo del que obtener el nombre .
	 * @return cadena con el nombre del fichero .
     */
	public static String obtenerNombreFichero(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf('=') + NUMBER_2, token.length() - 1);
            }
        }
		return "";
    }
	
	/**
	 * Devuelve un string de un número '?' separadas por ',' para usarlo en consultas
	 * de tipo where in.
	 * Por ejemplo si numParams, devuelve "?,?,?"
	 * @param numParams número de interrograciones
	 * @return string con un número de ? separadas por coma
	 */
	public static String consultaMultiplesParametros(int numParams) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < numParams; i++) {
			builder.append("?,");
		}
		
		return builder.deleteCharAt(builder.length() - 1).toString();
	}
	
	/**
	 * Devuelve el valor de un input del request o un valor por defecto si no existe.
	 * @param request .
	 * @param param .
	 * @param defaultParam .
	 * @return .
	 */
	public static String getParamForm(ServletRequest request, String param, String defaultParam) {
		String value = request.getParameter(param);
		
		if (value != null) {
			return value;
		}
		
		return defaultParam;
	}

	/**
	 * Devuleve si la cadena contiene un valor entero o no.
	 * @param value .
	 * @return .
	 */
	public static boolean isInteger(String value) {
		if (value == null) {
			return false;
		}
		
		String regex = "[0-9]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);

		return m.matches();
	}
	
	/**
	 * Devuleve si la cadena contiene un valor float o no.
	 * @param value .
	 * @return .
	 */
	public static boolean isFloat(String value) {
		if (value == null) {
			return false;
		}
		
		String[] valueSplitted = value.split(",");
		if (valueSplitted.length > 1) {
			value = value.replace(',', '.');
		}
		
		String regex = "[-+]?[0-9]*\\.?[0-9]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);

		return m.matches();
	}
	
	/**
	 * Trata de convertir a Float el valor recibido como parámetro. Si la conversión falla, devuelve null, de modo que nunca salte una excepción
	 * @param valor valor
	 * @return El valor convertido a float, o null si no es posible
	 */
	public static Float leeParametroFloat(String valor) {
		try {
			String[] valueSplitted = valor.split(",");
			if (valueSplitted.length > 1) {
				valor = valor.replace(',', '.');
			}
			return Float.parseFloat(valor);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Devuelve el valor del parametro solicitado .
	 * @param nombre .
	 * @return El valor .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public static String getParametroConfiguracion(String nombre) throws SQLException, UVException {
		ModeloParametrosConfiguracion modeloParam = ModeloParametrosConfiguracion.obtenerInstancia();
		return modeloParam.getParametroByNombre(nombre).getValor();
	}
	
	/**
	 * Checkea si un archivo es mas grande que una variable o no .
	 * @param archivo .
	 * @throws SQLException .
	 * @throws IOException .
	 * @throws UVException .
	 */
	public static void checkFileSize(InputStream archivo) throws UVException, SQLException, IOException {
		if (archivo.available() <= 0) {
			throw new UVException("No se puede insertar sin archivo o archivo vacio");
		}
		
		Integer maxSize = Formateador.leeParametroInteger(getParametroConfiguracion("bolsaempleo.maxEspacioArchivo"));
		if (maxSize == null) {
			throw new UVException("No se puede leer el tamañao máximo de archivo");
		}
		
		if (archivo.available() > maxSize) {
			throw new UVException("No se puede insertar un archivo tan grande");
		}	
	}
	
	/**
	 * Comprueba si es un fichero pdf válido.
	 * @param uploadedFile . 
	 * @return .
	 */
	public static boolean checkFileIsPDF(Part uploadedFile) { 
		if (uploadedFile != null && uploadedFile.getSize() >= 0) {
			String nombre = BolsaEmpleoUtils.obtenerNombreFichero(uploadedFile);
			int i = nombre.lastIndexOf('.');
			if (i > 0) {
				String extension = nombre.substring(i + 1);
				if ("pdf".equalsIgnoreCase(extension)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** Redondeo a dos decimales.
	 * @param v .
	 * @return .
	 */
	public static double redondeo(double v) {
		return Math.round(v * ROUNDER) / ROUNDER;
	}
	
	/**
	 * Devuelve la fecha actual del sistema.
	 * Según "java.time" classes should be used for dates and times (java:S2143)
	 * https://www.baeldung.com/java-date-to-localdate-and-localdatetime
	 * @return .
	 */
	public static Date getCurrentDate() {
		LocalDate ld = LocalDate.now();
		return java.sql.Date.valueOf(ld);
	}
	
	/**
	 * Devuelve la fecha actual del sistema.
	 * Según "java.time" classes should be used for dates and times (java:S2143)
	 * https://www.baeldung.com/java-date-to-localdate-and-localdatetime
	 * @return .
	 */
	public static Date getCurrentDateTime() {
		LocalDateTime ld = LocalDateTime.now();
		return java.sql.Timestamp.valueOf(ld);
	}
	
	/**
	 * Añade un mensaje de exito al bean y en la sessión, para luego su lectura.
	 * @param mensaje .
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 */
	public static void addMensajeDeExito(String mensaje, Vista bean, HttpServletRequest request) throws UVException {
		BolsaEmpleoUtils.addMensajeSession(mensaje, bean, request, TIPO_MENSAJE_EXITO);		
	}
	
	/**
	 * Añade un mensaje de exito al bean y en la sessión, para luego su lectura.
	 * @param mensaje .
	 * @param bean .
	 * @param request .
	 * @throws UVException .
	 */
	public static void addMensajeDeError(String mensaje, Vista bean, HttpServletRequest request) throws UVException {
		BolsaEmpleoUtils.addMensajeSession(mensaje, bean, request, TIPO_MENSAJE_ERROR);		
	}
	
	/**
	 * Comprueba si hay mensajes en la sessión y los mete en el bean.
	 * @param bean .
	 * @param request .
	 */
	public static void readMensajeSession(Vista bean, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		String mensajeExito = (String) session.getAttribute(MENSAJE_REDIRECT_SESSION_EXITO);		
		if (mensajeExito != null) {
			String[] mensajeExitoSplit = mensajeExito.split(Pattern.quote("|"));
			for (String m : mensajeExitoSplit) {
				bean.getMensajesDeExito().add(m);
			}
			session.removeAttribute(MENSAJE_REDIRECT_SESSION_EXITO);
		}
		
		String mensajeError = (String) session.getAttribute(MENSAJE_REDIRECT_SESSION_ERROR);		
		if (mensajeError != null) {
			String[] mensajeErrorSplit = mensajeError.split(Pattern.quote("|"));
			for (String m : mensajeErrorSplit) {
				bean.getMensajesDeError().add(m);
			}
			session.removeAttribute(MENSAJE_REDIRECT_SESSION_ERROR);
		}
	}
	
	private static void addMensajeSession(String mensaje, Vista bean, HttpServletRequest request, int tipo) throws UVException {		
		HttpSession session = request.getSession(false);
		
		switch (tipo) {
			case TIPO_MENSAJE_EXITO:
				String mensajeExito = (String) session.getAttribute(MENSAJE_REDIRECT_SESSION_EXITO);
				if (mensajeExito != null) {
					mensajeExito += "|" + mensaje;
				} else {
					mensajeExito = mensaje;
				}
				session.setAttribute(MENSAJE_REDIRECT_SESSION_EXITO, mensajeExito);
				bean.getMensajesDeExito().add(mensaje);
				break;
			case TIPO_MENSAJE_ERROR:
				String mensajeError = (String) session.getAttribute(MENSAJE_REDIRECT_SESSION_ERROR);
				if (mensajeError != null) {
					mensajeError += "|" + mensaje;
				} else {
					mensajeError = mensaje;
				}
				session.setAttribute(MENSAJE_REDIRECT_SESSION_ERROR, mensajeError); 
				bean.getMensajesDeError().add(mensaje);
				break;
			default:
				throw new UVException("Tipo de mensaje no válido");
		}
	}
}
