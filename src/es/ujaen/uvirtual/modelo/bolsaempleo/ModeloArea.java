package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de areas. 
 * @author ATISoluciones
 */
public class ModeloArea {
	
	/** Consulta areas en BBDD y las devuelve.
	 * @param clausula para filtrar las areas de la bd
	 * @return areas de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	private List<Area> listaAreas(String clausula) throws SQLException {
		ModeloDepartamento modeloDepartamento = new ModeloDepartamento();
		List<Area> areas = new ArrayList<>();
		String consulta = "SELECT bepare.* FROM TBEP_AREAS bepare " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							Area are = new Area();
							are.setCodNum(rs.getInt("CODNUM"));
							are.setDepartamento(modeloDepartamento.getDepartamentoById(rs.getInt("BEPDEP_CODNUM")));
							are.setIdAreaExterno(rs.getString("ID_AREA_CONOCIMIENTO"));
							are.setIdSeccion(rs.getString("ID_SECCION"));
							are.setDescripcion(rs.getString("DES_AREA_CONOCIMIENTO"));
							areas.add(are);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		return areas;
	}
	
	/** lista todas las areas.
	 * @return lista de todas las areas
	 * @throws SQLException si hay un error en la base de datos
	 */
	public List<Area> listaAreas() throws SQLException {
		return listaAreas(" ORDER BY DES_AREA_CONOCIMIENTO");
	}
	
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
