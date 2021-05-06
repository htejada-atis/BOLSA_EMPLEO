package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de titulaciones de usuarios . 
 * @author fcampos
 */
public class ModeloMisTitulaciones {

	public static final int ORDER_COLUMN_INDEX_ID = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 1;
	
	public static final int ORDER_COLUMN_INDEX_NOMBRE_USUARIO = 1;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 2;
	
	public static final int COLUMN_DESCRIPCION_MAXLENGTH = 250;
	
	public static final int MAX_FILE_SIZE = 5000000;
	
    protected static ModeloMisTitulaciones eInstancia = null;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMisTitulaciones();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloMisTitulaciones obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }
	
	/** obtiene una titulación a partir de su id.
	 * @param id codigo de la titulación
	 * @return titulación con el id especificado
	 * @throws SQLException en caso de error en la BD
	 */
	public Titulacion listaTitulacionUsuario(int id) throws SQLException, UVException {
		String clausulaWhere = "WHERE codnum = " + id;
		List<Titulacion> titulaciones = listaTitulacionesUsuarios(clausulaWhere);
		if (titulaciones.isEmpty()) {
			throw new UVException("No existe titulación");
		}
		return titulaciones.get(0);
	}
	
	/** obtiene una titulación a partir de su id.
	 * @param id codigo de la titulación
	 * @return titulación con el id especificado
	 * @throws SQLException en caso de error en la BD
	 */
	public Titulacion listaTitulacion(int id) throws SQLException, UVException {
		String clausulaWhere = "WHERE codnum = " + id;
		List<Titulacion> titulaciones = listaTitulaciones(clausulaWhere);
		if (titulaciones.isEmpty()) {
			throw new UVException("No existe titulación");
		}
		return titulaciones.get(0);
	}
	
	/** Consulta titulaciones en BBDD y las devuelve.
	 * @return todas las titulaciones de la base de datos .
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Titulacion> listaTitulaciones() throws SQLException {
		List<Titulacion> titulaciones = new ArrayList<>();
		String consulta = "SELECT * FROM TBEP_TITULACIONES_USUARIO";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						Titulacion tit = new Titulacion();
						tit.setCodNum(rs.getInt("CODNUM"));
						tit.setDescripcion(rs.getString("DESCRIPCION"));
						tit.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
						tit.setBorrado(rs.getString("FLGBORRADO").equals("S"));
						titulaciones.add(tit);	
					}
				}
			}
		
		return titulaciones;
	}
	
	
	/** Consulta titulaciones en BBDD y las devuelve.
	 * @param clausula para filtrar las titulaciones de la bd
	 * @return todas las titulaciones de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	private List<Titulacion> listaTitulacionesUsuarios(String clausula) throws SQLException {
		List<Titulacion> titulaciones = new ArrayList<>();
		String consulta = "SELECT beptus.* FROM tbep_titulaciones_usuario beptus " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						Titulacion tit = new Titulacion();
						tit.setCodNum(rs.getInt("CODNUM"));
						tit.setDescripcion(rs.getString("DESCRIPCION"));
						tit.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
						titulaciones.add(tit);
					}
				}
			}
		
		return titulaciones;
	}
	
	
	/** Consulta titulaciones en BBDD y las devuelve.
	 * @param clausula para filtrar las titulaciones de la bd
	 * @return todas las titulaciones de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	private List<Titulacion> listaTitulaciones(String clausula) throws SQLException {
		List<Titulacion> titulaciones = new ArrayList<>();
		String consulta = "SELECT beptit.* FROM tbep_titulaciones beptit " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						Titulacion tit = new Titulacion();
						tit.setCodNum(rs.getInt("CODNUM"));
						tit.setDescripcion(rs.getString("NOMBRE"));
						titulaciones.add(tit);
					}
				}
			}
		
		return titulaciones;
	}
	
	/**
	 * Listado de titulaciones en una tabla .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param codnum .
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public BolsaEmpleoDataTable<Titulacion> listaTitulacionesDatatable(Map<String, String[]> params, Integer codnum) throws SQLException, UVException {
		List<Titulacion> titulaciones = new ArrayList<>();
		BolsaEmpleoDataTable<Titulacion> dataTable = new BolsaEmpleoDataTable<Titulacion>(params);
		
		String consulta = "SELECT beptit.*, beptus.*"
				+ "FROM tbep_titulaciones beptit "
				+ "LEFT JOIN tbep_titulaciones_usuario beptus "
				+ "ON beptit.CODNUM = beptus.BEPTUS_TIT_CODNUM AND beptus.BEPTUS_USU_CODNUM = ? "
				+ "AND beptus.FLGBORRADO != 'S' "
				+ "WHERE beptus.BEPTUS_USU_CODNUM IS NULL";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "beptit.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE, "beptit.NOMBRE");
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
					Titulacion tit = new Titulacion();
					tit.setCodNum(rs.getInt("CODNUM"));
					tit.setNombre(rs.getString("NOMBRE"));
					titulaciones.add(tit);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(titulaciones);
		}
		
		return dataTable;
	}
	
	
	/**
	 * Listado de titulaciones de un usuario en una tabla .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param codNum .
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public BolsaEmpleoDataTable<Titulacion> listaTitulacionesUsuarioDatatable(Map<String, String[]> params, Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("No se pueden listar titulaciones sin usuario");
		}
		
		List<Titulacion> titulaciones = new ArrayList<>();
		BolsaEmpleoDataTable<Titulacion> dataTable = new BolsaEmpleoDataTable<Titulacion>(params);
		
		String consulta = "SELECT beptus.codnum as beptuscod,BEPTUS.DESCRIPCION AS BEPTUSDESCRIPCION, beptit.* FROM tbep_titulaciones_usuario beptus "
				+ "INNER JOIN tbep_titulaciones beptit ON beptit.codnum=beptus.beptus_tit_codnum "
				+ "WHERE beptus.BEPTUS_USU_CODNUM = ? AND beptus.FLGBORRADO != 'S'";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_USUARIO, "beptit.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "BEPTUSDESCRIPCION");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			
			int indexParam = 1;
			stmt.setInt(indexParam, codNum);
			stmtCount.setInt(indexParam++, codNum);
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Titulacion tit = new Titulacion();
					tit.setCodNum(rs.getInt("beptuscod"));
					tit.setNombre(rs.getString("NOMBRE"));
					tit.setDescripcion(rs.getString("BEPTUSDESCRIPCION"));
					titulaciones.add(tit);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(titulaciones);
		}
		
		return dataTable;
	}
	
	/**
	 * Devuelve un listado de las titulaciones de un usuario por su id.
	 * @param ids codnum de titulaciones
	 * @return listado de titulaciones
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<Titulacion> getTitulacionesUsuarios(int[] ids) throws SQLException, UVException {
		List<Titulacion> titulaciones = new ArrayList<>();
		
		for (int i = 0; i < ids.length; i++) {
			titulaciones.add(this.listaTitulacionUsuario(ids[i]));
	    }
		
		return titulaciones;
	}
	
	/**	Función que inserta una titulacion de usuario en la BD.
	 * @param titulacion a insertar en la BD
	 * @throws SQLException en caso de error en la BD
	 * @throws IOException .
	 */
	public void insertaTitulacionUsuario(Titulacion titulacion) throws SQLException, UVException, IOException {
		if (titulacion == null) {
			throw new UVException("No se puede insertar una titulación vacia");
		}
		if (titulacion.getArchivo() == null) {
			throw new UVException("No se puede insertar una titulación sin archivo");
		}
		Integer espacio = titulacion.getArchivo().available();
		if (titulacion.getArchivo().available() > MAX_FILE_SIZE) {
			throw new UVException("No se puede insertar un archivo tan grande");
		}
		
		String consulta = "INSERT INTO tbep_titulaciones_usuario " 
						+ " (BEPTUS_TIT_CODNUM,BEPTUS_USU_CODNUM,DESCRIPCION,ARCHIVO) "
						+ "VALUES (?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, titulacion.getTitulacion().getCodNum());
			stmt.setInt(parameterIndex++, titulacion.getUsuario().getCodNum());
			stmt.setString(parameterIndex++, titulacion.getDescripcion());
			stmt.setBinaryStream(parameterIndex++, titulacion.getArchivo());
			stmt.executeUpdate();
		}
	}
	
	
	
	/** Elimina una titulación de un usuario .
	 * @param titulaciones a borrar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si titulación no es valida
	 */
	public void borraTitulacionUsuario(List<Titulacion> titulaciones) throws SQLException, UVException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(titulaciones.size());
		String query = "UPDATE TBEP_TITULACIONES_USUARIO SET FLGBORRADO = ?, FECHA_BORRADO = ? WHERE CODNUM IN  (" + params + ")";		
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query);) {
			int parameterIndex = 1;

			stmt.setString(parameterIndex++, "S");
			
			Date date = new Date(System.currentTimeMillis());
			
			stmt.setDate(parameterIndex++, new java.sql.Date(date.getTime()));
			for (Titulacion titulacion : titulaciones) {
				stmt.setInt(parameterIndex++, titulacion.getCodNum()); 
			}
			stmt.executeUpdate();
		}
	}
	
}
