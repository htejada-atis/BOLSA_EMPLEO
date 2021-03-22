package es.ujaen.uvirtual.utilidades;

import java.util.List;

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
