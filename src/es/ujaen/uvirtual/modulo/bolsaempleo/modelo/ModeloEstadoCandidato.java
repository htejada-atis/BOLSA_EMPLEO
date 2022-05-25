package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoEstado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión del estado de los candidatos . 
 * @author ATISoluciones 2021
 */
public class ModeloEstadoCandidato {
	
	public static final int ORDER_COLUMN_INDEX_AREA = 1;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 2;
	public static final int ORDER_COLUMN_INDEX_PLAZA = 3;
	
	public static final String ESTADO_DISPONIBLE = "DISPONIBLE";
	public static final String ESTADO_CONTRATADO = "CONTRATADO";
	public static final String ESTADO_CONTRATADO_PRIMER_CUATRIMESTRE = "CONTRATADO 1º CUATRIMESTRE";
	public static final String ESTADO_CONTRATADO_PARCIAL = "CONTRATADO PARCIAL";
	public static final String ESTADO_SUSPENSION_PROVISIONAL = "SUSPENSION PROVISIONAL";
	public static final String ESTADO_NO_DISPONIBLE = "NO DISPONIBLE";
	public static final Map<String, String> ESTADOS = new HashMap<>();
	
	public static final String BEPBOL_CODNUM = "BEPBOL_CODNUM";
	public static final String BEPPLO_CODNUM = "BEPPLO_CODNUM";
	public static final String BEPUSU_CODNUM = "BEPUSU_CODNUM";
	public static final String CODNUM = "CODNUM";
	public static final String CONTRATOS = "CONTRATOS";
	public static final String DISPONIBILIDAD = "DISPONIBILIDAD";
	public static final String ESTADO = "ESTADO";
	
	public static final String MENSAJE_ERROR_OBJETO_VACIO = "No se puede %s un estado candidato vacía";
	public static final String MENSAJE_ERROR_PARAM_VACIO = "No se puede %s un estado candidato sin %s";
	
	static {
		ESTADOS.put(ESTADO_DISPONIBLE, "Disponible");
		ESTADOS.put(ESTADO_CONTRATADO, "Contratado");
		ESTADOS.put(ESTADO_CONTRATADO_PRIMER_CUATRIMESTRE, "Contratado 1º cuatrimestre");
		ESTADOS.put(ESTADO_CONTRATADO_PARCIAL, "Contratado parcial");
		ESTADOS.put(ESTADO_SUSPENSION_PROVISIONAL, "Suspensión provisional");
		ESTADOS.put(ESTADO_NO_DISPONIBLE, "No disponible");
	}

