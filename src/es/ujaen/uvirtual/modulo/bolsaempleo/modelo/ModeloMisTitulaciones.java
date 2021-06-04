package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de titulaciones de usuarios . 
 * @author ATISoluciones
 */
public class ModeloMisTitulaciones {
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 0;
	
	public static final int ORDER_COLUMN_INDEX_NOMBRE_USUARIO = 1;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 2;
	public static final int ORDER_COLUMN_INDEX_VALIDADA = 3;
	public static final int ORDER_COLUMN_INDEX_ARCHIVO = 4;
	
	public static final String CODNUM = "CODNUM";
	
	public static final String ERROR_NO_EXISTE_TITULACION = "No existe titulación";
	public static final String ERROR_EL_USUARIO_ES_REQUERIDO = "No se pueden listar titulaciones sin usuario";
	public static final String ERROR_TITULACION_VACIA = "No se puede insertar una titulación vacia";
	public static final String ERROR_TITULACION_SIN_ARCHIVO = "No se puede insertar una titulación sin archivo";
	public static final String ERROR_TITULACION_OBLIGATORIA = "Titulación obligatoria";
	public static final String ERROR_ID_TITULACION_OBLIGATORIO = "Id titulación no válido";
	public static final String ERROR_CANDIDATO_OBLIGATORIO = "Candidato obligatorio";
	public static final String ERROR_ID_USUARIO_NO_VALIDO = "Id usuario no válido";
	public static final String ERROR_SIN_TITULACION = "La titulación requerida";
	
	public static final int COLUMN_DESCRIPCION_MAXLENGTH = 250;

	protected static ModeloMisTitulaciones eInstancia;

	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMisTitulaciones();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloMisTitulaciones obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/** obtiene una titulación a partir de su id.
	 * @param id codigo de la titulación
	 * @return titulación con el id especificado
	 * @throws SQLException en caso de error en la BD
	 */
	public TitulacionUsuario getTitulacionUsuarioById(int id) throws SQLException, UVException {
		String clausulaWhere = "WHERE codnum = " + id;
		List<TitulacionUsuario> titulaciones = listaTitulacionesUsuarios(clausulaWhere);
		if (titulaciones.isEmpty()) {
			throw new UVException(ERROR_NO_EXISTE_TITULACION);
		}
		return titulaciones.get(0);
	}
	
