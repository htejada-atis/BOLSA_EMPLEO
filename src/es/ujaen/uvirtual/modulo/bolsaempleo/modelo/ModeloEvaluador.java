package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.AreaEvaluadoresTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para obtener la información de usuarios de UVIRTUAL.
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * @author ATISoluciones 2021
 */
public class ModeloEvaluador {
	
	public static final int ORDER_COLUMN_INDEX_NUMDOCUMENTO_EVALUADORES = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_EVALUADORES = 1;
	public static final int ORDER_COLUMN_INDEX_ACTIVO_EVALUADORES = 2;
	
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_CODIGO = 2;
	public static final int ORDER_COLUMN_INDEX_AREA = 3;
	
	protected static ModeloEvaluador eInstancia;
	public static final String MENSAJE_ERROR_NO_EXISTE_EVALUADOR = "No existe el evaluador sin ID";
	
	public static final Integer PARAM_ROL_ID = 1051;
	
	public static final String CODNUM = "CODNUM";
	public static final String ACTIVO = "S";
	public static final String INACTIVO = "N";
	
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloEvaluador();
		}
	}
	
	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloEvaluador obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
    
	/**
	 * Devuelve un evaluador por su id y por su area.
	 * @param codNum .
	 * @param idArea .
	 * @return Evaluador o null si no existe
	 * @throws SQLException .
	 */
	public Evaluador getEvaluadorById(Integer codNum, Integer idArea) throws SQLException, UVException {
		String consulta = "SELECT * FROM TBEP_EVALUADORES bepeva WHERE bepeva.BEPUSU_CODNUM = ? and bepeva.BEPARE_CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, codNum);
			stmt.setInt(paramIndex++, idArea);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM"));
					
					Integer codNumArea = rs.getInt("BEPARE_CODNUM");
					Boolean activo = rs.getString("FLGACTIVO").equals(ACTIVO);
					return new Evaluador(usuario, codNumArea, activo);
				}
			}
		}

		return null;
	}
	
	/**
	 * Listado de áreas de un departamento . 
	 * @param params .
	 * @param idDepartamento .
	 * @return listado de áreas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<AreaEvaluadoresTable> listaAreaDepartamentoDatatable(Map<String, String[]> params, Integer idDepartamento) throws SQLException, UVException {
		List<AreaEvaluadoresTable> bolsas = new ArrayList<>();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		BolsaEmpleoDataTable<AreaEvaluadoresTable> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepare.*,"
				+ " (SELECT COUNT(*) FROM TBEP_USUARIOS bepusu"
				+ "		INNER JOIN TBEP_EVALUADORES bepeva ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM"
				+ "		WHERE FLGBORRADO!='S' AND FLGEXCLUIDO!='S' AND bepeva.BEPARE_CODNUM = bepare.CODNUM"
				+ "		AND bepusu.ROL = 1051 AND bepeva.FLGACTIVO = 'S') COUNT_EVALUADORES"
				+ " FROM TBEP_AREAS bepare"
				+ "	INNER JOIN TBEP_AREAS_DEPARTAMENTOS bepade ON bepade.BEPARE_CODNUM = bepare.CODNUM"
				+ "	WHERE bepade.BEPDEP_CODNUM = ? ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepbol.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, idDepartamento);
			stmtCount.setInt(indexParam++, idDepartamento);
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Area area = modeloArea.getAreaById(rs.getInt(CODNUM));
					bolsas.add(new AreaEvaluadoresTable(area, rs.getInt("COUNT_EVALUADORES")));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bolsas);
		}
		
		return dataTable;
	}
	
	/** Listado de usuarios en función de un área . 
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param area .
	 * @return listado de usuarios .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<Evaluador> listaEvaluadoresDatatable(Map<String, String[]> params, Integer area) throws SQLException, UVException {
		
		if (area == null) {
			throw new UVException("No se pueden listar evaluadores sin area");
		}
		
		List<Evaluador> usuarios = new ArrayList<>();
		BolsaEmpleoDataTable<Evaluador> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu "
				+ "INNER JOIN TBEP_EVALUADORES bepeva ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM "
				+ "WHERE FLGBORRADO!='S' AND FLGEXCLUIDO!='S' AND bepeva.BEPARE_CODNUM = ? "
				+ "AND bepusu.ROL = " + PARAM_ROL_ID;
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO_EVALUADORES, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_EVALUADORES, "bepusu.CODCUENTA");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTIVO_EVALUADORES, "bepeva.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, area);
			stmtCount.setInt(indexParam++, area);
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("CODNUM"));
					Integer codNumArea = rs.getInt("BEPARE_CODNUM");
					Boolean activo = rs.getString("FLGACTIVO").equals(ACTIVO);
					Evaluador evaluador = new Evaluador(usuario, codNumArea, activo);
					usuarios.add(evaluador);
				}				
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}
		
		return dataTable;
	}
	
	/** Comprueba si el usuario está como evaluador del area.
	 * @param area .
	 * @param usu .
	 * @return boolean si el evaluador ya se encuentra asignado a ese area
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException .
	 */
	public boolean checkEvaluadorArea(Area area, UsuarioBolsaEmpleo usu) throws SQLException {
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu "
				+ "INNER JOIN TBEP_EVALUADORES bepeva ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM "
				+ "WHERE bepeva.BEPARE_CODNUM = ? AND BEPUSU_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, area.getCodNum());
			stmt.setInt(indexParam, usu.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**	Función que agrega evaluadores a un área .
	 * @param usuarios .
	 * @param area id del area por el que se va a filtrar .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parámetros .
	 */
	public void insertaEvaluadores(List<String> usuarios, Integer area, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		
		if (area == null) {
			throw new UVException("No se puede agregar un evaluador sin el id del área");
		}
		
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(usuarios.size());
		String consulta = "INSERT INTO TBEP_EVALUADORES (BEPARE_CODNUM, BEPUSU_CODNUM, UID_USUARIO)"
				+ " SELECT bepare.CODNUM AS BEPARE_CODNUM, bepusu.CODNUM AS BEPUSU_CODNUM, ? AS UID_USUARIO"
				+ " FROM TBEP_USUARIOS bepusu, TBEP_AREAS bepare WHERE bepare.CODNUM = ? AND "
				+ " bepusu.CODNUM IN (" + params + ")";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, area);
			for (String usuario: usuarios) {
				stmt.setString(indexParam++, usuario);
			}
			stmt.executeUpdate();
		}
	}
	
	/**	Función que agrega un evaluador a un área .
	 * @param usuario .
	 * @param area id del area por el que se va a filtrar .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parámetros .
	 */
	public void insertaEvaluador(UsuarioBolsaEmpleo usuario, Integer area, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		
		if (area == null) {
			throw new UVException("No se puede agregar un evaluador sin el id del área");
		}
		
		if (usuario == null) {
			throw new UVException("No se puede agregar un evaluador si el usuario esta vacio");
		}

		String consulta = "INSERT INTO TBEP_EVALUADORES (BEPARE_CODNUM, BEPUSU_CODNUM, UID_USUARIO) VALUES (?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;			
			stmt.setInt(indexParam++, area);
			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();
		}
	}
	
	/** Borra o restaura un evaluador .
	 * @param evaluador a borrar .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException si noticia no es valida .
	 */
	public void borraRestauraEvaluador(Evaluador evaluador, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (evaluador == null) {
			throw new UVException("No se puede eliminar un evaluador vacío");
		}
		if (evaluador.getCodNum() == null) {
			throw new UVException("No se puede eliminar un evaluador con id vacío");
		}
		if (evaluador.getCodNumArea() == null) {
			throw new UVException("No se puede eliminar un evaluador con id de area vacío");
		}
		String consulta = "UPDATE TBEP_EVALUADORES SET flgactivo=?, UID_USUARIO=? WHERE bepusu_codnum=? AND bepare_codnum=? ";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, evaluador.isActivo() ? ACTIVO : INACTIVO);
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, evaluador.getCodNum());
			stmt.setInt(parameterIndex++, evaluador.getCodNumArea());
			stmt.executeUpdate();
		}
	}
	
}
