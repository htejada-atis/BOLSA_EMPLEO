package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.UVException;


/**
 * Clase de modelo para la gestión de roles. 
 * @author ATISoluciones
 */
public class ModeloRol {
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 2;
	public static final int ORDER_COLUMN_INDEX_VALOR = 3;
	
    protected static ModeloRol eInstancia = null;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloRol();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
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
		List<Rol> roles = new ArrayList<Rol>();
		String consulta = "SELECT admrol.* FROM ADM_ROL admrol";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							Rol role = new Rol();
							role.setCodNum(rs.getInt("ROL_CODNUM"));
							role.setDescripcion(rs.getString("DESCRIPCION"));
							role.setValor(rs.getString("VALOR"));
							roles.add(role);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		return roles;
	}
		
	/**
	 * Devuelve un area por su id.
	 * @param codNum id de area
	 * @return area
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si area no es existe
	 */
	public Area getAreaById(int codNum) throws SQLException, UVException {
		ModeloDepartamento modeloDepartamento = ModeloDepartamento.obtenerInstancia();
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
	
	/**
	 * Devuelve un Role por su id.
	 * @param codNum id de rol
	 * @return rol
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si area no es existe
	 */
	public Rol getRoleById(int codNum) throws SQLException, UVException {
		String consulta = "SELECT admrol.* FROM ADM_ROL admrol WHERE admrol.ROL_CODNUM = ?";				
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el role con id " + codNum);
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
