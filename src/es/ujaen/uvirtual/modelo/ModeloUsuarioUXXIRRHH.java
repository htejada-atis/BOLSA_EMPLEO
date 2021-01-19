package es.ujaen.uvirtual.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import es.ujaen.uvirtual.beans.uxxirrhh.Cargo;
import es.ujaen.uvirtual.modelo.conexion.ConexionUxxiRrhh;

/**
 * Clase para obtener la información de usuarios implementando el modelo singleton y con cierre de conexiones (no es excesivamente utilizado).
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * @author julopez
 */
@SuppressWarnings("java:S1319")
public class ModeloUsuarioUXXIRRHH {
    /********************************************** METODOS PÚBLICOS PARA CONSULTAS *******************************************/
    /**
     * Obtiene los cargos de un usuario.
     * @param codigoPersona código del usuario en uxxirrhh
     * @return Vector de Cargo con los cargos del usuario
     * @throws SQLException si error en bd
     */
    public ArrayList<Cargo> listaCargos(Integer codigoPersona) throws SQLException {
    	ArrayList<Cargo> cargos = new ArrayList<>();
     	if (codigoPersona == null) {
     		return cargos;
     	}
    	String consultaDeCargos 
    		= "select distinct " 
    		+ "    id_cargo, " 
    		+ "    desc_cargo " 
    		+ "from " 
    		+ "    uxxirrhh.v_ldap_blancas " 
    		+ "where " 
    		+ "    id_cargo is not null " 
    		+ "and cod_persona = ? ";
    		
		try (Connection conexion = ConexionUxxiRrhh.obtenerInstancia();
			PreparedStatement stmtCargo = conexion.prepareStatement(consultaDeCargos);) {
			int parameterIndex = 1;
			stmtCargo.setInt(parameterIndex++, codigoPersona);
			try (ResultSet rsCargo = stmtCargo.executeQuery();) {
				while (rsCargo.next()) {
					Cargo cargo = new Cargo(
							rsCargo.getString("id_cargo"),
							rsCargo.getString("desc_cargo")
					);
					cargos.add(cargo);
				}
			}
		} 
    	return cargos;
    }
    
    /**
     * Lista los permisos del usuario en uxxirrhh.
     * @param codigoPersona código de persona en hominis
     * @return Vector de String con los permisos del usuario
     * @throws SQLException si error en db
     */
    public ArrayList<String> listaPermisos(Integer codigoPersona) throws SQLException {
    	ArrayList<String> grupos = new ArrayList<>();
	   	if (codigoPersona == null) {
	   		return grupos;
	   	}
	   	String consultaGrupos 
	   		= "select cod_persona, decode(pas_sn, 'S', 'pas', 'pdi') grupo " 
	   		+ "  from uxxirrhh.v_intranet_tipo_personal " 
	   		+ " where cod_persona = ? ";
	   	try (Connection conexion = ConexionUxxiRrhh.obtenerInstancia();
	   		PreparedStatement stmt = conexion.prepareStatement(consultaGrupos);) { 
	   		int parameterIndex = 1;
	   		stmt.setInt(parameterIndex++, codigoPersona.intValue());
	   		try (ResultSet rs = stmt.executeQuery();) {
		   		while (rs.next()) {
		   			grupos.add(rs.getString("grupo"));
		   		}
	   		}
	    }
	   	return grupos;
    }
}
