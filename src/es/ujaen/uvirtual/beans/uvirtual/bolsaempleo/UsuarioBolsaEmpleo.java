package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.Date;

import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.utilidades.Formateador;

/** Clase usuario de UVIRTUAL.
 * @author ATISoluciones
 *
 */
public class UsuarioBolsaEmpleo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Integer codpersona;
	private String codcuenta;
	private String tipodocumento;
	private String numdocumento;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String sexo;
	private String email;
	private Usuario usuarioArcos;
	private Rol rol;
	private Boolean listaDist;
	private Boolean excluido;
	private String razonExclusion;
	private Date fechaExclusion;
	private Boolean borrado;
	private Date fechaBorrado;

	
	/** Constructor por defecto.
	 */
	public UsuarioBolsaEmpleo() {
		
	}
	
	/** Constructor con parametros para Bolsa Empleo.
	 * @param pcodpersona .
	 * @param prol .
	 * @param plistaDist .
	 * @param pexcluido .
	 * @param pusu .
	 */
	public UsuarioBolsaEmpleo(Integer pcodpersona, String pusu, Rol prol, Boolean plistaDist, Boolean pexcluido) {
		super();
		this.codpersona = pcodpersona;
		this.codcuenta = pusu;
		this.rol = prol;
		this.listaDist = plistaDist;
		this.excluido = pexcluido;
	}
	
	/** Constructor con parametros para Bolsa Empleo excluir usuario.
	 * @param pcodNum .
	 * @param pexcluido .
	 * @param prazonexcluido .
	 * @param pfechaexclusion .
	 */
	public UsuarioBolsaEmpleo(Integer pcodNum, Boolean pexcluido, String prazonexcluido, Date pfechaexclusion) {
		super();
		this.codNum = pcodNum;
		this.excluido = pexcluido;
		this.razonExclusion = prazonexcluido;
		this.fechaExclusion = pfechaexclusion;
	}
	
	
	/** Constructor con parametros.
	 * @param prol .
	 * @param plistaDist .
	 * @param pexcluido .
	 * @param prazonexcluido .
	 * @param pfechaexclusion .
	 * @param pcodNum .
	 */
	public UsuarioBolsaEmpleo(Integer pcodNum, Rol prol, Boolean plistaDist, Boolean pexcluido, String prazonexcluido, Date pfechaexclusion) {
		super();
		this.codNum = pcodNum;
		this.rol = prol;
		this.listaDist = plistaDist;
		this.excluido = pexcluido;
		this.razonExclusion = prazonexcluido;
		this.fechaExclusion = pfechaexclusion;
	}
	
	/** Constructor con parametros.
	 * @param prol .
	 * @param plistaDist .
	 * @param pexcluido .
	 * @param prazonexcluido .
	 * @param pfechaexclusion .
	 * @param pusu .
	 * @param pcodpersona .
	 */
	public UsuarioBolsaEmpleo(Integer pcodpersona, String pusu, Rol prol, Boolean plistaDist, Boolean pexcluido, String prazonexcluido, Date pfechaexclusion) {
		super();
		this.codpersona = pcodpersona;
		this.codcuenta = pusu;
		this.rol = prol;
		this.listaDist = plistaDist;
		this.excluido = pexcluido;
		this.razonExclusion = prazonexcluido;
		this.fechaExclusion = pfechaexclusion;
	}
	
	/** Constructor copia.
	 * @param copia Usuario a copiar
	 */
	public UsuarioBolsaEmpleo(UsuarioBolsaEmpleo copia) {
		this.codNum = copia.codNum;
		this.codpersona = copia.codpersona;
		this.codcuenta = copia.codcuenta;
		this.tipodocumento = copia.tipodocumento;
		this.numdocumento = copia.numdocumento;
		this.nombre = copia.nombre;
		this.apellido1 = copia.apellido1;
		this.apellido2 = copia.apellido2;
		this.sexo = copia.sexo;
		this.email = copia.email;
		this.usuarioArcos = copia.usuarioArcos;
		this.rol = copia.rol;
		this.listaDist = copia.listaDist;
		this.excluido = copia.excluido;
		this.razonExclusion = copia.razonExclusion;
		this.fechaExclusion = copia.fechaExclusion;
		this.borrado = copia.borrado;
		this.fechaBorrado = copia.fechaBorrado;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public Integer getCodPersona() {
		return codpersona;
	}

	public void setCodPersona(Integer pcodpersona) {
		this.codpersona = pcodpersona;
	}
	
	public String getCodCuenta() {
		return codcuenta;
	}

	public void setCodCuenta(String pcodcuenta) {
		this.codcuenta = pcodcuenta;
	}
	
	public String getTipoDocumento() {
		return tipodocumento;
	}

	public void setTipoDocumento(String ptipodocumento) {
		this.tipodocumento = ptipodocumento;
	}
	
	public String getNumDocumento() {
		return numdocumento;
	}

	public void setNumDocumento(String pnumdocumento) {
		this.numdocumento = pnumdocumento;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getPrimerApellido() {
		return apellido1;
	}

	public void setPrimerApellido(String papellido1) {
		this.apellido1 = papellido1;
	}
	
	public String getSegundoApellido() {
		return apellido2;
	}

	public void setSegundoApellido(String papellido2) {
		this.apellido2 = papellido2;
	}
	
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String psexo) {
		this.sexo = psexo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Usuario getUsuarioArcos() {
		return usuarioArcos;
	}

	public void setUsuarioArcos(Usuario pusuarioArcos) {
		this.usuarioArcos = pusuarioArcos;
	}
	
	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	public Boolean getListaDist() {
		return listaDist;
	}

	public void setListaDist(Boolean listaDist) {
		this.listaDist = listaDist;
	}
	
	public Boolean getExcluido() {
		return excluido;
	}

	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}
	
	public String getRazonExcluido() {
		return razonExclusion;
	}

	public void setRazonExcluido(String prazonExclusion) {
		this.razonExclusion = prazonExclusion;
	}
	
	public Date getFechaExclusion() {
		return fechaExclusion;
	}

	public void setFechaExclusion(Date fechaExclusion) {
		this.fechaExclusion = fechaExclusion;
	}
	
	public String getFechaExclusionFormato() {
		return Formateador.formatoFecha(fechaExclusion, Formateador.FORMATO_FECHA_DDMMYYYY);
	}
	
	public String getFechaHoraExclusionFormato() {
		return Formateador.formatoFecha(fechaExclusion, Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS);
	}
	
	
	public Boolean getBorrado() {
		return borrado;
	}

	public void setBorrado(Boolean borrado) {
		this.borrado = borrado;
	}
	
	public Date getFechaBorrado() {
		return fechaBorrado;
	}

	public void setFechaBorrado(Date fechaBorrado) {
		this.fechaBorrado = fechaBorrado;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
