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
	
    protected static ModeloMerito eInstancia = null;
	
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
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
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
	
	/** obtiene un mérito a partir de su id.
	 * @param codNum id del mérito .
	 * @return archivo con el id especificado .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public Merito getMeritoById(Integer codNum) throws SQLException, UVException {
		
		String consulta = "SELECT bepmer.* FROM TBEP_MERITOS bepmer WHERE bepmer.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el merito con id " + codNum);
				}
				
				Merito mer = new Merito();
				mer.setCodNum(rs.getInt("CODNUM"));
				ModeloUsuarioBolsaEmpleo modelo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
				mer.setUsuario(modelo.getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
				ModeloBaremacion modeloBar = ModeloBaremacion.obtenerInstancia();
				mer.setItemBaremacion(modeloBar.getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));
				mer.setDescripcion(rs.getString("DESCRIPCION"));
				mer.setObservacion(rs.getString("OBSERVACION"));
				mer.setValor(rs.getFloat("VALOR"));
				
				return mer;
			}
		}
	}
	
	/**	Función que elimina méritos .
	 * @param meritos a eliminar .
	 * @throws SQLException en caso de error en la BD .
	 */
	public void eliminarMeritos(List<String> meritos) throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(meritos.size());
		String consulta = "DELETE FROM tbep_meritos WHERE CODNUM IN (" + params + ")";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			for (String merito: meritos) {
				stmt.setString(indexParam++, merito);	
			}
			stmt.executeUpdate();
		}
	}
	
	/**	Función que inserta un mérito .
	 * @param merito a insertar .
	 * @param usuarioId id del usuario .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public void insertaMerito(Merito merito, Integer usuarioId) throws SQLException, UVException {
		if (merito == null) {
			throw new UVException("No se puede insertar un mérito vacío");
		}
		if (merito.getDescripcion() == null || merito.getDescripcion().equals("")) {
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
		if (usuarioId == null) {
			throw new UVException("No se puede insertar un mérito sin usuario");
		}
		
		String consulta = "INSERT INTO tbep_meritos " 
				+ " (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO) "
				+ "VALUES (?,?,?,?,?,?)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, merito.getItemBaremacion().getCodNum());
			stmt.setInt(parameterIndex++, usuarioId);
			stmt.setFloat(parameterIndex++, merito.getValor());
			stmt.setString(parameterIndex++, merito.getDescripcion());
			stmt.setString(parameterIndex++, merito.getObservacion());
			stmt.setBinaryStream(parameterIndex++, merito.getArchivo());
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
	public BolsaEmpleoDataTable<Merito> listaMeritosDatatable(Map<String, String[]> params, Integer usuario) throws SQLException, UVException {
		
		if (usuario == null) {
			throw new UVException("No se pueden listar méritos sin el id del usuario");
		}
		
		List<Merito> meritos = new ArrayList<>();
		BolsaEmpleoDataTable<Merito> dataTable = new BolsaEmpleoDataTable<Merito>(params);
		
		String consulta = "SELECT bepmer.*, bepblo.BEPAPA_CODNUM FROM tbep_meritos bepmer"
				+ " INNER JOIN tbep_itemsbaremacion bepite"
				+ " ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN tbep_bloquesbaremacion bepblo"
				+ " ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
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
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, usuario);
			stmtCount.setInt(indexParam++, usuario);
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
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
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
		ModeloBaremacion modeloBar = ModeloBaremacion.obtenerInstancia();
		
		Merito mer = new Merito();		
		mer.setCodNum(rs.getInt("CODNUM"));
		mer.setItemBaremacion(modeloBar.getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));		
		mer.setValor(rs.getFloat("VALOR"));
		mer.setDescripcion(rs.getString("DESCRIPCION"));
		mer.setObservacion(rs.getString("OBSERVACION"));
		
		if (withUsuario) {
			mer.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
		}
		
		if (withFile) {
			mer.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
		}
		
		return mer;
	}
}
