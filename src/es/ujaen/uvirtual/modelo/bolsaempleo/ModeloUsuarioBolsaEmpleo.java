package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modelo.conexion.ConexionArcos;
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
	public static final int ORDER_COLUMN_INDEX_TIPO_DOCUMENTO = 2;
	public static final int ORDER_COLUMN_INDEX_NUMDOCUMENTO = 3;
	public static final int ORDER_COLUMN_INDEX_COD_CUENTA = 4;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS = 5;
	public static final int ORDER_COLUMN_INDEX_EMAIL = 6;
	public static final int ORDER_COLUMN_INDEX_ROL = 7;
	public static final int ORDER_COLUMN_INDEX_LISTA_DIST = 8;
	public static final int ORDER_COLUMN_INDEX_EXCLUIDO = 9;
	public static final int ORDER_COLUMN_INDEX_RAZON_EXCLUSION = 10;
	public static final int ORDER_COLUMN_INDEX_FECHA_EXCLUSION = 11;
	
	public static final Integer PARAM_ROL_ID = 1051;
		
	
	/** Consulta usuarios en BBDD y las devuelve.
	 * @param clausula para filtrar los usuarios de la bd
	 * @return todos los usuarios de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	private List<UsuarioBolsaEmpleo> listaUsuarios(String clausula) throws SQLException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		String consulta = "SELECT * FROM tbep_usuarios bepusu INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA " + clausula;
		
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
		String clausulaWhere = "WHERE bepusu.codcuenta = '" + nombre + "'";
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
		"SELECT * "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
		+ "WHERE FLGBORRADO!='S' "
		+ "AND FLGEXCLUIDO!='S'";
		
		String tipo = "general";
		
		DataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta, tipo);
				
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
		"SELECT * "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
		+ "WHERE bepusu.FLGBORRADO='S'";
		
		String tipo = "borrado";
		
		DataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta, tipo);
				
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
		"SELECT * "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
		+ "WHERE FLGEXCLUIDO='S' "
		+ "AND FLGBORRADO!='S'";
		
		String tipo = "excluido";
		
		DataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta, tipo);
			
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
	 * @throws UVException en caso de no poder crear el usuario
	 */	
	public UsuarioBolsaEmpleo setUsuario(ResultSet rs) throws SQLException, UVException {
		ModeloRol modeloRol = new ModeloRol();
		
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		usuario.setCodNum(rs.getInt("CODNUM"));
		usuario.setCodPersona(rs.getInt("CODPERSONA"));
		usuario.setCodCuenta(rs.getString("CODCUENTA"));
		usuario.setTipoDocumento(rs.getString("STRTIPODOCUMENTO"));
		usuario.setNumDocumento(rs.getString("IDNIF") + rs.getString("LETRANIF"));
		usuario.setNombre(rs.getString("STRNOMBRE"));
		usuario.setPrimerApellido(rs.getString("STRAPELLIDO1"));
		usuario.setSegundoApellido(rs.getString("STRAPELLIDO2"));
		usuario.setEmail(rs.getString("EMAIL_ALTA"));
		usuario.setRol(modeloRol.getRoleById(rs.getInt("ROL")));
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
	 * @param tipo .
	 * @return usuario
	 * @throws UVException en caso de error de paquete datatables
	 */	
	public DataTable<UsuarioBolsaEmpleo> setDatatable(Map<String, String[]> params, String consulta, String tipo) throws UVException {
		DataTable<UsuarioBolsaEmpleo> dataTable = new DataTable<UsuarioBolsaEmpleo>(params);
		
		switch (tipo) {
			case "excluido":
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "bepusu.CODNUM");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_TIPO_DOCUMENTO, "uvpersona.STRTIPODOCUMENTO");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO, "uvpersona.IDNIF");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_COD_CUENTA, "bepusu.CODCUENTA");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS, "uvpersona.STRAPELLIDO1");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_EMAIL, "bepusu.EMAIL");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ROL, "bepusu.ROL");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_RAZON_EXCLUSION, "bepusu.RAZON_EXCLUSION");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_FECHA_EXCLUSION, "bepusu.FECHA_EXCLUSION");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_LISTA_DIST, "bepusu.FLGLISTADISTRIBUCION");
				dataTable.setQuery(consulta);
				
				return dataTable;
				
			default:
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "bepusu.CODNUM");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_TIPO_DOCUMENTO, "uvpersona.STRTIPODOCUMENTO");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO, "uvpersona.IDNIF");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_COD_CUENTA, "bepusu.CODCUENTA");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS, "uvpersona.STRAPELLIDO1");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_EMAIL, "bepusu.EMAIL");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ROL, "bepusu.ROL");
				dataTable.setOrderColumn(ORDER_COLUMN_INDEX_LISTA_DIST, "bepusu.FLGLISTADISTRIBUCION");
				dataTable.setQuery(consulta);
				
				return dataTable;
		}
	}
	
	
	
	/**	Función que inserta un usuario en la BD.
	 * @param usuario a insertar en la BD
	 * @throws SQLException en caso de error en la BD
	 */
	public void insertaUsuario(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("No se puede insertar un usuario vacio");
		}
		if (usuario.getRol() == null || usuario.getRol().equals("")) {
			throw new UVException("No se puede insertar un usuario sin role");
		}
		
		if (!usuario.getExcluido()) {
			String consulta = "INSERT INTO tbep_usuarios " 
					+ " (CODPERSONA,CODCUENTA,ROL,EMAIL,FLGLISTADISTRIBUCION) "
					+ "VALUES (?, ?, ?, ?, ?)";
			
			try (Connection conexion = ConexionUvirtual.obtenerInstancia();
					PreparedStatement stmt = conexion.prepareStatement(consulta);) {
					int parameterIndex = 1;
					stmt.setInt(parameterIndex++, usuario.getCodPersona());
					stmt.setString(parameterIndex++, usuario.getCodCuenta());
					stmt.setInt(parameterIndex++, usuario.getRol().getCodNum());
					stmt.setString(parameterIndex++, usuario.getEmail());
					stmt.setString(parameterIndex++, usuario.getListaDist() ? "S" : "N");
					stmt.executeUpdate();
				}
		} else {
			String consulta = "INSERT INTO tbep_usuarios " 
					+ " (CODPERSONA,CODCUENTA,ROL,EMAIL,FLGLISTADISTRIBUCION,FLGEXCLUIDO,RAZON_EXCLUSION,FECHA_EXCLUSION) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			try (Connection conexion = ConexionUvirtual.obtenerInstancia();
					PreparedStatement stmt = conexion.prepareStatement(consulta);) {
					int parameterIndex = 1;
					stmt.setInt(parameterIndex++, usuario.getCodPersona());
					stmt.setString(parameterIndex++, usuario.getCodCuenta());
					stmt.setInt(parameterIndex++, usuario.getRol().getCodNum());
					stmt.setString(parameterIndex++, usuario.getEmail());
					stmt.setString(parameterIndex++, usuario.getListaDist() ? "S" : "N");
					stmt.setString(parameterIndex++, usuario.getExcluido() ? "S" : "N");
					stmt.setString(parameterIndex++, usuario.getRazonExcluido());
					stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusion().getTime()));
					stmt.executeUpdate();
				}
		}
	}
	
	/** Actualiza un usuario.
	 * @param usuario UsuarioBolsaEmpleo con los datos nuevos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de errores de validacion
	 */
	public void actualizaUsuario(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("usuario obligatorio");
		}
		if (usuario.getCodNum() == null) {
			throw new UVException("id usuario no válido");
		}
		
		String consulta = "UPDATE tbep_usuarios "
			+ " SET ROL=?, FLGLISTADISTRIBUCION=?, FLGEXCLUIDO=?, RAZON_EXCLUSION=?, FECHA_EXCLUSION=? "
			+ " WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, usuario.getRol().getCodNum());
			stmt.setString(parameterIndex++, usuario.getListaDist() ? "S" : "N");
			stmt.setString(parameterIndex++, usuario.getExcluido() ? "S" : "N");
			stmt.setString(parameterIndex++, usuario.getRazonExcluido());
			stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusion().getTime()));
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	
	/** Borra o restaura una noticia .
	 * @param usuario a borrar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException si noticia no es valida .
	 */
	public void borraRestauraUsuario(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("No se puede eliminar un usuario vacía");
		}
		if (usuario.getCodNum() == null) {
			throw new UVException("No se puede eliminar un usuario con id vacío");
		}
		String consulta = "UPDATE tbep_usuarios SET FLGBORRADO=? WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuario.getBorrado() ? "S" : "N");
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	
	/** Comprueba si el usuario candidato se encuentra en UVIRTUAL .
	 * @param  datos .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException si noticia no es valida .
	 */
	public void checkUser(UVDatos datos) {
		Usuario usuArcos = datos.getUsuario();
		try {
			UsuarioBolsaEmpleo usu = listaUsuario(usuArcos.getUid());
		} catch (UVException e) {
			ModeloRol modeloRol = new ModeloRol();
			Rol role;
			
			try {
				role = modeloRol.getRoleById(PARAM_ROL_ID);
				UsuarioBolsaEmpleo usuarioFinal = new UsuarioBolsaEmpleo(usuArcos.getCodigoPersonaArcos(), usuArcos.getUid(), role, true, false);
				insertaUsuario(usuarioFinal);
			} catch (SQLException | UVException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
