package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de las validaciones.
 * 
 * @author ATISoluciones 2021
 */
public class ModeloValidar {
	public static final int ORDER_COLUMN_INDEX_CODIGO_AREA = 0;
	public static final int ORDER_COLUMN_INDEX_AREA = 1;
	
	public static final String TOTAL_NO_VALIDADO = "TOTAL_NO_VALIDADO";
	public static final String TOTAL_VALIDADO = "TOTAL_VALIDADO";
	public static final String TOTAL_EXCLUIDO = "TOTAL_EXCLUIDO";
	public static final String TOTAL_MERITOS = "TOTAL_MERITOS";

	protected static ModeloValidar eInstancia = null;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloValidar();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloValidar obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Listado de bolsas sujetas a afinidad en una solicitud.
	 * 
	 * @param convocatoria .
	 * @param params    .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<BolsaValidacion> listadoAreasSujetasAfinidad(Convocatoria convocatoria, Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreas(convocatoria, params, true);
	}

	/**
	 * Listado de bolsas NO sujetas a afinidad en una solicitud.
	 * 
	 * @param convocatoria .
	 * @param params    .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<BolsaValidacion> listadoAreasNoSujetasAfinidad(Convocatoria convocatoria, Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreas(convocatoria, params, false);
	}
	
	
	/**
	 * Lista de bolsas para una convocatoria .
	 * @param convocatoria .
	 * @param params .
	 * @param afinidad .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private BolsaEmpleoDataTable<BolsaValidacion> listadoAreas(Convocatoria convocatoria, Map<String, String[]> params, boolean afinidad) 
			throws SQLException, UVException {
		List<BolsaValidacion> rows = new ArrayList<>();
		BolsaEmpleoDataTable<BolsaValidacion> dataTable = new BolsaEmpleoDataTable<BolsaValidacion>(params);
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();

		if (convocatoria == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		// subconsultas para seleccionar el número de méritos que contiene cada bolsa de la solicitud
		// en la convocatoria pasada, agregando después varios filtros
		String consultaCount = "SELECT COUNT(*) FROM UVIRTUAL.TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ "	WHERE bepsbo.BEPBOL_CODNUM = bepbol.CODNUM AND bepsol.BEPCON_CODNUM = ? AND bepsol.ESTADO = 'CERRADA'";
		
		// agrega el filtro de afinidad
		consultaCount += afinidad ? " AND bepite.AFINIDAD IS NOT NULL " : " AND bepite.AFINIDAD IS NULL ";
		
		// seleccionamos las bolsas, con meritos, en la convocatoria pasada
		String consulta = 
				"SELECT bepbol.*,"
				+ "	(" + consultaCount + " AND bepsbm.FLGVALIDADO = 'N' AND bepsbm.FLGEXCLUIDO = 'N') COUNT_NO_VALIDADOS, "
				+ "	(" + consultaCount + " AND bepsbm.FLGVALIDADO = 'S' AND bepsbm.FLGEXCLUIDO = 'N') COUNT_VALIDADOS, "
				+ "	(" + consultaCount + " AND bepsbm.FLGEXCLUIDO = 'S') COUNT_EXCLUIDOS, "
				+ "	(" + consultaCount + ") COUNT_TOTAL"
				+ "	FROM UVIRTUAL.TBEP_BOLSAS bepbol"
				+ " INNER JOIN UVIRTUAL.TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ "	WHERE bepbol.FLGBAREMABLE = 'S'";

		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_AREA, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");

		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());) {
			int paramIndex = 1;
			while (paramIndex <= 4) {
				stmt.setInt(paramIndex, convocatoria.getCodNum());
				stmtCount.setInt(paramIndex++, convocatoria.getCodNum());
			}

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = modeloBolsa.createFromResultSet(rs);
					BolsaValidacion bolsaValidacion = new BolsaValidacion(bolsa);
					bolsaValidacion.setTotalMeritosNoValidados(rs.getInt("COUNT_TOTAL"));
					bolsaValidacion.setTotalMeritosValidados(rs.getInt("COUNT_VALIDADOS"));
					bolsaValidacion.setTotalMeritosExcluidos(rs.getInt("COUNT_EXCLUIDOS"));
					bolsaValidacion.setTotalMeritos(rs.getInt("COUNT_TOTAL"));
					
					rows.add(bolsaValidacion);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
}
