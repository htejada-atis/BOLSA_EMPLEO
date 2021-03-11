package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.docentia.Convocatoria;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de areas. 
 * @author ATISoluciones
 */
public class ModeloArea {	
	/**
	 * Devuelve un area por su id.
	 * @param idArea id de area
	 * @return area
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si area no es existe
	 */
	public Area getAreaById(int idArea) throws SQLException, UVException {
		String consulta = "SELECT bepare.* FROM TBEP_AREAS bepare WHERE bepare.CODNUM = ?";				
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el area con id " + idArea);
				}
				
				Area area = new Area();
				area.setIdAreaConocimiento(rs.getInt("CODNUM"));
				area.setNombre(rs.getString("NOMBRE"));
				area.setCodigo(rs.getString("CODIGO"));
				
				return area;
			}
		}
	}
	
}
