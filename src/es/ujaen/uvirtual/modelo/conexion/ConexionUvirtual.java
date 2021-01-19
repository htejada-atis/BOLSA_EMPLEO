package es.ujaen.uvirtual.modelo.conexion;

import java.sql.Connection;
import java.sql.SQLException;

/** Conexion uvirtual.
 */
public class ConexionUvirtual {
	
	private ConexionUvirtual() { }

	/** obtiene conexion a uvirtual.
	 * @return conexion a uvirtual
	 * @throws SQLException si error en bd
	 */
	public static Connection obtenerInstancia() throws SQLException {
		return Conexion.obtenerInstancia().conexionUvirtual();
	}
}
