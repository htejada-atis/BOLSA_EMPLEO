package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;


/**
 * Clase de modelo para la gestión de noticias 
 * Modelo - Operaciones con nombres: lista, inserta
 * Controlador - Opers. con nombres: obtener, agregar
 * 
 * @author jlopez
 *
 */
public class ModeloNoticia {
	
	public static final int ORDER_COLUMN_INDEX_FECHA = 0;
	public static final int ORDER_COLUMN_INDEX_TEXTO = 1;
	public static final int ORDER_COLUMN_INDEX_ENLACE = 2;
	public static final int ORDER_COLUMN_INDEX_FLGPUBLICA = 3;
	public static final int ORDER_COLUMN_INDEX_FLGACTIVA = 4;
	public static final int ORDER_COLUMN_INDEX_ID = 5;
	
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS   ********************************************/

	/** Consulta noticias en BBDD y las devuelve.
	 * @param clausula para filtrar las noticias de la bd .
	 * @param id id de la noticia .
	 * @return todas las noticias de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	private List<Noticia> listaNoticias(String clausula, Integer id) throws SQLException {
		List<Noticia> noticias = new ArrayList<>();
		String consulta = "SELECT n.* FROM tbep_noticias n ";
		
		if (id != null) {
			consulta += "WHERE codnum = ?";
		}
		
		consulta += clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			if (id != null) {
				int parameterIndex = 1; 
				stmt.setInt(parameterIndex++, id);
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					try {
						Noticia not = new Noticia();
						not.setCodNum(rs.getInt("CODNUM"));
						not.setEnlace(rs.getString("ENLACE"));
						not.setTexto(rs.getString("TEXTO"));
						not.setFecha(rs.getTimestamp("FECHA"));
						not.setPublica(rs.getString("FLGPUBLICA").equals("S"));
						not.setActiva(rs.getString("FLGACTIVA").equals("S"));
						noticias.add(not);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			}
		return noticias;
	}
	
	/** obtiene una noticia a partir de su id.
	 * @param id codigo de la noticia
	 * @return noticia con el id especificado
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de error de parametros .
	 */
	public Noticia listaNoticia(Integer id) throws SQLException, UVException {
		if (id == null) {
			throw new UVException("No se puede listar una noticia sin id");
		}
		
		List<Noticia> noticias = listaNoticias("", id);
		if (noticias.isEmpty()) {
			throw new UVException("No existe noticia");
		}
		return noticias.get(0);
	}
	
	/** lista todas las noticias.
	 * @return lista de todas las noticias
	 * @throws SQLException si hay un error en la base de datos
	 */
	public List<Noticia> listaNoticias() throws SQLException {
		return listaNoticias(" ORDER BY fecha", null);
	}
	
	/** Consulta noticias en BBDD y las devuelve.
	 * @param anonimo .
	 * @return las 3 noticias más recientes de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Noticia> listaNoticiasInicio(boolean anonimo) throws SQLException {
		return listaNoticias("WHERE flgactiva = 'S' " + (anonimo ? " AND flgpublica = 'S' " : "")
				+ " ORDER BY fecha FETCH FIRST 3 ROWS ONLY", null);
	}
	
	/**
	 * Listado de noticias . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de noticias
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe noticia
	 */
	public BolsaEmpleoDataTable<Noticia> listaNoticiasDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<Noticia> noticias = new ArrayList<>();
		BolsaEmpleoDataTable<Noticia> dataTable = new BolsaEmpleoDataTable<Noticia>(params);
		
