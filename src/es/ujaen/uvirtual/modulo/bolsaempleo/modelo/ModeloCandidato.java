package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoAcreditacionesTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoTitulacionTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para obtener la información de usuarios de UVIRTUAL. Modelo -
 * Operaciones con nombres: lista, actualiza, borra, inserta Controlador -
 * Opers. con nombres: obtener, cambiar, eliminar, agregar
 * 
 * @author ATISoluciones 2021
 */
public class ModeloCandidato {
	
	public static final int ORDER_COLUMN_INDEX_DOCUMENTO_CANDIDATO = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_CANDIDATO = 1;
	public static final int ORDER_COLUMN_INDEX_EMAIL_CANDIDATO = 2;
	
	protected static ModeloCandidato eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloCandidato();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloCandidato obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Listado de candidatos con titulaciones y con solicitudes.
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @return listado de candidatos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public BolsaEmpleoDataTable<CandidatoTitulacionTable> listaCandidatosDatatable(Map<String, String[]> params) throws SQLException, UVException {		
		return this.listaCandidatosTitulacionesDatatable(params, true);
	}
	
	/**
	 * Listado de candidatos sin titulacion (o titulación borrada) y con solicitudes .
	 * @param params .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<CandidatoTitulacionTable> listaCandidatosSinTitulacionDatatable(Map<String, String[]> params) throws SQLException, UVException {		
		return this.listaCandidatosTitulacionesDatatable(params, false);
	}
	
	/**
	 * Devuelve un array pereparado para exportar un csv de titulaciones.
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<String[]> listaCandidatosSinTitulacionCsv() throws SQLException, UVException {
		List<String[]> rows = new ArrayList<>();
				
		String consulta = getQueryFromCandidatosTitulacion(false);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia(); 
				
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = modeloUsuario.getUsuarioById(rs.getInt("CODNUM"));
					
					rows.add(new String[] {
						String.format("\"%s\"", usuario.getCodNum().toString()),
						String.format("\"%s\"", usuario.getPrsNif() != null ? usuario.getPrsNif() : ""),
						String.format("\"%s %s %s\"",
								usuario.getNombre() != null ? usuario.getNombre() : "",
								usuario.getPrimerApellido() != null ? usuario.getPrimerApellido() : "",
								usuario.getSegundoApellido() != null ? usuario.getSegundoApellido() : ""),
						String.format("\"%s\"", usuario.getEmail() != null ? usuario.getEmail() : ""),
					});
				}
			}
		}
		
		return rows;
	} 	
	
	/**
	 * Listado de candidatos con acreditaciones y con solicitud cerrada .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @return listado de candidatos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public BolsaEmpleoDataTable<CandidatoAcreditacionesTable> listaCandidatosAcreditacionesDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<CandidatoAcreditacionesTable> usuarios = new ArrayList<>();
		BolsaEmpleoDataTable<CandidatoAcreditacionesTable> dataTable = new BolsaEmpleoDataTable<>(params);

		String subquery = ""
				+ " SELECT COUNT(*) "
				+ " FROM TBEP_MER_PRE_USUARIO bepmpu "
				+ "	INNER JOIN TBEP_MERITOS_PREFERENTES bepmep ON bepmep.CODNUM = bepmpu.BEPMEP_CODNUM "
				+ "	WHERE bepmpu.BEPUSU_CODNUM = bepusu.CODNUM AND bepmpu.FLGBORRADO = 'N' AND bepmep.TIPO = ? ";
		
		String consulta = ""
				+ " SELECT DISTINCT bepusu.*, "
				+ "		(" + subquery + ") AS COUNT_ACREDITACIONES, "
				+ "	    (SELECT COUNT(*) FROM TBEP_MER_PRE_USUARIO bepmpu "
				+ "		 INNER JOIN TBEP_MERITOS_PREFERENTES bepmep ON bepmep.CODNUM = bepmpu.BEPMEP_CODNUM "
				+ "		 WHERE bepmpu.BEPUSU_CODNUM = bepusu.CODNUM AND bepmpu.FLGBORRADO = 'N' AND bepmpu.FLGVALIDADO = 'S' AND bepmep.TIPO = ? "
				+ "     ) AS COUNT_VALIDADAS "
				+ " FROM TBEP_USUARIOS bepusu "
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPUSU_CODNUM = bepusu.CODNUM "
				+ " WHERE 1=1 "
				+ " 	AND bepusu.FLGBORRADO = 'N' "
				+ "		AND bepusu.rol = " + ModeloRol.ID_ROL_CANDIDATO + " "
				+ " 	AND (" + subquery + ") > 0 "
				+ " 	AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'";
		
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");

		dataTable.setColumn(ORDER_COLUMN_INDEX_DOCUMENTO_CANDIDATO, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_CANDIDATO, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);

		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			
			int paramIndex = 1;
			stmt.setString(paramIndex, ModeloMeritosPreferentes.TIPO_POSESION);
			stmtCount.setString(paramIndex++, ModeloMeritosPreferentes.TIPO_POSESION);
			stmt.setString(paramIndex, ModeloMeritosPreferentes.TIPO_POSESION);
			stmtCount.setString(paramIndex++, ModeloMeritosPreferentes.TIPO_POSESION);
			stmt.setString(paramIndex, ModeloMeritosPreferentes.TIPO_POSESION);
			stmtCount.setString(paramIndex++, ModeloMeritosPreferentes.TIPO_POSESION);
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
				
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = modeloUsuario.getUsuarioFromResultSet(rs);					
					Integer acreditaciones = rs.getInt("COUNT_ACREDITACIONES");
					Integer validadas = rs.getInt("COUNT_VALIDADAS");

					usuarios.add(new CandidatoAcreditacionesTable(usuario, acreditaciones, validadas));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}

		return dataTable;
	}

	/**
	 * Listado de candidatos en función de un área .
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param conTitulaciones con solicitud 
	 * @return listado de candidatos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	private BolsaEmpleoDataTable<CandidatoTitulacionTable> listaCandidatosTitulacionesDatatable(Map<String, String[]> params, boolean conTitulaciones) 
			throws SQLException, UVException {
		
		List<CandidatoTitulacionTable> usuarios = new ArrayList<>();
		BolsaEmpleoDataTable<CandidatoTitulacionTable> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = getQueryFromCandidatosTitulacion(conTitulaciones);
								
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");

		dataTable.setColumn(ORDER_COLUMN_INDEX_DOCUMENTO_CANDIDATO, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_CANDIDATO, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_EMAIL_CANDIDATO, "bepusu.VUAJA_EMAIL_ALTA");

		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);

			try (ResultSet rs = stmt.executeQuery()) {
				ModeloUsuarioBolsaEmpleo modeloUsuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia(); 
				
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = modeloUsuario.getUsuarioById(rs.getInt("CODNUM"));					
					Integer titulaciones = rs.getInt("COUNT_TITULACIONES");
					Integer validadas = rs.getInt("COUNT_VALIDADAS");

					usuarios.add(new CandidatoTitulacionTable(usuario, titulaciones, validadas));					
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}

		return dataTable;
	}
	
	private String getQueryFromCandidatosTitulacion(boolean conTitulaciones) {
		String subquery = ""
				+ " SELECT COUNT(*) "
				+ " FROM TBEP_TITULACIONES_USUARIO beptus "
				+ "	WHERE beptus.BEPTUS_USU_CODNUM = bepusu.CODNUM AND beptus.FLGBORRADO = 'N'";

		String consulta = ""
				+ " SELECT DISTINCT bepusu.CODNUM, "
				+ "		(" + subquery + ") AS COUNT_TITULACIONES, "
				+ "		(SELECT COUNT(*) FROM TBEP_TITULACIONES_USUARIO beptus "
				+ "		 WHERE beptus.BEPTUS_USU_CODNUM = bepusu.CODNUM AND beptus.FLGBORRADO = 'N' AND beptus.FLGVALIDADA = 'S' "
				+ "		) AS COUNT_VALIDADAS " 
				+ "	FROM TBEP_USUARIOS bepusu "
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPUSU_CODNUM = bepusu.CODNUM "
				+ "	WHERE 1=1 "
				+ "		AND bepusu.FLGBORRADO = 'N' "
				+ "		AND bepusu.ROL = " + ModeloRol.ID_ROL_CANDIDATO + " "
				+ "		AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'";
						
		if (conTitulaciones) {
			consulta += " AND (" + subquery + ") > 0 "; 
		} else {
			consulta += " AND (" + subquery + ") = 0 ";
		}
		
		return consulta;
	}
	
}
