package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Evaluador;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable.DataTableColumn;

/**
 * Clase para obtener la información de usuarios de UVIRTUAL.
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * @author ATISoluciones
 */
public class ModeloEvaluador {
	
	public static final int ORDER_COLUMN_INDEX_NUMDOCUMENTO_EVALUADORES = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_EVALUADORES = 1;
	public static final int ORDER_COLUMN_INDEX_ACTIVO_EVALUADORES = 2;
	
	public static final String USUARIO_BORRADO = "S";
	public static final String USUARIO_NO_BORRADO = "N";
	public static final String USUARIO_EXCLUIDO = "S";
	public static final String USUARIO_NO_EXCLUIDO = "N";
	
	public static final Integer PARAM_ROL_ID = 1052;
	
	
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
				+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
				+ "INNER JOIN TBEP_EVALUADORES bepeva ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM "
				+ "WHERE FLGBORRADO!='S' AND FLGEXCLUIDO!='S' AND bepeva.BEPARE_CODNUM = ? "
				+ "AND bepusu.ROL = " + PARAM_ROL_ID;
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO_EVALUADORES, "uvpersona.IDNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_EVALUADORES, "uvpersona.STRAPELLIDO1");
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
					UsuarioBolsaEmpleo usuario =  new UsuarioBolsaEmpleo();
					usuario.setCodNum(rs.getInt("CODNUM"));
					usuario.setTipoDocumento(rs.getString("STRTIPODOCUMENTO"));
					usuario.setNumDocumento(rs.getString("IDNIF") + rs.getString("LETRANIF"));
					usuario.setNombre(rs.getString("STRNOMBRE"));
					usuario.setPrimerApellido(rs.getString("STRAPELLIDO1"));
					usuario.setSegundoApellido(rs.getString("STRAPELLIDO2"));
					
					Integer codNumArea = rs.getInt("BEPARE_CODNUM");
					Boolean activo = rs.getString("FLGACTIVO").equals("S");
					Evaluador evaluador = new Evaluador(usuario, codNumArea, activo);
					usuarios.add(evaluador);
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}
		
		return dataTable;
	}
	
	/** Listado de usuarios con rol 1052 excluyendo la lista de evaluadores de un área . 
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param area .
	 * @return listado de usuarios .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<Evaluador> listaUsuariosRestantesDatatable(Map<String, String[]> params, Integer area) throws SQLException, UVException {
		if (area == null) {
			throw new UVException("No se pueden listar evaluadores sin area");
		}
		
		List<Evaluador> usuarios = new ArrayList<>();
		BolsaEmpleoDataTable<Evaluador> dataTable = new BolsaEmpleoDataTable<Evaluador>(params);
		
		String consulta2 = "SELECT bepeva.BEPUSU_CODNUM FROM TBEP_EVALUADORES bepeva "
				+ "INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepeva.BEPUSU_CODNUM "
				+ "WHERE bepusu.FLGBORRADO!='S' "
				+ "AND bepusu.FLGEXCLUIDO!='S' "
				+ "AND bepeva.BEPARE_CODNUM = ? ";
		
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu "
				+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
				+ "WHERE FLGBORRADO!='S' "
				+ "AND FLGEXCLUIDO!='S' "
				+ "AND bepusu.CODNUM NOT IN (" + consulta2 + ") "
				+ "AND bepusu.ROL = " + PARAM_ROL_ID;
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO_EVALUADORES + 1, "uvpersona.IDNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_EVALUADORES + 1, "uvpersona.STRAPELLIDO1");
		
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
					UsuarioBolsaEmpleo usuario =  new UsuarioBolsaEmpleo();
					usuario.setCodNum(rs.getInt("CODNUM"));
					usuario.setTipoDocumento(rs.getString("STRTIPODOCUMENTO"));
					usuario.setNumDocumento(rs.getString("IDNIF") + rs.getString("LETRANIF"));
					usuario.setNombre(rs.getString("STRNOMBRE"));
					usuario.setPrimerApellido(rs.getString("STRAPELLIDO1"));
					usuario.setSegundoApellido(rs.getString("STRAPELLIDO2"));
					
					usuarios.add(new Evaluador(usuario));
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}
		
		return dataTable;
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
