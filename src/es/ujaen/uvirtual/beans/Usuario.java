package es.ujaen.uvirtual.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.ujaen.uvirtual.beans.uxxirrhh.Cargo;
import es.ujaen.uvirtual.beans.uxxirrhh.Plaza;

/**
 * Bean usuario con los datos y grupos a los que pertenece y administra.
 * 20130516 - se añaden los roles de otros dominios (julopez)
 * 20110329 - Se añade el dominio (julopez)
 * 20110321 - Añade los campos para conexión con arcos (julopez)
 * 20140124 - se añade cuenta activa (jmoral)
 * 20140326 - se añade correo ruta, y cuenta google (jmoral)
 *
 * @author julopez
 *
 */
@SuppressWarnings({"java:S1319", "java:S1948"})
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = -8505032842990035675L;
	
	/**
	 * Definición de los roles tal y como aparecen en el campo VALOR de la tabla ADM_ROL.
	 */
	// ROLES GENÉRICOS
	public static final String ROL_PDI = "pdi";
	public static final String ROL_PAS = "pas";
	public static final String ROL_ALUMNO = "alumnos";
	public static final String ROL_IES = "ies"; // Profesores de secundaria
	
	// Roles relacionados con la corrección web de las PAU
	public static final String ROL_PAU_PAS = "paupas"; // Rol utilizado por el PAS que podrá supervisar e introducir las notas de las PAU a través de CV
	
	// Roles relacionados con los contratos de movilidad
	public static final String ROL_CONMOV_ADMINISTRADOR = "conmovadministrador";
	public static final String ROL_CONMOV_GESTOR = "conmovgestor";
	public static final String ROL_CONMOV_SECRETARIA = "conmovsecretaria";
	public static final String ROL_CONMOV_PDI_GESTOR = "conmovpdigestor"; // Rol "exclusivo" para Sebastián Bruque
	public static final String ROL_CONMOV_CONSULTA_EXTERNA = "conmovconsultaexterna"; //consulta desde calificaciones al contrato, no existe en adm_rol
	
	// Roles relacionados con la reserva de aulas de libre acceso
	public static final String ROL_RAI_ADMINISTRADOR = "raiadmin";
	public static final String ROL_RAI_TECNICO = "raitecnico";
	
	protected String uid = null;
	protected String nombre = null;
	protected String apellido1 = null;
	protected String apellido2 = null;
	protected String emailCuentaPersona = null;
	protected String telefonoPersona = null;
	protected String documentoNumero = null;
	protected String documentoTipo = null;
	protected boolean cuentaActiva = true;
	protected String correoRuta = null;
	protected boolean cuentaGoogle = false;
	protected boolean cuentaGoogleSuspendida = false;
	
	// datos de administración
	protected ArrayList<String> roles = null;
	protected ArrayList<String> rolesAdministrados = null;

	// Roles por dominio
	protected HashMap<String, ArrayList<String>> rolesPorDominio = null;
	
	// datos de uxxirrhh
	protected Integer codigoRRHH = null;
	protected ArrayList<Plaza> plazasRRHH = null;
	protected ArrayList<Cargo> cargosRRHH = null;
	protected String directorDepartamento = null;
	
	// datos de arcos
	protected Integer codigoPersonaArcos = null;
	protected Integer codigoCuentaArcos = null;
	protected String sexo = null;
	protected boolean personaManual = false;
	protected boolean cuentaBloqueada = false;
	protected boolean cuentaInstitucional = false;
	protected String cuentaDN = null;
	protected String dominio = null;
	
	// datos de uxxiac
	protected Integer codigoUXXIAC = null;
	protected String documentoUXXIAC = null;
	
	
	/**
	 * Lista el nombre y apellidos de la persona.
	 * @return apellidos, nombre del usuario
	 */
	public String getApellidosYNombre() {
		return ((apellido1 == null ? "" : apellido1 + " ") + (apellido2 == null ? "" : apellido2)).trim() + (nombre == null ? "" : ", " + nombre);
	}
	
	/**
	 * Lista el nombre y apellidos de la persona.
	 * @return nombre y apellidos de la persona
	 */
	public String getNombreYApellidos() {
		return (nombre == null ? "" : nombre + " ") + (apellido1 == null ? "" : apellido1 + " ") + (apellido2 == null ? "" : apellido2).trim();
	}
	
	/** get email calculado.
	 * @return email calculado
	 */
	public String getEmailCalculado() {
		if ("estudiante.ujaen.es".equals(this.dominio)) {
			return this.uid + "@red.ujaen.es";
		} else {
			return this.uid + "@" + dominio;
		}
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	
	public String getApellido2() {
		return apellido2;
	}
	
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	
	public String getDocumentoNumero() {
		return documentoNumero;
	}
	
	public void setDocumentoNumero(String documentoNumero) {
		this.documentoNumero = documentoNumero;
	}
	
	public String getDocumentoTipo() {
		return documentoTipo;
	}
	
	public void setDocumentoTipo(String documentoTipo) {
		this.documentoTipo = documentoTipo;
	}
	
	public ArrayList<String> getRoles() {
		return roles;
	}
	
	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}
	
	public ArrayList<String> getRolesAdministrados() {
		return rolesAdministrados;
	}

	public void setRolesAdministrados(ArrayList<String> rolesAdministrados) {
		this.rolesAdministrados = rolesAdministrados;
	}
	
	public HashMap<String, ArrayList<String>> getRolesPorDominio() {
		return rolesPorDominio;
	}

	public void setRolesPorDominio(HashMap<String, ArrayList<String>> rolesPorDominio) {
		this.rolesPorDominio = rolesPorDominio;
	}

	public String getEmailCuentaPersona() {
		return emailCuentaPersona;
	}

	public void setEmailCuentaPersona(String emailCuentaPersona) {
		this.emailCuentaPersona = emailCuentaPersona;
	}
	
	public String getTelefonoPersona() {
		return telefonoPersona;
	}

	public void setTelefonoPersona(String telefonoPersona) {
		this.telefonoPersona = telefonoPersona;
	}

	public Integer getCodigoRRHH() {
		return codigoRRHH;
	}
	
	public void setCodigoRRHH(Integer codigoRRHH) {
		this.codigoRRHH = codigoRRHH;
	}

	public ArrayList<Plaza> getPlazasRRHH() {
		return plazasRRHH;
	}

	public void setPlazasRRHH(ArrayList<Plaza> plazasRRHH) {
		this.plazasRRHH = plazasRRHH;
	}
	
	public ArrayList<Cargo> getCargosRRHH() {
		return cargosRRHH;
	}
	
	public void setCargosRRHH(ArrayList<Cargo> cargosRRHH) {
		this.cargosRRHH = cargosRRHH;
	}

	public Integer getCodigoPersonaArcos() {
		return codigoPersonaArcos;
	}

	public void setCodigoPersonaArcos(Integer codigoPersonaArcos) {
		this.codigoPersonaArcos = codigoPersonaArcos;
	}

	public Integer getCodigoCuentaArcos() {
		return codigoCuentaArcos;
	}

	public void setCodigoCuentaArcos(Integer codigoCuentaArcos) {
		this.codigoCuentaArcos = codigoCuentaArcos;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public boolean isPersonaManual() {
		return personaManual;
	}

	public void setPersonaManual(boolean personaManual) {
		this.personaManual = personaManual;
	}

	public boolean isCuentaBloqueada() {
		return cuentaBloqueada;
	}

	public void setCuentaBloqueada(boolean cuentaBloqueada) {
		this.cuentaBloqueada = cuentaBloqueada;
	}

	public boolean isCuentaInstitucional() {
		return cuentaInstitucional;
	}

	public void setCuentaInstitucional(boolean cuentaInstitucional) {
		this.cuentaInstitucional = cuentaInstitucional;
	}

	public String getCuentaDN() {
		return cuentaDN;
	}

	public void setCuentaDN(String cuentaDN) {
		this.cuentaDN = cuentaDN;
	}

	public Integer getCodigoUXXIAC() {
		return codigoUXXIAC;
	}

	public void setCodigoUXXIAC(Integer codigoUXXIAC) {
		this.codigoUXXIAC = codigoUXXIAC;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public String getDirectorDepartamento() {
		return directorDepartamento;
	}

	public void setDirectorDepartamento(String directorDepartamento) {
		this.directorDepartamento = directorDepartamento;
	}

	public String getDocumentoUXXIAC() {
		return documentoUXXIAC;
	}

	public void setDocumentoUXXIAC(String documentoUXXIAC) {
		this.documentoUXXIAC = documentoUXXIAC;
	}

	private String toStringRolesAdminitrados() {
		StringBuilder buffer = new StringBuilder();
		if (this.rolesAdministrados != null) {
			buffer.append("\n\n");
			buffer.append("ROLES ADM.").append("\n");
			buffer.append("==========").append("\n");
			for (String rol : this.rolesAdministrados) {
				buffer.append(rol).append("\n");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	private String toStringRoles() {
		StringBuilder buffer = new StringBuilder();
		if (this.roles != null) {
			buffer.append("\n\n");
			buffer.append("ROLES").append("\n");
			buffer.append("=====").append("\n");
			for (String rol : this.roles) {
				buffer.append(rol).append("\n");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	private String toStringRolesPorDominio() {
		StringBuilder buffer = new StringBuilder();
		if (this.rolesPorDominio != null) {
			buffer.append("\n\n");
			buffer.append("ROLES POR DOMINIO").append("\n");
			buffer.append("=================").append("\n");
			for (Map.Entry<String, ArrayList<String>> dominioRol : this.rolesPorDominio.entrySet()) {
				buffer.append(dominioRol.getKey()).append(": ");
				for (String rol : dominioRol.getValue()) {
					buffer.append(rol).append(", ");
				}
				buffer.append("\n");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	private String toStringDatosArcos() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("DATOS DE ARCOS").append("\n");
		buffer.append("===============").append("\n");
		buffer.append("Sexo ..........: ").append(this.sexo).append("\n");
		buffer.append("Persona manual : ").append(this.personaManual).append("\n");
		buffer.append("Cuenta bloq. ..: ").append(this.cuentaBloqueada).append("\n");
		buffer.append("Cuenta DN .....: ").append(this.cuentaDN).append("\n");
		buffer.append("Cuenta Dominio : ").append(this.dominio).append("\n");
		return buffer.toString();
	}
	
	private String toStringDatosRrhh() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("DATOS DE UXXI-RRHH").append("\n");
		buffer.append("==================").append("\n");
		buffer.append("Director Dept. : ").append(this.directorDepartamento).append("\n");
		if (this.plazasRRHH != null) {
			buffer.append("\n\n");
			buffer.append("PLAZAS").append("\n");
			buffer.append("======").append("\n");
			for (Plaza plaza : this.plazasRRHH) {
				buffer.append(plaza.toString()).append("\n\n"); 
			}
			buffer.append("\n");
		}
		if (this.cargosRRHH != null) {
			buffer.append("\n\n");
			buffer.append("CARGOS").append("\n");
			buffer.append("======").append("\n");
			for (Cargo cargo : this.cargosRRHH) {
				buffer.append(cargo.toString()).append("\n"); 
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Uid ...........: ").append(this.uid).append("\n");
		buffer.append("Nombre ........: ").append(this.nombre).append("\n");
		buffer.append("Apellido 1 ....: ").append(this.apellido1).append("\n");
		buffer.append("Apellido 2 ....: ").append(this.apellido2).append("\n");
		buffer.append("Email persona .: ").append(this.emailCuentaPersona).append("\n");
		buffer.append("Num. Documento : ").append(this.documentoNumero).append("\n");
		buffer.append("Tipo Documento : ").append(this.documentoTipo).append("\n");
		buffer.append("Código ARCOS(C): ").append(this.codigoCuentaArcos).append("\n");
		buffer.append("Código ARCOS(P): ").append(this.codigoPersonaArcos).append("\n");
		buffer.append("Código UXXIAC .: ").append(this.codigoUXXIAC).append("\n");
		buffer.append("Código UXXIRRHH: ").append(this.codigoRRHH).append("\n");
		buffer.append("Cuenta Activa .: ").append(this.cuentaActiva).append("\n");
		buffer.append("Correo Ruta   .: ").append(this.correoRuta).append("\n");
		buffer.append("Cuenta Ggl    .: ").append(this.cuentaGoogle).append("\n");
		buffer.append("Cta Ggl Suspen.: ").append(this.cuentaGoogleSuspendida).append("\n");
		buffer.append(toStringRoles());
		buffer.append(toStringRolesAdminitrados());
		buffer.append(toStringRolesPorDominio());
		buffer.append("\n\n");
		buffer.append(toStringDatosArcos());
		buffer.append("\n\n");
		buffer.append(toStringDatosRrhh());
		
		return buffer.toString();
	}

	public boolean isCuentaActiva() {
		return cuentaActiva;
	}

	public void setCuentaActiva(boolean cuentaActiva) {
		this.cuentaActiva = cuentaActiva;
	}

	public String getCorreoRuta() {
		return correoRuta;
	}

	public void setCorreoRuta(String correoRuta) {
		this.correoRuta = correoRuta;
	}

	public boolean isCuentaGoogle() {
		return cuentaGoogle;
	}

	public void setCuentaGoogle(boolean cuentaGoogle) {
		this.cuentaGoogle = cuentaGoogle;
	}

	public boolean isCuentaGoogleSuspendida() {
		return cuentaGoogleSuspendida;
	}

	public void setCuentaGoogleSuspendida(boolean cuentaGoogleSuspendida) {
		this.cuentaGoogleSuspendida = cuentaGoogleSuspendida;
	}
}
