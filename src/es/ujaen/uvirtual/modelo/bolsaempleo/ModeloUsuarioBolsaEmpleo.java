package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para obtener la información de usuarios de UVIRTUAL.
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * @author ATISoluciones
 */
public class ModeloUsuarioBolsaEmpleo {
	
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_DNI = 2;
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 3;
	public static final int ORDER_COLUMN_INDEX_TELEFONO = 4;
	public static final int ORDER_COLUMN_INDEX_MOVIL = 5;
	public static final int ORDER_COLUMN_INDEX_EMAIL = 6;
	public static final int ORDER_COLUMN_INDEX_ROL = 7;
	public static final int ORDER_COLUMN_INDEX_LISTA_DIST = 8;
	public static final int ORDER_COLUMN_INDEX_EXCLUIDO = 9;
	public static final int ORDER_COLUMN_INDEX_RAZON_EXCLUSION = 10;
	public static final int ORDER_COLUMN_INDEX_FECHA_EXCLUSION = 11;
	public static final int ORDER_COLUMN_INDEX_BORRADO = 12;
	public static final int ORDER_COLUMN_INDEX_FECHA_BORRADO = 13;
		
	
	/** Consulta usuarios en BBDD y las devuelve.
	 * @param clausula para filtrar los usuarios de la bd
	 * @return todos los usuarios de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	private List<UsuarioBolsaEmpleo> listaUsuarios(String clausula) throws SQLException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		String consulta = "SELECT n.* FROM tbep_usuarios n " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							UsuarioBolsaEmpleo usuario = setUsuario(rs);
							usuarios.add(usuario);		
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		return usuarios;
	}
	
	
	/** obtiene un usuario a partir de su nombre.
	 * @param nombre del usuario
	 * @return usuario con el nombre especificado
	 * @throws SQLException en caso de error en la BD
	 */
	public UsuarioBolsaEmpleo listaUsuario(String nombre) throws SQLException, UVException {
		String clausulaWhere = "WHERE n.nombre = '" + nombre + "'";
		List<UsuarioBolsaEmpleo> usuarios = listaUsuarios(clausulaWhere);
		if (usuarios.isEmpty()) {
			throw new UVException("No existe el usuario");
		}
		return usuarios.get(0);
	}
	
	
	
	/**
	 * Listado de areas. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public DataTable<UsuarioBolsaEmpleo> listaUsuarioBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		
		String consulta =
		"SELECT bepusu.* "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "WHERE FLGBORRADO!='S'"
		+ "AND FLGEXCLUIDO!='S'";
		
		DataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = setUsuario(rs);
					usuarios.add(usuario);				
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}
		
		return dataTable;
	}	
	
	
	/**
	 * Listado de areas. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */	
	public DataTable<UsuarioBolsaEmpleo> listaUsuarioBorradoBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		
		String consulta =
		"SELECT bepusu.* "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "WHERE FLGBORRADO='S'";
		
		DataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = setUsuario(rs);
					usuarios.add(usuario);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}
		
		return dataTable;
	}	
	
	/**
	 * Listado de areas. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */	
	public DataTable<UsuarioBolsaEmpleo> listaUsuarioExcluidoBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		
		String consulta =
		"SELECT bepusu.* "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "WHERE FLGEXCLUIDO='S'";
		
		DataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = setUsuario(rs);
					usuarios.add(usuario);					
				}				
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}
		
		return dataTable;
	}	
	
	
	/**
	 * Set usuario. 
	 * @param rs resultado de la consulta
	 * @return usuario
	 * @throws SQLException en caso de error de base de datos
	 */	
	public UsuarioBolsaEmpleo setUsuario(ResultSet rs) throws SQLException {
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		usuario.setCodNum(rs.getInt("CODNUM"));
		usuario.setDni(rs.getString("DNI"));
		usuario.setNombre(rs.getString("NOMBRE"));
		usuario.setTelefono(rs.getString("TELEFONO"));					
		usuario.setMovil(rs.getString("MOVIL"));
		usuario.setEmail(rs.getString("EMAIL"));
		usuario.setRol(rs.getString("ROL"));
		usuario.setListaDist(rs.getString("FLGLISTADISTRIBUCION").equals("S"));
		usuario.setExcluido(rs.getString("FLGEXCLUIDO").equals("S"));
		usuario.setRazonExcluido(rs.getString("RAZON_EXCLUSION"));
		usuario.setFechaExclusion(rs.getTimestamp("FECHA_EXCLUSION"));
		usuario.setBorrado(rs.getString("FLGBORRADO").equals("S"));
		usuario.setFechaBorrado(rs.getTimestamp("FECHA_BORRADO"));
		
		return usuario;
	}
	
	
	/**
	 * Get datatable. 
	 * @param params parametrospara tabla
	 * @param consulta query de la consulta
	 * @return usuario
	 * @throws UVException en caso de error de paquete datatables
	 */	
	public DataTable<UsuarioBolsaEmpleo> setDatatable(Map<String, String[]> params, String consulta) throws UVException {
		DataTable<UsuarioBolsaEmpleo> dataTable = new DataTable<UsuarioBolsaEmpleo>(params);
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "bepusu.CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_DNI, "bepusu.DNI");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NOMBRE, "bepusu.NOMBRE");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_TELEFONO, "bepusu.TELEFONO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_MOVIL, "bepusu.MOVIL");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_EMAIL, "bepusu.EMAIL");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ROL, "bepusu.ROL");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_LISTA_DIST, "bepusu.FLGLISTADISTRIBUCION");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_EXCLUIDO, "bepusu.FLGEXCLUIDO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_RAZON_EXCLUSION, "bepusu.RAZON_EXCLUSION");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_FECHA_EXCLUSION, "bepusu.FECHA_EXCLUSION");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_BORRADO, "bepusu.FLGBORRADO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_FECHA_BORRADO, "bepusu.FECHA_BORRADO");
		dataTable.setQuery(consulta);
		
		return dataTable;
	}
}
