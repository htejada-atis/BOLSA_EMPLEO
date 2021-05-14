package es.ujaen.uvirtual.utilidades;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;

/**
 * Contiene diversas funcionalidades para convertir y formatear valores.
 * 
 * @author icasanas
 *  
 */
@SuppressWarnings("checkstyle:ReturnCount")
public class Formateador {
	private static final String NOMBRE_ESTA_CLASE = Formateador.class.getName();
	
	private static final String MENSAJE_ERROR_APLICACION = "Se ha producido un error en la aplicación";
	
	public static final Locale IDIOMA_ESPANIOL = new Locale("es_ES");
	
	public static final String FORMATO_FECHA_SEPARADOR_DIAS = "/";
	
	public static final String FORMATO_FECHA_DDMMYYYY = "DDMMYYYY";
	public static final String FORMATO_FECHA_DDMMYYYY_HHMMSS = "DDMMYYYY_HHMMSS";
	public static final String FORMATO_FECHA_YYYYMMDD = "YYYYMMDD";
	public static final String FORMATO_FECHA_YYYYMMDD_HHMMSS = "YYYYMMDD_HHMMSS";
	public static final String FORMATO_FECHA_DIA_MES = "dd_MMMM";
	public static final String FORMATO_FECHA_DIA_MES_ANIO = "dd_MMMM_yyyy";
	public static final String FORMATO_FECHA_HORA_MINUTOS = "HHMM";
	
	private Formateador() { }
	
