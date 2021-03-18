package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
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
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS   ********************************************/

	/** Consulta noticias en BBDD y las devuelve.
	 * @param clausulaWhere clausula para filtrar las noticias de la bd
	 * @return todas las noticias de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	private List<Noticia> listaNoticias(String clausula) throws SQLException {
		List<Noticia> noticias = new ArrayList<>();
		String consulta = "SELECT n.* FROM tbep_noticias n " + clausula;
		System.out.println(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
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
	
	/** lista todas las noticias.
	 * @return vector con todas las convocatorias(abiertas,cerradas y creadas)
	 * @throws SQLException si hay un error en la base de datos
	 */
	public List<Noticia> listaNoticias() throws SQLException {
		return listaNoticias(" ORDER BY fecha");
	}
	
	/** lista todas las noticias.
	 * @return vector con todas las convocatorias(abiertas,cerradas y creadas)
	 * @throws SQLException si hay un error en la base de datos
	 */
	public List<Noticia> listaNoticiasInicioRestantes(String clausula) throws SQLException {
		return listaNoticias(clausula + " AND flgpublica = 'S' ORDER BY fecha");
	}
	
	/** Consulta noticias en BBDD y las devuelve.
	 * @return las 3 noticias más recientes de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Noticia> listaNoticiasInicio() throws SQLException {
		return listaNoticias("WHERE flgpublica = 'S' ORDER BY fecha FETCH FIRST 3 ROWS ONLY");
	}
	
	/** obtiene una noticia a partir de su id.
	 * @param id codigo de la noticia
	 * @return noticia con el id especificado
	 * @throws SQLException en caso de error en la BD
	 */
	public Noticia listaNoticia(int id) throws SQLException, UVException {
		String clausulaWhere = "WHERE codnum = " + id;
		List<Noticia> noticias = listaNoticias(clausulaWhere);
		if (noticias.isEmpty()) {
			throw new UVException("No existe noticia");
		}
		return noticias.get(0);
	}
	
	/**	Función que inserta una noticia en la BD.
	 * @param noticia a insertar en la BD
	 * @throws SQLException en caso de error en la BD
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
			stmt.setBoolean(parameterIndex++, noticia.isPublica());
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
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, noticia.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Actualiza una noticia.
	 * @param noticia Noticia con los datos nuevos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de errores de validacion
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
			stmt.setDate(parameterIndex++, new java.sql.Date(noticia.getFecha().getTime()));
			stmt.setString(parameterIndex++, noticia.isPublica() ? "S" : "N");
			stmt.setInt(parameterIndex++, noticia.getCodNum());
			stmt.executeUpdate();
		}
	}
	
}
