package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionArcos;
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
	
	protected static ModeloEvaluador eInstancia = null;
	public static final String MENSAJE_ERROR_NO_EXISTE_EVALUADOR = "No existe el evaluador sin ID";
	
	public static final Integer PARAM_ROL_ID = 1051;
	
	public static final String CODNUM = "CODNUM";
	
	
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
    
    /** Consulta evaluadores en BBDD y los devuelve.
	 * @return todos los evaluadores .
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Evaluador> listaEvaluadores() throws SQLException {
		List<Evaluador> evaluadores = new ArrayList<>();
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu "
				+ "INNER JOIN TBEP_EVALUADORES bepeva ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM "
				+ "WHERE FLGBORRADO!='S' AND FLGEXCLUIDO!='S' "
				+ "AND bepusu.ROL = " + PARAM_ROL_ID;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";
					
				    try (Connection conexionArcos = ConexionArcos.obtenerInstancia();
				    	PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos);) {
				    	stmtArcos.setString(1, rs.getString("PRSNIF"));
				    	try (ResultSet rsArcos = stmtArcos.executeQuery();) {
					    	while (rsArcos.next()) {
								UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
								usuario.setCodNum(rs.getInt("CODNUM"));
								usuario.setTipoDocumento(rsArcos.getString("STRTIPODOCUMENTO"));
								usuario.setNumDocumento(rsArcos.getString("PRSNIF"));
								usuario.setNombre(rsArcos.getString("STRNOMBRE"));
								usuario.setPrimerApellido(rsArcos.getString("STRAPELLIDO1"));
								usuario.setSegundoApellido(rsArcos.getString("STRAPELLIDO2"));
								
								Integer codNumArea = rs.getInt("BEPARE_CODNUM");
								Boolean activo = rs.getString("FLGACTIVO").equals("S");
								Evaluador evaluador = new Evaluador(usuario, codNumArea, activo);
								evaluadores.add(evaluador);
					    	}
				    	}
				    }
				}
			}
		}

		return evaluadores;
	}
	
	/**
	 * Devuelve un evaluador por su id.
	 * @param codNum .
	 * @return Evaluador o null si no existe
	 * @throws SQLException .
	 */
	public Evaluador getEvaluadorById(Integer codNum) throws SQLException, UVException {

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu "
				+ "INNER JOIN TBEP_EVALUADORES bepeva ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM "
				+ "WHERE FLGBORRADO!='S' AND FLGEXCLUIDO!='S' "
				+ "AND bepusu.ROL = " + PARAM_ROL_ID + " AND bepusu.CODNUM = ?";
		
		Evaluador evaluador = null;
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";
					
				    try (Connection conexionArcos = ConexionArcos.obtenerInstancia();
				    	PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos);) {
				    	stmtArcos.setString(1, rs.getString("PRSNIF"));
				    	try (ResultSet rsArcos = stmtArcos.executeQuery();) {
					    	while (rsArcos.next()) {
								UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
								usuario.setCodNum(rs.getInt("CODNUM"));
								usuario.setTipoDocumento(rsArcos.getString("STRTIPODOCUMENTO"));
								usuario.setNumDocumento(rsArcos.getString("PRSNIF"));
								usuario.setNombre(rsArcos.getString("STRNOMBRE"));
								usuario.setPrimerApellido(rsArcos.getString("STRAPELLIDO1"));
								usuario.setSegundoApellido(rsArcos.getString("STRAPELLIDO2"));
								usuario.setListaDist(rs.getString("FLGLISTADISTRIBUCION").equals("S"));
								usuario.setExcluido(rs.getString("FLGEXCLUIDO").equals("S"));
								usuario.setExcluidoTipo(rs.getString("FLGEXCLUIDOTIPO"));
								usuario.setFechaExclusionInicio(rs.getTimestamp("FECHA_EXCLUSION_INICIO"));
								usuario.setFechaExclusionFin(rs.getTimestamp("FECHA_EXCLUSION_FIN"));
								usuario.setRazonExcluido(rs.getString("RAZON_EXCLUSION"));
								usuario.setFechaExclusion(rs.getTimestamp("FECHA_EXCLUSION"));
								usuario.setBorrado(rs.getString("FLGBORRADO").equals("S"));
								usuario.setFechaBorrado(rs.getTimestamp("FECHA_BORRADO"));
								
								Integer codNumArea = rs.getInt("BEPARE_CODNUM");
								Boolean activo = rs.getString("FLGACTIVO").equals("S");
								evaluador = new Evaluador(usuario, codNumArea, activo);
								
								return evaluador;
					    	}
				    	}
				    }
				}
			}
		}
		
		return evaluador;
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
				+ " (SELECT COUNT(*) FROM UVIRTUAL.TBEP_USUARIOS bepusu"
				+ "		INNER JOIN UVIRTUAL.TBEP_EVALUADORES bepeva ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM"
				+ "		WHERE FLGBORRADO!='S' AND FLGEXCLUIDO!='S' AND bepeva.BEPARE_CODNUM = bepare.CODNUM"
				+ "		AND bepusu.ROL = 1051) COUNT_EVALUADORES"
				+ " FROM UVIRTUAL.TBEP_AREAS bepare"
				+ "	INNER JOIN UVIRTUAL.TBEP_AREAS_DEPARTAMENTOS bepade ON bepade.BEPARE_CODNUM = bepare.CODNUM"
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
		BolsaEmpleoDataTable<Evaluador> dataTable = new BolsaEmpleoDataTable<Evaluador>(params);
		
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu "
				+ "INNER JOIN TBEP_EVALUADORES bepeva ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM "
				+ "WHERE FLGBORRADO!='S' AND FLGEXCLUIDO!='S' AND bepeva.BEPARE_CODNUM = ? "
				+ "AND bepusu.ROL = " + PARAM_ROL_ID;
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO_EVALUADORES, "bepusu.PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_EVALUADORES, "bepusu.CODCUENTA");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTIVO_EVALUADORES, "bepeva.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, area);
			stmtCount.setInt(indexParam++, area);
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";
					
				    try (Connection conexionArcos = ConexionArcos.obtenerInstancia();
				    	PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos);) {
				    	stmtArcos.setString(1, rs.getString("PRSNIF"));
				    	try (ResultSet rsArcos = stmtArcos.executeQuery();) {
					    	while (rsArcos.next()) {
								UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
								usuario.setCodNum(rs.getInt("CODNUM"));
								usuario.setTipoDocumento(rsArcos.getString("STRTIPODOCUMENTO"));
								usuario.setNumDocumento(rsArcos.getString("PRSNIF"));
								usuario.setNombre(rsArcos.getString("STRNOMBRE"));
								usuario.setPrimerApellido(rsArcos.getString("STRAPELLIDO1"));
								usuario.setSegundoApellido(rsArcos.getString("STRAPELLIDO2"));
								
								Integer codNumArea = rs.getInt("BEPARE_CODNUM");
								Boolean activo = rs.getString("FLGACTIVO").equals("S");
								Evaluador evaluador = new Evaluador(usuario, codNumArea, activo);
								usuarios.add(evaluador);
					    	}
				    	}
				    }
				}				
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}
		
		return dataTable;
	}
	
	/** Consulta titulaciones en BBDD y las devuelve.
	 * @param area .
	 * @param usu .
	 * @return boolean si el evaluador ya se encuentra asignado a ese area
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException .
	 */
	public Boolean checkEvaluadorArea(Area area, UsuarioBolsaEmpleo usu) throws SQLException, UVException {
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu "
				+ "INNER JOIN TBEP_EVALUADORES bepeva ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM "
				+ "WHERE bepeva.BEPARE_CODNUM = ? AND BEPUSU_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
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
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parámetros .
	 */
	public void insertaEvaluadores(List<String> usuarios, Integer area) throws SQLException, UVException {
		
		if (area == null) {
			throw new UVException("No se puede agregar un evaluador sin el id del área");
		}
		
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(usuarios.size());
		String consulta = "INSERT INTO TBEP_EVALUADORES (BEPARE_CODNUM, BEPUSU_CODNUM)"
				+ " SELECT bepare.CODNUM AS BEPARE_CODNUM, bepusu.CODNUM AS BEPUSU_CODNUM"
				+ " FROM TBEP_USUARIOS bepusu, TBEP_AREAS bepare WHERE bepare.CODNUM = ? AND "
				+ " bepusu.CODNUM IN (" + params + ")";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
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
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parámetros .
	 */
	public void insertaEvaluador(UsuarioBolsaEmpleo usuario, Integer area) throws SQLException, UVException {
		
		if (area == null) {
			throw new UVException("No se puede agregar un evaluador sin el id del área");
		}
		
		if (usuario == null) {
			throw new UVException("No se puede agregar un evaluador si el usuario esta vacio");
		}

		String consulta = "INSERT INTO TBEP_EVALUADORES (BEPARE_CODNUM, BEPUSU_CODNUM)"
				+ " SELECT bepare.CODNUM AS BEPARE_CODNUM, bepusu.CODNUM AS BEPUSU_CODNUM"
				+ " FROM TBEP_USUARIOS bepusu, TBEP_AREAS bepare WHERE bepare.CODNUM = ? AND"
				+ " bepusu.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, area);
			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Borra o restaura un evaluador .
	 * @param evaluador a borrar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException si noticia no es valida .
	 */
	public void borraRestauraEvaluador(Evaluador evaluador) throws SQLException, UVException {
		if (evaluador == null) {
			throw new UVException("No se puede eliminar un evaluador vacío");
		}
		if (evaluador.getCodNum() == null) {
			throw new UVException("No se puede eliminar un evaluador con id de usuario vacío");
		}
		if (evaluador.getCodNumArea() == null) {
			throw new UVException("No se puede eliminar un evaluador con id de area vacío");
		}
		String consulta = "UPDATE TBEP_EVALUADORES SET flgactivo=? WHERE bepusu_codnum=? AND bepare_codnum=? ";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, evaluador.isActivo() ? "S" : "N");
			stmt.setInt(parameterIndex++, evaluador.getCodNum());
			stmt.setInt(parameterIndex++, evaluador.getCodNumArea());
			stmt.executeUpdate();
		}
	}
	
}
