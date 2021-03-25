package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de titulaciones. 
 * @author ATISoluciones
 */
public class ModeloTitulacion {
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 2;
	public static final int ORDER_COLUMN_INDEX_REQUERIDA = 3;
	
	/**
	 * Listado de titulaciones en una tabla .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param area id del area por el que se va a filtrar
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public DataTable<Titulacion> listaTitulacionesDatatable(Map<String, String[]> params, Integer area) throws SQLException, UVException {
		List<Titulacion> titulaciones = new ArrayList<>();
		DataTable<Titulacion> dataTable = new DataTable<Titulacion>(params);
		
		String consultaNotIn = "SELECT beptit.CODNUM FROM tbep_titulaciones beptit "
				+ "INNER JOIN tbep_titulacionesarea beptiar ON beptit.codnum = beptiar.beptit_codnum "
				+ "INNER JOIN TBEP_AREAS bepare ON bepare.codnum = beptiar.bepare_codnum "
				+ "WHERE bepare.CODNUM = " + area;
		String consulta = "SELECT beptit.* FROM tbep_titulaciones beptit WHERE beptit.CODNUM NOT IN (" + consultaNotIn + ")";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "beptit.CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NOMBRE, "beptit.NOMBRE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Titulacion tit = new Titulacion();
					tit.setCodNum(rs.getInt("CODNUM"));
					tit.setNombre(rs.getString("NOMBRE"));
					titulaciones.add(tit);
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(titulaciones);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado de titulaciones de un area en una tabla .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param area id del area por el que se va a filtrar
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public DataTable<Titulacion> listaTitulacionesAreaDatatable(Map<String, String[]> params, Integer area) throws SQLException, UVException {
		List<Titulacion> titulaciones = new ArrayList<>();
		DataTable<Titulacion> dataTable = new DataTable<Titulacion>(params);
		
		String consulta = "SELECT beptit.CODNUM, beptit.NOMBRE beptiar.FLGREQUERIDA, FROM tbep_titulaciones beptit "
				+ "INNER JOIN tbep_titulacionesarea beptiar ON beptit.codnum = beptiar.beptit_codnum "
				+ "INNER JOIN TBEP_AREAS bepare ON bepare.codnum = beptiar.bepare_codnum "
				+ "WHERE bepare.CODNUM = " + area;
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "beptit.CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NOMBRE, "beptit.NOMBRE");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_REQUERIDA, "beptiar.FLGREQUERIDA");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Titulacion tit = new Titulacion();
					tit.setCodNum(rs.getInt("CODNUM"));
					tit.setNombre(rs.getString("NOMBRE"));
					tit.setRequerida(rs.getString("FLGREQUERIDA").equals("S"));
					titulaciones.add(tit);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(titulaciones);
		}
		
		return dataTable;
	}
}
