package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BolsaCandidato;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable.DataTableColumn;

/**
 * Clase de modelo para la gestión de areas. 
 * @author ATISoluciones
 */
public class ModeloArea {	
	
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_CODIGO = 2;
	public static final int ORDER_COLUMN_INDEX_AREA = 3;
	public static final int ORDER_COLUMN_INDEX_BAREMALE = 4;
	
	public static final int ORDER_COLUMN_INDEX_CODIGO_SOLICITUDES = 1;
	public static final int ORDER_COLUMN_INDEX_AREA_SOLICITUDES = 2;
	public static final int ORDER_COLUMN_INDEX_EXCLUIDO_SOLICITUDES = 3;
	public static final int ORDER_COLUMN_INDEX_BAREMABLE_SOLICITUDES = 4;
	
	public static final int ORDER_COLUMN_INDEX_CODIGO_CANDIDATO = 0;
	public static final int ORDER_COLUMN_INDEX_AREA_CANDIDATO = 1;
	
    protected static ModeloArea eInstancia = null;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloArea();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloArea obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }
	
	/** Consulta areas en BBDD y las devuelve.
	 * @param clausula para filtrar las areas de la bd
	 * @return areas de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	private List<Area> listaAreas(String clausula) throws SQLException {
		ModeloDepartamento modeloDepartamento = ModeloDepartamento.obtenerInstancia();
		List<Area> areas = new ArrayList<>();
		String consulta = "SELECT bepare.* FROM TBEP_AREAS bepare " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							Area are = new Area();
							are.setCodNum(rs.getInt("CODNUM"));
							are.setDepartamento(modeloDepartamento.getDepartamentoById(rs.getInt("BEPDEP_CODNUM")));
							are.setIdAreaExterno(rs.getString("ID_AREA_CONOCIMIENTO"));
							are.setIdSeccion(rs.getString("ID_SECCION"));
							are.setDescripcion(rs.getString("DES_AREA_CONOCIMIENTO"));
							areas.add(are);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		return areas;
	}
	
	/** lista todas las areas.
	 * @return lista de todas las areas
	 * @throws SQLException si hay un error en la base de datos
	 */
	public List<Area> listaAreas() throws SQLException {
		return listaAreas(" ORDER BY DES_AREA_CONOCIMIENTO");
	}
		
	/**
	 * Devuelve un area por su id.
	 * @param codNum id de area
	 * @return area
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si area no es existe
	 */
	public Area getAreaById(int codNum) throws SQLException, UVException {
		ModeloDepartamento modeloDepartamento = ModeloDepartamento.obtenerInstancia();
		String consulta = "SELECT bepare.* FROM TBEP_AREAS bepare WHERE bepare.CODNUM = ?";				
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el area con id " + codNum);
				}
				
				Area area = new Area();
				area.setCodNum(rs.getInt("CODNUM"));
				area.setDepartamento(modeloDepartamento.getDepartamentoById(rs.getInt("BEPDEP_CODNUM")));
				area.setIdAreaExterno(rs.getString("ID_AREA_CONOCIMIENTO"));
				area.setIdSeccion(rs.getString("ID_SECCION"));
				area.setDescripcion(rs.getString("DES_AREA_CONOCIMIENTO"));
				
				return area;
			}
		}
	}
	
	/**
	 * Devuelve un listado de areas por su id.
	 * @param ids codnum de areas
	 * @return listado de areas
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<Area> getAreasByIds(int[] ids) throws SQLException, UVException {
		List<Area> areas = new ArrayList<>();
		
		for (int i = 0; i < ids.length; i++) {
			areas.add(this.getAreaById(ids[i]));
	    }
		
		return areas;
	}
		
	/**
	 * Listado de areas. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<Bolsa> listaAreaDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();	
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<Bolsa>(params);
		
		String consulta =
		"SELECT bepbol.* "
		+ "FROM TBEP_BOLSAS bepbol "
		+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
		+ "WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepbol.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BAREMALE, "bepbol.FLGBAREMABLE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
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
	
	/**
	 * Listado de areas. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<Bolsa> listaAreaCandidatoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();	
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<Bolsa>(params);
		
		String consulta =
		"SELECT bepbol.* "
		+ "FROM TBEP_BOLSAS bepbol "
		+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
		+ "WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_CANDIDATO, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA_CANDIDATO, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
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
	
	/**
	 * Listado de areas excluidas de un usuario. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param codnum .
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<Bolsa> listaAreaExcluidasUsuarioDatatable(Map<String, String[]> params, Integer codnum) throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();	
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<Bolsa>(params);
		
		String consulta =
		"SELECT bepbol.* "
		+ "FROM TBEP_BOLSAS bepbol "
		+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
		+ "LEFT JOIN UVIRTUAL.TBEP_USUARIOS_EXCLUIDOS_AREA bepusuexc "
		+ "ON bepare.CODNUM = bepusuexc.AREA AND bepusuexc.USUARIO = ? "
		+ "WHERE bepusuexc.USUARIO IS NULL";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepare.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BAREMALE, "bepbol.FLGBAREMABLE", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
		
			int indexParam = 1;
			stmt.setInt(indexParam, codnum);
			stmtCount.setInt(indexParam++, codnum);
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
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
	
	/**
	 * Listado de areas solicitudes . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param codnum .
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<BolsaCandidato> listaAreaSolicitudDatatable(Map<String, String[]> params, Integer codnum) throws SQLException, UVException {
		List<BolsaCandidato> bolsas = new ArrayList<>();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();	
		BolsaEmpleoDataTable<BolsaCandidato> dataTable = new BolsaEmpleoDataTable<BolsaCandidato>(params);
		
		String consulta =
		"SELECT bepbol.*, bepusuexc.USUARIO "
		+ "FROM TBEP_BOLSAS bepbol "
		+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
		+ "LEFT JOIN UVIRTUAL.TBEP_USUARIOS_EXCLUIDOS_AREA bepusuexc "
		+ "ON bepare.CODNUM = bepusuexc.AREA AND bepusuexc.USUARIO = ? "
		+ "WHERE bepbol.FLGBAREMABLE = 'S' ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_SOLICITUDES, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA_SOLICITUDES, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_EXCLUIDO_SOLICITUDES, "bepusuexc.USUARIO", DataTableColumn.COLUMN_TYPE_IS_NULL);
		dataTable.setColumn(ORDER_COLUMN_INDEX_BAREMABLE_SOLICITUDES, "bepbol.FLGBAREMABLE", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, codnum);
			stmtCount.setInt(indexParam++, codnum);
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
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
					Boolean excluido = rs.getString("USUARIO") != null;
					BolsaCandidato bolsaCandidato = new BolsaCandidato(bolsa, excluido);
					bolsas.add(bolsaCandidato);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bolsas);
		}
		
		return dataTable;
	}
	
//	/** Seleccionar bolsas .
//	 * @param bolsas .
//	 * @param solicitud .
//	 * @throws UVException .
//	 * @throws SQLException .
//	 */
//	public void seleccionarBolsasSolicitud(List<String> bolsas, Integer solicitud) throws SQLException, UVException {
//		if (solicitud == null) {
//			throw new UVException("No se puede incluir bolsas en la solicitud sin el id de la solicitud");
//		}
//		
//		String params = BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size());
//		String consulta = "INSERT INTO TBEP_SOLICITUD_BOLSAS (BEPBOL_CODNUM, BEPSOL_CODNUM)"
//				+ " SELECT bepbol.CODNUM AS BEPBOL_CODNUM, bepsol.CODNUM AS BEPSOL_CODNUM"
//				+ " FROM TBEP_BOLSAS bepbol, TBEP_SOLICITUDES bepsol WHERE bepsol.CODNUM = ? AND "
//				+ " bepbol.CODNUM IN (" + params + ")";
//		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
//			int indexParam = 1;
//			stmt.setInt(indexParam++, solicitud);
//			for (String bolsa: bolsas) {
//				stmt.setString(indexParam++, bolsa);		
//			}
//			stmt.executeUpdate();
//		}
//	}
//	
	/**
	 * Devuelve si el usuario está excluido de la area.
	 * @param usuario .
	 * @param area .
	 * @return true o false si está excluido o no
	 * @throws SQLException .
	 */
	public Boolean isUsuarioExcluidoBolsa(UsuarioBolsaEmpleo usuario, Area area) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM TBEP_USUARIOS_EXCLUIDOS_AREA WHERE USUARIO = ? AND AREA = ?";		
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(query);) {
			int param = 1;
			stmt.setInt(param++, usuario.getCodNum());
			stmt.setInt(param++, area.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				
				if (rs.getInt("count") > 0) {
					return true;
				}
			}
		}
		
		return false;
	}
}
