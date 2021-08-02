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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Dedicacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para las plazas ofertadas para la contratación .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloDedicacion {
	
	public static final int ORDER_COLUMN_INDEX_ID = 0;
	public static final int ORDER_COLUMN_INDEX_TEXTO = 1;
	public static final int ORDER_COLUMN_INDEX_SUELDO = 2;
	public static final int ORDER_COLUMN_INDEX_FECHA_VIGENCIA = 3;
	public static final int ORDER_COLUMN_INDEX_ACTIVA = 4;
	
	public static final String MENSAJE_ERROR_DEDICACION_ID_NO_EXISTE = "No existe la dedicación con el id indicando";
	public static final String MENSAJE_ERROR_OBJETO_VACIO = "No se puede %s una dedicación vacía";
	public static final String MENSAJE_ERROR_PARAM_VACIO = "No se puede %s una dedicación sin %s";
	
	public static final String CODNUM = "CODNUM";
	public static final String FECHA_VIGENCIA = "FECHA_VIGENCIA";
	public static final String FLGACTIVA = "FLGACTIVA";
	public static final String SUELDO = "SUELDO";
	public static final String TEXTO = "TEXTO";
	
	private static final String ACTIVA = "S";
	private static final String INACTIVA = "N";
	
	
	protected static ModeloDedicacion eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloDedicacion();
		}
	}

	/**
	 * Obtiene una instancia de la conexión .
	 * @return instancia .
	 */
	public static ModeloDedicacion obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/** Consulta dedicaciones en BBDD y las devuelve .
	 * @param clausula para filtrar las dedicaciones de la bd .
	 * @return dedicaciones de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 */
	private List<Dedicacion> listaDedicaciones(String clausula) throws SQLException {
		List<Dedicacion> listaDedicaciones = new ArrayList<>();
		String consulta = "SELECT bepded.* FROM TBEP_DEDICACIONES bepded " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					listaDedicaciones.add(createDedicacionFromResultSet(rs));
				}
			}
		}
		
		return listaDedicaciones;
	}
	
	/** Devuelve una lista con todas las dedicaciones activas de la base de datos .
	 * @return dedicaciones de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 */
	public List<Dedicacion> listaDedicacionesActivas() throws SQLException {
		return listaDedicaciones(String.format("WHERE %s = %s", FLGACTIVA, ACTIVA));
	}
	
	public Dedicacion getDedicacionById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "buscar", "id"));
		}
		
		String consulta = String.format("SELECT bepded.* FROM TBEP_DEDICACIONES bepded WHERE %s=?", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_DEDICACION_ID_NO_EXISTE);
				}
				
				return createDedicacionFromResultSet(rs);
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
	public BolsaEmpleoDataTable<Dedicacion> listadoDedicaciones(Map<String, String[]> params) throws SQLException, UVException {
		List<Dedicacion> rows = new ArrayList<>();
		BolsaEmpleoDataTable<Dedicacion> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepded.* FROM TBEP_DEDICACIONES bepded WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, CODNUM, DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_TEXTO, TEXTO);
		dataTable.setColumn(ORDER_COLUMN_INDEX_SUELDO, SUELDO, DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA_VIGENCIA, FECHA_VIGENCIA, DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTIVA, FLGACTIVA, DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					rows.add(createDedicacionFromResultSet(rs));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}
		
		return dataTable;
	}
	
	/** inserta dedicación .
	 * @param dedicacion .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void insertaDedicacion(Dedicacion dedicacion, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (dedicacion == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "insertar"));
		}
		
		if (dedicacion.getTexto() == null || dedicacion.getTexto().isBlank()) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "insertar", "texto"));
		}
		
		if (dedicacion.getSueldo() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "insertar", "sueldo"));
		}
		
		String consulta = String.format("INSERT INTO TBEP_DEDICACIONES (%s,%s,%s,%s) VALUES (?,?,?,?)", TEXTO, SUELDO, FECHA_VIGENCIA, "UID_USUARIO");
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, dedicacion.getTexto());
			stmt.setDouble(parameterIndex++, dedicacion.getSueldo());
			stmt.setDate(parameterIndex++, new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();
		}
	}
	
	/** actualiza dedicación .
	 * @param dedicacion .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void actualizaDedicacion(Dedicacion dedicacion, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (dedicacion == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "actualizar"));
		}
		
		if (dedicacion.getTexto() == null || dedicacion.getTexto().isBlank()) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "actualizar", "texto"));
		}
		
		if (dedicacion.getSueldo() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "actualizar", "sueldo"));
		}
		
		if (dedicacion.getFechaVigencia() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "actualizar", "vigencia"));
		}
		
		String consulta = String.format("UPDATE TBEP_DEDICACIONES SET %s=?, %s=?, %s=?, %s=? WHERE %s=?", TEXTO, SUELDO, FECHA_VIGENCIA, "UID_USUARIO", "CODNUM");
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, dedicacion.getTexto());
			stmt.setDouble(parameterIndex++, dedicacion.getSueldo());
			stmt.setDate(parameterIndex++, new Date(dedicacion.getFechaVigencia().getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, dedicacion.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** actualiza activa dedicación .
	 * @param dedicacion .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void actualizaActivaDedicacion(Dedicacion dedicacion, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (dedicacion == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "actualizar"));
		}
		
		if (dedicacion.isActiva() == null) {
			throw new UVException(String.format(MENSAJE_ERROR_PARAM_VACIO, "actualizar", "activa"));
		}
		
		String consulta = String.format("UPDATE TBEP_DEDICACIONES SET %s=?, %s=? WHERE %s=?", FLGACTIVA, "UID_USUARIO", "CODNUM");
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, dedicacion.isActiva() ? ACTIVA : INACTIVA);
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, dedicacion.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	private Dedicacion createDedicacionFromResultSet(ResultSet rs) throws SQLException {
		Dedicacion dedicacion = new Dedicacion();
		dedicacion.setCodNum(rs.getInt(CODNUM));
		dedicacion.setTexto(rs.getString(TEXTO));
		dedicacion.setSueldo(rs.getDouble(SUELDO));
		dedicacion.setFechaVigencia(rs.getDate(FECHA_VIGENCIA));
		dedicacion.setActiva(rs.getString(FLGACTIVA).equals(ACTIVA));
		
		return dedicacion;
	}
	
}
