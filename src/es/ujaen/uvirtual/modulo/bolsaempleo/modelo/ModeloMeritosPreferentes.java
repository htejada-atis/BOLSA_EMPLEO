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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de méritos preferentes.
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloMeritosPreferentes {
	// ordenación 
	public static final int ORDER_COLUMN_INDEX_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 1;
	public static final int ORDER_COLUMN_INDEX_TIPO = 2;
	public static final int ORDER_COLUMN_INDEX_APLICABLE = 3;
	public static final int ORDER_COLUMN_INDEX_FACTOR = 4;
	public static final int ORDER_COLUMN_INDEX_ACTIVO = 5;
	
	public static final int MAX_LENGTH_COLUMN_DESCRIPCION = 1000;
	public static final int MAX_LENGTH_COLUMN_FACTOR = 20;
	public static final int MAX_LENGTH_COLUMN_CODIGO = 10;
	
	// tipo de meritos preferentes
	public static final String TIPO_TITULACION_PREFERENTE = "TITULACION_PREFERENTE";
	public static final String TIPO_MERITO = "MERITO";
	public static final String TIPO_POSESION = "POSESION";
		
	// tipo de cálculo
	public static final String TIPO_CALCULO_FACTOR = "FACTOR";
	public static final String TIPO_CALCULO_VALOR_MERITO_FACTOR = "VALOR_MERITO_FACTOR";
	public static final String TIPO_CALCULO_VALOR_MERITO_MAYOR_QUE = "VALOR_MERITO_MAYOR_QUE";
	
	// tipos de aplicable
	public static final String APLICABLE_BLOQUE = "BLOQUE";
	public static final String APLICABLE_APARTADO = "APARTADO";
	public static final String APLICABLE_ITEM = "ITEM"; 
	public static final String APLICABLE_TOTAL = "TOTAL";
		
	public static final String ERROR_MERITO_NOEXITE = "El mérito no existe";

	protected static ModeloMeritosPreferentes eInstancia;

	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMeritosPreferentes();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloMeritosPreferentes obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
		
    /**
	 * Devuelve un mérito preferente por su id.
	 * @param codNum .
	 * @return merito o excepción si no existe
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException .
	 */
	public MeritoPreferente getMeritoPreferenteById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("El merito preferente es requerido");
		}
		
		String sql = "SELECT bepmep.* FROM TBEP_MERITOS_PREFERENTES bepmep WHERE bepmep.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createApartadoFromResultSet(rs);					
				}
			}
		}
		
		throw new UVException(ERROR_MERITO_NOEXITE);		
	}
    
	/**
	 * Listado. 
	 * @param params .
	 * @return . 
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<MeritoPreferente> listadoMeritosPreferentes(Map<String, String[]> params) throws SQLException, UVException {
		List<MeritoPreferente> apartados = new ArrayList<>();
		BolsaEmpleoDataTable<MeritoPreferente> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepmep.* FROM TBEP_MERITOS_PREFERENTES bepmep WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepmep.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepmep.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_TIPO, "bepmep.TIPO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APLICABLE, "bepmep.APLICABLE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_FACTOR, "bepmep.FACTOR");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTIVO, "bepmep.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
				
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoPreferente row = this.createApartadoFromResultSet(rs);
					apartados.add(row);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(apartados);
		}
		
		return dataTable;
	}
		
	/** Consulta meritos preferentes en BBDD y las devuelve.
	 * @return todos los meritos de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException .
	 */
	public List<MeritoPreferente> listaMeritosPreferentes() throws SQLException, UVException {
		List<MeritoPreferente> meritos = new ArrayList<>();
		String consulta = "SELECT bepmep.* "
				  + "FROM TBEP_MERITOS_PREFERENTES bepmep";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoPreferente row = this.createApartadoFromResultSet(rs);
					meritos.add(row);		
				}
			}
		}
		
		return meritos;
	}

	/**
	 * Devuelve los meritos preferentes por posesión.
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoPreferente> getMeritosPreferentesPorPosesion() throws SQLException, UVException {
		String consulta = "SELECT bepmep.* FROM TBEP_MERITOS_PREFERENTES bepmep WHERE bepmep.FLGACTIVO = 'S' AND bepmep.TIPO = ?";
		ArrayList<MeritoPreferente> meritosPreferentesPosesion = new ArrayList<>();
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, ModeloMeritosPreferentes.TIPO_POSESION);
						
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					meritosPreferentesPosesion.add(this.createApartadoFromResultSet(rs));
				}
			}
		}
		
		return meritosPreferentesPosesion;
	}
	
	/**
	 * Inserta un merito en la db.
	 * @param merito .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void crearMeritoPreferente(MeritoPreferente merito) throws UVException, SQLException {
		if (merito == null) {
			throw new UVException("No se puede insertar un merito preferente vacio");
		}
		
		String sql = "INSERT INTO TBEP_MERITOS_PREFERENTES ("
				+ "CODIGO,DESCRIPCION,TIPO,APLICABLE,FACTOR,VALOR_MAXIMO,"
				+ "BEPITE_TIPO_CODNUM,BEPBLO_APLICABLE_CODNUM,BEPAPA_APLICABLE_CODNUM,BEPITE_APLICABLE_CODNUM) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, merito.getCodigo());
			stmt.setString(parameterIndex++, merito.getDescripcion());
			stmt.setString(parameterIndex++, merito.getTipo());
			stmt.setString(parameterIndex++, merito.getAplicable());
			stmt.setDouble(parameterIndex++, merito.getFactor());
						
			if (merito.getValorMaximo() != null) {
				stmt.setDouble(parameterIndex++, merito.getValorMaximo());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			
			if (merito.getTipoItemBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getTipoItemBaremacion().getCodNum());				
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			
			if (merito.getAplicableBloqueBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableBloqueBaremacion().getCodNum());				
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			if (merito.getAplicableApartadoBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableApartadoBaremacion().getCodNum());				
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			if (merito.getAplicableItemBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableItemBaremacion().getCodNum());				
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
						
			stmt.executeUpdate();
		}
	} 
	
	/**
	 * Edita un mérito preferente .
	 * @param merito .
	 * @throws SQLException .
	 * @throws UVException . 
	 */
	public void editarMeritoPreferente(MeritoPreferente merito) throws SQLException {
		String query = "UPDATE TBEP_MERITOS_PREFERENTES SET "
				+ "CODIGO = ?, "
				+ "DESCRIPCION = ?, "
				+ "TIPO = ?, "
				+ "TIPO_CALCULO = ?, "
				+ "APLICABLE = ?, "
				+ "BASE = ?, "
				+ "FACTOR = ?, "
				+ "VALOR_MAXIMO = ?, "
				+ "BEPITE_TIPO_CODNUM = ?, "
				+ "BEPBLO_APLICABLE_CODNUM = ?, "
				+ "BEPAPA_APLICABLE_CODNUM = ?, "
				+ "BEPITE_APLICABLE_CODNUM = ?, "
				+ "FLGACTIVO = ? "
				+ "WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, merito.getCodigo());
			stmt.setString(parameterIndex++, merito.getDescripcion());
			stmt.setString(parameterIndex++, merito.getTipo());
			stmt.setString(parameterIndex++, merito.getTipoCalculo());
			stmt.setString(parameterIndex++, merito.getAplicable());
			if (merito.getBase() != null) {
				stmt.setDouble(parameterIndex++, merito.getBase());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			stmt.setDouble(parameterIndex++, merito.getFactor());
			if (merito.getValorMaximo() != null) {
				stmt.setDouble(parameterIndex++, merito.getValorMaximo());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			if (merito.getTipoItemBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getTipoItemBaremacion().getCodNum());	
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			if (merito.getAplicableBloqueBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableBloqueBaremacion().getCodNum());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			if (merito.getAplicableApartadoBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableApartadoBaremacion().getCodNum());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			if (merito.getAplicableItemBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableItemBaremacion().getCodNum());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(merito.getActivo()) ? "S" : "N");
			stmt.setInt(parameterIndex++, merito.getCodNum());
			
			stmt.executeUpdate();
		}		
	}	
	
	/**
	 * Activa el merito. 
	 * @param merito .
	 * @param flgactivo .
	 * @throws SQLException .
	 */
	public void cambiaFlagActivoMeritoPreferente(MeritoPreferente merito, String flgactivo) throws SQLException {
		String query = "UPDATE TBEP_MERITOS_PREFERENTES SET FLGACTIVO = ? WHERE CODNUM IN ?";		
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, flgactivo);
			stmt.setInt(indexParam++, merito.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Chequea si hay un mérito activo con el codigo pasado.
	 * @param merito .
	 * @return .
	 * @throws SQLException .
	 */
	public boolean isMeritoActivoConCodigo(MeritoPreferente merito) throws SQLException {		
		String query = "SELECT COUNT(*) as count FROM TBEP_MERITOS_PREFERENTES WHERE FLGACTIVO = 'S' AND CODIGO = ?";
		
		if (merito.getCodNum() != null) {
			query += " AND CODNUM <> ? ";
		}
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(query)) {
			int param = 1;
			stmt.setString(param++, merito.getCodigo());			
			if (merito.getCodNum() != null) {
				stmt.setInt(param++, merito.getCodNum());
			}
			
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				
				if (rs.getInt("count") > 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private MeritoPreferente createApartadoFromResultSet(ResultSet rs) throws SQLException, UVException {
		MeritoPreferente obj = new MeritoPreferente();
		obj.setCodNum(rs.getInt("CODNUM"));
		obj.setCodigo(rs.getString("CODIGO"));
		obj.setDescripcion(rs.getString("DESCRIPCION"));
		obj.setTipo(rs.getString("TIPO"));
		obj.setTipoCalculo(rs.getString("TIPO_CALCULO"));
		obj.setAplicable(rs.getString("APLICABLE"));
		obj.setBase(rs.getDouble("BASE") == 0 ? null : rs.getDouble("BASE"));
		obj.setFactor(rs.getDouble("FACTOR"));
		obj.setValorMaximo(rs.getDouble("VALOR_MAXIMO") == 0 ? null : rs.getDouble("VALOR_MAXIMO"));
		obj.setTipoItemBaremacion(rs.getInt("BEPITE_TIPO_CODNUM") == 0 ? null 
			: ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(rs.getInt("BEPITE_TIPO_CODNUM")));
		obj.setAplicableApartadoBaremacion(rs.getInt("BEPAPA_APLICABLE_CODNUM") == 0 ? null 
			: ModeloBaremacionApartados.obtenerInstancia().getApartadoBaremacionById(rs.getInt("BEPAPA_APLICABLE_CODNUM")));
		obj.setAplicableBloqueBaremacion(rs.getInt("BEPBLO_APLICABLE_CODNUM") == 0 ? null 
			: ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(rs.getInt("BEPBLO_APLICABLE_CODNUM")));
		obj.setAplicableItemBaremacion(rs.getInt("BEPITE_APLICABLE_CODNUM") == 0 ? null 
			: ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(rs.getInt("BEPITE_APLICABLE_CODNUM")));
		obj.setActivo("S".equals(rs.getString("FLGACTIVO")));
		return obj;
	}
}
