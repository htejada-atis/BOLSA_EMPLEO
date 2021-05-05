package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de departamentos. 
 * @author ATISoluciones
 */
public class ModeloDepartamento {	
	
	
    protected static ModeloDepartamento eInstancia = null;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloDepartamento();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloDepartamento obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }
	
	
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
