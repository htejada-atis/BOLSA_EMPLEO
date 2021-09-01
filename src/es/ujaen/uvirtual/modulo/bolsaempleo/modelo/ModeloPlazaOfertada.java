package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Contratacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Plantilla;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para las plazas ofertadas para la contratación .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloPlazaOfertada {
	
	public static final int ORDER_COLUMN_INDEX_CODNUM = 0;
	public static final int ORDER_COLUMN_INDEX_AREA = 1;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 2;
	public static final int ORDER_COLUMN_INDEX_FECHA_CREACION = 3;
	public static final int ORDER_COLUMN_INDEX_FECHA_ABIERTA = 4;
	public static final int ORDER_COLUMN_INDEX_FECHA_FIN_OFERTA = 5;
	public static final int ORDER_COLUMN_INDEX_FECHA_CERRADA = 6;
	
	public static final int ORDER_COLUMN_INDEX_NIF_CANDIDATOS = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_CANDIDATOS = 1;
	public static final int ORDER_COLUMN_INDEX_PUNTUACION_CANDIDATOS = 2;
	
	public static final String PLAZA_ESTADO_ABIERTA = "ABIERTA";
	public static final String PLAZA_ESTADO_CERRADA = "CERRADA";
	public static final String PLAZA_ESTADO_CONTRATACION = "CONTRATACION";
	public static final String PLAZA_ESTADO_CREACION = "CREACION";
	public static final String PLAZA_ESTADO_TRAMITACION = "TRAMITACION";
	
	public static final String CENTRO_DESTINO_JAEN = "JAEN";
	public static final String CENTRO_DESTINO_LINARES = "LINARES";
	
	public static final String CUATRIMESTRE_PRIMERO = "1º CUATRIMESTRE";
	public static final String CUATRIMESTRE_SEGUNDO = "2º CUATRIMESTRE";
	public static final String CUATRIMESTRE_TODO_EL_CURSO = "TODO EL CURSO";
	
	public static final String MENSAJE_ERROR_OBJETO_VACIO = "No se puede %s una plaza vacía";
	public static final String MENSAJE_ERROR_PARAM_VACIO = "No se puede %s una plaza sin %s";
	public static final String MENSAJE_ERROR_PLAZA_OFERTADA_ID_NO_EXISTE = "No existe la plaza ofertada con el id indicando";
	public static final String MENSAJE_ERROR_NO_HAY_DESTINATARIOS_DISPONIBLES = "No hay candidatos disponibles para la plaza";
	public static final String MENSAJE_ERROR_PLANTILLA_APERTURA_PLAZA_NO_EXISTE = "La plantilla de apertura de plaza no existe";
	
	public static final String BEPARE_CODNUM = "BEPARE_CODNUM";
	public static final String BEPDED_CODNUM = "BEPDED_CODNUM";
	public static final String CENTRO_DESTINO = "CENTRO_DESTINO";
	public static final String CODNUM = "CODNUM";
	public static final String CUATRIMESTRE = "CUATRIMESTRE";
	public static final String DURACION_PREVISTA = "DURACION_PREVISTA";
	public static final String ESTADO = "ESTADO";
	public static final String FECHA_ABIERTA = "FECHA_ABIERTA";
	public static final String FECHA_CERRADA = "FECHA_CERRADA";
	public static final String FECHA_CREACION = "FECHA_CREACION";
	public static final String FECHA_FIN_OFERTA = "FECHA_FIN_OFERTA";
	public static final String HORARIO = "HORARIO";
	public static final String JUSTIFICACION = "JUSTIFICACION";
	public static final String NRI = "NRI";
	public static final String NRI_FECHA = "NRI_FECHA";
	public static final String OBSERVACIONES_INTERNAS = "OBSERVACIONES_INTERNAS";
	public static final String ABIERTA_VIGENTE = "ABIERTA_VIGENTE";
	
	public static final Map<String, String> CENTROS_DESTINO = new HashMap<>();
	public static final Map<String, String> CUATRIMESTRES = new HashMap<>();
	public static final Map<String, String> ESTADOS = new HashMap<>();
	
	static {
		CENTROS_DESTINO.put(CENTRO_DESTINO_JAEN, "Jaén");
		CENTROS_DESTINO.put(CENTRO_DESTINO_LINARES, "Linares");
		CUATRIMESTRES.put(CUATRIMESTRE_PRIMERO, "1º Cuatrimestre");
		CUATRIMESTRES.put(CUATRIMESTRE_SEGUNDO, "2º Cuatrimestre");
		CUATRIMESTRES.put(CUATRIMESTRE_TODO_EL_CURSO, "Todo el curso");
		ESTADOS.put(PLAZA_ESTADO_ABIERTA, "Abierta");
		ESTADOS.put(PLAZA_ESTADO_CERRADA, "Cerrada");
		ESTADOS.put(PLAZA_ESTADO_CONTRATACION, "Contratación");
		ESTADOS.put(PLAZA_ESTADO_CREACION, "Creación");
		ESTADOS.put(PLAZA_ESTADO_TRAMITACION, "Tramitación");
	}
	
	public static final int COLUMN_DURACION_PREVISTA_MAXLENGTH = 100;
	public static final int COLUMN_JUSTIFICACION_MAXLENGTH = 400;
	
	
	protected static ModeloPlazaOfertada eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloPlazaOfertada();
		}
	}
	
	/**
	 * Obtiene una instancia de la conexión .
	 * @return instancia .
	 */
	public static ModeloPlazaOfertada obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/** Devuelve la lista de plazas ofertadas con estado abierta que tienen una fecha fin mayor a la actual .
	 * @return lista de plazas ofertadas .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public List<PlazaOfertada> listaPlazasOfertadasAContratacion() throws SQLException, UVException {
		List<PlazaOfertada> plazasOfertadas = new ArrayList<>();
		
		String consulta = "SELECT bepplo.*, "
				+ "     CASE"
				+ "         WHEN bepplo.FECHA_FIN_OFERTA > SYSDATE THEN 1"
				+ "         ELSE 0"
				+ "     END AS ABIERTA_VIGENTE"
				+ " FROM TBEP_PLAZAS_OFERTADAS bepplo "
				+ " WHERE bepplo.ESTADO = '" + PLAZA_ESTADO_ABIERTA + "' AND bepplo.FECHA_FIN_OFERTA <= SYSDATE ";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					plazasOfertadas.add(createPlazaOfertadaFromResultSet(rs, false));
				}
			}
		}
		
		return plazasOfertadas;
	}
	
	/** Devuelve una plaza ofertada por el id .
	 * @param codNum .
	 * @return plaza ofertada .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public PlazaOfertada getPlazaOfertadaById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "buscar", "id"));
		}
		
		String consulta = String.format(
				"SELECT bepplo.*,"
				+ "     CASE"
				+ "         WHEN bepplo.FECHA_FIN_OFERTA > SYSDATE THEN 1"
				+ "         ELSE 0"
				+ "     END AS %s"
				+ " FROM TBEP_PLAZAS_OFERTADAS bepplo WHERE %s=?", ABIERTA_VIGENTE, CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_PLAZA_OFERTADA_ID_NO_EXISTE);
				}
				
				return createPlazaOfertadaFromResultSet(rs, true);
			}
		}
	}
	
	/** inserta plaza ofertada .
	 * @param plaza .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	@SuppressWarnings({"checkstyle:npathcomplexity"})
	public void insertaPlazaOfertada(PlazaOfertada plaza, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plaza == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "insertar"));
		}
		
		if (plaza.getArea() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "insertar", "área"));
		}
		
		if (plaza.getJustificacion() == null || plaza.getJustificacion().isBlank()) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "insertar", "justificación"));
		}
		
		if (plaza.getCentroDestino() == null || plaza.getCentroDestino().isBlank()) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "insertar", "centro destino"));
		}
		
		String consulta = "";
		
		if (usuarioUpdate.isServicioPersonal()) {
			consulta = String.format("INSERT INTO TBEP_PLAZAS_OFERTADAS (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
					BEPARE_CODNUM, JUSTIFICACION, CENTRO_DESTINO, FECHA_CREACION, "UID_USUARIO", BEPDED_CODNUM, CUATRIMESTRE, DURACION_PREVISTA, 
					FECHA_FIN_OFERTA, NRI, NRI_FECHA, HORARIO);
		} else {
			consulta = String.format("INSERT INTO TBEP_PLAZAS_OFERTADAS (%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?)", 
					BEPARE_CODNUM, JUSTIFICACION, CENTRO_DESTINO, FECHA_CREACION, "UID_USUARIO", HORARIO);
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, plaza.getArea().getCodNum());
			stmt.setString(parameterIndex++, plaza.getJustificacion());
			stmt.setString(parameterIndex++, plaza.getCentroDestino());
			stmt.setDate(parameterIndex++, new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			if (usuarioUpdate.isServicioPersonal()) {
				stmt.setInt(parameterIndex++, plaza.getDedicacion().getCodNum());
				stmt.setString(parameterIndex++, plaza.getCuatrimestre());
				stmt.setString(parameterIndex++, plaza.getDuracionPrevista());
				stmt.setDate(parameterIndex++, plaza.getFechaFinOferta() != null ? new Date(plaza.getFechaFinOferta().getTime()) : null);
				stmt.setBlob(parameterIndex++, plaza.getNri());
				stmt.setDate(parameterIndex++, plaza.getNri() != null ? new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()) : null);
			}
			stmt.setBlob(parameterIndex++, plaza.getHorario());
			stmt.executeUpdate();
		}
	}
	
	/** actualiza plaza ofertada .
	 * @param plaza .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public void actualizaPlazaOfertada(PlazaOfertada plaza, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plaza == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "actualizar"));
		}
		
		if (plaza.getArea() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "actualizar", "área"));
		}
		
		if (plaza.getJustificacion() == null || plaza.getJustificacion().isBlank()) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "actualizar", "justificación"));
		}
		
		if (plaza.getCentroDestino() == null || plaza.getCentroDestino().isBlank()) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "actualizar", "centro destino"));
		}
		
		String consulta = "";
		
		if (usuarioUpdate.isServicioPersonal()) {
			consulta = String.format("UPDATE TBEP_PLAZAS_OFERTADAS SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? "
					+ (plaza.getHorario() != null ? ", " + HORARIO + "=?" : "")
					+ (plaza.getNri() != null ? ", " + NRI + "=?, " + NRI_FECHA + "=?" : "")
					+ " WHERE %s=?",
					BEPARE_CODNUM, JUSTIFICACION, CENTRO_DESTINO, "UID_USUARIO", BEPDED_CODNUM, CUATRIMESTRE, DURACION_PREVISTA, 
					FECHA_FIN_OFERTA, CODNUM);
		} else {
			consulta = String.format("UPDATE TBEP_PLAZAS_OFERTADAS SET %s=?, %s=?, %s=?, %s=?"
					+ (plaza.getHorario() != null ? ", " + HORARIO + "=?" : "")
					+ " WHERE %s=?", 
					BEPARE_CODNUM, JUSTIFICACION, CENTRO_DESTINO, "UID_USUARIO", CODNUM);
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, plaza.getArea().getCodNum());
			stmt.setString(parameterIndex++, plaza.getJustificacion());
			stmt.setString(parameterIndex++, plaza.getCentroDestino());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			if (usuarioUpdate.isServicioPersonal()) {
				if (plaza.getDedicacion() != null) {
					stmt.setInt(parameterIndex++, plaza.getDedicacion().getCodNum());
				} else {
					stmt.setNull(parameterIndex++, Types.NUMERIC);
				}
				stmt.setString(parameterIndex++, plaza.getCuatrimestre());
				stmt.setString(parameterIndex++, plaza.getDuracionPrevista());
				stmt.setDate(parameterIndex++, plaza.getFechaFinOferta() != null ? new Date(plaza.getFechaFinOferta().getTime()) : null);
				if (plaza.getNri() != null) {
					stmt.setBlob(parameterIndex++, plaza.getNri());
					stmt.setDate(parameterIndex++, new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
				}
			}
			if (plaza.getHorario() != null) {
				stmt.setBlob(parameterIndex++, plaza.getHorario());
			}
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** abrir plaza ofertada .
	 * @param plaza .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void abrirPlazaOfertada(PlazaOfertada plaza, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plaza == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "abrir plaza"));
		}
		
		if (plaza.getFechaFinOferta() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "abrir plaza", "fecha fin oferta"));
		}
		
		String consulta = String.format("UPDATE TBEP_PLAZAS_OFERTADAS SET %s=?, %s=?, %s=?, %s=?"
				+ " WHERE %s=?",
				ESTADO, FECHA_FIN_OFERTA, FECHA_ABIERTA, "UID_USUARIO", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, PLAZA_ESTADO_ABIERTA);
			stmt.setDate(parameterIndex++, new Date(plaza.getFechaFinOferta().getTime()));
			stmt.setDate(parameterIndex++, new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** cerrar plaza ofertada .
	 * @param plaza .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void cerrarPlazaOfertada(PlazaOfertada plaza, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plaza == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "cerrar plaza"));
		}
		
		String consulta = String.format("UPDATE TBEP_PLAZAS_OFERTADAS SET %s=?, %s=?, %s=?"
				+ " WHERE %s=?",
				ESTADO, FECHA_CERRADA, "UID_USUARIO", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, PLAZA_ESTADO_CERRADA);
			stmt.setDate(parameterIndex++, new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** cambia el estado de una plaza a contratación .
	 * @param plaza .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void cambiarEstadoPlazaAContratacion(PlazaOfertada plaza, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plaza == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "cambiar estado a contratación"));
		}
		
		String consulta = String.format("UPDATE TBEP_PLAZAS_OFERTADAS SET %s=?, %s=?"
				+ " WHERE %s=?",
				ESTADO, "UID_USUARIO", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, PLAZA_ESTADO_CONTRATACION);
			stmt.setString(parameterIndex++, usuarioUpdate == null ? "TAREA PROGRAMADA" : usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** comprueba si una plaza está disponible para un candidato .
	 * @param plaza .
	 * @param usuario .
	 * @return boolean si la plaza está disponible o no .
	 * @throws SQLException .
	 */
	public boolean checkPlazaOfertadaCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo usuario) throws SQLException {
		String consulta = "SELECT bepplo.* FROM TBEP_PLAZAS_OFERTADAS bepplo"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.BEPARE_CODNUM = bepplo.BEPARE_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPBOL_CODNUM = bepbol.CODNUM AND bepsob.FECHABAREMACION IS NOT NULL"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsob.BEPSOL_CODNUM = bepsol.CODNUM"
				+ " WHERE   bepsol.BEPUSU_CODNUM = ?"
				+ "     AND bepplo.ESTADO IN ('" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "', '" + ModeloPlazaOfertada.PLAZA_ESTADO_CONTRATACION + "',"
				+ "     '" + ModeloPlazaOfertada.PLAZA_ESTADO_CERRADA + "')"
				+ "     AND bepplo.CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.setInt(indexParam++, plaza.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** Lista de candidatos que han aceptado una plaza .
	 * @param params .
	 * @param plaza .
	 * @return datatable de candidatos .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<OfertaCandidato> listadoCandidatosDisponibles(Map<String, String[]> params, PlazaOfertada plaza)
			throws SQLException, UVException {
		List<OfertaCandidato> rows = new ArrayList<>();
		BolsaEmpleoDataTable<OfertaCandidato> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepusu.CODNUM AS BEPUSU_CODNUM, bepsob.TOTAL AS PUNTUACION, bepofc.CODNUM, bepofc.FLGRESULTADO, "
				+ "     bepofc.FECHA_RESULTADO, bepofc.PREFERENCIA, bepsol.BEPUSU_CODNUM AS BEPUSU_CODNUM, ? AS BEPPLO_CODNUM,"
				+ "     bepcnt.CODNUM AS CONTRATACION"
				+ " FROM TBEP_USUARIOS bepusu"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPUSU_CODNUM = bepusu.CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPSOL_CODNUM = bepsol.CODNUM AND bepsob.BEPBOL_CODNUM = ?"
				+ " LEFT JOIN TBEP_OFERTAS_CANDIDATOS bepofc ON bepofc.BEPUSU_CODNUM = bepusu.CODNUM AND bepofc.BEPPLO_CODNUM = ?"
				+ " LEFT JOIN TBEP_CONTRATACIONES bepcnt ON bepcnt.BEPUSU_CODNUM = bepusu.CODNUM AND bepcnt.BEPPLO_CODNUM = ?"
				+ " WHERE 1=1";
		
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");
				
		dataTable.setColumn(ORDER_COLUMN_INDEX_NIF_CANDIDATOS, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_CANDIDATOS, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_PUNTUACION_CANDIDATOS, "bepsob.TOTAL", DataTableColumn.COLUMN_TYPE_DOUBLE);
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, plaza.getCodNum());
			stmtCount.setInt(paramIndex++, plaza.getCodNum());
			stmt.setInt(paramIndex, plaza.getArea().getCodNum());
			stmtCount.setInt(paramIndex++, plaza.getArea().getCodNum());
			stmt.setInt(paramIndex, plaza.getCodNum());
			stmtCount.setInt(paramIndex++, plaza.getCodNum());
			stmt.setInt(paramIndex, plaza.getCodNum());
			stmtCount.setInt(paramIndex++, plaza.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					OfertaCandidato oferta = ModeloOfertaCandidato.obtenerInstancia().createOfertaCandidatoFromResultSet(rs);
					oferta.setPuntuacion(rs.getDouble("PUNTUACION"));
					rows.add(oferta);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}
		
		return dataTable;
	}
	
	/**
	 * Lista de plazas ofertadas .
	 * @param params .
	 * @param usuario .
	 * @return datatable de plazas ofertadas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<PlazaOfertada> listadoPlazasOfertadas(Map<String, String[]> params, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		List<PlazaOfertada> rows = new ArrayList<>();
		BolsaEmpleoDataTable<PlazaOfertada> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepplo.*,"
				+ "     CASE"
				+ "         WHEN bepplo.FECHA_FIN_OFERTA > SYSDATE THEN 1"
				+ "         ELSE 0"
				+ "     END AS ABIERTA_VIGENTE"
				+ " FROM TBEP_PLAZAS_OFERTADAS bepplo"
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepplo.BEPARE_CODNUM"
				+ (usuario.isDirectorDepartamento()
						? " INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepare.CODNUM AND bepeva.FLGACTIVO = 'S'"
						+ " WHERE bepeva.BEPUSU_CODNUM = ? "
						: " WHERE 1=1 ");
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODNUM, "bepplo.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepplo.ESTADO", DataTableColumn.COLUMN_TYPE_EXACT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA_CREACION, FECHA_CREACION, DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA_ABIERTA, FECHA_ABIERTA, DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA_FIN_OFERTA, FECHA_FIN_OFERTA, DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA_CERRADA, FECHA_CERRADA, DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			if (usuario.isDirectorDepartamento()) {
				stmt.setInt(paramIndex, usuario.getCodNum());
				stmtCount.setInt(paramIndex++, usuario.getCodNum());
			}
			
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					rows.add(createPlazaOfertadaFromResultSet(rs, false));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}
		
		return dataTable;
	}
	
	/** Método para crear un mensaje de apertura de una plaza y ponerlo como pendiente de envío para los candidatos disponibles .
	 * @param plaza .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws IOException .
	 */
	public void crearMensajeAperturaPlaza(PlazaOfertada plaza, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException, IOException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				ModeloPlantilla modeloPlantilla = ModeloPlantilla.obtenerInstancia();
				Plantilla plantilla = new Plantilla();
				
				// Obtenemos la plantilla de la apertura de la plaza si existe
				String consultaSelectPlantilla = "SELECT beppls.* FROM TBEP_PLANTILLAS beppls"
						+ " INNER JOIN TBEP_PARAMETROS_CONFIG beppac ON beppac.VALOR = beppls.CODNUM "
						+ " WHERE beppac.NOMBRE = '" + ModeloParametrosConfiguracion.PARAMETRO_PLANTILLA_APERTURA_PLAZA + "'";
				
				try (PreparedStatement stmt = conexion.prepareStatement(consultaSelectPlantilla)) {
					try (ResultSet rs = stmt.executeQuery()) {
						if (!rs.next()) {
							throw new UVException(MENSAJE_ERROR_PLANTILLA_APERTURA_PLAZA_NO_EXISTE);
						}
						
						plantilla = modeloPlantilla.createPlantillaFromResultSet(rs);
					}
				}
				
				// Creamos un nuevo mensaje con los datos de la plaza en la plantilla
				Mensaje mensaje = new Mensaje(modeloPlantilla.reemplazaPlazaEnPlantilla(plaza, plantilla.getTitulo()), 
						modeloPlantilla.reemplazaPlazaEnPlantilla(plaza, plantilla.getCuerpo()),
						BolsaEmpleoUtils.getCurrentDateTime(), ModeloMensajes.MENSAJE_ESTADO_BORRADOR);
				
				int idMensaje = modeloPlantilla.insertarMensajeDePlantilla(mensaje, usuarioUpdate, conexion);
				
				// Obtenemos los candidatos disponibles y los insertamos para enviar el mensaje de la plaza
				listaDestinatariosPlaza(plaza, idMensaje, usuarioUpdate, conexion);
				
				// Actualizamos el estado del mensaje a 'ENVIANDO'
				String consultaUpdateMsg = String.format("UPDATE TBEP_MENSAJES SET %s=?, %s=? WHERE %s=?",
						"ESTADO", "UID_USUARIO", "CODNUM");
				try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdateMsg)) {
					int indexParam = 1;
					
					stmt.setString(indexParam++, ModeloMensajes.MENSAJE_ESTADO_ENVIANDO);
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.setInt(indexParam++, idMensaje);
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
	
	/** Método para crear un mensaje de cierre de una plaza y ponerlo como pendiente de envío para los candidatos disponibles .
	 * @param plaza .
	 * @param contratacion .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws IOException .
	 */
	public void crearMensajeCierrePlaza(PlazaOfertada plaza, Contratacion contratacion, UsuarioBolsaEmpleo usuarioUpdate)
			throws SQLException, UVException, IOException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				ModeloPlantilla modeloPlantilla = ModeloPlantilla.obtenerInstancia();
				Plantilla plantilla = new Plantilla();
				
				// Obtenemos la plantilla de la apertura de la plaza si existe
				String consultaSelectPlantilla = "SELECT beppls.* FROM TBEP_PLANTILLAS beppls"
						+ " INNER JOIN TBEP_PARAMETROS_CONFIG beppac ON beppac.VALOR = beppls.CODNUM "
						+ " WHERE beppac.NOMBRE = '" + ModeloParametrosConfiguracion.PARAMETRO_PLANTILLA_CIERRE_PLAZA + "'";
				
				try (PreparedStatement stmt = conexion.prepareStatement(consultaSelectPlantilla)) {
					try (ResultSet rs = stmt.executeQuery()) {
						if (!rs.next()) {
							throw new UVException(MENSAJE_ERROR_PLANTILLA_APERTURA_PLAZA_NO_EXISTE);
						}
						
						plantilla = modeloPlantilla.createPlantillaFromResultSet(rs);
					}
				}
				
				// Creamos un nuevo mensaje con los datos de la plaza en la plantilla
				Mensaje mensaje = new Mensaje(modeloPlantilla.reemplazaPlazaEnPlantilla(plaza, plantilla.getTitulo()), 
						modeloPlantilla.reemplazaPlazaYContratacionEnPlantilla(plaza, contratacion, plantilla.getCuerpo()),
						BolsaEmpleoUtils.getCurrentDateTime(), ModeloMensajes.MENSAJE_ESTADO_BORRADOR);
				
				int idMensaje = modeloPlantilla.insertarMensajeDePlantilla(mensaje, usuarioUpdate, conexion);
				
				// Obtenemos los candidatos disponibles y los insertamos para enviar el mensaje de la plaza
				listaDestinatariosPlaza(plaza, idMensaje, usuarioUpdate, conexion);
				
				// Actualizamos el estado del mensaje a 'ENVIANDO'
				String consultaUpdateMsg = String.format("UPDATE TBEP_MENSAJES SET %s=?, %s=? WHERE %s=?",
						"ESTADO", "UID_USUARIO", "CODNUM");
				try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdateMsg)) {
					int indexParam = 1;
					
					stmt.setString(indexParam++, ModeloMensajes.MENSAJE_ESTADO_ENVIANDO);
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.setInt(indexParam++, idMensaje);
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
	
	private void listaDestinatariosPlaza(PlazaOfertada plaza, Integer idMensaje, UsuarioBolsaEmpleo usuarioUpdate, Connection conexion)
			throws SQLException, UVException {
		List<Integer> destinatarios = new ArrayList<>();
		
		String consultaSelectDest = "SELECT bepusu.* FROM TBEP_USUARIOS bepusu"
				+ " LEFT JOIN TBEP_ESTADO_CANDIDATOS bepesc ON bepesc.BEPUSU_CODNUM = bepusu.CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPUSU_CODNUM = bepusu.CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPSOL_CODNUM = bepsol.CODNUM AND bepsob.FECHABAREMACION IS NOT NULL"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsob.BEPBOL_CODNUM"
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ " INNER JOIN TBEP_PLAZAS_OFERTADAS bepplo ON bepplo.BEPARE_CODNUM = bepare.CODNUM"
				+ " WHERE bepplo.CODNUM = ?"
				+ "     AND bepusu.ROL = " + ModeloRol.ID_ROL_CANDIDATO
				+ "     AND (bepesc.CODNUM IS NULL"
				+ "         OR bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_DISPONIBLE + "'"
				+ "         OR (bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_CONTRATADO_PRIMER_CUATRIMESTRE + "' "
				+ "             AND bepplo.CUATRIMESTRE = '" + CUATRIMESTRE_SEGUNDO + "')"
				+ "     )";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consultaSelectDest)) {
			stmt.setInt(1, plaza.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					destinatarios.add(rs.getInt("CODNUM"));
				}
			}
		}
		
		if (destinatarios.size() == 0) {
			throw new UVException(MENSAJE_ERROR_NO_HAY_DESTINATARIOS_DISPONIBLES);
		}
		
		for (Integer idDestinatario: destinatarios) {
			String consultaInsertDest = String.format("INSERT INTO TBEP_MEN_DESTINATARIOS (%s, %s, %s) VALUES (?, ?, ?)", 
					"BEPMEN_CODNUM", "BEPUSU_CODNUM", "UID_USUARIO");
			try (PreparedStatement stmt = conexion.prepareStatement(consultaInsertDest)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, idMensaje);
				stmt.setInt(parameterIndex++, idDestinatario);
				stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
				stmt.executeUpdate();
			}
		}
	}
	
	public PlazaOfertada createPlazaOfertadaFromResultSet(ResultSet rs, Boolean withFiles) throws SQLException, UVException {
		PlazaOfertada plaza = new PlazaOfertada();
		
		plaza.setCodNum(rs.getInt(CODNUM));
		plaza.setEstado(rs.getString(ESTADO));
		plaza.setArea(ModeloArea.obtenerInstancia().getAreaById(rs.getInt(BEPARE_CODNUM)));
		plaza.setDedicacion(rs.getInt(BEPDED_CODNUM) != 0 ? ModeloDedicacion.obtenerInstancia().getDedicacionById(rs.getInt(BEPDED_CODNUM)) : null);
		plaza.setJustificacion(rs.getString(JUSTIFICACION));
		plaza.setCuatrimestre(rs.getString(CUATRIMESTRE));
		plaza.setDuracionPrevista(rs.getString(DURACION_PREVISTA));
		plaza.setCentroDestino(rs.getString(CENTRO_DESTINO));
		plaza.setFechaCreacion(rs.getDate(FECHA_CREACION));
		plaza.setFechaAbierta(rs.getDate(FECHA_ABIERTA));
		plaza.setFechaFinOferta(rs.getDate(FECHA_FIN_OFERTA));
		plaza.setFechaCerrada(rs.getDate(FECHA_CERRADA));
		plaza.setFechaNRI(rs.getDate(NRI_FECHA));
		
		if (withFiles) {
			plaza.setHorario(rs.getBlob(HORARIO) != null ? rs.getBlob(HORARIO).getBinaryStream() : null);
			plaza.setNri(rs.getBlob(NRI) != null ? rs.getBlob(NRI).getBinaryStream() : null);
		}
		
		plaza.setAbiertaVigente(rs.getInt("ABIERTA_VIGENTE") == 1);
		
		return plaza;
	}
	
}