package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de departamentos. 
 * @author ATISoluciones 2021
 */
public class ModeloDepartamento {
	
	public static final String MENSAJE_DEPARTAMENTO_NO_EXISTE = "No existe el departamento con id %";
	
	
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
	
    
    /** Consulta departamentos en BBDD y los devuelve.
	 * @param clausula para filtrar los departamentos de la bd .
	 * @return departamentos de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public List<Departamento> listaDepartamentos() throws SQLException, UVException {
		List<Departamento> departamentos = new ArrayList<>();
		String consulta = "SELECT bepdep.* FROM TBEP_DEPARTAMENTOS bepdep ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
							Departamento dep = new Departamento();
							dep.setCodNum(rs.getInt("CODNUM"));
							dep.setIdDepartamentoExterno(rs.getString("ID_DEPARTAMENTO"));
							dep.setDescripcion(rs.getString("DES_DEPARTAMENTO"));
							departamentos.add(dep);	
					}
				}
			}
		return departamentos;
	}
	
	/**
	 * Devuelve un departamento por su id.
	 * @param idDepartamento id del departamento .
	 * @return departamento .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException si area no es existe .
	 */
	public Departamento getDepartamentoByCodNum(int idDepartamento) throws SQLException, UVException {
		String consulta = "SELECT bepdep.* FROM TBEP_DEPARTAMENTOS bepdep WHERE bepdep.CODNUM = ?";				
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, idDepartamento);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(String.format(MENSAJE_DEPARTAMENTO_NO_EXISTE, idDepartamento));
				}
				
				Departamento dep = new Departamento();
				dep.setCodNum(rs.getInt("CODNUM"));
				dep.setIdDepartamentoExterno(rs.getString("ID_DEPARTAMENTO"));
				dep.setDescripcion(rs.getString("DES_DEPARTAMENTO"));
								
				return dep;
			}
		}
	}
	
	/**
	 * Devuelve un departamento por su id externo.
	 * @param idDepartamento id del departamento .
	 * @return departamento .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException si area no es existe .
	 */
	public Departamento getDepartamentoByIdExterno(String idDepartamento) throws SQLException {
		String consulta = "SELECT bepdep.* FROM TBEP_DEPARTAMENTOS bepdep WHERE bepdep.ID_DEPARTAMENTO = ?";				
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setString(1, idDepartamento);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				
				Departamento dep = new Departamento();
				dep.setCodNum(rs.getInt("CODNUM"));
				dep.setIdDepartamentoExterno(rs.getString("ID_DEPARTAMENTO"));
				dep.setDescripcion(rs.getString("DES_DEPARTAMENTO"));
								
				return dep;
			}
		}
	}
	
	/**	Función que inserta un departamento en la BD.
	 * @param conexion .
	 * @param departamento a insertar en la BD .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public void insertaDepartamento(Connection conexion, Departamento departamento) throws SQLException, UVException {
		if (departamento == null) {
			throw new UVException("No se puede insertar un departamento vacio");
		}
		if (departamento.getIdDepartamentoExterno() == null || departamento.getIdDepartamentoExterno().equals("")) {
			throw new UVException("No se puede insertar un departamento sin id externo");
		}
		
		String consulta = "INSERT INTO TBEP_DEPARTAMENTOS " 
				+ " (ID_DEPARTAMENTO,DES_DEPARTAMENTO)"
				+ " VALUES (?, ?)";
		
		try (PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, departamento.getIdDepartamentoExterno());
			stmt.setString(parameterIndex++, departamento.getDescripcion());
			stmt.executeUpdate();
		}
	}
	
}
