package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.UVException;
import oracle.jdbc.OraclePreparedStatement;

/**
 * Clase de modelo para la gestión de las solicitudes 
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloSolicitud {	
	public static final String SOLICITUD_ESTADO_ABIERTA = "ABIERTA";
	public static final String SOLICITUD_ESTADO_CERRADA = "CERRADA";
		
	/**
	 * Listado de solicitudes. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public DataTable<Solicitud> listaSolicitudesDatatable(Map<String, String[]> params) throws SQLException, UVException {
		ModeloConvocatoria modeloConvocatoria = new ModeloConvocatoria(); 
		List<Solicitud> data = new ArrayList<>();
		DataTable<Solicitud> dataTable = new DataTable<Solicitud>(params);
		
		String consulta =
			"SELECT bepcon.CODNUM, bepcon.DESCRIPCION, bepcon.FECHACIERRE, bepcon.ESTADO ESTADO_CONVOCATORIA, "
		  + "       bepsol.CODNUM SOLICITUD_CODNUM, bepsol.ESTADO ESTADO_SOLICITUD "
		  + "FROM TBEP_CONVOCATORIAS bepcon "
		  + "LEFT JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPCON_CODNUM = bepcon.CODNUM "
		  + "WHERE bepcon.ESTADO = ? ";
		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			stmtCount.setString(1, ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA);
			stmt.setString(1, ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Convocatoria convocatoria = modeloConvocatoria.getConvocatoriaById(rs.getInt("CODNUM"));
					if (convocatoria == null) {
						throw new UVException("No existe la convocatoria con id " + rs.getInt("CODNUM"));
					}
					
					Solicitud solicitud = new Solicitud();
					solicitud.setCodNum(rs.getInt("SOLICITUD_CODNUM"));					
					solicitud.setConvocatoria(convocatoria);					
					solicitud.setEstado(rs.getString("ESTADO_SOLICITUD") != null ? rs.getString("ESTADO_SOLICITUD") : ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
					data.add(solicitud);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}
		
		return dataTable;
	}	
	
	/**
	 * Comprueba si hay solicitudes abiertas para una convocatoria.
	 * @param convocatoria .
	 * @return .
	 * @throws SQLException .
	 */
	public boolean haySolicitudAbiertaParaConvocatoria(Convocatoria convocatoria) throws SQLException {
		String consulta =
				"SELECT COUNT(1) numero_solicitudes_abiertas "
			  + "FROM TBEP_SOLICITUDES bepsol "		  
			  + "WHERE bepsol.ESTADO = ? AND bepsol.BEPCON_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setString(1, SOLICITUD_ESTADO_ABIERTA);
			stmt.setInt(2, convocatoria.getCodNum());			
    		try (ResultSet rs = stmt.executeQuery();) {
	    		if (rs.next()) {
	    			if (rs.getInt("numero_solicitudes_abiertas") > 0) {
	    				return true;
	    			}
	    		}
    		}
		}
		
		return false;
	}
	
	/**
	 * Crea una nueva solicitud para una convocatoria.
	 * @param convocatoria .
	 * @return solicitud creada	 
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Solicitud nuevaSolicitud(Convocatoria convocatoria) throws SQLException, UVException {
		String consulta =
			"INSERT INTO TBEP_SOLICITUDES (BEPCON_CODNUM, ESTADO) " 
			+ "VALUES (?, ?) "
			+ "RETURNING CODNUM INTO ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				OraclePreparedStatement stmt = (OraclePreparedStatement) conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, convocatoria.getCodNum());
			stmt.setString(parameterIndex++, ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA);
			stmt.registerReturnParameter(parameterIndex++, Types.INTEGER);
			stmt.executeUpdate();
			
			try (ResultSet rs = stmt.getReturnResultSet();) {
				if (rs.last()) {
					Integer idSolicitud = rs.getInt(1);
					return this.getSolicitudById(idSolicitud);
				}
			}			
		}
		
		return null;		
	}
	
	/**
	 * Devuelve una solicitud por su pk.
	 * @param codNum .
	 * @return Solicitud o null si no existe
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Solicitud getSolicitudById(int codNum) throws SQLException, UVException {
		ModeloConvocatoria modeloConvocatoria = new ModeloConvocatoria();
		String consulta = "SELECT bepsol.* FROM TBEP_SOLICITUDES bepsol WHERE bepsol.CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				
				Solicitud solicitud = new Solicitud();
				solicitud.setCodNum(rs.getInt("CODNUM"));
				solicitud.setConvocatoria(modeloConvocatoria.getConvocatoriaById(rs.getInt("BEPCON_CODNUM")));
				solicitud.setEstado(rs.getString("ESTADO"));
				
				return solicitud;
			}
		}
	}
}
