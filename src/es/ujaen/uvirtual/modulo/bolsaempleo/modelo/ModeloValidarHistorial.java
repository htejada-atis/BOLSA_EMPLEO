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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ValorMeritoBolsaTable;
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
	
	public static final String FECHALOG = "FECHALOG";
	public static final String COMPARA_EXCLUIDO = "COMPARA_EXCLUIDO";
	public static final String COMPARA_ITEM = "COMPARA_ITEM";
	public static final String COMPARA_VALIDADO = "COMPARA_VALIDADO";
	public static final String COMPARA_VALOR = "COMPARA_VALOR";
	public static final String COMPARA_VALORACION = "COMPARA_VALORACION";
	public static final String OBSERVACION_CANDIDATO = "OBSERVACION_CANDIDATO";
	public static final String UID_USUARIO = "UID_USUARIO";

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
	
	public List<HistorialValidacion> listaHistorialSolicitudBolsasMeritos(Convocatoria convocatoria, Merito merito, Bolsa bolsa) throws SQLException {
		List<HistorialValidacion> historiales = new ArrayList<>();
		
		String consulta = 
				"SELECT thsbm.LOG, thsbm.FECHALOG, thsbm.OBSERVACION_CANDIDATO, thsbm.UID_USUARIO, thsbm.FLGEXCLUIDO,"
				+ "	thsbm.FLGVALIDADO, thsbm.VALOR, thsbm.BEPITE_CODNUM, thsbm.VALOR,"
				+ "	(SELECT"
				+ "		CASE"
				+ "			WHEN thsbm2.FLGEXCLUIDO = 'N' AND thsbm.FLGEXCLUIDO = 'S' THEN 'N => S'"
				+ "			WHEN thsbm2.FLGEXCLUIDO = 'S' AND thsbm.FLGEXCLUIDO = 'N' THEN 'S => N'"
				+ "			ELSE ''"
				+ "		END"
				+ "	FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS COMPARA_EXCLUIDO,"
				+ "	(SELECT"
				+ "		CASE"
				+ "			WHEN thsbm2.FLGVALIDADO = 'N' AND thsbm.FLGVALIDADO = 'S' THEN 'N => S'"
				+ "			WHEN thsbm2.FLGVALIDADO = 'S' AND thsbm.FLGVALIDADO = 'N' THEN 'S => N'"
				+ "			ELSE ''"
				+ "		END"
				+ "	FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS COMPARA_VALIDADO,"
				+ "	(SELECT"
				+ "		CASE"
				+ "			WHEN thsbm2.VALOR IS NULL AND thsbm.VALOR IS NOT NULL THEN (thsbm2.VALOR || ' => ' || thsbm.VALOR)"
				+ "			WHEN thsbm2.VALOR != thsbm.VALOR THEN (thsbm2.VALOR || ' => ' || thsbm.VALOR)"
				+ "			ELSE ''"
				+ "		END"
				+ "	FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS COMPARA_VALOR,"
				+ "	(SELECT"
				+ "		CASE"
				+ "			WHEN thsbm2.RESULTADO IS NULL AND thsbm.RESULTADO IS NOT NULL THEN (thsbm2.RESULTADO || ' => ' || thsbm.RESULTADO)"
				+ "			WHEN thsbm2.RESULTADO != thsbm.RESULTADO THEN (thsbm2.RESULTADO || ' => ' || thsbm.RESULTADO)"
				+ "			ELSE ''"
				+ "		END"
				+ "	FROM TBEP_HTO_SOL_BOL_MERITOS thsbm2 WHERE LOG IN ('BEFORE_UPDATE') AND thsbm2.CODCAMBIO = thsbm.CODCAMBIO - 1) AS COMPARA_RESULTADO"
				+ "FROM TBEP_HTO_SOL_BOL_MERITOS thsbm"
				+ "INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.CODNUM = thsbm.BEPSBO_CODNUM"
				+ "INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmt.setInt(paramIndex, merito.getCodNum());
			stmt.setInt(paramIndex, bolsa.getCodNum());
			
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
					historiales.add(new HistorialValidacion(date, item, uidUsuario, excluido, observaciones, validado, valoracion, valor));
				}
			}
		}
		
		return historiales;
	}
	
}
