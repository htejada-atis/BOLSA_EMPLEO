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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoResultadoTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para los resultados de las solicitudes del candidato .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloMisResultados {

protected static ModeloMisResultados eInstancia;

	public static final int ORDER_COLUMN_INDEX_COD_AREA_BOLSAS = 0;
	public static final int ORDER_COLUMN_INDEX_DESC_AREA_BOLSAS = 1;
	public static final int ORDER_COLUMN_INDEX_PUNTUACION_BOLSAS = 2;
	
	public static final int ORDER_COLUMN_INDEX_PUNTUACION_CANDIDATOS = 2;
	
	public static final String CODNUM = "CODNUM";
	public static final String FECHABAREMACION = "FECHABAREMACION";
	public static final String TOTAL = "TOTAL";
	public static final String VUAJA_PRSNIF = "VUAJA_PRSNIF";
	public static final String VUAJA_STRAPELLIDO1 = "VUAJA_STRAPELLIDO1";
	public static final String VUAJA_STRAPELLIDO2 = "VUAJA_STRAPELLIDO2";
	public static final String VUAJA_STRNOMBRE = "VUAJA_STRNOMBRE";
	
	private static final int TOTAL_COUNT_USER = 4;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMisResultados();
		}
	}
	
    /**
     * Obtiene una instancia de la conexión .
     * @return instancia .
     */
	public static ModeloMisResultados obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
        }
		return eInstancia;
    }
	
	/** Listado de bolsas para resultados del candidato .
	 * @param convocatoria .
	 * @param candidato .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @return listado de bolsas de empleo .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public BolsaEmpleoDataTable<BolsaResultado> listaBolsasResultadosCandidatoDatatable(Convocatoria convocatoria, UsuarioBolsaEmpleo candidato, Map<String, String[]> params)
			throws SQLException, UVException {
		List<BolsaResultado> bolsas = new ArrayList<>();
		BolsaEmpleoDataTable<BolsaResultado> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT bepbol.CODNUM, bepsob.TOTAL, bepsob.FECHABAREMACION,"
				+ "		CASE"
				+ "			WHEN bepcon.ESTADO = 'CERRADA' THEN 1"
				+ "			ELSE 0"
				+ "		END AS RESULTADOS_ACTUALES"
				+ "	FROM TBEP_BOLSAS bepbol"
				+ "	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPBOL_CODNUM = bepbol.CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ "	INNER JOIN TBEP_CONVOCATORIAS bepcon ON bepcon.CODNUM = bepsol.BEPCON_CODNUM"
				+ "	WHERE bepsol.BEPUSU_CODNUM = ? AND bepcon.CODNUM = ?";

		dataTable.setColumn(ORDER_COLUMN_INDEX_COD_AREA_BOLSAS, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESC_AREA_BOLSAS, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_PUNTUACION_BOLSAS, "bepsob.TOTAL", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmtCount.setInt(paramIndex, candidato.getCodNum());
			stmt.setInt(paramIndex++, candidato.getCodNum());
			stmtCount.setInt(paramIndex, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(this.createBolsaResultadoFromResultset(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bolsas);
		}

		return dataTable;
	}
	
	/** Lista de resultados de un candidato en un área .
	 * @param bolsa .
	 * @param convocatoria .
	 * @param candidato .
	 * @param params .
	 * @return datatable de resultados .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<CandidatoResultadoTable> listadoResultadosCandidatosArea(Bolsa bolsa, Convocatoria convocatoria, UsuarioBolsaEmpleo candidato,
			Map<String, String[]> params) throws SQLException, UVException {
		List<CandidatoResultadoTable> rows = new ArrayList<>();
		BolsaEmpleoDataTable<CandidatoResultadoTable> dataTable = new BolsaEmpleoDataTable<>(params);
		
		if (convocatoria == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = "SELECT bepusu.CODNUM, bepsob.TOTAL,"
				+ "		CASE"
				+ "			WHEN bepusu.CODNUM = ? THEN bepusu.VUAJA_PRSNIF"
				+ "			ELSE substr(bepusu.VUAJA_PRSNIF,1,3) || lpad('*',length(bepusu.VUAJA_PRSNIF)-3,'*')"
				+ "		END AS VUAJA_PRSNIF,"
				+ "		CASE"
				+ "			WHEN bepusu.CODNUM = ? THEN bepusu.VUAJA_STRNOMBRE"
				+ "			ELSE lpad('*',length(bepusu.VUAJA_STRNOMBRE),'*')"
				+ "		END AS VUAJA_STRNOMBRE,"
				+ "		CASE"
				+ "			WHEN bepusu.CODNUM = ? THEN bepusu.VUAJA_STRAPELLIDO1"
				+ "			ELSE lpad('*',length(bepusu.VUAJA_STRAPELLIDO1),'*')"
				+ "		END AS VUAJA_STRAPELLIDO1,"
				+ "		CASE"
				+ "			WHEN bepusu.CODNUM = ? THEN bepusu.VUAJA_STRAPELLIDO2"
				+ "			ELSE lpad('*',length(bepusu.VUAJA_STRAPELLIDO2),'*')"
				+ "		END AS VUAJA_STRAPELLIDO2"
				+ "	FROM TBEP_SOLICITUD_BOLSAS bepsob"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ "	INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ "	WHERE bepsol.BEPCON_CODNUM = ? AND bepsob.BEPBOL_CODNUM = ? AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_PUNTUACION_CANDIDATOS, "bepsob.TOTAL", DataTableColumn.COLUMN_TYPE_DOUBLE);
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			while (paramIndex <= TOTAL_COUNT_USER) {
				stmtCount.setInt(paramIndex, candidato.getCodNum());
				stmt.setInt(paramIndex++, candidato.getCodNum());
			}
			stmtCount.setInt(paramIndex, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex, bolsa.getCodNum());
			stmt.setInt(paramIndex++, bolsa.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					CandidatoResultadoTable usu = new CandidatoResultadoTable();
					usu.setTotal(rs.getDouble(TOTAL));
					usu.setCodNum(rs.getInt(CODNUM));
					usu.setNombre(rs.getString(VUAJA_STRNOMBRE));
					usu.setPrimerApellido(rs.getString(VUAJA_STRAPELLIDO1));
					usu.setSegundoApellido(rs.getString(VUAJA_STRAPELLIDO2));
					usu.setPrsNif(rs.getString(VUAJA_PRSNIF));
					rows.add(usu);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/** Crea una bolsa resultado a partir de un resultset .
	 * @param rs .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaResultado createBolsaResultadoFromResultset(ResultSet rs) throws SQLException, UVException {
		Bolsa bol = ModeloBolsa.obtenerInstancia().getBolsaById(rs.getInt(CODNUM));
		Double total = rs.getDouble(TOTAL);
		Date fechaBaremacion = rs.getDate(FECHABAREMACION);
		Boolean resultadoActual = rs.getBoolean("RESULTADOS_ACTUALES");
		
		BolsaResultado bolsa = new BolsaResultado(bol, total, resultadoActual);
		bolsa.setFechaBaremacion(fechaBaremacion);
		
		return bolsa;
	}
}
