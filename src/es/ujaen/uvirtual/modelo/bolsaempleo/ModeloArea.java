package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de areas. 
 * @author ATISoluciones
 */
public class ModeloArea {	
	/**
	 * Devuelve un area por su id.
	 * @param codNum id de area
	 * @return area
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si area no es existe
	 */
	public Area getAreaById(int codNum) throws SQLException, UVException {
		ModeloDepartamento modeloDepartamento = new ModeloDepartamento();
		String consulta = "SELECT bepare.* FROM TBEP_AREAS bepare WHERE bepare.CODNUM = ?";				
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el area con id " + codNum);
				}
				
				Area area = new Area();
				area.setCodNum(rs.getInt("CODNUM"));
				area.setDepartamento(modeloDepartamento.getDepartamentoById(rs.getInt("BEPDEP_CODNUM")));
				area.setIdAreaExterno(rs.getString("ID_AREA_CONOCIMIENTO"));
				area.setIdSeccion(rs.getString("ID_SECCION"));
				area.setDescripcion(rs.getString("DES_AREA_CONOCIMIENTO"));
				
				return area;
			}
		}
	}
	
}
