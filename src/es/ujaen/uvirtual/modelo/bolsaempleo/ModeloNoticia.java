package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;


/**
 * Clase de modelo para la gestión de noticias 
 * Modelo - Operaciones con nombres: lista
 * Controlador - Opers. con nombres: obtener
 * 
 * @author jlopez
 *
 */
public class ModeloNoticia {
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS   ********************************************/

	/** Consulta noticias en BBDD y las devuelve.
	 * @return todas las noticias de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Noticia> listaNoticias(JSONArray noticias_iniciales) throws SQLException {
		List<Noticia> noticias = new ArrayList<>();
		String consulta = "SELECT n.* FROM tbep_noticias n WHERE CODNUM not in (";
		
		if(noticias_iniciales.length() == 0) {
			consulta += ")";
		}
		
		for (int i = 0; i < noticias_iniciales.length(); i++) {
			try {
				if(i < noticias_iniciales.length() - 1) {
					consulta += noticias_iniciales.getString(i) + ",";
				} else {
					consulta += noticias_iniciales.getString(i) + ")";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(consulta);
		
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
							not.setIdNoticia(idNoticia);
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
							not.setIdNoticia(idNoticia);
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
	
}
