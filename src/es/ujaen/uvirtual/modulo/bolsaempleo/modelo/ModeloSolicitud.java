package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TablaBolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;

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
	 * @param archivo booleano que nos indica si incluir el archivo al obtener la solicitud o no .
	 * @return Solicitud o null si no existe
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	private Solicitud getSolicitudById(Integer codNum, boolean archivo) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("La solicitud es requerida");
		}
			
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
				solicitud.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
				solicitud.setFechaConfirmacion(rs.getDate("FECHACONFIRMACION"));
				
				if (archivo) {
					solicitud.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
				}
				
				return solicitud;
			}
		}
	}
	
	/**
	 * Devuelve una solicitud por su id.
	 * @param codNum .
	 * @return Solicitud o null si no existe .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Solicitud getSolicitudById(Integer codNum) throws SQLException, UVException {
		return getSolicitudById(codNum, false);
	}
	
	/**
	 * Devuelve una solicitud por su id.
	 * @param codNum .
	 * @return Solicitud o null si no existe .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Solicitud getSolicitudByIdArchivo(Integer codNum) throws SQLException, UVException {
		return getSolicitudById(codNum, true);
	}
	
	/**
	 * El usuario selecciona las bolsas para su solicitud.
	 * @param bolsas .
	 * @param solicitud .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void asignarBolsasASolicitud(Solicitud solicitud, List<Bolsa> bolsas) throws SQLException, UVException {
		if (solicitud.getEstado().equals(SOLICITUD_ESTADO_CERRADA)) {
			throw new UVException("La solicitud está cerrada");
		}
		
		ArrayList<Bolsa> bolsasExcluidas = listaBolsasSolicitudExcluidas(solicitud, bolsas);
		ArrayList<Bolsa> bolsasAgregadas = listaBolsasSolicitudAgregadas(solicitud, bolsas);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();) {
			conexion.setAutoCommit(false);
			
			try {				
				// eliminamos las bolsas excluidas de la solicitud
				if (bolsasExcluidas.size() > 0) {
					this.eliminarBolsasExcluidasDeLaSolicitud(conexion, solicitud, bolsasExcluidas);
				}
				
				// insertamos la bolsas agregadas a la solicitud
				if (bolsasAgregadas.size() > 0) {
					String paramsAgregadas = BolsaEmpleoUtils.consultaMultiplesParametros(bolsasAgregadas.size());
										
					String consultaInsert = "INSERT INTO TBEP_SOLICITUD_BOLSAS (BEPBOL_CODNUM, BEPSOL_CODNUM)"
							+ " SELECT bepbol.CODNUM AS BEPBOL_CODNUM, bepsol.CODNUM AS BEPSOL_CODNUM"
							+ " FROM TBEP_BOLSAS bepbol, TBEP_SOLICITUDES bepsol WHERE bepsol.CODNUM = ? AND "
							+ " bepbol.CODNUM IN (" + paramsAgregadas + ")";
					
					try (PreparedStatement stmt = conexion.prepareStatement(consultaInsert)) {
						int indexParam = 1;
						stmt.setInt(indexParam++, solicitud.getCodNum());
						for (Bolsa bolsa: bolsasAgregadas) {
							stmt.setInt(indexParam++, bolsa.getCodNum());
						}
						stmt.executeUpdate();
					}					
				}
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
		
		String consulta = "SELECT bepsbo.* FROM TBEP_SOLICITUD_BOLSAS bepsbo WHERE bepsbo.BEPSOL_CODNUM = ?";
		
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
	 * Devuleve las bolsas de una solicitud con sus méritos añadidos.
	 * @param solicitud .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException  .
	 */
	public List<BolsaSolicitud> getBolsasSolicitudMeritos(Solicitud solicitud) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ArrayList<BolsaSolicitud> bolsas = new ArrayList<BolsaSolicitud>();
		
		String consulta = "SELECT bepsbo.* FROM TBEP_SOLICITUD_BOLSAS bepsbo WHERE bepsbo.BEPSOL_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = modeloBolsa.getBolsaById(rs.getInt("BEPBOL_CODNUM"));
					List<MeritoSolicitud> meritos = this.getMeritosSolicitudBolsa(solicitud, bolsa);
					bolsas.add(new BolsaSolicitud(bolsa, meritos));
				}				
			}
		}
		
		return bolsas;
	}

	/**
	 * Devuelve las bolsas de una solicitud, con su conteo de méritos.
	 * @param solicitud .
	 * @param params .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public BolsaEmpleoDataTable<TablaBolsaSolicitud> listaBolsasSolicitudesDatatable(Solicitud solicitud, Map<String, String[]> params) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		List<TablaBolsaSolicitud> data = new ArrayList<>();
		BolsaEmpleoDataTable<TablaBolsaSolicitud> dataTable = new BolsaEmpleoDataTable<TablaBolsaSolicitud>(params);
		
		String consulta = ""
				+ "SELECT "
				+ "		bepsbo.*, "
				+ "		(SELECT COUNT(*) FROM TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm WHERE bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM) COUNT_MERITOS "
				+ "FROM TBEP_SOLICITUD_BOLSAS bepsbo "
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
					data.add(new TablaBolsaSolicitud(bolsa, rs.getInt("COUNT_MERITOS")));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}
		
		return dataTable;
	}
	
	/** El usuario selecciona un mérito para una bolsa en la solicitud .
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void asignarMeritosASolicitudBolsa(Solicitud solicitud, Bolsa bolsa, Merito merito) throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();) {
			conexion.setAutoCommit(false);
			
			// insertamos el mérito en la solicitud bolsa
			String consulta = "INSERT INTO TBEP_SOLICITUD_BOLSAS_MERITOS (BEPSBO_CODNUM, BEPMER_CODNUM)"
					+ " SELECT bepsbo.CODNUM AS BEPSBO_CODNUM, bepmer.CODNUM AS BEPMER_CODNUM"
					+ " FROM TBEP_SOLICITUD_BOLSAS bepsbo, TBEP_MERITOS bepmer"
					+ " WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ? AND bepmer.CODNUM = ? ";
			
			try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int indexParam = 1;
				stmt.setInt(indexParam++, solicitud.getCodNum());
				stmt.setInt(indexParam++, bolsa.getCodNum());
				stmt.setInt(indexParam++, merito.getCodNum());
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
	
	/** El usuario deselecciona un mérito para una bolsa en la solicitud .
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void borrarMeritoDeSolicitudBolsa(Solicitud solicitud, Bolsa bolsa, Merito merito) throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();) {
			conexion.setAutoCommit(false);
			// eliminamos el mérito de la bolsa en la solicitud
			String consulta = "DELETE FROM TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm "
					+ "	WHERE bepsbm.BEPSBO_CODNUM IN ("
					+ "		SELECT bepsbo.CODNUM FROM TBEP_SOLICITUD_BOLSAS bepsbo"
					+ "		WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ?)"
					+ "	AND bepsbm.BEPMER_CODNUM = ?";
			
			try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int indexParam = 1;
				stmt.setInt(indexParam++, solicitud.getCodNum());
				stmt.setInt(indexParam++, bolsa.getCodNum());
				stmt.setInt(indexParam++, merito.getCodNum());
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
	 * Devuelve un merito solicitud, el de un merito en una solicitud.
	 * @param idMerito .
	 * @param idSolicitudBolsa .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public MeritoSolicitud getMeritoSolicitudById(Integer idMerito, Integer idSolicitudBolsa) throws SQLException, UVException {
		String consulta = "SELECT bepsbm.* "
				+ "FROM TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm "
				+ "WHERE 1=1 "
				+ "AND bepsbm.BEPSBO_CODNUM = ? "
				+ "AND bepsbm.BEPMER_CODNUM = ? ";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, idSolicitudBolsa);
			stmt.setInt(2, idMerito);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el merito de la solicitud");
				}
				
				Merito merito = ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt("BEPMER_CODNUM"), false, false);
				MeritoSolicitud meritoSolicitud = new MeritoSolicitud(rs.getInt("CODNUM"), merito, rs.getInt("BEPSBO_CODNUM"), 
						rs.getString("FLGEXCLUIDO").equals("S"));
								
				return meritoSolicitud;
			}
		}
	}

	/**
	 * Obtiene el total de méritos por bloque que hay en la solicitud .
	 * @param solicitud .
	 * @param merito .
	 * @return total .
	 * @throws SQLException .
	 */
	public Integer obtenerTotalMeritosPorBloqueSolicitud(Solicitud solicitud, Merito merito) throws SQLException {
		Integer total = 0;
		String consulta = "SELECT COUNT(*) AS total FROM TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM"
				+ "	INNER JOIN TBEP_MERITOS bepmer ON bepsbm.BEPMER_CODNUM = bepmer.CODNUM"
				+ "	INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepmer.BEPITE_CODNUM = bepite.CODNUM"
				+ "	INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepite.BEPBLO_CODNUM = bepblo.CODNUM"
				+ "	INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepblo.CODNUM = bepapa.CODNUM"
				+ "	WHERE bepsbo.BEPSOL_CODNUM = ? AND bepapa.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, solicitud.getCodNum());
			stmt.setInt(parameterIndex++, merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodNum());
			stmt.executeUpdate();
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					total = rs.getInt("total");
				}
			}
		}
		return total;
	}
	
	/**
	 * Obtiene el total de méritos que hay en la solicitud .
	 * @param solicitud .
	 * @return total .
	 * @throws SQLException .
	 */
	public Integer obtenerTotalMeritosSolicitud(Solicitud solicitud) throws SQLException {
		Integer total = 0;
		String consulta = "SELECT COUNT(*) AS total FROM TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM"
				+ "	WHERE bepsbo.BEPSOL_CODNUM = ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, solicitud.getCodNum());
			stmt.executeUpdate();
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					total = rs.getInt("total");
				}
			}
		}
		return total;
	}
	
	/**
	 * Actualiza el estado de la solicitud a cerrado .
	 * @param solicitud .
	 * @throws SQLException .
	 */
	public void confirmacionSolicitud(Solicitud solicitud) throws SQLException, UVException {
		if (solicitud == null) {
			throw new UVException("No se puede confirmar una solicitud vacía");
		}
		
		if (solicitud.getCodNum() == null) {
			throw new UVException("No se puede confirmar una solicitud con id vacío");
		}
		
		if (solicitud.getFechaConfirmacion() == null) {
			throw new UVException("No se puede confirmar una solicitud sin fecha confirmacion");
		}
		
		if (solicitud.getEstado() == null || !solicitud.getEstado().equals(SOLICITUD_ESTADO_CERRADA)) {
			throw new UVException("No se puede confirmar una solicitud sin estado cerrado");
		}
		
		if (solicitud.getArchivo() == null) {
			throw new UVException("No se puede confirmar una solicitud sin archivo");
		}
		
		String consulta = "UPDATE TBEP_SOLICITUDES "
				+ " SET ESTADO=?, FECHACONFIRMACION=?, ARCHIVO=?"
				+ " WHERE CODNUM=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, solicitud.getEstado());
			stmt.setDate(parameterIndex++, new Date(solicitud.getFechaConfirmacion().getTime()));
			stmt.setBinaryStream(parameterIndex++, solicitud.getArchivo());
			stmt.setInt(parameterIndex++, solicitud.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Devuelve los méritos de la bolsa en una solicitud .
	 * @param solicitud .
	 * @param bolsa .
	 * @return meritos .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public ArrayList<MeritoSolicitud> getMeritosSolicitudBolsa(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		ArrayList<MeritoSolicitud> meritos = new ArrayList<MeritoSolicitud>();
		
		String consulta = "SELECT bepsbm.* "
				+ " FROM TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm "
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM"
				+ " WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoSolicitud ms = new MeritoSolicitud(rs.getInt("CODNUM"),
							ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt("BEPMER_CODNUM")), 
							rs.getInt("BEPSBO_CODNUM"),
							rs.getString("FLGEXCLUIDO").equals("S")
					);
					meritos.add(ms);
				}
			}
		}
		
		return meritos;
	}
	
	/** Lista de bolsas de la solicitud deseleccionadas por el usuario .
	 * @param solicitud .
	 * @param bolsas .
	 * @return bolsas excluidas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private ArrayList<Bolsa> listaBolsasSolicitudExcluidas(Solicitud solicitud, List<Bolsa> bolsas) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ArrayList<Bolsa> bolsasExcluidas = new ArrayList<Bolsa>();
		
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size());
		
		// leemos las bolsas que están asignadas y ya dejan de estarlo.
		String consulta = "SELECT bepsbo.* "
				+ " FROM TBEP_SOLICITUD_BOLSAS bepsbo"
				+ " WHERE bepsbo.BEPSOL_CODNUM = ?"
				+ " AND bepsbo.BEPBOL_CODNUM NOT IN (" + params + ")";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (Bolsa bolsa: bolsas) {
				stmt.setInt(indexParam++, bolsa.getCodNum());		
			}
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = modeloBolsa.getBolsaById(rs.getInt("BEPBOL_CODNUM"));
					bolsasExcluidas.add(bolsa);
				}
			}
			
			stmt.executeUpdate();
		}
		
		return bolsasExcluidas;
	}
	
	/** Lista de bolsas de la solicitud seleccionadas por el usuario que se agregan .
	 * @param solicitud .
	 * @param bolsas .
	 * @return bolsas agregadas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private ArrayList<Bolsa> listaBolsasSolicitudAgregadas(Solicitud solicitud, List<Bolsa> bolsas) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ArrayList<Bolsa> bolsasAgregadas = new ArrayList<Bolsa>();
		
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size());
		
		// leemos las bolsas seleccionadas y le restamos el resultado de las que ya existen en la solicitud,
		// diferencia la cual nos devuelve las bolsas que hay que agregar .
		String consulta = 
				"SELECT bepbol.CODNUM FROM TBEP_BOLSAS bepbol"
				+ " WHERE bepbol.CODNUM IN (" + params + ")"
				+ " MINUS"
				+ " SELECT bepsbo.BEPBOL_CODNUM FROM TBEP_SOLICITUD_BOLSAS bepsbo"
				+ " WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM IN (" + params + ")";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			
			for (Bolsa bolsa: bolsas) {
				stmt.setInt(indexParam++, bolsa.getCodNum());		
			}
			
			stmt.setInt(indexParam++, solicitud.getCodNum());
			
			for (Bolsa bolsa: bolsas) {
				stmt.setInt(indexParam++, bolsa.getCodNum());		
			}
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = modeloBolsa.getBolsaById(rs.getInt("CODNUM"));
					bolsasAgregadas.add(bolsa);
				}
			}
			
			stmt.executeUpdate();
		}
		
		return bolsasAgregadas;
	}
	
	/**
	 * Elimina las bolsas asociadas a las solicitud y los meritos a cada bolsa si lo tienen.
	 * @param conexion . 
	 * @param solicitud .
	 * @param bolsasExcluidas .
	 * @throws SQLException .
	 */
	private void eliminarBolsasExcluidasDeLaSolicitud(Connection conexion, Solicitud solicitud, ArrayList<Bolsa> bolsasExcluidas) throws SQLException {
		String paramsExcluidas = BolsaEmpleoUtils.consultaMultiplesParametros(bolsasExcluidas.size());
		
		// eliminamos meritos
		String sqlDeleteMeritos = "DELETE FROM TBEP_SOLICITUD_BOLSAS_MERITOS bepsbm"
				+ " WHERE bepsbm.BEPSBO_CODNUM IN ("
				+ "		SELECT bepsbo.CODNUM "
				+ "		FROM TBEP_SOLICITUD_BOLSAS bepsbo "
				+ "		WHERE bepsbo.BEPSOL_CODNUM = ? "
				+ "		AND bepsbo.BEPBOL_CODNUM IN (" + paramsExcluidas + ")"
				+ " ) ";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlDeleteMeritos)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (Bolsa bolsaExcluida: bolsasExcluidas) {
				stmt.setInt(indexParam++, bolsaExcluida.getCodNum());
			}
			stmt.executeUpdate();
		}
		
		// eliminamos bolsas
		String sqlDelete = "DELETE FROM TBEP_SOLICITUD_BOLSAS bepsbo"
				+ " WHERE bepsbo.BEPSOL_CODNUM = ?"
				+ " AND bepsbo.BEPBOL_CODNUM IN (" + paramsExcluidas + ")";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlDelete)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (Bolsa bolsaExcluida: bolsasExcluidas) {
				stmt.setInt(indexParam++, bolsaExcluida.getCodNum());		
			}
			stmt.executeUpdate();
		}
	}
}
