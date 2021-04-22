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

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
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
	 * @param usuario .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<Solicitud> listaSolicitudesDatatable(UsuarioBolsaEmpleo usuario, Map<String, String[]> params) throws SQLException, UVException {
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia(); 
		List<Solicitud> data = new ArrayList<>();
		BolsaEmpleoDataTable<Solicitud> dataTable = new BolsaEmpleoDataTable<Solicitud>(params);
		
		String consulta =
			"SELECT bepcon.CODNUM, bepcon.DESCRIPCION, bepcon.FECHACIERRE, bepcon.ESTADO ESTADO_CONVOCATORIA, "
		  + "       bepsol.CODNUM SOLICITUD_CODNUM, bepsol.ESTADO ESTADO_SOLICITUD "
		  + "FROM TBEP_CONVOCATORIAS bepcon "
		  + "LEFT JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPCON_CODNUM = bepcon.CODNUM "
		  + "WHERE (bepsol.BEPUSU_CODNUM IS NULL OR bepsol.BEPUSU_CODNUM = ?) "
		  + "ORDER BY bepcon.CODNUM desc";
		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {				
			stmt.setInt(1, usuario.getCodNum());
			stmtCount.setInt(1, usuario.getCodNum());
			
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
	 * @param usuario .
	 * @param convocatoria .
	 * @return .
	 * @throws SQLException .
	 */
	public boolean haySolicitudAbiertaParaConvocatoria(UsuarioBolsaEmpleo usuario, Convocatoria convocatoria) throws SQLException {
		String consulta =
				"SELECT COUNT(1) numero_solicitudes_abiertas "
			  + "FROM TBEP_SOLICITUDES bepsol "		  
			  + "WHERE bepsol.ESTADO = ? "
			  + "AND bepsol.BEPCON_CODNUM = ? "
			  + "AND bepsol.BEPUSU_CODNUM = ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, SOLICITUD_ESTADO_ABIERTA);
			stmt.setInt(paramIndex++, convocatoria.getCodNum());			
			stmt.setInt(paramIndex++, usuario.getCodNum());
			
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
	 * @param usuario .
	 * @param convocatoria .
	 * @return solicitud creada	 
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Solicitud nuevaSolicitud(UsuarioBolsaEmpleo usuario, Convocatoria convocatoria) throws SQLException, UVException {
		if (!convocatoria.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA)) {
			throw new UVException(MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA);
		}
		
		if (this.haySolicitudAbiertaParaConvocatoria(usuario, convocatoria)) {
			throw new UVException(MENSAJE_ERROR_SOLICITUDES_ABIERTAS);
		}
				
		String consulta =
			"INSERT INTO TBEP_SOLICITUDES (BEPUSU_CODNUM, BEPCON_CODNUM, ESTADO) " 
			+ "VALUES (?, ?, ?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
		
			try (PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{"CODNUM"})) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, usuario.getCodNum());
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
					throw new UVException("No existe la solicitud");
				}
				
				Solicitud solicitud = new Solicitud();
				solicitud.setCodNum(rs.getInt("CODNUM"));
				solicitud.setConvocatoria(modeloConvocatoria.getConvocatoriaById(rs.getInt("BEPCON_CODNUM")));
				solicitud.setEstado(rs.getString("ESTADO"));
				
				return solicitud;
			}
		}
	}

	/**
	 * El usuario selecciona las bolsas para su solicitud.
	 * @param bolsas .
	 * @param solicitud .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void asignarBolsasASolicitud(Solicitud solicitud, List<Bolsa> bolsas) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size());
		
		// leemos las bolsas que están asignadas y ya dejan de estarlo. para ver si tiene meritos asigando o no
		// TODO: refactor la asignación de bolsas a la solicitud		
//		String consulta = "SELECT bepsbo.* "
//				+ "FROM TBEP_SOLICITUDBOLSAS "
//				+ "WHERE 1=1 "
//				+ "AND bepsbo.BEPSOL_CODNUM = ? "
//				+ "AND bepsbo.BEPBOL_CODNUM NOT IN (" + params + ")";
//		
//		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
//			int indexParam = 1;
//			stmt.setInt(indexParam++, solicitud.getCodNum());
//			for (Bolsa bolsa: bolsas) {
//				stmt.setInt(indexParam++, bolsa.getCodNum());		
//			}
//			
//			try (ResultSet rs = stmt.executeQuery()) {
//				while (rs.next()) {
//					Bolsa bolsaQueSale = modeloBolsa.getBolsaById(rs.getInt("BEPBOL_CODNUM"));
//								
//				}				
//			}
//			
//			stmt.executeUpdate();
//		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();) {
			conexion.setAutoCommit(false);
			
			// eliminamos las bolsas anteriores
			String sqlDelete = "DELETE FROM TBEP_SOLICITUDBOLSAS WHERE BEPSOL_CODNUM = ?";
			
			try (PreparedStatement stmt = conexion.prepareStatement(sqlDelete)) {
				stmt.setInt(1, solicitud.getCodNum());
				stmt.executeUpdate();
			} catch (SQLException e) {
				if (conexion != null) { 
					conexion.rollback();
				}
				throw e;
			}
			
			// insertamos la bolsas en la solicidad		
			String consultaInsert = "INSERT INTO TBEP_SOLICITUDBOLSAS (BEPBOL_CODNUM, BEPSOL_CODNUM)"
					+ " SELECT bepbol.CODNUM AS BEPBOL_CODNUM, bepsol.CODNUM AS BEPSOL_CODNUM"
					+ " FROM TBEP_BOLSAS bepbol, TBEP_SOLICITUDES bepsol WHERE bepsol.CODNUM = ? AND "
					+ " bepbol.CODNUM IN (" + params + ")";
			
			try (PreparedStatement stmt = conexion.prepareStatement(consultaInsert)) {
				int indexParam = 1;
				stmt.setInt(indexParam++, solicitud.getCodNum());
				for (Bolsa bolsa: bolsas) {
					stmt.setInt(indexParam++, bolsa.getCodNum());		
				}
				stmt.executeUpdate();
			} catch (SQLException e) {
				if (conexion != null) { 
					conexion.rollback();
				}
				throw e;
			}
			
			conexion.commit();
		}		
	}

	/**
	 * Devuleve las bolsas de una solicitud .
	 * @param solicitud .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException  .
	 */
	public List<Bolsa> getBolsasSolicitud(Solicitud solicitud) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ArrayList<Bolsa> bolsas = new ArrayList<Bolsa>();
		
		String consulta = "SELECT bepsbo.* FROM TBEP_SOLICITUDBOLSAS WHERE bepsbo.BEPSOL_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(modeloBolsa.getBolsaById(rs.getInt("BEPBOL_CODNUM")));
				}				
			}
		}
		
		return bolsas;
	}

	/**
	 * Devuelve las bolsas de una solicitud.
	 * @param solicitud .
	 * @param params .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public BolsaEmpleoDataTable<Bolsa> listaBolsasSolicitudesDatatable(Solicitud solicitud, Map<String, String[]> params) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		List<Bolsa> data = new ArrayList<>();
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<Bolsa>(params);
		
		String consulta =
			"SELECT bepsbo.* "
		  + "FROM TBEP_SOLICITUDBOLSAS bepsbo "
		  + "WHERE bepsbo.BEPSOL_CODNUM = ? ";
		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {				
			stmt.setInt(1, solicitud.getCodNum());
			stmtCount.setInt(1, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = modeloBolsa.getBolsaById(rs.getInt("BEPBOL_CODNUM"));
					data.add(bolsa);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}
		
		return dataTable;
	}
}
