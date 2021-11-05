package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoValidarTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ValorMeritoBolsaTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
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
	public static final int ORDER_COLUMN_INDEX_COUNT_NO_VALIDADOS_CANDIDATO = 2;
	public static final int ORDER_COLUMN_INDEX_COUNT_VALIDADOS_CANDIDATO = 3;
	public static final int ORDER_COLUMN_INDEX_COUNT_EXCLUIDOS_CANDIDATO = 4;
	public static final int ORDER_COLUMN_INDEX_COUNT_TOTAL_CANDIDATO = 5;
	
	public static final int ORDER_COLUMN_INDEX_ID_MERITO = 0;
	public static final int ORDER_COLUMN_INDEX_ESTADO_MERITO = 1;
	public static final int ORDER_COLUMN_INDEX_CODIGO_MERITO = 2;
	public static final int ORDER_COLUMN_INDEX_VALOR_MERITO = 3;
	
	public static final int ORDER_COLUMN_INDEX_ID_CONVOCATORIA_VALORES = 0;
	public static final int ORDER_COLUMN_INDEX_ID_AREA_VALORES = 1;
	public static final int ORDER_COLUMN_INDEX_ITEM_VALORES = 2;
	public static final int ORDER_COLUMN_INDEX_VALOR_MERITO_VALORES = 3;
	
	public static final String TOTAL_NO_VALIDADO = "TOTAL_NO_VALIDADO";
	public static final String TOTAL_VALIDADO = "TOTAL_VALIDADO";
	public static final String TOTAL_EXCLUIDO = "TOTAL_EXCLUIDO";
	public static final String TOTAL_MERITOS = "TOTAL_MERITOS";
	
	public static final String MERITO_EXCLUIDO = "S";
	public static final String MERITO_VALIDADO = "S";	

	protected static ModeloValidar eInstancia;

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
	
	/** Devuelve las bolsas en las que está inscrito el usuario.
	 * @param convocatoria .
	 * @param candidato .
	 * @param merito .
	 * @param usuario .
	 * @return bolsas .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException .
	 */
	public List<ValorMeritoBolsaTable> listadoAreasCandidatoSujetasAfinidad(Convocatoria convocatoria, UsuarioBolsaEmpleo candidato, MeritoSolicitud merito, 
			UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		List<ValorMeritoBolsaTable> bolsas = new ArrayList<>();
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		boolean evaluador = usuario.getRol().getValor().equals(ModeloRol.ROL_MIEMBRO_COMISION) 
				|| usuario.getRol().getValor().equals(ModeloRol.ROL_DIRECTOR_DEPARTAMENTO);
		
		String consulta = 
				"SELECT bepbol.*, bepare.*, bepsbm.BEPMER_CODNUM, bepsbm.CODNUM AS BEPSBM_CODNUM"
				+ "	FROM TBEP_BOLSAS bepbol"
				+ "	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ (evaluador ? " INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepare.CODNUM AND bepeva.FLGACTIVO = 'S'" : "")
				+ "	INNER JOIN TBEP_SOL_BOL_MERITOS bepsbm ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM AND bepsbm.BEPMER_CODNUM = ?"
				+ "	WHERE bepbol.FLGBAREMABLE = 'S' AND bepsol.BEPCON_CODNUM = ? AND bepsol.BEPUSU_CODNUM = ?"
				+ (evaluador ? " AND bepeva.BEPUSU_CODNUM = ?" : "");
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, merito.getMerito().getCodNum());
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, candidato.getCodNum());

			if (evaluador) {
				stmt.setInt(paramIndex++, usuario.getCodNum());
			}

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bol = modeloBolsa.createFromResultSet(rs);
					int idMeritoSolicitud = rs.getInt("BEPSBM_CODNUM");
					MeritoSolicitud ms = new MeritoSolicitud();
					ms.setMerito(merito.getMerito());
					ms.setValoraciones(merito.getValoraciones());
					ms.setExcluido(false);
					ms.setValidado(false);
					List<MeritoSolicitudValoracion> valoraciones = new ArrayList<>();

					if (idMeritoSolicitud != 0) {
						ms = modeloSolicitud.getMeritoSolicitudById(idMeritoSolicitud);
						valoraciones = modeloSolicitud.getValoracionesMeritoSolicitud(idMeritoSolicitud, false);
						ms.setValoraciones(valoraciones);
					}
					
					bolsas.add(new ValorMeritoBolsaTable(ms, bol));
				}
			}
		}
		return bolsas;
	}
	
	/**
	 * Lista de bolsas no sujetas a afinidad para una convocatoria .
	 * @param convocatoria .
	 * @param params .
	 * @return datatable de bolsas de validación .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<BolsaValidacion> listadoAreasNoSujetasAfinidad(Convocatoria convocatoria, Map<String, String[]> params) 
			throws SQLException, UVException {
		List<BolsaValidacion> rows = new ArrayList<>();
		BolsaEmpleoDataTable<BolsaValidacion> dataTable = new BolsaEmpleoDataTable<>(params);

		if (convocatoria == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = 
				"SELECT bepbol.*, counts.COUNT_NO_VALIDADOS, counts.COUNT_VALIDADOS, counts.COUNT_EXCLUIDOS, counts.COUNT_TOTAL"
				+ "	FROM TBEP_BOLSAS bepbol"
				+ "	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ "	LEFT JOIN ("
				+ "		SELECT"
				+ "			SUM(CASE WHEN bepsbm.FLGVALIDADO = 'N' AND bepsbm.FLGEXCLUIDO = 'N' THEN 1 ELSE 0 END) AS COUNT_NO_VALIDADOS,"
				+ "			SUM(CASE WHEN bepsbm.FLGVALIDADO = 'S' AND bepsbm.FLGEXCLUIDO = 'N' THEN 1 ELSE 0 END) AS COUNT_VALIDADOS,"
				+ "			SUM(CASE WHEN bepsbm.FLGEXCLUIDO = 'S' THEN 1 ELSE 0 END) AS COUNT_EXCLUIDOS,"
				+ "			COUNT(*) AS COUNT_TOTAL,"
				+ "			bepsbo.BEPBOL_CODNUM "
				+ "		FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "		INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "		INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "		INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ "		INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepmer.BEPUSU_CODNUM"
				+ "		WHERE 	bepsol.BEPCON_CODNUM = ?"
				+ "			AND bepsol.FLGEXCLUIDO = 'N'"
				+ "			AND bepite.AFINIDAD IS NULL"
				+ "			AND bepusu.FLGBORRADO = 'N'"
				+ "			AND bepusu.ROL = " + ModeloRol.ID_ROL_CANDIDATO
				+ "			AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'"
				+ "			GROUP BY bepsbo.BEPBOL_CODNUM"
				+ "	) counts ON counts.BEPBOL_CODNUM = bepbol.CODNUM"
				+ "	WHERE bepbol.FLGBAREMABLE = 'S'";
		
		String orderByNoValidados = "CASE WHEN COUNT_NO_VALIDADOS IS NULL THEN 1 ELSE 0 END, COUNT_NO_VALIDADOS %s";
		String orderByValidados = "CASE WHEN COUNT_NO_VALIDADOS IS NULL THEN 1 ELSE 0 END, COUNT_VALIDADOS %s";
		String orderByExcluidos = "CASE WHEN COUNT_NO_VALIDADOS IS NULL THEN 1 ELSE 0 END, COUNT_EXCLUIDOS %s";
		String orderByTotal = "CASE WHEN COUNT_NO_VALIDADOS IS NULL THEN 1 ELSE 0 END, COUNT_TOTAL %s";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_AREA, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_NO_VALIDADOS, "COUNT_NO_VALIDADOS", DataTableColumn.COLUMN_TYPE_NUMBER, orderByNoValidados);
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_VALIDADOS, "COUNT_VALIDADOS", DataTableColumn.COLUMN_TYPE_NUMBER, orderByValidados);
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_EXCLUIDOS, "COUNT_EXCLUIDOS", DataTableColumn.COLUMN_TYPE_NUMBER, orderByExcluidos);
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_TOTAL, "COUNT_TOTAL", DataTableColumn.COLUMN_TYPE_NUMBER, orderByTotal);

		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					rows.add(createBolsaValidacionFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/**
	 * Lista de bolsas sujetas a afinidad para una convocatoria .
	 * @param convocatoria .
	 * @param usuario .
	 * @param params .
	 * @return datatable de bolsas de validación .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	@SuppressWarnings({"checkstyle:ExecutableStatementCount", "checkstyle:NPathComplexity"})
	public BolsaEmpleoDataTable<BolsaValidacion> listadoAreasSujetasAfinidad(Convocatoria convocatoria, UsuarioBolsaEmpleo usuario, Map<String, String[]> params) 
			throws SQLException, UVException {
		List<BolsaValidacion> rows = new ArrayList<>();
		BolsaEmpleoDataTable<BolsaValidacion> dataTable = new BolsaEmpleoDataTable<>(params);

		if (convocatoria == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		boolean evaluador = usuario.getRol().getValor().equals(ModeloRol.ROL_MIEMBRO_COMISION) 
				|| usuario.getRol().getValor().equals(ModeloRol.ROL_DIRECTOR_DEPARTAMENTO);
		
		String consulta = 
				"SELECT bepbol.CODNUM, bepbol.BEPARE_CODNUM, bepbol.ESTADO, bepbol.FLGBAREMABLE, bepbol.FECHAACTUALIZACION,"
				+ "		bepbol.FECHABLOQUEO, bepbol.FECHADEBLOQUEO, bepbol.FECHABAREMACION,"
				+ "		counts.COUNT_NO_VALIDADOS, counts.COUNT_VALIDADOS, counts.COUNT_EXCLUIDOS, counts.COUNT_TOTAL"
				+ "	FROM TBEP_BOLSAS bepbol"
				+ "	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ (evaluador ? " INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepare.CODNUM AND bepeva.FLGACTIVO = 'S'" : "")
				+ "	LEFT JOIN("
				+ "		SELECT"
				+ "			SUM(CASE WHEN bepsbm.FLGVALIDADO = 'N' AND bepsbm.FLGEXCLUIDO = 'N' THEN 1 ELSE 0 END) AS COUNT_NO_VALIDADOS,"
				+ "			SUM(CASE WHEN bepsbm.FLGVALIDADO = 'S' AND bepsbm.FLGEXCLUIDO = 'N' THEN 1 ELSE 0 END) AS COUNT_VALIDADOS,"
				+ "			SUM(CASE WHEN bepsbm.FLGEXCLUIDO = 'S' THEN 1 ELSE 0 END) AS COUNT_EXCLUIDOS,"
				+ "			COUNT(*) AS COUNT_TOTAL,"
				+ "			bepsbo.BEPBOL_CODNUM"
				+ "		FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "		INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "		INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "		INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ "		INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepmer.BEPUSU_CODNUM"
				+ "		WHERE"
				+ "				bepusu.ROL = " + ModeloRol.ID_ROL_CANDIDATO
				+ "			AND bepsol.BEPCON_CODNUM = ?"
				+ "			AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'"
				+ "			AND bepite.AFINIDAD IS NOT NULL"
				+ "			AND bepusu.FLGBORRADO = 'N'"
				+ "		GROUP BY bepsbo.BEPBOL_CODNUM"
				+ "	) counts ON counts.BEPBOL_CODNUM = bepbol.CODNUM"
				+ "	WHERE bepbol.FLGBAREMABLE = 'S'"
				+ (evaluador ? " AND bepeva.BEPUSU_CODNUM = ?" : "")
				+ (usuario.getRol().getValor().equals(ModeloRol.ROL_MIEMBRO_COMISION) ? " AND bepbol.ESTADO = ? " : "");
		
		String orderByNoValidados = "CASE WHEN COUNT_NO_VALIDADOS IS NULL THEN 1 ELSE 0 END, COUNT_NO_VALIDADOS %s";
		String orderByValidados = "CASE WHEN COUNT_NO_VALIDADOS IS NULL THEN 1 ELSE 0 END, COUNT_VALIDADOS %s";
		String orderByExcluidos = "CASE WHEN COUNT_NO_VALIDADOS IS NULL THEN 1 ELSE 0 END, COUNT_EXCLUIDOS %s";
		String orderByTotal = "CASE WHEN COUNT_NO_VALIDADOS IS NULL THEN 1 ELSE 0 END, COUNT_TOTAL %s";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_AREA, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_NO_VALIDADOS, "COUNT_NO_VALIDADOS", DataTableColumn.COLUMN_TYPE_NUMBER, orderByNoValidados);
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_VALIDADOS, "COUNT_VALIDADOS", DataTableColumn.COLUMN_TYPE_NUMBER, orderByValidados);
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_EXCLUIDOS, "COUNT_EXCLUIDOS", DataTableColumn.COLUMN_TYPE_NUMBER, orderByExcluidos);
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_TOTAL, "COUNT_TOTAL", DataTableColumn.COLUMN_TYPE_NUMBER, orderByTotal);

		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());
			
			if (evaluador) {
				stmt.setInt(paramIndex, usuario.getCodNum());
				stmtCount.setInt(paramIndex++, usuario.getCodNum());
				
				if (usuario.getRol().getValor().equals(ModeloRol.ROL_MIEMBRO_COMISION)) {
					stmt.setString(paramIndex, ModeloBolsa.BOLSA_ESTADO_BAREMACION);
					stmtCount.setString(paramIndex++, ModeloBolsa.BOLSA_ESTADO_BAREMACION);
				}
			}

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					rows.add(createBolsaValidacionFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/** 
	 * Lista de bolsas para la vista de no sujetas a afinidad en las que se apuntó un candidato .
	 * @param convocatoria .
	 * @param candidato .
	 * @param merito .
	 * @param params .
	 * @return datatable de bolsas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<Bolsa> listadoAreasCandidatoNoSujetasAfinidad(Convocatoria convocatoria, UsuarioBolsaEmpleo candidato, Merito merito,  
			Map<String, String[]> params) throws SQLException, UVException {
		List<Bolsa> rows = new ArrayList<>();
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<>(params);
		
		if (convocatoria == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = 
				"SELECT bepbol.CODNUM, bepbol.BEPARE_CODNUM, bepbol.ESTADO, bepbol.FLGBAREMABLE, bepbol.FECHAACTUALIZACION, "
				+ "	bepbol.FECHABLOQUEO, bepbol.FECHADEBLOQUEO, bepbol.FECHABAREMACION, bepare.ID_AREA_CONOCIMIENTO"
				+ "	FROM TBEP_BOLSAS bepbol"
				+ "	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	INNER JOIN TBEP_SOL_BOL_MERITOS bepsbm ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "	WHERE bepbol.FLGBAREMABLE = 'S' AND bepsol.BEPCON_CODNUM = ? AND bepsol.BEPUSU_CODNUM = ? AND bepsbm.BEPMER_CODNUM = ?";

		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_AREA, "bepare.ID_AREA_CONOCIMIENTO");

		dataTable.setQuery(consulta);
		
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex, candidato.getCodNum());
			stmtCount.setInt(paramIndex++, candidato.getCodNum());
			stmt.setInt(paramIndex, merito.getCodNum());
			stmtCount.setInt(paramIndex++, merito.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					rows.add(modeloBolsa.createFromResultSet(rs));
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
		BolsaEmpleoDataTable<CandidatoValidacion> dataTable = new BolsaEmpleoDataTable<>(params);
		
		if (convocatoria == null || bolsa == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = "SELECT bepusu.CODNUM, bepusu.CODCUENTA, bepusu.VUAJA_STRTIPODOCUMENTO, bepusu.VUAJA_IDNIF, bepusu.VUAJA_LETRANIF,"
				+ "	bepusu.VUAJA_PRSNIF, bepusu.VUAJA_STRNOMBRE, bepusu.VUAJA_STRAPELLIDO1, bepusu.VUAJA_STRAPELLIDO2, bepusu.VUAJA_EMAIL_ALTA,"
				+ "	SUM(CASE WHEN bepsbm.FLGVALIDADO = 'N' AND bepsbm.FLGEXCLUIDO = 'N' THEN 1 ELSE 0 END) AS COUNT_NO_VALIDADOS,"
				+ "	SUM(CASE WHEN bepsbm.FLGVALIDADO = 'S' AND bepsbm.FLGEXCLUIDO = 'N' THEN 1 ELSE 0 END) AS COUNT_VALIDADOS,"
				+ "	SUM(CASE WHEN bepsbm.FLGEXCLUIDO = 'S' THEN 1 ELSE 0 END) AS COUNT_EXCLUIDOS,"
				+ "	COUNT(*) AS COUNT_TOTAL"
				+ "	FROM TBEP_USUARIOS bepusu"
				+ "	INNER JOIN TBEP_MERITOS bepmer ON bepusu.CODNUM = bepmer.BEPUSU_CODNUM"
				+ "	INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ "	INNER JOIN TBEP_SOL_BOL_MERITOS bepsbm ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	WHERE	bepusu.ROL = " + ModeloRol.ID_ROL_CANDIDATO
				+ "		AND bepsol.FLGEXCLUIDO = 'N'"
				+ "		AND bepsbo.BEPBOL_CODNUM = ?"
				+ "		AND bepsol.BEPCON_CODNUM = ?"
				+ "		AND bepusu.FLGBORRADO = 'N'"
				+ "		AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'"
				+ (afinidad ? " AND bepite.AFINIDAD IS NOT NULL" : " AND bepite.AFINIDAD IS NULL")
				+ "	GROUP BY bepusu.CODNUM, bepusu.CODCUENTA, bepusu.VUAJA_STRTIPODOCUMENTO, bepusu.VUAJA_IDNIF, bepusu.VUAJA_LETRANIF,"
				+ "	bepusu.VUAJA_PRSNIF, bepusu.VUAJA_STRNOMBRE, bepusu.VUAJA_STRAPELLIDO1, bepusu.VUAJA_STRAPELLIDO2, bepusu.VUAJA_EMAIL_ALTA ";

		dataTable.setColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO_CANDIDATO, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_CANDIDATO, "bepusu.VUAJA_STRNOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_NO_VALIDADOS_CANDIDATO, "COUNT_NO_VALIDADOS");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_VALIDADOS_CANDIDATO, "COUNT_VALIDADOS");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_EXCLUIDOS_CANDIDATO, "COUNT_EXCLUIDOS");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COUNT_TOTAL_CANDIDATO, "COUNT_TOTAL");

		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, bolsa.getCodNum());
			stmtCount.setInt(paramIndex++, bolsa.getCodNum());
			stmt.setInt(paramIndex, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex++, convocatoria.getCodNum());

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {				
				while (rs.next()) {
					CandidatoValidacion candidato = createCandidatoValidacionFromResultSet(rs);
					rows.add(candidato);
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
	@SuppressWarnings({"checkstyle:ExecutableStatementCount"})
	private BolsaEmpleoDataTable<MeritoValidarTable> listadoMeritos(Convocatoria convocatoria, Bolsa bolsa, UsuarioBolsaEmpleo candidato,
			Map<String, String[]> params, boolean afinidad) throws SQLException, UVException {
		List<MeritoValidarTable> rows = new ArrayList<>();
		BolsaEmpleoDataTable<MeritoValidarTable> dataTable = new BolsaEmpleoDataTable<>(params);
				
		if (convocatoria == null || bolsa == null || candidato == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = "SELECT bepmer.CODNUM, bepmer.BEPITE_CODNUM, bepmer.VALOR, bepmer.DESCRIPCION,"
				+ "		bepmer.OBSERVACION, bepsbm.FLGEXCLUIDO, bepsbm.FLGVALIDADO"
				+ " FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "	INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM"
				+ "	WHERE bepsol.ESTADO = 'CERRADA' AND bepsbo.BEPBOL_CODNUM = ? AND bepsol.BEPCON_CODNUM = ? AND bepmer.BEPUSU_CODNUM = ?";
		
		// agrega el filtro de afinidad
		consulta += afinidad ? " AND bepite.AFINIDAD IS NOT NULL" : " AND bepite.AFINIDAD IS NULL";
		
		String whereCodigo = String.format("(%s || '.' || %s || '.' || %s)", "bepapa.CODIGO", "bepblo.CODIGO", "bepite.CODIGO");
		String orderCodigo = String.format("(%s || '.' || %s || '.' || %s) %%s", "LPAD(bepapa.CODIGO, 3)", "LPAD(bepblo.CODIGO, 3)", "LPAD(bepite.CODIGO, 3)");
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_MERITO, "bepmer.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_MERITO, whereCodigo, DataTableColumn.COLUMN_TYPE_TEXT, orderCodigo);
		dataTable.setColumn(ORDER_COLUMN_INDEX_VALOR_MERITO, "bepmer.VALOR");
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
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
					Merito merito = new Merito();
					merito.setCodNum(rs.getInt("CODNUM"));
					merito.setItemBaremacion(ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));	
					merito.setValor(rs.getDouble("VALOR"));
					merito.setDescripcion(rs.getString("DESCRIPCION"));
					merito.setObservacion(rs.getString("OBSERVACION"));
					
					Boolean excluido = rs.getString("FLGEXCLUIDO").equals(MERITO_EXCLUIDO);
					Boolean validado = rs.getString("FLGVALIDADO").equals(MERITO_VALIDADO);
					
					rows.add(new MeritoValidarTable(merito, excluido, validado));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/** datatable que devuelve un historial de méritos con su valor en una bolsa .
	 * @param candidato .
	 * @param merito .
	 * @param params .
	 * @return valores méritos .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	@SuppressWarnings({"checkstyle:ExecutableStatementCount"})
	public BolsaEmpleoDataTable<ValorMeritoBolsaTable> listadoValoresMeritoBolsa(UsuarioBolsaEmpleo candidato, Merito merito, 
			Map<String, String[]> params) throws SQLException, UVException {
		List<ValorMeritoBolsaTable> rows = new ArrayList<>();
		BolsaEmpleoDataTable<ValorMeritoBolsaTable> dataTable = new BolsaEmpleoDataTable<>(params);
						
		if (merito == null || candidato == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = "SELECT bepbol.CODNUM, bepbol.BEPARE_CODNUM, bepbol.ESTADO, bepbol.FLGBAREMABLE, bepbol.FECHAACTUALIZACION,"
				+ "		bepbol.FECHABLOQUEO, bepbol.FECHADEBLOQUEO, bepbol.FECHABAREMACION, bepsbm.CODNUM AS BEPSBM_CODNUM, bepsol.BEPCON_CODNUM,"
				+ "		bepsbm.UID_USUARIO AS UID_EVALUADOR"
				+ "	FROM TBEP_BOLSAS bepbol"
				+ "	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	INNER JOIN TBEP_SOL_BOL_MERITOS bepsbm ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM AND bepsbm.BEPMER_CODNUM = ?"
				+ "	LEFT JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepsbm.BEPITE_CODNUM"
				+ "	LEFT JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ "	LEFT JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM"
				+ "	WHERE bepbol.FLGBAREMABLE = 'S' AND bepsol.BEPUSU_CODNUM = ?";
		
		String whereCodigo = String.format("(%s || '.' || %s || '.' || %s)", "bepapa.CODIGO", "bepblo.CODIGO", "bepite.CODIGO");
		String orderCodigo = String.format("(%s || '.' || %s || '.' || %s) %%s", "LPAD(bepapa.CODIGO, 3)", "LPAD(bepblo.CODIGO, 3)", "LPAD(bepite.CODIGO, 3)");
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_CONVOCATORIA_VALORES, "bepsbm.BEPMER_CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_AREA_VALORES, "bepbol.BEPARE_CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEM_VALORES, whereCodigo, DataTableColumn.COLUMN_TYPE_TEXT, orderCodigo);
		dataTable.setColumn(ORDER_COLUMN_INDEX_VALOR_MERITO_VALORES, "bepsbm.VALOR", DataTableColumn.COLUMN_TYPE_NUMBER);
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, merito.getCodNum());
			stmtCount.setInt(paramIndex++, merito.getCodNum());
			stmt.setInt(paramIndex, candidato.getCodNum());
			stmtCount.setInt(paramIndex++, candidato.getCodNum());

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);
			
			ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
			ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bol = modeloBolsa.createFromResultSet(rs);
					Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(rs.getInt("BEPCON_CODNUM"));
					int idMeritoSolicitud = rs.getInt("BEPSBM_CODNUM");
					String uidUsuario = rs.getString("UID_EVALUADOR");
					rows.add(new ValorMeritoBolsaTable(modeloSolicitud.getMeritoSolicitudById(idMeritoSolicitud), convocatoria, bol, uidUsuario));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/** Método para evaluar un mérito en una bolsa .
	 * @param idBolsa .
	 * @param merito .
	 * @param convocatoria .
	 * @param usuarioUpdate .
	 * @param valida .
	 * @throws SQLException .
	 */
	public void evaluarMerito(String idBolsa, MeritoSolicitud merito, Convocatoria convocatoria, UsuarioBolsaEmpleo usuarioUpdate, boolean valida)
			throws SQLException {
		String consulta = 
				"	UPDATE"
				+ " ("
				+ "		SELECT bepsbm.FLGEXCLUIDO AS EXCLUIDO, bepsbm.FLGVALIDADO AS VALIDADO, bepsbm.OBSERVACION_CANDIDATO AS OBSERVACION,"
				+ "			bepsbm.VALOR AS VALOR, bepsbm.BEPITE_CODNUM AS BEPITE_CODNUM, bepsbm.UID_USUARIO AS UID_USUARIO"
				+ "  	FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ " 	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "  	WHERE bepsbo.BEPBOL_CODNUM = ? AND bepsbm.BEPMER_CODNUM = ? AND bepsol.BEPCON_CODNUM = ?"
				+ " ) MERITO"
				+ " SET EXCLUIDO = ?, VALIDADO = ?, OBSERVACION = ?, VALOR = ?, BEPITE_CODNUM = ?, UID_USUARIO = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmtUpdate = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmtUpdate.setString(indexParam++, idBolsa);
			stmtUpdate.setInt(indexParam++, merito.getMerito().getCodNum());
			stmtUpdate.setInt(indexParam++, convocatoria.getCodNum());
			stmtUpdate.setString(indexParam++, valida ? "N" : "S");
			stmtUpdate.setString(indexParam++, valida ? "S" : "N");
			stmtUpdate.setString(indexParam++, merito.getObservacionCandidato());
			stmtUpdate.setDouble(indexParam++, merito.getValor() != null && merito.getValor() != 0 ? merito.getValor() : merito.getMerito().getValor());
			stmtUpdate.setInt(indexParam++, merito.getItem() != null ? merito.getItem().getCodNum() : merito.getMerito().getItemBaremacion().getCodNum());
			stmtUpdate.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmtUpdate.executeUpdate();
		}
	}
	
	/**
	 * Método para excluir un mérito en distintas bolsas .
	 * @param idMerito .
	 * @param observacion .
	 * @param bolsa .
	 * @param convocatoria .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void excluirMeritoEnBolsas(Integer idMerito, String observacion, Bolsa bolsa, Convocatoria convocatoria, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		boolean evaluador = usuarioUpdate.getRol().getValor().equals(ModeloRol.ROL_MIEMBRO_COMISION) 
				|| usuarioUpdate.getRol().getValor().equals(ModeloRol.ROL_DIRECTOR_DEPARTAMENTO);
		
		String consulta = 
				"	UPDATE"
				+ " ("
				+ "		SELECT bepsbm.FLGEXCLUIDO AS EXCLUIDO, bepsbm.FLGVALIDADO AS VALIDADO, bepsbm.OBSERVACION_CANDIDATO AS OBSERVACION,"
				+ "			bepsbm.UID_USUARIO AS UID_USUARIO"
				+ "  	FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "  	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "		INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM AND (bepbol.ESTADO = 'BAREMACION' OR bepbol.CODNUM = ?)"
				+ (evaluador ? (" INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepsbo.BEPBOL_CODNUM AND bepeva.BEPUSU_CODNUM = ?"
							 + "  AND bepeva.FLGACTIVO = 'S'")
							 : "")
				+ "  	WHERE bepsbm.BEPMER_CODNUM = ? AND bepsol.BEPCON_CODNUM = ?"
				+ " ) MERITO"
				+ " SET EXCLUIDO = 'S', VALIDADO = 'N', OBSERVACION = ?, UID_USUARIO = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmtUpdate = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmtUpdate.setInt(indexParam++, bolsa.getCodNum());
			if (evaluador) {
				stmtUpdate.setInt(indexParam++, usuarioUpdate.getCodNum());
			}
			stmtUpdate.setInt(indexParam++, idMerito);
			stmtUpdate.setInt(indexParam++, convocatoria.getCodNum());
			stmtUpdate.setString(indexParam++, observacion);
			stmtUpdate.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmtUpdate.executeUpdate();
		}
	}
	
	/**
	 * Método para validar mérito no afín .
	 * @param idBolsa .
	 * @param idMerito .
	 * @param observacion .
	 * @param convocatoria .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void validaMeritoNoAfines(String idBolsa, Integer idMerito, String observacion, Convocatoria convocatoria, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String consulta = "UPDATE"
				+ " (SELECT bepsbm.FLGEXCLUIDO AS EXCLUIDO, bepsbm.FLGVALIDADO AS VALIDADO, bepsbm.OBSERVACION_CANDIDATO AS OBSERVACION, "
				+ "			bepsbm.UID_USUARIO AS UID_USUARIO, bepsbm.VALOR"
				+ "  	FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ " 	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "  	WHERE bepsbo.BEPBOL_CODNUM = ? AND bepsbm.BEPMER_CODNUM = ? AND bepsol.BEPCON_CODNUM = ?"
				+ " ) MERITO"
				+ " SET EXCLUIDO = 'N', VALIDADO = 'S', OBSERVACION = ?, UID_USUARIO = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmtUpdate = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmtUpdate.setString(indexParam++, idBolsa);
			stmtUpdate.setInt(indexParam++, idMerito);
			stmtUpdate.setInt(indexParam++, convocatoria.getCodNum());
			stmtUpdate.setString(indexParam++, observacion);
			stmtUpdate.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmtUpdate.executeUpdate();
		}
	}
	
	/** Método que elimina las valoraciones de un mérito .
	 * @param merito .
	 * @param solicitud .
	 * @param bolsa .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void borrarValoracionesMerito(Merito merito, Solicitud solicitud, Bolsa bolsa, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			boolean evaluador = usuarioUpdate.getRol().getValor().equals(ModeloRol.ROL_MIEMBRO_COMISION) 
					|| usuarioUpdate.getRol().getValor().equals(ModeloRol.ROL_DIRECTOR_DEPARTAMENTO);
			
			try {
				// actualizamos usuario en valoraciones
				String sqlUpdateValoraciones = "UPDATE TBEP_SOL_BOL_MER_VALORACION"
						+ " SET UID_USUARIO = ?"
						+ " WHERE BEPSBM_CODNUM IN ("
						+ "		SELECT bepsbm.CODNUM"
						+ "		FROM TBEP_SOL_BOL_MERITOS bepsbm"
						+ "		WHERE bepsbm.BEPMER_CODNUM = ? AND bepsbm.BEPSBO_CODNUM IN ("
						+ "			SELECT bepsbo.CODNUM"
						+ "			FROM TBEP_SOLICITUD_BOLSAS bepsbo"
						+ "			INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
						+ "				AND (bepbol.CODNUM = ? OR bepbol.ESTADO = '" + ModeloBolsa.BOLSA_ESTADO_BAREMACION + "')"
						+ (evaluador ? (" "
						+ " 		INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
						+ " 		INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepare.CODNUM"
						+ " 			AND bepeva.BEPUSU_CODNUM = ? AND bepeva.FLGACTIVO = 'S'") : "")
						+ "			WHERE bepsbo.BEPSOL_CODNUM = ?"
						+ "		)"
						+ " )";
				
				try (PreparedStatement stmt = conexion.prepareStatement(sqlUpdateValoraciones)) {
					int indexParam = 1;
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.setInt(indexParam++, merito.getCodNum());
					stmt.setInt(indexParam++, bolsa.getCodNum());
					if (evaluador) {
						stmt.setInt(indexParam++, usuarioUpdate.getCodNum());
					}
					stmt.setInt(indexParam++, solicitud.getCodNum());
					stmt.executeUpdate();
				}
							
				// eliminamos valoraciones
				String sqlValoraciones = ""
						+ "DELETE FROM TBEP_SOL_BOL_MER_VALORACION bepsbv WHERE bepsbv.BEPSBM_CODNUM IN ("
						+ "		SELECT bepsbm.CODNUM"
						+ "		FROM TBEP_SOL_BOL_MERITOS bepsbm"
						+ "		WHERE bepsbm.BEPMER_CODNUM = ? AND bepsbm.BEPSBO_CODNUM IN ("
						+ "			SELECT bepsbo.CODNUM"
						+ "			FROM TBEP_SOLICITUD_BOLSAS bepsbo"
						+ "			INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
						+ "				AND (bepbol.CODNUM = ? OR bepbol.ESTADO = '" + ModeloBolsa.BOLSA_ESTADO_BAREMACION + "')"
						+ (evaluador ? (" "
						+ " 		INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
						+ " 		INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepare.CODNUM"
						+ " 			AND bepeva.BEPUSU_CODNUM = ? AND bepeva.FLGACTIVO = 'S'") : "")
						+ "			WHERE bepsbo.BEPSOL_CODNUM = ?"
						+ "		)"
						+ " )";
				
				try (PreparedStatement stmt = conexion.prepareStatement(sqlValoraciones)) {
					int indexParam = 1;
					stmt.setInt(indexParam++, merito.getCodNum());
					stmt.setInt(indexParam++, bolsa.getCodNum());
					if (evaluador) {
						stmt.setInt(indexParam++, usuarioUpdate.getCodNum());
					}
					stmt.setInt(indexParam++, solicitud.getCodNum());
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
	
	/** Método que elimina las valoraciones de un mérito .
	 * @param merito .
	 * @param solicitud .
	 * @param bolsas .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void borrarValoracionesMeritoBolsas(Merito merito, Solicitud solicitud, Collection<String> bolsas, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				// actualizamos usuario en valoraciones
				String sqlUpdateValoraciones = "UPDATE TBEP_SOL_BOL_MER_VALORACION"
						+ " SET UID_USUARIO = ?"
						+ " WHERE BEPSBM_CODNUM IN ("
						+ "	SELECT bepsbm.CODNUM"
						+ "	FROM TBEP_SOL_BOL_MERITOS bepsbm"
						+ "	WHERE bepsbm.BEPMER_CODNUM = ? AND bepsbm.BEPSBO_CODNUM IN ("
						+ "		SELECT bepsbo.CODNUM"
						+ "		FROM TBEP_SOLICITUD_BOLSAS bepsbo"
						+ "		INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
						+ "			AND bepbol.CODNUM IN (" + BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size()) + ")"
						+ "		WHERE bepsbo.BEPSOL_CODNUM = ?"
						+ "		)"
						+ " )";
				
				try (PreparedStatement stmt = conexion.prepareStatement(sqlUpdateValoraciones)) {
					int indexParam = 1;
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.setInt(indexParam++, merito.getCodNum());
					for (String bolsa: bolsas) {
						stmt.setInt(indexParam++, Integer.parseInt(bolsa));
					}
					stmt.setInt(indexParam++, solicitud.getCodNum());
					stmt.executeUpdate();
				}
							
				// eliminamos valoraciones
				String sqlValoraciones = ""
						+ "DELETE FROM TBEP_SOL_BOL_MER_VALORACION bepsbv WHERE bepsbv.BEPSBM_CODNUM IN ("
						+ "		SELECT bepsbm.CODNUM"
						+ "		FROM TBEP_SOL_BOL_MERITOS bepsbm"
						+ "		WHERE bepsbm.BEPMER_CODNUM = ? AND bepsbm.BEPSBO_CODNUM IN ("
						+ "			SELECT bepsbo.CODNUM"
						+ "			FROM TBEP_SOLICITUD_BOLSAS bepsbo"
						+ "			INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
						+ "				AND bepbol.CODNUM IN (" + BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size()) + ")"
						+ "			WHERE bepsbo.BEPSOL_CODNUM = ?"
						+ "		)"
						+ " )";
				
				try (PreparedStatement stmt = conexion.prepareStatement(sqlValoraciones)) {
					int indexParam = 1;
					stmt.setInt(indexParam++, merito.getCodNum());
					for (String bolsa: bolsas) {
						stmt.setInt(indexParam++, Integer.parseInt(bolsa));
					}
					stmt.setInt(indexParam++, solicitud.getCodNum());
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
	 * Borra las evaluaciones de un mérito en un solicitud.
	 * @param merito .
	 * @param solicitud .
	 * @param bolsa .
	 * @param usuarioUpdate . 
	 * @throws SQLException .
	 */
	public void borrarEvaluacionMerito(MeritoSolicitud merito, Solicitud solicitud, Bolsa bolsa, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		boolean evaluador = usuarioUpdate.getRol().getValor().equals(ModeloRol.ROL_MIEMBRO_COMISION) 
				|| usuarioUpdate.getRol().getValor().equals(ModeloRol.ROL_DIRECTOR_DEPARTAMENTO);
		
		// limpiamos las evaluaciones del mérito para todas las bolsas en baremación
		String sqlUpdateValoraciones = ""
				+ " UPDATE ("
				+ "		SELECT bepsbm.FLGEXCLUIDO, bepsbm.FLGVALIDADO, bepsbm.UID_USUARIO, bepsbm.OBSERVACION_CANDIDATO, bepsbm.VALOR AS VALOR,"
				+ "			CASE WHEN bepbol.CODNUM = ? THEN 1 ELSE 0 END AS BOLSA_ACTUAL, bepsbm.BEPITE_CODNUM AS ITEM"
				+ "		FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "		INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM AND (bepbol.ESTADO = 'BAREMACION' OR bepbol.CODNUM = ?)"
				+ (evaluador ? " "
				+ " 	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ " 	INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepare.CODNUM AND bepeva.BEPUSU_CODNUM = ? AND bepeva.FLGACTIVO = 'S'" : "")
				+ "		WHERE bepsbm.BEPMER_CODNUM  = ? AND bepsbo.BEPSOL_CODNUM  = ?"
				+ ")"
				+ "	SET UID_USUARIO = ?, FLGEXCLUIDO = 'N', FLGVALIDADO = 'N', VALOR = ?, ITEM = ?,"
				+ "		OBSERVACION_CANDIDATO = CASE WHEN BOLSA_ACTUAL = 1 THEN ? ELSE NULL END";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(sqlUpdateValoraciones)) {			
			int indexParam = 1;
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			if (evaluador) {
				stmt.setInt(indexParam++, usuarioUpdate.getCodNum());
			}
			stmt.setInt(indexParam++, merito.getMerito().getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setDouble(indexParam++, merito.getValor());
			stmt.setInt(indexParam++, merito.getItem().getCodNum());
			stmt.setString(indexParam++, merito.getObservacionCandidato());
			stmt.executeUpdate();			
		}
	}
	
	/**
	 * Borra las evaluaciones de un mérito en una solicitud de una lista de bolsas.
	 * @param merito .
	 * @param solicitud .
	 * @param bolsas .
	 * @param usuarioUpdate . 
	 * @throws SQLException .
	 */
	public void borrarEvaluacionMeritoBolsas(MeritoSolicitud merito, Solicitud solicitud, Collection<String> bolsas, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		// limpiamos las evaluaciones del mérito para todas las bolsas en baremación
		String sqlUpdateValoraciones = ""
				+ " UPDATE ("
				+ "		SELECT bepsbm.FLGEXCLUIDO, bepsbm.FLGVALIDADO, bepsbm.UID_USUARIO, bepsbm.OBSERVACION_CANDIDATO, bepsbm.VALOR AS VALOR,"
				+ "			bepsbm.BEPITE_CODNUM AS ITEM"
				+ "		FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "		INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ "		WHERE bepsbm.BEPMER_CODNUM  = ? AND bepsbo.BEPSOL_CODNUM  = ? AND bepbol.CODNUM IN (" 
							+ BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size()) + ")"
				+ ")"
				+ "	SET UID_USUARIO = ?, FLGEXCLUIDO = 'N', FLGVALIDADO = 'N', VALOR = ?, ITEM = ?,"
				+ "		OBSERVACION_CANDIDATO = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(sqlUpdateValoraciones)) {			
			int indexParam = 1;
			stmt.setInt(indexParam++, merito.getMerito().getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (String bolsa: bolsas) {
				stmt.setInt(indexParam++, Integer.parseInt(bolsa));
			}
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setDouble(indexParam++, merito.getValor());
			stmt.setInt(indexParam++, merito.getItem().getCodNum());
			stmt.setString(indexParam++, merito.getObservacionCandidato());
			stmt.executeUpdate();			
		}
	}
	
	/**
	 * Crea un candidato validación a partir de un ResultSet.
	 * @param rs .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private CandidatoValidacion createCandidatoValidacionFromResultSet(ResultSet rs) throws SQLException {
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		usuario.setCodNum(rs.getInt("CODNUM"));
		usuario.setCodCuenta(rs.getString("CODCUENTA"));
		usuario.setTipoDocumento(rs.getString("VUAJA_STRTIPODOCUMENTO"));
		usuario.setIdNif(rs.getString("VUAJA_IDNIF"));
		usuario.setLetraNif(rs.getString("VUAJA_LETRANIF"));
		usuario.setPrsNif(rs.getString("VUAJA_PRSNIF"));
		usuario.setNombre(rs.getString("VUAJA_STRNOMBRE"));
		usuario.setPrimerApellido(rs.getString("VUAJA_STRAPELLIDO1"));
		usuario.setSegundoApellido(rs.getString("VUAJA_STRAPELLIDO2"));
		usuario.setEmail(rs.getString("VUAJA_EMAIL_ALTA"));
		
		CandidatoValidacion candidato = new CandidatoValidacion(usuario);
		candidato.setTotalMeritosNoValidados(rs.getInt("COUNT_NO_VALIDADOS"));
		candidato.setTotalMeritosValidados(rs.getInt("COUNT_VALIDADOS"));
		candidato.setTotalMeritosExcluidos(rs.getInt("COUNT_EXCLUIDOS"));
		candidato.setTotalMeritos(rs.getInt("COUNT_TOTAL"));
		
		return candidato;
	}
	
	private BolsaValidacion createBolsaValidacionFromResultSet(ResultSet rs) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		Bolsa bolsa = modeloBolsa.createFromResultSet(rs);
		BolsaValidacion bolsaValidacion = new BolsaValidacion(bolsa);
		bolsaValidacion.setTotalMeritosNoValidados(rs.getInt("COUNT_NO_VALIDADOS"));
		bolsaValidacion.setTotalMeritosValidados(rs.getInt("COUNT_VALIDADOS"));
		bolsaValidacion.setTotalMeritosExcluidos(rs.getInt("COUNT_EXCLUIDOS"));
		bolsaValidacion.setTotalMeritos(rs.getInt("COUNT_TOTAL"));
		return bolsaValidacion;
	}
	
}
