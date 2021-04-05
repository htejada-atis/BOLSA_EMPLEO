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
	private Integer adm_usu_codNum;
	private String dni;
	private String nombre;
	private String telefono;
	private String movil;
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
	
	public Integer getAdmUsuCodNum() {
		return adm_usu_codNum;
	}

	public void setAdmUsuCodNum(Integer admusucodNum) {
		this.adm_usu_codNum = admusucodNum;
	}
	
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
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
