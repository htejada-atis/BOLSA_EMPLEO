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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de afinidades .
 * 
 * @author fcampos
 */
public class ModeloAfinidad {
	public static final int ORDER_COLUMN_INDEX_CODIGO = 1;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 2;
	public static final int ORDER_COLUMN_INDEX_MODULACION = 3;
	
	public static final int COLUMN_DESCRIPCION_MAXLENGTH = 250;
	public static final int COLUMN_CODIGO_MAXLENGTH = 4;
	
	public static final String MENSAJE_ERROR_NO_EXISTE_AFINIDAD = "No existe la afinidad";
	public static final String MENSAJE_ERROR_AFINIDAD_NULL = "No se puede insertar una afinidad vacio";
	public static final String MENSAJE_AFINIDAD_OBLIGATORIA = "Afinidad obligatorio";
	public static final String MENSAJE_AFINIDAD_CODNUM_REQUERIDO = "id afinidad no válido";
	
	public static final String CODNUM = "CODNUM";
	public static final String CODIGO = "CODIGO";
	public static final String DESCRIPCION = "DESCRIPCION";
	public static final String MODULACION = "MODULACION";
	
	protected static ModeloAfinidad eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloAfinidad();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloAfinidad obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/**
	 * Listado de afinidades.
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de afinidades
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  error si no existe la area
	 */
	public BolsaEmpleoDataTable<Afinidad> listaAfinidadesDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<Afinidad> afinidades = new ArrayList<>();
		BolsaEmpleoDataTable<Afinidad> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta =
			"SELECT bepafi.* "
		  + "FROM TBEP_AFINIDADES bepafi "
		  + "WHERE FLGBORRADO!='S'"; 
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepafi.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepafi.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_MODULACION, "bepafi.MODULACION", DataTableColumn.COLUMN_TYPE_NUMBER);	
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {					
			
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Afinidad afinidad = createAfinidadFromResultSet(rs);
					afinidades.add(afinidad);
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(afinidades);
		}
		
		return dataTable;
	}	
	
	/**
	 * Consulta afinidades en BBDD y las devuelve.
	 * 
	 * @return todas las afinidades de la base de datos ordenadas por modulacion de mayor a menor
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Afinidad> listaAfinidades() throws SQLException {
		List<Afinidad> afinidades = new ArrayList<>();
		String consulta = "SELECT * FROM TBEP_AFINIDADES bepafi " + "WHERE bepafi.FLGBORRADO != 'S' "
				+ "ORDER BY bepafi.MODULACION DESC";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Afinidad afinidad = createAfinidadFromResultSet(rs);
					afinidades.add(afinidad);
				}
			}
		}

		return afinidades;
	}

	/**
	 * Añade una afinidad al sistema cerrada.
	 * @param afinidad .	 
	 * @return id afinidad creada.
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Integer nuevaAfinidad(Afinidad afinidad) throws SQLException, UVException {
		if (afinidad == null) {
			throw new UVException(MENSAJE_ERROR_AFINIDAD_NULL);
		}
		
		String consulta = "INSERT INTO TBEP_AFINIDADES (CODIGO, DESCRIPCION, MODULACION) VALUES (?, ?, ?)";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{CODNUM})) {
			int parameterIndex = 1;
			
			stmt.setString(parameterIndex++, afinidad.getCodigo());
			stmt.setString(parameterIndex++, afinidad.getDescripcion());
			stmt.setDouble(parameterIndex++, afinidad.getModulacion());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			
			return rs.getInt(1);
		}	
	}
	
	/**
	 * Devuelve un listado de afinidades por su id.
	 * @param ids codnum de afinidad
	 * @return listado de afinidades
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<Afinidad> getAfinidadesByIds(int[] ids) throws SQLException, UVException {
		List<Afinidad> afinidades = new ArrayList<>();
		
		for (int i = 0; i < ids.length; i++) {
			afinidades.add(this.getAfinidadById(ids[i]));
	    }
		
		return afinidades;
	}
	
	/**
	 * Devuelve una afinidad por su id.
	 * @param codNum .
	 * @return Afinidad o null si no existe
	 * @throws SQLException .
	 */
	public Afinidad getAfinidadById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_AFINIDAD);
		}
		String consulta = "SELECT bepafi.* FROM TBEP_AFINIDADES bepafi WHERE bepafi.CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_AFINIDAD);
				}
				
				return this.createAfinidadFromResultSet(rs);				
			}
		}
	}
		
	/** Actualiza una afinidad.
	 * @param afi Afinidad con los datos nuevos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de errores de validacion
	 */
	public void actualizaAfinidad(Afinidad afi) throws SQLException, UVException {
		if (afi == null) {
			throw new UVException(MENSAJE_AFINIDAD_OBLIGATORIA);
		}
		if (afi.getCodNum() == null) {
			throw new UVException(MENSAJE_AFINIDAD_CODNUM_REQUERIDO);
		}
		
		String consulta = "UPDATE tbep_afinidades "
			+ " SET CODIGO=?, DESCRIPCION=?, MODULACION=? "
			+ " WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
		PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, afi.getCodigo());
			stmt.setString(parameterIndex++, afi.getDescripcion());
			stmt.setDouble(parameterIndex++, afi.getModulacion());
			stmt.setInt(parameterIndex++, afi.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Elimina afinidades.
	 * @param afinidades Afinidades a borrar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si afinidad no es valida
	 */
	public void borraAfinidades(Collection<Afinidad> afinidades) throws SQLException, UVException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(afinidades.size());
		String query = "UPDATE tbep_afinidades SET FLGBORRADO= ?, FECHA_BORRADO = ? WHERE CODNUM IN (" + params + ")";		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, "S");
			
			stmt.setDate(indexParam++, (java.sql.Date) BolsaEmpleoUtils.getCurrentDate());
			for (Afinidad afiniad : afinidades) {
				stmt.setInt(indexParam++, afiniad.getCodNum()); 	
			}
			stmt.executeUpdate();
		}
	}
	
	/** Devuelve los tipos de afinidad disponibles.
	 * 
	 * @return lista de tipos de afinidad.
	 * @throws SQLException .
	 */
	public List<String> getTiposAfinidad() throws SQLException {
		List<String> afinidades = new ArrayList<>();
		String consulta = 
				"SELECT bepafi.CODIGO "
				+ "FROM TBEP_AFINIDADES bepafi "
				+ "WHERE bepafi.FLGBORRADO = 'N' GROUP BY bepafi.CODIGO";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					afinidades.add(rs.getString(CODIGO));
				}
			}
		}

		return afinidades;
	}
	
	private Afinidad createAfinidadFromResultSet(ResultSet rs) throws SQLException {
		Afinidad afinidad = new Afinidad();
		afinidad.setCodNum(rs.getInt(CODNUM));
		afinidad.setCodigo(rs.getString(CODIGO));
		afinidad.setDescripcion(rs.getString(DESCRIPCION));
		afinidad.setModulacion(rs.getDouble(MODULACION));
		afinidad.setBorrada("S".equals(rs.getString("FLGBORRADO")));
		afinidad.setFechaBorrada(rs.getDate("FECHA_BORRADO"));
		return afinidad;
	}
}
