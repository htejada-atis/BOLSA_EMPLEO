package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Plantilla;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de plantillas .
 * 
 * @author ATISoluciones
 */
public class ModeloPlantilla {
	
	public static final int PLANTILLAS_COLUMN_INDEX_CODNUM = 0;
	public static final int PLANTILLAS_COLUMN_INDEX_NOMBRE = 1;
	
	public static final int PLANTILLAS_COLUMN_TITULO_MAXLENGTH = 500;
	
	public static final String CUERPO = "CUERPO";
	public static final String CODNUM = "CODNUM";
	public static final String NOMBRE = "NOMBRE";
	public static final String TITULO = "TITULO";
	
	public static final String MENSAJE_ERROR_NO_EXISTE_PLANTILLA = "No existe la plantilla";
	public static final String MENSAJE_ERROR_PLANTILLA_NULL = "No se puede insertar una plantilla vacía";
	
	protected static ModeloPlantilla eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloPlantilla();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloPlantilla obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
		
	/** Listado de plantillas .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de plantillas .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 * @throws IOException .
	 */
	public BolsaEmpleoDataTable<Plantilla> listaPlantillasDatatable(Map<String, String[]> params) throws SQLException, UVException, IOException {
		List<Plantilla> mensajes = new ArrayList<>();
		BolsaEmpleoDataTable<Plantilla> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT beppls.* FROM TBEP_PLANTILLAS beppls";

		dataTable.setColumn(PLANTILLAS_COLUMN_INDEX_CODNUM, CODNUM);
		dataTable.setColumn(PLANTILLAS_COLUMN_INDEX_NOMBRE, NOMBRE);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			dataTable.setFiltersParams(stmt, stmtCount, 1);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					mensajes.add(createPlantillaFromResultSet(rs));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(mensajes);
		}
		
		return dataTable;
	}
	
	/** Devuelve una plantilla por su id.
	 * @param codNum .
	 * @return plantilla o null si no existe
	 * @throws IOException .
	 * @throws SQLException .
	 */
	public Plantilla getPlantillaById(Integer codNum) throws SQLException, UVException, IOException {
		if (codNum == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_PLANTILLA);
		}
		String consulta = "SELECT beppls.* FROM TBEP_PLANTILLAS beppls WHERE beppls.CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_PLANTILLA);
				}
				
				return this.createPlantillaFromResultSet(rs);
			}
		}
	}

	/** Añade una plantilla .
	 * @param plantilla .
	 * @param usuarioUpdate .
	 * @return id plantilla creada .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Integer nuevaPlantilla(Plantilla plantilla, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plantilla == null) {
			throw new UVException(MENSAJE_ERROR_PLANTILLA_NULL);
		}
		
		String consulta = String.format("INSERT INTO TBEP_PLANTILLAS (%s,%s,%s,%s) VALUES (?,?,?,?)", TITULO, CUERPO, NOMBRE, "UID_USUARIO");
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta, new String[] {CODNUM})) {
			int indexParam = 1;
			
			stmt.setString(indexParam++, plantilla.getTitulo());
			stmt.setClob(indexParam++, BolsaEmpleoUtils.stringToClob(plantilla.getCuerpo(), conexion));
			stmt.setString(indexParam++, plantilla.getNombre());
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		}
	}
	
	/** Actualiza una plantilla .
	 * @param plantilla .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void actualizarPlantilla(Plantilla plantilla, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plantilla == null) {
			throw new UVException(MENSAJE_ERROR_PLANTILLA_NULL);
		}
		
		String consulta = String.format("UPDATE TBEP_PLANTILLAS SET %s=?, %s=?, %s=?, %s=? WHERE %s=?", TITULO, CUERPO, NOMBRE, "UID_USUARIO", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setString(indexParam++, plantilla.getTitulo());
			stmt.setClob(indexParam++, BolsaEmpleoUtils.stringToClob(plantilla.getCuerpo(), conexion));
			stmt.setString(indexParam++, plantilla.getNombre());
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, plantilla.getCodNum());
			stmt.executeUpdate();
		}
	}

	private Plantilla createPlantillaFromResultSet(ResultSet rs) throws SQLException, IOException {
		Plantilla plantilla = new Plantilla();
		plantilla.setCodNum(rs.getInt(CODNUM));
		plantilla.setTitulo(rs.getString(TITULO));
		plantilla.setCuerpo(BolsaEmpleoUtils.clobToString(rs.getClob(CUERPO)));
		return plantilla;
	}
	
}
