package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de mensajes .
 * 
 * @author ATISoluciones
 */
public class ModeloMensajes {
	public static final int MENSAJES_COLUMN_INDEX_FECHACREACION = 1;
	public static final int MENSAJES_COLUMN_INDEX_TITULO = 2;	
	public static final int MENSAJES_COLUMN_INDEX_ESTADO = 3;
	
	public static final int DESTINATARIOS_COLUMN_INDEX_DOCUMENTO = 1;
	public static final int DESTINATARIOS_COLUMN_INDEX_CORREO = 3;
	public static final int DESTINATARIOS_COLUMN_INDEX_ROL = 4;
	public static final int DESTINATARIOS_COLUMN_INDEX_DISTRIBUCION = 5;
	public static final int DESTINATARIOS_COLUMN_INDEX_CONVOCATORIA = 6;
	public static final int DESTINATARIOS_COLUMN_INDEX_AREA = 7;
	
	public static final int MENSAJES_COLUMN_TITULO_MAXLENGTH = 500;
	
	public static final String MENSAJE_ERROR_MENSAJE_NULL = "No se puede insertar un mensaje vacio";	
	public static final String MENSAJE_ERROR_NO_EXISTE_MENSAJE = "No existe el mensaje";
	
	public static final String MENSAJE_AFINIDAD_OBLIGATORIA = "Afinidad obligatorio";
	public static final String MENSAJE_AFINIDAD_CODNUM_REQUERIDO = "id afinidad no válido";

	public static final String CODNUM = "CODNUM";
	public static final String ESTADO_BORRADOR = "BORRADOR";
	public static final String ESTADO_ENVIADO = "ENVIADO";
	
	public static final String ERROR_DESTINATARIO_YA_EXISTE = "El destinatario ya estaba agregado al mensaje previamente";

	protected static ModeloMensajes eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMensajes();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloMensajes obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Listado de mensajes.
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de mensajes
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  error si no existe la area
	 * @throws IOException .
	 */
	public BolsaEmpleoDataTable<Mensaje> listaMensajesDatatable(Map<String, String[]> params) throws SQLException, UVException, IOException {
		List<Mensaje> mensajes = new ArrayList<>();
		BolsaEmpleoDataTable<Mensaje> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT bepmen.* FROM TBEP_MENSAJES bepmen";

		dataTable.setColumn(MENSAJES_COLUMN_INDEX_FECHACREACION, "bepmen.FECHA_CREACION");
		dataTable.setColumn(MENSAJES_COLUMN_INDEX_TITULO, "bepmen.TITULO");		
		dataTable.setColumn(MENSAJES_COLUMN_INDEX_ESTADO, "bepmen.ESTADO");
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			dataTable.setFiltersParams(stmt, stmtCount, 1);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					mensajes.add(createMensajeFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(mensajes);
		}

		return dataTable;
	}
	
