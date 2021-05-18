package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.utilidades.UVException;


/**
 * Clase de modelo para la gestión de roles. 
 * @author ATISoluciones 2021
 */
public class ModeloRol {
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 2;
	public static final int ORDER_COLUMN_INDEX_VALOR = 3;
	
	public static final Integer ID_ROL_SERVICIO_PERSONAL = 1050;
	public static final Integer ID_ROL_MIEMBRO_COMISION = 1051;
	public static final Integer ID_ROL_CANDIDATO = 1052;
	public static final Integer ID_ROL_DIRECTOR_DEPARTAMENTO = 1053;
	
	public static final String ROL_SERVICIO_PERSONAL = "bolemppersonal";	
	public static final String ROL_MIEMBRO_COMISION = "bolempcomision";
	public static final String ROL_CANDIDATO = "bolempcandidato";
	public static final String ROL_DIRECTOR_DEPARTAMENTO = "bolempdirdepartamento";
	
	public static final String ERROR_ROL_REQUERIDO = "El rol es requerido";
	public static final String ERROR_ROL_NO_EXISTE = "El rol no existe";

	protected static ModeloRol eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloRol();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloRol obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/** Consulta areas en BBDD y las devuelve.
	 * @return areas de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Rol> listaRoles() throws SQLException {
		List<Rol> roles = new ArrayList<>();
		String consulta = "SELECT admrol.* FROM ADM_ROL admrol WHERE ROL_CODNUM IN (?,?,?,?)";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			
			stmt.setInt(paramIndex++, ModeloRol.ID_ROL_SERVICIO_PERSONAL);
			stmt.setInt(paramIndex++, ModeloRol.ID_ROL_MIEMBRO_COMISION);
			stmt.setInt(paramIndex++, ModeloRol.ID_ROL_CANDIDATO);
			stmt.setInt(paramIndex++, ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Rol role = new Rol();
					role.setCodNum(rs.getInt("ROL_CODNUM"));
					role.setDescripcion(rs.getString("DESCRIPCION"));
					role.setValor(rs.getString("VALOR"));
					roles.add(role);
				}
			}
		}
		return roles;
	}
		
	/**
	 * Devuelve un Role por su id.
	 * @param codNum id de rol
	 * @return rol
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si area no es existe
	 */
	public Rol getRoleById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(ERROR_ROL_REQUERIDO);
		}
			
		String consulta = "SELECT admrol.* FROM ADM_ROL admrol WHERE admrol.ROL_CODNUM = ?";				
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(ERROR_ROL_NO_EXISTE);
				}
				
				Rol role = new Rol();
				role.setCodNum(rs.getInt("ROL_CODNUM"));
				role.setDescripcion(rs.getString("DESCRIPCION"));
				role.setValor(rs.getString("VALOR"));
				
				return role;
			}
		}
	}
}
