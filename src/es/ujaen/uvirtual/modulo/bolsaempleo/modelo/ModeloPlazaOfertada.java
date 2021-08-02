package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para las plazas ofertadas para la contratación .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloPlazaOfertada {
	
	public static final String PLAZA_ESTADO_ABIERTA = "ABIERTA";
	public static final String PLAZA_ESTADO_CERRADA = "CERRADA";
	public static final String PLAZA_ESTADO_CONTRATACION = "CONTRATACION";
	public static final String PLAZA_ESTADO_CREACION = "BLOQUEADA";
	public static final String PLAZA_ESTADO_TRAMITACION = "TRAMITACION";
	
	public static final String CENTRO_DESTINO_JAEN = "JAEN";
	public static final String CENTRO_DESTINO_LINARES = "LINARES";
	
	public static final String CUATRIMESTRE_PRIMERO = "1º CUATRIMESTRE";
	public static final String CUATRIMESTRE_SEGUNDO = "2º CUATRIMESTRE";
	public static final String CUATRIMESTRE_TODO_EL_CURSO = "TODO EL CURSO";
	
	public static final String MENSAJE_ERROR_OBJETO_VACIO = "No se puede %s una dedicación vacía";
	public static final String MENSAJE_ERROR_PARAM_VACIO = "No se puede %s una dedicación sin %s";
	
	public static final String MENSAJE_ERROR_PLAZA_OFERTADA_ID_NO_EXISTE = "No existe la plaza ofertada con el id indicando";
	
	public static final String CODNUM = "CODNUM";
	
	public static final Map<String, String> CENTROS_DESTINO = new HashMap<>();
	public static final Map<String, String> CUATRIMESTRES = new HashMap<>();
	
	static {
		CENTROS_DESTINO.put(CENTRO_DESTINO_JAEN, "Jaén");
		CENTROS_DESTINO.put(CENTRO_DESTINO_LINARES, "Linares");
		CUATRIMESTRES.put(CUATRIMESTRE_PRIMERO, "1º Cuatrimestre");
		CUATRIMESTRES.put(CUATRIMESTRE_SEGUNDO, "2º Cuatrimestre");
		CUATRIMESTRES.put(CUATRIMESTRE_TODO_EL_CURSO, "Todo el curso");
	}
	
	
	protected static ModeloPlazaOfertada eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloPlazaOfertada();
		}
	}
	
	/**
	 * Obtiene una instancia de la conexión .
	 * @return instancia .
	 */
	public static ModeloPlazaOfertada obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/** Devuelve una plaza ofertada por el id .
	 * @param codNum .
	 * @return plaza ofertada .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public PlazaOfertada getPlazaOfertadaById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "buscar", "id"));
		}
		
		String consulta = String.format("SELECT bepplo.* FROM TBEP_PLAZAS_OFERTADAS bepplo WHERE %s=?", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_PLAZA_OFERTADA_ID_NO_EXISTE);
				}
				
				return createPlazaOfertadaFromResultSet(rs);
			}
		}
	}
	
	
	
	/**
	 * Lista de plazas ofertadas .
	 * @param params .
	 * @return datatable de plazas ofertadas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<PlazaOfertada> listadoPlazasOfertadas(Map<String, String[]> params) throws SQLException, UVException {
		List<PlazaOfertada> rows = new ArrayList<>();
		BolsaEmpleoDataTable<PlazaOfertada> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT * FROM TBEP_PLAZAS_OFERTADAS";
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					rows.add(createPlazaOfertadaFromResultSet(rs));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}
		
		return dataTable;
	}
	
	private PlazaOfertada createPlazaOfertadaFromResultSet(ResultSet rs) throws SQLException, UVException {
		PlazaOfertada plaza = new PlazaOfertada();
		
		plaza.setCodNum(rs.getInt("CODNUM"));
		plaza.setEstado(rs.getString("ESTADO"));
		plaza.setArea(ModeloArea.obtenerInstancia().getAreaById(rs.getInt("BEPARE_CODNUM")));
		plaza.setFechaCreacion(rs.getDate("FECHA_CREACION"));
		plaza.setFechaAbierta(rs.getDate("FECHA_ABIERTA"));
		plaza.setFechaCerrada(rs.getDate("FECHA_CERRADA"));
		
		return plaza;
	}
	
}