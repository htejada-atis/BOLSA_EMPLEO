package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Noticia> listaNoticias() throws SQLException {
		List<Noticia> noticias = new ArrayList<>();
		String consulta = "SELECT n.* FROM tbep_noticias n";
		System.out.println(consulta);
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						System.out.println("enlace: " +rs.getString("enlace"));
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
							System.out.println("se añade bien");
						} catch(Exception e) {
							e.printStackTrace();
							System.out.println("no se añade bien");
						}
						
					}
				}
			}
		return noticias;
	}
	
}
