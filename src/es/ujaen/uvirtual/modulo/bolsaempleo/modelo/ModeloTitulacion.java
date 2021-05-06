package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de titulaciones. 
 * @author ATISoluciones 2021
 */
public class ModeloTitulacion {
	public static final int ORDER_COLUMN_INDEX_ID = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 1;
	
	public static final int ORDER_COLUMN_INDEX_ID_SELECTABLE = 1;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_SELECTABLE = 2;
	
	public static final int ORDER_COLUMN_INDEX_NOMBRE_CANDIDATO = 0;
	
	public static final int COLUMN_NOMBRE_MAXLENGTH = 150;
	
	public static final String ERROR_TITULACION_NOEXITE = "No existe la titulación";
	public static final String ERROR_TITULACION_REQUERIDA = "La titulación es requerida";
	
    protected static ModeloTitulacion eInstancia = null;
    	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloTitulacion();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloTitulacion obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
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
						tit.setNombre(rs.getString("NOMBRE"));
						titulaciones.add(tit);
						
					}
				}
			}
		return titulaciones;
	}
	
	/** lista todas las titulaciones.
	 * @return lista de todas las titulaciones
	 * @throws SQLException si hay un error en la base de datos
	 */
	public List<Titulacion> listaTitulaciones() throws SQLException {
		return listaTitulaciones(" ORDER BY nombre");
	}
	
	/** lista todas las titulaciones del candidato .
	 * @param usuario .
	 * @return lista de todas las titulaciones .
	 * @throws SQLException si hay un error en la base de datos .
	 */
	public List<Titulacion> listaTitulacionesCandidato(Integer usuario) throws SQLException {
		List<Titulacion> titulaciones = new ArrayList<>();
		String consulta = "SELECT beptit.* FROM TBEP_TITULACIONES beptit"
				+ " INNER JOIN TBEP_TITULACIONES_USUARIO beptus ON beptus.BEPTUS_TIT_CODNUM = beptit.CODNUM"
				+ " WHERE beptus.BEPTUS_USU_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, usuario);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Titulacion tit = new Titulacion();
					tit.setCodNum(rs.getInt("CODNUM"));
					tit.setNombre(rs.getString("NOMBRE"));
					titulaciones.add(tit);
				}
			}
		}
		return titulaciones;
	}
	
	/** lista todas las titulaciones validadas del candidato .
	 * @param usuario .
	 * @return lista de todas las titulaciones .
	 * @throws SQLException si hay un error en la base de datos .
	 */
	public List<Titulacion> listaTitulacionesValidadasCandidato(Integer usuario) throws SQLException {
		List<Titulacion> titulaciones = new ArrayList<>();
		String consulta = "SELECT beptit.* FROM TBEP_TITULACIONES beptit"
				+ " INNER JOIN TBEP_TITULACIONES_USUARIO beptus ON beptus.BEPTUS_TIT_CODNUM = beptit.CODNUM"
				+ " WHERE beptus.BEPTUS_USU_CODNUM = ? AND beptus.FLGVALIDADA = 'S'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, usuario);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Titulacion tit = new Titulacion();
					tit.setCodNum(rs.getInt("CODNUM"));
					tit.setNombre(rs.getString("NOMBRE"));
					titulaciones.add(tit);
				}
			}
		}
		return titulaciones;
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
	
	/** Actualiza una titulación.
	 * @param titulacion Titulación con los datos nuevos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de errores de validacion
	 */
	public void actualizaTitulacion(Titulacion titulacion) throws SQLException, UVException {
		if (titulacion == null) {
			throw new UVException("titulación obligatoria");
		}
		if (titulacion.getCodNum() == null) {
			throw new UVException("id titulación no válido");
		}
		if (titulacion.getNombre() == null) {
			throw new UVException("nombre obligatorio");
		}
		
		String consulta = "UPDATE tbep_titulaciones "
						+ "   SET nombre=?"
						+ " WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, titulacion.getNombre());
			stmt.setInt(parameterIndex++, titulacion.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Valida una titulación .
	 * @param titulacion .
	 * @param candidato .
	 * @param date .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void validaTitulacion(Titulacion titulacion, UsuarioBolsaEmpleo candidato, java.util.Date date) throws SQLException, UVException {
		if (titulacion == null) {
			throw new UVException("titulación obligatoria");
		}
		if (titulacion.getCodNum() == null) {
			throw new UVException("id titulación no válido");
		}
		if (candidato == null) {
			throw new UVException("candidato obligatorio");
		}
		if (candidato.getCodNum() == null) {
			throw new UVException("id usuario no válido");
		}
		
		String consulta = "UPDATE TBEP_TITULACIONES_USUARIO "
				+ "	SET FLGVALIDADA = 'S', FECHA_VALIDADA = ?"
				+ " WHERE BEPTUS_TIT_CODNUM = ? AND BEPTUS_USU_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setDate(parameterIndex++, new Date(date.getTime()));
			stmt.setInt(parameterIndex++, titulacion.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Valida una titulación a false .
	 * @param titulacion .
	 * @param candidato .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desvalidaTitulacion(Titulacion titulacion, UsuarioBolsaEmpleo candidato) throws SQLException, UVException {
		if (titulacion == null) {
			throw new UVException("titulación obligatoria");
		}
		if (titulacion.getCodNum() == null) {
			throw new UVException("id titulación no válido");
		}
		if (candidato == null) {
			throw new UVException("candidato obligatorio");
		}
		if (candidato.getCodNum() == null) {
			throw new UVException("id usuario no válido");
		}
		
		String consulta = "UPDATE TBEP_TITULACIONES_USUARIO "
				+ " SET FLGVALIDADA='N', FECHA_VALIDADA=null"
				+ " WHERE BEPTUS_TIT_CODNUM = ? AND BEPTUS_USU_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, titulacion.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Elimina una titulación .
	 * @param titulacion a borrar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si titulación no es valida
	 */
	public void borraTitulacion(Titulacion titulacion) throws SQLException, UVException {
		if (titulacion == null) {
			throw new UVException("No se puede eliminar una titulación vacía");
		}
		if (titulacion.getCodNum() == null) {
			throw new UVException("No se puede eliminar una titulación con id vacío");
		}
		String consulta = "DELETE FROM tbep_titulaciones WHERE codnum = ? ";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, titulacion.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**	Función que inserta una titulacion en la BD.
	 * @param titulacion a insertar en la BD
	 * @throws SQLException en caso de error en la BD
	 */
	public void insertaTitulacion(Titulacion titulacion) throws SQLException, UVException {
		if (titulacion == null) {
			throw new UVException("No se puede insertar una titulación vacia");
		}
		if (titulacion.getNombre() == null || titulacion.getNombre().equals("")) {
			throw new UVException("No se puede insertar una titulación sin nombre");
		}
		
		String consulta = "INSERT INTO tbep_titulaciones " 
						+ " (NOMBRE) "
						+ "VALUES (?)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, titulacion.getNombre());
			stmt.executeUpdate();
		}
	}
	
	/**	Función que inserta titulaciones preferentes a un area .
	 * @param titulaciones a insertar en las titulaciones preferentes por area .
	 * @param area id del area por el que se va a filtrar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parámetros .
	 */
	public void incluirTitulacionesPreferentesArea(List<String> titulaciones, Integer area) throws SQLException, SQLIntegrityConstraintViolationException, UVException {
		
		if (area == null) {
			throw new UVException("No se puede incluir titulación sin el id del área");
		}
		
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(titulaciones.size());
		String consulta = "INSERT INTO TBEP_TITULACIONES_PREFERENTES_AREA (BEPARE_CODNUM, BEPTIT_CODNUM)"
				+ " SELECT bepare.CODNUM AS BEPARE_CODNUM, beptit.CODNUM AS BEPTIT_CODNUM"
				+ " FROM TBEP_TITULACIONES beptit, TBEP_AREAS bepare WHERE bepare.CODNUM = ? AND "
				+ " beptit.CODNUM IN (" + params + ")";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, area);
			for (String titulacion: titulaciones) {
				stmt.setString(indexParam++, titulacion);		
			}
			stmt.executeUpdate();
		}
	}
	
	/**	Función que elimina titulaciones preferentes a un area .
	 * @param titulaciones a eliminar en las titulaciones preferentes por area .
	 * @param area id del area por el que se va a filtrar .
	 * @throws SQLException en caso de error en la BD .
	 */
	public void eliminarTitulacionesPreferentesArea(List<String> titulaciones, Integer area) throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(titulaciones.size());
		String consulta = "DELETE FROM TBEP_TITULACIONES_PREFERENTES_AREA WHERE BEPARE_CODNUM = ? AND BEPTIT_CODNUM IN (" + params + ")";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, area);
			for (String titulacion: titulaciones) {
				stmt.setString(indexParam++, titulacion);	
			}
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Listado de titulaciones en una tabla .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public BolsaEmpleoDataTable<Titulacion> listaTitulacionesDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<Titulacion> titulaciones = new ArrayList<>();
		BolsaEmpleoDataTable<Titulacion> dataTable = new BolsaEmpleoDataTable<Titulacion>(params);
		
		String consulta = "SELECT beptit.* FROM tbep_titulaciones beptit WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "beptit.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE, "beptit.NOMBRE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
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
	 * Listado de titulaciones en una tabla excluyendo las de un área .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param area id del area por el que se va a filtrar
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public BolsaEmpleoDataTable<Titulacion> listaTitulacionesDatatable(Map<String, String[]> params, Integer area) throws SQLException, UVException {
		
		if (area == null) {
			throw new UVException("No se pueden listar titulaciones sin el id del área");
		}
		
		List<Titulacion> titulaciones = new ArrayList<>();
		BolsaEmpleoDataTable<Titulacion> dataTable = new BolsaEmpleoDataTable<Titulacion>(params);
		
		String consultaNotIn = "SELECT beptit.CODNUM FROM tbep_titulaciones beptit "
				+ "INNER JOIN tbep_titulaciones_preferentes_area beptpa ON beptit.codnum = beptpa.beptit_codnum "
				+ "INNER JOIN TBEP_AREAS bepare ON bepare.codnum = beptpa.bepare_codnum "
				+ "WHERE bepare.CODNUM = ? ";
		String consulta = "SELECT beptit.* FROM tbep_titulaciones beptit WHERE beptit.CODNUM NOT IN (" + consultaNotIn + ")";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_SELECTABLE, "beptit.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_SELECTABLE, "beptit.NOMBRE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, area);
			stmtCount.setInt(indexParam++, area);
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
	 * Listado de titulaciones de un area en una tabla .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param area id del area por el que se va a filtrar
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public BolsaEmpleoDataTable<TitulacionArea> listaTitulacionesAreaDatatable(Map<String, String[]> params, Integer area) throws SQLException, UVException {
		
		if (area == null) {
			throw new UVException("No se pueden listar titulaciones sin el id del área");
		}
		
		List<TitulacionArea> titulaciones = new ArrayList<>();
		BolsaEmpleoDataTable<TitulacionArea> dataTable = new BolsaEmpleoDataTable<TitulacionArea>(params);
		
		String consulta = "SELECT beptit.CODNUM, beptit.NOMBRE FROM tbep_titulaciones beptit "
				+ "INNER JOIN tbep_titulaciones_preferentes_area beptpa ON beptit.codnum = beptpa.beptit_codnum "
				+ "INNER JOIN TBEP_AREAS bepare ON bepare.codnum = beptpa.bepare_codnum "
				+ "WHERE bepare.CODNUM = ? ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_SELECTABLE, "beptit.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_SELECTABLE, "beptit.NOMBRE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, area);
			stmtCount.setInt(indexParam++, area);
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Titulacion tit = new Titulacion();
					tit.setCodNum(rs.getInt("CODNUM"));
					tit.setNombre(rs.getString("NOMBRE"));
					ModeloArea modeloArea = ModeloArea.obtenerInstancia();
					titulaciones.add(new TitulacionArea(tit, modeloArea.getAreaById(area)));
				}
			}
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(titulaciones);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado de titulaciones de un area en una tabla .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param area id del area por el que se va a filtrar
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public BolsaEmpleoDataTable<TitulacionArea> listaTitulacionesAreaCandidatoDatatable(Map<String, String[]> params, Integer area) throws SQLException, UVException {
		
		if (area == null) {
			throw new UVException("No se pueden listar titulaciones sin el id del área");
		}
		
		List<TitulacionArea> titulaciones = new ArrayList<>();
		BolsaEmpleoDataTable<TitulacionArea> dataTable = new BolsaEmpleoDataTable<TitulacionArea>(params);
		
		String consulta = "SELECT beptit.CODNUM, beptit.NOMBRE FROM tbep_titulaciones beptit "
				+ "INNER JOIN tbep_titulaciones_preferentes_area beptpa ON beptit.codnum = beptpa.beptit_codnum "
				+ "INNER JOIN TBEP_AREAS bepare ON bepare.codnum = beptpa.bepare_codnum "
				+ "WHERE bepare.CODNUM = ? ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_CANDIDATO, "beptit.NOMBRE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, area);
			stmtCount.setInt(indexParam++, area);
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Titulacion tit = new Titulacion();
					tit.setCodNum(rs.getInt("CODNUM"));
					tit.setNombre(rs.getString("NOMBRE"));
					ModeloArea modeloArea = ModeloArea.obtenerInstancia();
					titulaciones.add(new TitulacionArea(tit, modeloArea.getAreaById(area)));
				}
			}
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(titulaciones);
		}
		
		return dataTable;
	}
	
	/**
	 * Devuelve una titulación por su código.
	 * @param codNum .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Titulacion getTitulacionById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(ERROR_TITULACION_REQUERIDA);
		} 
		
		String consulta = "SELECT beptit.* FROM TBEP_TITULACIONES beptit WHERE beptit.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Titulacion t = new Titulacion();
					t.setCodNum(rs.getInt("CODNUM"));
					t.setNombre(rs.getString("NOMBRE"));
					
					return t;
				}
			}
		}
		
		throw new UVException(ERROR_TITULACION_NOEXITE);	
	}
}
