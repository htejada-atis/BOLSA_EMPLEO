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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de items de baremación.
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloBaremacionItems {
	// ordenación ítems de baremación
	
	public static final int ORDER_COLUMN_INDEX_ITEMS_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_ITEMS_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_ITEMS_UNIDADES = 2;
	public static final int ORDER_COLUMN_INDEX_ITEMS_VALOR = 3;
	public static final int ORDER_COLUMN_INDEX_ITEMS_VALOR_MINIMO = 4; 
	public static final int ORDER_COLUMN_INDEX_ITEMS_VALOR_MAXIMO = 5;
	public static final int ORDER_COLUMN_INDEX_ITEMS_AFINIDAD = 6;
	public static final int ORDER_COLUMN_INDEX_ITEMS_ACTIVO = 7;
	
	// ordenación ítems de baremación exclusiones
	
	public static final int ORDER_COLUMN_INDEX_ITEMS_EXCLUYENTES_ID = 1;
	public static final int ORDER_COLUMN_INDEX_ITEMS_EXCLUYENTES_CODIGO = 2;
	public static final int ORDER_COLUMN_INDEX_ITEMS_EXCLUYENTES_NOMBRE = 3;
	public static final int ORDER_COLUMN_INDEX_ITEMS_EXCLUYENTES_ACTIVO = 4;
	
	// tipos de unidades de los items
	
	public static final String ITEM_UNIDADES_MEDICION_ENTERO = "ENTERO";
	public static final String ITEM_UNIDADES_MEDICION_DECIMAL = "DECIMAL";
	public static final String ITEM_UNIDADES_MEDICION_SINO = "SI/NO";
		
	// errores
	public static final String ERROR_ITEM_MISMO_CODIGO = "Ya existe un ítem con el código introducido";
	public static final String ERROR_ITEM_NOEXITE = "Item no encontrado";
	public static final String ERROR_ITEM_REQUERIDO = "El ítem de baremación es requerido";
	
	public static final Integer COLUMN_CODIGO_MAXLENGTH = 3; 
	public static final Integer COLUMN_NOMBRE_MAXLENGTH = 1000;
	public static final Integer COLUMN_DESCRIPCION_MAXLENGTH = 1000;

	public static final Double MINIMO_VALOR_FLOAT = 0.01;
	public static final Double MAXIMO_VALOR_PORCENTAGE = 100.0;
	
	public static final String CODIGO = "CODIGO";
	public static final String CODNUM = "CODNUM";

	protected static ModeloBaremacionItems eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloBaremacionItems();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
	public static ModeloBaremacionItems obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/**
	 * Devuelve un item de baremación por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si bloque no es existe
	 */
	public ItemBaremacion getItemBaremacionById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(ERROR_ITEM_REQUERIDO);
		}
		
		String sql = "SELECT bepite.* FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createItemFromResultSet(rs);					
				}
			}
		}
		
		throw new UVException(ERROR_ITEM_NOEXITE);			
	}
	
	/**
	 * Listado de ítems de baremación . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param bloque id del bloque .
	 * @return listado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<ItemBaremacion> listadoItemsBaremacionDatatable(Map<String, String[]> params, BloqueBaremacion bloque) throws SQLException, UVException {
		List<ItemBaremacion> items = new ArrayList<>();
		BolsaEmpleoDataTable<ItemBaremacion> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepite.* FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.BEPBLO_CODNUM = ?";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_CODIGO, "bepite.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_NOMBRE, "bepite.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_UNIDADES, "bepite.UNIDADES");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_VALOR, "bepite.VALOR");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_VALOR_MINIMO, "bepite.VALOR_MINIMO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_VALOR_MAXIMO, "bepite.VALOR_MAXIMO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_AFINIDAD, "bepite.AFINIDAD");		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_ACTIVO, "bepite.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, bloque.getCodNum());
			stmtCount.setInt(indexParam++, bloque.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {					
					items.add(this.createItemFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(items);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado de ítems de baremación excluyentes entre si. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param item id del item .
	 * @return listado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<ItemBaremacion> listadoItemsBaremacionExcluyentesDatatable(Map<String, String[]> params, 
			ItemBaremacion item) throws SQLException, UVException {
		List<ItemBaremacion> items = new ArrayList<>();
		BolsaEmpleoDataTable<ItemBaremacion> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = ""
				+ " SELECT bepite.*, SEL.HIJO "
				+ " FROM TBEP_ITEMSBAREMACION bepite "
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM "
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM "
				+ " LEFT JOIN (SELECT bepmex.BEPITE_CODNUM_HIJO HIJO FROM TBEP_ITEMSBAREMACION bepite " 
				+ "	LEFT JOIN TBEP_MERITOS_EXCLUYENTES bepmex ON bepite.CODNUM = bepmex.BEPITE_CODNUM_HIJO AND bepmex.BEPITE_CODNUM_PADRE = ?) "
				+ "		SEL ON SEL.HIJO = bepite.CODNUM "
				+ " WHERE bepite.CODNUM != ? ";
		
		String whereCodigo = String.format("(%s || '.' || %s || '.' || %s)", "bepapa.CODIGO", "bepblo.CODIGO", "bepite.CODIGO");
		String orderCodigo = String.format("(%s || '.' || %s || '.' || %s) %%s", "LPAD(bepapa.CODIGO, 3)", "LPAD(bepblo.CODIGO, 3)", "LPAD(bepite.CODIGO, 3)");
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_EXCLUYENTES_ID, "bepite.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_EXCLUYENTES_CODIGO, whereCodigo, DataTableColumn.COLUMN_TYPE_TEXT, orderCodigo);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_EXCLUYENTES_NOMBRE, "bepite.NOMBRE");	
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_EXCLUYENTES_ACTIVO, "bepite.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, item.getCodNum());
			stmtCount.setInt(indexParam++, item.getCodNum());
			
			stmt.setInt(indexParam, item.getCodNum());
			stmtCount.setInt(indexParam++, item.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {					
					items.add(this.createItemFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(items);
		}
		
		return dataTable;
	}
	
	/** Devuelve los items excluyentes de otro.
	 * @param item .
	 * @return meritos .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<ItemBaremacion> getItemsExcluyentes(ItemBaremacion item) throws SQLException, UVException {
		ArrayList<ItemBaremacion> items = new ArrayList<>();
		
		String consulta = "SELECT bepite.*,bepmex.BEPITE_CODNUM_HIJO FROM TBEP_ITEMSBAREMACION bepite "
				+ "LEFT JOIN TBEP_MERITOS_EXCLUYENTES bepmex "
				+ "ON bepite.CODNUM = bepmex.BEPITE_CODNUM_HIJO "
				+ "AND bepmex.BEPITE_CODNUM_PADRE = ? "
				+ "WHERE bepmex.BEPITE_CODNUM_PADRE = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, item.getCodNum());
			stmt.setInt(indexParam++, item.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					if (rs.getString(CODIGO) != null) {
						items.add(this.createItemFromResultSet(rs));
					}
				}
			}
		}
		return items;
	}
	
	/** Consulta para obtener el último código de los items de un bloque .
	 * @param bloque .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public String getUltimoCodigoItem(BloqueBaremacion bloque) throws SQLException {		
		String consulta = "SELECT bepite.CODIGO, bepite.BEPBLO_CODNUM "
				+ "FROM TBEP_ITEMSBAREMACION bepite "
				+ "WHERE bepite.BEPBLO_CODNUM = ? "
				+ "ORDER BY bepite.CODIGO DESC "
				+ "FETCH FIRST 1 ROW ONLY";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, bloque.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return "0";
				} else {
					return rs.getString(CODIGO);
				}
			}
		}
	}
	
	/** Actualiza un item .
	 * @param item con los datos nuevos a actualizar .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaItem(ItemBaremacion item, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		this.chequearItemParaInsertarOActualizar(item);
		
		String consulta = "UPDATE TBEP_ITEMSBAREMACION SET "
				+ "CODIGO = ?, "
				+ "NOMBRE = ?, "
				+ "DESCRIPCION = ?, "
				+ "FLGACTIVO = ?, "
				+ "UNIDADES = ?, "
				+ "VALOR = ?, "
				+ "VALOR_MINIMO = ?, "
				+ "VALOR_MAXIMO = ?, "
				+ "AFINIDAD = ?, "
				+ "INDIVIDUALIZADO = ?, "
				+ "UID_USUARIO = ?"
				+ "WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, item.getCodigo());
			stmt.setString(parameterIndex++, item.getNombre());
			stmt.setString(parameterIndex++, item.getDescripcion());
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(item.getActivo()) ? "S" : "N");
			stmt.setString(parameterIndex++, item.getUnidades());
			stmt.setDouble(parameterIndex++, item.getValor());
			stmt.setDouble(parameterIndex++, item.getValorMinimo());
			stmt.setDouble(parameterIndex++, item.getValorMaximo());
			if (item.getAfinidad() == null) {
				stmt.setNull(parameterIndex++, Types.NULL);
			} else {
				stmt.setString(parameterIndex++, item.getAfinidad());	
			}	
			if (item.getIndividualizado() == null) {
				stmt.setString(parameterIndex++, "N");
			} else {
				stmt.setString(parameterIndex++, Boolean.TRUE.equals(item.getIndividualizado()) ? "S" : "N");
			}			
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, item.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** 
	 * Desactiva un item de baremación.
	 * @param item .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desactivarItem(ItemBaremacion item, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		this.activaDesactivaItem(item, false, usuarioUpdate);
	}
	
	/** 
	 * Activa un item de baremación.
	 * @param item .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void activarItem(ItemBaremacion item, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		this.activaDesactivaItem(item, true, usuarioUpdate);
	}

	/** Función que inserta un ítem en la BD.
	 * @param item .
	 * @param usuarioInsert .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Integer insertaItem(ItemBaremacion item, UsuarioBolsaEmpleo usuarioInsert) throws SQLException, UVException {
		if (item == null) {
			throw new UVException(ERROR_ITEM_REQUERIDO);
		}
		
		this.chequearItemParaInsertarOActualizar(item);
		
		String consulta = "INSERT INTO TBEP_ITEMSBAREMACION "
				+ " (CODIGO,NOMBRE,DESCRIPCION,BEPBLO_CODNUM,UNIDADES,VALOR,VALOR_MINIMO,VALOR_MAXIMO,AFINIDAD,INDIVIDUALIZADO,UID_USUARIO)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{CODNUM})) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, item.getCodigo());
			stmt.setString(parameterIndex++, item.getNombre());
			stmt.setString(parameterIndex++, item.getDescripcion());
			stmt.setInt(parameterIndex++, item.getBloqueBaremacion().getCodNum());
			stmt.setString(parameterIndex++, item.getUnidades());
			stmt.setDouble(parameterIndex++, item.getValor());
			stmt.setDouble(parameterIndex++, item.getValorMinimo());
			stmt.setDouble(parameterIndex++, item.getValorMaximo());
			stmt.setString(parameterIndex++, item.getAfinidad());
			if (item.getIndividualizado() == null) {
				stmt.setString(parameterIndex++, "N");
			} else {
				stmt.setString(parameterIndex++, Boolean.TRUE.equals(item.getIndividualizado()) ? "S" : "N");
			}			
			stmt.setString(parameterIndex++, usuarioInsert.getCodCuenta());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			
			return rs.getInt(1);
		}
	}
	
	/** Lista todos los ítems de un apartado.
	 * @param apartado .
	 * @return vector con todos los ítems .
	 * @throws SQLException si hay un error en la base de datos .
	 * @throws UVException .
	 */
	public List<ItemBaremacion> getItemsDeApartado(ApartadoBaremacion apartado) throws SQLException, UVException {
		List<ItemBaremacion> items = new ArrayList<>();
		
		String consulta = ""
				+ "SELECT bepite.* "
				+ "FROM TBEP_ITEMSBAREMACION bepite "
				+ "INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM "
				+ "WHERE bepblo.BEPAPA_CODNUM = ? "
				+ "ORDER BY LPAD(bepblo.CODIGO, 3) || LPAD(bepite.CODIGO, 3)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, apartado.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ItemBaremacion item = this.getItemBaremacionById(rs.getInt(CODNUM)); 							
					items.add(item);
				}
			}
		}
		
		return items;
	}

	/**
	 * Devuelve los items de bareamación activos.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<ItemBaremacion> listaItemBaremacion() throws SQLException, UVException {
		return listaItemBaremacion(" WHERE FLGACTIVO = 'S' ORDER BY codigo");
	} 
	
	private List<ItemBaremacion> listaItemBaremacion(String clausula) throws SQLException, UVException {
		List<ItemBaremacion> items = new ArrayList<>();
		String consulta = "SELECT bepite.* FROM TBEP_ITEMSBAREMACION bepite " + clausula;

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					items.add(this.createItemFromResultSet(rs));
				}
			}
		}
		return items;
	}
	
	private ItemBaremacion createItemFromResultSet(ResultSet rs) throws SQLException, UVException {	
		ItemBaremacion item = new ItemBaremacion();
		item.setCodNum(rs.getInt(CODNUM));
		item.setBloqueBaremacion(ModeloBaremacionBloques.obtenerInstancia().getBloqueBaremacionById(rs.getInt("BEPBLO_CODNUM")));
		item.setCodigo(rs.getString(CODIGO));
		item.setNombre(rs.getString("NOMBRE"));
		item.setDescripcion(rs.getString("DESCRIPCION"));
		item.setActivo("S".equals(rs.getString("FLGACTIVO")));		
		item.setUnidades(rs.getString("UNIDADES"));
		item.setValor(rs.getDouble("VALOR"));
		item.setValorMinimo(rs.getDouble("VALOR_MINIMO"));
		item.setValorMaximo(rs.getDouble("VALOR_MAXIMO"));
		item.setAfinidad(rs.getString("AFINIDAD"));
		item.setIndividualizado("S".equals(rs.getString("INDIVIDUALIZADO")));
		return item;
	}
	
	private void activaDesactivaItem(ItemBaremacion item, Boolean activo, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String consulta = "UPDATE TBEP_ITEMSBAREMACION SET FLGACTIVO = ?, UID_USUARIO = ? WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(activo) ? "S" : "N");
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, item.getCodNum());
			stmt.executeUpdate();
		}	
	}
	
	private void chequearItemParaInsertarOActualizar(ItemBaremacion item) throws SQLException, UVException {
		if (this.existeOtroItemActivoPorCodigo(item)) {
			throw new UVException(ERROR_ITEM_MISMO_CODIGO);
		}
		
		ArrayList<String> unidades = new ArrayList<>();
		unidades.add(ITEM_UNIDADES_MEDICION_ENTERO);
		unidades.add(ITEM_UNIDADES_MEDICION_DECIMAL);
		unidades.add(ITEM_UNIDADES_MEDICION_SINO);
			
		if (!unidades.contains(item.getUnidades())) {
			throw new UVException("Tipo de unidad no válida");
		}
		
		if (item.getValorMinimo() < MINIMO_VALOR_FLOAT) {
			throw new UVException("El valor mínimo deber al menos " + MINIMO_VALOR_FLOAT);
		}
		
		if (item.getValorMaximo() < MINIMO_VALOR_FLOAT) {
			throw new UVException("El valor máximo deber al menos " + MINIMO_VALOR_FLOAT);
		}
		
		if (item.getValorMinimo() > item.getValorMaximo()) {
			throw new UVException("El valor máximo deber mayor que el valor mínimo");
		}
	}

	private boolean existeOtroItemActivoPorCodigo(ItemBaremacion item) throws SQLException {		
		String sql = "SELECT bepite.* "
				+ "FROM TBEP_ITEMSBAREMACION bepite "
				+ "WHERE 1=1 "
				+ "AND bepite.BEPBLO_CODNUM = ? "
				+ "AND bepite.CODIGO = ? "
				+ "AND bepite.FLGACTIVO = 'S' ";
		
		if (item.getCodNum() != null) {
			sql += " AND bepite.CODNUM <> ? ";
		}
		
		sql += " FETCH FIRST 1 ROW ONLY";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, item.getBloqueBaremacion().getCodNum());
			stmt.setString(parameterIndex++, item.getCodigo());
			
			if (item.getCodNum() != null) {
				stmt.setInt(parameterIndex++, item.getCodNum());
			}
						
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return true;									
				}
			}
		}
		
		return false;
	} 		
	
	/** El usuario selecciona un item para excluirlo de otro item .
	 * @param itemPadre .
	 * @param itemHijo .
	 * @param usuarioInsert .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void asignarItemsExcluyentesAItem(ItemBaremacion itemPadre, ItemBaremacion itemHijo, UsuarioBolsaEmpleo usuarioInsert) throws SQLException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {			
			String consulta = "INSERT INTO TBEP_MERITOS_EXCLUYENTES (BEPITE_CODNUM_PADRE, BEPITE_CODNUM_HIJO, UID_USUARIO) VALUES (?,?,?)";
			
			try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int indexParam = 1;
				stmt.setInt(indexParam++, itemPadre.getCodNum());
				stmt.setInt(indexParam++, itemHijo.getCodNum());
				stmt.setString(indexParam++, usuarioInsert.getCodCuenta());
				stmt.executeUpdate();
			}
		}
	}
	
	
	/** El usuario deselecciona un item para borrarlo de su lista de excluyentes de otro .
	 * @param itemPadre .
	 * @param itemHijo .
	 * @param usuarioDelete .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void borrarItemsExcluyentesAItem(ItemBaremacion itemPadre, ItemBaremacion itemHijo, UsuarioBolsaEmpleo usuarioDelete) throws SQLException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			String consultaUpdate = "UPDATE TBEP_MERITOS_EXCLUYENTES SET UID_USUARIO = ? WHERE BEPITE_CODNUM_PADRE = ? AND BEPITE_CODNUM_HIJO = ?";			
			try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
				int indexParam = 1;
				stmt.setString(indexParam++, usuarioDelete.getCodCuenta());
				stmt.setInt(indexParam++, itemPadre.getCodNum());
				stmt.setInt(indexParam++, itemHijo.getCodNum());
				stmt.executeUpdate();
			}
			
			String consulta = "DELETE FROM TBEP_MERITOS_EXCLUYENTES WHERE BEPITE_CODNUM_PADRE = ? AND BEPITE_CODNUM_HIJO = ?";		
			try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int indexParam = 1;
				stmt.setInt(indexParam++, itemPadre.getCodNum());
				stmt.setInt(indexParam++, itemHijo.getCodNum());
				stmt.executeUpdate();
			}
		}
	}
	
	
	/** Comprueba que el item que se ha seleccionado no es excluyente con uno ya existente en la solicitud .
	 * @param itemPadre .
	 * @param itemHijo .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void checkItemsExcluyentes(ItemBaremacion itemPadre, ItemBaremacion itemHijo) throws SQLException, UVException {
		String consulta = ""
				+ "SELECT bepite.* "
				+ "FROM TBEP_ITEMSBAREMACION bepite "
				+ "LEFT JOIN TBEP_MERITOS_EXCLUYENTES bepmex ON bepite.CODNUM = bepmex.BEPITE_CODNUM_HIJO AND bepmex.BEPITE_CODNUM_PADRE = ? "
				+ "WHERE bepmex.BEPITE_CODNUM_PADRE = ? AND bepmex.BEPITE_CODNUM_HIJO = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, itemPadre.getCodNum());
				stmt.setInt(parameterIndex++, itemPadre.getCodNum());
				stmt.setInt(parameterIndex++, itemHijo.getCodNum());
				stmt.executeUpdate();
				
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						throw new UVException("El item " + itemPadre.getFullCode() 
							+ " es excluyente con el item " + itemHijo.getFullCode());						
					}
				}
			}
			
			try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, itemHijo.getCodNum());
				stmt.setInt(parameterIndex++, itemHijo.getCodNum());
				stmt.setInt(parameterIndex++, itemPadre.getCodNum());
				stmt.executeUpdate();
				
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						throw new UVException("El item " + itemHijo.getFullCode() 
							+ " es excluyente con el item " + itemPadre.getFullCode());
					}
				}
			}
		}
	}
}