	/**
	 * Datatable de los destinatarios del mensaje.
	 * @param params .
	 * @param mensaje .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws IOException .
	 */
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaDestinatariosMensajeDatatable(Map<String, String[]> params, Mensaje mensaje) throws SQLException, UVException {
		if (mensaje == null) {
			throw new UVException("El mensaje es requerido");
		}
			
		List<UsuarioBolsaEmpleo> destinatarios = new ArrayList<>();
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = ""
				+ " SELECT bepusu.* "
				+ " FROM TBEP_MEN_DESTINATARIOS bepmde "
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepmde.BEPUSU_CODNUM "
				+ " WHERE bepmde.BEPMEN_CODNUM = ? ";

		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DOCUMENTO, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_CORREO, "bepusu.VUAJA_EMAIL_ALTA");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_ROL, "bepusu.ROL");
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			int indexParams = 1;
			stmt.setInt(indexParams, mensaje.getCodNum());
			stmtCount.setInt(indexParams++, mensaje.getCodNum());			
			dataTable.setFiltersParams(stmt, stmtCount, indexParams);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {					
					destinatarios.add(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("CODNUM")));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(destinatarios);
		}

		return dataTable;
	}
	
	/**
	 * Datatable de los destinatarios disponibles del mensaje.
	 * @param params .
	 * @param mensaje .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws IOException .
	 */
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaDestinatariosDisponiblesMensajeDatatable(Map<String, String[]> params, Mensaje mensaje) 
			throws SQLException, UVException {
		if (mensaje == null) {
			throw new UVException("El mensaje es requerido");
		}
			
		List<UsuarioBolsaEmpleo> destinatarios = new ArrayList<>();
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT bepusu.* FROM TBEP_USUARIOS bepusu"
				+ "	LEFT JOIN TBEP_SOLICITUDES bepsol ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ "	LEFT JOIN TBEP_CONVOCATORIAS bepcon ON bepcon.CODNUM  = bepsol.BEPCON_CODNUM"
				+ "	LEFT JOIN TBEP_MEN_DESTINATARIOS bepmde ON bepusu.CODNUM = bepmde.BEPUSU_CODNUM AND bepmde.BEPMEN_CODNUM = ?"
				+ "	WHERE bepusu.FLGBORRADO = 'N' AND bepmde.BEPUSU_CODNUM IS NULL AND bepusu.VUAJA_EMAIL_ALTA IS NOT NULL";

		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DOCUMENTO, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_CORREO, "bepusu.VUAJA_EMAIL_ALTA");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_ROL, "bepusu.ROL");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DISTRIBUCION, "bepusu.FLGLISTADISTRIBUCION", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_CONVOCATORIA, "bepcon.CODNUM");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_AREA, "bepcon.CODNUM");
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			int indexParams = 1;
			stmt.setInt(indexParams, mensaje.getCodNum());
			stmtCount.setInt(indexParams++, mensaje.getCodNum());	
			dataTable.setFiltersParams(stmt, stmtCount, indexParams);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {					
					destinatarios.add(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("CODNUM")));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(destinatarios);
		}

		return dataTable;
	}
	
	/**
	 * Devuelve un mensaje por su id.
	 * 
	 * @param codNum .
	 * @return Mensaje o null si no existe
	 * @throws IOException .
	 * @throws SQLException .
	 */
	public Mensaje getMensajeById(Integer codNum) throws SQLException, UVException, IOException {
		if (codNum == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_MENSAJE);
		}
		String consulta = "SELECT bepmen.* FROM TBEP_MENSAJES bepmen WHERE bepmen.CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_MENSAJE);
				}

				return this.createMensajeFromResultSet(rs);
			}
		}
	}

	/**
	 * Añade un mensaje.
	 * 
	 * @param mensaje .
	 * @param usuarioUpdate .
	 * @return id mensaje creado.
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Integer nuevoMensaje(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (mensaje == null) {
			throw new UVException(MENSAJE_ERROR_MENSAJE_NULL);
		}

		String consulta = "INSERT INTO TBEP_MENSAJES (TITULO,CUERPO,FECHA_CREACION,ESTADO,UID_USUARIO) VALUES (?,?,?,?,?)";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta, new String[] {CODNUM})) {
			int parameterIndex = 1;

			stmt.setString(parameterIndex++, mensaje.getTitulo());
			stmt.setClob(parameterIndex++, BolsaEmpleoUtils.stringToClob(mensaje.getCuerpo(), conexion));
			stmt.setDate(parameterIndex++, new java.sql.Date(mensaje.getFechaCreacion().getTime()));
			stmt.setString(parameterIndex++, mensaje.getEstado());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		}
	}

	/**
	 * Crea un nuevo mensaje en estado en borrador con un titulo en borrador.
	 * @param usuarioUpdate .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Integer nuevoMensajeEnBorrador(UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		Mensaje mensaje = new Mensaje();
		mensaje.setTitulo(ESTADO_BORRADOR);
		mensaje.setEstado(ESTADO_BORRADOR);
		mensaje.setFechaCreacion(BolsaEmpleoUtils.getCurrentDateTime());
		mensaje.setCuerpo(ESTADO_BORRADOR);
		
		return nuevoMensaje(mensaje, usuarioUpdate);
	}
	
	/** Actualiza un mensaje .
	 * @param mensaje .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void actualizarMensaje(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (mensaje == null) {
			throw new UVException(MENSAJE_ERROR_MENSAJE_NULL);
		}
		
		String consulta = "UPDATE TBEP_MENSAJES SET TITULO = ?, CUERPO = ?, ESTADO = ?, UID_USUARIO = ?"
				+ "	WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setString(indexParam++, mensaje.getTitulo());
			stmt.setString(indexParam++, mensaje.getCuerpo());
			stmt.setString(indexParam++, mensaje.getEstado());
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, mensaje.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Agrega un destinatario a un mensaje .
	 * @param mensaje .
	 * @param destinatario .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void agregarDestinatario(Mensaje mensaje, UsuarioBolsaEmpleo destinatario, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			String consultaSelect = "SELECT * FROM TBEP_MEN_DESTINATARIOS WHERE BEPMEN_CODNUM = ? AND BEPUSU_CODNUM = ?";
			try (PreparedStatement stmt = conexion.prepareStatement(consultaSelect)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, mensaje.getCodNum());
				stmt.setInt(parameterIndex++, destinatario.getCodNum());
				
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						throw new UVException(ERROR_DESTINATARIO_YA_EXISTE);									
					}
				}
			}
		
			String consultaInsert = "INSERT INTO TBEP_MEN_DESTINATARIOS (BEPMEN_CODNUM, BEPUSU_CODNUM, UID_USUARIO)"
					+ "	VALUES (?, ?, ?)";
			try (PreparedStatement stmt = conexion.prepareStatement(consultaInsert)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, mensaje.getCodNum());
				stmt.setInt(parameterIndex++, destinatario.getCodNum());
				stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
				stmt.executeUpdate();
			}
		}
	}
	
	/** Elimina un destinatario de un mensaje .
	 * @param mensaje .
	 * @param destinatario .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void eliminarDestinatario(Mensaje mensaje, UsuarioBolsaEmpleo destinatario, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				String consultaUpdate = "UPDATE TBEP_MEN_DESTINATARIOS SET UID_USUARIO = ? WHERE BEPMEN_CODNUM = ? AND BEPUSU_CODNUM = ?";			
				try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
					int indexParam = 1;
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.setInt(indexParam++, mensaje.getCodNum());
					stmt.setInt(indexParam++, destinatario.getCodNum());
					stmt.executeUpdate();
				}
				
				String consultaDelete = "DELETE FROM TBEP_MEN_DESTINATARIOS WHERE BEPMEN_CODNUM = ? AND BEPUSU_CODNUM = ?";
				try (PreparedStatement stmt = conexion.prepareStatement(consultaDelete)) {
					int indexParam = 1;
					stmt.setInt(indexParam++, mensaje.getCodNum());
					stmt.setInt(indexParam++, destinatario.getCodNum());
					stmt.executeUpdate();
				}
				
				conexion.commit();
			} catch (Exception e) {
				conexion.rollback();
				throw e;
			} finally {
				conexion.setAutoCommit(true);
			}
		}
	}
	
	/**
	 * Elimina un mensaje en borrador.
	 * @param mensaje .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void eliminarMensajeBorrador(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate) throws UVException, SQLException {
		if (mensaje == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_MENSAJE);
		}
		if (!mensaje.getEstado().equals(ESTADO_BORRADOR)) {
			throw new UVException("El mensaje no está en borrador");
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			this.eliminaDestinatariosMensaje(mensaje, usuarioUpdate);
			
			try {
				String consultaUpdate = "UPDATE TBEP_MENSAJES SET UID_USUARIO = ? WHERE CODNUM = ?";
				try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
					int parameterIndex = 1;
					stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
					stmt.setInt(parameterIndex++, mensaje.getCodNum());
					stmt.executeUpdate();
				}
				
				String consultaDelete = "DELETE FROM TBEP_MENSAJES WHERE CODNUM = ?";
				try (PreparedStatement stmt = conexion.prepareStatement(consultaDelete)) {
					int parameterIndex = 1;
					stmt.setInt(parameterIndex++, mensaje.getCodNum());
					stmt.executeUpdate();
				}
				
				conexion.commit();
			} catch (Exception e) {
				conexion.rollback();
				throw e;
			} finally {
				conexion.setAutoCommit(true);
			}
		}
	}
	
	/**
	 * Elimina los destinatarios de un mensaje .
	 * @param mensaje .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void eliminaDestinatariosMensaje(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate) throws UVException, SQLException {
		List<UsuarioBolsaEmpleo> listaUsuarios = obtenerDestinatariosMensaje(mensaje);
		
		for (UsuarioBolsaEmpleo usuario: listaUsuarios) {
			this.eliminarDestinatario(mensaje, usuario, usuarioUpdate);
		}
	}
	
	/** Obtiene la lista de destinatarios de un mensaje .
	 * @param mensaje .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<UsuarioBolsaEmpleo> obtenerDestinatariosMensaje(Mensaje mensaje) throws UVException, SQLException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		
		if (mensaje == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_MENSAJE);
		}
		
		String consultaSelect = "SELECT bepusu.* FROM TBEP_MEN_DESTINATARIOS bepmde"
				+ "	INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepmde.BEPUSU_CODNUM"
				+ " WHERE BEPMEN_CODNUM = ? AND bepusu.VUAJA_EMAIL_ALTA IS NOT NULL";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consultaSelect)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, mensaje.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					usuarios.add(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioFromResultSet(rs));
				}
			}
		}
		
		return usuarios;
	}

	private Mensaje createMensajeFromResultSet(ResultSet rs) throws SQLException, IOException {
		Mensaje mensaje = new Mensaje();
		mensaje.setCodNum(rs.getInt(CODNUM));
		mensaje.setTitulo(rs.getString("TITULO"));
		mensaje.setCuerpo(BolsaEmpleoUtils.clobToString(rs.getClob("CUERPO")));
		mensaje.setFechaCreacion(rs.getDate("FECHA_CREACION"));
		mensaje.setEstado(rs.getString("ESTADO"));		
		return mensaje;
	}
}
