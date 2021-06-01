package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modelo.conexion.ConexionUxxiRrhh;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de areas. 
 * @author ATISoluciones 2021
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
		
	public static final String CODNUM = "CODNUM";
	public static final String BEPARE_CODNUM = "BEPARE_CODNUM";
	public static final String ESTADO = "ESTADO";
	
	public static final String MENSAJE_ERROR_AREA_REQUERIDA = "El área es requerida";
	
	protected static ModeloArea eInstancia;
	
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
	 * @throws UVException .
	 */
	private List<Area> listaAreas(String clausula) throws SQLException {
		List<Area> areas = new ArrayList<>();
		String consulta = "SELECT bepare.* FROM TBEP_AREAS bepare " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Area are = createAreaFromResulSet(rs);
					areas.add(are);
				}
			}
		}
		return areas;
	}

	/** lista todas las areas.
	 * @return lista de todas las areas
	 * @throws SQLException si hay un error en la base de datos
	 * @throws UVException .
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
	public Area getAreaById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(MENSAJE_ERROR_AREA_REQUERIDA);
		}
			
		String consulta = "SELECT bepare.* FROM TBEP_AREAS bepare WHERE bepare.CODNUM = ?";				
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el area con id " + codNum);
				}
				
				Area area = new Area();
				area.setCodNum(rs.getInt(CODNUM));
				area.setIdAreaExterno(rs.getString("ID_AREA_CONOCIMIENTO"));
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
	 * Devuelve las areas de un departamento.
	 * @param dep .
	 * @return .
	 * @throws SQLException .
	 */
	public List<Area> getAreasByDepartamento(Departamento dep) throws SQLException {
		ArrayList<Area> areas = new ArrayList<>();
		String consulta = 
				"   SELECT bepare.* FROM TBEP_AREAS bepare "
				+ " INNER JOIN TBEP_AREAS_DEPARTAMENTOS bepade ON bepade.BEPARE_CODNUM = bepare.CODNUM "
				+ " WHERE bepade.BEPDEP_CODNUM = ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam, dep.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					areas.add(this.createAreaFromResulSet(rs));					
				}
			}			
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
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepbol.* FROM TBEP_BOLSAS bepbol "
		+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
		+ "WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepbol.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BAREMALE, "bepbol.FLGBAREMABLE", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = new Bolsa();
					bolsa.setCodNum(rs.getInt(CODNUM));
					bolsa.setArea(modeloArea.getAreaById(rs.getInt(BEPARE_CODNUM)));
					bolsa.setEstado(rs.getString(ESTADO));
					bolsa.setBaremable("S".equals(rs.getString("FLGBAREMABLE")));		
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
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<>(params);
		
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
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = new Bolsa();
					bolsa.setCodNum(rs.getInt(CODNUM));
					bolsa.setArea(modeloArea.getAreaById(rs.getInt(BEPARE_CODNUM)));
					bolsa.setEstado(rs.getString(ESTADO));
					bolsa.setBaremable("S".equals(rs.getString("FLGBAREMABLE")));			
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
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta =
		"SELECT bepbol.* "
		+ "FROM TBEP_BOLSAS bepbol "
		+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM AND bepbol.FLGBAREMABLE = 'S' "
		+ "LEFT JOIN TBEP_USU_EXCLUIDOS_AREA bepuea "
		+ "ON bepare.CODNUM = bepuea.AREA AND bepuea.USUARIO = ? "
		+ "WHERE bepuea.USUARIO IS NULL";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepare.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BAREMALE, "bepbol.FLGBAREMABLE", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {					
		
			int indexParam = 1;
			stmt.setInt(indexParam, codnum);
			stmtCount.setInt(indexParam++, codnum);
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = new Bolsa();
					bolsa.setCodNum(rs.getInt(CODNUM));
					bolsa.setArea(modeloArea.getAreaById(rs.getInt(BEPARE_CODNUM)));
					bolsa.setEstado(rs.getString(ESTADO));
					bolsa.setBaremable("S".equals(rs.getString("FLGBAREMABLE")));					
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
		BolsaEmpleoDataTable<BolsaCandidato> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta =
		"SELECT bepbol.*, bepuea.USUARIO "
		+ " FROM TBEP_BOLSAS bepbol"
		+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
		+ " LEFT JOIN TBEP_USU_EXCLUIDOS_AREA bepuea "
		+ " ON bepare.CODNUM = bepuea.AREA AND bepuea.USUARIO = ?"
		+ " WHERE bepbol.FLGBAREMABLE = 'S' ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO_SOLICITUDES, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA_SOLICITUDES, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_EXCLUIDO_SOLICITUDES, "bepuea.USUARIO", DataTableColumn.COLUMN_TYPE_IS_NULL);
		dataTable.setColumn(ORDER_COLUMN_INDEX_BAREMABLE_SOLICITUDES, "bepbol.FLGBAREMABLE", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, codnum);
			stmtCount.setInt(indexParam++, codnum);
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = new Bolsa();
					bolsa.setCodNum(rs.getInt(CODNUM));
					bolsa.setArea(modeloArea.getAreaById(rs.getInt(BEPARE_CODNUM)));
					bolsa.setEstado(rs.getString(ESTADO));
					bolsa.setBaremable("S".equals(rs.getString("FLGBAREMABLE")));
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

	/**
	 * Devuelve si el usuario está excluido de la area.
	 * @param usuario .
	 * @param area .
	 * @return true o false si está excluido o no
	 * @throws SQLException .
	 */
	public boolean isUsuarioExcluidoBolsa(UsuarioBolsaEmpleo usuario, Area area) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM TBEP_USU_EXCLUIDOS_AREA WHERE USUARIO = ? AND AREA = ?";		
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(query)) {
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// MÉTODOS PARA IMPORTAR ÁREAS DE UVIRTUAL
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// método para convertir lista de areas en un string con los ids separados por comas .
	private String obtenerStringIdsAreas(List<Area> areas) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < areas.size(); i++) {
			builder.append("'" + areas.get(i).getIdAreaExterno() + "',");
		}
		return builder.deleteCharAt(builder.length() - 1).toString();
	}
	
	// método para convertir lista de areas en un string con las descripciones separadas por comas .
	private String obtenerStringDescsAreas(List<Area> areas) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < areas.size(); i++) {
			builder.append("'" + areas.get(i).getDescripcion() + "',");
		}
		return builder.deleteCharAt(builder.length() - 1).toString();
	}
	
	// método para convertir lista de departamentos en un string con los ids separados por comas .
	private String obtenerStringIdsDepartamentos(List<Departamento> departamentos) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < departamentos.size(); i++) {
			builder.append("'" + departamentos.get(i).getIdDepartamentoExterno() + "',");
		}
		return builder.deleteCharAt(builder.length() - 1).toString();
	}
	
	/** Agrega las áreas de UVirtual que no están en el sistema .
	 * @param usuario .
	 * @return total de áreas nuevas insertadas .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Integer insertarAreasNuevasExternas(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		Integer totalInsertadas = 0;
				
		// leemos las areas externas omitiendo aquellas que ya existen en el sistema .
		String consulta = "SELECT * FROM UXXIRRHH.VUJA_NET_BEP_RH_DEPTO_SECC_AREA uvnbrdsa WHERE uvnbrdsa.ID_AREA_CONOCIMIENTO NOT IN ("
				+ obtenerStringIdsAreas(this.listaAreas("")) + ")";
		
		try (Connection conexionUxxiRrhh = ConexionUxxiRrhh.obtenerInstancia(); PreparedStatement stmt = conexionUxxiRrhh.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String idArea = rs.getString("ID_AREA_CONOCIMIENTO");
					String descArea = rs.getString("DES_AREA_CONOCIMIENTO");
					String idDepartamento = rs.getString("ID_DEPARTAMENTO");
					String descDepartamento = rs.getString("DES_DEPARTAMENTO");
					String idSeccion = rs.getString("ID_SECCION");
					String descSeccion = rs.getString("DES_SECCION");
					
					try (Connection conexionUvirtual = ConexionUvirtual.obtenerInstancia()) {
						conexionUvirtual.setAutoCommit(false);
						
						try {
							// insertamos el área
							Area area = insertarAreaSiNoExiste(conexionUvirtual, idArea, descArea, usuario);
							if (area != null) {
								// insertamos el departamento si no existe en la base de datos interna
								Departamento departamento = insertarDepartamentoSiNoExiste(
										conexionUvirtual, idDepartamento, descDepartamento, usuario);
								
								// insertamos la relación de área y departamento
								insertaAreaDepartamento(conexionUvirtual, area, departamento, idSeccion, descSeccion, usuario);
								
								totalInsertadas++;
							}
							
							conexionUvirtual.commit();
							conexionUvirtual.setAutoCommit(true);
						} catch (SQLException e) {
							conexionUvirtual.rollback();
							conexionUvirtual.setAutoCommit(true);
							throw e;
						}
					}
				}
			}
		}
		
		return totalInsertadas;
	}
	
	/** Actualiza las áreas del sistema con las de UVirtual .
	 * @param usuario .
	 * @return total de áreas nuevas actualizadas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Integer actualizaAreasDeExternas(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		Integer total = 0;
		
		try (Connection conexionUxxiRrhh = ConexionUxxiRrhh.obtenerInstancia()) {
			conexionUxxiRrhh.setAutoCommit(false);
			
			List<Area> areasInternas = this.listaAreas("");
			List<Departamento> departamentosInternos = ModeloDepartamento.obtenerInstancia().listaDepartamentos();
			
			try {
				total += actualizaAreasPorDescripcion(conexionUxxiRrhh, areasInternas, usuario);
				total += actualizaAreasPorDepartamento(conexionUxxiRrhh, areasInternas, departamentosInternos, usuario);
				
				conexionUxxiRrhh.commit();
				conexionUxxiRrhh.setAutoCommit(true);
			} catch (SQLException e) {
				conexionUxxiRrhh.rollback();
				conexionUxxiRrhh.setAutoCommit(true);
				throw e;
			}
		}
		
		return total;
	}
	
	/** Función que actualiza un área en la BD.
	 * @param area .
	 * @param usuario .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void actualizaArea(Area area, UsuarioBolsaEmpleo usuario) throws SQLException {
		String consulta = "UPDATE TBEP_AREAS SET ID_AREA_CONOCIMIENTO=?, DES_AREA_CONOCIMIENTO=?, UID_USUARIO=? WHERE ID_AREA_CONOCIMIENTO = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, area.getIdAreaExterno());
			stmt.setString(parameterIndex++, area.getDescripcion());
			stmt.setString(parameterIndex++, usuario.getCodCuenta());
			stmt.setString(parameterIndex++, area.getIdAreaExterno());
			stmt.executeUpdate();
		}
	}
	
	/** Función que compara la áreas del sistema con las de uvirtual y actualiza sus descripciones en caso de no coincidir .
	 * @param conexion .
	 * @param areasInternas .
	 * @param usuario .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private Integer actualizaAreasPorDescripcion(Connection conexion, List<Area> areasInternas, UsuarioBolsaEmpleo usuario) throws SQLException {
		Integer total = 0;
		String consulta = "SELECT * FROM UXXIRRHH.VUJA_NET_BEP_RH_DEPTO_SECC_AREA uvnbrdsa"
				+ "	WHERE uvnbrdsa.ID_AREA_CONOCIMIENTO IN ("
				+ obtenerStringIdsAreas(areasInternas) + ")"
				+ "	AND uvnbrdsa.DES_AREA_CONOCIMIENTO NOT IN ("
				+ obtenerStringDescsAreas(areasInternas) + ")";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Area area = new Area();
					area.setIdAreaExterno(rs.getString("ID_AREA_CONOCIMIENTO"));
					area.setDescripcion(rs.getString("DES_AREA_CONOCIMIENTO"));
					this.actualizaArea(area, usuario);
					total++;
				}
			}
		}
		
		return total;
	}
	
	/** Función que compara la áreas del sistema con las de uvirtual y actualiza sus departamentos en caso de no coincidir .
	 * @param conexion .
	 * @param areasInternas .
	 * @param departamentosInternos .
	 * @param usuario .
	 * @return total de áreas actualizadas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private Integer actualizaAreasPorDepartamento(Connection conexion, List<Area> areasInternas, List<Departamento> departamentosInternos, UsuarioBolsaEmpleo usuario) 
			throws SQLException, UVException {
		Integer total = 0;
		
		String consulta = "SELECT * FROM UXXIRRHH.VUJA_NET_BEP_RH_DEPTO_SECC_AREA uvnbrdsa"
				+ "	WHERE uvnbrdsa.ID_AREA_CONOCIMIENTO IN ("
				+ obtenerStringIdsAreas(areasInternas) + ")"
				+ "	AND uvnbrdsa.ID_DEPARTAMENTO NOT IN ("
				+ obtenerStringIdsDepartamentos(departamentosInternos) + ")";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String idArea = rs.getString("ID_AREA_CONOCIMIENTO");
					String descArea = rs.getString("DES_AREA_CONOCIMIENTO");
					String idDepartamento = rs.getString("ID_DEPARTAMENTO");
					String descDepartamento = rs.getString("DES_DEPARTAMENTO");
					String idSeccion = rs.getString("ID_SECCION");
					String descSeccion = rs.getString("DES_SECCION");
					
					try (Connection conexionUvirtual = ConexionUvirtual.obtenerInstancia()) {
						conexionUvirtual.setAutoCommit(false);
						
						try {
							// insertamos el área
							Area area = insertarAreaSiNoExiste(conexionUvirtual, idArea, descArea, usuario);
							if (area != null) {
								// insertamos el departamento si no existe en la base de datos interna
								Departamento departamento = insertarDepartamentoSiNoExiste(
										conexionUvirtual, idDepartamento, descDepartamento, usuario);
								
								// insertamos la relación de área y departamento
								insertaAreaDepartamento(conexionUvirtual, area, departamento, idSeccion, descSeccion, usuario);
								total++;
							}
							
							conexionUvirtual.commit();
							conexionUvirtual.setAutoCommit(true);
						} catch (SQLException e) {
							conexionUvirtual.rollback();
							conexionUvirtual.setAutoCommit(true);
							throw e;
						}
					}
				}
			}
		}
		
		return total;
	}
	
	/**	Función que inserta relación entre área y departamento en la BD.
	 * @param conexion .
	 * @param area a insertar en la BD .
	 * @param departamento .
	 * @param idSeccion .
	 * @param desSeccion .
	 * @param usuario .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	private void insertaAreaDepartamento(Connection conexion, Area area, Departamento departamento, String idSeccion, String desSeccion, UsuarioBolsaEmpleo usuario) 
			throws SQLException {
		String consulta = "INSERT INTO TBEP_AREAS_DEPARTAMENTOS (BEPARE_CODNUM,BEPDEP_CODNUM,ID_SECCION,DES_SECCION,UID_USUARIO) VALUES (?,?,?,?,?)";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, area.getCodNum());
			stmt.setInt(parameterIndex++, departamento.getCodNum());
			stmt.setString(parameterIndex++, idSeccion);
			stmt.setString(parameterIndex++, desSeccion);
			stmt.setString(parameterIndex++, usuario.getCodCuenta());
			stmt.executeUpdate();
		}
	}
	
	/**	Función que inserta un área en la BD.
	 * @param conexion .
	 * @param area a insertar en la BD .
	 * @param usuario .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	private void insertaArea(Connection conexion, Area area, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (area == null) {
			throw new UVException("No se puede insertar un área vacio");
		}
		if (area.getIdAreaExterno() == null || "".equals(area.getIdAreaExterno())) {
			throw new UVException("No se puede insertar un área sin id externo");
		}
		
		String consulta = "INSERT INTO TBEP_AREAS (ID_AREA_CONOCIMIENTO,DES_AREA_CONOCIMIENTO,UID_USUARIO) VALUES (?, ?, ?)";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, area.getIdAreaExterno());
			stmt.setString(parameterIndex++, area.getDescripcion());
			stmt.setString(parameterIndex++, usuario.getCodCuenta());
			stmt.executeUpdate();
		}
	}
	
	/** Función que comprueba si existe un departamento en el sistema y en caso de que no exista lo crea .
	 * @param conexion .
	 * @param idDepartamentoExterno .
	 * @param descDepartamentoExterno .
	 * @param usuario .
	 * @return departamento .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private Departamento insertarDepartamentoSiNoExiste(Connection conexion, String idDepartamentoExterno, String descDepartamentoExterno, UsuarioBolsaEmpleo usuario) 
			throws SQLException, UVException {
		ModeloDepartamento modeloDepartamento = ModeloDepartamento.obtenerInstancia();
		Departamento departamento = modeloDepartamento.getDepartamentoByIdExterno(idDepartamentoExterno);
		
		if (departamento == null) {
			departamento = new Departamento();
			departamento.setIdDepartamentoExterno(idDepartamentoExterno);
			departamento.setDescripcion(descDepartamentoExterno);
			modeloDepartamento.insertaDepartamento(conexion, departamento, usuario);
			departamento = modeloDepartamento.getDepartamentoByIdExterno(idDepartamentoExterno);
		}
		
		return departamento;
	}
	
	/** Función que comprueba si existe un área en el sistema y en caso de que no exista la crea .
	 * @param conexion .
	 * @param idAreaExterna .
	 * @param descAreaExterna .
	 * @param usuario .
	 * @return area .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private Area insertarAreaSiNoExiste(Connection conexion, String idAreaExterna, String descAreaExterna, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		Area area = getAreaByIdExterno(conexion, idAreaExterna);
		
		if (area == null) {
			area = new Area();
			area.setIdAreaExterno(idAreaExterna);
			area.setDescripcion(descAreaExterna);
			this.insertaArea(conexion, area, usuario);
			area = getAreaByIdExterno(conexion, idAreaExterna);
		}
		
		return area;
	}
	
	/** Devuelve un area por su id externo.
	 * @param conexion .
	 * @param idExterno id externo del area .
	 * @return area .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException si area no es existe .
	 */
	private Area getAreaByIdExterno(Connection conexion, String idExterno) throws SQLException {
		String consulta = "SELECT bepare.* FROM TBEP_AREAS bepare WHERE bepare.ID_AREA_CONOCIMIENTO = ?";				
			
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, idExterno);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				
				return this.createAreaFromResulSet(rs);
			}
		}
	}
	
	private Area createAreaFromResulSet(ResultSet rs) throws SQLException {
		Area are = new Area();
		are.setCodNum(rs.getInt(CODNUM));
		are.setIdAreaExterno(rs.getString("ID_AREA_CONOCIMIENTO"));
		are.setDescripcion(rs.getString("DES_AREA_CONOCIMIENTO"));
		return are;
	}
	
}
