package es.ujaen.uvirtual.utilidades;

import java.util.List;

import javax.servlet.http.Part;

/**
 * Métodos para la bolsa de empleo.
 * @author jlopez
 *
 */
public class BolsaEmpleoUtils {
	
	private BolsaEmpleoUtils() { }
	
	/** Método para construir cláusula "not in" de consulta sql.
	 * @param campo columna por la que se va a filtrar.
	 * @param valores array de valores.
	 * @return cadena con la consulta sql formateada.
	 */
	public static String consultaNotIn(String campo, List<String> valores) {
		String consulta = "";
		
		if (valores.size() > 0) {
			consulta = "WHERE " + campo + " not in (";
			for (int i = 0; i < valores.size(); i++) {
				if (i < valores.size() - 1) {
					consulta += valores.get(i) + ",";
				} else {
					consulta += valores.get(i) + ")";
				}
			}
		}
		
		return consulta;
	}
	
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

}
