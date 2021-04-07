package es.ujaen.uvirtual.modelo.bolsaempleo;

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
	public static final int ORDER_COLUMN_INDEX_TITULO = 3;
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS   ********************************************/
	
	/** Consulta ficheros en BBDD y los devuelve .
	 * @param clausula para filtrar los ficheros de la bd .
	 * @return lista todos los ficheros de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 */
	private List<Fichero> listaFicheros(String clausula) throws SQLException {
		List<Fichero> ficheros = new ArrayList<>();
		String consulta = "SELECT bepfich.* FROM tbep_ficheros bepfich " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							Fichero fich = new Fichero();
							fich.setCodNum(rs.getInt("CODNUM"));
							fich.setNombre(rs.getString("NOMBRE"));
							fich.setTitulo(rs.getString("TITULO"));
							fich.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
							ficheros.add(fich);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		return ficheros;
	}
	
	/** lista todos los ficheros .
	 * @return lista de todos los ficheros .
	 * @throws SQLException si hay un error en la base de datos .
	 */
	public List<Fichero> listaFicheros() throws SQLException {
		List<Fichero> ficheros = new ArrayList<>();
		String consulta = "SELECT bepfich.codnum, bepfich.nombre, bepfich.titulo FROM tbep_ficheros bepfich";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							Fichero fich = new Fichero();
							fich.setCodNum(rs.getInt("CODNUM"));
							fich.setNombre(rs.getString("NOMBRE"));
							fich.setTitulo(rs.getString("TITULO"));
							ficheros.add(fich);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		return ficheros;
	}
	
	/** Elimina un fichero .
	 * @param fichero a borrar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si fichero no es válido
	 */
	public void borraFichero(Fichero fichero) throws SQLException, UVException {
		if (fichero == null) {
			throw new UVException("No se puede eliminar un fichero vacío");
		}
		if (fichero.getCodNum() == null) {
			throw new UVException("No se puede eliminar un fichero con id vacío");
		}
		String consulta = "DELETE FROM tbep_ficheros WHERE codnum = ? ";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, fichero.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**	Función que inserta un fichero .
	 * @param fichero fichero a insertar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public void insertaFichero(Fichero fichero) throws SQLException, UVException {
		if (fichero == null) {
			throw new UVException("No se puede insertar un fichero vacío");
		}
		if (fichero.getNombre() == null || fichero.getNombre().equals("")) {
			throw new UVException("No se puede insertar un fichero sin nombre");
		}
		if (fichero.getTitulo() == null || fichero.getTitulo().equals("")) {
			throw new UVException("No se puede insertar un fichero sin título");
		}
		if (fichero.getArchivo() == null) {
			throw new UVException("No se puede insertar un fichero sin archivo");
		}
		
		String consulta = "INSERT INTO tbep_ficheros " 
				+ " (NOMBRE,TITULO,ARCHIVO) "
				+ "VALUES (?,?,?)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, fichero.getNombre());
			stmt.setString(parameterIndex++, fichero.getTitulo());
			stmt.setBinaryStream(parameterIndex++, fichero.getArchivo());
			stmt.executeUpdate();
		}
	}
	
	/** obtiene un archivo a partir de su id.
	 * @param id codigo del fichero .
	 * @return archivo con el id especificado .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public Fichero listaFichero(int id) throws SQLException, UVException {
		String clausulaWhere = "WHERE codnum = " + id;
		List<Fichero> ficheros = listaFicheros(clausulaWhere);
		if (ficheros.isEmpty()) {
			throw new UVException("No existe fichero");
		}
		return ficheros.get(0);
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
		
		String consulta = "SELECT bepfich.codnum, bepfich.nombre, bepfich.titulo FROM tbep_ficheros bepfich WHERE 1=1 ";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "bepfich.CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NOMBRE, "bepfich.NOMBRE");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_TITULO, "bepfich.TITULO");
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
					fichero.setTitulo(rs.getString("TITULO"));
					ficheros.add(fichero);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(ficheros);
		}
		
		return dataTable;
	}
	
}