	protected static ModeloEstadoCandidato eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloEstadoCandidato();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloEstadoCandidato obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	
	/** Obtener un estado candidato .
	 * @param candidato .
	 * @param bolsa .
	 * @return candidato estado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public CandidatoEstado getEstadoCandidatoByUsuarioBolsa(UsuarioBolsaEmpleo candidato, Bolsa bolsa) throws SQLException, UVException {
		String consulta = "SELECT bepesc.* FROM TBEP_ESTADO_CANDIDATOS bepesc "
				+ " WHERE bepesc.BEPUSU_CODNUM = ? AND bepesc.BEPBOL_CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, candidato.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return this.createCandidatoEstadoFromResultSet(rs, null, false);
				}
			}
		}
		
		return null;
	}
	
	/** Listado de estados de un candidato .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param candidato .
	 * @return datatable .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public BolsaEmpleoDataTable<CandidatoEstado> listaEstadosCandidato(Map<String, String[]> params, UsuarioBolsaEmpleo candidato)
			throws SQLException, UVException {
		List<CandidatoEstado> estados = new ArrayList<>();
		BolsaEmpleoDataTable<CandidatoEstado> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepbol.CODNUM AS BEPBOL_CODNUM, bepplo.CODNUM AS BEPPLO_CODNUM, bepesc.CODNUM,"
				+ "     bepesc.ESTADO, bepesc.BEPUSU_CODNUM"
				+ " FROM TBEP_BOLSAS bepbol "
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ " LEFT JOIN TBEP_ESTADO_CANDIDATOS bepesc ON bepesc.BEPBOL_CODNUM = bepbol.CODNUM AND bepesc.BEPUSU_CODNUM = ?"
				+ " LEFT JOIN TBEP_PLAZAS_OFERTADAS bepplo ON bepplo.CODNUM = bepesc.BEPPLO_CODNUM"
				+ " WHERE 1=1";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepesc.ESTADO", DataTableColumn.COLUMN_TYPE_EXACT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_PLAZA, "bepplo.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int indexParam = 1;
			stmt.setInt(indexParam, candidato.getCodNum());
			stmtCount.setInt(indexParam++, candidato.getCodNum());
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					estados.add(this.createCandidatoEstadoFromResultSet(rs, candidato, false));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(estados);
		}
		
		return dataTable;
	}
	
	/** Listado de candidatos disponibles para una plaza .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param plaza .
	 * @return datatable .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public BolsaEmpleoDataTable<CandidatoEstado> listaEstadosCandidatoDisponiblesPlaza(Map<String, String[]> params, PlazaOfertada plaza)
			throws SQLException, UVException {
		List<CandidatoEstado> estados = new ArrayList<>();
		BolsaEmpleoDataTable<CandidatoEstado> dataTable = new BolsaEmpleoDataTable<>(params);
		
		Bolsa bolsa = ModeloPlazaOfertada.obtenerInstancia().getBolsaConContracionHabilitadaPlaza(plaza);
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaByCurso(plaza.getCurso());
		if (bolsa == null || convocatoria == null) {
			dataTable.setData(estados);
			return dataTable;
		}
		
		String consulta = this.getQueryCandidatosEstadoPlaza();
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int indexParam = 1;
			stmt.setInt(indexParam, convocatoria.getCodNum());
			stmtCount.setInt(indexParam++, convocatoria.getCodNum());
			
			stmt.setInt(indexParam, bolsa.getCodNum());
			stmtCount.setInt(indexParam++, bolsa.getCodNum());
			
			stmt.setInt(indexParam, plaza.getCodNum());
			stmtCount.setInt(indexParam++, plaza.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					estados.add(this.createCandidatoEstadoFromResultSet(rs, null, true));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(estados);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado de candidatos disponibles para la plaza.
	 * @param plaza .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<CandidatoEstado> listaEstadosCandidatoPlazaDisponibles(PlazaOfertada plaza) throws SQLException, UVException {
		List<CandidatoEstado> estados = new ArrayList<>();
		
		Bolsa bolsa = ModeloPlazaOfertada.obtenerInstancia().getBolsaConContracionHabilitadaPlaza(plaza);
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaByCurso(plaza.getCurso());
		if (bolsa == null || convocatoria == null) {
			return estados;
		}
		
		String consulta = this.getQueryCandidatosEstadoPlaza();
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, convocatoria.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, plaza.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					CandidatoEstado c = this.createCandidatoEstadoFromResultSet(rs, null, true);
					if (c.isDisponibilidad()) {
						estados.add(c);
					}
				}
			}
		}
		
		return estados;
	}
	
	/** Cambiar estados del candidato para distintas bolsas .
	 * @param candidato .
	 * @param estado .
	 * @param bolsas .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public void cambiarEstadosCandidato(CandidatoEstado candidato, String estado, List<Bolsa> bolsas, UsuarioBolsaEmpleo usuarioUpdate)
			throws SQLException, UVException {
		for (Bolsa bolsa: bolsas) {
			this.cambiarEstadoCandidato(candidato, estado, bolsa, usuarioUpdate);
		}
	}
	
	/** Cambiar estado del candidato para una bolsa .
	 * @param candidato .
	 * @param estado .
	 * @param bolsa .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public void cambiarEstadoCandidato(CandidatoEstado candidato, String estado, Bolsa bolsa, UsuarioBolsaEmpleo usuarioUpdate)
			throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				// comprobamos si el estado está creado para actualizar o crear
				CandidatoEstado candidatoEstado = this.getEstadoCandidatoByUsuarioBolsa(candidato, bolsa);
				
				if (candidatoEstado == null) {
					candidatoEstado = new CandidatoEstado(candidato);
					candidatoEstado.setEstado(estado);
					candidatoEstado.setBolsa(bolsa);
					candidatoEstado.setPlaza(candidato.getPlaza());
					insertaEstadoCandidato(candidatoEstado, usuarioUpdate, conexion);
				} else {
					candidatoEstado.setEstado(estado);
					
					if (estado.equals(ESTADO_DISPONIBLE) || estado.equals(ESTADO_NO_DISPONIBLE) || estado.equals(ESTADO_SUSPENSION_PROVISIONAL)) {
						candidatoEstado.setPlaza(null);
					} else {
						candidatoEstado.setPlaza(candidato.getPlaza());
					}
					
					actualizaEstadoCandidato(candidatoEstado, usuarioUpdate, conexion);
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
	
	/** inserta estado candidato .
	 * @param candidatoEstado .
	 * @param usuarioUpdate .
	 * @param conexion .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	@SuppressWarnings({"checkstyle:npathcomplexity"})
	public void insertaEstadoCandidato(CandidatoEstado candidatoEstado, UsuarioBolsaEmpleo usuarioUpdate, Connection conexion) throws SQLException, UVException {
		if (candidatoEstado == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "insertar"));
		}
		
		if (candidatoEstado.getBolsa() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "insertar", "bolsa"));
		}
		
		if (candidatoEstado.getCodNum() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "insertar", "candidato"));
		}
		
		String consulta = String.format("INSERT INTO TBEP_ESTADO_CANDIDATOS (%s,%s,%s,%s,%s) VALUES (?,?,?,?,?)",
				BEPBOL_CODNUM, BEPUSU_CODNUM, BEPPLO_CODNUM, ESTADO, "UID_USUARIO");
		
		if (conexion == null) {
			conexion = ConexionUvirtual.obtenerInstancia();
		}
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidatoEstado.getBolsa().getCodNum());
			stmt.setInt(parameterIndex++, candidatoEstado.getCodNum());
			
			if (candidatoEstado.getPlaza() != null) {
				stmt.setInt(parameterIndex++, candidatoEstado.getPlaza().getCodNum());
			} else {
				stmt.setNull(parameterIndex++, Types.NUMERIC);
			}
			
			stmt.setString(parameterIndex++, candidatoEstado.getEstado() != null ? candidatoEstado.getEstado() : ESTADO_DISPONIBLE);
			stmt.setString(parameterIndex++, usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA PROGRAMADA");
			stmt.executeUpdate();
		}
	}
	
	/** actualiza plaza ofertada .
	 * @param candidatoEstado .
	 * @param usuarioUpdate .
	 * @param conexion .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public void actualizaEstadoCandidato(CandidatoEstado candidatoEstado, UsuarioBolsaEmpleo usuarioUpdate, Connection conexion) throws SQLException, UVException {
		if (candidatoEstado == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "insertar"));
		}
		
		if (candidatoEstado.getBolsa() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "insertar", "bolsa"));
		}
		
		if (candidatoEstado.getCodNum() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "insertar", "candidato"));
		}
		
		String consulta = String.format("UPDATE TBEP_ESTADO_CANDIDATOS SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
				BEPBOL_CODNUM, BEPUSU_CODNUM, BEPPLO_CODNUM, ESTADO, "UID_USUARIO", CODNUM);
		
		if (conexion == null) {
			conexion = ConexionUvirtual.obtenerInstancia();
		}
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidatoEstado.getBolsa().getCodNum());
			stmt.setInt(parameterIndex++, candidatoEstado.getCodNum());
			
			if (candidatoEstado.getPlaza() != null) {
				stmt.setInt(parameterIndex++, candidatoEstado.getPlaza().getCodNum());
			} else {
				stmt.setNull(parameterIndex++, Types.NUMERIC);
			}
			
			stmt.setString(parameterIndex++, candidatoEstado.getEstado() != null ? candidatoEstado.getEstado() : ESTADO_DISPONIBLE);
			stmt.setString(parameterIndex++, usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA PROGRAMADA");
			stmt.setInt(parameterIndex++, candidatoEstado.getCodNumEstado());
			stmt.executeUpdate();
		}
	}
	
	/** Crea un candidato estado de un ResultSet.
	 * @param rs .
	 * @param usuario .
	 * @param extraParams .
	 * @return candidato estado .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public CandidatoEstado createCandidatoEstadoFromResultSet(ResultSet rs, UsuarioBolsaEmpleo usuario, boolean extraParams) throws SQLException, UVException {
		CandidatoEstado candidato = new CandidatoEstado(usuario == null 
				? ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt(BEPUSU_CODNUM)) : usuario);
		candidato.setCodNumEstado(rs.getInt(CODNUM));
		candidato.setBolsa(ModeloBolsa.obtenerInstancia().getBolsaById(rs.getInt(BEPBOL_CODNUM)));
		candidato.setPlaza(rs.getInt(BEPPLO_CODNUM) != 0 ? ModeloPlazaOfertada.obtenerInstancia().getPlazaOfertadaById(rs.getInt(BEPPLO_CODNUM)) : null);
		candidato.setEstado(rs.getString(ESTADO));
		
		if (extraParams) {
			candidato.setContratos(rs.getInt(CONTRATOS));
			candidato.setDisponibilidad(rs.getBoolean(DISPONIBILIDAD));
		}
		
		return candidato;
	}
	
	/**
	 * Devuelve la query de estados de candidatos para una plaza.
	 * @return .
	 */
	public String getQueryCandidatosEstadoPlaza() {
		String estadosCandidato = "'" + ModeloEstadoCandidato.ESTADO_DISPONIBLE + "','" 
				+ ModeloEstadoCandidato.ESTADO_SUSPENSION_PROVISIONAL + "','" 
				+ ModeloEstadoCandidato.ESTADO_NO_DISPONIBLE + "'";
				
		return ""
			+ " SELECT DISTINCT bepusu.CODNUM AS BEPUSU_CODNUM, bepesc.CODNUM, bepesc.ESTADO, bepplo.CODNUM AS BEPPLO_CODNUM, "
			+ "		bepbol.CODNUM AS BEPBOL_CODNUM, bepcts.CONTRATOS, bepofc.CODNUM AS BEPOFC_CODNUM, bepsob.TOTAL AS PUNTUACION, "
			+ "     CASE"
			+ "         WHEN bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_CONTRATADO_PRIMER_CUATRIMESTRE + "'"
			+ "             AND bepplo.CUATRIMESTRE = '" + ModeloPlazaOfertada.CUATRIMESTRE_SEGUNDO + "' THEN 1"
			+ "         WHEN bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_CONTRATADO_PARCIAL + "'"
			+ "             AND bepded.TIPO = '" + ModeloDedicacion.TIPO_TIEMPO_PARCIAL + "' THEN 1"
			+ "         WHEN bepcts.CONTRATOS > 0 THEN 0"
			+ "         WHEN bepesc.CODNUM IS NULL THEN 1"
			+ "         WHEN bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_DISPONIBLE + "' THEN 1"
			+ "         ELSE 0"
			+ "     END AS DISPONIBILIDAD"
			+ " FROM TBEP_USUARIOS bepusu"
			+ " INNER JOIN TBEP_SOLICITUDES bepsol ON (bepsol.BEPUSU_CODNUM = bepusu.CODNUM AND bepsol.BEPCON_CODNUM = ? "
			+ "		AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "')"
			+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON (bepsob.BEPSOL_CODNUM = bepsol.CODNUM AND bepsob.FECHABAREMACION IS NOT NULL)"
			+ " INNER JOIN TBEP_BOLSAS bepbol ON (bepbol.CODNUM = bepsob.BEPBOL_CODNUM AND bepbol.CODNUM = ?)"
			+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
			+ " INNER JOIN TBEP_PLAZAS_OFERTADAS bepplo ON bepplo.BEPARE_CODNUM = bepare.CODNUM"
			+ " INNER JOIN TBEP_DEDICACIONES bepded ON bepded.CODNUM = bepplo.BEPDED_CODNUM"
			+ " LEFT JOIN TBEP_ESTADO_CANDIDATOS bepesc ON (bepesc.BEPUSU_CODNUM = bepusu.CODNUM AND bepesc.BEPBOL_CODNUM = bepbol.CODNUM) "
			+ " LEFT JOIN TBEP_OFERTAS_CANDIDATOS bepofc ON (bepofc.BEPUSU_CODNUM = bepusu.CODNUM AND bepofc.BEPPLO_CODNUM = bepplo.CODNUM) "
			+ " LEFT JOIN ("
			+ "     SELECT bepesc.BEPUSU_CODNUM AS BEPUSU_CODNUM, COUNT(DISTINCT bepesc.BEPPLO_CODNUM) AS CONTRATOS"
			+ "     FROM TBEP_ESTADO_CANDIDATOS bepesc"
			+ "     WHERE bepesc.ESTADO NOT IN (" + estadosCandidato + ")"
			+ "     GROUP BY bepesc.BEPUSU_CODNUM"
			+ " ) bepcts ON bepcts.BEPUSU_CODNUM = bepusu.CODNUM"
			+ " WHERE bepplo.CODNUM = ?"
			+ "     AND bepusu.ROL = " + ModeloRol.ID_ROL_CANDIDATO
			+ "     AND bepusu.FLGBORRADO = 'N'";
	}
}
