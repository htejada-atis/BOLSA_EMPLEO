package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
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
	public static final int DESTINATARIOS_COLUMN_INDEX_ROL = 4;
	public static final int DESTINATARIOS_COLUMN_INDEX_DISTRIBUCION = 5;
	
	public static final int MENSAJES_COLUMN_TITULO_MAXLENGTH = 500;
	
	public static final String MENSAJE_ERROR_MENSAJE_NULL = "No se puede insertar un mensaje vacio";	
	public static final String MENSAJE_ERROR_NO_EXISTE_MENSAJE = "No existe el mensaje";
	
	public static final String MENSAJE_AFINIDAD_OBLIGATORIA = "Afinidad obligatorio";
	public static final String MENSAJE_AFINIDAD_CODNUM_REQUERIDO = "id afinidad no válido";

	public static final String CODNUM = "CODNUM";
	public static final String ESTADO_BORRADOR = "BORRADOR";
	public static final String ESTADO_ENVIADO = "ENVIADO";

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

		String consulta = "SELECT bepusu.* FROM TBEP_MEN_DESTINATARIOS bepmde "
				+ "INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepmde.BEPUSU_CODNUM "
				+ "WHERE bepmde.BEPMEN_CODNUM = ?";

		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DOCUMENTO, "bepusu.PRSNIF");
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

		String consulta = "SELECT bepusu.* FROM TBEP_USUARIOS bepusu WHERE bepusu.FLGBORRADO = 'N'";

		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DOCUMENTO, "bepusu.PRSNIF");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_ROL, "bepusu.ROL");
		dataTable.setColumn(DESTINATARIOS_COLUMN_INDEX_DISTRIBUCION, "bepusu.FLGLISTADISTRIBUCION", DataTableColumn.COLUMN_TYPE_BOOLEAN);		
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			int indexParams = 1;
//			stmt.setInt(indexParams, mensaje.getCodNum());
//			stmtCount.setInt(indexParams++, mensaje.getCodNum());			
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
				conexion.setAutoCommit(true);
			} catch (Exception e) {
				conexion.rollback();
				conexion.setAutoCommit(true);
				throw e;
			}
		}
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
