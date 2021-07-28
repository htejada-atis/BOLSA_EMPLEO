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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Historial;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialSBM;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de historial de las validaciones.
 * 
 * @author ATISoluciones 2021
 */
public class ModeloValidarHistorial {

	protected static ModeloValidarHistorial eInstancia;
	
	public static final int ORDER_COLUMN_INDEX_FECHA = 0;
	public static final int ORDER_COLUMN_INDEX_EVALUADOR = 1;
	public static final int ORDER_COLUMN_INDEX_CODIGO_MERITO = 2;
	public static final int ORDER_COLUMN_INDEX_VALOR = 3;
	
	public static final String BEPITE_CODNUM = "BEPITE_CODNUM";
	public static final String COMPARA_EXCLUIDO = "COMPARA_EXCLUIDO";
	public static final String COMPARA_ITEM = "COMPARA_ITEM";
	public static final String COMPARA_RESULTADO = "COMPARA_RESULTADO";
	public static final String COMPARA_VALIDADO = "COMPARA_VALIDADO";
	public static final String COMPARA_VALOR = "COMPARA_VALOR";
	public static final String COMPARA_VALORACION = "COMPARA_VALORACION";
	public static final String FECHALOG = "FECHALOG";
	public static final String FLGEXCLUIDO = "FLGEXCLUIDO";
	public static final String FLGVALIDADO = "FLGVALIDADO";
	public static final String LOG = "LOG";
	public static final String OBSERVACION_CANDIDATO = "OBSERVACION_CANDIDATO";
	public static final String RESULTADO = "RESULTADO";
	public static final String ROL_USUARIO = "ROL_USUARIO";
	public static final String UID_USUARIO = "UID_USUARIO";
	public static final String VALOR = "VALOR";

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloValidarHistorial();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloValidarHistorial obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/** Lista de meritos de un candidato para una bolsa .
	 * @param convocatoria .
	 * @param merito .
	 * @param bolsa .
	 * @param params .
	 * @return datatable de méritos de validación .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<HistorialValidacion> listadoHistorialValidacionesMerito(Convocatoria convocatoria, Merito merito, 
			Bolsa bolsa, Map<String, String[]> params) throws SQLException, UVException {
		List<HistorialValidacion> rows = new ArrayList<>();
		BolsaEmpleoDataTable<HistorialValidacion> dataTable = new BolsaEmpleoDataTable<>(params);
				
		if (convocatoria == null || merito == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = ""
				+ "	SELECT * FROM"
				+ "		(SELECT bepsol.BEPCON_CODNUM, thsbm.BEPMER_CODNUM, bepsob.BEPBOL_CODNUM, thsbm.FECHALOG, thsbm.OBSERVACION_CANDIDATO, "
				+ "		 	thsbm.UID_USUARIO,"
				+ "			(SELECT"
				+ "				CASE"
				+ "      			WHEN thsbm2.FLGEXCLUIDO = 'S' AND thsbm.FLGEXCLUIDO = 'S' THEN 'S => S'"
				+ "      			WHEN thsbm2.FLGEXCLUIDO = 'N' AND thsbm.FLGEXCLUIDO = 'S' THEN 'N => S'"
				+ "      			WHEN thsbm2.FLGEXCLUIDO = 'S' AND thsbm.FLGEXCLUIDO = 'N' THEN 'S => N'"
				+ "      			ELSE ''"
				+ "				END"
				+ "			FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS " 
							+ COMPARA_EXCLUIDO + ","
				+ "			(SELECT"
				+ "				CASE"
				+ "      			WHEN thsbm2.FLGVALIDADO = 'S' AND thsbm.FLGVALIDADO = 'S' THEN 'S => S'"
				+ "      			WHEN thsbm2.FLGVALIDADO = 'N' AND thsbm.FLGVALIDADO = 'S' THEN 'N => S'"
				+ "      			WHEN thsbm2.FLGVALIDADO = 'S' AND thsbm.FLGVALIDADO = 'N' THEN 'S => N'"
				+ "      			ELSE ''"
				+ "				END"
				+ "			FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS "
							+ COMPARA_VALIDADO + ","
				+ " 		'' AS COMPARA_VALOR, '' AS COMPARA_ITEM, '' AS COMPARA_VALORACION"
				+ "		FROM TBEP_HTO_SOL_BOL_MERITOS thsbm"
				+ "		INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.CODNUM = thsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ "		WHERE LOG IN ('AFTER_UPDATE')"
				+ " 	UNION"
				+ " 	SELECT bepsol.BEPCON_CODNUM, thmer.CODNUM AS BEPMER_CODNUM, bepsob.BEPBOL_CODNUM, thmer.FECHALOG, '' AS OBSERVACION_CANDIDATO,"
				+ "		 	thmer.UID_USUARIO, '' AS COMPARA_EXCLUIDO, '' AS COMPARA_VALIDADO,"
				+ "			(SELECT"
				+ "				CASE"
				+ "     			WHEN thmer2.VALOR != thmer.VALOR THEN (thmer2.VALOR || ' => ' || thmer.VALOR)"
				+ "     			ELSE ''"
				+ "     		END"
				+ "			FROM TBEP_HTO_MERITOS thmer2 WHERE LOG IN ('BEFORE_UPDATE') AND thmer2.CODCAMBIO = thmer.CODCAMBIO - 1) AS COMPARA_VALOR,"
				+ "			(SELECT"
				+ "				CASE"
				+ "	  				WHEN thmer2.BEPITE_CODNUM != thmer.BEPITE_CODNUM THEN (bepapa2.CODIGO || '.' || bepblo2.CODIGO ||"
				+ "					'.' || bepite2.CODIGO || ' => ' || bepapa.CODIGO || '.' || bepblo.CODIGO "
				+ "					|| '.' || bepite.CODIGO)"
				+ "				ELSE ''"
				+ "	  			END"
				+ "			FROM TBEP_HTO_MERITOS thmer2"
				+ "			INNER JOIN TBEP_ITEMSBAREMACION bepite2 ON bepite2.CODNUM = thmer2.BEPITE_CODNUM"
				+ "			INNER JOIN TBEP_BLOQUESBAREMACION bepblo2 ON bepblo2.CODNUM = bepite2.BEPBLO_CODNUM"
				+ "			INNER JOIN TBEP_APARTADOSBAREMACION bepapa2 ON bepapa2.CODNUM = bepblo2.BEPAPA_CODNUM"
				+ "			WHERE LOG IN ('BEFORE_UPDATE') AND thmer2.CODCAMBIO = thmer.CODCAMBIO - 1) AS COMPARA_ITEM,"
				+ "			'' AS COMPARA_VALORACION"
				+ "		FROM TBEP_HTO_MERITOS thmer"
				+ "		INNER JOIN TBEP_SOL_BOL_MERITOS bepsbm ON thmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ "		INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = thmer.BEPITE_CODNUM"
				+ "		INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ "		INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM = bepblo.BEPAPA_CODNUM"
				+ "		WHERE LOG IN ('AFTER_UPDATE')"
				+ "		UNION"
				+ "		SELECT bepsol.BEPCON_CODNUM, bepsbm.BEPMER_CODNUM, bepsob.BEPBOL_CODNUM, thsbv.FECHALOG, '' AS OBSERVACION_CANDIDATO, "
				+ "		thsbv.UID_USUARIO, '' AS COMPARA_EXCLUIDO, '' AS COMPARA_VALIDADO, '' AS COMPARA_VALOR, '' AS COMPARA_ITEM,"
				+ "		(SELECT"
				+ "				CASE"
				+ "					WHEN thsbv2.VALOR != thsbv.VALOR THEN (thsbv2.VALOR || ' => ' || thsbv.VALOR)"
				+ "					WHEN thsbv2.BEPAFI_CODNUM != thsbv.BEPAFI_CODNUM THEN (bepafi2.MODULACION * 100 || '% => ' "
				+ "					|| bepafi.MODULACION * 100 || '%')"
				+ "					WHEN thsbv2.VALOR IS NULL THEN (bepafi2.MODULACION * 100 || '% => ' "
				+ "					|| bepafi.MODULACION * 100 || '%')"
				+ "					WHEN thsbv2.VALOR IS NOT NULL THEN (thsbv2.VALOR || ' => ' || thsbv.VALOR)"
				+ "					ELSE ''"
				+ "				END"
				+ "			FROM TBEP_HTO_SOL_BOL_MER_VALORA thsbv2"
				+ "			INNER JOIN TBEP_AFINIDADES bepafi2 ON bepafi2.CODNUM = thsbv2.BEPAFI_CODNUM"
				+ "			WHERE LOG IN ('BEFORE_UPDATE') AND thsbv2.CODCAMBIO = thsbv.CODCAMBIO - 1) AS COMPARA_VALORACION"
				+ "		FROM TBEP_HTO_SOL_BOL_MER_VALORA thsbv"
				+ "		INNER JOIN TBEP_AFINIDADES bepafi ON bepafi.CODNUM = thsbv.BEPAFI_CODNUM"
				+ "		INNER JOIN TBEP_SOL_BOL_MERITOS bepsbm ON bepsbm.CODNUM = thsbv.BEPSBM_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ "		WHERE LOG IN ('AFTER_UPDATE')"
				+ "	)"
				+ "	WHERE BEPCON_CODNUM = ? AND BEPMER_CODNUM = ? AND BEPBOL_CODNUM = ?";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA, FECHALOG, DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_EVALUADOR, UID_USUARIO);
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex, merito.getCodNum());
			stmtCount.setInt(paramIndex++, merito.getCodNum());
			stmt.setInt(paramIndex, bolsa.getCodNum());
			stmtCount.setInt(paramIndex++, bolsa.getCodNum());

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Date date = rs.getTimestamp(FECHALOG);
					String uidUsuario = rs.getString(UID_USUARIO);
					String item = rs.getString(COMPARA_ITEM);
					String excluido = rs.getString(COMPARA_EXCLUIDO);
					String validado = rs.getString(COMPARA_VALIDADO);
					String valor = rs.getString(COMPARA_VALOR);
					String valoracion = rs.getString(COMPARA_VALORACION);
					String observaciones = rs.getString(OBSERVACION_CANDIDATO);
					rows.add(new HistorialValidacion(date, item, uidUsuario, excluido, observaciones, validado, valoracion, valor));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/** Lista de historiales de un mérito de una solicitud para una bolsa .
	 * @param convocatoria .
	 * @param merito .
	 * @param bolsa .
	 * @param candidato .
	 * @return lista historial .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<HistorialSBM> listaHistorialSolicitudBolsasMeritos(Convocatoria convocatoria, Merito merito, Bolsa bolsa, UsuarioBolsaEmpleo candidato)
			throws SQLException, UVException {
		List<HistorialSBM> historiales = new ArrayList<>();
		
		String consulta = 
				"SELECT thsbm.LOG, thsbm.FECHALOG, thsbm.OBSERVACION_CANDIDATO, thsbm.UID_USUARIO, thsbm.FLGEXCLUIDO,"
				+ " thsbm.FLGVALIDADO, thsbm.VALOR, thsbm.BEPITE_CODNUM, thsbm.VALOR, thsbm.RESULTADO, bepusu.ROL AS ROL_USUARIO,"
				+ "     (SELECT"
				+ "         CASE"
				+ "             WHEN thsbm2.FLGEXCLUIDO = 'N' AND thsbm.FLGEXCLUIDO = 'S' THEN 'N => S'"
				+ "             WHEN thsbm2.FLGEXCLUIDO = 'S' AND thsbm.FLGEXCLUIDO = 'N' THEN 'S => N'"
				+ "             ELSE ''"
				+ "         END"
				+ "     FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS COMPARA_EXCLUIDO,"
				+ "     (SELECT"
				+ "         CASE"
				+ "             WHEN thsbm2.FLGVALIDADO = 'N' AND thsbm.FLGVALIDADO = 'S' THEN 'N => S'"
				+ "             WHEN thsbm2.FLGVALIDADO = 'S' AND thsbm.FLGVALIDADO = 'N' THEN 'S => N'"
				+ "             ELSE ''"
				+ "         END"
				+ "     FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS COMPARA_VALIDADO,"
				+ "     (SELECT"
				+ "         CASE"
				+ "             WHEN thsbm2.VALOR IS NULL AND thsbm.VALOR IS NOT NULL THEN (thsbm2.VALOR || ' => ' || thsbm.VALOR)"
				+ "             WHEN thsbm2.VALOR != thsbm.VALOR THEN (thsbm2.VALOR || ' => ' || thsbm.VALOR)"
				+ "             ELSE ''"
				+ "         END"
				+ "     FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS COMPARA_VALOR,"
				+ "     (SELECT"
				+ "             CASE"
				+ "                 WHEN thsbm2.RESULTADO IS NULL AND thsbm.RESULTADO IS NOT NULL THEN (thsbm2.RESULTADO || ' => ' || thsbm.RESULTADO)"
				+ "                 WHEN thsbm2.RESULTADO != thsbm.RESULTADO THEN (thsbm2.RESULTADO || ' => ' || thsbm.RESULTADO)"
				+ "             ELSE ''"
				+ "         END"
				+ "     FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS COMPARA_RESULTADO,"
				+ "     (SELECT"
				+ "         CASE"
				+ "             WHEN thsbm2.BEPITE_CODNUM IS NULL AND thsbm.BEPITE_CODNUM IS NOT NULL THEN (bepapa2.CODIGO || '.' || bepblo2.CODIGO ||"
				+ "                 '.' || bepite2.CODIGO || ' => ' || bepapa.CODIGO || '.' || bepblo.CODIGO || '.' || bepite.CODIGO)"
				+ "             WHEN thsbm2.BEPITE_CODNUM != thsbm.BEPITE_CODNUM THEN (bepapa2.CODIGO || '.' || bepblo2.CODIGO ||"
				+ "                 '.' || bepite2.CODIGO || ' => ' || bepapa.CODIGO || '.' || bepblo.CODIGO || '.' || bepite.CODIGO)"
				+ "             ELSE ''"
				+ "         END"
				+ "     FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2"
				+ "     INNER JOIN TBEP_ITEMSBAREMACION bepite2 ON bepite2.CODNUM = thsbm2.BEPITE_CODNUM"
				+ "     INNER JOIN TBEP_BLOQUESBAREMACION bepblo2 ON bepblo2.CODNUM = bepite2.BEPBLO_CODNUM"
				+ "     INNER JOIN TBEP_APARTADOSBAREMACION bepapa2 ON bepapa2.CODNUM = bepblo2.BEPAPA_CODNUM"
				+ "     WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS COMPARA_ITEM"
				+ " FROM TBEP_HTO_SOL_BOL_MERITOS thsbm"
				+ " LEFT JOIN TBEP_USUARIOS bepusu ON bepusu.CODCUENTA = thsbm.UID_USUARIO"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.CODNUM = thsbm.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ " LEFT JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = thsbm.BEPITE_CODNUM"
				+ " LEFT JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " LEFT JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM = bepblo.BEPAPA_CODNUM"
				+ " WHERE bepsol.BEPCON_CODNUM = ?"
				+ "     AND thsbm.BEPMER_CODNUM = ?"
				+ "     AND bepsob.BEPBOL_CODNUM = ?"
				+ " ORDER BY thsbm.FECHALOG DESC";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, merito.getCodNum());
			stmt.setInt(paramIndex++, bolsa.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					historiales.add(createHistorialSBMFromResultset(rs));
				}
			}
		}
		
		return historiales;
	}
	
	/** Lista de historiales de un mérito .
	 * @param merito .
	 * @param candidato .
	 * @return lista historial .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<HistorialMerito> listaHistorialMerito(Merito merito, UsuarioBolsaEmpleo candidato)
			throws SQLException, UVException {
		List<HistorialMerito> historiales = new ArrayList<>();
		
		String consulta = 
				"SELECT thmer.LOG, thmer.FECHALOG, thmer.VALOR, thmer.BEPITE_CODNUM, thmer.UID_USUARIO, bepusu.ROL AS ROL_USUARIO,"
				+ "     (SELECT"
				+ "         CASE"
				+ "             WHEN thmer2.VALOR IS NULL AND thmer.VALOR IS NOT NULL THEN (thmer2.VALOR || ' => ' || thmer.VALOR)"
				+ "             WHEN thmer2.VALOR != thmer.VALOR THEN (thmer2.VALOR || ' => ' || thmer.VALOR)"
				+ "             ELSE ''"
				+ "         END"
				+ "     FROM TBEP_HTO_SOL_BOL_MERITOS thmer2 WHERE LOG IN ('BEFORE_UPDATE') AND thmer2.CODCAMBIO = thmer.CODCAMBIO - 1) AS COMPARA_VALOR,"
				+ "     (SELECT"
				+ "         CASE"
				+ "             WHEN thmer2.BEPITE_CODNUM IS NULL AND thmer.BEPITE_CODNUM IS NOT NULL THEN (bepapa2.CODIGO || '.' || bepblo2.CODIGO ||"
				+ "                 '.' || bepite2.CODIGO || ' => ' || bepapa.CODIGO || '.' || bepblo.CODIGO || '.' || bepite.CODIGO)"
				+ "             WHEN thmer2.BEPITE_CODNUM != thmer.BEPITE_CODNUM THEN (bepapa2.CODIGO || '.' || bepblo2.CODIGO ||"
				+ "                 '.' || bepite2.CODIGO || ' => ' || bepapa.CODIGO || '.' || bepblo.CODIGO || '.' || bepite.CODIGO)"
				+ "             ELSE ''"
				+ "         END"
				+ "     FROM TBEP_HTO_MERITOS thmer2"
				+ "     INNER JOIN TBEP_ITEMSBAREMACION bepite2 ON bepite2.CODNUM = thmer2.BEPITE_CODNUM"
				+ "     INNER JOIN TBEP_BLOQUESBAREMACION bepblo2 ON bepblo2.CODNUM = bepite2.BEPBLO_CODNUM"
				+ "     INNER JOIN TBEP_APARTADOSBAREMACION bepapa2 ON bepapa2.CODNUM = bepblo2.BEPAPA_CODNUM"
				+ "     WHERE LOG IN ('BEFORE_UPDATE') AND thmer2.CODCAMBIO = thmer.CODCAMBIO - 1) AS COMPARA_ITEM"
				+ " FROM TBEP_HTO_MERITOS thmer"
				+ " LEFT JOIN TBEP_USUARIOS bepusu ON bepusu.CODCUENTA = thmer.UID_USUARIO"
				+ " LEFT JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = thmer.BEPITE_CODNUM"
				+ " LEFT JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " LEFT JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM = bepblo.BEPAPA_CODNUM"
				+ " WHERE thmer.CODNUM = ?"
				+ " ORDER BY thmer.FECHALOG DESC";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, merito.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					historiales.add(createHistorialMeritoFromResultset(rs));
				}
			}
		}
		
		return historiales;
	}
	
	public List<HistorialMerito> listaHistorialValoracionesMerito(Merito merito, UsuarioBolsaEmpleo candidato)
			throws SQLException, UVException {
		List<HistorialMerito> historiales = new ArrayList<>();
		
		String consulta = 
				"SELECT thsbv.LOG, thsbv.FECHALOG, thsbv.VALOR, thsbv.BEPAFI_CODNUM, thsbv.UID_USUARIO, bepusu.ROL AS ROL_USUARIO,"
				+ "     (SELECT"
				+ "         CASE"
				+ "             WHEN thsbv2.VALOR != thsbv.VALOR THEN (thsbv2.VALOR || ' => ' || thsbv.VALOR)"
				+ "             WHEN thsbv2.BEPAFI_CODNUM != thsbv.BEPAFI_CODNUM THEN (bepafi2.MODULACION * 100 || '% => ' || bepafi.MODULACION * 100 || '%')"
				+ "             WHEN thsbv2.VALOR IS NULL THEN (bepafi2.MODULACION * 100 || '% => ' || bepafi.MODULACION * 100 || '%')"
				+ "             WHEN thsbv2.VALOR IS NOT NULL THEN (thsbv2.VALOR || ' => ' || thsbv.VALOR)"
				+ "             ELSE ''"
				+ "         END"
				+ "     FROM TBEP_HTO_SOL_BOL_MER_VALORA thsbv2"
				+ "     INNER JOIN TBEP_AFINIDADES bepafi2 ON bepafi2.CODNUM = thsbv2.BEPAFI_CODNUM"
				+ "     WHERE LOG IN ('BEFORE_UPDATE') AND thsbv2.CODCAMBIO = thsbv.CODCAMBIO - 1) AS COMPARA_VALOR"
				+ " FROM TBEP_HTO_SOL_BOL_MER_VALORA thsbv"
				+ " LEFT JOIN TBEP_USUARIOS bepusu ON bepusu.CODCUENTA = thsbv.UID_USUARIO"
				+ " INNER JOIN TBEP_AFINIDADES bepafi ON bepafi.CODNUM = thsbv.BEPAFI_CODNUM"
				+ " INNER JOIN TBEP_SOL_BOL_MERITOS bepsbm ON bepsbm.CODNUM = thsbv.BEPSBM_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ " WHERE bepsbm.BEPMER_CODNUM = ?"
				+ " ORDER BY thsbv.FECHALOG DESC";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, merito.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					historiales.add(createHistorialMeritoFromResultset(rs));
				}
			}
		}
		
		return historiales;
	}
	
	private HistorialSBM createHistorialSBMFromResultset(ResultSet rs) throws SQLException, UVException {
		String comparaExcluido = rs.getString(COMPARA_EXCLUIDO);
		String comparaValidado = rs.getString(COMPARA_VALIDADO);
		String comparaValor = rs.getString(COMPARA_VALOR);
		String comparaResultado = rs.getString(COMPARA_RESULTADO);
		String comparaItem = rs.getString(COMPARA_ITEM);
		
		Date fechaLog = rs.getTimestamp(FECHALOG);
		String log = rs.getString(LOG);
		String uidUsuario = rs.getString(UID_USUARIO);
		Integer idRol = rs.getInt(ROL_USUARIO);
		String rolUsuario = "";
		
		if (idRol.equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
			rolUsuario = "Personal";
		} else if (idRol.equals(ModeloRol.ID_ROL_MIEMBRO_COMISION)) {
			rolUsuario = "Comision";
		} else if (idRol.equals(ModeloRol.ID_ROL_CANDIDATO)) {
			rolUsuario = "Candidatos";
		} else if (idRol.equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) {
			rolUsuario = "Director departamento";
		}
		
		Historial historial = new Historial(fechaLog, log, uidUsuario, rolUsuario);
		
		Boolean excluido = rs.getString(FLGEXCLUIDO).equals("S");
		Boolean validado = rs.getString(FLGVALIDADO).equals("S");
		String observacionCandidato = rs.getString(OBSERVACION_CANDIDATO);
		ItemBaremacion item = rs.getInt(BEPITE_CODNUM) != 0 ? ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(rs.getInt(BEPITE_CODNUM)) : null;
		Double resultado = rs.getDouble(RESULTADO);
		Double valor = rs.getDouble(VALOR);
		
		return new HistorialSBM(historial, excluido, validado, observacionCandidato, item, resultado, valor, comparaExcluido, comparaValidado,
				comparaValor, comparaResultado, comparaItem);
	}
	
	private HistorialMerito createHistorialMeritoFromResultset(ResultSet rs) throws SQLException, UVException {
		String comparaValor = rs.getString(COMPARA_VALOR);
		String comparaItem = rs.getString(COMPARA_ITEM);
		
		Date fechaLog = rs.getTimestamp(FECHALOG);
		String log = rs.getString(LOG);
		String uidUsuario = rs.getString(UID_USUARIO);
		Integer idRol = rs.getInt(ROL_USUARIO);
		String rolUsuario = "";
		
		if (idRol.equals(ModeloRol.ID_ROL_SERVICIO_PERSONAL)) {
			rolUsuario = "Personal";
		} else if (idRol.equals(ModeloRol.ID_ROL_MIEMBRO_COMISION)) {
			rolUsuario = "Comision";
		} else if (idRol.equals(ModeloRol.ID_ROL_CANDIDATO)) {
			rolUsuario = "Candidatos";
		} else if (idRol.equals(ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO)) {
			rolUsuario = "Director departamento";
		}
		
		Historial historial = new Historial(fechaLog, log, uidUsuario, rolUsuario);
		
		ItemBaremacion item = rs.getInt(BEPITE_CODNUM) != 0 ? ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(rs.getInt(BEPITE_CODNUM)) : null;
		Double valor = rs.getDouble(VALOR);
		
		return new HistorialMerito(historial, item, valor, comparaValor, comparaItem);
	}
	
}
