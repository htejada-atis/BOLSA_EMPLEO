package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de items de baremación.
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloBaremacion {
	// ordenación apartados generales de baremación
	public static final int ORDER_COLUMN_INDEX_APARTADOS_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_ACTIVO = 2;
	
	// ordenación bloques de baremación
	public static final int ORDER_COLUMN_INDEX_BLOQUES_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_BLOQUES_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_BLOQUES_ACTIVO = 2;
	
	// ordenación ítems de baremación
	public static final int ORDER_COLUMN_INDEX_ITEMS_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_ITEMS_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_ITEMS_ACTIVO = 2;
	
	// opción de consulta por defecto devuelve la lista con todas las filas de la tabla
	public static final int OPCION_DEFAULT = 0;
	// opción de consulta que filtra la lista con un id dado
	public static final int OPCION_1 = 1;
	// opción de consulta para comprobar si ya existe un código al editar
	public static final int OPCION_2 = 2;
	// opción de consulta para comprobar si ya existe un código al agregar
	public static final int OPCION_3 = 3;
	// opción de consulta para obtener todos los ítems de un apartado
	public static final int OPCION_4 = 4;
	// opción de consulta para editar campos
	public static final int OPCION_5 = 5;
	// opción de consulta para desactivar o activar
	public static final int OPCION_6 = 6;
	
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS APARTADOBAREMACION  ********************************************/
	
	/** Consulta apartados en la BBDD y los devuelve .
	 * @param apartado .
	 * @param opcion opción según la consulta .
	 * @return lista de apartados .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private List<ApartadoBaremacion> listaApartados(ApartadoBaremacion apartado, int opcion) throws SQLException, UVException {
		List<ApartadoBaremacion> apartados = new ArrayList<>();
		String consulta = "SELECT bepapa.* FROM TBEP_APARTADOSBAREMACION bepapa ";
		
		if (opcion == OPCION_1 || opcion == OPCION_2 || opcion == OPCION_3) {
			if (apartado == null) {
				throw new UVException("apartado obligatorio");
			}
		}
		if (opcion == OPCION_1 || opcion == OPCION_2) {
			if (apartado.getCodNum() == null) {
				throw new UVException("id apartado no válido");
			}
		}
		if (opcion == OPCION_2 || opcion == OPCION_3) {
			if (apartado.getCodigo() == null || apartado.getCodigo().equals("")) {
				throw new UVException("código de apartado no válido");
			}
			
			consulta += "WHERE bepapa.CODIGO = ? ";
		}
		if (opcion == OPCION_1) {
			consulta += "WHERE bepapa.CODNUM = ? ";
		}
		if (opcion == OPCION_2) {
			consulta += "AND bepapa.CODNUM != ? ";
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			if (opcion == OPCION_2 || opcion == OPCION_3) {
				stmt.setString(parameterIndex++, apartado.getCodigo());
			}
			if (opcion == OPCION_1 || opcion == OPCION_2) {
				stmt.setInt(parameterIndex++, apartado.getCodNum());
			}
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					try {
						ApartadoBaremacion ap = new ApartadoBaremacion();
						ap.setCodNum(rs.getInt("CODNUM"));
						ap.setCodigo(rs.getString("CODIGO"));
						ap.setNombre(rs.getString("NOMBRE"));
						ap.setActivo(rs.getString("FLGACTIVO").equals("S"));
						apartados.add(ap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return apartados;
	}
	
	/** Consulta para obtener el último código de los apartados .
	 * @return bloque .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public ApartadoBaremacion getUltimoCodigoApartado() throws SQLException, UVException {
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		String consulta = "SELECT bepapa.CODIGO FROM TBEP_APARTADOSBAREMACION bepapa "
				+ " ORDER BY bepapa.CODIGO DESC"
				+ " FETCH FIRST 1 ROW ONLY";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					apartado.setCodigo("1");
				} else {
					apartado.setCodigo(rs.getString("CODIGO"));
				}
			}
		}
		
		return apartado;
	}
	
	/** lista todos los apartados.
	 * @return vector con todos los apartados .
	 * @throws SQLException si hay un error en la base de datos .
	 * @throws UVException .
	 */
	public List<ApartadoBaremacion> listaApartados() throws SQLException, UVException {
		return listaApartados(null, OPCION_DEFAULT);
	}
	
	/**
	 * Devuelve un apartado de baremación por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si apartado no es existe
	 */
	public ApartadoBaremacion getApartadoBaremacionById(Integer codNum) throws SQLException, UVException {
		List<ApartadoBaremacion> apartados = listaApartados(new ApartadoBaremacion(codNum), OPCION_1);
		if (apartados.isEmpty()) {
			throw new UVException("No existe apartado");
		}
		return apartados.get(0);
	}
	
	/**
	 * Listado de apartados generales de baremación. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado 
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<ApartadoBaremacion> listadoApartadosGeneralesBaremacionDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<ApartadoBaremacion> apartados = new ArrayList<>();
		BolsaEmpleoDataTable<ApartadoBaremacion> dataTable = new BolsaEmpleoDataTable<ApartadoBaremacion>(params);
		
		String consulta =
			"SELECT bepapa.* "
		  + "FROM TBEP_APARTADOSBAREMACION bepapa "		  
		  + "WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_CODIGO, "bepapa.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_NOMBRE, "bepapa.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_ACTIVO, "bepapa.FLGACTIVO");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ApartadoBaremacion apartado = new ApartadoBaremacion();
					apartado.setCodNum(rs.getInt("CODNUM"));
					apartado.setCodigo(rs.getString("CODIGO"));
					apartado.setNombre(rs.getString("NOMBRE"));
					apartado.setActivo(rs.getString("FLGACTIVO").equals("S"));	
					apartados.add(apartado);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(apartados);
		}
		
		return dataTable;
	}
	
	/** Actualiza un apartado .
	 * @param apartado con los datos nuevos a actualizar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaApartado(ApartadoBaremacion apartado, int opcion) throws SQLException, UVException {
		
		String consulta = "UPDATE TBEP_APARTADOSBAREMACION SET ";
		
		if (apartado == null) {
			throw new UVException("apartado obligatorio");
		}
		if (apartado.getCodNum() == null) {
			throw new UVException("id apartado no válido");
		}
		
		if (opcion == OPCION_5) {
			consulta += " codigo=?, nombre=?";
			
			if (apartado.getCodigo() == null) {
				throw new UVException("código obligatorio");
			}
			if (apartado.getNombre() == null) {
				throw new UVException("nombre obligatorio");
			}
			
			if (listaApartados(apartado, OPCION_2).size() > 0) {
				throw new UVException("ya existe un apartado con éste código");
			}
		}
		
		if (opcion == OPCION_6) {
			consulta += " flgactivo=?";
			
			if (apartado.isActivo() == null) {
				throw new UVException("activo obligatorio");
			}
		}
		
		consulta += " WHERE codnum=?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			
			if (opcion == OPCION_5) {
				stmt.setString(parameterIndex++, apartado.getCodigo());
				stmt.setString(parameterIndex++, apartado.getNombre());
			}
			if (opcion == OPCION_6) {
				stmt.setString(parameterIndex++, apartado.isActivo() ? "S" : "N");
			}
			
			stmt.setInt(parameterIndex++, apartado.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Función que inserta un apartado en la BD.
	 * @param apartado .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void insertaApartado(ApartadoBaremacion apartado) throws SQLException, UVException {
		if (apartado == null) {
			throw new UVException("No se puede insertar un apartado vacío");
		}
		if (apartado.getCodigo() == null) {
			throw new UVException("código obligatorio");
		}
		if (apartado.getNombre() == null) {
			throw new UVException("nombre obligatorio");
		}
		
		if (listaApartados(apartado, OPCION_3).size() > 0) {
			throw new UVException("ya existe un apartado con éste código");
		}
		
		String consulta = "INSERT INTO TBEP_APARTADOSBAREMACION " 
				+ " (CODIGO,NOMBRE)"
				+ " VALUES (?, ?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, apartado.getCodigo());
			stmt.setString(parameterIndex++, apartado.getNombre());
			stmt.executeUpdate();
		}
	}
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS BLOQUEBAREMACION  ********************************************/
	
	/** Consulta bloques en la BBDD y los devuelve .
	 * @param bloque .
	 * @param opcion opción según la consulta .
	 * @return lista de bloques .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private List<BloqueBaremacion> listaBloques(BloqueBaremacion bloque, int opcion) throws SQLException, UVException {
		List<BloqueBaremacion> bloques = new ArrayList<>();
		String consulta = "SELECT bepblo.* FROM TBEP_BLOQUESBAREMACION bepblo ";
		
		if (opcion == OPCION_1 || opcion == OPCION_2 || opcion == OPCION_3) {
			if (bloque == null) {
				throw new UVException("bloque obligatorio");
			}
		}
		if (opcion == OPCION_1 || opcion == OPCION_2) {
			if (bloque.getCodNum() == null) {
				throw new UVException("id bloque no válido");
			}
		}
		if (opcion == OPCION_2 || opcion == OPCION_3) {
			if (bloque.getCodigo() == null || bloque.getCodigo().equals("")) {
				throw new UVException("código de bloque no válido");
			}
			
			consulta += "WHERE bepblo.CODIGO = ? AND bepblo.BEPAPA_CODNUM = ? ";
		}
		if (opcion == OPCION_1) {
			consulta += "WHERE bepblo.CODNUM = ? ";
		}
		if (opcion == OPCION_2) {
			consulta += "AND bepblo.CODNUM != ? ";
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			if (opcion == OPCION_2 || opcion == OPCION_3) {
				stmt.setString(parameterIndex++, bloque.getCodigo());
				stmt.setInt(parameterIndex++, bloque.getApartadoBaremacion().getCodNum());
			}
			if (opcion == OPCION_1 || opcion == OPCION_2) {
				stmt.setInt(parameterIndex++, bloque.getCodNum());
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					try {
						BloqueBaremacion blo = new BloqueBaremacion();
						blo.setCodNum(rs.getInt("CODNUM"));
						blo.setCodigo(rs.getString("CODIGO"));
						blo.setNombre(rs.getString("NOMBRE"));
						blo.setActivo(rs.getString("FLGACTIVO").equals("S"));					
						blo.setApartadoBaremacion(this.getApartadoBaremacionById(rs.getInt("BEPAPA_CODNUM")));
						bloques.add(blo);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		return bloques;
	}
	
	/** Consulta para obtener el último código de los bloques de un apartado .
	 * @param apartadoId .
	 * @return bloque .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BloqueBaremacion getUltimoCodigoBloque(Integer apartadoId) throws SQLException, UVException {
		if (apartadoId == null) {
			throw new UVException("id de apartado requerido");
		}
		
		BloqueBaremacion bloque = new BloqueBaremacion();
		String consulta = "SELECT bepblo.CODIGO, bepblo.BEPAPA_CODNUM FROM TBEP_BLOQUESBAREMACION bepblo "
				+ " WHERE bepblo.BEPAPA_CODNUM = ? ORDER BY bepblo.CODIGO DESC"
				+ " FETCH FIRST 1 ROW ONLY";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, apartadoId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					bloque.setCodigo("1");
					bloque.setApartadoBaremacion(this.getApartadoBaremacionById(apartadoId));
				} else {
					bloque.setCodigo(rs.getString("CODIGO"));
					bloque.setApartadoBaremacion(this.getApartadoBaremacionById(rs.getInt("BEPAPA_CODNUM")));
				}
			}
		}
		
		return bloque;
	}
	
	/**
	 * Devuelve un bloque de baremación por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si bloque no es existe
	 */
	public BloqueBaremacion getBloqueBaremacionById(Integer codNum) throws SQLException, UVException {
		List<BloqueBaremacion> bloques = listaBloques(new BloqueBaremacion(codNum), OPCION_1);
		if (bloques.isEmpty()) {
			throw new UVException("No existe bloque");
		}
		return bloques.get(0);
	}
	
	/**
	 * Listado de bloques de baremación . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param apartado id del apartado .
	 * @return listado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<BloqueBaremacion> listadoBloquesBaremacionDatatable(Map<String, String[]> params, Integer apartado) throws SQLException, UVException {
		
		if (apartado == null) {
			throw new UVException("No se pueden listar bloques sin el id del apartado");
		}
		
		List<BloqueBaremacion> bloques = new ArrayList<>();
		BolsaEmpleoDataTable<BloqueBaremacion> dataTable = new BolsaEmpleoDataTable<BloqueBaremacion>(params);
		
		String consulta = "SELECT bepblo.* FROM TBEP_BLOQUESBAREMACION bepblo"
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa"
				+ " ON bepapa.CODNUM = bepblo.CODNUM"
				+ " WHERE bepblo.BEPAPA_CODNUM = ? ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_CODIGO, "bepblo.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_NOMBRE, "bepblo.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_ACTIVO, "bepblo.FLGACTIVO");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, apartado);
			stmtCount.setInt(indexParam++, apartado);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					BloqueBaremacion bloque = new BloqueBaremacion();
					bloque.setCodNum(rs.getInt("CODNUM"));
					bloque.setCodigo(rs.getString("CODIGO"));
					bloque.setNombre(rs.getString("NOMBRE"));
					bloque.setActivo(rs.getString("FLGACTIVO").equals("S"));
					bloque.setApartadoBaremacion(this.getApartadoBaremacionById(rs.getInt("BEPAPA_CODNUM")));
					bloques.add(bloque);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bloques);
		}
		
		return dataTable;
	}
	
	/** Actualiza un bloque .
	 * @param bloque con los datos nuevos a actualizar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaBloque(BloqueBaremacion bloque, int opcion) throws SQLException, UVException {
		
		String consulta = "UPDATE TBEP_BLOQUESBAREMACION SET ";
		
		if (bloque == null) {
			throw new UVException("bloque obligatorio");
		}
		if (bloque.getCodNum() == null) {
			throw new UVException("id bloque no válido");
		}
		
		if (opcion == OPCION_5) {
			consulta += " codigo=?, nombre=?, bepapa_codnum=?";
			
			if (bloque.getCodigo() == null) {
				throw new UVException("código obligatorio");
			}
			if (bloque.getNombre() == null) {
				throw new UVException("nombre obligatorio");
			}
			
			if (bloque.getApartadoBaremacion().getCodNum() == null) {
				throw new UVException("apartado obligatorio");
			}
			
			if (listaBloques(bloque, OPCION_2).size() > 0) {
				throw new UVException("ya existe un bloque con éste código");
			}
		}
		
		if (opcion == OPCION_6) {
			consulta += " flgactivo=?";
			
			if (bloque.isActivo() == null) {
				throw new UVException("activo obligatorio");
			}
		}
		
		consulta += " WHERE codnum=?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			
			if (opcion == OPCION_5) {
				stmt.setString(parameterIndex++, bloque.getCodigo());
				stmt.setString(parameterIndex++, bloque.getNombre());
				stmt.setInt(parameterIndex++, bloque.getApartadoBaremacion().getCodNum());
			}
			if (opcion == OPCION_6) {
				stmt.setString(parameterIndex++, bloque.isActivo() ? "S" : "N");
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
			throw new UVException("No se puede insertar un bloque vacío");
		}
		if (bloque.getCodigo() == null) {
			throw new UVException("codigo obligatorio");
		}
		if (bloque.getNombre() == null) {
			throw new UVException("nombre obligatorio");
		}
		
		if (listaBloques(bloque, OPCION_3).size() > 0) {
			throw new UVException("ya existe un bloque con éste código");
		}
		
		String consulta = "INSERT INTO TBEP_BLOQUESBAREMACION " 
				+ " (CODIGO,NOMBRE,BEPAPA_CODNUM)"
				+ " VALUES (?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, bloque.getCodigo());
			stmt.setString(parameterIndex++, bloque.getNombre());
			stmt.setInt(parameterIndex++, bloque.getApartadoBaremacion().getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS ITEMBAREMACION  ********************************************/
	
	/** Consulta items en la BBDD y los devuelve .
	 * @param item .
	 * @param opcion opción según la consulta .
	 * @return lista de items .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private List<ItemBaremacion> listaItems(ItemBaremacion item, int opcion) throws SQLException, UVException {
		List<ItemBaremacion> items = new ArrayList<>();
		String consulta = "SELECT bepite.* FROM TBEP_ITEMSBAREMACION bepite ";
		
		if (opcion == OPCION_1 || opcion == OPCION_2 || opcion == OPCION_3) {
			if (item == null) {
				throw new UVException("item obligatorio");
			}
		}
		if (opcion == OPCION_1 || opcion == OPCION_2) {
			if (item.getCodNum() == null) {
				throw new UVException("id item no válido");
			}
		}
		if (opcion == OPCION_2 || opcion == OPCION_3) {
			if (item.getCodigo() == null || item.getCodigo().equals("")) {
				throw new UVException("código de item no válido");
			}
			
			consulta += "WHERE bepite.CODIGO = ? AND bepite.BEPBLO_CODNUM = ? ";
		}
		if (opcion == OPCION_1) {
			consulta += "WHERE bepite.CODNUM = ? ";
		}
		if (opcion == OPCION_2) {
			consulta += "AND bepite.CODNUM != ? ";
		}
		if (opcion == OPCION_4) {
			if (item.getCodNum() == null) {
				throw new UVException("id apartado no válido");
			}
			
			consulta += "INNER JOIN TBEP_BLOQUESBAREMACION bepblo"
					+ " ON bepblo.CODNUM = bepite.BEPBLO_CODNUM "
					+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa "
					+ " ON bepapa.CODNUM = bepblo.BEPAPA_CODNUM "
					+ " WHERE bepapa.CODNUM = ?";
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			if (opcion == OPCION_2 || opcion == OPCION_3) {
				stmt.setString(parameterIndex++, item.getCodigo());
				stmt.setInt(parameterIndex++, item.getBloqueBaremacion().getCodNum());
			}
			if (opcion == OPCION_1 || opcion == OPCION_2 || opcion == OPCION_4) {
				stmt.setInt(parameterIndex++, item.getCodNum());
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					try {
						ItemBaremacion it = new ItemBaremacion();
						it.setCodNum(rs.getInt("CODNUM"));
						it.setCodigo(rs.getString("CODIGO"));
						it.setNombre(rs.getString("NOMBRE"));
						it.setActivo(rs.getString("FLGACTIVO").equals("S"));					
						it.setBloqueBaremacion(this.getBloqueBaremacionById(rs.getInt("BEPBLO_CODNUM")));
						items.add(it);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			}
		return items;
	}
	
	/** Consulta para obtener el último código de los ítems de un bloque .
	 * @param bloqueId .
	 * @return bloque .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public ItemBaremacion getUltimoCodigoItem(Integer bloqueId) throws SQLException, UVException {
		if (bloqueId == null) {
			throw new UVException("id de bloque requerido");
		}
		
		ItemBaremacion item = new ItemBaremacion();
		String consulta = "SELECT bepite.CODIGO, bepite.BEPBLO_CODNUM FROM TBEP_ITEMSBAREMACION bepite "
				+ " WHERE bepite.BEPBLO_CODNUM = ? ORDER BY bepite.CODIGO DESC"
				+ " FETCH FIRST 1 ROW ONLY";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, bloqueId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					item.setCodigo("1");
					item.setBloqueBaremacion(this.getBloqueBaremacionById(bloqueId));
				} else {
					item.setCodigo(rs.getString("CODIGO"));
					item.setBloqueBaremacion(this.getBloqueBaremacionById(rs.getInt("BEPBLO_CODNUM")));
				}
			}
		}
		
		return item;
	}
	
	/** lista todos los ítems de un apartado.
	 * @param id .
	 * @return vector con todos los ítems .
	 * @throws SQLException si hay un error en la base de datos .
	 * @throws UVException .
	 */
	public List<ItemBaremacion> listaItemsApartado(Integer id) throws SQLException, UVException {
		return listaItems(new ItemBaremacion(id), OPCION_4);
	}
	
	/**
	 * Devuelve un ítem de baremación por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException si ítem no es existe .
	 */
	public ItemBaremacion getItemBaremacionById(Integer codNum) throws SQLException, UVException {
		List<ItemBaremacion> items = listaItems(new ItemBaremacion(codNum), OPCION_1);
		if (items.isEmpty()) {
			throw new UVException("No existe ítem");
		}
		return items.get(0);
	}
	
	/**
	 * Listado de ítems de baremación . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param bloque id del bloque .
	 * @return listado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<ItemBaremacion> listadoItemsBaremacionDatatable(Map<String, String[]> params, Integer bloque) throws SQLException, UVException {
		
		if (bloque == null) {
			throw new UVException("No se pueden listar ítems sin el id del bloque");
		}
		
		List<ItemBaremacion> items = new ArrayList<>();
		BolsaEmpleoDataTable<ItemBaremacion> dataTable = new BolsaEmpleoDataTable<ItemBaremacion>(params);
		
		String consulta = "SELECT bepite.* FROM TBEP_ITEMSBAREMACION bepite"
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo"
				+ " ON bepblo.CODNUM = bepite.CODNUM"
				+ " WHERE bepite.BEPBLO_CODNUM = ? ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_CODIGO, "bepite.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_NOMBRE, "bepite.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_ACTIVO, "bepite.FLGACTIVO");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, bloque);
			stmtCount.setInt(indexParam++, bloque);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ItemBaremacion item = new ItemBaremacion();
					item.setCodNum(rs.getInt("CODNUM"));
					item.setCodigo(rs.getString("CODIGO"));
					item.setNombre(rs.getString("NOMBRE"));
					item.setActivo(rs.getString("FLGACTIVO").equals("S"));
					item.setBloqueBaremacion(this.getBloqueBaremacionById(rs.getInt("BEPBLO_CODNUM")));
					items.add(item);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(items);
		}
		
		return dataTable;
	}
	
	/** Actualiza un item .
	 * @param item con los datos nuevos a actualizar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaItem(ItemBaremacion item, int opcion) throws SQLException, UVException {
		
		String consulta = "UPDATE TBEP_ITEMSBAREMACION SET ";
		
		if (item == null) {
			throw new UVException("item obligatorio");
		}
		if (item.getCodNum() == null) {
			throw new UVException("id item no válido");
		}
		
		if (opcion == OPCION_5) {
			consulta += " codigo=?, nombre=?, bepblo_codnum=?";
			
			if (item.getCodigo() == null) {
				throw new UVException("código obligatorio");
			}
			if (item.getNombre() == null) {
				throw new UVException("nombre obligatorio");
			}
			if (item.getBloqueBaremacion().getCodNum() == null) {
				throw new UVException("bloque obligatorio");
			}
			
			if (listaItems(item, OPCION_2).size() > 0) {
				throw new UVException("ya existe un item con éste código");
			}
		}
		
		if (opcion == OPCION_6) {
			consulta += " flgactivo=?";
			
			if (item.isActivo() == null) {
				throw new UVException("activo obligatorio");
			}
		}
		
		consulta += " WHERE codnum=?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			
			if (opcion == OPCION_5) {
				stmt.setString(parameterIndex++, item.getCodigo());
				stmt.setString(parameterIndex++, item.getNombre());
				stmt.setInt(parameterIndex++, item.getBloqueBaremacion().getCodNum());
			}
			if (opcion == OPCION_6) {
				stmt.setString(parameterIndex++, item.isActivo() ? "S" : "N");
			}
			
			stmt.setInt(parameterIndex++, item.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Función que inserta un ítem en la BD.
	 * @param item .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void insertaItem(ItemBaremacion item) throws SQLException, UVException {
		if (item == null) {
			throw new UVException("No se puede insertar un item vacío");
		}
		if (item.getCodigo() == null) {
			throw new UVException("código obligatorio");
		}
		if (item.getNombre() == null) {
			throw new UVException("nombre obligatorio");
		}
		
		if (listaItems(item, OPCION_3).size() > 0) {
			throw new UVException("ya existe un item con éste código");
		}
		
		String consulta = "INSERT INTO TBEP_ITEMSBAREMACION "
				+ " (CODIGO,NOMBRE,BEPBLO_CODNUM)"
				+ " VALUES (?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, item.getCodigo());
			stmt.setString(parameterIndex++, item.getNombre());
			stmt.setInt(parameterIndex++, item.getBloqueBaremacion().getCodNum());
			stmt.executeUpdate();
		}
	}
	
}
