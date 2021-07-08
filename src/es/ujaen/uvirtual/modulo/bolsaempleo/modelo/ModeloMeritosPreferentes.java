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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteOpcion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de méritos preferentes.
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloMeritosPreferentes {
	// ordenación 
	public static final int ORDER_COLUMN_INDEX_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_TIPO = 2;
	public static final int ORDER_COLUMN_INDEX_APLICABLE = 3;
	public static final int ORDER_COLUMN_INDEX_FACTOR = 4;
	public static final int ORDER_COLUMN_INDEX_ACTIVO = 5;

	public static final int ORDER_COLUMN_INDEX_OPCIONES_NOMBRE = 0;
	public static final int ORDER_COLUMN_INDEX_OPCIONES_FACTOR = 1;
	
	public static final int MAX_LENGTH_COLUMN_NOMBRE = 500;
	public static final int MAX_LENGTH_COLUMN_OBSERVACIONES = 1000;
	public static final int MAX_LENGTH_COLUMN_CODIGO = 10;
	public static final int MAX_LENGTH_COLUMN_NOMBRE_OPCION = 50;	
	
	// tipo de meritos preferentes
	public static final String TIPO_TITULACION_PREFERENTE = "TITULACION_PREFERENTE";
	public static final String TIPO_MERITO = "MERITO";
	public static final String TIPO_POSESION = "POSESION";
		
	// tipo de cálculo
	public static final String TIPO_CALCULO_FACTOR = "FACTOR";
	public static final String TIPO_CALCULO_VALOR_MERITO_FACTOR = "VALOR_MERITO_FACTOR";
	public static final String TIPO_CALCULO_VALOR_MERITO_MAYOR_QUE = "VALOR_MERITO_MAYOR_QUE";
	public static final String TIPO_CALCULO_OPCIONES = "OPCIONES";
	
	// tipos de aplicable
	public static final String APLICABLE_BLOQUE = "BLOQUE";
	public static final String APLICABLE_APARTADO = "APARTADO";
	public static final String APLICABLE_ITEM = "ITEM"; 
	public static final String APLICABLE_TOTAL = "TOTAL";
		
	public static final String ERROR_MERITO_NOEXITE = "El mérito no existe";
	public static final String ERROR_MERITO_REQUERIDO = "El merito preferente es requerido";
	public static final String OPCION_MERITO_REQUERIDO = "Opción de mérito requerido";
	public static final String OPCION_MERITO_NOEXISTE = "No existe la opción del mérito";
	public static final String MERITO_PREFERENTE_TIPO_MERITO_NOEXISTE = "No existe mérito preferente de tipo mérito";
	
	private static final String CODNUM = "CODNUM";
	private static final String FACTOR = "FACTOR";

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
			throw new UVException(ERROR_MERITO_REQUERIDO);
		}
		
		String sql = "SELECT bepmep.* FROM TBEP_MERITOS_PREFERENTES bepmep WHERE bepmep.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return this.createMeritoPreferenteFromResultSet(rs);					
				}
			}
		}
		
		throw new UVException(ERROR_MERITO_NOEXITE);
	}
	
	/**
	 * Devuelve la opción del mérito por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public MeritoPreferenteOpcion getMeritoPreferenteOpcionById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(OPCION_MERITO_REQUERIDO);
		}
		
		String sql = "SELECT bepmpo.* FROM TBEP_MER_PRE_OPCIONES bepmpo WHERE bepmpo.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return this.createMeritoPreferenteOpcionFromResultSet(rs);					
				}
			}
		}
		
		throw new UVException(OPCION_MERITO_NOEXISTE);
	}
	
	/** Listado de méritos preferentes . 
	 * @return lista méritos preferentes .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoPreferente> listaMeritosPreferentesActivos() throws SQLException, UVException {
		List<MeritoPreferente> meritosPreferentes = new ArrayList<>();
		
		String consulta = "SELECT bepmep.* FROM TBEP_MERITOS_PREFERENTES bepmep WHERE FLGACTIVO = 'S'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoPreferente row = this.createMeritoPreferenteFromResultSet(rs);
					meritosPreferentes.add(row);
				}
			}
		}
		
		return meritosPreferentes;
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
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE, "bepmep.NOMBRE");
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
					MeritoPreferente row = this.createMeritoPreferenteFromResultSet(rs);
					apartados.add(row);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(apartados);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado opciones méritos preferentes.
	 * @param params .
	 * @param merito .
	 * @return . 
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<MeritoPreferenteOpcion> listadoOpcionesMeritosPreferentes(Map<String, String[]> params, MeritoPreferente merito) 
			throws SQLException, UVException {
		List<MeritoPreferenteOpcion> data = new ArrayList<>();
		BolsaEmpleoDataTable<MeritoPreferenteOpcion> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepmpo.* FROM TBEP_MER_PRE_OPCIONES bepmpo WHERE bepmpo.BEPMEP_CODNUM = ? AND bepmpo.FLGBORRADO = 'N' ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_OPCIONES_NOMBRE, "bepmpo.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_OPCIONES_FACTOR, "bepmpo.FACTOR");
				
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			
			int paramIndex = 1;
			stmt.setInt(paramIndex, merito.getCodNum());
			stmtCount.setInt(paramIndex++, merito.getCodNum());

			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoPreferenteOpcion row = this.createMeritoPreferenteOpcionFromResultSet(rs);
					data.add(row);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}

		return dataTable;
	}
	
	/**
	 * Listado opciones de un merito preferente ordenador por factor.
	 * @param merito .
	 * @return . 
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoPreferenteOpcion> listadoOpcionesMeritosPreferentes(MeritoPreferente merito) throws SQLException {
		List<MeritoPreferenteOpcion> data = new ArrayList<>();
		
		String consulta = "SELECT bepmpo.* FROM TBEP_MER_PRE_OPCIONES bepmpo "
				+ "WHERE bepmpo.BEPMEP_CODNUM = ? AND bepmpo.FLGBORRADO = 'N' ORDER BY bepmpo.FACTOR ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {			
			int paramIndex = 1;
			stmt.setInt(paramIndex, merito.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoPreferenteOpcion row = this.createMeritoPreferenteOpcionFromResultSet(rs);
					data.add(row);
				}
			}					
		}

		return data;
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
					meritosPreferentesPosesion.add(this.createMeritoPreferenteFromResultSet(rs));
				}
			}
		}
		
		return meritosPreferentesPosesion;
	}
	
	/** Devuelve el merito preferente de tipo mérito .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public MeritoPreferente getMeritoPreferenteTipoMerito() throws SQLException, UVException {
		String consulta = "SELECT bepmep.* FROM TBEP_MERITOS_PREFERENTES bepmep WHERE bepmep.FLGACTIVO = 'S' AND bepmep.TIPO = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, ModeloMeritosPreferentes.TIPO_MERITO);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return this.createMeritoPreferenteFromResultSet(rs);
				}
			}
		}
		
		throw new UVException(MERITO_PREFERENTE_TIPO_MERITO_NOEXISTE);
	}
	
	/**
	 * Inserta un merito en la db y devuelve el objeto insertado.
	 * @param merito .
	 * @param usuarioUpdate .
	 * @return codNum creado .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Integer crearMeritoPreferente(MeritoPreferente merito, UsuarioBolsaEmpleo usuarioUpdate) throws UVException, SQLException {
		if (merito == null) {
			throw new UVException("No se puede insertar un merito preferente vacio");
		}
		
		String sql = "INSERT INTO TBEP_MERITOS_PREFERENTES ("
				+ "CODIGO,NOMBRE,OBSERVACIONES,TIPO,APLICABLE,BASE,FACTOR,VALOR_MAXIMO,"
				+ "BEPITE_TIPO_CODNUM,BEPBLO_APLICABLE_CODNUM,BEPAPA_APLICABLE_CODNUM,BEPITE_APLICABLE_CODNUM,UID_USUARIO) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmt = conexion.prepareStatement(sql, new String[]{CODNUM})) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, merito.getCodigo());
			stmt.setString(parameterIndex++, merito.getNombre());
			stmt.setString(parameterIndex++, merito.getObservaciones());
			stmt.setString(parameterIndex++, merito.getTipo());
			stmt.setString(parameterIndex++, merito.getAplicable());
			
			if (merito.getBase() != null) {
				stmt.setDouble(parameterIndex++, merito.getBase());
			} else {
				stmt.setNull(parameterIndex++, Types.NUMERIC);
			}
			
			stmt.setDouble(parameterIndex++, merito.getFactor());
						
			if (merito.getValorMaximo() != null) {
				stmt.setDouble(parameterIndex++, merito.getValorMaximo());
			} else {
				stmt.setNull(parameterIndex++, Types.NUMERIC);
			}
			
			if (merito.getTipoItemBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getTipoItemBaremacion().getCodNum());				
			} else {
				stmt.setNull(parameterIndex++, Types.INTEGER);
			}
			
			if (merito.getAplicableBloqueBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableBloqueBaremacion().getCodNum());				
			} else {
				stmt.setNull(parameterIndex++, Types.INTEGER);
			}
			if (merito.getAplicableApartadoBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableApartadoBaremacion().getCodNum());				
			} else {
				stmt.setNull(parameterIndex++, Types.INTEGER);
			}
			if (merito.getAplicableItemBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableItemBaremacion().getCodNum());				
			} else {
				stmt.setNull(parameterIndex++, Types.INTEGER);
			}
			
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			
			return rs.getInt(1);			
		}
	} 
	
	/**
	 * Edita un mérito preferente .
	 * @param merito .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException . 
	 */
	public void editarMeritoPreferente(MeritoPreferente merito, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String query = "UPDATE TBEP_MERITOS_PREFERENTES SET "
				+ "	CODIGO = ?,"
				+ "	NOMBRE = ?,"
				+ "	OBSERVACIONES = ?,"
				+ "	TIPO = ?,"
				+ "	TIPO_CALCULO = ?,"
				+ "	APLICABLE = ?,"
				+ "	BASE = ?,"
				+ "	FACTOR = ?,"
				+ "	VALOR_MAXIMO = ?,"
				+ "	BEPITE_TIPO_CODNUM = ?,"
				+ "	BEPBLO_APLICABLE_CODNUM = ?,"
				+ "	BEPAPA_APLICABLE_CODNUM = ?,"
				+ "	BEPITE_APLICABLE_CODNUM = ?,"
				+ "	FLGACTIVO = ?,"
				+ "	UID_USUARIO = ?"
				+ "	WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, merito.getCodigo());
			stmt.setString(parameterIndex++, merito.getNombre());
			stmt.setString(parameterIndex++, merito.getObservaciones());
			stmt.setString(parameterIndex++, merito.getTipo());
			stmt.setString(parameterIndex++, merito.getTipoCalculo());
			stmt.setString(parameterIndex++, merito.getAplicable());
			if (merito.getBase() != null) {
				stmt.setDouble(parameterIndex++, merito.getBase());
			} else {
				stmt.setNull(parameterIndex++, Types.NUMERIC);
			}
			stmt.setDouble(parameterIndex++, merito.getFactor());
			if (merito.getValorMaximo() != null) {
				stmt.setDouble(parameterIndex++, merito.getValorMaximo());
			} else {
				stmt.setNull(parameterIndex++, Types.NUMERIC);
			}
			if (merito.getTipoItemBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getTipoItemBaremacion().getCodNum());	
			} else {
				stmt.setNull(parameterIndex++, Types.INTEGER);
			}
			if (merito.getAplicableBloqueBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableBloqueBaremacion().getCodNum());
			} else {
				stmt.setNull(parameterIndex++, Types.INTEGER);
			}
			if (merito.getAplicableApartadoBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableApartadoBaremacion().getCodNum());
			} else {
				stmt.setNull(parameterIndex++, Types.INTEGER);
			}
			if (merito.getAplicableItemBaremacion() != null) {
				stmt.setInt(parameterIndex++, merito.getAplicableItemBaremacion().getCodNum());
			} else {
				stmt.setNull(parameterIndex++, Types.INTEGER);
			}
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(merito.getActivo()) ? "S" : "N");
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, merito.getCodNum());
			
			stmt.executeUpdate();
		}		
	}	
	
	/**
	 * Activa el merito. 
	 * @param merito .
	 * @param flgactivo .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void cambiaFlagActivoMeritoPreferente(MeritoPreferente merito, String flgactivo, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String query = "UPDATE TBEP_MERITOS_PREFERENTES SET FLGACTIVO = ?, UID_USUARIO = ? WHERE CODNUM IN ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, flgactivo);
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
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
	
	/**
	 * Crea un merito preferente opción.
	 * @param meritoOpcion .
	 * @param usuarioUpdate .
	 * @return .
	 * @throws SQLException .
	 */
	public Integer crearMeritoPreferenteOpcion(MeritoPreferenteOpcion meritoOpcion, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String sql = "INSERT INTO TBEP_MER_PRE_OPCIONES (BEPMEP_CODNUM,NOMBRE,FACTOR,UID_USUARIO) VALUES (?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmt = conexion.prepareStatement(sql, new String[]{CODNUM})) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, meritoOpcion.getMeritoPreferenteCodNum());
			stmt.setString(parameterIndex++, meritoOpcion.getNombre());
			stmt.setDouble(parameterIndex++, meritoOpcion.getFactor());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			
			return rs.getInt(1);			
		}
	} 
	
	/**
	 * Desactiva una opción .
	 * @param meritoOpcion .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void desactivarMeritoPreferenteOpcion(MeritoPreferenteOpcion meritoOpcion, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String query = "UPDATE TBEP_MER_PRE_OPCIONES SET FLGBORRADO = ?, FECHA_BORRADO = ?, UID_USUARIO = ? WHERE CODNUM IN ?";		
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, "S");
			stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, meritoOpcion.getCodNum());
			stmt.executeUpdate();
		}
	}
		
	private MeritoPreferente createMeritoPreferenteFromResultSet(ResultSet rs) throws SQLException, UVException {
		MeritoPreferente obj = new MeritoPreferente();
		obj.setCodNum(rs.getInt(CODNUM));
		obj.setCodigo(rs.getString("CODIGO"));
		obj.setNombre(rs.getString("NOMBRE"));
		obj.setObservaciones(rs.getString("OBSERVACIONES"));
		obj.setTipo(rs.getString("TIPO"));
		obj.setTipoCalculo(rs.getString("TIPO_CALCULO"));
		obj.setAplicable(rs.getString("APLICABLE"));
		obj.setBase(rs.getDouble("BASE") == 0 ? null : rs.getDouble("BASE"));
		obj.setFactor(rs.getDouble(FACTOR));
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
	
	private MeritoPreferenteOpcion createMeritoPreferenteOpcionFromResultSet(ResultSet rs) throws SQLException {
		MeritoPreferenteOpcion obj = new MeritoPreferenteOpcion();
		obj.setCodNum(rs.getInt(CODNUM));
		obj.setMeritoPreferenteCodNum(rs.getInt("BEPMEP_CODNUM"));
		obj.setNombre(rs.getString("NOMBRE"));
		obj.setFactor(rs.getDouble(FACTOR));
		obj.setBorrado("S".equals(rs.getString("FLGBORRADO")));
		obj.setFechaBorrado(rs.getDate("FECHA_BORRADO"));
		return obj;
	}
}
