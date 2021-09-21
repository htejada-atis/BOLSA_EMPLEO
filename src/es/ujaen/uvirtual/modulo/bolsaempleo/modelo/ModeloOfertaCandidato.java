package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para las plazas ofertadas para la contratación .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloOfertaCandidato {
	
	public static final int ORDER_COLUMN_INDEX_ID_PLAZA = 0;
	public static final int ORDER_COLUMN_INDEX_AREA = 1;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 2;
	public static final int ORDER_COLUMN_INDEX_FECHA_FIN_OFERTA = 3;
	public static final int ORDER_COLUMN_CONFIRMACION = 4;
	
	public static final String BEPPLO_CODNUM = "BEPPLO_CODNUM";
	public static final String BEPUSU_CODNUM = "BEPUSU_CODNUM";
	public static final String CODNUM = "CODNUM";
	public static final String FECHA_RESULTADO = "FECHA_RESULTADO";
	public static final String PREFERENCIA = "PREFERENCIA";
	public static final String FLGRESULTADO = "FLGRESULTADO";
	
	public static final String MENSAJE_ERROR_OFERTA_CANDIDATO_ID_NO_EXISTE = "No existe la oferta candidato con el id indicando";
	
	
	protected static ModeloOfertaCandidato eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloOfertaCandidato();
		}
	}

	/**
	 * Obtiene una instancia de la conexión .
	 * @return instancia .
	 */
	public static ModeloOfertaCandidato obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	
	/** Obtiene una lista de las plazas ofertadas confirmadas por el usuario y su preferencia .
	 * @param candidato .
	 * @param convocatoria .
	 * @return instancia .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<OfertaCandidato> listaOfertasCandidatoPreferentes(UsuarioBolsaEmpleo candidato, Convocatoria convocatoria) throws SQLException, UVException {
		List<OfertaCandidato> listaOfertas = new ArrayList<>();
		
		String consulta = "SELECT bepofc.BEPUSU_CODNUM, bepplo.CODNUM AS BEPPLO_CODNUM, bepofc.*, bepcnt.CODNUM AS CONTRATACION"
				+ " FROM TBEP_PLAZAS_OFERTADAS bepplo"
				+ " INNER JOIN TBEP_OFERTAS_CANDIDATOS bepofc ON bepofc.BEPPLO_CODNUM = bepplo.CODNUM"
				+ " LEFT JOIN TBEP_CONTRATACIONES bepcnt ON bepcnt.BEPUSU_CODNUM = bepofc.BEPUSU_CODNUM AND bepcnt.BEPPLO_CODNUM = bepplo.CODNUM"
				+ " WHERE   bepplo.ESTADO IN ('" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "', '" + ModeloPlazaOfertada.PLAZA_ESTADO_CONTRATACION + "')"
				+ "     AND bepofc.BEPUSU_CODNUM = ?"
				+ "     AND bepofc.FLGRESULTADO = 'S'"
				+ "     AND bepplo.FLGACTIVA = 'S'"
				+ "     AND bepofc.FECHA_RESULTADO > (SELECT bepcon.FECHACIERRE FROM TBEP_CONVOCATORIAS bepcon WHERE bepcon.CODNUM = ?)"
				+ " ORDER BY bepofc.PREFERENCIA";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setInt(parameterIndex++, convocatoria.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					listaOfertas.add(createOfertaCandidatoFromResultSet(rs));
				}
			}
		}
		
		return listaOfertas;
	}
	
	/**
	 * Obtiene una oferta candidato con el id de la plaza y el candidato .
	 * @param plaza .
	 * @param candidato .
	 * @return instancia .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public OfertaCandidato getOfertaByPlazaCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato) throws SQLException, UVException {
		String consulta = "SELECT bepplo.CODNUM AS BEPPLO_CODNUM, bepofc.CODNUM, bepofc.FLGRESULTADO, bepofc.FECHA_RESULTADO,"
				+ "     bepofc.PREFERENCIA, ? AS BEPUSU_CODNUM, bepcnt.CODNUM AS CONTRATACION"
				+ " FROM TBEP_PLAZAS_OFERTADAS bepplo"
				+ " LEFT JOIN TBEP_OFERTAS_CANDIDATOS bepofc ON bepofc.BEPPLO_CODNUM = bepplo.CODNUM AND bepofc.BEPUSU_CODNUM = ?"
				+ " LEFT JOIN TBEP_CONTRATACIONES bepcnt ON bepcnt.BEPUSU_CODNUM = ? AND bepcnt.BEPPLO_CODNUM = bepplo.CODNUM"
				+ " WHERE   bepplo.ESTADO IN ('" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "', '" + ModeloPlazaOfertada.PLAZA_ESTADO_CONTRATACION + "',"
				+ "         '" + ModeloPlazaOfertada.PLAZA_ESTADO_CERRADA + "')"
				+ "     AND bepplo.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_OFERTA_CANDIDATO_ID_NO_EXISTE);
				}
				
				return createOfertaCandidatoFromResultSet(rs);
			}
		}
	}
	
	/** aceptar plaza ofertada para un candidato .
	 * @param plaza .
	 * @param candidato .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void aceptarOfertaCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			try {
				OfertaCandidato oferta = usuarioUpdate.isServicioPersonal() ? obtenerOfertaPlazaCandidato(plaza, candidato, conexion) 
						: obtenerOfertaPlazaAbiertaCandidato(plaza, candidato, conexion);
				if (oferta != null) {
					oferta.setResultado(true);
					actualizaOfertaCandidato(oferta, usuarioUpdate, conexion);
				} else {
					oferta = new OfertaCandidato(plaza, candidato, true, 0);
					insertaOfertaCandidato(oferta, usuarioUpdate, conexion);
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
	
	/** rechazar plaza ofertada para un candidato .
	 * @param plaza .
	 * @param candidato .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void rechazarOfertaCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato) throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			try {
				
				OfertaCandidato oferta = obtenerOfertaPlazaAbiertaCandidato(plaza, candidato, conexion);
				if (oferta != null) {
					oferta.setResultado(false);
					actualizaOfertaCandidato(oferta, candidato, conexion);
				} else {
					oferta = new OfertaCandidato(plaza, candidato, false, 0);
					insertaOfertaCandidato(oferta, candidato, conexion);
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
	
	private OfertaCandidato obtenerOfertaPlazaAbiertaCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato, Connection conexion) throws SQLException, UVException {
		String consulta = "SELECT bepofc.*, bepcnt.CODNUM AS CONTRATACION,"
				+ "     CASE"
				+ "         WHEN bepplo.FECHA_FIN_OFERTA > SYSDATE THEN 1"
				+ "         ELSE 0"
				+ "     END AS ABIERTA_VIGENTE"
				+ " FROM TBEP_OFERTAS_CANDIDATOS bepofc"
				+ " INNER JOIN TBEP_PLAZAS_OFERTADAS bepplo ON bepplo.CODNUM = bepofc.BEPPLO_CODNUM"
				+ " LEFT JOIN TBEP_CONTRATACIONES bepcnt ON bepcnt.BEPUSU_CODNUM = bepofc.BEPUSU_CODNUM AND bepcnt.BEPPLO_CODNUM = bepplo.CODNUM"
				+ " WHERE   bepofc.BEPUSU_CODNUM = ?"
				+ "     AND bepplo.ESTADO = '" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "'"
				+ "     AND bepplo.CODNUM = ?";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					if (rs.getInt("ABIERTA_VIGENTE") == 0) {
						throw new UVException("La plaza ya no está disponible");
					}
					return createOfertaCandidatoFromResultSet(rs);
				}
				
			}
		}
		
		return null;
	}
	
	private OfertaCandidato obtenerOfertaPlazaCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato, Connection conexion) throws SQLException, UVException {
		String consulta = "SELECT bepofc.*, bepcnt.CODNUM AS CONTRATACION"
				+ " FROM TBEP_OFERTAS_CANDIDATOS bepofc"
				+ " INNER JOIN TBEP_PLAZAS_OFERTADAS bepplo ON bepplo.CODNUM = bepofc.BEPPLO_CODNUM"
				+ " LEFT JOIN TBEP_CONTRATACIONES bepcnt ON bepcnt.BEPUSU_CODNUM = bepofc.BEPUSU_CODNUM AND bepcnt.BEPPLO_CODNUM = bepplo.CODNUM"
				+ " WHERE   bepofc.BEPUSU_CODNUM = ?"
				+ "     AND bepplo.CODNUM = ?";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return createOfertaCandidatoFromResultSet(rs);
				}
				
			}
		}
		
		return null;
	}
	
	private void actualizaOfertaCandidato(OfertaCandidato oferta, UsuarioBolsaEmpleo usuarioUpdate, Connection conexion) throws SQLException {
		String consulta = String.format("UPDATE TBEP_OFERTAS_CANDIDATOS SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
				BEPPLO_CODNUM, BEPUSU_CODNUM, FLGRESULTADO, FECHA_RESULTADO, PREFERENCIA, "UID_USUARIO", CODNUM);
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, oferta.getPlaza().getCodNum());
			stmt.setInt(parameterIndex++, oferta.getCandidato().getCodNum());
			stmt.setString(parameterIndex++, oferta.isResultado() == null ? null : oferta.isResultado() ? "S" : "N");
			stmt.setDate(parameterIndex++, oferta.isResultado() == null ? null : new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setInt(parameterIndex++, oferta.getPreferencia());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, oferta.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	private void insertaOfertaCandidato(OfertaCandidato oferta, UsuarioBolsaEmpleo usuarioUpdate, Connection conexion) throws SQLException {
		String consulta = String.format("INSERT INTO TBEP_OFERTAS_CANDIDATOS (%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?)",
				BEPPLO_CODNUM, BEPUSU_CODNUM, FLGRESULTADO, FECHA_RESULTADO, PREFERENCIA, "UID_USUARIO");
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, oferta.getPlaza().getCodNum());
			stmt.setInt(parameterIndex++, oferta.getCandidato().getCodNum());
			stmt.setString(parameterIndex++, oferta.isResultado() == null ? null : oferta.isResultado() ? "S" : "N");
			stmt.setDate(parameterIndex++, oferta.isResultado() == null ? null : new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setInt(parameterIndex++, oferta.getPreferencia());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();
		}
	}
	
	/** actualiza la preferencia de un candidato para una plaza .
	 * @param idOferta .
	 * @param preferencia .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void actualizaPreferenciaOfertaCandidato(Integer idOferta, Integer preferencia, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String consulta = String.format("UPDATE TBEP_OFERTAS_CANDIDATOS SET %s=?, %s=? WHERE %s=?",
				PREFERENCIA, "UID_USUARIO", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, preferencia);
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, idOferta);
			stmt.executeUpdate();
		}
	}
	
	/** Lista de plazas ofertadas de un candidato .
	 * @param params .
	 * @param usuario .
	 * @param convocatoria .
	 * @return datatable de plazas ofertadas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<OfertaCandidato> listadoPlazasOfertadasCandidato(Map<String, String[]> params, UsuarioBolsaEmpleo usuario, Convocatoria convocatoria)
			throws SQLException, UVException {
		List<OfertaCandidato> rows = new ArrayList<>();
		BolsaEmpleoDataTable<OfertaCandidato> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepplo.CODNUM AS BEPPLO_CODNUM, bepofc.CODNUM, bepofc.FLGRESULTADO, bepofc.FECHA_RESULTADO,"
				+ "     bepofc.PREFERENCIA, bepsol.BEPUSU_CODNUM AS BEPUSU_CODNUM, bepcnt.CODNUM AS CONTRATACION"
				+ " FROM TBEP_PLAZAS_OFERTADAS bepplo"
				+ " INNER JOIN TBEP_DEDICACIONES bepded ON bepded.CODNUM = bepplo.BEPDED_CODNUM"
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepplo.BEPARE_CODNUM"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.BEPARE_CODNUM = bepare.CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPBOL_CODNUM = bepbol.CODNUM AND bepsob.FECHABAREMACION IS NOT NULL"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsob.BEPSOL_CODNUM = bepsol.CODNUM AND bepsol.BEPCON_CODNUM = ?"
				+ " LEFT JOIN TBEP_OFERTAS_CANDIDATOS bepofc ON bepofc.BEPPLO_CODNUM = bepplo.CODNUM AND bepofc.BEPUSU_CODNUM = bepsol.BEPUSU_CODNUM"
				+ " LEFT JOIN TBEP_CONTRATACIONES bepcnt ON bepcnt.BEPUSU_CODNUM = bepsol.BEPUSU_CODNUM AND bepcnt.BEPPLO_CODNUM = bepplo.CODNUM"
				+ " LEFT JOIN TBEP_ESTADO_CANDIDATOS bepesc ON bepesc.BEPUSU_CODNUM = bepsol.BEPUSU_CODNUM AND bepesc.BEPBOL_CODNUM = bepbol.CODNUM"
				+ " LEFT JOIN ("
				+ "     SELECT"
				+ "         bepesc.BEPUSU_CODNUM AS BEPUSU_CODNUM,"
				+ "     COUNT(DISTINCT bepesc.BEPPLO_CODNUM) AS CONTRATOS"
				+ "     FROM TBEP_ESTADO_CANDIDATOS bepesc"
				+ "     WHERE bepesc.ESTADO NOT IN ('" + ModeloEstadoCandidato.ESTADO_DISPONIBLE + "',"
				+ "         '" + ModeloEstadoCandidato.ESTADO_SUSPENSION_PROVISIONAL + "', '" + ModeloEstadoCandidato.ESTADO_NO_DISPONIBLE + "')"
				+ "     GROUP BY bepesc.BEPUSU_CODNUM"
				+ " ) bepcts ON bepcts.BEPUSU_CODNUM = bepsol.BEPUSU_CODNUM"
				+ " WHERE   bepsol.BEPUSU_CODNUM = ? "
				+ "     AND bepplo.ESTADO IN ('" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "', '" + ModeloPlazaOfertada.PLAZA_ESTADO_CONTRATACION + "')"
				+ "     AND ("
				+ "         CASE"
				+ "             WHEN bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_CONTRATADO_PRIMER_CUATRIMESTRE + "'"
				+ "                 AND bepplo.CUATRIMESTRE = '" + ModeloPlazaOfertada.CUATRIMESTRE_SEGUNDO + "' THEN 1"
				+ "             WHEN bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_CONTRATADO_PARCIAL + "'"
				+ "                 AND bepded.TIPO = '" + ModeloDedicacion.TIPO_TIEMPO_PARCIAL + "' THEN 1"
				+ "             WHEN bepcts.CONTRATOS > 0 THEN 0"
				+ "             WHEN bepesc.CODNUM IS NULL THEN 1"
				+ "             WHEN bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_DISPONIBLE + "' THEN 1"
				+ "             ELSE 0"
				+ "         END) = 1"
				+ "     AND bepplo.FLGACTIVA = 'S'";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_PLAZA, "bepplo.ID_PLAZA");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepplo.ESTADO", DataTableColumn.COLUMN_TYPE_EXACT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA_FIN_OFERTA, "bepplo.FECHA_FIN_OFERTA", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_CONFIRMACION, "bepofc.FLGRESULTADO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
				
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex, usuario.getCodNum());
			stmtCount.setInt(paramIndex++, usuario.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					rows.add(createOfertaCandidatoFromResultSet(rs));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}
		
		return dataTable;
	}
	
	/** Crear oferta candidato de un ResultSet .
	 * @param rs .
	 * @return oferta candidato .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public OfertaCandidato createOfertaCandidatoFromResultSet(ResultSet rs) throws SQLException, UVException {
		OfertaCandidato oferta = new OfertaCandidato();
		
		if (rs.getInt(CODNUM) != 0) {
			oferta.setCodNum(rs.getInt(CODNUM));
			oferta.setResultado("S".equals(rs.getString(FLGRESULTADO)));
			oferta.setFechaResultado(rs.getDate(FECHA_RESULTADO));
			oferta.setPreferencia(rs.getInt(PREFERENCIA));
		}
		
		oferta.setCandidato(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt(BEPUSU_CODNUM)));
		oferta.setPlaza(ModeloPlazaOfertada.obtenerInstancia().getPlazaOfertadaById(rs.getInt(BEPPLO_CODNUM)));
		oferta.setContratacion(rs.getInt("CONTRATACION") != 0 
				? ModeloContratacion.obtenerInstancia().getContratacionById(rs.getInt("CONTRATACION")) : null);
		
		return oferta;
	}
	
}
