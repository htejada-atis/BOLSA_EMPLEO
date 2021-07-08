package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
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
	public static final String SOLICITUD_EXCLUIDA = "S";
	public static final String SOLICITUD_NO_EXCLUIDA = "N";
	
	public static final String MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA = "La convocatoria no está abierta";
	public static final String MENSAJE_ERROR_SOLICITUDES_ABIERTAS = "Ya existen solicitides abiertas";
	public static final String MENSAJE_ERROR_YA_TIENES_SOLICITUD = "Ya tiene solicitudes";
	public static final String MENSAJE_ERROR_SOLICITUDE_NO_EXISTE = "No existe la solicitud";
	public static final String MENSAJE_ERROR_NO_EXISTE_MERITO_SOLICITUD = "No existe el merito de la solicitud";
	public static final String MENSAJE_ERROR_FALTAN_DATOS_CONFIRMAR_SOLICITUD = 
		"Para confirmar la solicitud debe primero completar sus datos personales. Complételos en la sección 'Mis Datos'.";
	public static final String MENSAJE_ERROR_FALTAN_DATOS_CONFIRMAR_SOLICITUD_PERSONAL = 
			"Para confirmar la solicitud el candidato debe primero completar sus datos personales.";
	public static final String MENSAJE_ERROR_SIN_SOLICITUD_CERRADA = "No existe solicitud cerrada";
	public static final String MENSAJE_ERROR_SOLICITUD_CERRADA = "La solicitud está cerrada";
	public static final String MENSAJE_ERROR_TIPO_AFINIDAD = "La afinidad no es del mismo tipo que la del item del mérito";
	
	public static final int ORDER_COLUMN_INDEX_MERITOS_ID_MERITO = 1;
	public static final int ORDER_COLUMN_INDEX_MERITOS_ITEM_CODIGO = 2;
	public static final int ORDER_COLUMN_INDEX_MERITOS_ITEM_NOMBRE = 3;
	public static final int ORDER_COLUMN_INDEX_MERITOS_VALOR = 4;
	public static final int ORDER_COLUMN_INDEX_MERITOS_AFINIDAD = 5;
	
	public static final int ORDER_COLUMN_INDEX_ID_SOLICITUD_CANDIDATO = 0;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION_CONVOCATORIA_CANDIDATO = 1;
	public static final int ORDER_COLUMN_INDEX_ESTADO_SOLICITUD_CANDIDATO = 2;
	
	public static final String CODNUM = "CODNUM";
	public static final String BEPBOL_CODNUM = "BEPBOL_CODNUM";
	public static final String BEPMER_CODNUM = "BEPMER_CODNUM";
	public static final String FLGEXCLUIDO = "FLGEXCLUIDO";
	public static final String FLGVALIDADO = "FLGVALIDADO";
	public static final String VALOR = "VALOR";
	public static final String OBSERVACION_CANDIDATO = "OBSERVACION_CANDIDATO";
	public static final String S = "S";
	public static final int RAZON_EXCLUSION_SOLICITUD_MAXLENGTH = 500;
		
	protected static ModeloSolicitud eInstancia;
	
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
	 * Listado de solicitudes en mis solicitudes. 
	 * @param usuario .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<Solicitud> listaSolicitudesDatatable(UsuarioBolsaEmpleo usuario, Map<String, String[]> params) throws SQLException, UVException {
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia(); 
		List<Solicitud> data = new ArrayList<>();
		BolsaEmpleoDataTable<Solicitud> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta =
			"SELECT "
			+ "	bepcon.CODNUM, bepcon.DESCRIPCION, bepcon.FECHACIERRE, bepcon.ESTADO ESTADO_CONVOCATORIA, bepsol.CODNUM SOLICITUD_CODNUM, "
			+ " bepsol.ESTADO ESTADO_SOLICITUD, bepsol.FLGEXCLUIDO, bepsol.RAZON_EXCLUSION, bepsol.FECHA_EXCLUSION "
			+ "FROM TBEP_CONVOCATORIAS bepcon LEFT JOIN TBEP_SOLICITUDES bepsol "
			+ "	ON bepsol.BEPCON_CODNUM = bepcon.CODNUM AND (bepsol.BEPUSU_CODNUM IS NULL OR bepsol.BEPUSU_CODNUM = ?) "
			+ "ORDER BY bepcon.CODNUM desc";
						
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {				
			stmt.setInt(1, usuario.getCodNum());
			stmtCount.setInt(1, usuario.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Solicitud solicitud = new Solicitud();
					solicitud.setCodNum(rs.getInt("SOLICITUD_CODNUM"));
					solicitud.setConvocatoria(modeloConvocatoria.getConvocatoriaById(rs.getInt(CODNUM)));
					solicitud.setEstado(rs.getString("ESTADO_SOLICITUD") != null ? rs.getString("ESTADO_SOLICITUD") : ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
					solicitud.setExcluido(rs.getString("FLGEXCLUIDO") != null ? rs.getString("FLGEXCLUIDO").equals(SOLICITUD_EXCLUIDA) : false);
					solicitud.setRazonExclusion(rs.getString("RAZON_EXCLUSION"));
					solicitud.setFechaExclusion(rs.getDate("FECHA_EXCLUSION"));
					data.add(solicitud);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado de solicitudes de un candidato. 
	 * @param candidato .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @return listado de bolsas de empleo .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<Solicitud> listaSolicitudesCandidatoDatatable(UsuarioBolsaEmpleo candidato, Map<String, String[]> params) throws SQLException, UVException {
		List<Solicitud> data = new ArrayList<>();
		BolsaEmpleoDataTable<Solicitud> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = ""
			+ " SELECT bepsol.* "
			+ "	FROM TBEP_SOLICITUDES bepsol "
			+ " WHERE bepsol.BEPUSU_CODNUM = ?";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_SOLICITUD_CANDIDATO, "bepsol.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION_CONVOCATORIA_CANDIDATO, "bepcon.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO_SOLICITUD_CANDIDATO, "bepsol.ESTADO");
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, candidato.getCodNum());
			stmtCount.setInt(indexParam++, candidato.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					data.add(this.createSolicitudFromResultSet(rs, false));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado de bolsas seleccionadas de una solicitud.
	 * @param params .
	 * @param solicitud .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<Bolsa> listaAreaSolicitudSeleccionadasDatatable(Map<String, String[]> params, Solicitud solicitud) throws UVException, SQLException {
		
		List<Bolsa> bolsas = new ArrayList<>();
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta =
			"  SELECT bepbol.* "
			+ "FROM TBEP_SOLICITUD_BOLSAS bepsbo "
			+ "INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM "
			+ "WHERE bepsbo.BEPSOL_CODNUM = ? ";
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {				
			stmt.setInt(1, solicitud.getCodNum());
			stmtCount.setInt(1, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(ModeloBolsa.obtenerInstancia().createFromResultSet(rs));					
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bolsas);
		}
		
		return dataTable;
	}
	
	/**
	 * Devuelve las bolsas de una solicitud, con su conteo de méritos.
	 * @param solicitud .
	 * @param params .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public BolsaEmpleoDataTable<BolsaSolicitudTable> listaBolsasSolicitudesDatatable(Solicitud solicitud, Map<String, String[]> params) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		List<BolsaSolicitudTable> data = new ArrayList<>();
		BolsaEmpleoDataTable<BolsaSolicitudTable> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = ""
				+ "SELECT "
				+ "		bepsbo.*, "
				+ "		(SELECT COUNT(*) FROM TBEP_SOL_BOL_MERITOS bepsbm WHERE bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM) COUNT_MERITOS "
				+ "FROM TBEP_SOLICITUD_BOLSAS bepsbo "
				+ "WHERE bepsbo.BEPSOL_CODNUM = ? ";
		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {				
			stmt.setInt(1, solicitud.getCodNum());
			stmtCount.setInt(1, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = modeloBolsa.getBolsaById(rs.getInt(BEPBOL_CODNUM));
					data.add(new BolsaSolicitudTable(bolsa, rs.getInt("COUNT_MERITOS")));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado de méritos de un usuario en la solicitud y una bolsa.
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param usuario id del usuario .
	 * @param bolsa id de la bolsa.
	 * @param solicitud .
	 * @return listado de titulaciones .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe titulación .
	 */
	public BolsaEmpleoDataTable<MeritoSolicitudTable> listaMeritosSolicitudDatatable(Map<String, String[]> params, UsuarioBolsaEmpleo usuario, Bolsa bolsa, 
			Solicitud solicitud) throws SQLException, UVException {
		
		List<MeritoSolicitudTable> meritos = new ArrayList<>();
		BolsaEmpleoDataTable<MeritoSolicitudTable> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = ""
				+ " SELECT bepmer.CODNUM, bepmer.VALOR, bepite.CODIGO, bepite.NOMBRE, MERITOS_BOLSAS.CODNUM SBM_CODNUM"
				+ " FROM TBEP_MERITOS bepmer"
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM"
				+ " LEFT JOIN ("
				+ " 	SELECT bepsbm.*"
				+ " 	FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "		INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "		WHERE bepsbo.BEPBOL_CODNUM = ? AND bepsbo.BEPSOL_CODNUM = ?"
				+ " ) MERITOS_BOLSAS ON MERITOS_BOLSAS.BEPMER_CODNUM = bepmer.CODNUM"
				+ " WHERE 1=1 "
				+ "		AND bepmer.BEPUSU_CODNUM = ? "
				+ "		AND bepite.FLGACTIVO = 'S' "
				+ "		AND bepblo.FLGACTIVO = 'S' "
				+ "		AND bepapa.FLGACTIVO = 'S' ";

		String whereCodigo = String.format("(%s || '.' || %s || '.' || %s)", "bepapa.CODIGO", "bepblo.CODIGO", "bepite.CODIGO");
		String orderCodigo = String.format("(%s || '.' || %s || '.' || %s) %%s", "LPAD(bepapa.CODIGO, 3)", "LPAD(bepblo.CODIGO, 3)", "LPAD(bepite.CODIGO, 3)");
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_MERITOS_ID_MERITO, "bepmer.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_MERITOS_ITEM_CODIGO, whereCodigo, DataTableColumn.COLUMN_TYPE_TEXT, orderCodigo);
		dataTable.setColumn(ORDER_COLUMN_INDEX_MERITOS_ITEM_NOMBRE, "bepite.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_MERITOS_VALOR, "bepmer.VALOR");
		dataTable.setColumn(ORDER_COLUMN_INDEX_MERITOS_AFINIDAD, "bepmer.CODNUM");
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, bolsa.getCodNum());
			stmtCount.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam, solicitud.getCodNum());
			stmtCount.setInt(indexParam++, solicitud.getCodNum());
			stmt.setInt(indexParam, usuario.getCodNum());
			stmtCount.setInt(indexParam++, usuario.getCodNum());			
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Merito merito = ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt(CODNUM), false, false);
					Integer idMeritoSolicitud = rs.getInt("SBM_CODNUM"); 									
					MeritoSolicitud ms = idMeritoSolicitud != 0 ? this.getMeritoSolicitudById(idMeritoSolicitud) : null;
					List<MeritoSolicitudValoracion> valoraciones = idMeritoSolicitud != null 
							? this.getValoracionesMeritoSolicitud(idMeritoSolicitud, false) : new ArrayList<>();
					
					MeritoSolicitudTable row = new MeritoSolicitudTable(merito, ms, valoraciones, this.isMeritoExcluido(merito, ms, bolsa));
					meritos.add(row);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(meritos);
		}
		
		return dataTable;
	}
	
	/**
	 * Comprueba si hay solicitudes abiertas para una convocatoria y un usuario.
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
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, SOLICITUD_ESTADO_ABIERTA);
			stmt.setInt(paramIndex++, convocatoria.getCodNum());			
			stmt.setInt(paramIndex++, usuario.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next() && rs.getInt("numero_solicitudes_abiertas") > 0) {
					return true;	    			
	    		}
    		}
		}
		
		return false;
	}
	
	/**
	 * Comprueba si hay solicitudes cerradas de un usuario para una convocatoria.
	 * @param usuario .
	 * @param convocatoria .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Solicitud getSolicitudCerradaByConvocatoriaUsuario(UsuarioBolsaEmpleo usuario, Convocatoria convocatoria) throws SQLException, UVException {
		String consulta = ""
				+ " SELECT bepsol.* "
				+ " FROM TBEP_SOLICITUDES bepsol "
				+ " WHERE bepsol.ESTADO = ? AND bepsol.BEPCON_CODNUM = ? AND bepsol.BEPUSU_CODNUM = ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, SOLICITUD_ESTADO_CERRADA);
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, usuario.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_SIN_SOLICITUD_CERRADA);
				}
				
				return this.createSolicitudFromResultSet(rs, true);
    		}
		}
	}
	
	/**
	 * Devuelve una solicitud del usuario en un convocatoria o null si no tiene.
	 * @param usuario .
	 * @param convocatoria .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException . 
	 */
	public Solicitud getSolicitudByConvocatoriaUsuario(UsuarioBolsaEmpleo usuario, Convocatoria convocatoria) throws SQLException, UVException {
		String consulta = ""
				+ " SELECT bepsol.* "
				+ " FROM TBEP_SOLICITUDES bepsol "
				+ " WHERE bepsol.BEPCON_CODNUM = ? AND bepsol.BEPUSU_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {			
			int paramIndex = 1;
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, usuario.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {					
					return this.createSolicitudFromResultSet(rs, true);
				}
    		}
		}
		
		return null;		
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
	 * Devuelve una solicitud por su id con el archivo .
	 * @param codNum .
	 * @return Solicitud o null si no existe .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Solicitud getSolicitudByIdArchivo(Integer codNum) throws SQLException, UVException {
		return getSolicitudById(codNum, true);
	}
	
	/**
	 * Devuelve un merito solicitud asociado a una solicitud y una bolsa.
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public MeritoSolicitud getMeritoSolicitud(Solicitud solicitud, Bolsa bolsa, Merito merito) throws SQLException, UVException {
		String consulta = "SELECT bepsbm.* "
				+ "FROM TBEP_SOL_BOL_MERITOS bepsbm "
				+ "INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM "
				+ "WHERE 1=1 "
				+ "AND bepsbm.BEPMER_CODNUM = ? "
				+ "AND bepsbo.BEPSOL_CODNUM = ? "
				+ "AND bepsbo.BEPBOL_CODNUM = ? ";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {			
			int params = 1;
			stmt.setInt(params++, merito.getCodNum());
			stmt.setInt(params++, solicitud.getCodNum());
			stmt.setInt(params++, bolsa.getCodNum());
									
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_MERITO_SOLICITUD);
				}
				
				Merito meritoRead = ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt(BEPMER_CODNUM), false, false);
				
				return new MeritoSolicitud(rs.getInt(CODNUM), meritoRead, rs.getString(FLGEXCLUIDO).equals(S));
			}
		}
	}
	
	/**
	 * Devuelve un merito solicitud asociado a una solicitud y una bolsa.
	 * @param convocatoria .
	 * @param bolsa .
	 * @param merito .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public MeritoSolicitud getMeritoSolicitudByConvocatoria(Convocatoria convocatoria, Bolsa bolsa, Merito merito) throws SQLException, UVException {
		String consulta = "SELECT bepsbm.*"
				+ "	FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	WHERE bepsbm.BEPMER_CODNUM = ?"
				+ "	AND bepsol.BEPCON_CODNUM = ?"
				+ "	AND bepsbo.BEPBOL_CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			
			int params = 1;
			stmt.setInt(params++, merito.getCodNum());
			stmt.setInt(params++, convocatoria.getCodNum());
			stmt.setInt(params++, bolsa.getCodNum());
									
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_MERITO_SOLICITUD);
				}
				
				Merito mer = ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt(BEPMER_CODNUM), false, false);
				int codNum = rs.getInt(CODNUM);
				Boolean excluido = rs.getString(FLGEXCLUIDO).equals(S);
				Boolean validado = rs.getString(FLGVALIDADO).equals(S);
				String observacion = rs.getString(OBSERVACION_CANDIDATO);
				MeritoSolicitud merSol = new MeritoSolicitud(codNum, mer, excluido, validado, observacion);
				merSol.setValoraciones(this.getValoracionesMeritoSolicitud(codNum, false));
				return merSol;
			}
		}
	}
	
	/**
	 * Devuelve un merito solicitud por su id.
	 * @param idMeritoSolicitud .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public MeritoSolicitud getMeritoSolicitudById(Integer idMeritoSolicitud) throws SQLException, UVException {
		if (idMeritoSolicitud == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_MERITO_SOLICITUD);
		}
		
		String consulta = "SELECT bepsbm.* FROM TBEP_SOL_BOL_MERITOS bepsbm WHERE bepsbm.CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			
			int params = 1;
			stmt.setInt(params++, idMeritoSolicitud);
									
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_MERITO_SOLICITUD);
				}
				
				Merito merito = ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt(BEPMER_CODNUM), false, false);
				
				return new MeritoSolicitud(rs.getInt(CODNUM), merito, rs.getString(FLGEXCLUIDO).equals(S),
						rs.getString(FLGVALIDADO).equals(S), rs.getString(OBSERVACION_CANDIDATO), rs.getDouble(VALOR));
			}
		}
	}
	
	/**
	 * Devuleve el listado de valoraciones de un meritoSolicitud.
	 * @param idMeritoSolicitud .
	 * @param loadMeritoSolicitud indica si cargar en memoria el MeritoSolicitud asociado
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoSolicitudValoracion> getValoracionesMeritoSolicitud(int idMeritoSolicitud, boolean loadMeritoSolicitud) throws SQLException, UVException {
		List<MeritoSolicitudValoracion> valoraciones = new ArrayList<>();
		
		String consulta = "SELECT bepsbv.* FROM TBEP_SOL_BOL_MER_VALORACION bepsbv"
				+ "	INNER JOIN TBEP_AFINIDADES bepafi ON bepafi.CODNUM = bepsbv.BEPAFI_CODNUM"
				+ "	WHERE bepsbv.BEPSBM_CODNUM = ? ORDER BY bepafi.MODULACION DESC";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam, idMeritoSolicitud);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoSolicitudValoracion msv = new MeritoSolicitudValoracion(
						rs.getInt(CODNUM),
						loadMeritoSolicitud ? this.getMeritoSolicitudById(rs.getInt("BEPSBM_CODNUM")) : null,
						ModeloAfinidad.obtenerInstancia().getAfinidadById(rs.getInt("BEPAFI_CODNUM")),
						rs.getDouble("VALOR")
					);
					valoraciones.add(msv);
				}
			}
		}
		
		return valoraciones;
	}
	
	/**
	 * Devuleve las bolsas de una solicitud .
	 * 
	 * @param solicitud .
	 * @return .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public List<Bolsa> getBolsasSolicitud(Solicitud solicitud) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ArrayList<Bolsa> bolsas = new ArrayList<>();

		String consulta = "SELECT bepsbo.* FROM TBEP_SOLICITUD_BOLSAS bepsbo WHERE bepsbo.BEPSOL_CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(modeloBolsa.getBolsaById(rs.getInt(BEPBOL_CODNUM)));
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
		ArrayList<BolsaSolicitud> bolsas = new ArrayList<>();
		
		String consulta = ""
				+ " SELECT bepsbo.* "
				+ " FROM TBEP_SOLICITUD_BOLSAS bepsbo "
				+ " WHERE bepsbo.BEPSOL_CODNUM = ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = modeloBolsa.getBolsaById(rs.getInt(BEPBOL_CODNUM));
					List<MeritoSolicitudTable> meritos = this.getMeritosValoracionesSolicitudBolsa(solicitud, bolsa);
					bolsas.add(new BolsaSolicitud(bolsa, meritos));
				}
			}
		}
		
		return bolsas;
	}

	/**
	 * Devuelve los méritos con valoraciones de la bolsa en una solicitud .
	 * @param bolsa .
	 * @param solicitud .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoSolicitudTable> getMeritosValoracionesSolicitudBolsa(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		List<MeritoSolicitudTable> meritos = new ArrayList<>();
		
		String consulta = ""
				+ " SELECT bepsbm.BEPMER_CODNUM, bepsbm.CODNUM "
				+ " FROM TBEP_SOL_BOL_MERITOS bepsbm "				
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM  = bepsbm.BEPSBO_CODNUM "
				+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM "
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM "
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM "
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM "				
				+ "	WHERE bepsbo.BEPBOL_CODNUM = ? AND bepsbo.BEPSOL_CODNUM = ? "
				+ " ORDER BY (LPAD(bepapa.CODIGO, 3) || '.' || LPAD(bepblo.CODIGO, 3) || '.' || LPAD(bepite.CODIGO, 3)) ASC";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Merito merito = ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt(BEPMER_CODNUM), false, false);
					Integer idMeritoSolicitud = rs.getInt(CODNUM);				
					MeritoSolicitud ms = idMeritoSolicitud != 0 ? this.getMeritoSolicitudById(idMeritoSolicitud) : null;
					List<MeritoSolicitudValoracion> valoraciones = idMeritoSolicitud != null 
							? this.getValoracionesMeritoSolicitud(idMeritoSolicitud, false) : new ArrayList<>();
					
					MeritoSolicitudTable row = new MeritoSolicitudTable(merito, ms, valoraciones, this.isMeritoExcluido(merito, ms, bolsa));
					meritos.add(row);
				}
			}
		}
		
		return meritos;
	}
	
	/** Devuelve los méritos de la bolsa en una solicitud .
	 * @param solicitud .
	 * @param bolsa .
	 * @return meritos .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoSolicitud> getMeritosSolicitudBolsa(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		ArrayList<MeritoSolicitud> meritos = new ArrayList<>();
		
		String consulta = "SELECT bepsbm.* "
				+ " FROM TBEP_SOL_BOL_MERITOS bepsbm "
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM"
				+ " WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoSolicitud ms = new MeritoSolicitud(rs.getInt(CODNUM),
							ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt(BEPMER_CODNUM)), 
							rs.getString(FLGEXCLUIDO).equals(S)
					);
					ms.setValoraciones(this.getValoracionesMeritoSolicitud(rs.getInt(CODNUM), false));					
					meritos.add(ms);
				}
			}
		}
		
		return meritos;
	}
	
	/**
	 * Crea una nueva solicitud para una convocatoria.
	 * @param usuario .
	 * @param convocatoria .
	 * @param usuarioInsert .
	 * @return solicitud creada	 
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Solicitud nuevaSolicitud(UsuarioBolsaEmpleo usuario, Convocatoria convocatoria, UsuarioBolsaEmpleo usuarioInsert) throws SQLException, UVException {
		if (ModeloConvocatoria.obtenerInstancia().isConvocatoriaCerrada(convocatoria)) {
			throw new UVException(MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA);
		}
		
		if (this.haySolicitudAbiertaParaConvocatoria(usuario, convocatoria)) {
			throw new UVException(MENSAJE_ERROR_SOLICITUDES_ABIERTAS);
		}
		
		if (this.getSolicitudByConvocatoriaUsuario(usuario, convocatoria) != null) {
			throw new UVException(MENSAJE_ERROR_YA_TIENES_SOLICITUD);
		}
				
		String consulta =
			"INSERT INTO TBEP_SOLICITUDES (BEPUSU_CODNUM, BEPCON_CODNUM, ESTADO, UID_USUARIO) " 
			+ "VALUES (?, ?, ?, ?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{CODNUM})) {
		
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.setInt(parameterIndex++, convocatoria.getCodNum());
			stmt.setString(parameterIndex++, ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA);
			stmt.setString(parameterIndex++, usuarioInsert.getCodCuenta());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			Integer idSolucitud = rs.getInt(1);

			return this.getSolicitudById(idSolucitud);
		}
	}	
	
	/**
	 * El usuario selecciona las bolsas para su solicitud.
	 * @param bolsas .
	 * @param solicitud .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void asignarBolsasASolicitud(Solicitud solicitud, List<Bolsa> bolsas, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (solicitud.getEstado().equals(SOLICITUD_ESTADO_CERRADA)) {
			throw new UVException(MENSAJE_ERROR_SOLICITUD_CERRADA);
		}
		
		ArrayList<Bolsa> bolsasExcluidas = listaBolsasSolicitudExcluidas(solicitud, bolsas);
		ArrayList<Bolsa> bolsasAgregadas = listaBolsasSolicitudAgregadas(solicitud, bolsas);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				// eliminamos las bolsas excluidas de la solicitud
				if (!bolsasExcluidas.isEmpty()) {
					this.eliminarBolsasExcluidasDeLaSolicitud(conexion, solicitud, bolsasExcluidas, usuarioUpdate);
				}
				
				// insertamos la bolsas agregadas a la solicitud
				if (!bolsasAgregadas.isEmpty()) {
					String paramsAgregadas = BolsaEmpleoUtils.consultaMultiplesParametros(bolsasAgregadas.size());

					String consultaInsert = "INSERT INTO TBEP_SOLICITUD_BOLSAS (BEPBOL_CODNUM, BEPSOL_CODNUM, UID_USUARIO)"
							+ " SELECT bepbol.CODNUM AS BEPBOL_CODNUM, bepsol.CODNUM AS BEPSOL_CODNUM, ? AS UID_USUARIO"
							+ " FROM TBEP_BOLSAS bepbol, TBEP_SOLICITUDES bepsol WHERE bepsol.CODNUM = ? AND "
							+ " bepbol.CODNUM IN (" + paramsAgregadas + ")";
					
					try (PreparedStatement stmt = conexion.prepareStatement(consultaInsert)) {
						int indexParam = 1;
						stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
						stmt.setInt(indexParam++, solicitud.getCodNum());
						for (Bolsa bolsa: bolsasAgregadas) {
							stmt.setInt(indexParam++, bolsa.getCodNum());
						}
						stmt.executeUpdate();
					}					
				}
				
				conexion.commit();
			} catch (Exception e) {
				conexion.rollback();
				throw e;
			} finally {
				conexion.setAutoCommit(true);
			}
		} 	
	}

	/**
	 * Se deseleccionan bolsas de la solicitud.
	 * @param solicitud .
	 * @param bolsas .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desasignarBolsasASolicitud(Solicitud solicitud, List<Bolsa> bolsas, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (solicitud.getEstado().equals(SOLICITUD_ESTADO_CERRADA)) {
			throw new UVException(MENSAJE_ERROR_SOLICITUD_CERRADA);
		}
		
		if (!bolsas.isEmpty()) {
			try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
				conexion.setAutoCommit(false);
				
				try {
					this.eliminarBolsasExcluidasDeLaSolicitud(conexion, solicitud, bolsas, usuarioUpdate);
					conexion.commit();
				} catch (Exception e) {
					conexion.rollback();
					throw e;
				} finally {
					conexion.setAutoCommit(true);
				}
			}
		}
	}
	
	/** El usuario selecciona un mérito para una bolsa en la solicitud .
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void asignarMeritosASolicitudBolsa(Solicitud solicitud, Bolsa bolsa, Merito merito, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (solicitud.getEstado().equals(SOLICITUD_ESTADO_CERRADA)) {
			throw new UVException(MENSAJE_ERROR_SOLICITUD_CERRADA);
		}
		if (!bolsa.getEstado().equals(ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA)) {
			throw new UVException("La bolsa no está desbloqueada. No se puede añadir méritos.");
		}
		
		// insertamos el mérito en la solicitud bolsa
		String consulta = "INSERT INTO TBEP_SOL_BOL_MERITOS (BEPSBO_CODNUM, BEPMER_CODNUM, UID_USUARIO)"
				+ " SELECT bepsbo.CODNUM AS BEPSBO_CODNUM, bepmer.CODNUM AS BEPMER_CODNUM, ? AS UID_USUARIO"
				+ " FROM TBEP_SOLICITUD_BOLSAS bepsbo, TBEP_MERITOS bepmer"
				+ " WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ? AND bepmer.CODNUM = ? ";
					
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, merito.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** El usuario deselecciona un mérito para una bolsa en la solicitud .
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void borrarMeritoDeSolicitudBolsa(Solicitud solicitud, Bolsa bolsa, Merito merito, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (solicitud.getEstado().equals(SOLICITUD_ESTADO_CERRADA)) {
			throw new UVException(MENSAJE_ERROR_SOLICITUD_CERRADA);
		}
		if (!bolsa.getEstado().equals(ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA)) {
			throw new UVException("La bolsa no está desbloqueada. No se puede quitar méritos.");
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				borrarValoracionesMeritoSolicitudBolsa(solicitud, bolsa, merito, usuarioUpdate, conexion);
				
				// actualizamos usuario en valoraciones
				String sqlUpdateMeritos = "UPDATE TBEP_SOL_BOL_MERITOS bepsbm"
						+ " SET bepsbm.UID_USUARIO = ?"
						+ "	WHERE bepsbm.BEPSBO_CODNUM IN ("
						+ "		SELECT bepsbo.CODNUM"
						+ "		FROM TBEP_SOLICITUD_BOLSAS bepsbo"
						+ "		WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ?"
						+ "	) AND bepsbm.BEPMER_CODNUM = ?";
				
				try (PreparedStatement stmt = conexion.prepareStatement(sqlUpdateMeritos)) {
					int indexParam = 1;
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.setInt(indexParam++, solicitud.getCodNum());
					stmt.setInt(indexParam++, bolsa.getCodNum());
					stmt.setInt(indexParam++, merito.getCodNum());
					stmt.executeUpdate();
				}
				
				// eliminamos merito en la solicitud
				String sqlMeritos = ""
						+ "DELETE FROM TBEP_SOL_BOL_MERITOS bepsbm "
						+ "WHERE bepsbm.BEPSBO_CODNUM IN ( "
						+ "		SELECT bepsbo.CODNUM "
						+ "		FROM TBEP_SOLICITUD_BOLSAS bepsbo "
						+ "		WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ? "
						+ ") AND bepsbm.BEPMER_CODNUM = ?";
				
				try (PreparedStatement stmt = conexion.prepareStatement(sqlMeritos)) {
					int indexParam = 1;
					stmt.setInt(indexParam++, solicitud.getCodNum());
					stmt.setInt(indexParam++, bolsa.getCodNum());
					stmt.setInt(indexParam++, merito.getCodNum());
					stmt.executeUpdate();
				}
				
				conexion.commit();
			} catch (Exception e) {
				conexion.rollback();
				throw e;
			} finally {
				conexion.setAutoCommit(true);				
			}
		}
	}
	
	/** El usuario ha seleccionado la afinidad del mérito individualizado. 
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito -
	 * @param afinidad .
	 * @param usuarioInsert .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void asignarAfinidadMeritoIndividualizado(Solicitud solicitud, Bolsa bolsa, Merito merito, Afinidad afinidad, UsuarioBolsaEmpleo usuarioInsert) 
			throws SQLException, UVException {
		
		if (!merito.getItemBaremacion().getAfinidad().equals(afinidad.getCodigo())) {
			throw new UVException(MENSAJE_ERROR_TIPO_AFINIDAD);
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				MeritoSolicitud meritoSolicitud = this.getMeritoSolicitud(solicitud, bolsa, merito);
				
				// eliminamos la afinidad previamente seleccionada
				this.eliminarValoracionesDelMeritoSolicitud(conexion, meritoSolicitud);
							
				// insertamos la afinidad del mérito
				String sqlInsert = "INSERT INTO TBEP_SOL_BOL_MER_VALORACION (BEPSBM_CODNUM, BEPAFI_CODNUM, VALOR, UID_USUARIO) VALUES (?,?,NULL,?)";
				try (PreparedStatement stmt = conexion.prepareStatement(sqlInsert)) {
					int indexParam = 1;
					stmt.setInt(indexParam++, meritoSolicitud.getCodNum());
					stmt.setInt(indexParam++, afinidad.getCodNum());	
					stmt.setString(indexParam++, usuarioInsert.getCodCuenta());
					stmt.executeUpdate();
				}		
				
				conexion.commit();
			} catch (Exception e) {				
				conexion.rollback();
				throw e;
			} finally {
				conexion.setAutoCommit(true);
			}
		}
	}
	
	/** El usuario ha seleccionado la afinidad del mérito no individualizado. 
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito .
	 * @param afinidades .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void asignarAfinidadMeritoNoIndividualizado(Solicitud solicitud, Bolsa bolsa, Merito merito, Map<Afinidad, Double> afinidades, UsuarioBolsaEmpleo usuarioUpdate)
			throws SQLException, UVException {
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				MeritoSolicitud meritoSolicitud = this.getMeritoSolicitud(solicitud, bolsa, merito);
				
				// eliminamos las afinidades previamente seleccionadas
				this.eliminarValoracionesDelMeritoSolicitud(conexion, meritoSolicitud);
										
				// insertamos las afinidades del mérito
				for (Map.Entry<Afinidad, Double> entry : afinidades.entrySet()) {
					if (!merito.getItemBaremacion().getAfinidad().equals(entry.getKey().getCodigo())) {
						throw new UVException("La afinidad no es del mismo tipo que la del item del mérito");
					}
					
					String sqlInsert = "INSERT INTO TBEP_SOL_BOL_MER_VALORACION (BEPSBM_CODNUM, BEPAFI_CODNUM, VALOR, UID_USUARIO) VALUES (?,?,?,?)";
					
					try (PreparedStatement stmt = conexion.prepareStatement(sqlInsert)) {
						int indexParam = 1;
						stmt.setInt(indexParam++, meritoSolicitud.getCodNum());
						stmt.setInt(indexParam++, entry.getKey().getCodNum());
						stmt.setDouble(indexParam++, entry.getValue());
						stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
						stmt.executeUpdate();
					}			
				}
				
				conexion.commit();
			} catch (Exception e) {
				conexion.rollback();
				throw e;
			} finally {
				conexion.setAutoCommit(true);
			}
		}
	}
	
	/** El evaluador ha modificado la afinidad del mérito no individualizado . 
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito .
	 * @param afinidades .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void actualizarAfinidadesMeritoNoIndividualizado(Solicitud solicitud, Bolsa bolsa, Merito merito, Map<Afinidad, Double> afinidades, UsuarioBolsaEmpleo usuarioUpdate)
			throws SQLException, UVException {
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				MeritoSolicitud meritoSolicitud = this.getMeritoSolicitud(solicitud, bolsa, merito);
				
				for (Map.Entry<Afinidad, Double> entry : afinidades.entrySet()) {
					if (compruebaSiExisteAfinidad(meritoSolicitud, entry.getKey().getCodNum(), conexion)) {
						String consultaUpdate = "UPDATE TBEP_SOL_BOL_MER_VALORACION"
								+ " SET VALOR = ?, UID_USUARIO = ?"
								+ " WHERE BEPSBM_CODNUM = ? AND BEPAFI_CODNUM = ?";
						
						try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
							int indexParam = 1;
							stmt.setDouble(indexParam++, entry.getValue());
							stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
							stmt.setInt(indexParam++, meritoSolicitud.getCodNum());
							stmt.setInt(indexParam++, entry.getKey().getCodNum());
							stmt.executeUpdate();
						}
					} else {
						String consultaInsert = "INSERT INTO TBEP_SOL_BOL_MER_VALORACION (BEPSBM_CODNUM, BEPAFI_CODNUM, VALOR, UID_USUARIO)"
								+ " VALUES (?,?,?,?)";
						
						try (PreparedStatement stmt = conexion.prepareStatement(consultaInsert)) {
							int indexParam = 1;
							stmt.setInt(indexParam++, meritoSolicitud.getCodNum());
							stmt.setInt(indexParam++, entry.getKey().getCodNum());
							stmt.setDouble(indexParam++, entry.getValue());
							stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
							stmt.executeUpdate();
						}
					}
				}
				
				conexion.commit();
			} catch (Exception e) {
				conexion.rollback();
				throw e;
			} finally {
				conexion.setAutoCommit(true);
			}
		}
	}
	
	/** Método que comprueba si existe una afinidad para luego insertarla o actualizarla . 
	 * @param meritoSolicitud .
	 * @param idAfinidad .
	 * @param conexion .
	 * @return booleano que devuelve si existe o no .
	 * @throws SQLException .
	 */
	private boolean compruebaSiExisteAfinidad(MeritoSolicitud meritoSolicitud, int idAfinidad, Connection conexion) throws SQLException {
		String consultaSelect = "SELECT * FROM TBEP_SOL_BOL_MER_VALORACION"
				+ "	WHERE BEPSBM_CODNUM = ? AND BEPAFI_CODNUM = ?";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consultaSelect)) {			
			int indexParam = 1;
			stmt.setInt(indexParam++, meritoSolicitud.getCodNum());
			stmt.setInt(indexParam++, idAfinidad);
									
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** El evaluador ha modificado la afinidad del mérito no individualizado .
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito .
	 * @param afinidad .
	 * @param idValoracion .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void actualizarAfinidadesMeritoIndividualizado(Solicitud solicitud, Bolsa bolsa, Merito merito, Afinidad afinidad, Integer idValoracion,
			UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		MeritoSolicitud meritoSolicitud = this.getMeritoSolicitud(solicitud, bolsa, merito);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
		
			if (idValoracion != 0) {
				String consultaUpdate = "UPDATE TBEP_SOL_BOL_MER_VALORACION"
						+ " SET VALOR = ?, BEPAFI_CODNUM = ?, UID_USUARIO = ?"
						+ " WHERE BEPSBM_CODNUM = ? AND CODNUM = ?";
				
				try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
					int indexParam = 1;
					stmt.setNull(indexParam++, Types.NUMERIC);
					stmt.setInt(indexParam++, afinidad.getCodNum());
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.setInt(indexParam++, meritoSolicitud.getCodNum());
					stmt.setInt(indexParam++, idValoracion);
					stmt.executeUpdate();
				}
			} else {
				String consultaInsert = "INSERT INTO TBEP_SOL_BOL_MER_VALORACION (BEPSBM_CODNUM, BEPAFI_CODNUM, VALOR, UID_USUARIO)"
						+ " VALUES (?,?,?,?)";
				
				try (PreparedStatement stmt = conexion.prepareStatement(consultaInsert)) {
					int indexParam = 1;
					stmt.setInt(indexParam++, meritoSolicitud.getCodNum());
					stmt.setInt(indexParam++, afinidad.getCodNum());
					stmt.setNull(indexParam++, Types.NUMERIC);
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.executeUpdate();
				}
			}
		}
	}
	
	/**
	 * Obtiene el total de méritos por bloque que hay en la solicitud .
	 * @param solicitud .
	 * @param bolsa .
	 * @param merito .
	 * @return total .
	 * @throws SQLException .
	 */
	public Integer obtenerTotalMeritosPorBloqueSolicitud(Solicitud solicitud, Bolsa bolsa, Merito merito) throws SQLException {
		Integer total = 0;
		String consulta = 
				"   SELECT COUNT(*) AS total " 
				+ " FROM TBEP_SOL_BOL_MERITOS bepsbm "
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM "
				+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM "
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM "
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM "
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM = bepblo.BEPAPA_CODNUM "
				+ " WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ? AND bepapa.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, solicitud.getCodNum());
			stmt.setInt(parameterIndex++, bolsa.getCodNum());
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
		String consulta = "SELECT COUNT(*) AS total FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM"
				+ "	WHERE bepsbo.BEPSOL_CODNUM = ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
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
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void confirmacionSolicitud(Solicitud solicitud, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
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
				+ " SET ESTADO=?, FECHACONFIRMACION=?, ARCHIVO=?, UID_USUARIO=?"
				+ " WHERE CODNUM=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, solicitud.getEstado());
			stmt.setDate(parameterIndex++, new Date(solicitud.getFechaConfirmacion().getTime()));
			stmt.setBinaryStream(parameterIndex++, solicitud.getArchivo());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, solicitud.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Actualiza el estado de la solicitud a abierto .
	 * @param solicitud .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void reabrirSolicitud(Solicitud solicitud, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (solicitud == null) {
			throw new UVException("No se puede reabrir una solicitud vacía");
		}
		
		if (solicitud.getCodNum() == null) {
			throw new UVException("No se puede reabrir una solicitud con id vacío");
		}
		
		String consulta = "UPDATE TBEP_SOLICITUDES "
				+ " SET ESTADO=?, FECHACONFIRMACION=null, ARCHIVO=null, UID_USUARIO=?"
				+ " WHERE CODNUM=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, solicitud.getEstado());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, solicitud.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Excluye/incluye una solicitud de un candidato para que no se evaluada.
	 * @param solicitud .
	 * @param excluir .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void excluirIncluirSolicitud(Solicitud solicitud, boolean excluir, UsuarioBolsaEmpleo usuarioUpdate) throws UVException, SQLException {
		if (solicitud == null) {
			throw new UVException("No se puede reabrir una solicitud vacía");
		}
		
		if (solicitud.getCodNum() == null) {
			throw new UVException("No se puede reabrir una solicitud con id vacío");
		}
		
		String consulta = ""
				+ " UPDATE TBEP_SOLICITUDES "
				+ " SET FLGEXCLUIDO=?, RAZON_EXCLUSION=?, FECHA_EXCLUSION=?, UID_USUARIO=?"
				+ " WHERE CODNUM=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			
			if (excluir) {
				stmt.setString(parameterIndex++, SOLICITUD_EXCLUIDA);
				stmt.setString(parameterIndex++, solicitud.getRazonExclusion());
				stmt.setDate(parameterIndex++, new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			} else {
				stmt.setString(parameterIndex++, SOLICITUD_NO_EXCLUIDA);
				stmt.setNull(parameterIndex++, Types.VARCHAR);
				stmt.setNull(parameterIndex++, Types.DATE);
			}
			
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, solicitud.getCodNum());
			stmt.executeUpdate();
		}
	} 
		
	/**
	 * Comprueba los méritos que tienen afinidad y no tienen valoración .
	 * @param solicitud .
	 * @return booleano que devuelve si hay méritos con afinidad sin valorar .
	 * @throws SQLException .
	 */
	public boolean comprobarMeritosAfinidadSinValoracion(Solicitud solicitud) throws SQLException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			// comprobar individualizados
			String consultaIndividualizados = "SELECT * FROM TBEP_SOL_BOL_MERITOS bepsbm"
					+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM"
					+ "	LEFT JOIN TBEP_SOL_BOL_MER_VALORACION bepsbv ON bepsbv.BEPSBM_CODNUM  = bepsbm.CODNUM"
					+ "	INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM  = bepsbm.BEPMER_CODNUM"
					+ "	INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
					+ "	WHERE bepsbo.BEPSOL_CODNUM = ? AND bepite.AFINIDAD IS NOT NULL AND bepsbv.CODNUM IS NULL AND bepite.INDIVIDUALIZADO = 'S'";
			
			try (PreparedStatement stmt = conexion.prepareStatement(consultaIndividualizados)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, solicitud.getCodNum());
				stmt.executeUpdate();
				
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						return true;
					}
				}
			}
			
			// comprobar no individualizados
			String consultaNoIndividualizados = "SELECT * FROM TBEP_SOL_BOL_MERITOS bepsbm"
					+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbm.BEPSBO_CODNUM = bepsbo.CODNUM"
					+ "	LEFT JOIN TBEP_SOL_BOL_MER_VALORACION bepsbv ON bepsbv.BEPSBM_CODNUM  = bepsbm.CODNUM"
					+ "	INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM  = bepsbm.BEPMER_CODNUM"
					+ "	INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
					+ "	WHERE bepsbo.BEPSOL_CODNUM = ? AND bepite.AFINIDAD IS NOT NULL AND bepsbv.VALOR IS NULL AND bepite.INDIVIDUALIZADO = 'N'";
			
			try (PreparedStatement stmt = conexion.prepareStatement(consultaNoIndividualizados)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, solicitud.getCodNum());
				stmt.executeUpdate();
				
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/** Comprueba si un mérito puede ser borrado .
	 * @param merito .
	 * @return booleano que devuelve si la consulta obtiene resultados . 
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public boolean comprobarMeritoPuedeSerBorrado(Merito merito) throws SQLException {
		boolean asociadoASolicitud = false;
		boolean evaluado = false;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			// si el mérito está asociado a una solicitud, no se puede borrar		
			String consultaSolicitud = "SELECT besbm.* FROM TBEP_SOL_BOL_MERITOS besbm WHERE besbm.BEPMER_CODNUM = ?";
			
			try (PreparedStatement stmt = conexion.prepareStatement(consultaSolicitud)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, merito.getCodNum());
				
				try (ResultSet rs = stmt.executeQuery()) {
					asociadoASolicitud = rs.next();				
				}
			}
			
			// si el mérito ha sido evaludado, no se puede borrar
			String consultaEvaluado = "SELECT bepsbm.* FROM TBEP_SOL_BOL_MERITOS bepsbm WHERE bepsbm.FLGVALIDADO = 'S' AND bepsbm.BEPMER_CODNUM = ?";
			
			try (PreparedStatement stmt = conexion.prepareStatement(consultaEvaluado)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, merito.getCodNum());
				
				try (ResultSet rs = stmt.executeQuery()) {
					evaluado = rs.next();				
				}
			}
		}
		
		// si la convocatoria está cerrada, no se puede borrar
		Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		boolean convocatoriaCerrada = ModeloConvocatoria.obtenerInstancia().isConvocatoriaCerrada(c);
		
		return !asociadoASolicitud && !evaluado && !convocatoriaCerrada;
	}
	
	/**
	 * Comprueba si el mérito preferente se puede borrar.
	 * @param merito .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public boolean comprobarMeritoPreferentePuedeSerBorrado(MeritoPreferenteUsuario merito) throws SQLException {
		// si la convocatoria está cerrada, no se puede
		Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		boolean convocatoriaCerrada = ModeloConvocatoria.obtenerInstancia().isConvocatoriaCerrada(c);

		return !merito.isValidado() && !convocatoriaCerrada;
	}
	
	/**
	 * Devuelve si la titulación de usuario puede ser borrada.
	 * @param t .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public boolean comprobarTitulacionUsuarioPuedeSerBorrada(TitulacionUsuario t) throws SQLException {
		// si la convocatoria está cerrada, no se puede
		Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		boolean convocatoriaCerrada = ModeloConvocatoria.obtenerInstancia().isConvocatoriaCerrada(c);
		
		return !t.getValidada() && !convocatoriaCerrada;
	}
	
	/**
	 * Comprueba si el mérito preferente se puede crear.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public boolean comprobarMeritoPreferentePuedeSerCreado() throws SQLException {
		return this.comprobarTitulacionPuedeSerCreada();
	}
	
	/**
	 * Comprueba si la titulación se puede crear.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public boolean comprobarTitulacionPuedeSerCreada() throws SQLException {
		// si la convocatoria está cerrada, no se puede
		Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		boolean convocatoriaCerrada = ModeloConvocatoria.obtenerInstancia().isConvocatoriaCerrada(c);

		return !convocatoriaCerrada;
	}

	/**
	 * Comprueba si se puede confirmar la solicitud.
	 * @param solicitud .
	 * @param personal booleano que indica si es un usuario de personal el que hace la acción .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void comprobarSolicitudCorrecta(Solicitud solicitud, boolean personal) throws SQLException, UVException {			
		// comprobamos datos de usuario completos
		if (ModeloUsuarioBolsaEmpleo.obtenerInstancia().compruebaUsuarioMisDatosValidos(solicitud.getUsuario())) {
			throw new UVException(personal ? MENSAJE_ERROR_FALTAN_DATOS_CONFIRMAR_SOLICITUD_PERSONAL : MENSAJE_ERROR_FALTAN_DATOS_CONFIRMAR_SOLICITUD);
		}
		
		// comprobamos si la convocatoria está abierta
		if (ModeloConvocatoria.obtenerInstancia().isConvocatoriaCerrada(solicitud.getConvocatoria()) && !personal) {
			throw new UVException(MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA);
		}
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
		ArrayList<Bolsa> bolsasExcluidas = new ArrayList<>();
		
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
					Bolsa bolsa = modeloBolsa.getBolsaById(rs.getInt(BEPBOL_CODNUM));
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
	@SuppressWarnings("java:S3047")
	private ArrayList<Bolsa> listaBolsasSolicitudAgregadas(Solicitud solicitud, List<Bolsa> bolsas) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		ArrayList<Bolsa> bolsasAgregadas = new ArrayList<>();
		
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
					Bolsa bolsa = modeloBolsa.getBolsaById(rs.getInt(CODNUM));
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
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	private void eliminarBolsasExcluidasDeLaSolicitud(Connection conexion, Solicitud solicitud, List<Bolsa> bolsasExcluidas, UsuarioBolsaEmpleo usuarioUpdate)
			throws SQLException {
		
		String paramsExcluidas = BolsaEmpleoUtils.consultaMultiplesParametros(bolsasExcluidas.size());
		
		// subquery solicitudes_bolsas
		
		String subQuerybepsbo = ""
				+ " SELECT bepsbo.CODNUM "
				+ "	FROM TBEP_SOLICITUD_BOLSAS bepsbo "
				+ "	WHERE bepsbo.BEPSOL_CODNUM = ? "
				+ "	AND bepsbo.BEPBOL_CODNUM IN (" + paramsExcluidas + ")";
			
		// eliminamos valoraciones de los méritos (antes update de usuario)
		
		String sqlUpdateValoracionMeritos = ""
				+ " UPDATE TBEP_SOL_BOL_MER_VALORACION bepsbv SET bepsbv.UID_USUARIO = ? "
				+ " WHERE bepsbv.BEPSBM_CODNUM IN ( "
				+ " 	SELECT bepsbm.CODNUM "
				+ "	    FROM TBEP_SOL_BOL_MERITOS bepsbm "
				+ "		WHERE bepsbm.BEPSBO_CODNUM IN (" + subQuerybepsbo + ")"
				+ " )";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlUpdateValoracionMeritos)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (Bolsa bolsaExcluida: bolsasExcluidas) {
				stmt.setInt(indexParam++, bolsaExcluida.getCodNum());
			}
			stmt.executeUpdate();
		}
		
		String sqlDeleteValoraciones = ""
				+ " DELETE FROM TBEP_SOL_BOL_MER_VALORACION bepsbv "
				+ " WHERE bepsbv.BEPSBM_CODNUM IN ( "
				+ " 	SELECT bepsbm.CODNUM "
				+ "	    FROM TBEP_SOL_BOL_MERITOS bepsbm "
				+ "		WHERE bepsbm.BEPSBO_CODNUM IN (" + subQuerybepsbo + ")"
				+ " )";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlDeleteValoraciones)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (Bolsa bolsaExcluida: bolsasExcluidas) {
				stmt.setInt(indexParam++, bolsaExcluida.getCodNum());
			}
			stmt.executeUpdate();
		}
		
		// eliminamos méritos de las bolsas (antes update usuario)
		
		String sqlUpdateMeritos = ""
				+ " UPDATE TBEP_SOL_BOL_MERITOS bepsbm SET bepsbm.UID_USUARIO = ?"
				+ " WHERE bepsbm.BEPSBO_CODNUM IN (" + subQuerybepsbo + ")";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlUpdateMeritos)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (Bolsa bolsaExcluida: bolsasExcluidas) {
				stmt.setInt(indexParam++, bolsaExcluida.getCodNum());
			}
			stmt.executeUpdate();
		}
		
		// eliminamos meritos
		String sqlDeleteMeritos = ""
				+ " DELETE FROM TBEP_SOL_BOL_MERITOS bepsbm "
				+ " WHERE bepsbm.BEPSBO_CODNUM IN (" + subQuerybepsbo + ")";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlDeleteMeritos)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (Bolsa bolsaExcluida: bolsasExcluidas) {
				stmt.setInt(indexParam++, bolsaExcluida.getCodNum());
			}
			stmt.executeUpdate();
		}
		
		// actualiza usuario de las bolsas de la solicitud
		String sqlUpdateBolsas = "UPDATE TBEP_SOLICITUD_BOLSAS bepsbo"
				+ "	SET bepsbo.UID_USUARIO = ?"
				+ " WHERE bepsbo.BEPSOL_CODNUM = ?"
				+ " AND bepsbo.BEPBOL_CODNUM IN (" + paramsExcluidas + ")";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlUpdateBolsas)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (Bolsa bolsaExcluida: bolsasExcluidas) {
				stmt.setInt(indexParam++, bolsaExcluida.getCodNum());		
			}
			stmt.executeUpdate();
		}
		
		// eliminamos bolsas
		String sqlDeleteBolsas = "DELETE FROM TBEP_SOLICITUD_BOLSAS bepsbo"
				+ " WHERE bepsbo.BEPSOL_CODNUM = ?"
				+ " AND bepsbo.BEPBOL_CODNUM IN (" + paramsExcluidas + ")";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlDeleteBolsas)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			for (Bolsa bolsaExcluida: bolsasExcluidas) {
				stmt.setInt(indexParam++, bolsaExcluida.getCodNum());		
			}
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Elimina las valoraciones (asociadas a la afinidad de una solicitudMerito).
	 * @param conexion .
	 * @param meritoSolicitud .
	 * @throws SQLException .
	 */
	private void eliminarValoracionesDelMeritoSolicitud(Connection conexion, MeritoSolicitud meritoSolicitud) throws SQLException {
		String sqlDelete = "DELETE FROM TBEP_SOL_BOL_MER_VALORACION bepsbv WHERE bepsbv.BEPSBM_CODNUM = ?";
		try (PreparedStatement stmt = conexion.prepareStatement(sqlDelete)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, meritoSolicitud.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	private void borrarValoracionesMeritoSolicitudBolsa(Solicitud solicitud, Bolsa bolsa, Merito merito, UsuarioBolsaEmpleo usuarioUpdate, Connection conexion) 
			throws SQLException {
		
		// actualizamos usuario en valoraciones
		String sqlUpdateValoraciones = "UPDATE TBEP_SOL_BOL_MER_VALORACION"
				+ " SET UID_USUARIO = ?"
				+ " WHERE BEPSBM_CODNUM IN ("
				+ "		SELECT bepsbm.CODNUM "
				+ "		FROM TBEP_SOL_BOL_MERITOS bepsbm "
				+ "		WHERE bepsbm.BEPMER_CODNUM = ? AND bepsbm.BEPSBO_CODNUM IN ( "
				+ "			SELECT bepsbo.CODNUM "
				+ "			FROM TBEP_SOLICITUD_BOLSAS bepsbo "
				+ "			WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ? "						
				+ "		)"
				+ ")";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlUpdateValoraciones)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, merito.getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.executeUpdate();
		}
					
		// eliminamos valoraciones
		String sqlValoraciones = ""
				+ "DELETE FROM TBEP_SOL_BOL_MER_VALORACION bepsbv WHERE bepsbv.BEPSBM_CODNUM IN ( "
				+ "		SELECT bepsbm.CODNUM "
				+ "		FROM TBEP_SOL_BOL_MERITOS bepsbm "
				+ "		WHERE bepsbm.BEPMER_CODNUM = ? AND bepsbm.BEPSBO_CODNUM IN ( "
				+ "			SELECT bepsbo.CODNUM "
				+ "			FROM TBEP_SOLICITUD_BOLSAS bepsbo "
				+ "			WHERE bepsbo.BEPSOL_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ? "						
				+ "		)"
				+ ")";
		
		try (PreparedStatement stmt = conexion.prepareStatement(sqlValoraciones)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, merito.getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.executeUpdate();
		}
	}
	 
	private Solicitud getSolicitudById(Integer codNum, boolean archivo) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("La solicitud es requerida");
		}
			
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia();
		String consulta = "SELECT bepsol.* FROM TBEP_SOLICITUDES bepsol WHERE bepsol.CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_SOLICITUDE_NO_EXISTE);
				}
				
				Solicitud solicitud = new Solicitud();
				solicitud.setCodNum(rs.getInt(CODNUM));
				solicitud.setConvocatoria(modeloConvocatoria.getConvocatoriaById(rs.getInt("BEPCON_CODNUM")));
				solicitud.setEstado(rs.getString("ESTADO"));
				solicitud.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
				solicitud.setFechaConfirmacion(rs.getDate("FECHACONFIRMACION"));
				
				if (archivo) {
					solicitud.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
				}
				
				return this.createSolicitudFromResultSet(rs, archivo);
			}
		}
	}
	
	/**
	 * Comprueba si un mérito está excluido en una bolsa.
	 * @param merito .
	 * @param ms .
	 * @param bolsa .
	 * @return .
	 */
	public Boolean isMeritoExcluido(Merito merito, MeritoSolicitud ms, Bolsa bolsa) {
		return false;
	}	

	public Solicitud createSolicitudFromResultSet(ResultSet rs, boolean archivo) throws SQLException, UVException {
		Solicitud solicitud = new Solicitud();
		
		solicitud.setCodNum(rs.getInt("CODNUM"));
		solicitud.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
		solicitud.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(rs.getInt("BEPCON_CODNUM")));
		solicitud.setEstado(rs.getString("ESTADO") != null ? rs.getString("ESTADO") : ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
		solicitud.setFechaConfirmacion(rs.getDate("FECHACONFIRMACION"));
		solicitud.setExcluido(rs.getString("FLGEXCLUIDO").equals(SOLICITUD_EXCLUIDA));		
		solicitud.setRazonExclusion(rs.getString("RAZON_EXCLUSION"));
		solicitud.setFechaExclusion(rs.getDate("FECHA_EXCLUSION"));
		
		if (archivo && rs.getBlob("ARCHIVO") != null) {
			solicitud.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
		}
		
		return solicitud;
	}
}
