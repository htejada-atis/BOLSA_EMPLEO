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
import es.ujaen.uvirtual.utilidades.DataTable;
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
	
	public static final String APARTADO_ACTIVO = "S";
	public static final String APARTADO_NO_ACTIVO = "N";
		
	/**
	 * Listado de apartados generales de baremación. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado 
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public DataTable<ApartadoBaremacion> listadoApartadosGeneralesBaremacionDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<ApartadoBaremacion> apartados = new ArrayList<>();
		DataTable<ApartadoBaremacion> dataTable = new DataTable<ApartadoBaremacion>(params);
		
		String consulta =
			"SELECT bepapa.* "
		  + "FROM TBEP_APARTADOSBAREMACION bepapa "		  
		  + "WHERE 1=1 ";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_APARTADOS_CODIGO, "bepapa.CODIGO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_APARTADOS_NOMBRE, "bepapa.NOMBRE");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_APARTADOS_ACTIVO, "bepapa.FLGACTIVO");
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
	
	/**
	 * Listado de bloques de baremación . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param apartado id del apartado .
	 * @return listado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public DataTable<BloqueBaremacion> listadoBloquesBaremacionDatatable(Map<String, String[]> params, Integer apartado) throws SQLException, UVException {
		
		if (apartado == null) {
			throw new UVException("No se pueden listar bloques sin el id del apartado");
		}
		
		List<BloqueBaremacion> bloques = new ArrayList<>();
		DataTable<BloqueBaremacion> dataTable = new DataTable<BloqueBaremacion>(params);
		
		String consulta = "SELECT bepblo.* FROM TBEP_BLOQUESBAREMACION bepblo"
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa"
				+ " ON bepapa.CODNUM = bepblo.CODNUM"
				+ " WHERE bepblo.BEPAPA_CODNUM = ? ";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_BLOQUES_CODIGO, "bepblo.CODIGO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_BLOQUES_NOMBRE, "bepblo.NOMBRE");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_BLOQUES_ACTIVO, "bepblo.FLGACTIVO");
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
	
	/**
	 * Listado de ítems de baremación . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param bloque id del bloque .
	 * @return listado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public DataTable<ItemBaremacion> listadoItemsBaremacionDatatable(Map<String, String[]> params, Integer bloque) throws SQLException, UVException {
		
		if (bloque == null) {
			throw new UVException("No se pueden listar ítems sin el id del bloque");
		}
		
		List<ItemBaremacion> items = new ArrayList<>();
		DataTable<ItemBaremacion> dataTable = new DataTable<ItemBaremacion>(params);
		
		String consulta = "SELECT bepite.* FROM TBEP_ITEMSBAREMACION bepite"
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo"
				+ " ON bepblo.CODNUM = bepite.CODNUM"
				+ " WHERE bepite.BEPBLO_CODNUM = ? ";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ITEMS_CODIGO, "bepite.CODIGO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ITEMS_NOMBRE, "bepite.NOMBRE");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ITEMS_ACTIVO, "bepite.FLGACTIVO");
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
	
	/**
	 * Devuelve un apartado de baremación por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si bolsa no es existe
	 */
	public ApartadoBaremacion getApartadoBaremacionById(int codNum) throws SQLException, UVException {
		String consulta = "SELECT bepapa.* FROM TBEP_APARTADOSBAREMACION bepapa WHERE bepapa.CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el apartado con id " + codNum);
				}
				
				ApartadoBaremacion apartado = new ApartadoBaremacion();
				apartado.setCodNum(rs.getInt("CODNUM"));
				apartado.setCodigo(rs.getString("CODIGO"));
				apartado.setNombre(rs.getString("NOMBRE"));
				apartado.setActivo(rs.getString("FLGACTIVO").equals("S"));					
								
				return apartado;
			}
		}
	}
	
	/**
	 * Devuelve un bloque de baremación por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si bolsa no es existe
	 */
	public BloqueBaremacion getBloqueBaremacionById(int codNum) throws SQLException, UVException {
		String consulta = "SELECT bepblo.* FROM TBEP_BLOQUESBAREMACION bepblo WHERE bepblo.CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el bloque con id " + codNum);
				}
				
				BloqueBaremacion bloque = new BloqueBaremacion();
				bloque.setCodNum(rs.getInt("CODNUM"));
				bloque.setCodigo(rs.getString("CODIGO"));
				bloque.setNombre(rs.getString("NOMBRE"));
				bloque.setActivo(rs.getString("FLGACTIVO").equals("S"));					
				bloque.setApartadoBaremacion(this.getApartadoBaremacionById(rs.getInt("BEPAPA_CODNUM")));
				return bloque;
			}
		}
	}
	
}