	/** Consulta titulaciones en BBDD y las devuelve.
	 * @param clausula para filtrar las titulaciones de la bd
	 * @return todas las titulaciones de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException .
	 */
	public List<TitulacionUsuario> listaTitulacionesUsuarios(String clausula) throws SQLException, UVException {
		List<TitulacionUsuario> titulaciones = new ArrayList<>();
		String consulta = "SELECT beptus.* FROM tbep_titulaciones_usuario beptus " + clausula;

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					TitulacionUsuario tit = setTitulacionUsuarioFromResultSet(rs, true);
					titulaciones.add(tit);
				}
			}
		}

		return titulaciones;
	}
	
	/**
	 * Listado de titulaciones del candidato en una tabla .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param codnum .
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public BolsaEmpleoDataTable<Titulacion> listaTitulacionesDatatable(Map<String, String[]> params, Integer codnum) throws SQLException, UVException {
		List<Titulacion> titulaciones = new ArrayList<>();
		BolsaEmpleoDataTable<Titulacion> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = ""
				+ " SELECT beptit.CODNUM, beptit.NOMBRE "
				+ " FROM TBEP_TITULACIONES beptit "
				+ " LEFT JOIN TBEP_TITULACIONES_USUARIO beptus "
				+ "		ON beptit.CODNUM = beptus.BEPTUS_TIT_CODNUM AND beptus.BEPTUS_USU_CODNUM = ? AND beptus.FLGBORRADO != 'S' "
				+ "	WHERE beptit.FLGBORRADO != 'S' AND beptus.BEPTUS_USU_CODNUM IS NULL ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE, "beptit.NOMBRE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, codnum);
			stmtCount.setInt(indexParam++, codnum);
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Titulacion tit = new Titulacion();
					tit.setCodNum(rs.getInt("CODNUM"));
					tit.setNombre(rs.getString("NOMBRE"));
					titulaciones.add(tit);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(titulaciones);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado de titulaciones de un usuario en una tabla .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param codNum .
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public BolsaEmpleoDataTable<TitulacionUsuario> listaTitulacionesUsuarioDatatable(Map<String, String[]> params, Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(ERROR_EL_USUARIO_ES_REQUERIDO);
		}
		
		List<TitulacionUsuario> titulaciones = new ArrayList<>();
		BolsaEmpleoDataTable<TitulacionUsuario> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT beptit.CODNUM as beptitcod, beptit.NOMBRE AS beptitnombre, beptus.CODNUM, beptus.BEPTUS_USU_CODNUM, beptus.BEPTUS_TIT_CODNUM, "
				+ "beptus.DESCRIPCION, beptus.FLGBORRADO, beptus.FLGVALIDADA, beptus.FECHA_BORRADO, beptus.FECHA_VALIDADA, beptus.OTRATITULACION "
				+ "FROM tbep_titulaciones_usuario beptus "
				+ "left JOIN tbep_titulaciones beptit ON beptit.codnum=beptus.beptus_tit_codnum "
				+ "WHERE beptus.BEPTUS_USU_CODNUM = ? AND beptus.FLGBORRADO != 'S'";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_USUARIO, "beptit.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "beptus.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_VALIDADA, "beptus.FLGVALIDADA", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ARCHIVO, "beptus.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			
			int indexParam = 1;
			stmt.setInt(indexParam, codNum);
			stmtCount.setInt(indexParam++, codNum);
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					TitulacionUsuario tit = setTitulacionUsuarioFromResultSet(rs, false);
					titulaciones.add(tit);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(titulaciones);
		}
		
		return dataTable;
	}
	
	/**
	 * Devuelve un listado de las titulaciones de usuario por su id.
	 * @param ids codnum de titulaciones
	 * @return listado de titulaciones
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<TitulacionUsuario> getTitulacionesUsuarioByIds(int[] ids) throws SQLException, UVException {
		List<TitulacionUsuario> titulaciones = new ArrayList<>();
		
		for (int i = 0; i < ids.length; i++) {
			titulaciones.add(this.getTitulacionUsuarioById(ids[i]));
	    }
		
		return titulaciones;
	}
	
	/**	Función que inserta una titulacion de usuario en la BD.
	 * @param titulacion a insertar en la BD
	 * @param usuarioInsert .
	 * @return id de titulacion insertada.
	 * @throws SQLException en caso de error en la BD
	 * @throws IOException .
	 */
	public Integer insertaTitulacionUsuario(TitulacionUsuario titulacion, UsuarioBolsaEmpleo usuarioInsert) throws SQLException, UVException {
		if (titulacion == null) {
			throw new UVException(ERROR_TITULACION_VACIA);
		}
		if (titulacion.getArchivo() == null) {
			throw new UVException(ERROR_TITULACION_SIN_ARCHIVO);
		}
		
		if (titulacion.getTitulacion() != null) {
			String consulta = "INSERT INTO tbep_titulaciones_usuario " 
					+ " (BEPTUS_TIT_CODNUM,BEPTUS_USU_CODNUM,DESCRIPCION,ARCHIVO,UID_USUARIO) "
					+ "VALUES (?,?,?,?,?)";
	
			try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
					PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{CODNUM})) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, titulacion.getTitulacion().getCodNum());
				stmt.setInt(parameterIndex++, titulacion.getUsuario().getCodNum());
				stmt.setString(parameterIndex++, titulacion.getDescripcion());
				stmt.setBinaryStream(parameterIndex++, titulacion.getArchivo());
				stmt.setString(parameterIndex++, usuarioInsert.getCodCuenta());
				stmt.executeUpdate();
				
				ResultSet rs = stmt.getGeneratedKeys();
				rs.next();
				
				return rs.getInt(1);
			}
		} else {
			String consulta = "INSERT INTO tbep_titulaciones_usuario " 
					+ " (BEPTUS_USU_CODNUM,DESCRIPCION,ARCHIVO,OTRATITULACION,UID_USUARIO) "
					+ "VALUES (?,?,?,?,?)";
	
			try (Connection conexion = ConexionUvirtual.obtenerInstancia();
					PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{CODNUM})) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, titulacion.getUsuario().getCodNum());
				stmt.setString(parameterIndex++, titulacion.getDescripcion());
				stmt.setBinaryStream(parameterIndex++, titulacion.getArchivo());
				stmt.setString(parameterIndex++, titulacion.getOtraTitulacion());
				stmt.setString(parameterIndex++, usuarioInsert.getCodCuenta());
				stmt.executeUpdate();
				
				ResultSet rs = stmt.getGeneratedKeys();
				rs.next();
				
				return rs.getInt(1);
			}
		}
	}
	
	/** Elimina una titulación de un usuario .
	 * @param titulaciones a borrar
	 * @param usuarioInserta .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si titulación no es valida
	 */
	public void borraTitulacionUsuario(List<TitulacionUsuario> titulaciones, UsuarioBolsaEmpleo usuarioInserta) throws SQLException, UVException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(titulaciones.size());
		String query = "UPDATE TBEP_TITULACIONES_USUARIO SET FLGBORRADO=?,FECHA_BORRADO=?,UID_USUARIO=? WHERE CODNUM IN (" + params + ")";		
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, "S");
			stmt.setDate(parameterIndex++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(parameterIndex++, usuarioInserta.getCodCuenta());
			
			for (TitulacionUsuario titulacion : titulaciones) {
				stmt.setInt(parameterIndex++, titulacion.getCodNum()); 
			}
			stmt.executeUpdate();
		}
	}
	
	/** lista todas las titulaciones validadas del candidato .
	 * @param usuario .
	 * @return lista de todas las titulaciones .
	 * @throws SQLException si hay un error en la base de datos .
	 * @throws UVException .
	 */
	public List<TitulacionUsuario> listaTitulacionesValidadasCandidato(Integer usuario) throws SQLException, UVException {
		List<TitulacionUsuario> titulaciones = new ArrayList<>();
		String consulta = "SELECT beptit.CODNUM as beptitcod, beptit.NOMBRE AS beptitnombre, beptus.*"
				+ " FROM TBEP_TITULACIONES beptit"
				+ " INNER JOIN TBEP_TITULACIONES_USUARIO beptus ON beptus.BEPTUS_TIT_CODNUM = beptit.CODNUM"
				+ " WHERE beptus.BEPTUS_USU_CODNUM = ? AND beptus.FLGVALIDADA = 'S'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, usuario);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					TitulacionUsuario tit = setTitulacionUsuarioFromResultSet(rs, true);
					titulaciones.add(tit);
				}
			}
		}
		return titulaciones;
	}
	
	/**
	 * Valida una titulación .
	 * @param titulacion .
	 * @param candidato .
	 * @param usuarioUpdate .
	 * @param date .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void validaTitulacion(TitulacionUsuario titulacion, UsuarioBolsaEmpleo candidato, Date date, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		checkeosValidadDesvalida(titulacion, candidato);
		
		String consulta = "UPDATE TBEP_TITULACIONES_USUARIO SET FLGVALIDADA='S',FECHA_VALIDADA=?,UID_USUARIO=?"
				+ " WHERE BEPTUS_TIT_CODNUM = ? AND BEPTUS_USU_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setDate(parameterIndex++, new java.sql.Date(date.getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, titulacion.getTitulacion().getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Valida una titulación a false .
	 * @param titulacion .
	 * @param candidato .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desvalidaTitulacion(TitulacionUsuario titulacion, UsuarioBolsaEmpleo candidato, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		checkeosValidadDesvalida(titulacion, candidato);
		
		String consulta = "UPDATE TBEP_TITULACIONES_USUARIO SET FLGVALIDADA='N', FECHA_VALIDADA=null, UID_USUARIO=?"
				+ " WHERE BEPTUS_TIT_CODNUM = ? AND BEPTUS_USU_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, titulacion.getTitulacion().getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Cambia la titulación (fk) del usuario.
	 * @param titulacionUsuario .
	 * @param candidato .
	 * @param titulacion .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void cambiarTitulacion(TitulacionUsuario titulacionUsuario, UsuarioBolsaEmpleo candidato, Titulacion titulacion, UsuarioBolsaEmpleo usuarioUpdate) 
			throws UVException, SQLException {
		if (titulacionUsuario == null) {
			throw new UVException(ERROR_TITULACION_OBLIGATORIA);
		}
		if (titulacionUsuario.getCodNum() == null) {
			throw new UVException(ERROR_ID_TITULACION_OBLIGATORIO);
		}
		if (candidato == null) {
			throw new UVException(ERROR_CANDIDATO_OBLIGATORIO);
		}
		if (candidato.getCodNum() == null) {
			throw new UVException(ERROR_ID_USUARIO_NO_VALIDO);
		}
		if (titulacion == null || titulacion.getCodNum() == null) {
			throw new UVException(ERROR_TITULACION_OBLIGATORIA);
		}
		
		String consulta = "UPDATE TBEP_TITULACIONES_USUARIO SET BEPTUS_TIT_CODNUM = ?,UID_USUARIO=? WHERE CODNUM = ? AND BEPTUS_USU_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, titulacion.getCodNum());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, titulacionUsuario.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	private void checkeosValidadDesvalida(TitulacionUsuario titulacion, UsuarioBolsaEmpleo candidato) throws UVException {
		if (titulacion == null) {
			throw new UVException(ERROR_TITULACION_OBLIGATORIA);
		}
		if (titulacion.getCodNum() == null) {
			throw new UVException(ERROR_ID_TITULACION_OBLIGATORIO);
		}
		if (candidato == null) {
			throw new UVException(ERROR_CANDIDATO_OBLIGATORIO);
		}
		if (candidato.getCodNum() == null) {
			throw new UVException(ERROR_ID_USUARIO_NO_VALIDO);
		}
		if (titulacion.getTitulacion() == null) {
			throw new UVException(ERROR_SIN_TITULACION);
		}
	}

	private TitulacionUsuario setTitulacionUsuarioFromResultSet(ResultSet rs, Boolean archivo) throws SQLException, UVException {
		TitulacionUsuario tit = new TitulacionUsuario();
		tit.setCodNum(rs.getInt("CODNUM"));
		tit.setDescripcion(rs.getString("DESCRIPCION"));
		tit.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPTUS_USU_CODNUM")));
		
		if (rs.getInt("BEPTUS_TIT_CODNUM") != 0) {
			tit.setTitulacion(ModeloTitulacion.obtenerInstancia().getTitulacionById(rs.getInt("BEPTUS_TIT_CODNUM")));
		} else {
			tit.setOtraTitulacion(EscapaHTML.ajustaCodificacion(rs.getString("OTRATITULACION")));
		}

		if (Boolean.TRUE.equals(archivo)) {
			tit.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
		}

		tit.setBorrado("S".equals(rs.getString("FLGBORRADO")));
		tit.setFechaBorrado(rs.getDate("FECHA_BORRADO"));
		tit.setValidada("S".equals(rs.getString("FLGVALIDADA")));
		tit.setFechaValidada(rs.getDate("FECHA_VALIDADA"));
		
		return tit;
	}
}
