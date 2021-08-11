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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para las plazas ofertadas para la contratación .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloOfertaCandidato {
	
	public static final int ORDER_COLUMN_INDEX_AREA = 0;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 1;
	public static final int ORDER_COLUMN_INDEX_FECHA_FIN_OFERTA = 2;
	public static final int ORDER_COLUMN_CONFIRMACION = 3;
	
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
	
	
	public List<OfertaCandidato> listaOfertasCandidatoPreferentes(UsuarioBolsaEmpleo candidato) throws SQLException, UVException {
		List<OfertaCandidato> listaOfertas = new ArrayList<>();
		
		String consulta = String.format("SELECT bepplo.CODNUM AS BEPPLO_CODNUM, bepofc.* FROM TBEP_PLAZAS_OFERTADAS bepplo"
				+ " INNER JOIN TBEP_OFERTAS_CANDIDATOS bepofc ON bepofc.BEPPLO_CODNUM = bepplo.CODNUM"
				+ " WHERE   bepplo.ESTADO = '" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "'"
				+ "     AND bepofc.BEPUSU_CODNUM = ?"
				+ "     AND bepofc.FLGRESULTADO = 'S'"
				+ " ORDER BY bepofc.PREFERENCIA");
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			
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
		String consulta = "SELECT bepplo.CODNUM AS BEPPLO_CODNUM, bepofc.* FROM TBEP_PLAZAS_OFERTADAS bepplo"
				+ " LEFT JOIN TBEP_OFERTAS_CANDIDATOS bepofc ON bepofc.BEPPLO_CODNUM = bepplo.CODNUM AND bepofc.BEPUSU_CODNUM = ?"
				+ " WHERE   bepplo.ESTADO = '" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "'"
				+ "     AND bepplo.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
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
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void aceptarOfertaCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato) throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			try {
				
				OfertaCandidato oferta = obtenerOfertaPorPlazaYCandidato(plaza, candidato, conexion);
				if (oferta != null) {
					oferta.setResultado(true);
					actualizaOfertaCandidato(oferta, candidato, conexion);
				} else {
					oferta = new OfertaCandidato(plaza, candidato, true, 0);
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
				
				OfertaCandidato oferta = obtenerOfertaPorPlazaYCandidato(plaza, candidato, conexion);
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
	
	private OfertaCandidato obtenerOfertaPorPlazaYCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato, Connection conexion) throws SQLException, UVException {
		String consulta = "SELECT bepofc.* FROM TBEP_OFERTAS_CANDIDATOS bepofc"
				+ " INNER JOIN TBEP_PLAZAS_OFERTADAS bepplo ON bepplo.CODNUM = bepofc.BEPPLO_CODNUM"
				+ " WHERE   bepofc.BEPUSU_CODNUM = ?"
				+ "     AND bepplo.ESTADO = '" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "'"
				+ "     AND bepplo.CODNUM = ?";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					createOfertaCandidatoFromResultSet(rs);
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
		System.out.println(consulta);
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
	 * @return datatable de plazas ofertadas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<OfertaCandidato> listadoPlazasOfertadasCandidato(Map<String, String[]> params, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		List<OfertaCandidato> rows = new ArrayList<>();
		BolsaEmpleoDataTable<OfertaCandidato> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepplo.CODNUM AS BEPPLO_CODNUM, bepofc.* FROM TBEP_PLAZAS_OFERTADAS bepplo"
				+ "	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepplo.BEPARE_CODNUM"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.BEPARE_CODNUM = bepare.CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPBOL_CODNUM = bepbol.CODNUM AND bepsob.FECHABAREMACION IS NOT NULL"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsob.BEPSOL_CODNUM = bepsol.CODNUM"
				+ " LEFT JOIN TBEP_OFERTAS_CANDIDATOS bepofc ON bepofc.BEPPLO_CODNUM = bepplo.CODNUM AND bepofc.BEPUSU_CODNUM = bepsol.BEPUSU_CODNUM"
				+ " WHERE   bepsol.BEPUSU_CODNUM = ? "
				+ "     AND bepplo.ESTADO = '" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "'";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepplo.ESTADO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA_FIN_OFERTA, "bepplo.FECHA_FIN_OFERTA");
		dataTable.setColumn(ORDER_COLUMN_CONFIRMACION, "bepofc.FLGRESULTADO");
				
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
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
	
	private OfertaCandidato createOfertaCandidatoFromResultSet(ResultSet rs) throws SQLException, UVException {
		OfertaCandidato oferta = new OfertaCandidato();
		
		if (rs.getInt(CODNUM) != 0) {
			oferta.setCodNum(rs.getInt(CODNUM));
			oferta.setCandidato(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt(BEPUSU_CODNUM)));
			oferta.setResultado("S".equals(rs.getString(FLGRESULTADO)));
			oferta.setFechaResultado(rs.getDate(FECHA_RESULTADO));
			oferta.setPreferencia(rs.getInt(PREFERENCIA));
		}
		
		oferta.setPlaza(ModeloPlazaOfertada.obtenerInstancia().getPlazaOfertadaById(rs.getInt(BEPPLO_CODNUM)));
		
		return oferta;
	}
	
}