	/**
	 * Obtener de una excepción la pila de llamadas como una cadena.
	 * Sacade de org/apache/commons/lang3/exception/ExceptionUtils.java
	 * @param throwable excepcion
	 * @return string con cadena de llamadas
	 */
	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}    

	/**
	 * HashMap meses: devuelve la denominación del mes a partir del número de mes.
	 */
	private static final HashMap<Integer, String> MESES = inicializaMeses();
	
	@SuppressWarnings("checkstyle:magicnumber")
	private static HashMap<Integer, String> inicializaMeses() {
		HashMap<Integer, String> todosMeses = new HashMap<>();
		todosMeses.put(1, "enero");
		todosMeses.put(2, "febrero");
		todosMeses.put(3, "marzo");
		todosMeses.put(4, "abril");
		todosMeses.put(5, "mayo");
		todosMeses.put(6, "junio");
		todosMeses.put(7, "julio");
		todosMeses.put(8, "agosto");
		todosMeses.put(9, "septiembre");
		todosMeses.put(10, "octubre");
		todosMeses.put(11, "noviembre");
		todosMeses.put(12, "diciembre");
		return todosMeses;
	}
	
	/** fija decimales double.
	 * @param valor valor
	 * @param numDecimales numdecimales
	 * @return double con el numero de decimales indicados
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public static Double fijaDecimalesDouble(Double valor, int numDecimales) {
		if (valor == null || numDecimales < 0 || numDecimales > 100) {
			return null;
		}
		
		double multiplicador = Math.pow(10, numDecimales);
		return Double.valueOf((Math.round(valor * multiplicador)) / multiplicador);
	}
	
	/**
	 * Devuelve la cadena recibida como parámetro eliminando los espacios iniciales y finales. Si el parámetro está vacío (null), devuelve una cadena vacía ("")
	 * @param valor El parámetro que estamos leyendo con request.getParameter()
	 * @return El valor del parámetro sin espacios iniciales ni finales, o una cadena vacía.
	 */
	public static String leeParametroString(String valor) {
		if (valor == null) {
			return "";
		} else {
			return EscapaHTML.ajustaCodificacion(valor.trim());
		}
	}
	
	/**
	 * Devuelve la cadena recibida como parámetro eliminando los espacios iniciales y finales. Si el parámetro no existe (null) o está vacío (""), devuelve valorPorDefecto
	 * @param valor -> El parámetro que estamos leyendo con request.getParameter()
	 * @param valorPorDefecto -> Valor por defecto del parámetro
	 * @return El valor del parámetro sin espacios iniciales ni finales, o una cadena vacía.
	 */
	public static String leeParametroString(String valor, String valorPorDefecto) {
		if (valor == null || "".equals(valor)) {
			return valorPorDefecto;
		} else {
			return EscapaHTML.ajustaCodificacion(valor.trim());
		}
	}
	
	
	/**
	 * Trata de convertir a Integer el valor recibido como parámetro. Si la conversión falla, devuelve null, de modo que nunca salte una excepción
	 * @param valor valor
	 * @return El valor convertido a Integer, o null si no es posible
	 */
	public static Integer leeParametroInteger(String valor) {
		try {
			return Integer.parseInt(valor);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Trata de convertir a Double el valor recibido como parámetro. Si la conversión falla, devuelve null, de modo que nunca salte una excepción
	 * @param valor valor
	 * @return El valor convertido a Integer, o null si no es posible
	 */
	public static Double leeParametroDouble(String valor) {
		try {
			return Double.parseDouble(valor);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Recibe un array de parámetros (o sea, el valor de varios campos de un mismo formulario que tienen el mismo nombre) y devuelve el mismo array, 
	 * pero con todos los valores recortados los espacios iniciales y finales, y reemplazando los null por cadenas vacías.
	 * Si el array es null o está vacío, devuelve un String[0]
	 * @param parameterValues paramaterValues
	 * @return array de parametros
	 */
	public static String[] leeParametros(String[] parameterValues) {
		String[] parametrosSinNulos = null;
		
		if (parameterValues == null) {
			parametrosSinNulos = new String[0];
		} else if (parameterValues.length == 0) {
			parametrosSinNulos = new String[0];
		} else {
			int i = 0;
			parametrosSinNulos = new String[parameterValues.length];
			for (String parametro : parameterValues) {
				parametrosSinNulos[i++] = leeParametroString(parametro);
			}
		}
		return parametrosSinNulos;
	}
	
	/**
	 * Trata de convertir a Integer el valor recibido como parámetro. 
	 * Si la conversión falla, devuelve el valor valorPorDefecto que pasemos como parámetro (convertido de int a Integer).
	 * Es muy útil para evitar una excepción del tipo NullPointerException al ejecutar el método CallableStatement.setInt() en el modelo
	 * @param valor valor
	 * @param valorPorDefecto valor por defecto
	 * @return El valor convertido a Integer, o null si no es posible
	 */
	public static Integer leeParametroInteger(String valor, int valorPorDefecto) {
		try {
			return Integer.parseInt(valor);
		} catch (Exception e) {
			return Integer.valueOf(valorPorDefecto);
		}
	}
	
	/** obtiene la fecha en formato yyyyMMdd.
	 * @param valor cadena a analizar para obtener la fecha
	 * @param separador separador de ano mes día
	 * @return cadena en formato yyyyMMdd
	 */
	@SuppressWarnings({"checkstyle:magicnumber"})
	private static String obtenerFechaAnoMesDia(String valor, String separador) {
		String strDia = null;
		String strMes = null;
		String strAnio = null;
		if (valor == null || "".equals(valor)) {
			return null;
		} else {
			if ("".equals(separador)) {
				if (valor.length() != 8) {
					return null;
				}
				strDia = valor.substring(0, 2);
				strMes = valor.substring(2, 4);
				strAnio = valor.substring(4, 8);
			} else {
				String[] campos = valor.split(separador);
				if (campos.length != 3) {
					return null;
				}
				strDia = campos[0].length() == 1 ? "0" + campos[0] : campos[0];
				strMes = campos[1].length() == 1 ? "0" + campos[1] : campos[1];
				strAnio = campos[2];
			}
		}
		
		return strAnio + strMes + strDia;
	}
	
	/**
	 * Trata de convertir a Date el valor recibido como parámetro. Si la conversión falla, devuelve null, de modo que nunca salte una excepción
	 * @param valor -> Cadena con el valor que queremos convertir a Date
	 * @param formato -> Formato utilizado en la cadena para expresar la fecha. Utilizar los valores predefinidos en esta clase (ejemplo: Formateador.FORMATO_FECHA_DDMMYYYY)
	 * @param separador -> Carácter utilizado para separar los días de los meses,
	 *        * y los meses de los años. Se permite sólo la barra "/", el guión "-", o bien dejarlo en blanco "" / null
	 * @return fecha en formato date
	 * @throws UVException si error de formato
	 */
	public static Date leeParametroFecha(String valor, String formato, String separador) throws UVException {
		String eNombreDeEsteMetodo = "leeParametroFecha";
		Date dateFecha = null;
		String separador2 = separador;
		if (separador2 == null) {
			separador2 = "";
		}
		// Comprobaciones previas para asegurarnos que estamos usando la función de la forma correcta
		if (!FORMATO_FECHA_DDMMYYYY.equals(formato)) {
			throw new UVException(Level.SEVERE, NOMBRE_ESTA_CLASE, eNombreDeEsteMetodo, 
					"El formato de fecha especificado no es válido: " + formato, MENSAJE_ERROR_APLICACION, true);
		} else if (!"".equals(separador2) && !"/".equals(separador2) && !"-".equals(separador2)) {
			throw new UVException(Level.SEVERE, NOMBRE_ESTA_CLASE, eNombreDeEsteMetodo, 
					"El carácter para separar los días '" + separador2 + "' no es válido. Sólo puede ser '/' o '-'. "
					+ "También está permitido no usar ningún separador.", MENSAJE_ERROR_APLICACION, true);
		}
		
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
		formatoFecha.setLenient(false);
		// Comprobamos que la fecha tiene un valor correcto
		try {
			String strFecha = obtenerFechaAnoMesDia(valor, separador2);
			if (strFecha != null) {
				dateFecha = formatoFecha.parse(strFecha);
			}
		} catch (ParseException e) {
			return null;
		}
		return dateFecha;
	}
	
	/** lee parametro boolean.
	 * @param valor Valor recuperado del formulario.
	 * @param valorTrue Cadena que representa el valor 'true'
	 * @param valorFalse Cadena que representa el valor 'false'
	 * @return true si la cadena valor es igual a valorTrue y false si es igual a valorFalse 
	 * @throws UVException Si alguno de los parámetros es 'null' o si la cadena valor no es igual a valorTrue ni valorFalse
	 */
	public static boolean leeParametroBoolean(String valor, String valorTrue, String valorFalse) throws UVException {
		String eNombreDeEsteMetodo = "leeParametroBoolean";
		if (valor == null || valorTrue == null || valorFalse == null) {
			throw new UVException(Level.SEVERE, NOMBRE_ESTA_CLASE, eNombreDeEsteMetodo, 
					"Error en el uso de la función Formateador.leeParametroBoolean(). "
					+ "Alguno de los parámetros vale 'null'.", MENSAJE_ERROR_APLICACION, true);
		}
		if (valor.equals(valorTrue)) {
			return true;
		} else if (valor.equals(valorFalse)) {
			return false;
		} else {
			throw new UVException(Level.SEVERE, NOMBRE_ESTA_CLASE, eNombreDeEsteMetodo, 
					"Error en el uso de la función Formateador.leeParametroBoolean(). "
					+ "El valor recibido (" + valor + ") no coincide con el valor true (" + valorTrue + ") "
					+ "ni false (" + valorFalse + ").", MENSAJE_ERROR_APLICACION, true);
		}
	}
	
	/**
	 * Devuelve cualquier objeto que recibe como parámetro convertido a cadena de caracteres no nula.
	 * En caso de error o si el tipo de objeto no está contemplado, devuelve una cadena vacía "".
	 * 
	 * @param ob -> Cualquier objeto que queremos mostrar en formato de cadena de caracteres
	 * @return Un string con el valor del objeto (si es posible), y si no la cadena ""
	 */
	public static String noNulo(Object ob) {
		try {
			if (ob == null) {
				return "";
			} else {
				return ob.toString();
			}
		} catch (Exception ex) {
			return "";
		}
	}
	
	/** get numero de decimales.
	 * @param numero numero 
	 * @return numero de decimales
	 */
	public static int getNumeroDecimales(Object numero) {
		// Versión con DecimalFormat
		String numFormateado;
		String parteDecimal;
		DecimalFormat formateador = new DecimalFormat("0.#####"); // 5 decimales de precisión como máximo, eliminando los ceros no significativos
		formateador.setRoundingMode(RoundingMode.HALF_UP);
		if (numero == null) {
			return 0;
		} else if (numero instanceof Double) {
			numFormateado = formateador.format(((Double) numero).doubleValue());
		} else if (numero instanceof Float) {
			numFormateado = formateador.format(((Float) numero).doubleValue());
		} else {
			return 0;
		}
		if (numFormateado.indexOf(",") == -1) {
			return 0;
		} else {
			parteDecimal = numFormateado.substring(numFormateado.indexOf(","));
			return parteDecimal.length() - 1;
		}
	}

	/**
	 * Representa en formato de moneda el valor que le pasamos, con dos decimales de precisión y la coma como separador de decimales y el punto como separador de miles.
	 * Además, se añade el símbolo del euro al final con un espacio separando de la cantidad. Ejemplo de conversión: 12345.6 -> "12.345,60 €"
	 * 
	 * @param cantidad -> Valor que queremos pasar a formato moneda
	 * @param mostrarCeros -> Indica si el valor 0 se ha de convertir o si lo dejamos vacío (útil para generar el certificado de retenciones)
	 * @return Una cadena con el valor en euros correspondiente a 'cantidad'
	 */
	public static String formatoMoneda(Float cantidad, boolean mostrarCeros) {
		return formatoMoneda(Double.valueOf(cantidad.doubleValue()), mostrarCeros);
	}
	
	/**
	 * Representa en formato de moneda el valor que le pasamos, con dos decimales de precisión y la coma como separador de decimales y el punto como separador de miles.
	 * Además, se añade el símbolo del euro al final con un espacio separando de la cantidad. Ejemplo de conversión: 12345.6 -> "12.345,60 €"
	 * 
	 * @param cantidad -> Valor que queremos pasar a formato moneda
	 * @param mostrarCeros -> Indica si el valor 0 se ha de convertir o si lo dejamos vacío (útil para generar el certificado de retenciones)
	 * @return Una cadena con el valor en euros correspondiente a 'cantidad'
	 */
	public static String formatoMoneda(Double cantidad, boolean mostrarCeros) {
		Double cero = Double.valueOf(0.0);
		
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator(',');
		simbolos.setGroupingSeparator('.');
		simbolos.setCurrencySymbol("€");
		
		DecimalFormat formateador = new DecimalFormat("#,##0.00 €", simbolos);
		try {
			if (cantidad == null || (cero.equals(cantidad) && !mostrarCeros)) {
				return "";
			} else {
				return formateador.format(cantidad.doubleValue());
			}
		} catch (Exception ex) {
			return "#¡ERROR DE CONVERSIÓN!#";
		}
	}
	
	/**
	 * Representa en formato de moneda el valor que le pasamos, con dos decimales de precisión y la coma como separador de decimales y el punto como separador de miles.
	 * Además, se añade el símbolo del euro al final con un espacio separando de la cantidad. Ejemplo de conversión: 12345.6 -> "12.345,60 €"
	 * 
	 * @param cantidad -> Valor que queremos pasar a formato moneda
	 * @return Una cadena con el valor en euros correspondiente a 'cantidad'
	 */
	public static String formatoMoneda(Float cantidad) {
		return formatoMoneda(cantidad, true);
	}
	
	/**
	 * Representa en formato de moneda el valor que le pasamos, con dos decimales de precisión y la coma como separador de decimales y el punto como separador de miles.
	 * Además, se añade el símbolo del euro al final con un espacio separando de la cantidad. Ejemplo de conversión: 12345.6 -> "12.345,60 €"
	 * 
	 * @param cantidad -> Valor que queremos pasar a formato moneda
	 * @return Una cadena con el valor en euros correspondiente a 'cantidad'
	 */
	public static String formatoMoneda(Double cantidad) {
		return formatoMoneda(cantidad, true);
	}
	
	/**
	 * Convierte la calificación que pasamos como parámetro a una cadena de caracteres con la precisión indicada y el punto como separador de decimales.
	 *  
	 * @param calificacion -> Valor numérico de la calificación
	 * @param numeroDecimales -> Número de decimales de precisión que deseamos obtener
	 * @return Una cadena con la calificación formateada
	 */
	public static String formatoCalificacion(Float calificacion, int numeroDecimales) {
		if (calificacion == null) {
			return "";
		}
		return formatoCalificacion(Double.valueOf(calificacion.doubleValue()), numeroDecimales);
	}

	/**
	 * limita la cadena al tamano indicado, anade puntos suspensivos a las cadenas efectivamente limitadas.
	 * 
	 * @param cadena -> cadena que queremos limitar en tamano
	 * @param limite -> numero de caracteres maximos para la cadena
	 * @return cadena limitada a la longitud indicada
	 */
	public static String limita(String cadena, int limite) {
		if (cadena == null) {
			return cadena;
		}
		if (cadena.length() > limite) {
			return cadena.substring(0, limite) + "...";
		}
		return cadena;
	}
	
	
	/**
	 * Convierte la calificación que pasamos como parámetro a una cadena de caracteres con la precisión indicada y el punto como separador de decimales.
	 *  
	 * @param calificacion -> Valor numérico de la calificación
	 * @param numeroDecimales -> Número de decimales de precisión que deseamos obtener
	 * @return Una cadena con la calificación formateada
	 */
	public static String formatoCalificacion(Double calificacion, int numeroDecimales) {
		Double limiteInferior = Double.valueOf("0.0");
		Double limiteSuperior = Double.valueOf("10.0");
		final int tresDecimales = 3;
		DecimalFormat formateador = null;
		if (numeroDecimales < 0 || numeroDecimales > tresDecimales) {
			return "#¡ERROR DE PRECISIÓN!#";
		} else if (calificacion == null) {
			return "";
		} else if (calificacion < limiteInferior || calificacion > limiteSuperior) {
			return "#¡CALIFICACIÓN NO VÁLIDA!#";
		} else {
			if (numeroDecimales == 0) {
				formateador = new DecimalFormat("0");
			} else if (numeroDecimales == 1) {
				formateador = new DecimalFormat("0.0");
			} else if (numeroDecimales == 2) {
				formateador = new DecimalFormat("0.00");
			} else if (numeroDecimales == tresDecimales) {
				formateador = new DecimalFormat("0.000");
			} else {
				formateador = new DecimalFormat("0.00");
			}
			return formateador.format(calificacion.doubleValue());
		}
	}
	
	/**
	 * Convierte los créditos que pasamos como parámetro a una cadena de caracteres con la precisión indicada y el punto como separador de decimales.
	 *  
	 * @param creditos -> Valor numérico de los créditos
	 * @param numeroDecimales -> Número de decimales de precisión que deseamos obtener
	 * @return Una cadena con la calificación formateada
	 */
	public static String formatoCreditos(Double creditos, int numeroDecimales) {
		return formatoCreditos(creditos, numeroDecimales, true, false); // Por defecto, mostramos a 0 los créditos vacíos y no añadimos ceros decimales
	}
	
	/**
	 * Convierte los créditos que pasamos como parámetro a una cadena de caracteres con la precisión indicada y el punto como separador de decimales.
	 *  
	 * @param creditos -> Valor numérico de los créditos
	 * @param numeroDecimales -> Número de decimales de precisión que deseamos obtener
	 * @param mostrarVaciosComoCero -> Cuando el valor de los créditos es 'null', indica si queremos mostrar el valor 0 o no mostrar nada
	 * @return Una cadena con la calificación formateada
	 */
	public static String formatoCreditos(Double creditos, int numeroDecimales, boolean mostrarVaciosComoCero) {
		return formatoCreditos(creditos, numeroDecimales, mostrarVaciosComoCero, false); // Por defecto, no añadimos ceros decimales
	}
	
	
	/**
	 * Convierte los créditos que pasamos como parámetro a una cadena de caracteres con la precisión indicada y el punto como separador de decimales.
	 *  
	 * @param creditos -> Valor numérico de los créditos
	 * @param numeroDecimales -> Número de decimales de precisión que deseamos obtener
	 * @param mostrarVaciosComoCero -> Cuando el valor de los créditos es 'null', indica si queremos mostrar el valor 0 o no mostrar nada
	 * @param mostrarCerosDecimales -> Para mostrar o no los ceros a la derecha del punto decimal (tantos como indique el parámetro numeroDecimales)
	 * @return Una cadena con la calificación formateada
	 */
	@SuppressWarnings("checkstyle:CyclomaticComplexity")
	public static String formatoCreditos(Double creditos, int numeroDecimales, boolean mostrarVaciosComoCero, boolean mostrarCerosDecimales) {
		Double cero = Double.valueOf("0.0");
		Double limiteInferior = Double.valueOf("0.0");
		DecimalFormat formateador = null;
		if (numeroDecimales < 0 || numeroDecimales > 2) {
			return "#¡ERROR DE PRECISIÓN!#";
		} else if (creditos == null || (cero.equals(creditos) && !mostrarVaciosComoCero)) {
			return "";
		} else if (creditos < limiteInferior) {
			return "#¡NÚMERO DE CRÉDITOS NO VÁLIDO!#";
		} else {
			if (numeroDecimales == 0) {
				formateador = new DecimalFormat("0");
			} else if (numeroDecimales == 1) {
				formateador = mostrarCerosDecimales ? new DecimalFormat("0.0") : new DecimalFormat("0.#");
			} else if (numeroDecimales == 2) {
				formateador = mostrarCerosDecimales ? new DecimalFormat("0.00") : new DecimalFormat("0.##");
			} else {
				formateador = mostrarCerosDecimales ? new DecimalFormat("0.00") : new DecimalFormat("0.##");
			}
			return formateador.format(creditos.doubleValue());
		}
	}
	
	
	/** formato fecha.
	 * @param fecha fecha
	 * @param formato formato
	 * @param separadorDias separador dias
	 * @return cadena con el formato indicado
	 */
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:ExecutableStatementCount"})
	public static String formatoFecha(Date fecha, String formato, String separadorDias) {
		String resultado = "";
		SimpleDateFormat formateador = null;
		
		String separadorDias2 = separadorDias;
		if (separadorDias2 == null) {
			separadorDias2 = "/";
		}
		if (!"/".equals(separadorDias2) && !"-".equals(separadorDias2) && !"".equals(separadorDias2)) {
			return "#¡SEPARADOR DE FECHAS NO VÁLIDO!#";
		}
		
		try {
			if (FORMATO_FECHA_DDMMYYYY.equals(formato)) {
				formateador = new SimpleDateFormat("dd" + separadorDias2 + "MM" + separadorDias2 + "yyyy");
				formateador.setLenient(false);
				resultado = formateador.format(fecha);
			} else if (FORMATO_FECHA_YYYYMMDD.equals(formato)) {
				formateador = new SimpleDateFormat("yyyy" + separadorDias2 + "MM" + separadorDias2 + "dd");
				formateador.setLenient(false);
				resultado = formateador.format(fecha);
			} else if (FORMATO_FECHA_DDMMYYYY_HHMMSS.equals(formato)) {
				formateador = new SimpleDateFormat("dd" + separadorDias2 + "MM" + separadorDias2 + "yyyy" + " " + "HH:mm:ss");
				formateador.setLenient(false);
				resultado = formateador.format(fecha);
			} else if (FORMATO_FECHA_YYYYMMDD_HHMMSS.equals(formato)) {
				formateador = new SimpleDateFormat("yyyy" + separadorDias2 + "MM" + separadorDias2 + "dd" + " " + "HH:mm:ss");
				formateador.setLenient(false);
				resultado = formateador.format(fecha);
			} else if (FORMATO_FECHA_DIA_MES.equals(formato)) {
				Calendar calFecha = Calendar.getInstance();
				calFecha.setTime(fecha);
				resultado = calFecha.get(Calendar.DAY_OF_MONTH) + " de " + MESES.get(calFecha.get(Calendar.MONTH) + 1);
			} else if (FORMATO_FECHA_DIA_MES_ANIO.equals(formato)) {
				Calendar calFecha = Calendar.getInstance();
				calFecha.setTime(fecha);
				resultado = calFecha.get(Calendar.DAY_OF_MONTH) + " de " + MESES.get(calFecha.get(Calendar.MONTH) + 1) + " de " + calFecha.get(Calendar.YEAR);
			} else if (FORMATO_FECHA_HORA_MINUTOS.equals(formato)) {
				formateador = new SimpleDateFormat("HH:mm");
				formateador.setLenient(false);
				resultado = formateador.format(fecha);
			} else {
				resultado = "#¡FORMATO DE FECHA NO VÁLIDO!#";
			}
		} catch (Exception e) {
			resultado = "#¡ERROR DE CONVERSIÓN DE FECHAS!#";
		}
		return resultado;
	}
	
	/** formato fecha.
	 * @param fecha fecha
	 * @param formato formato
	 * @return cadena con la fecha
	 */
	public static String formatoFecha(Date fecha, String formato) {
		return formatoFecha(fecha, formato, null);
	}
	
	/** formato no nulo.
	 * @param textoPrevio texto previo
	 * @param elemento elemento
	 * @return cadena con texto previo y elemento
	 */
	public static String formatoNoNulo(String textoPrevio, Object elemento) {
		return formatoNoNulo(textoPrevio, elemento, null);
	}
	
	/** formato no nulo.
	 * @param elemento elemento
	 * @return cadena con elemento
	 */
	public static String formatoNoNulo(Object elemento) {
		return formatoNoNulo(null, elemento, null);
	}
	
	/**
	 * Da formato a una texto evitando que salga el texto "null" o similiar y con textos previos o posteriores que saldrán sólo si el texto no es nulo.
	 * @param textoPrevio texto previo
	 * @param elemento elemento
	 * @param textoPost texto post
	 * @return texto
	 */
	public static String formatoNoNulo(String textoPrevio, Object elemento, String textoPost) {
		StringBuilder salida = new StringBuilder();
		if (textoPrevio != null) {
			salida.append(textoPrevio);
		}
		if (elemento != null) {
			salida.append(elemento.toString());
		}
		if (textoPost != null) {
			salida.append(textoPost);
		}
		return salida.toString();
	}
}

