package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de convocatorias Modelo - Operaciones con
 * nombres: lista, actualiza, borra, inserta Controlador - Opers. con nombres:
 * obtener, cambiar, eliminar, agregar
 * 
 * @author ATISoluciones 2021
 */
public class ModeloConvocatoria {
	public static final int ORDER_COLUMN_INDEX_ID = 0;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 1;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 2;
	public static final int ORDER_COLUMN_INDEX_FECHACIERRE = 3;
	
	public static final String CONVOCATORIA_ESTADO_ABIERTA = "ABIERTA";
	public static final String CONVOCATORIA_ESTADO_CERRADA = "CERRADA";
	public static final String CONVOCATORIA_ESTADO_FINALIZADA = "FINALIZADA";
	
	public static final int COLUMN_DESCRIPCION_MAXLENGTH = 150;
	
	public static final String MENSAJE_ERROR_NO_EXISTE_CONVOCATORIA = "No existe la convocatoria";
	
	protected static ModeloConvocatoria eInstancia;
	
	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloConvocatoria();
		}
	}
	
	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloConvocatoria obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/**
	 * Listado de bolsas de convocatorias.
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  error si no existe la area
	 */
	public BolsaEmpleoDataTable<Convocatoria> listaConvocatoriasDatatable(Map<String, String[]> params)
			throws SQLException, UVException {
		List<Convocatoria> data = new ArrayList<>();
		BolsaEmpleoDataTable<Convocatoria> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepcon.* FROM TBEP_CONVOCATORIAS bepcon WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepcon.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepcon.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepcon.ESTADO", DataTableColumn.COLUMN_TYPE_EXACT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHACIERRE, "bepcon.FECHACIERRE");
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					data.add(this.createFromResultSet(rs));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}
		
		return dataTable;
	}
	
	/**
	 * Comprueba si todas las convocatorias estan finalizadas. Error en otro caso.
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void chequearCreacionConvocatoria() throws SQLException, UVException {
		String consulta = "SELECT bepcon.* FROM TBEP_CONVOCATORIAS bepcon";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Convocatoria c = this.createFromResultSet(rs);
					if (!c.getEstado().equals(CONVOCATORIA_ESTADO_FINALIZADA)) {
						throw new UVException("No se puede crear. Existen convocatorias no finalizadas");
					}
				}
			}
		}
	}
	
	/** Consulta convocatorias en BBDD y las devuelve .
	 * @return todas las convocatorias de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public List<Convocatoria> listaConvocatorias() throws SQLException {
		List<Convocatoria> convocatorias = new ArrayList<>();
		String consulta = "SELECT bepcon.* FROM TBEP_CONVOCATORIAS bepcon ORDER BY CODNUM";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					convocatorias.add(this.createFromResultSet(rs));
				}
			}
		}
		
		return convocatorias;
	}
	
	/** Lista de convocatorias en las que participa un candidato además de la última convocatoria .
	 * @param candidato .
	 * @return convocatorias .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public List<Convocatoria> listaConvocatoriasCandidato(UsuarioBolsaEmpleo candidato) throws SQLException {
		List<Convocatoria> convocatorias = new ArrayList<>();
		String consulta = "SELECT bepcon.* FROM TBEP_CONVOCATORIAS bepcon"
				+ " LEFT JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPCON_CODNUM = bepcon.CODNUM AND bepsol.BEPUSU_CODNUM = ?"
				+ " WHERE bepsol.CODNUM IS NOT NULL OR bepcon.CODNUM = ?"
				+ " ORDER BY bepcon.CODNUM";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setInt(parameterIndex++, this.getUltimaConvocatoria().getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					convocatorias.add(this.createFromResultSet(rs));
				}
			}
		}
		
		return convocatorias;
	}

	/**
	 * Lista de convocatorias a cerrar por fecha de cierre.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<Convocatoria> listaConvocatoriasACerrar() throws SQLException {
		List<Convocatoria> convocatorias = new ArrayList<>();
		String consulta = ""
				+ " SELECT bepcon.* "
				+ " FROM TBEP_CONVOCATORIAS bepcon "
				+ " WHERE bepcon.ESTADO = 'ABIERTA' AND bepcon.FECHACIERRE <= SYSDATE ";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					convocatorias.add(this.createFromResultSet(rs));
				}
			}
		}

		return convocatorias;
	}
	
	/**
	 * Añade una convocatoria al sistema cerrada.
	 * 
	 * @param convocatoria .
	 * @param usuarioInsert .
	 * @return .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Integer nuevaConvocatoria(Convocatoria convocatoria, UsuarioBolsaEmpleo usuarioInsert) throws SQLException, UVException {
		if (convocatoria == null) {
			throw new UVException("No se puede insertar una convocatoria vacio");
		}

		String consulta = "INSERT INTO TBEP_CONVOCATORIAS (DESCRIPCION, FECHACIERRE, ESTADO, NUMBOLSASMAXIMO, NUMMERITOSPORBLOQUE, UID_USUARIO) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{"CODNUM"})) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, convocatoria.getDescripcion());
			stmt.setDate(parameterIndex++, new java.sql.Date(convocatoria.getFechaCierre().getTime()));
			stmt.setString(parameterIndex++, convocatoria.getEstado());
			stmt.setInt(parameterIndex++, convocatoria.getNumBolsasMaximo());
			stmt.setInt(parameterIndex++, convocatoria.getNumMeritosPorBloque());
			stmt.setString(parameterIndex++, usuarioInsert.getCodCuenta());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			
			return rs.getInt(1);
		}
	}

	/**
	 * Devuelve un convocatoria por su pk.
	 * 
	 * @param codNum .
	 * @return Convocatoria o null si no existe
	 * @throws SQLException .
	 */
	public Convocatoria getConvocatoriaById(int codNum) throws SQLException, UVException {
		String consulta = "SELECT bepcon.*"
				+ " FROM TBEP_CONVOCATORIAS bepcon"
				+ " WHERE bepcon.CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_CONVOCATORIA);
				}
				
				return createFromResultSet(rs);
			}
		}
	}

	/**
	 * Devuelve numero de solicitudes asociadas a convocatoria .
	 * 
	 * @param codNum .
	 * @return Convocatoria o null si no existe
	 * @throws SQLException .
	 */
	public Integer getNumSolicitudesByConvocatoriaId(int codNum) throws SQLException {
		String consulta = "SELECT COUNT(*) AS total FROM TBEP_SOLICITUDES bepsol WHERE bepsol.BEPCON_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return rs.getInt("total");
				}
			}
		}
		return null;
	}
	
	/** Devuelve si una convocatoria contiene méritos sin validar .
	 * @param convocatoria .
	 * @return true o false .
	 * @throws SQLException .
	 */
	public boolean contieneMeritosSinValidar(Convocatoria convocatoria) throws SQLException {
		String consulta = "SELECT COUNT(*) COUNT_SIN_VALIDAR"
				+ " FROM TBEP_SOLICITUDES bepsol"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.BEPSOL_CODNUM = bepsol.CODNUM"
				+ " INNER JOIN TBEP_SOL_BOL_MERITOS bepsbm ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM"
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ " WHERE bepsol.BEPCON_CODNUM = ?"
				+ "     AND bepusu.ROL = " + ModeloRol.ID_ROL_CANDIDATO
				+ "     AND bepsol.ESTADO = 'CERRADA' "
				+ "     AND bepusu.FLGBORRADO = 'N'"
				+ "     AND bepsbm.FLGVALIDADO = 'N'"
				+ "     AND bepsbm.FLGEXCLUIDO = 'N'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, convocatoria.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next() && rs.getInt("COUNT_SIN_VALIDAR") > 0) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Actualiza una convocatoria.
	 * 
	 * @param conv Convocatoria con los datos nuevos a actualizar
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  en caso de errores de validacion
	 */
	public void actualizaConvocatoria(Convocatoria conv, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		validateConvocatoria(conv);

		String consulta = "UPDATE tbep_convocatorias SET DESCRIPCION=?, FECHACIERRE=?, ESTADO=?, NUMBOLSASMAXIMO=?, NUMMERITOSPORBLOQUE=?, UID_USUARIO=? "
				+ " WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, conv.getDescripcion());
			stmt.setDate(parameterIndex++, new java.sql.Date(conv.getFechaCierre().getTime()));
			stmt.setString(parameterIndex++, conv.getEstado());
			stmt.setInt(parameterIndex++, conv.getNumBolsasMaximo());
			stmt.setInt(parameterIndex++, conv.getNumMeritosPorBloque());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, conv.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * Cambia el estado de una convocatoria.
	 * 
	 * @param conv Convocatoria con los datos a actualizar
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  en caso de errores de validacion
	 */
	public void cambiaEstadoConvocatoria(Convocatoria conv, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		validateConvocatoria(conv);
		
		if (conv.getEstado().equals(CONVOCATORIA_ESTADO_CERRADA)) {
			this.cerrarConvocatoria(conv, usuarioUpdate);
		} else {
			String consulta = "UPDATE tbep_convocatorias SET ESTADO=?,UID_USUARIO=? WHERE codnum=?";
			try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int parameterIndex = 1;
				stmt.setString(parameterIndex++, conv.getEstado());
				stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
				stmt.setInt(parameterIndex++, conv.getCodNum());
				stmt.executeUpdate();
			}
		}
	}

	/**
	 * Elimina una convocatoria.
	 * 
	 * @param conv convocatoria a borrar
	 * @param usuarioDetele .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  si convocatoria no es valida
	 */
	public void borraConvocatoria(Convocatoria conv, UsuarioBolsaEmpleo usuarioDetele) throws SQLException, UVException {
		validateConvocatoria(conv);
		
		if (this.getNumSolicitudesByConvocatoriaId(conv.getCodNum()) > 0) {
			throw new UVException("No se puede eliminar, hay solicitudes en la convocatoria");
		}
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			String consultaUpdate = "UPDATE tbep_convocatorias SET UID_USUARIO=? WHERE codnum = ?";
			try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
				int parameterIndex = 1;
				stmt.setString(parameterIndex++, usuarioDetele.getCodCuenta());
				stmt.setInt(parameterIndex++, conv.getCodNum());
				stmt.executeUpdate();
			}
			
			String consulta = "DELETE FROM tbep_convocatorias WHERE codnum = ?";
			try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, conv.getCodNum());
				stmt.executeUpdate();
			}
		}
	}
	
	/**
	 * Devuelve la última convocatoria o null si no hay.
	 * 
	 * @return .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Convocatoria getUltimaConvocatoria() throws SQLException {
		String consulta = "SELECT bepcon.* FROM TBEP_CONVOCATORIAS bepcon ORDER BY bepcon.CODNUM DESC FETCH FIRST 1 ROW ONLY";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createFromResultSet(rs);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Devuelve la última convocatoria finalizada o null si no hay.
	 * 
	 * @return .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Convocatoria getUltimaConvocatoriaFinalizada() throws SQLException {
		String consulta = String.format("SELECT * FROM TBEP_CONVOCATORIAS WHERE ESTADO = '%s' ORDER BY CODNUM DESC FETCH FIRST 1 ROW ONLY",
				CONVOCATORIA_ESTADO_FINALIZADA);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createFromResultSet(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Comprueba si las bolsas estan desbloqueadas.
	 * 
	 * @return bool .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public boolean checkBolsasDesbloqueadas() throws SQLException {
		String sql = "SELECT ESTADO FROM TBEP_BOLSAS";

		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					if (!rs.getString("ESTADO").equals(ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Cierra la convocatoria, y ajusta el estado de las bolsas.
	 * 
	 * @param conv convocatoria a cerrar .
	 * @param usuario usuario que cierra la convocatoria, si es null, se cierra desde el cron.
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void cerrarConvocatoria(Convocatoria conv, UsuarioBolsaEmpleo usuario) throws UVException, SQLException {
		validateConvocatoria(conv);
		
		String usuarioUpdate = usuario != null ? usuario.getCodCuenta() : "TAREA_PROGRAMADA";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				// cambiamos estado de la convocatoria
				String updateConvocatoria = "UPDATE TBEP_CONVOCATORIAS SET ESTADO=?, UID_USUARIO=? WHERE CODNUM=?";
				try (PreparedStatement stmt = conexion.prepareStatement(updateConvocatoria)) {
					int parameterIndex = 1;
					stmt.setString(parameterIndex++, CONVOCATORIA_ESTADO_CERRADA);
					stmt.setString(parameterIndex++, usuarioUpdate);
					stmt.setInt(parameterIndex++, conv.getCodNum());
					stmt.executeUpdate();
				}
				
				// cambiamos estado de las bolsas a revisión
				String updateBolsas = "UPDATE TBEP_BOLSAS SET ESTADO=?, UID_USUARIO=?";
				try (PreparedStatement stmt = conexion.prepareStatement(updateBolsas)) {
					int parameterIndex = 1;
					stmt.setString(parameterIndex++, ModeloBolsa.BOLSA_ESTADO_REVISION);
					stmt.setString(parameterIndex++, usuarioUpdate);
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
	 * Finaliza la convocatoria.
	 * @param conv .
	 * @param usuario .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void finalizarConvocatoria(Convocatoria conv, UsuarioBolsaEmpleo usuario) throws UVException, SQLException {
		validateConvocatoria(conv);
		
		if (!conv.getEstado().equals(CONVOCATORIA_ESTADO_CERRADA)) {
			throw new UVException("La convocatoria no está cerrada");
		}
		
		String usuarioUpdate = usuario != null ? usuario.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String consulta = "UPDATE tbep_convocatorias SET ESTADO=?, UID_USUARIO=?, FECHA_FINALIZACION=? WHERE codnum=?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, CONVOCATORIA_ESTADO_FINALIZADA);
			stmt.setString(parameterIndex++, usuarioUpdate);
			stmt.setDate(parameterIndex++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setInt(parameterIndex++, conv.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Cambia el estado a cerrada una convocatoria finalizada.
	 * 
	 * @param conv .
	 * @param usuario usuario que cierra la convocatoria, si es null, se cierra desde el cron.
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void ponerEnCerradaConvocatoriaFinalizada(Convocatoria conv, UsuarioBolsaEmpleo usuario) throws UVException, SQLException {
		validateConvocatoria(conv);
		
		if (!conv.getEstado().equals(CONVOCATORIA_ESTADO_FINALIZADA)) {
			throw new UVException("La convocatoria no está finalizada");
		}
		
		String usuarioUpdate = usuario != null ? usuario.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String consulta = "UPDATE tbep_convocatorias SET ESTADO=?, UID_USUARIO=? WHERE codnum=?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, CONVOCATORIA_ESTADO_CERRADA);
			stmt.setString(parameterIndex++, usuarioUpdate);
			stmt.setInt(parameterIndex++, conv.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * Devuelve si la convocatoria está cerrada o no.
	 * @param c conv.
	 * @return .
	 */
	public boolean isConvocatoriaCerrada(Convocatoria c) {
		return c.getEstado().equals(CONVOCATORIA_ESTADO_CERRADA) 
				|| c.getFechaCierre().before(BolsaEmpleoUtils.getCurrentDateTime());
	}
	
	/** Crea una convocatoria a partir de un resulset.
	 * @param rs .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public Convocatoria createFromResultSet(ResultSet rs) throws SQLException {
		Convocatoria convocatoria = new Convocatoria();
		convocatoria.setCodNum(rs.getInt("CODNUM"));
		convocatoria.setDescripcion(rs.getString("DESCRIPCION"));
		convocatoria.setFechaCierre(rs.getDate("FECHACIERRE"));
		convocatoria.setFechaFinalizacion(rs.getDate("FECHA_FINALIZACION"));
		convocatoria.setEstado(rs.getString("ESTADO"));
		convocatoria.setNumBolsasMaximo(rs.getInt("NUMBOLSASMAXIMO"));
		convocatoria.setNumMeritosPorBloque(rs.getInt("NUMMERITOSPORBLOQUE"));
		return convocatoria;
	}

	private void validateConvocatoria(Convocatoria conv) throws UVException {
		if (conv == null) {
			throw new UVException("Convocatoria obligatorio");
		}
		if (conv.getCodNum() == null) {
			throw new UVException("id convocatoria no válido");
		}
	}
}