		String consulta = "SELECT bepnot.* FROM tbep_noticias bepnot WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA, "bepnot.FECHA", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ENLACE, "bepnot.ENLACE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_TEXTO, "bepnot.TEXTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_FLGPUBLICA, "bepnot.FLGPUBLICA", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ORDER_COLUMN_INDEX_FLGACTIVA, "bepnot.FLGACTIVA", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Noticia not = new Noticia();
					not.setCodNum(rs.getInt("CODNUM"));
					not.setEnlace(rs.getString("ENLACE"));
					not.setTexto(rs.getString("TEXTO"));
					not.setFecha(rs.getTimestamp("FECHA"));
					not.setPublica(rs.getString("FLGPUBLICA").equals("S"));
					not.setActiva(rs.getString("FLGACTIVA").equals("S"));
					noticias.add(not);
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(noticias);
		}
		
		return dataTable;
	}	
	
	/** lista de noticias excluyendo las ya cargadas.
	 * @param noticiasExcluidas noticias ya mostradas a excluir de la consulta.
	 * @param anonimo .
	 * @return lista de noticias filtradas .
	 * @throws SQLException si hay un error en la base de datos .
	 * @throws UVException en caso de error de parametros .
	 */
	public List<Noticia> listaNoticiasInicioRestantes(List<String> noticiasExcluidas, boolean anonimo) throws SQLException, UVException {
		List<Noticia> noticias = new ArrayList<>();
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(noticiasExcluidas.size());
		String consulta = "SELECT n.* FROM tbep_noticias n WHERE codnum NOT IN (" + params + ") "
				+ (anonimo ? " AND flgpublica = 'S' " : "")
				+ " AND flgactiva = 'S' ORDER BY fecha";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int indexParam = 1;
			for (String noticia: noticiasExcluidas) {
				stmt.setString(indexParam++, noticia);
			}
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					try {
						Noticia not = new Noticia();
						not.setCodNum(rs.getInt("CODNUM"));
						not.setEnlace(rs.getString("ENLACE"));
						not.setTexto(rs.getString("TEXTO"));
						not.setFecha(rs.getTimestamp("FECHA"));
						not.setPublica(rs.getString("FLGPUBLICA").equals("S"));
						not.setActiva(rs.getString("FLGACTIVA").equals("S"));
						noticias.add(not);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return noticias;
	}
	
	/**	Función que inserta una noticia en la BD.
	 * @param noticia a insertar en la BD
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de error de parametros .
	 */
	public void insertaNoticia(Noticia noticia) throws SQLException, UVException {
		if (noticia == null) {
			throw new UVException("No se puede insertar una noticia vacia");
		}
		if (noticia.getTexto() == null || noticia.getTexto().equals("")) {
			throw new UVException("No se puede insertar una noticia sin texto");
		}
		if (noticia.getEnlace() == null) {
			throw new UVException("enlace obligatorio");
		}
		if (noticia.getFecha() == null) {
			throw new UVException("fecha obligatoria");
		}
		
		String consulta = "INSERT INTO tbep_noticias " 
						+ " (ENLACE,TEXTO,FECHA,FLGPUBLICA) "
						+ "VALUES (?, ?, ?, ?)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, noticia.getEnlace());
			stmt.setString(parameterIndex++, noticia.getTexto());
			stmt.setDate(parameterIndex++, new java.sql.Date(noticia.getFecha().getTime()));
			stmt.setString(parameterIndex++, noticia.isPublica() ? "S" : "N");
			stmt.executeUpdate();
		}
	}
	
	/** Elimina una noticia.
	 * @param noticia a borrar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si noticia no es valida
	 */
	public void borraNoticia(Noticia noticia) throws SQLException, UVException {
		if (noticia == null) {
			throw new UVException("No se puede eliminar una noticia vacía");
		}
		if (noticia.getCodNum() == null) {
			throw new UVException("No se puede eliminar una noticia con id vacío");
		}
		String consulta = "DELETE FROM tbep_noticias WHERE codnum = ? ";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, noticia.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Borra o restaura una noticia .
	 * @param noticia a borrar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException si noticia no es valida .
	 */
	public void borraRestauraNoticia(Noticia noticia) throws SQLException, UVException {
		if (noticia == null) {
			throw new UVException("No se puede eliminar una noticia vacía");
		}
		if (noticia.getCodNum() == null) {
			throw new UVException("No se puede eliminar una noticia con id vacío");
		}
		String consulta = "UPDATE tbep_noticias SET flgactiva=? WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, noticia.isActiva() ? "S" : "N");
			stmt.setInt(parameterIndex++, noticia.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Actualiza una noticia.
	 * @param noticia Noticia con los datos nuevos a actualizar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaNoticia(Noticia noticia) throws SQLException, UVException {
		if (noticia == null) {
			throw new UVException("noticia obligatoria");
		}
		if (noticia.getCodNum() == null) {
			throw new UVException("id noticia no válido");
		}
		if (noticia.getEnlace() == null) {
			throw new UVException("enlace obligatorio");
		}
		if (noticia.getTexto() == null) {
			throw new UVException("texto obligatoria");
		}
		if (noticia.getFecha() == null) {
			throw new UVException("fecha obligatoria");
		}
		if (noticia.isPublica() == null) {
			throw new UVException("publica obligatoria");
		}
		
		String consulta = "UPDATE tbep_noticias "
						+ "   SET enlace=?, texto=?, "
						+ "       fecha=?, flgpublica=? "
						+ " WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, noticia.getEnlace());
			stmt.setString(parameterIndex++, noticia.getTexto());
			stmt.setDate(parameterIndex++, new Date(noticia.getFecha().getTime()));
			stmt.setString(parameterIndex++, noticia.isPublica() ? "S" : "N");
			stmt.setInt(parameterIndex++, noticia.getCodNum());
			stmt.executeUpdate();
		}
	}
	
}
