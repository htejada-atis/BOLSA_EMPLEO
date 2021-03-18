package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de dotencia 
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * 
 * @author acr00058
 *
 */
public class ModeloBolsa {	
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_AREA = 2;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 3;
	public static final int ORDER_COLUMN_INDEX_ACTUALIZADA = 4;
	public static final int ORDER_COLUMN_INDEX_BLOQUEO = 5;
	public static final int ORDER_COLUMN_INDEX_DESBLOQUEO = 6;
	public static final int ORDER_COLUMN_INDEX_BAREMALE = 7;
	
	/**
	 * Listado de bolsas de empleo. 
	 * @param request para leer los parametros de paginación, ordenacion, etc
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public DataTable<Bolsa> listaBolsaEmpleo(HttpServletRequest request) throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		ModeloArea modeloArea = new ModeloArea();
		DataTable<Bolsa> dataTable = new DataTable<Bolsa>(request.getParameterMap());
		
		String consulta =
			"SELECT bepbol.* "
		  + "FROM TBEP_BOLSAS bepbol "
		  + "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
		  + "WHERE 1=1 ";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "bepbol.CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ESTADO, "bepbol.ESTADO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ACTUALIZADA, "bepbol.FECHAACTUALIZACION");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_BLOQUEO, "bepbol.FECHABLOQUEO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_DESBLOQUEO, "bepbol.FECHADEBLOQUEO");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_BAREMALE, "bepbol.FLGBAREMABLE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = new Bolsa();
					bolsa.setCodNum(rs.getInt("CODNUM"));
					bolsa.setArea(modeloArea.getAreaById(rs.getInt("BEPARE_CODNUM")));
					bolsa.setEstado(rs.getString("ESTADO"));
					bolsa.setBaremable(rs.getString("FLGBAREMABLE").equals("S"));					
					bolsa.setFechaActualizacion(rs.getTimestamp("FECHAACTUALIZACION"));
					bolsa.setFechaBloqueo(rs.getTimestamp("FECHABLOQUEO"));
					bolsa.setFechaDesBloqueo(rs.getTimestamp("FECHADEBLOQUEO"));
					
					bolsas.add(bolsa);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bolsas);
		}
		
		return dataTable;
	}	
}
