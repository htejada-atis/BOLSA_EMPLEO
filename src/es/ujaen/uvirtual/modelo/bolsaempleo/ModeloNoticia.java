package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
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
	public List<Noticia> listaNoticias(String clausulaWhere) throws SQLException {
		List<Noticia> noticias = new ArrayList<>();
		String consulta = "SELECT n.* FROM tbep_noticias n " + clausulaWhere;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							int idNoticia = rs.getInt("codnum");
							String enlace = rs.getString("enlace");
							String texto = rs.getString("texto");
							Date fecha = rs.getTimestamp("fecha");
							Noticia not = new Noticia();
							not.setCodNum(idNoticia);
							not.setEnlace(enlace);
							not.setTexto(texto);
							not.setFecha(fecha);
							noticias.add(not);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		return noticias;
	}
	
	/** Consulta noticias en BBDD y las devuelve.
	 * @return las 3 noticias más recientes de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Noticia> listaNoticiasIniciales() throws SQLException {
		List<Noticia> noticias = new ArrayList<>();
		String consulta = "SELECT n.* FROM tbep_noticias n ORDER BY fecha FETCH FIRST 3 ROWS ONLY";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							int idNoticia = rs.getInt("codnum");
							String enlace = rs.getString("enlace");
							String texto = rs.getString("texto");
							Date fecha = rs.getTimestamp("fecha");
							Noticia not = new Noticia(); 
							not.setCodNum(idNoticia);
							not.setEnlace(enlace);
							not.setTexto(texto);
							not.setFecha(fecha);
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
	 */
	public Noticia listaNoticia(int id) throws SQLException, UVException {
		String clausulaWhere = "WHERE codnum = " +id;
		List<Noticia> noticias = listaNoticias("");
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
						+ " (ENLACE,TEXTO,FECHA) "
						+ "VALUES (?, ?, ?)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, noticia.getEnlace());
			stmt.setString(parameterIndex++, noticia.getTexto());
			stmt.setDate(parameterIndex++, new java.sql.Date(noticia.getFecha().getTime()));
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
		
		String consulta = "UPDATE tbep_noticias "
						+ "   SET enlace=?, texto=?, "
						+ "       fecha=? "
						+ " WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, noticia.getEnlace());
			stmt.setString(parameterIndex++, noticia.getTexto());
			stmt.setDate(parameterIndex++, new java.sql.Date(noticia.getFecha().getTime()));
			stmt.setInt(parameterIndex++, noticia.getCodNum());
			stmt.executeUpdate();
		}
	}
	
}
