package es.ujaen.uvirtual.modelo.conexion;

import java.sql.Connection;
import java.sql.SQLException;

/** Conexion uvirtual.
 */
public class ConexionUxxiAc {
	
	private ConexionUxxiAc() { }

	/** obtiene conexion a academico.
	 * @return conexion a academico
	 * @throws SQLException si error en bd
	 */
	public static Connection obtenerInstancia() throws SQLException {
		return Conexion.obtenerInstancia().conexionUxxiAc();
	}
}
