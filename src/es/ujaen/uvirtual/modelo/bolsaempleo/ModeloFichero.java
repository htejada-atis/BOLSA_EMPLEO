package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Fichero;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;


/**
 * Clase de modelo para la gestión de ficheros 
 * Modelo - Operaciones con nombres: lista, inserta
 * Controlador - Opers. con nombres: obtener, agregar
 * 
 * @author jlopez
 *
 */
public class ModeloFichero {
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS   ********************************************/
	
	
	/** lista todos los ficheros.
	 * @return lista de todos los ficheros .
	 * @throws SQLException si hay un error en la base de datos .
	 */
	public List<Fichero> listaFicheros() throws SQLException {
		List<Fichero> ficheros = new ArrayList<>();
		String consulta = "SELECT f.nombre FROM tbep_ficheros f";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							Fichero fich = new Fichero();
							fich.setCodNum(rs.getInt("CODNUM"));
							fich.setNombre(rs.getString("NOMBRE"));
							ficheros.add(fich);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		return ficheros;
	}
	
	/**	Función que inserta un fichero .
	 * @param in fichero a insertar en la BD .
	 * @param nombre nombre del fichero insertado .
	 * @throws SQLException en caso de error en la BD .
	 */
	public void insertaFichero(InputStream in, String nombre) throws SQLException {
		String consulta = "INSERT INTO tbep_ficheros " 
				+ " (NOMBRE,FICHERO) "
				+ "VALUES (?,?)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, nombre);
			stmt.setBinaryStream(parameterIndex++, in);
			stmt.executeUpdate();
		}
	}
	
}
