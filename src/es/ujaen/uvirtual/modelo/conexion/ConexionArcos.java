package es.ujaen.uvirtual.modelo.conexion;

import java.sql.Connection;
import java.sql.SQLException;

/** Conexion a arcos.
 */
public class ConexionArcos {
	
	private ConexionArcos() { }

	/** obtener instancia.
	 * @return conexion a arcos
	 * @throws SQLException si error en bd
	 */
	public static Connection obtenerInstancia() throws SQLException {
		return Conexion.obtenerInstancia().conexionArcos();
	}
}
