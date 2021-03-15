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
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Departamento;
import es.ujaen.uvirtual.beans.uvirtual.docentia.Convocatoria;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de departamentos. 
 * @author ATISoluciones
 */
public class ModeloDepartamento {	
	/**
	 * Devuelve un departamento por su id.
	 * @param idDepartamento id del departamento
	 * @return departamento
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si area no es existe
	 */
	public Departamento getDepartamentoById(int idDepartamento) throws SQLException, UVException {
		String consulta = "SELECT bepdep.* FROM TBEP_DEPARTAMENTOS bepdep WHERE bepdep.CODNUM = ?";				
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, idDepartamento);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el departamento con id " + idDepartamento);
				}
				
				Departamento dep = new Departamento();
				dep.setCodNum(rs.getInt("CODNUM"));
				dep.setIdDepartamentoExterno(rs.getString("ID_DEPARTAMENTO"));
				dep.setDescripcion(rs.getString("DES_DEPARTAMENTO"));
								
				return dep;
			}
		}
	}
	
}
