package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de meritos. 
 * @author ATISoluciones 2021
 */
public class ModeloMerito {
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_BLOQUE = 2;
	public static final int ORDER_COLUMN_INDEX_ITEM = 3;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_ITEM = 4;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 5;
	public static final int ORDER_COLUMN_INDEX_VALOR = 6;
	public static final int ORDER_COLUMN_INDEX_OBSERVACION = 7;
	
	public static final int COLUMN_DESCRIPCION_MAXLENGTH = 200;
	public static final int COLUMN_OBSERVACION_MAXLENGTH = 300;

	protected static ModeloMerito eInstancia;

	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMerito();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
	public static ModeloMerito obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/** Consulta méritos en BBDD y los devuelve .
	 * @param id para devolver un mérito .
	 * @return lista de los méritos de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	private List<Merito> listaMeritos(Integer id) throws SQLException, UVException {
		List<Merito> meritos = new ArrayList<>();
		String consulta = "SELECT bepmer.* FROM tbep_meritos bepmer ";
		
		if (id != null) {
			consulta += "WHERE codnum = ?";
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			if (id != null) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, id);
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Merito mer = this.createMeritoFromResultset(rs, true, true); 								
					meritos.add(mer);
				}
			}
			}
		return meritos;
	}
	
	/** lista todos los méritos de la BBDD .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<Merito> listaMeritos() throws SQLException, UVException {
		return listaMeritos(null);
	}
	
	/** obtiene un mérito a partir de su id.
	 * @param id del mérito .
	 * @return archivo con el id especificado .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public Merito listaMerito(Integer id) throws SQLException, UVException {
		List<Merito> meritos = listaMeritos(id);
		if (meritos.isEmpty()) {
			throw new UVException("No existe mérito");
		}
		return meritos.get(0);
	}
	
	/**	Función que elimina méritos .
	 * @param meritos a eliminar .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD .
	 */
	public void eliminarMeritos(List<String> meritos, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				String params = BolsaEmpleoUtils.consultaMultiplesParametros(meritos.size());
				
				// actualiza el usuario de los méritos antes de eliminar
				String consultaUpdate = "UPDATE TBEP_MERITOS SET UID_USUARIO = ? WHERE CODNUM IN (" + params + ")";
				
				try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
					int indexParam = 1;
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					for (String merito: meritos) {
						stmt.setString(indexParam++, merito);
					}
					stmt.executeUpdate();
				}
				
				// delete usuarios seleccionados
				String consultaDelete = "DELETE FROM TBEP_MERITOS WHERE CODNUM IN (" + params + ")";
				
				try (PreparedStatement stmt = conexion.prepareStatement(consultaDelete)) {
					int indexParam = 1;
					for (String merito: meritos) {
						stmt.setString(indexParam++, merito);
					}
					stmt.executeUpdate();
				}
				
