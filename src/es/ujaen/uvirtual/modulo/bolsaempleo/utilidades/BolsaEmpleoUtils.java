package es.ujaen.uvirtual.modulo.bolsaempleo.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Métodos para la bolsa de empleo.
 * @author jlopez
 *
 */
public class BolsaEmpleoUtils {
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
				return token.substring(token.indexOf("=") + NUMBER_2, token.length() - 1);
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
	public static String getParamForm(HttpServletRequest request, String param, String defaultParam) {
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
	public static Boolean isFloat(String value) {
		if (value == null) {
			return false;
		}
		
		String regex = "[0-9]+(,[0-9]+)?";
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
	 * @throws IOException .
	 * @throws NumberFormatException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public static void checkFileSize(InputStream archivo) throws SQLException, UVException, NumberFormatException, IOException {
	
		if (archivo.available() <= 0) {
			throw new UVException("No se puede insertar sin archivo o archivo vacio");
		}
		
		if (archivo.available() > Integer.parseInt(getParametroConfiguracion("bolsaempleo.maxEspacioArchivo"))) {
			throw new UVException("No se puede insertar un archivo tan grande");
		}	
	}
	
	/** Redondeo a dos decimales.
	 * @param v .
	 * @return .
	 */
	public static double redondeo(double v) {
		return Math.round(v * ROUNDER) / ROUNDER;
	}
}
