package es.ujaen.uvirtual.utilidades;

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

}