				conexion.commit();
				conexion.setAutoCommit(true);
			} catch (Exception e) {
				conexion.rollback();
				conexion.setAutoCommit(true);
				throw e;
			}
		}
	}
	
	/**	Función que inserta un mérito .
	 * @param merito a insertar .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public void insertaMerito(Merito merito, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (merito == null) {
			throw new UVException("No se puede insertar un mérito vacío");
		}
		if (merito.getDescripcion() == null || "".equals(merito.getDescripcion())) {
			throw new UVException("No se puede insertar un mérito sin descripción");
		}
		if (merito.getValor() == null) {
			throw new UVException("No se puede insertar un mérito sin valor");
		}
		if (merito.getArchivo() == null) {
			throw new UVException("No se puede insertar un mérito sin archivo");
		}
		if (merito.getItemBaremacion().getCodNum() == null) {
			throw new UVException("No se puede insertar un mérito sin ítem de baremación");
		}
		if (usuarioUpdate == null) {
			throw new UVException("No se puede insertar un mérito sin usuario");
		}
		
		String consulta = "INSERT INTO tbep_meritos " 
				+ " (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO) "
				+ "VALUES (?,?,?,?,?,?,?)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, merito.getItemBaremacion().getCodNum());
			stmt.setInt(parameterIndex++, usuarioUpdate.getCodNum());
			stmt.setFloat(parameterIndex++, merito.getValor());
			stmt.setString(parameterIndex++, merito.getDescripcion());
			stmt.setString(parameterIndex++, merito.getObservacion());
			stmt.setBinaryStream(parameterIndex++, merito.getArchivo());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();
		}
	}
	
	/** Función que edita un mérito .
	 * @param merito .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void actualizaMerito(Merito merito, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (merito == null) {
			throw new UVException("No se puede modificar un mérito vacío");
		}
		if (merito.getCodNum() == null) {
			throw new UVException("No se puede modificar un mérito sin id");
		}
		if (merito.getItemBaremacion().getCodNum() == null) {
			throw new UVException("No se puede modificar un mérito sin ítem de baremación");
		}
		if (merito.getValor() == null) {
			throw new UVException("No se puede modificar un mérito sin valor");
		}
		if (usuarioUpdate == null) {
			throw new UVException("No se puede modificar un mérito sin usuario");
		}
		
		String consulta = "UPDATE TBEP_MERITOS bepmer" 
				+ " SET BEPITE_CODNUM = ?, VALOR = ?, UID_USUARIO = ?"
				+ " WHERE bepmer.CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				 PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, merito.getItemBaremacion().getCodNum());
				stmt.setFloat(parameterIndex++, merito.getValor());
				stmt.setInt(parameterIndex++, merito.getCodNum());
				stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
				stmt.executeUpdate();
			}
	}
	
	/**
	 * Listado de méritos de un usuario .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param usuario id del usuario .
	 * @return listado de titulaciones .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe titulación .
	 */
	public BolsaEmpleoDataTable<Merito> listaMeritosDatatable(Map<String, String[]> params, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		
		if (usuario == null) {
			throw new UVException("No se pueden listar méritos sin el id del usuario");
		}
		
		List<Merito> meritos = new ArrayList<>();
		BolsaEmpleoDataTable<Merito> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepmer.*, bepblo.BEPAPA_CODNUM FROM tbep_meritos bepmer"
				+ " INNER JOIN tbep_itemsbaremacion bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN tbep_bloquesbaremacion bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " WHERE bepmer.BEPUSU_CODNUM = ? ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepmer.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUE, "bepblo.BEPAPA_CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEM, "bepmer.BEPITE_CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_ITEM, "bepite.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepmer.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_VALOR, "bepmer.VALOR");
		dataTable.setColumn(ORDER_COLUMN_INDEX_OBSERVACION, "bepmer.OBSERVACION");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, usuario.getCodNum());
			stmtCount.setInt(indexParam++, usuario.getCodNum());
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {					
					Merito mer = this.createMeritoFromResultset(rs, false, false); 
					meritos.add(mer);
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(meritos);
		}
		
		return dataTable;
	}
	
	/** obtiene un mérito a partir de su id.
	 * @param codNum id del mérito .
	 * @return archivo con el id especificado .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public Merito getMeritoById(Integer codNum) throws SQLException, UVException {
		String consulta = "SELECT bepmer.* FROM TBEP_MERITOS bepmer WHERE bepmer.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el merito con id " + codNum);
				}
				
				return this.createMeritoFromResultset(rs, true, false);
			}
		}
	}
	
	/**
	 * Devuelve el merito por su id o excepcion si no existe.
	 * @param id .
	 * @param withUsuario indica si cargar en memoria el usuario asociado al merito o no.
	 * @param withFichero indica si cargar en memoria el fichero del mérito o no .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Merito getMeritoById(Integer id, Boolean withUsuario, Boolean withFichero) throws UVException, SQLException {
		if (id == null) {
			throw new UVException("El mérito es requerido");
		}
		
		String consulta = "SELECT bepmer.* "
				+ "FROM TBEP_MERITOS bepmer "
				+ "WHERE 1=1 "
				+ "AND bepmer.CODNUM = ? ";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, id);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el merito");
				}
				
				return this.createMeritoFromResultset(rs, withUsuario, withFichero);						
			}
		}
	}
	
	/**
	 * Crea un mérito a partir de un resultset.
	 * @param rs .
	 * @param withUsuario .
	 * @param withFile .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Merito createMeritoFromResultset(ResultSet rs, Boolean withUsuario, Boolean withFile) throws SQLException, UVException {
		ModeloBaremacionItems modeloBar = ModeloBaremacionItems.obtenerInstancia();
		
		Merito mer = new Merito();		
		mer.setCodNum(rs.getInt("CODNUM"));
		mer.setItemBaremacion(modeloBar.getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));		
		mer.setValor(rs.getFloat("VALOR"));
		mer.setDescripcion(rs.getString("DESCRIPCION"));
		mer.setObservacion(rs.getString("OBSERVACION"));
		
		if (Boolean.TRUE.equals(withUsuario)) {
			mer.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
		}
		
		if (Boolean.TRUE.equals(withFile)) {
			mer.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
		}
		
		return mer;
	}
}
