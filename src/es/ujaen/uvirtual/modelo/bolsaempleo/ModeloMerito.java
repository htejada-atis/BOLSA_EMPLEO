package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de titulaciones. 
 * @author ATISoluciones
 */
public class ModeloMerito {
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_APARTADO = 2;
	public static final int ORDER_COLUMN_INDEX_ITEM = 3;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 4;
	public static final int ORDER_COLUMN_INDEX_VALOR = 5;
	public static final int ORDER_COLUMN_INDEX_OBSERVACION = 6;
	
	/** Consulta méritos en BBDD y los devuelve .
	 * @param id para devolver un mérito .
	 * @return lista de los méritos de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 */
	private List<Merito> listaMeritos(Integer id) throws SQLException {
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
					try {
						Merito mer = new Merito();
						mer.setCodNum(rs.getInt("CODNUM"));
						mer.setItemBaremacion(new ModeloBaremacion().getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));
						mer.setDescripcion(rs.getString("DESCRIPCION"));
						mer.setObservacion(rs.getString("OBSERVACION"));
						mer.setValor(rs.getFloat("VALOR"));
						mer.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
						meritos.add(mer);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			}
		return meritos;
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
	public DataTable<Merito> listaMeritosDatatable(Map<String, String[]> params, Integer usuario) throws SQLException, UVException {
		
		if (usuario == null) {
			throw new UVException("No se pueden listar méritos sin el id del usuario");
		}
		
		List<Merito> meritos = new ArrayList<>();
		DataTable<Merito> dataTable = new DataTable<Merito>(params);
		
		String consulta = "SELECT bepmer.*, bepblo.BEPAPA_CODNUM FROM tbep_meritos bepmer"
				+ " INNER JOIN tbep_itemsbaremacion bepite"
				+ " ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN tbep_bloquesbaremacion bepblo"
				+ " ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " WHERE bepmer.BEPUSU_CODNUM = ? ";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "bepmer.CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_APARTADO, "bepblo.BEPAPA_CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ITEM, "bepmer.BEPITE_CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepmer.DESCRIPCION");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_VALOR, "bepmer.VALOR");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_OBSERVACION, "bepmer.OBSERVACION");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, usuario);
			stmtCount.setInt(indexParam++, usuario);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Merito mer = new Merito();
					mer.setCodNum(rs.getInt("CODNUM"));
					mer.setItemBaremacion(new ModeloBaremacion().getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));
					mer.setDescripcion(rs.getString("DESCRIPCION"));
					mer.setObservacion(rs.getString("OBSERVACION"));
					mer.setValor(rs.getFloat("VALOR"));
					meritos.add(mer);
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(meritos);
		}
		
		return dataTable;
	}
	
}
