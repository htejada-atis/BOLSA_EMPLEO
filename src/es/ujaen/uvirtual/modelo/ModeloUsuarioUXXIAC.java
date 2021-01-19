package es.ujaen.uvirtual.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.conexion.ConexionUxxiAc;
import es.ujaen.uvirtual.utilidades.AdaptadorDocumentoIdentidad;

/** Clase para el modelo.
 * @author julopez
 *
 */
public class ModeloUsuarioUXXIAC {
    
    public static final String CORREO_ESTUDIANTE = "correoEstudiante";
    public static final String CORREO_PDI = "correoPDI";
    public static final String CORREO_PAS = "correoPAS";
    public static final String CORREO_PAU = "correoPAU";
    
    /********************************************** METODOS PÚBLICOS PARA CONSULTAS *******************************************/
    
    /**
     * Lista los permisos que genera UXXIAC para un código de persona / cuenta de usuario.
     * @param usuario usuario del que obtener los permisos
     * @return lista con los permisos dle usuario
     */
     public ArrayList<String> listaPermisos(Usuario usuario) throws SQLException {
    	 ArrayList<String> grupos = new ArrayList<String>();
    	 String consultaAlumnoOficialCursoActual  
    		= "select count(1) numero_matriculas_curso_actual " 
    	    + "  from uxxiac.vuja_net_plancursoactual " 
    	    + " where alu_dnialu = ? ";
    	 String consultaEsAlumno 
    	 	= "select count(1) numero_matriculas " 
    		+ "  from uxxiac.vuja_net_plaassmatr " 
    		+ " where alu_dnialu = ? ";
    	String consultaAccesoActas 
    		= "select count(1) numero_planes " 
    		+ "  from uxxiac.vuja_net_actas_pas_plan " 
    		+ " where codnum = ? ";
    			
    	try (Connection conexion = ConexionUxxiAc.obtenerInstancia();
    			PreparedStatement stmt = conexion.prepareStatement(consultaAccesoActas);
    			PreparedStatement stmtAlumno = conexion.prepareStatement(consultaEsAlumno);
    			PreparedStatement stmtAlumnoCursoActual = conexion.prepareStatement(consultaAlumnoOficialCursoActual);
    		) {
    		int parameterIndex = 1;
    		if (usuario.getCodigoUXXIAC() != null) {
	    		stmt.setInt(parameterIndex++, usuario.getCodigoUXXIAC().intValue());
	    		try (ResultSet rs = stmt.executeQuery();) {
		    		if (rs.next()) {
		    			if (rs.getInt("numero_planes") > 0) {
		    				grupos.add("actaspas");
		    			}
		    		}
	    		}
    		}
    		
    		if (usuario.getDocumentoNumero() != null) {
    			parameterIndex = 1;
    			stmtAlumno.setString(parameterIndex++, AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usuario));
    			try (ResultSet rsAlumno = stmtAlumno.executeQuery();) {
	    			if (rsAlumno.next()) {
	    				if (rsAlumno.getInt("numero_matriculas") > 0) {
	    					grupos.add("alumnos");
	    				}
	    			}
    			}

    			parameterIndex = 1;
    			stmtAlumnoCursoActual.setString(parameterIndex++, AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usuario));
    			try (ResultSet rsAlumnoCursoActual = stmtAlumnoCursoActual.executeQuery();) {
	    			if (rsAlumnoCursoActual.next()) {
	    				if (rsAlumnoCursoActual.getInt("numero_matriculas_curso_actual") > 0) {
	    					grupos.add("alumnocursoactual");
	    				}
	    			}
    			}
    		}
    	}
    	return grupos;
     }
}
