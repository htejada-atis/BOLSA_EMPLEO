package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
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
	
	public static final String MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA = "La convocatoria no está abierta";
	public static final String MENSAJE_ERROR_SOLICITUDES_ABIERTAS = "Ya existen solicitides abiertas";
	public static final String MENSAJE_ERROR_SOLICITUDE_NO_EXISTE = "No existe la solicitud";
	
		
    protected static ModeloSolicitud eInstancia = null;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloSolicitud();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloSolicitud obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }
	
	
	/**
	 * Listado de solicitudes. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<Solicitud> listaSolicitudesDatatable(Map<String, String[]> params) throws SQLException, UVException {
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia(); 
		List<Solicitud> data = new ArrayList<>();
		BolsaEmpleoDataTable<Solicitud> dataTable = new BolsaEmpleoDataTable<Solicitud>(params);
		
		String consulta =
			"SELECT bepcon.CODNUM, bepcon.DESCRIPCION, bepcon.FECHACIERRE, bepcon.ESTADO ESTADO_CONVOCATORIA, "
		  + "       bepsol.CODNUM SOLICITUD_CODNUM, bepsol.ESTADO ESTADO_SOLICITUD "
		  + "FROM TBEP_CONVOCATORIAS bepcon "
		  + "LEFT JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPCON_CODNUM = bepcon.CODNUM "
		  + "ORDER BY bepcon.CODNUM desc";
		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			//stmtCount.setString(1, ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA);
			//stmt.setString(1, ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA);
			
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
	 * @param idConvocatoria .
	 * @return solicitud creada	 
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Solicitud nuevaSolicitud(Integer idConvocatoria) throws SQLException, UVException {
		ModeloConvocatoria modeloConvocatoria = new ModeloConvocatoria();
		Convocatoria convocatoria = modeloConvocatoria.getConvocatoriaById(idConvocatoria);
		
		if (!convocatoria.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA)) {
			throw new UVException(MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA);
		}
		
		if (this.haySolicitudAbiertaParaConvocatoria(convocatoria)) {
			throw new UVException(MENSAJE_ERROR_SOLICITUDES_ABIERTAS);
		}
				
		String consulta =
			"INSERT INTO TBEP_SOLICITUDES (BEPCON_CODNUM, ESTADO) " 
			+ "VALUES (?, ?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
		
			try (PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{"CODNUM"})) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, convocatoria.getCodNum());
				stmt.setString(parameterIndex++, ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA);
				stmt.executeUpdate();
				
				ResultSet rs = stmt.getGeneratedKeys();
				rs.next();
				Integer idSolucitud = rs.getInt(1);

				conexion.commit();
				
				return this.getSolicitudById(idSolucitud); 
			} catch (SQLException | UVException e) {
				if (conexion != null) { 
					conexion.rollback();
				}
				throw e;
			}
		}
	}
	
	/**
	 * Devuelve una solicitud por su pk.
	 * @param codNum .
	 * @return Solicitud o null si no existe
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Solicitud getSolicitudById(int codNum) throws SQLException, UVException {
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia();
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
