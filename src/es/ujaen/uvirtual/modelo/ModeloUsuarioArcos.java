package es.ujaen.uvirtual.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.conexion.ConexionArcos;

/**
 * Acceso a la base de datos de arcos.
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * 20130225 - julopez - Añade listado de permisos de arcos
 * 
 * @author julopez
 *
 */
public class ModeloUsuarioArcos {
    
    // Tipos de cuenta, se utiliza para devolver el correo de un usuario a partir de su DNI y del perfil indicado
	public static final String CUENTA_BACHILLERATO = "bachillerato";
	public static final String CUENTA_ESTUDIANTE = "estudiante";
	public static final String CUENTA_PAS = "pas";
	public static final String CUENTA_PASPDI = "paspdi";
	public static final String CUENTA_PAU = "pau";
	public static final String CUENTA_PDI = "pdi";
    
    ///********************************************** METODOS PÚBLICOS PARA CONSULTAS *******************************************/
	
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity", 
		"checkstyle:JavaNCSS", "checkstyle:ExecutableStatementCount", "checkstyle:MethodName"})
	private List<Usuario> _listaCuentaUsuarios(String where, List<String> params) throws SQLException {
    	List<Usuario> listaUsuarios = new ArrayList<>();
    	String consultaDeCuenta 
    		= "select " 
    		+ "    c.ididentificador, " 
    		+ "    p.strnombre, " 
    		+ "    p.strapellido1, " 
    		+ "    p.strapellido2, " 
    		+ "    p.numdocumento, " 
    		+ "    p.strtipodocumento, " 
    		+ "    p.codint, " 
    		+ "    p.cod_persona_rh, " 
    		+ "    p.cod_persona_ac, " 
    		+ "    p.blnmanual, " 
    		+ "    c.idcuenta, " 
    		+ "    c.iddominio, " 
    		+ "    c.blnbloqueada, " 
    		+ "    c.strdn, " 
    		+ "    c.blninstitucional, " 
    		+ "    c.strdescripcion, " 
    		+ "    p.sexo, " 
    		+ "    c.strcorreoruta, " 
    		+ "    c.blnctaggl, " 
    		+ "    c.blnctagglsusp " 
    		+ "from " 
    		+ "    arcos.v_persona_intranet p, " 
    		+ "    arcos.v_cuentas_intranet c " 
    		+ "where " 
    		+ "    c.codint = p.codint " 
    		+ "  and " + where;
    	
		try (Connection conexion = ConexionArcos.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consultaDeCuenta)) {
			int parameterIndex = 1;
			for (String param : params) {
				stmt.setString(parameterIndex++, param);
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Usuario usuario = new Usuario();
					usuario.setUid(rs.getString("ididentificador"));
					usuario.setCuentaInstitucional("S".equals(rs.getString("blninstitucional")));
					usuario.setCuentaBloqueada("S".equals(rs.getString("blnbloqueada")));
					usuario.setDominio(rs.getString("iddominio"));
					usuario.setCuentaDN(rs.getString("strdn"));
					usuario.setCodigoCuentaArcos((rs.getString("idcuenta") == null) ? null : (rs.getInt("idcuenta")));
					usuario.setCorreoRuta(rs.getString("strcorreoruta"));
					usuario.setCuentaGoogle("S".equals(rs.getString("blnctaggl")));
					usuario.setCuentaGoogleSuspendida("S".equals(rs.getString("blnctagglsusp")));
					if (usuario.isCuentaInstitucional()) {
						usuario.setNombre(rs.getString("strdescripcion"));
						usuario.setApellido1(""); // Por que el primer apellido se espera como obligatorio algunas veces
						usuario.setDocumentoNumero(rs.getString("ididentificador"));
						usuario.setDocumentoTipo("OTR");
					} else {
						usuario.setNombre(rs.getString("strnombre"));
						usuario.setApellido1(rs.getString("strapellido1"));
						usuario.setApellido2(rs.getString("strapellido2"));
						usuario.setDocumentoNumero(rs.getString("numdocumento"));
						usuario.setDocumentoTipo(rs.getString("strtipodocumento"));
						usuario.setCodigoPersonaArcos((rs.getString("codint") == null) ? null : (rs.getInt("codint")));
						usuario.setCodigoRRHH((rs.getString("cod_persona_rh") == null) ? null : (rs.getInt("cod_persona_rh")));
						usuario.setCodigoUXXIAC((rs.getString("cod_persona_ac") == null) ? null : (rs.getInt("cod_persona_ac")));
						usuario.setPersonaManual("S".equals(rs.getString("blnmanual")));
						usuario.setSexo(rs.getString("sexo"));
					}
					listaUsuarios.add(usuario);
				}	
			}
		} 
    	return listaUsuarios;
    }
    
    /**
     * Obtiene los datos de una cuenta de usuario (sólo datos básicos, no de grupos). 
     * @param uid uid
     * @return el Usuario con el uid indicado, en caso de no existir, nulo
     * @throws SQLException en caso de error de acceso a las bases de datos
     */
    public Usuario listaCuentaUsuario(String uid) throws SQLException {
    	Usuario usuario = null;
    	List<String> parametros = new ArrayList<>();
    	parametros.add(uid);
    	List<Usuario> listaUsuarios = _listaCuentaUsuarios("c.ididentificador = ? ", parametros);
    	if (listaUsuarios.size() == 1) {
    		return listaUsuarios.get(0);
    	}
    	return usuario;
    }
    
    /**
     * Lista los permisos del usuario en arcos.
     * @param codigoCuenta código de cuenta en arcos
     * @return Vector de String con los permisos del usuario
     * @throws SQLException si error en bd
     */
    @SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity"})
    public List<String> listaPermisos(Integer codigoCuenta) throws SQLException {
    	List<String> grupos = new ArrayList<>();
   	 	if (codigoCuenta == null) {
   	 		return grupos;
   	 	}
	   	String consultaGrupos 
	   		= "select " 
	   		+ "    c.blnctaggl, " 
	   		+ "    c.blnctagglsusp, " 
	   		+ "    c.iddominio, " 
	   		+ "    c.blninstitucional " 
	   		+ "from " 
	   		+ "    arcos.v_cuentas_intranet c " 
	   		+ "where " 
	   		+ "    c.idcuenta = ? ";
	   	 
	   	String consultaResporganicos 
	   		= "select count(1) es_responsableorganico" 
	   		+ "  from arcos.v_resporganico_intranet ro" 
	   	   	+ " where ro.idcuenta = ? ";   	 
	   	 
	   	 String consultaServiciosActivos 
	   	 	= "select s.ididentificador, "  
	   		+ "       s.idservicio, " 
	   		+ "       s.datalta, "  
	   		+ "       s.datfinalizacion, " 
	   		+ "       s.blnhabilitada, " 
	   		+ "       s.idcuenta " 
	   		+ "  from arcos.vinfo_cta_srv_act_intranet s " 
	   		+ " where s.idcuenta = ? ";
	
	   	try (Connection conexion = ConexionArcos.obtenerInstancia();
	   		 PreparedStatement stmt = conexion.prepareStatement(consultaGrupos);
	   		 PreparedStatement stmtResporganicos = conexion.prepareStatement(consultaResporganicos);
	   		 PreparedStatement stmtServiciosActivos = conexion.prepareStatement(consultaServiciosActivos);) {
	   		int parameterIndex = 1;
	   		stmt.setInt(parameterIndex++, codigoCuenta.intValue());
	   		try (ResultSet rs = stmt.executeQuery();) {
		   		if (rs.next()) {
		   			if (("S".equals(rs.getString("blnctaggl"))) && ("N".equals(rs.getString("blnctagglsusp")))) {
		   				grupos.add("cuentagoogle");
		   			}
		   			if ("ies.ujaen.es".equals(rs.getString("iddominio"))) {
		   				grupos.add("ies");
		   			}
		   			if ("ujaen.es".equals(rs.getString("iddominio")) && "N".equals(rs.getString("blninstitucional"))) {
		   				grupos.add("cuentaGoogleUjaenNoInst");
		   			}
				}
	   		}
	
	   		parameterIndex = 1;
	   		stmtResporganicos.setInt(parameterIndex++, codigoCuenta.intValue());
	   		try (ResultSet rsResporganicos = stmtResporganicos.executeQuery();) {
		   		if (rsResporganicos.next()) {
					if (rsResporganicos.getInt("es_responsableorganico") > 0) {
						grupos.add("resporganicas");
					}
				}
	   		}
	   		
	   		parameterIndex = 1;
	   		stmtServiciosActivos.setInt(parameterIndex++, codigoCuenta.intValue());
	   		try (ResultSet rsServiciosActivos = stmtServiciosActivos.executeQuery();) {
		   		while (rsServiciosActivos.next()) {
		   			if ("centro.ies.srv".equals(rsServiciosActivos.getString("idservicio"))) {
		   				grupos.add("cenensec");
		   			}
				}
	   		}
	   		
	   		if (!listaCuentasInstitucionalesUsuario(codigoCuenta).isEmpty()) {
	   			grupos.add("respCuentaInstitucional");
	   		}
		} 
	   	return grupos;
    }
    
	/**
	 * Lista las cuentas institucionales de una persona en función de los parámetros indicados.
	 * 
	 * @param codigoCuenta
	 *            codigo de cuenta en arcos
	 * @return lista de Usuario que cumplen los criterios
	 * @throws SQLException
	 *             en caso de erro en la base de datos
	 */
	public List<Usuario> listaCuentasInstitucionalesUsuario(Integer codigoCuenta) throws SQLException {
		List<String> parametros = new ArrayList<>();
		String where = "(1=1) ";
		if (codigoCuenta != null) {
			//primero se busca la persona de la cuenta y luego las cuentas institucionales de esa persona
			where += " and c.idcuenta = ? ";
			parametros.add(codigoCuenta.toString());
			Usuario usuario = _listaCuentaUsuarios(where, parametros).get(0);
			if (usuario.getCodigoPersonaArcos() == null) {
				return new ArrayList<>();
			}
			where = " (1=1) " 
				  + " and p.codint = ? " 
		          + " and c.blninstitucional = 'S'";
			parametros = new ArrayList<>();
			parametros.add(usuario.getCodigoPersonaArcos().toString());
		}
		return _listaCuentaUsuarios(where, parametros);
	}
}
