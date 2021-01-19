package es.ujaen.uvirtual.modelo.conexion;

import java.sql.Connection;
import java.sql.SQLException;

/** Conexion uvirtual.
 */
public class ConexionUxxiRrhh {
	
	private ConexionUxxiRrhh() { }

	/** obtiene conexion a rrhh.
	 * @return conexion a rrhh
	 * @throws SQLException si error en bd
	 */
	public static Connection obtenerInstancia() throws SQLException {
		return Conexion.obtenerInstancia().conexionUxxiRrhh();
	}
}
