package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionArcos;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaCandidatoValidacionTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Candidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoValidarTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ValorMeritoBolsaTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de las validaciones.
 * 
 * @author ATISoluciones 2021
 */
public class ModeloValidar {
	
	public static final int ORDER_COLUMN_INDEX_CODIGO_AREA = 0;
	public static final int ORDER_COLUMN_INDEX_AREA = 1;
	public static final int ORDER_COLUMN_INDEX_COUNT_NO_VALIDADOS = 2;
	public static final int ORDER_COLUMN_INDEX_COUNT_VALIDADOS = 3;
	public static final int ORDER_COLUMN_INDEX_COUNT_EXCLUIDOS = 4;
	public static final int ORDER_COLUMN_INDEX_COUNT_TOTAL = 5;
	
	public static final int ORDER_COLUMN_INDEX_NUMDOCUMENTO_CANDIDATO = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_CANDIDATO = 1;
	
	public static final int ORDER_COLUMN_INDEX_ID_MERITO = 0;
	public static final int ORDER_COLUMN_INDEX_ESTADO_MERITO = 1;
	public static final int ORDER_COLUMN_INDEX_CODIGO_MERITO = 2;
	public static final int ORDER_COLUMN_INDEX_VALOR_MERITO = 3;
	
	public static final String TOTAL_NO_VALIDADO = "TOTAL_NO_VALIDADO";
	public static final String TOTAL_VALIDADO = "TOTAL_VALIDADO";
	public static final String TOTAL_EXCLUIDO = "TOTAL_EXCLUIDO";
	public static final String TOTAL_MERITOS = "TOTAL_MERITOS";
	
	private static final int TOTAL_COUNT_SUBQUERIES = 4;

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
	 * @param convocatoria .
	 * @param params .
	 * @return datatable de bolsas de validación .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<BolsaValidacion> listadoAreasSujetasAfinidad(Convocatoria convocatoria, Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreas(convocatoria, params, true);
	}

