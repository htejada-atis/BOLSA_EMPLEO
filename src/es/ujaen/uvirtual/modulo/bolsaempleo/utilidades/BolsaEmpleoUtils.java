package es.ujaen.uvirtual.modulo.bolsaempleo.utilidades;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Métodos para la bolsa de empleo.
 * 
 * @author ATISoluciones
 */
public final class BolsaEmpleoUtils {
	private static final String MENSAJE_REDIRECT_SESSION_EXITO = "BEP_MENSAJE_REDIRECT_SESSION_EXITO";
	private static final String MENSAJE_REDIRECT_SESSION_ERROR = "BEP_MENSAJE_REDIRECT_SESSION_ERROR";
	private static final int TIPO_MENSAJE_ERROR = 0;
	private static final int TIPO_MENSAJE_EXITO = 1;
	private static final double ROUNDER = 100.0;
	private static final int HOURS_END = 23;
	private static final int MINUTES_SECONDS_END = 59;
	private static final int NUMBER_1024 = 1024;
	private static final int NUMBER_1000 = 1000;
	private static final int NUMBER_999_950 = 999_950;
	
	private static final String NOMBREDEESTACLASE = BolsaEmpleoUtils.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	private BolsaEmpleoUtils() {
	}
		
	/**
	 * Devuelve un string de un número '?' separadas por ',' para usarlo en
	 * consultas de tipo where in. Por ejemplo si numParams, devuelve "?,?,?"
	 * 
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
	 * Devuelve el valor de un input del request o un valor por defecto si no
	 * existe.
	 * 
	 * @param request      .
	 * @param param        .
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
	 * 
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
	 * 
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
	 * Trata de convertir a Float el valor recibido como parámetro. Si la conversión
	 * falla, devuelve null, de modo que nunca salte una excepción
	 * 
	 * @param valor valor
	 * @return El valor convertido a float, o null si no es posible
	 */
	public static Double leeParametroDouble(String valor) {
		try {
			String[] valueSplitted = valor.split(",");
			if (valueSplitted.length > 1) {
				valor = valor.replace(',', '.');
			}
			return Double.parseDouble(valor);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Devuelve el valor del parametro solicitado .
	 * 
	 * @param nombre .
	 * @return El valor .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public static String getParametroConfiguracion(String nombre) throws SQLException, UVException {
		ModeloParametrosConfiguracion modeloParam = ModeloParametrosConfiguracion.obtenerInstancia();
		return modeloParam.getParametroByNombre(nombre).getValor();
	}

	/**
	 * Devuelve el contenido en string utf8 del inputstream pasado.
	 * https://www.baeldung.com/convert-input-stream-to-string
	 * 
	 * @param f .
	 * @return .
	 */
	public static String inputStreamToString(InputStream f) {
		return new BufferedReader(new InputStreamReader(f, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
	}

	/**
	 * Checkea si un archivo es mas grande que una variable o no .
	 * 
	 * @param archivo .
	 * @return file .
	 * @throws SQLException .
	 * @throws IOException  .
	 * @throws UVException  .
	 */
	public static InputStream checkFileSize(InputStream archivo) throws UVException, SQLException, IOException {
		try (ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
			int bytesLeidos = 0;
			byte[] bytes = new byte[NUMBER_1024];
			long bytesTotal = 0;
			
			Integer maxSize = Formateador.leeParametroInteger(getParametroConfiguracion("bolsaempleo.maxEspacioArchivo"));
			
			while ((bytesLeidos = archivo.read(bytes)) != -1) {
				salida.write(bytes, 0, bytesLeidos);
				bytesTotal += bytesLeidos;
				if (bytesTotal > maxSize) {
					throw new UVException("No se puede insertar un archivo tan grande. Máximo: " + humanReadableByteCountSI(maxSize));
				}
			}
			
			if (bytesTotal == 0) {
				throw new UVException("No se puede insertar sin archivo o archivo vacio");
			}
			
			return new ByteArrayInputStream(salida.toByteArray());
		}
	}
	
	/**
	 * Valor de bytes para pintarlo humanamente.
	 * https://programming.guide/java/formatting-byte-size-to-human-readable-format.html
	 * @param bytes .
	 * @return .
	 */
	public static String humanReadableByteCountSI(long bytes) {
		if (-NUMBER_1000 < bytes && bytes < NUMBER_1000) {
			return bytes + " B";
		}
		CharacterIterator ci = new StringCharacterIterator("kMGTPE");
		while (bytes <= -NUMBER_999_950 || bytes >= NUMBER_999_950) {
			bytes /= NUMBER_1000;
			ci.next();
		}
		return String.format("%.1f %cB", bytes / (NUMBER_1000 * 1.0), ci.current());
	}

	/**
	 * Comprueba si es un fichero pdf válido.
	 * 
	 * @param nombre .
	 * @return .
	 */
	public static boolean checkFileIsPDF(String nombre) {
		return nombre.toLowerCase().endsWith(".pdf");
	}

	/**
	 * Redondeo a dos decimales.
	 * 
	 * @param v .
	 * @return .
	 */
	public static double redondeo(double v) {
		return Math.round(v * ROUNDER) / ROUNDER;
	}

	/**
	 * Devuelve la fecha actual del sistema. Con 00:00:00 
	 * Según "java.time" classes should be used for dates and times (java:S2143)
	 * https://www.baeldung.com/java-date-to-localdate-and-localdatetime
	 * 
	 * @return .
	 */
	public static Date getCurrentDate() {
		LocalDate ld = LocalDate.now();
		return java.sql.Date.valueOf(ld);
	}
	
	/**
	 * Devuelve la fecha actual del sistema. Según "java.time" classes should be
	 * used for dates and times (java:S2143)
	 * https://www.baeldung.com/java-date-to-localdate-and-localdatetime
	 * 
	 * @return .
	 */
	public static Date getCurrentDateTime() {
		LocalDateTime ld = LocalDateTime.now();
		return java.sql.Timestamp.valueOf(ld);
	}
	
	/**
	 * Establece la fecha y hora de la fecha a las 23:59:59.
	 * https://www.baeldung.com/java-date-to-localdate-and-localdatetime
	 * @param date .
	 * @return .
	 */
	public static Date setDateTimeAtEndOfDay(Date date) {
		LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalTime currentTime = LocalTime.of(HOURS_END, MINUTES_SECONDS_END, MINUTES_SECONDS_END);
		LocalDateTime fromDateAndTime = LocalDateTime.of(currentDate, currentTime);
		return java.sql.Timestamp.valueOf(fromDateAndTime);
	}
	
	/**
	 * Convierte un clob a string.
	 * https://www.tutorialspoint.com/how-to-convert-a-clob-type-to-string-in-java
	 * @param clob .
	 * @return .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	public static String clobToString(Clob clob) throws IOException, SQLException {
		try (Reader r = clob.getCharacterStream()) {
			StringBuilder buffer = new StringBuilder();
			int ch;
			while ((ch = r.read()) != -1) {
				buffer.append("" + (char) ch);
			}			
			return buffer.toString();
		}
	}
		
	/**
	 * Convierte string to clob.
	 * https://stackoverflow.com/questions/4687696/string-to-clob-in-java
	 * @param s .
	 * @param conexion .
	 * @return .
	 * @throws SQLException .
	 */
	public static Clob stringToClob(String s, Connection conexion) throws SQLException {
		Clob clob = conexion.createClob();
		clob.setString(1, s);		
		return clob;
	}
	
	/**
	 * Añade un mensaje de exito al bean y en la sessión, para luego su lectura.
	 * 
	 * @param mensaje .
	 * @param bean    .
	 * @param request .
	 * @throws UVException .
	 */
	public static void addMensajeDeExito(String mensaje, Vista bean, HttpServletRequest request) throws UVException {
		BolsaEmpleoUtils.addMensajeSession(mensaje, bean, request, TIPO_MENSAJE_EXITO);
	}

	/**
	 * Añade un mensaje de exito al bean y en la sessión, para luego su lectura.
	 * 
	 * @param mensaje .
	 * @param bean    .
	 * @param request .
	 * @throws UVException .
	 */
	public static void addMensajeDeError(String mensaje, Vista bean, HttpServletRequest request) throws UVException {
		BolsaEmpleoUtils.addMensajeSession(mensaje, bean, request, TIPO_MENSAJE_ERROR);
	}

	/**
	 * Comprueba si hay mensajes en la sessión y los mete en el bean.
	 * 
	 * @param bean    .
	 * @param request .
	 */
	public static void readMensajeSession(Vista bean, HttpServletRequest request) {
		LOGGER.log(Level.FINER, "Leyendo mensajes sessión");
		
		HttpSession session = request.getSession(false);

		String mensajeExito = (String) session.getAttribute(MENSAJE_REDIRECT_SESSION_EXITO);
		
		LOGGER.log(Level.FINER, String.format("MensajeExito [%s]", mensajeExito));
		
		if (mensajeExito != null) {
			String[] mensajeExitoSplit = mensajeExito.split(Pattern.quote("|"));
			for (String m : mensajeExitoSplit) {
				bean.getMensajesDeExito().add(m);
			}
			session.removeAttribute(MENSAJE_REDIRECT_SESSION_EXITO);
		}

		String mensajeError = (String) session.getAttribute(MENSAJE_REDIRECT_SESSION_ERROR);
		
		LOGGER.log(Level.FINER, String.format("MensajeError [%s]", mensajeError));
		
		if (mensajeError != null) {
			String[] mensajeErrorSplit = mensajeError.split(Pattern.quote("|"));
			for (String m : mensajeErrorSplit) {
				bean.getMensajesDeError().add(m);
			}
			session.removeAttribute(MENSAJE_REDIRECT_SESSION_ERROR);
		}
		
		LOGGER.log(Level.FINER, "Fin leyendo mensajes sessión");
	}

	/**
	 * Redirecciona a la url actual con parametros en la sessión.
	 * 
	 * @param request  .
	 * @param response .
	 * @param params   .
	 * @throws IOException .
	 */
	public static void redirectWithParams(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> params) throws IOException {
		HttpSession session = request.getSession(false);

		for (Map.Entry<String, String> entry : params.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}

		response.sendRedirect(request.getServletPath());
	}

	/**
	 * Devuelve un parámetro guardado en sessión y si no, leemos lo que viene del
	 * request.
	 * 
	 * @param request .
	 * @param param   .
	 * @return .
	 */
	public static String getParamRequestOrSession(HttpServletRequest request, String param) {
		HttpSession session = request.getSession(false);
		String valueSession = (String) session.getAttribute(param);
		session.removeAttribute(param);

		if (valueSession == null || valueSession.isBlank()) {
			return request.getParameter(param);
		}

		return valueSession;
	}

	private static void addMensajeSession(String mensaje, Vista bean, HttpServletRequest request, int tipo) throws UVException {
		LOGGER.log(Level.FINER, String.format("Añadiendo mensajes de sessión [%d][%s]", tipo, mensaje));
		
		HttpSession session = request.getSession(false);

		switch (tipo) {
			case TIPO_MENSAJE_EXITO:
				String mensajeExito = (String) session.getAttribute(MENSAJE_REDIRECT_SESSION_EXITO);
				if (mensajeExito != null) {
					mensajeExito += "|" + mensaje;
				} else {
					mensajeExito = mensaje;
				}
				LOGGER.log(Level.FINER, String.format("Session set atribute [%s]", mensajeExito));
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
				LOGGER.log(Level.FINER, String.format("Session set atribute [%s]", mensajeError));
				session.setAttribute(MENSAJE_REDIRECT_SESSION_ERROR, mensajeError);
				bean.getMensajesDeError().add(mensaje);
				break;
			default:
				throw new UVException("Tipo de mensaje no válido");
		}
	}
}
