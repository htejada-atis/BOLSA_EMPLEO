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
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
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
		  + "FROM TBEP_APARTADOBAREMACION bepapa "		  
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
	 * Listado de bloques de baremación. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado 
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public DataTable<BloqueBaremacion> listadoBloquesBaremacionDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<BloqueBaremacion> bloques = new ArrayList<>();
		DataTable<BloqueBaremacion> dataTable = new DataTable<BloqueBaremacion>(params);
		
		String consulta = "SELECT bepblo.* FROM TBEP_BLOQUESBAREMACION bepblo WHERE 1=1";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_BLOQUES_CODIGO, "bepblo.CODIGO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_BLOQUES_NOMBRE, "bepblo.NOMBRE");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_BLOQUES_ACTIVO, "bepblo.FLGACTIVO");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					BloqueBaremacion bloque = new BloqueBaremacion();
					bloque.setCodNum(rs.getInt("CODNUM"));
					bloque.setCodigo(rs.getString("CODIGO"));
					bloque.setNombre(rs.getString("NOMBRE"));
					bloque.setActivo(rs.getString("FLGACTIVO").equals("S"));
					bloque.setApartadoBaremacion(this.getApartadoBaremacionById(rs.getInt("BEPBOL_CODNUM")));
					bloques.add(bloque);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bloques);
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
					throw new UVException("No existe la bolsa con id " + codNum);
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
	
}
