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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Destinatario;
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
	
	public static final int DESTINATARIOS_COLUMN_INDEX_DOCUMENTO_ENVIO = 0;
	public static final int DESTINATARIOS_COLUMN_INDEX_NOMBRE_ENVIO = 1;
	public static final int DESTINATARIOS_COLUMN_INDEX_CORREO_ENVIO = 2;
	public static final int DESTINATARIOS_COLUMN_INDEX_ROL_ENVIO = 3;
	
	public static final int DESTINATARIOS_COLUMN_INDEX_DOCUMENTO = 1;
	public static final int DESTINATARIOS_COLUMN_INDEX_NOMBRE = 2;
	public static final int DESTINATARIOS_COLUMN_INDEX_CORREO = 3;
	public static final int DESTINATARIOS_COLUMN_INDEX_ROL = 4;
	public static final int DESTINATARIOS_COLUMN_INDEX_DISTRIBUCION = 5;
	public static final int DESTINATARIOS_COLUMN_INDEX_CONVOCATORIA = 6;
	public static final int DESTINATARIOS_COLUMN_INDEX_AREA = 7;
	
	public static final int NUMERO_MENSAJES_ENVIAR_BLOQUE = 100;
	public static final int MENSAJES_COLUMN_TITULO_MAXLENGTH = 500;
	
	public static final String MENSAJE_ERROR_MENSAJE_NULL = "No se puede insertar un mensaje vacio";
	public static final String MENSAJE_ERROR_NO_EXISTE_DESTINATARIO = "No existe el destinatario";
	public static final String MENSAJE_ERROR_NO_EXISTE_MENSAJE = "No existe el mensaje";
	public static final String MENSAJE_AFINIDAD_OBLIGATORIA = "Afinidad obligatorio";
	public static final String MENSAJE_AFINIDAD_CODNUM_REQUERIDO = "id afinidad no válido";

	public static final String ADJUNTO = "ADJUNTO";
	public static final String BEPMEN_CODNUM = "BEPMEN_CODNUM";
	public static final String BEPUSU_CODNUM = "BEPUSU_CODNUM";
	public static final String CODNUM = "CODNUM";
	public static final String EMAIL = "EMAIL";
	public static final String ESTADO = "ESTADO";
	
	public static final String MENSAJE_ESTADO_BORRADOR = "BORRADOR";
	public static final String MENSAJE_ESTADO_ENVIADO = "ENVIADO";
	public static final String MENSAJE_ESTADO_ENVIANDO = "ENVIANDO";
	
	public static final String DESTINATARIO_ESTADO_ENVIADO = "ENVIADO";
	public static final String DESTINATARIO_ESTADO_SINENVIAR = "SIN ENVIAR";
	public static final String DESTINATARIO_ESTADO_FALLO = "FALLO";
		
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
					mensajes.add(createMensajeFromResultSet(rs, false));
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
	public BolsaEmpleoDataTable<Destinatario> listaDestinatariosMensajeDatatable(Map<String, String[]> params, Mensaje mensaje)
			throws SQLException, UVException, IOException {
		if (mensaje == null) {
			throw new UVException("El mensaje es requerido");
		}
			
		List<Destinatario> destinatarios = new ArrayList<>();
		BolsaEmpleoDataTable<Destinatario> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT bepmde.*"
				+ " FROM TBEP_MEN_DESTINATARIOS bepmde"
				+ " LEFT JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepmde.BEPUSU_CODNUM"
				+ " WHERE bepmde.BEPMEN_CODNUM = ?";
		
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");
		
		if (mensaje.getEstado().equals(MENSAJE_ESTADO_BORRADOR)) {
			dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DOCUMENTO, "bepusu.VUAJA_PRSNIF");
			dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_NOMBRE, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);
			dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_CORREO, "bepusu.VUAJA_EMAIL_ALTA");
			dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_ROL, "bepusu.ROL");
		} else {
			dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DOCUMENTO_ENVIO, "bepusu.VUAJA_PRSNIF");
			dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_NOMBRE_ENVIO, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);
			dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_CORREO_ENVIO, "bepusu.VUAJA_EMAIL_ALTA");
			dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_ROL_ENVIO, "bepusu.ROL");
		}
		
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
					destinatarios.add(this.createDestinatarioFromResultSet(rs));
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

		String consulta = ""
				+ " SELECT bepusu.* "
				+ " FROM TBEP_USUARIOS bepusu "
				+ (dataTable.filterExists(DESTINATARIOS_COLUMN_INDEX_CONVOCATORIA) 
						? "	LEFT JOIN TBEP_SOLICITUDES bepsol ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM" : "")
				+ (dataTable.filterExists(DESTINATARIOS_COLUMN_INDEX_AREA)
						? " LEFT JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM" : "")
				+ "	LEFT JOIN TBEP_MEN_DESTINATARIOS bepmde ON bepusu.CODNUM = bepmde.BEPUSU_CODNUM AND bepmde.BEPMEN_CODNUM = ?"
				+ "	WHERE 1=1 "
				+ "		AND bepusu.FLGBORRADO = 'N' "
				+ " 	AND bepmde.BEPUSU_CODNUM IS NULL "
				+ "		AND bepusu.VUAJA_EMAIL_ALTA IS NOT NULL";
		
		String whereConvocatoria = "(SELECT bepsol2.BEPCON_CODNUM FROM TBEP_SOLICITUDES bepsol2"
				+ "	WHERE bepusu.CODNUM = bepsol2.BEPUSU_CODNUM AND bepsol2.BEPCON_CODNUM = bepsol.BEPCON_CODNUM)";
		
		String whereArea = "(SELECT bepsbo2.BEPBOL_CODNUM FROM TBEP_SOLICITUDES bepsol2"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo2 ON bepsol2.CODNUM = bepsbo2.BEPSOL_CODNUM"
				+ "	WHERE bepusu.CODNUM = bepsol2.BEPUSU_CODNUM AND bepsbo2.BEPBOL_CODNUM = bepsbo.BEPBOL_CODNUM)";
		
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");

		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DOCUMENTO, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_NOMBRE, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_CORREO, "bepusu.VUAJA_EMAIL_ALTA");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_ROL, "bepusu.ROL");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DISTRIBUCION, "bepusu.FLGLISTADISTRIBUCION", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_CONVOCATORIA, whereConvocatoria, DataTableColumn.COLUMN_TYPE_OPTION);
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_AREA, whereArea, DataTableColumn.COLUMN_TYPE_OPTION);
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
	 * @param withFile .
	 * @return Mensaje o null si no existe
	 * @throws IOException .
	 * @throws SQLException .
	 */
	public Mensaje getMensajeById(Integer codNum, boolean withFile) throws SQLException, UVException, IOException {
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

				return this.createMensajeFromResultSet(rs, withFile);
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
	
	/** Añade un nuevo mensaje y devuelve su id .
	 * @param mensaje .
	 * @param usuarioUpdate .
	 * @param conexion .
	 * @return devuelve la id del mensaje agregado .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  si fichero no es válido .
	 */
	public Integer nuevoMensajeConexion(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate, Connection conexion) throws SQLException {
		String consultaInsertMsg = String.format("INSERT INTO TBEP_MENSAJES (%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?)", 
				"TITULO", "CUERPO", "FECHA_CREACION", ESTADO, "ADJUNTO", "UID_USUARIO");
		
		try (PreparedStatement stmt = conexion.prepareStatement(consultaInsertMsg, new String[] {CODNUM})) {
			int parameterIndex = 1;

			stmt.setString(parameterIndex++, mensaje.getTitulo());
			stmt.setClob(parameterIndex++, BolsaEmpleoUtils.stringToClob(mensaje.getCuerpo(), conexion));
			stmt.setDate(parameterIndex++, new java.sql.Date(mensaje.getFechaCreacion().getTime()));
			stmt.setString(parameterIndex++, mensaje.getEstado());
			stmt.setBinaryStream(parameterIndex++, mensaje.getAdjunto());
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
		mensaje.setTitulo(MENSAJE_ESTADO_BORRADOR);
		mensaje.setEstado(MENSAJE_ESTADO_BORRADOR);
		mensaje.setFechaCreacion(BolsaEmpleoUtils.getCurrentDateTime());
		mensaje.setCuerpo(MENSAJE_ESTADO_BORRADOR);
		
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
		
		if (!mensaje.getEstado().contains(MENSAJE_ESTADO_BORRADOR)) {
			throw new UVException("Para actualizar un mensaje debe estar en estado borrador");
		}
		
		String consulta = "UPDATE TBEP_MENSAJES SET TITULO = ?,"
				+ " CUERPO = ?,"
				+ (mensaje.getAdjunto() != null ? " ADJUNTO = ?," : "")
				+ " UID_USUARIO = ?"
				+ " WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setString(indexParam++, mensaje.getTitulo());
			stmt.setString(indexParam++, mensaje.getCuerpo());
			if (mensaje.getAdjunto() != null) {
				stmt.setBlob(indexParam++, mensaje.getAdjunto());
			}
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, mensaje.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Actualiza el estado de un mensaje a enviando .
	 * @param mensaje .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void actualizaEstadoMensajeComoEnviando(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		this.actualizaEstadoMensaje(mensaje, MENSAJE_ESTADO_ENVIANDO, usuarioUpdate);
	}
	
	/** Actualiza el estado de un mensaje a enviado .
	 * @param mensaje .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void actualizaEstadoMensajeComoEnviado(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		this.actualizaEstadoMensaje(mensaje, MENSAJE_ESTADO_ENVIADO, usuarioUpdate);
	}
	
	private void actualizaEstadoMensaje(Mensaje mensaje, String estado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (mensaje == null) {
			throw new UVException(MENSAJE_ERROR_MENSAJE_NULL);
		}
		
		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String consulta = "UPDATE TBEP_MENSAJES SET ESTADO = ?, FECHA_ENVIO = ?, UID_USUARIO = ?"
				+ "	WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setString(indexParam++, estado);
			
			if (estado.equals(MENSAJE_ESTADO_ENVIADO)) {
				stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			} else {
				stmt.setNull(indexParam++, Types.DATE);
			}
			
			stmt.setString(indexParam++, usuarioUp);
			stmt.setInt(indexParam++, mensaje.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Devuelve un destinatario por su id.
	 * @param codNum .
	 * @return destinatario o null si no existe .
	 * @throws IOException .
	 * @throws SQLException .
	 */
	public Destinatario getDestinatarioById(Integer codNum) throws SQLException, UVException, IOException {
		if (codNum == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_DESTINATARIO);
		}
		String consulta = "SELECT bepmde.* FROM TBEP_MEN_DESTINATARIOS bepmde WHERE bepmde.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_DESTINATARIO);
				}
				
				return this.createDestinatarioFromResultSet(rs);
			}
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
	public void agregarDestinatarioUsuario(Mensaje mensaje, UsuarioBolsaEmpleo destinatario, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
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
		
			String consultaInsert = "INSERT INTO TBEP_MEN_DESTINATARIOS (BEPMEN_CODNUM, BEPUSU_CODNUM, UID_USUARIO) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = conexion.prepareStatement(consultaInsert)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, mensaje.getCodNum());
				stmt.setInt(parameterIndex++, destinatario.getCodNum());
				stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
				stmt.executeUpdate();
			}
		}
	}
	
	/** Agrega un email a un mensaje .
	 * @param mensaje .
	 * @param email .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void agregarDestinatarioEmail(Mensaje mensaje, String email, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			String consultaSelect = "SELECT * FROM TBEP_MEN_DESTINATARIOS WHERE BEPMEN_CODNUM = ? AND EMAIL = ?";
			try (PreparedStatement stmt = conexion.prepareStatement(consultaSelect)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, mensaje.getCodNum());
				stmt.setString(parameterIndex++, email);
				
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						throw new UVException(ERROR_DESTINATARIO_YA_EXISTE);
					}
				}
			}
			
			String consultaInsert = "INSERT INTO TBEP_MEN_DESTINATARIOS (BEPMEN_CODNUM, EMAIL, UID_USUARIO) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = conexion.prepareStatement(consultaInsert)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, mensaje.getCodNum());
				stmt.setString(parameterIndex++, email);
				stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
				stmt.executeUpdate();
			}
		}
	}
	
	/** Elimina un destinatario de un mensaje .
	 * @param destinatario .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void eliminarDestinatario(Destinatario destinatario, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				String consultaUpdate = "UPDATE TBEP_MEN_DESTINATARIOS SET UID_USUARIO = ? WHERE CODNUM = ?";
				try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
					int indexParam = 1;
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.setInt(indexParam++, destinatario.getCodNum());
					stmt.executeUpdate();
				}
				
				String consultaDelete = "DELETE FROM TBEP_MEN_DESTINATARIOS WHERE CODNUM = ?";
				try (PreparedStatement stmt = conexion.prepareStatement(consultaDelete)) {
					int indexParam = 1;
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
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void eliminarMensajeBorrador(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate) throws UVException, SQLException, IOException {
		if (mensaje == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_MENSAJE);
		}
		if (!mensaje.getEstado().equals(MENSAJE_ESTADO_BORRADOR)) {
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
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void eliminaDestinatariosMensaje(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate) throws UVException, SQLException, IOException {
		List<Destinatario> listaDestinatarios = obtenerDestinatariosMensaje(mensaje);
		
		for (Destinatario destinatario: listaDestinatarios) {
			this.eliminarDestinatario(destinatario, usuarioUpdate);
		}
	}
	
	/** Obtiene la lista de destinatarios de un mensaje .
	 * @param mensaje .
	 * @return .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<Destinatario> obtenerDestinatariosMensaje(Mensaje mensaje) throws UVException, SQLException, IOException {
		List<Destinatario> usuarios = new ArrayList<>();
		
		if (mensaje == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_MENSAJE);
		}
		
		// limit de 100 en 100 destinatarios.
		String consultaSelect = "SELECT bepmde.* FROM TBEP_MEN_DESTINATARIOS bepmde"
				+ " LEFT JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepmde.BEPUSU_CODNUM"
				+ " WHERE BEPMEN_CODNUM = ? AND ((bepusu.VUAJA_EMAIL_ALTA IS NOT NULL AND bepusu.FLGBORRADO = 'N') OR bepmde.EMAIL IS NOT NULL)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consultaSelect)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, mensaje.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					usuarios.add(this.createDestinatarioFromResultSet(rs));
				}
			}
		}
		
		return usuarios;
	}
	
	/** Actualiza el estado de un destinatario a enviando .
	 * @param destinatario .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void actualizaEstadoDestinatarioComoEnviado(Destinatario destinatario, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		this.actualizaEstadoDestinatario(destinatario, DESTINATARIO_ESTADO_ENVIADO, usuarioUpdate);
	}
	
	/** Actualiza el estado de un destinatario a fallo .
	 * @param destinatario .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void actualizaEstadoDestinatarioComoFallo(Destinatario destinatario, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		this.actualizaEstadoDestinatario(destinatario, DESTINATARIO_ESTADO_FALLO, usuarioUpdate);
	}
	
	/**
	 * Devuelve los destinatarios de mensajes pendientes de envio en bloques.
	 * @return .
	 * @throws IOException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<Destinatario> listaDestinatariosAEnviarEnBloques() throws SQLException, UVException, IOException {
		String consulta = ""
				+ " SELECT bepmde.*"
				+ " FROM TBEP_MEN_DESTINATARIOS bepmde"
				+ " INNER JOIN TBEP_MENSAJES bepmen ON bepmen.CODNUM = bepmde.BEPMEN_CODNUM"
				+ " LEFT JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepmde.BEPUSU_CODNUM"
				+ " WHERE bepmde.ESTADO = '" + ModeloMensajes.DESTINATARIO_ESTADO_SINENVIAR + "'"
				+ "     AND (bepmde.EMAIL IS NOT NULL OR (bepusu.VUAJA_EMAIL_ALTA IS NOT NULL"
				+ "     AND bepusu.FLGBORRADO = 'N'))"
				+ "     AND bepmen.ESTADO = '" + ModeloMensajes.MENSAJE_ESTADO_ENVIANDO + "'"
				+ " FETCH FIRST " + ModeloMensajes.NUMERO_MENSAJES_ENVIAR_BLOQUE + " ROWS ONLY";
		
		List<Destinatario> destinatarios = new ArrayList<>();
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					destinatarios.add(createDestinatarioFromResultSet(rs));
				}
			}
		}

		return destinatarios;
	}
	
	/**
	 * Actualiza el estado del mensaje a enviado si todos los destinatarios del mismos han
	 * sido enviados (o si ha ocurrido un error). 
	 * @param m .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void actualizaEstadoMensajePorDestinatarios(Mensaje m) throws SQLException, UVException {
		String countQuery = ""
				+ " SELECT COUNT(*) AS TOTAL "
				+ " FROM TBEP_MEN_DESTINATARIOS bepmde "
				+ " INNER JOIN TBEP_MENSAJES bepmen ON bepmen.CODNUM = bepmde.BEPMEN_CODNUM "
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepmde.BEPUSU_CODNUM "
				+ " WHERE 1=1 "
				+ "     AND bepmde.ESTADO = '" + ModeloMensajes.DESTINATARIO_ESTADO_SINENVIAR + "' "
				+ "     AND (bepmde.EMAIL IS NOT NULL OR (bepusu.VUAJA_EMAIL_ALTA IS NOT NULL"
				+ "     AND bepusu.FLGBORRADO = 'N'))"
				+ "     AND bepmen.ESTADO = '" + ModeloMensajes.MENSAJE_ESTADO_ENVIANDO + "' "
				+ "     AND bepmen.CODNUM = ? ";
		
		int count = 0;
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(countQuery)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, m.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					count = rs.getInt("TOTAL"); 
				}
			}
		}
		
		if (count == 0) {
			this.actualizaEstadoMensajeComoEnviado(m, null);
		}
	} 
	
	private void actualizaEstadoDestinatario(Destinatario destinatario, String estado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String consulta = "UPDATE TBEP_MEN_DESTINATARIOS SET ESTADO = ?, UID_USUARIO = ?"
				+ " WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setString(indexParam++, estado);
			stmt.setString(indexParam++, usuarioUp);
			stmt.setInt(indexParam++, destinatario.getCodNum());
			stmt.executeUpdate();
		}
	}

	private Mensaje createMensajeFromResultSet(ResultSet rs, boolean withFile) throws SQLException, IOException {
		Mensaje mensaje = new Mensaje();
		mensaje.setCodNum(rs.getInt(CODNUM));
		mensaje.setTitulo(rs.getString("TITULO"));
		mensaje.setCuerpo(BolsaEmpleoUtils.clobToString(rs.getClob("CUERPO")));
		mensaje.setFechaCreacion(rs.getDate("FECHA_CREACION"));
		mensaje.setEstado(rs.getString("ESTADO"));
		
		if (Boolean.TRUE.equals(withFile)) {
			mensaje.setAdjunto(rs.getBlob(ADJUNTO) != null ? rs.getBlob(ADJUNTO).getBinaryStream() : null);
			mensaje.setAdjuntoBytes(rs.getBytes(ADJUNTO));
		}
		
		return mensaje;
	}
	
	private Destinatario createDestinatarioFromResultSet(ResultSet rs) throws SQLException, UVException, IOException {
		Destinatario dest = new Destinatario();
		
		dest.setCodNum(rs.getInt(CODNUM));
		dest.setMensaje(this.getMensajeById(rs.getInt(BEPMEN_CODNUM), false));
		dest.setUsuario(rs.getInt(BEPUSU_CODNUM) != 0 ? ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt(BEPUSU_CODNUM)) : null);
		dest.setEmail(rs.getString(EMAIL));
		dest.setEstado(rs.getString(ESTADO));
		
		return dest;
	}
	
}
