package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de bloques de baremación (APARTADOS). 
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloBaremacionBloques {
	// ordenación bloques de baremación
	public static final int ORDER_COLUMN_INDEX_BLOQUES_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_BLOQUES_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_BLOQUES_NUMERO_MAXIMO_MERITOS = 2;
	public static final int ORDER_COLUMN_INDEX_BLOQUES_ACTIVO = 3;
		
	// errores
	public static final String ERROR_BLOQUE_NOEXITE = "Bloque no encontrado";	

	public static final Integer COLUMN_CODIGO_MAXLENGTH = 3; 
	public static final Integer COLUMN_NOMBRE_MAXLENGTH = 100;
	public static final Integer COLUMN_DESCRIPCION_MAXLENGTH = 250;

	public static final String CODIGO = "CODIGO";
	public static final String CODNUM = "CODNUM";

	protected static ModeloBaremacionBloques eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloBaremacionBloques();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
	public static ModeloBaremacionBloques obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
		
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS BLOQUEBAREMACION  ********************************************/
	
	/**
	 * Devuelve un bloque de baremación por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si bloque no es existe
	 */
	public BloqueBaremacion getBloqueBaremacionById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("El bloque de baremación es requerido");
		}
		
		String sql = "SELECT bepblo.* FROM TBEP_BLOQUESBAREMACION bepblo WHERE bepblo.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createBloqueFromResultSet(rs);
				}
			}
		}
		
		throw new UVException(ERROR_BLOQUE_NOEXITE);	
	}
	
	/**
	 * Listado de bloques de baremación . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param apartado id del apartado .
	 * @return listado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<BloqueBaremacion> listadoBloquesBaremacionDatatable(Map<String, String[]> params, ApartadoBaremacion apartado) 
			throws SQLException, UVException {
		
		List<BloqueBaremacion> bloques = new ArrayList<>();
		BolsaEmpleoDataTable<BloqueBaremacion> dataTable = new BolsaEmpleoDataTable<BloqueBaremacion>(params);
		
		String consulta = "SELECT bepblo.* "
				+ "FROM TBEP_BLOQUESBAREMACION bepblo "
				+ "WHERE bepblo.BEPAPA_CODNUM = ? ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_CODIGO, "bepblo.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_NOMBRE, "bepblo.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_NUMERO_MAXIMO_MERITOS, "bepblo.NUMERO_MAXIMO_MERITOS");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_ACTIVO, "bepblo.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, apartado.getCodNum());
			stmtCount.setInt(indexParam++, apartado.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bloques.add(this.createBloqueFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bloques);
		}
		
		return dataTable;
	}

	/** 
	 * Desactiva un bloque de baremación.
	 * @param bloque .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desactivarBloque(BloqueBaremacion bloque) throws SQLException, UVException {
		this.activaDesactivaBloque(bloque, false);
	}
	
	/** 
	 * Actia un apartado de baremación.
	 * @param bloque .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void activarBloque(BloqueBaremacion bloque) throws SQLException, UVException {
		this.activaDesactivaBloque(bloque, true);
	}
	
	/** Actualiza un bloque .
	 * @param bloque con los datos nuevos a actualizar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaBloque(BloqueBaremacion bloque) throws SQLException, UVException {
		this.chequearBloqueParaInsertarOActualizar(bloque);
		
		String consulta = "UPDATE TBEP_BLOQUESBAREMACION SET "
				+ "CODIGO = ?, "
				+ "NOMBRE = ?, "
				+ "FLGACTIVO = ?, "
				+ "NUMERO_MAXIMO_MERITOS = ? "
				+ "WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, bloque.getCodigo());
			stmt.setString(parameterIndex++, bloque.getNombre());
			stmt.setString(parameterIndex++, bloque.isActivo() ? "S" : "N");
			
			if (bloque.getNumeroMaximoMeritos() != null) {
				stmt.setInt(parameterIndex++, bloque.getNumeroMaximoMeritos());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			
			stmt.setInt(parameterIndex++, bloque.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Función que inserta un bloque en la BD.
	 * @param bloque .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void insertaBloque(BloqueBaremacion bloque) throws SQLException, UVException {
		if (bloque == null) {
			throw new UVException("No se puede insertar un bloque vacio");
		}
		
		this.chequearBloqueParaInsertarOActualizar(bloque);
		
		String consulta = "INSERT INTO TBEP_BLOQUESBAREMACION " 
				+ " (CODIGO,NOMBRE,BEPAPA_CODNUM,NUMERO_MAXIMO_MERITOS)"
				+ " VALUES (?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, bloque.getCodigo());
			stmt.setString(parameterIndex++, bloque.getNombre());
			stmt.setInt(parameterIndex++, bloque.getApartadoBaremacion().getCodNum());
			if (bloque.getNumeroMaximoMeritos() != null) {
				stmt.setInt(parameterIndex++, bloque.getNumeroMaximoMeritos());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			stmt.executeUpdate();
		}
	}
		
	/** Consulta para obtener el último código de los bloques de un apartado .
	 * @param apartado .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public String getUltimoCodigoBloque(ApartadoBaremacion apartado) throws SQLException, UVException {
		String consulta = "SELECT bepblo.CODIGO, bepblo.BEPAPA_CODNUM FROM TBEP_BLOQUESBAREMACION bepblo "
				+ " WHERE bepblo.BEPAPA_CODNUM = ? ORDER BY bepblo.CODIGO DESC"
				+ " FETCH FIRST 1 ROW ONLY";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, apartado.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return "0";
				} else {
					return rs.getString(CODIGO);
				}
			}
		}
	}
	
	/**
	 * Devuelve los bloques de bareamación activos.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<BloqueBaremacion> listaBloqueBaremacion() throws SQLException, UVException {
		return listaBloqueBaremacion(" WHERE FLGACTIVO = 'S' ORDER BY CODIGO, NOMBRE ");
	} 
	
	private List<BloqueBaremacion> listaBloqueBaremacion(String clausula) throws SQLException, UVException {
		List<BloqueBaremacion> items = new ArrayList<>();
		String consulta = "SELECT bepblo.* FROM TBEP_BLOQUESBAREMACION bepblo " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					items.add(this.createBloqueFromResultSet(rs));
				}
			}
		}
		return items;
	}
	
	private BloqueBaremacion createBloqueFromResultSet(ResultSet rs) throws SQLException, UVException {
		BloqueBaremacion obj = new BloqueBaremacion();
		obj.setCodNum(rs.getInt(CODNUM));
		obj.setCodigo(rs.getString(CODIGO));
		obj.setNombre(rs.getString("NOMBRE"));
		obj.setActivo(rs.getString("FLGACTIVO").equals("S"));
		obj.setNumeroMaximoMeritos(rs.getInt("NUMERO_MAXIMO_MERITOS") == 0 ? null : rs.getInt("NUMERO_MAXIMO_MERITOS"));
		obj.setApartadoBaremacion(ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(rs.getInt("BEPAPA_CODNUM")));
		return obj;
	}
	
	private void chequearBloqueParaInsertarOActualizar(BloqueBaremacion bloque) throws SQLException, UVException {
		if (this.existeOtroBloqueActivoPorCodigo(bloque)) {
			throw new UVException("Ya existe un bloque con el código introducido");
		}		
	}

	private void activaDesactivaBloque(BloqueBaremacion bloque, Boolean activo) throws SQLException, UVException {
		String consulta = "UPDATE TBEP_BLOQUESBAREMACION SET FLGACTIVO = ? WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			
			stmt.setString(parameterIndex++, activo ? "S" : "N");
			stmt.setInt(parameterIndex++, bloque.getCodNum());
			stmt.executeUpdate();
		}	
	}
	
	private boolean existeOtroBloqueActivoPorCodigo(BloqueBaremacion bloque) throws SQLException {		
		String sql = "SELECT bepblo.* "
				+ "FROM TBEP_BLOQUESBAREMACION bepblo "
				+ "WHERE 1=1 "
				+ "AND bepblo.BEPAPA_CODNUM = ? "
				+ "AND bepblo.CODIGO = ? "
				+ "AND bepblo.FLGACTIVO = 'S' ";
		
		if (bloque.getCodNum() != null) {
			sql += " AND bepblo.CODNUM <> ? ";
		}
		
		sql += " FETCH FIRST 1 ROW ONLY";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, bloque.getApartadoBaremacion().getCodNum());
			stmt.setString(parameterIndex++, bloque.getCodigo());
			
			if (bloque.getCodNum() != null) {
				stmt.setInt(parameterIndex++, bloque.getCodNum());
			}
						
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return true;									
				}
			}
		}
		
		return false;
	} 
}
