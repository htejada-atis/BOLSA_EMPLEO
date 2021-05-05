package es.ujaen.uvirtual.modulo.bolsaempleo.utilidades;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 * Métodos para la bolsa de empleo.
 * @author jlopez
 *
 */
public class BolsaEmpleoUtils {
	
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
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
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
	public static Boolean isInteger(String value) {
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
}
