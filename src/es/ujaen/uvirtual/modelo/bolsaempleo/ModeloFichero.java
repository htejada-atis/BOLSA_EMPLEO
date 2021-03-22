package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Fichero;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.UVException;


/**
 * Clase de modelo para la gestión de ficheros 
 * Modelo - Operaciones con nombres: lista, inserta
 * Controlador - Opers. con nombres: obtener, agregar
 * 
 * @author jlopez
 *
 */
public class ModeloFichero {
	
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 2;
	
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
		
	/**
	 * Listado de ficheros . 
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @return listado de ficheros .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException si el fichero no es valido .
	 */
	public DataTable<Fichero> listaFicherosDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<Fichero> ficheros = new ArrayList<>();
		DataTable<Fichero> dataTable = new DataTable<Fichero>(params);
		
		String consulta = "SELECT bepfich.codnum, bepfich.nombre  FROM tbep_ficheros bepfich WHERE 1=1 ";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "bepfich.CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NOMBRE, "bepfich.NOMBRE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Fichero fichero = new Fichero();
					fichero.setCodNum(rs.getInt("CODNUM"));
					fichero.setNombre(rs.getString("NOMBRE"));
					
					ficheros.add(fichero);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(ficheros);
		}
		
		return dataTable;
	}
	
}
