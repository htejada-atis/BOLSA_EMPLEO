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
	
	// tipo de meritos preferentes
	public static final String TIPO_TITULACION_PREFERENTE = "TITULACION_PREFERENTE";
	public static final String TIPO_MERITO = "MERITO";
	
	// tipos de aplicable
	public static final String APLICABLE_BLOQUE = "BLOQUE";
	public static final String APLICABLE_APARTADO = "APARTADO";
	public static final String APLICABLE_ITEM = "ITEM"; 
	public static final String APLICABLE_TOTAL = "TOTAL";
	
	public static final String ERROR_MERITO_NOEXITE = "El mérito no existe";
		
    protected static ModeloMeritosPreferentes eInstancia = null;
	
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
		String sql = "SELECT bepmep.* FROM TBEP_MERITOS_PREFERENTES bepmep WHERE bepmep.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
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
		BolsaEmpleoDataTable<MeritoPreferente> dataTable = new BolsaEmpleoDataTable<MeritoPreferente>(params);
		
		String consulta =
			"SELECT bepmep.* "
		  + "FROM TBEP_MERITOS_PREFERENTES bepmep "		  
		  + "WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepmep.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepmep.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_TIPO, "bepmep.TIPO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APLICABLE, "bepmep.APLICABLE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_FACTOR, "bepmep.FACTOR");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTIVO, "bepmep.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
				
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
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
	 */
	public List<MeritoPreferente> listaMeritosPreferentes() throws SQLException {
		List<MeritoPreferente> meritos = new ArrayList<>();
		String consulta = "SELECT bepmep.* "
				  + "FROM TBEP_MERITOS_PREFERENTES bepmep";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							MeritoPreferente row = this.createApartadoFromResultSet(rs);
							meritos.add(row);		
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		
		return meritos;
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
		
		this.validateMeritoPreferente(merito);
						
		String sql = "INSERT INTO TBEP_MERITOS_PREFERENTES ("
				+ "DESCRIPCION,TIPO,APLICABLE,FACTOR,VALOR_MAXIMO,"
				+ "BEPITE_TIPO_CODNUM,BEPBLO_APLICABLE_CODNUM,BEPAPA_APLICABLE_CODNUM,BEPITE_APLICABLE_CODNUM) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, merito.getDescripcion());
			stmt.setString(parameterIndex++, merito.getTipo());
			stmt.setString(parameterIndex++, merito.getAplicable());
			stmt.setString(parameterIndex++, merito.getFactor());
						
			if (merito.getValorMaximo() != null) {
				stmt.setFloat(parameterIndex++, merito.getValorMaximo());
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
	public void editarMeritoPreferente(MeritoPreferente merito) throws UVException, SQLException {
		this.validateMeritoPreferente(merito);
		
		// TODO, condiciones para poder modificar un mérito preferente
		String query = "UPDATE TBEP_MERITOS_PREFERENTES SET "
				+ "DESCRIPCION = ?, "
				+ "TIPO = ?, "
				+ "APLICABLE = ?, "
				+ "FACTOR = ?, "
				+ "VALOR_MAXIMO = ?, "
				+ "BEPITE_TIPO_CODNUM = ?, "
				+ "BEPBLO_APLICABLE_CODNUM = ?, "
				+ "BEPAPA_APLICABLE_CODNUM = ?, "
				+ "BEPITE_APLICABLE_CODNUM = ?, "
				+ "FLGACTIVO = ? "
				+ "WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, merito.getDescripcion());
			stmt.setString(parameterIndex++, merito.getTipo());
			stmt.setString(parameterIndex++, merito.getAplicable());
			stmt.setString(parameterIndex++, merito.getFactor());
			if (merito.getValorMaximo() != null) {
				stmt.setFloat(parameterIndex++, merito.getValorMaximo());
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
			stmt.setString(parameterIndex++, merito.getActivo() ? "S" : "N");
			stmt.setInt(parameterIndex++, merito.getCodNum());
			
			stmt.executeUpdate();
		}		
	}	
	
	private void validateMeritoPreferente(MeritoPreferente merito) throws UVException {
		if (merito.getDescripcion().isBlank()) {
			throw new UVException("La descripción es requerida");
		}
		if (merito.getDescripcion().length() > MAX_LENGTH_COLUMN_DESCRIPCION) {
			throw new UVException("La descripción tiene demasiados caracteres. Máximo: " + MAX_LENGTH_COLUMN_DESCRIPCION);
		}		
		
		if (merito.getFactor().isBlank()) {
			throw new UVException("El factor es requerido");
		} 
		if (merito.getFactor().length() > MAX_LENGTH_COLUMN_FACTOR) { 
			throw new UVException("El factor tiene demasiados caracteres. Máximo: " + MAX_LENGTH_COLUMN_FACTOR);
		}
		
		if (merito.getValorMaximo() != null && merito.getValorMaximo() <= 0) {
			throw new UVException("El valor máximo debe ser mayor que cero");
		}
		
		// comprobamos si el factor es una expresión válida
		// TODO: librería para parsear una expresión
//		try {
//			ScriptEngineManager manager = new ScriptEngineManager(null);
//			ScriptEngine engine = manager.getEngineByName("JavaScript");
//			System.out.println("engine " + engine);
//			
//			Object result = engine.eval(merito.getFactor().replace("N", "0"));
//			System.out.println(result);
//		} catch (ScriptException ex) {
//			throw new UVException("El factor contiene una expresión inválida");
//		}
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

	
	
	private MeritoPreferente createApartadoFromResultSet(ResultSet rs) throws SQLException, UVException {
		MeritoPreferente obj = new MeritoPreferente();
		obj.setCodNum(rs.getInt("CODNUM"));
		obj.setDescripcion(rs.getString("DESCRIPCION"));
		obj.setTipo(rs.getString("TIPO"));		
		obj.setAplicable(rs.getString("APLICABLE"));
		obj.setFactor(rs.getString("FACTOR"));
		obj.setValorMaximo(rs.getFloat("VALOR_MAXIMO") == 0 ? null : rs.getFloat("VALOR_MAXIMO"));
		obj.setTipoItemBaremacion(rs.getInt("BEPITE_TIPO_CODNUM") == 0 ? null 
			: ModeloBaremacion.obtenerInstancia().getItemBaremacionById(rs.getInt("BEPITE_TIPO_CODNUM")));
		obj.setAplicableApartadoBaremacion(rs.getInt("BEPAPA_APLICABLE_CODNUM") == 0 ? null 
			: ModeloBaremacion.obtenerInstancia().getApartadoBaremacionById(rs.getInt("BEPAPA_APLICABLE_CODNUM")));
		obj.setAplicableBloqueBaremacion(rs.getInt("BEPBLO_APLICABLE_CODNUM") == 0 ? null 
			: ModeloBaremacion.obtenerInstancia().getBloqueBaremacionById(rs.getInt("BEPBLO_APLICABLE_CODNUM")));
		obj.setAplicableItemBaremacion(rs.getInt("BEPITE_APLICABLE_CODNUM") == 0 ? null 
			: ModeloBaremacion.obtenerInstancia().getItemBaremacionById(rs.getInt("BEPITE_APLICABLE_CODNUM")));
		obj.setActivo(rs.getString("FLGACTIVO").equals("S"));
		return obj;
	}

			
}