	/**
	 * Listado de bolsas NO sujetas a afinidad en una solicitud.
	 * @param convocatoria .
	 * @param params .
	 * @return datatable de bolsas de validación .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<BolsaValidacion> listadoAreasNoSujetasAfinidad(Convocatoria convocatoria, Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreas(convocatoria, params, false);
	}
	
	/**
	 * Listado de bolsas en las que está apuntado actualmente el candidato sujetas a afinidad en una solicitud.
	 * @param convocatoria .
	 * @param merito .
	 * @param candidato .
	 * @param params .
	 * @return datatable de bolsas de un candidato .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<BolsaCandidatoValidacionTable> listadoAreasCandidatoSujetasAfinidad(Convocatoria convocatoria, UsuarioBolsaEmpleo candidato, Merito merito,
			Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreasCandidato(convocatoria, candidato, merito, params, false);
	}
	
	/**
	 * Listado de bolsas en las que está apuntado actualmente el candidato NO sujetas a afinidad en una solicitud.
	 * @param convocatoria .
	 * @param merito .
	 * @param candidato .
	 * @param params .
	 * @return datatable de bolsas de un candidato .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<BolsaCandidatoValidacionTable> listadoAreasCandidatoNoSujetasAfinidad(Convocatoria convocatoria, UsuarioBolsaEmpleo candidato, Merito merito, 
			Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoAreasCandidato(convocatoria, candidato, merito, params, false);
	}
	
	/**
	 * Listado de candidatos con bolsas sujetas a afinidad en una solicitud.
	 * @param convocatoria .
	 * @param bolsa .
	 * @param params .
	 * @return datatable de candidatos de validación .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<CandidatoValidacion> listadoCandidatosSujetosAfinidad(Convocatoria convocatoria, Bolsa bolsa, Map<String, String[]> params)
			throws SQLException, UVException {
		return this.listadoCandidatos(convocatoria, bolsa, params, true);
	}

	/**
	 * Listado de candidatos de bolsa NO sujeta a afinidad en una solicitud.
	 * @param convocatoria .
	 * @param bolsa .
	 * @param params .
	 * @return datatable de candidatos de validación .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<CandidatoValidacion> listadoCandidatosNoSujetosAfinidad(Convocatoria convocatoria, Bolsa bolsa, Map<String, String[]> params)
			throws SQLException, UVException {
		return this.listadoCandidatos(convocatoria, bolsa, params, false);
	}

	/**
	 * Listado de meritos del candidato de la bolsa sujeta a afinidad en una solicitud.
	 * @param convocatoria .
	 * @param bolsa .
	 * @param candidato .
	 * @param params .
	 * @return datatable de méritos de validación .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<MeritoValidarTable> listadoMeritosSujetosAfinidad(Convocatoria convocatoria, Bolsa bolsa, UsuarioBolsaEmpleo candidato,
			Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoMeritos(convocatoria, bolsa, candidato, params, true);
	}

	/**
	 * Listado de meritos del candidato de la bolsa NO sujeta a afinidad en una solicitud.
	 * @param convocatoria .
	 * @param bolsa .
	 * @param candidato .
	 * @param params .
	 * @return datatable de méritos de validación .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<MeritoValidarTable> listadoMeritosNoSujetosAfinidad(Convocatoria convocatoria, Bolsa bolsa, UsuarioBolsaEmpleo candidato,
			Map<String, String[]> params) throws SQLException, UVException {
		return this.listadoMeritos(convocatoria, bolsa, candidato, params, false);
	}
	
	/**
	 * Lista de bolsas para una convocatoria .
	 * @param convocatoria .
	 * @param params .
	 * @param afinidad .
	 * @return datatable de bolsas de validación .
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
		String consultaCount = "SELECT COUNT(*) FROM UVIRTUAL.TBEP_SOL_BOL_MERITOS bepsbm"
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
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_NO_VALIDADOS, "COUNT_NO_VALIDADOS");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_VALIDADOS, "COUNT_VALIDADOS");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_EXCLUIDOS, "COUNT_EXCLUIDOS");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_TOTAL, "COUNT_TOTAL");

		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());) {
			int paramIndex = 1;
			
			// bucle para rellenar los parámetros de las 4 subconsultas
			while (paramIndex <= TOTAL_COUNT_SUBQUERIES) {
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
	
	private BolsaEmpleoDataTable<BolsaCandidatoValidacionTable> listadoAreasCandidato(Convocatoria convocatoria, UsuarioBolsaEmpleo candidato, Merito merito,  
			Map<String, String[]> params, boolean afinidad) throws SQLException, UVException {
		List<BolsaCandidatoValidacionTable> rows = new ArrayList<>();
		BolsaEmpleoDataTable<BolsaCandidatoValidacionTable> dataTable = new BolsaEmpleoDataTable<BolsaCandidatoValidacionTable>(params);
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();

		if (convocatoria == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = 
				"SELECT bepbol.*, bepare.*,"
				+ " (SELECT COUNT(*) FROM UVIRTUAL.TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm"
				+ "		INNER JOIN UVIRTUAL.TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		WHERE bepsbm.BEPMER_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = bepbol.CODNUM"
				+ "	) AS CONTIENE_MERITO"
				+ "	FROM UVIRTUAL.TBEP_BOLSAS bepbol"
				+ "	INNER JOIN UVIRTUAL.TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUD_BOLSAS bepsbo ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	WHERE bepbol.FLGBAREMABLE = 'S' AND bepsol.BEPCON_CODNUM = ? AND bepsol.BEPUSU_CODNUM = ?";

		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_AREA, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");

		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, merito.getCodNum());
			stmtCount.setInt(paramIndex++, merito.getCodNum());
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex, candidato.getCodNum());
			stmtCount.setInt(paramIndex++, candidato.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bol = modeloBolsa.createFromResultSet(rs);
					Boolean contieneMerito = rs.getBoolean("CONTIENE_MERITO");
					rows.add(new BolsaCandidatoValidacionTable(bol, contieneMerito));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/**
	 * Lista de candidatos para una bolsa .
	 * @param convocatoria .
	 * @param bolsa .
	 * @param params .
	 * @param afinidad .
	 * @return datatable de candidatos de validación .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private BolsaEmpleoDataTable<CandidatoValidacion> listadoCandidatos(Convocatoria convocatoria, Bolsa bolsa, Map<String, String[]> params, boolean afinidad) 
			throws SQLException, UVException {
		List<CandidatoValidacion> rows = new ArrayList<>();
		BolsaEmpleoDataTable<CandidatoValidacion> dataTable = new BolsaEmpleoDataTable<CandidatoValidacion>(params);

		if (convocatoria == null || bolsa == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		// subconsultas para seleccionar el número de méritos que contiene cada bolsa de la solicitud
		// en la convocatoria pasada, agregando después varios filtros
		String consultaCount = "SELECT COUNT(*) FROM UVIRTUAL.TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ "	WHERE bepsol.ESTADO = 'CERRADA' AND bepsbo.BEPBOL_CODNUM = ? AND bepsol.BEPCON_CODNUM = ? AND bepmer.BEPUSU_CODNUM = bepusu.CODNUM";
		
		// agrega el filtro de afinidad
		consultaCount += afinidad ? " AND bepite.AFINIDAD IS NOT NULL " : " AND bepite.AFINIDAD IS NULL ";
		
		// seleccionamos los candidatos, con meritos, en la convocatoria pasada
		String consulta = 
				"SELECT bepusu.CODNUM, bepusu.PRSNIF,"
				+ "	(" + consultaCount + " AND bepsbm.FLGVALIDADO = 'N' AND bepsbm.FLGEXCLUIDO = 'N') COUNT_NO_VALIDADOS,"
				+ "	(" + consultaCount + " AND bepsbm.FLGVALIDADO = 'S' AND bepsbm.FLGEXCLUIDO = 'N') COUNT_VALIDADOS,"
				+ "	(" + consultaCount + " AND bepsbm.FLGEXCLUIDO = 'S') COUNT_EXCLUIDOS,"
				+ "	(" + consultaCount + ") COUNT_TOTAL"
				+ "	FROM UVIRTUAL.TBEP_USUARIOS bepusu"
				+ "	INNER JOIN UVIRTUAL.TBEP_MERITOS bepmer ON bepusu.CODNUM = bepmer.BEPUSU_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOL_BOL_MERITOS bepsbm ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "	WHERE bepusu.ROL = " + ModeloRol.ID_ROL_CANDIDATO + " AND bepsbo.BEPBOL_CODNUM = ?";
		
		// agrega el filtro de afinidad
		consulta += afinidad ? " AND bepite.AFINIDAD IS NOT NULL" : " AND bepite.AFINIDAD IS NULL";
		
		consulta += " GROUP BY bepusu.CODNUM, bepusu.PRSNIF";

		dataTable.setColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO_CANDIDATO, "bepusu.PRSNIF");

		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());) {
			int paramIndex = 1;
			
			// bucle para rellenar los parámetros de las 4 subconsultas
			while (paramIndex <= TOTAL_COUNT_SUBQUERIES * 2) {
				stmt.setInt(paramIndex, bolsa.getCodNum());
				stmtCount.setInt(paramIndex++, bolsa.getCodNum());
				stmt.setInt(paramIndex, convocatoria.getCodNum());
				stmtCount.setInt(paramIndex++, convocatoria.getCodNum());
			}
			
			stmt.setInt(paramIndex, bolsa.getCodNum());
			stmtCount.setInt(paramIndex++, bolsa.getCodNum());

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";
					
					 try (Connection conexionArcos = ConexionArcos.obtenerInstancia();
				    	PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos);) {
				    	stmtArcos.setString(1, rs.getString("PRSNIF"));
				    	try (ResultSet rsArcos = stmtArcos.executeQuery();) {
					    	while (rsArcos.next()) {
								CandidatoValidacion candidato = createCandidatoFromResultSet(rs, rsArcos);
								rows.add(candidato);
					    	}
				    	}
				    }
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/**
	 * Lista de meritos de un candidato para una bolsa .
	 * @param convocatoria .
	 * @param bolsa .
	 * @param candidato .
	 * @param params .
	 * @param afinidad .
	 * @return datatable de méritos de validación .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private BolsaEmpleoDataTable<MeritoValidarTable> listadoMeritos(Convocatoria convocatoria, Bolsa bolsa, UsuarioBolsaEmpleo candidato,
			Map<String, String[]> params, boolean afinidad) throws SQLException, UVException {
		List<MeritoValidarTable> rows = new ArrayList<>();
		BolsaEmpleoDataTable<MeritoValidarTable> dataTable = new BolsaEmpleoDataTable<MeritoValidarTable>(params);
		ModeloMerito modeloMerito = ModeloMerito.obtenerInstancia();
		
		if (convocatoria == null || bolsa == null || candidato == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = "SELECT bepmer.*, bepite.*, bepsbm.*"
				+ " FROM UVIRTUAL.TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ "	WHERE bepsol.ESTADO = 'CERRADA' AND bepsbo.BEPBOL_CODNUM = ? AND bepsol.BEPCON_CODNUM = ? AND bepmer.BEPUSU_CODNUM = ?";
		
		// agrega el filtro de afinidad
		consulta += afinidad ? " AND bepite.AFINIDAD IS NOT NULL" : " AND bepite.AFINIDAD IS NULL";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_MERITO, "bepmer.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO_MERITO, "bepmer.BEPITE_CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_MERITO, "bepite.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_VALOR_MERITO, "bepite.VALOR");
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());) {
			int paramIndex = 1;
			
			stmt.setInt(paramIndex, bolsa.getCodNum());
			stmtCount.setInt(paramIndex++, bolsa.getCodNum());
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex, candidato.getCodNum());
			stmtCount.setInt(paramIndex++, candidato.getCodNum());

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Merito merito = modeloMerito.createMeritoFromResultset(rs, false, true);
					Boolean excluido = rs.getString("FLGEXCLUIDO").equals("S");
					Boolean validado = rs.getString("FLGVALIDADO").equals("S");
					rows.add(new MeritoValidarTable(merito, excluido, validado));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	public BolsaEmpleoDataTable<ValorMeritoBolsaTable> listadoValoresMeritoBolsa(Convocatoria convocatoria, UsuarioBolsaEmpleo candidato, Merito merito, 
			Map<String, String[]> params) throws SQLException, UVException {
		List<ValorMeritoBolsaTable> rows = new ArrayList<>();
		BolsaEmpleoDataTable<ValorMeritoBolsaTable> dataTable = new BolsaEmpleoDataTable<ValorMeritoBolsaTable>(params);
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		if (convocatoria == null || merito == null || candidato == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = 
				"SELECT bepbol.*, bepare.*, bepsbm.BEPMER_CODNUM, bepsbm.CODNUM AS BEPSBM_CODNUM"
				+ "	FROM UVIRTUAL.TBEP_BOLSAS bepbol"
				+ "	INNER JOIN UVIRTUAL.TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUD_BOLSAS bepsbo ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ "	INNER JOIN UVIRTUAL.TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	LEFT JOIN UVIRTUAL.TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM AND bepsbm.BEPMER_CODNUM = ?"
				+ "	WHERE bepbol.FLGBAREMABLE = 'S' AND bepsol.BEPCON_CODNUM = ? AND bepsol.BEPUSU_CODNUM = ?";
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, merito.getCodNum());
			stmtCount.setInt(paramIndex++, merito.getCodNum());
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex, candidato.getCodNum());
			stmtCount.setInt(paramIndex++, candidato.getCodNum());

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bol = modeloBolsa.createFromResultSet(rs);
					int idMeritoSolicitud = rs.getInt("BEPSBM_CODNUM");
					MeritoSolicitud ms = idMeritoSolicitud != 0 ? modeloSolicitud.getMeritoSolicitudById(idMeritoSolicitud)
							: new MeritoSolicitud(merito, false);
					rows.add(new ValorMeritoBolsaTable(ms, bol));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/**
	 * Validar méritos no afines .
	 * @param meritos .
	 */
	public void validarMeritos(Collection<String> meritosSolicitud) throws SQLException, UVException {
		
		for (String mer : meritosSolicitud) {
			
		}
		
	}
	
	/**
	 * Crea un candidato validación a partir de un ResultSet.
	 * @param rs .
	 * @param rsArcos .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private CandidatoValidacion createCandidatoFromResultSet(ResultSet rs, ResultSet rsArcos) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		usuario.setTipoDocumento(rsArcos.getString("STRTIPODOCUMENTO"));
		usuario.setNumDocumento(rsArcos.getString("PRSNIF"));
		usuario.setNombre(rsArcos.getString("STRNOMBRE"));
		usuario.setPrimerApellido(rsArcos.getString("STRAPELLIDO1"));
		usuario.setSegundoApellido(rsArcos.getString("STRAPELLIDO2"));
		usuario.setCodNum(rs.getInt("CODNUM"));
		
		CandidatoValidacion candidato = new CandidatoValidacion(usuario);
		candidato.setTotalMeritosNoValidados(rs.getInt("COUNT_TOTAL"));
		candidato.setTotalMeritosValidados(rs.getInt("COUNT_VALIDADOS"));
		candidato.setTotalMeritosExcluidos(rs.getInt("COUNT_EXCLUIDOS"));
		candidato.setTotalMeritos(rs.getInt("COUNT_TOTAL"));
		
		return candidato;
	}
	
}
