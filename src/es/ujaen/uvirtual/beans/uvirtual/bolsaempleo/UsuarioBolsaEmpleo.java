package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.Date;

import es.ujaen.uvirtual.beans.Rol;

/** Clase usuario de UVIRTUAL.
 * @author ATISoluciones
 *
 */
public class UsuarioBolsaEmpleo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String admusucodNum;
	private String tipodocumento;
	private String numdocumento;
	private String ujaendni;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String sexo;
	private String email;
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
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pnombre .
	 */
	public UsuarioBolsaEmpleo(Integer pcodNum, String pnombre) {
		super();
		this.codNum = pcodNum;
		this.nombre = pnombre;
	}
	
	/** Constructor con parametros.
	 * @param pnombre .
	 */
	public UsuarioBolsaEmpleo(String pnombre) {
		super();
		this.nombre = pnombre;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 */
	public UsuarioBolsaEmpleo(Integer pcodNum) {
		super();
		this.codNum = pcodNum;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public String getAdmUsuCodNum() {
		return admusucodNum;
	}

	public void setAdmUsuCodNum(String padmusucodNum) {
		this.admusucodNum = padmusucodNum;
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
	
	public String getUjaEnDni() {
		return ujaendni;
	}

	public void setUjaEnDni(String pujaendni) {
		this.ujaendni = pujaendni;
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
