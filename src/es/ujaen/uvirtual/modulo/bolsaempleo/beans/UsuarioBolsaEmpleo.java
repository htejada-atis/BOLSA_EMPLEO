package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;

import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.Formateador;

/** Clase usuario de UVIRTUAL.
 * @author ATISoluciones 2021 
 */
public class UsuarioBolsaEmpleo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String codcuenta;
	private String tipodocumento;
	private String numdocumento;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String email;
	private String direccion;
	private String codigopostal;
	private String localidad;
	private String provincia;
	private String telefono;
	private String nacionalidad;
	private String sexo;
	
	private Usuario usuarioArcos;
	private Rol rol;
	private Boolean listaDist;
	private Boolean excluido;
	private String excluidoTipo;
	private Date fechaExclusionInicio;
	private Date fechaExclusionFin;
	private String razonExclusion;
	private Date fechaExclusion;
	private Boolean borrado;
	private String razonBorrado;
	private Date fechaBorrado;

	
	/** Constructor por defecto.
	 */
	public UsuarioBolsaEmpleo() {
		
	}
	
	/** Constructor con parametros para Bolsa Empleo.
	 * @param pdocumento .
	 * @param prol .
	 * @param plistaDist .
	 * @param pexcluido .
	 * @param pexcluidotipo .
	 * @param pfechaexclusionini .
	 * @param pfechaexclusionfin .
	 * @param pusu .
	 */
	public UsuarioBolsaEmpleo(String pdocumento, String pusu, Rol prol, Boolean plistaDist,
			Boolean pexcluido, String pexcluidotipo, Date pfechaexclusionini, Date pfechaexclusionfin) {
		super();
		this.numdocumento = pdocumento;
		this.codcuenta = pusu;
		this.rol = prol;
		this.listaDist = plistaDist;
		this.excluido = pexcluido;
		this.excluidoTipo = pexcluidotipo;
		this.fechaExclusionInicio = pfechaexclusionini;
		this.fechaExclusionFin = pfechaexclusionfin;
	}
	
	/** Constructor con parametros para Bolsa Empleo excluir usuario.
	 * @param pcodNum .
	 * @param pexcluido .
	 * @param pexcluidotipo .
	 * @param prazonexcluido .
	 * @param pfechaexclusion .
	 * @param pfechaexclusionini .
	 * @param pfechaexclusionfin .
	 */
	public UsuarioBolsaEmpleo(Integer pcodNum, Boolean pexcluido, String pexcluidotipo, String prazonexcluido, 
			Date pfechaexclusion, Date pfechaexclusionini, Date pfechaexclusionfin) {
		super();
		this.codNum = pcodNum;
		this.excluido = pexcluido;
		this.excluidoTipo = pexcluidotipo;
		this.fechaExclusionInicio = pfechaexclusionini;
		this.fechaExclusionFin = pfechaexclusionfin;
		this.razonExclusion = prazonexcluido;
		this.fechaExclusion = pfechaexclusion;
	}
	
	
	/** Constructor con parametros.
	 * @param prol .
	 * @param plistaDist .
	 * @param pexcluido .
	 * @param pexcluidotipo .
	 * @param pfechaexclusionini .
	 * @param pfechaexclusionfin .
	 * @param prazonexcluido .
	 * @param pfechaexclusion .
	 * @param pcodNum .
	 */
	public UsuarioBolsaEmpleo(Integer pcodNum, Rol prol, Boolean plistaDist, Boolean pexcluido, 
			String pexcluidotipo, String prazonexcluido, Date pfechaexclusion, Date pfechaexclusionini, Date pfechaexclusionfin) {
		super();
		this.codNum = pcodNum;
		this.rol = prol;
		this.listaDist = plistaDist;
		this.excluido = pexcluido;
		this.excluidoTipo = pexcluidotipo;
		this.fechaExclusionInicio = pfechaexclusionini;
		this.fechaExclusionFin = pfechaexclusionfin;
		this.razonExclusion = prazonexcluido;
		this.fechaExclusion = pfechaexclusion;
	}
	
	/** Constructor con parametros.
	 * @param prol .
	 * @param plistaDist .
	 * @param pexcluido .
	 * @param pexcluidotipo .
	 * @param pfechaexclusionini .
	 * @param pfechaexclusionfin .
	 * @param prazonexcluido .
	 * @param pfechaexclusion .
	 * @param pusu .
	 * @param pdocumento .
	 */
	public UsuarioBolsaEmpleo(String pusu, String pdocumento, Rol prol, Boolean plistaDist, Boolean pexcluido, String pexcluidotipo, String prazonexcluido, 
			Date pfechaexclusion, Date pfechaexclusionini, Date pfechaexclusionfin) {
		super();
		this.codcuenta = pusu;
		this.numdocumento = pdocumento;
		this.rol = prol;
		this.listaDist = plistaDist;
		this.excluido = pexcluido;
		this.excluidoTipo = pexcluidotipo;
		this.fechaExclusionInicio = pfechaexclusionini;
		this.fechaExclusionFin = pfechaexclusionfin;
		this.razonExclusion = prazonexcluido;
		this.fechaExclusion = pfechaexclusion;
	}
	
	/** Constructor con parametros para envio de datos personales.
	 * @param pcodNum .
	 * @param pnombre .
	 * @param primerapellido .
	 * @param segundoapellido .
	 * @param pemail .
	 * @param pdireccion .
	 * @param pcodigopostal .
	 * @param plocalidad .
	 * @param pprovincia .
	 * @param ptelefono .
	 * @param pnacionalidad .
	 * @param psexo .
	 * @param plistaDist .
	 */
	public UsuarioBolsaEmpleo(Integer pcodNum, String pnombre, String primerapellido, String segundoapellido, String pemail, String pdireccion, String pcodigopostal,
			String plocalidad, String pprovincia, String ptelefono, String pnacionalidad, String psexo, Boolean plistaDist) {
		super();
		this.codNum = pcodNum;
		this.nombre = pnombre;
		this.apellido1 = primerapellido;
		this.apellido2 = segundoapellido;
		this.email = pemail;
		this.direccion = pdireccion;
		this.codigopostal = pcodigopostal;
		this.localidad = plocalidad;
		this.provincia = pprovincia;
		this.telefono = ptelefono;
		this.nacionalidad = pnacionalidad;
		this.sexo = psexo;
		this.listaDist = plistaDist;
	}
	
	
	
	/** Constructor copia.
	 * @param copia Usuario a copiar
	 */
	public UsuarioBolsaEmpleo(UsuarioBolsaEmpleo copia) {
		this.codNum = copia.codNum;
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
		this.excluidoTipo = copia.excluidoTipo;
		this.fechaExclusionInicio = copia.fechaExclusionInicio;
		this.fechaExclusionFin = copia.fechaExclusionFin;
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
	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String pdireccion) {
		this.direccion = pdireccion;
	}
	
	public String getCodigoPostal() {
		return codigopostal;
	}

	public void setCodigoPostal(String pcodigopostal) {
		this.codigopostal = pcodigopostal;
	}
	
	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String plocalidad) {
		this.localidad = plocalidad;
	}
	
	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String pprovincia) {
		this.provincia = pprovincia;
	}
	
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String ptelefono) {
		this.telefono = ptelefono;
	}
	
	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String pnacionalidad) {
		this.nacionalidad = pnacionalidad;
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
	
	public String getExcluidoTipo() {
		return excluidoTipo;
	}

	public void setExcluidoTipo(String excluidoTipo) {
		this.excluidoTipo = excluidoTipo;
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
	
	public Date getFechaExclusionInicio() {
		return fechaExclusionInicio;
	}

	public void setFechaExclusionInicio(Date fechaExclusionIni) {
		this.fechaExclusionInicio = fechaExclusionIni;
	}
	
	public String getFechaExclusionInicioFormato() {
		return Formateador.formatoFecha(fechaExclusionInicio, Formateador.FORMATO_FECHA_DDMMYYYY);
	}
	
	public String getFechaHoraExclusionInicioFormato() {
		return Formateador.formatoFecha(fechaExclusionInicio, Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS);
	}
	
	public Date getFechaExclusionFin() {
		return fechaExclusionFin;
	}

	public void setFechaExclusionFin(Date fechaExclusionFin) {
		this.fechaExclusionFin = fechaExclusionFin;
	}
	
	public String getFechaExclusionFinFormato() {
		return Formateador.formatoFecha(fechaExclusionFin, Formateador.FORMATO_FECHA_DDMMYYYY);
	}
	
	public String getFechaHoraExclusionFinFormato() {
		return Formateador.formatoFecha(fechaExclusionFin, Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS);
	}
	
	public Boolean getBorrado() {
		return borrado;
	}

	public void setBorrado(Boolean borrado) {
		this.borrado = borrado;
	}
	
	public String getRazonBorrado() {
		return razonBorrado;
	}

	public void setRazonBorrado(String prazonBorrado) {
		this.razonBorrado = prazonBorrado;
	}
	
	public Date getFechaBorrado() {
		return fechaBorrado;
	}

	public void setFechaBorrado(Date fechaBorrado) {
		this.fechaBorrado = fechaBorrado;
	}
	
	public boolean isCandidato() {
		return this.getRol() != null && this.getRol().getValor().equals(ModeloUsuarioBolsaEmpleo.ROL_CANDIDATO);
	}
	
	public boolean isServicioPersonal() {
		return this.getRol() != null && this.getRol().getValor().equals(ModeloUsuarioBolsaEmpleo.ROL_SERVICIO_PERSONAL);
	}
	
	public boolean isMiembroComision() {
		return this.getRol() != null && this.getRol().getValor().equals(ModeloUsuarioBolsaEmpleo.ROL_MIEMBRO_COMISION);
	}
	
	public boolean isDirectorDepartamento() {
		return this.getRol() != null && this.getRol().getValor().equals(ModeloUsuarioBolsaEmpleo.ROL_DIRECTOR_DEPARTAMENTO);
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Usuario [codNum=" + codNum + ", codcuenta=" + codcuenta + ", tipodocumento=" + tipodocumento + ", numdocumento=" + numdocumento + ", nombre="
				+ nombre + ", apellido1=" + apellido1 + ", apellido2=" + apellido2 + ", email=" + email + ", direccion=" + direccion + ", codigopostal=" 
				+ codigopostal + ", localidad=" + localidad + ", provincia=" + provincia + ", telefono=" + telefono + ", nacionalidad=" + nacionalidad + ", sexo=" 
				+ sexo + ", usuarioArcos=" + usuarioArcos + ", rol=" + rol + ", listaDist=" + listaDist + ", excluido=" + excluido + ", excluidoTipo=" 
				+ excluidoTipo + ", fechaExclusionInicio=" + fechaExclusionInicio + ", fechaExclusionFin=" + fechaExclusionFin + ", razonExclusion=" 
				+ razonExclusion + ", fechaExclusion=" + fechaExclusion + ", borrado=" + borrado + ", razonBorrado=" + razonBorrado + ", fechaBorrado=" 
				+ fechaBorrado + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((codcuenta == null) ? 0 : codcuenta.hashCode());
		result = prime * result + ((tipodocumento == null) ? 0 : tipodocumento.hashCode());
		result = prime * result + ((numdocumento == null) ? 0 : numdocumento.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((apellido1 == null) ? 0 : apellido1.hashCode());
		result = prime * result + ((apellido2 == null) ? 0 : apellido2.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((codigopostal == null) ? 0 : codigopostal.hashCode());
		result = prime * result + ((localidad == null) ? 0 : localidad.hashCode());
		result = prime * result + ((provincia == null) ? 0 : provincia.hashCode());
		result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result + ((nacionalidad == null) ? 0 : nacionalidad.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
		result = prime * result + ((usuarioArcos == null) ? 0 : usuarioArcos.hashCode());
		result = prime * result + ((rol == null) ? 0 : rol.hashCode());
		result = prime * result + ((listaDist == null) ? 0 : listaDist.hashCode());
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());
		result = prime * result + ((excluidoTipo == null) ? 0 : excluidoTipo.hashCode());
		result = prime * result + ((fechaExclusionInicio == null) ? 0 : fechaExclusionInicio.hashCode());
		result = prime * result + ((fechaExclusionFin == null) ? 0 : fechaExclusionFin.hashCode());
		result = prime * result + ((razonExclusion == null) ? 0 : razonExclusion.hashCode());
		result = prime * result + ((fechaExclusion == null) ? 0 : fechaExclusion.hashCode());
		result = prime * result + ((borrado == null) ? 0 : borrado.hashCode());
		result = prime * result + ((razonBorrado == null) ? 0 : razonBorrado.hashCode());
		result = prime * result + ((fechaBorrado == null) ? 0 : fechaBorrado.hashCode());
		
		return result;
	}
	
	@Override
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity"})
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UsuarioBolsaEmpleo other = (UsuarioBolsaEmpleo) obj;
		if (codcuenta == null) {
			if (other.codcuenta != null) {
				return false;
			}
		} else if (!codcuenta.equals(other.codcuenta)) {
			return false;
		}
		if (tipodocumento == null) {
			if (other.tipodocumento != null) {
				return false;
			}
		} else if (!tipodocumento.equals(other.tipodocumento)) {
			return false;
		}
		if (numdocumento == null) {
			if (other.numdocumento != null) {
				return false;
			}
		} else if (!numdocumento.equals(other.numdocumento)) {
			return false;
		}
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (apellido1 == null) {
			if (other.apellido1 != null) {
				return false;
			}
		} else if (!apellido1.equals(other.apellido1)) {
			return false;
		}
		if (apellido2 == null) {
			if (other.apellido2 != null) {
				return false;
			}
		} else if (!apellido2.equals(other.apellido2)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (direccion == null) {
			if (other.direccion != null) {
				return false;
			}
		} else if (!direccion.equals(other.direccion)) {
			return false;
		}
		if (codigopostal == null) {
			if (other.codigopostal != null) {
				return false;
			}
		} else if (!codigopostal.equals(other.codigopostal)) {
			return false;
		}
		if (localidad == null) {
			if (other.localidad != null) {
				return false;
			}
		} else if (!localidad.equals(other.localidad)) {
			return false;
		}
		if (provincia == null) {
			if (other.provincia != null) {
				return false;
			}
		} else if (!provincia.equals(other.provincia)) {
			return false;
		}
		if (telefono == null) {
			if (other.telefono != null) {
				return false;
			}
		} else if (!telefono.equals(other.telefono)) {
			return false;
		}
		if (nacionalidad == null) {
			if (other.nacionalidad != null) {
				return false;
			}
		} else if (!nacionalidad.equals(other.nacionalidad)) {
			return false;
		}
		if (sexo == null) {
			if (other.sexo != null) {
				return false;
			}
		} else if (!sexo.equals(other.sexo)) {
			return false;
		}
		if (usuarioArcos == null) {
			if (other.usuarioArcos != null) {
				return false;
			}
		} else if (!usuarioArcos.equals(other.usuarioArcos)) {
			return false;
		}
		if (rol == null) {
			if (other.rol != null) {
				return false;
			}
		} else if (!rol.equals(other.rol)) {
			return false;
		}
		if (listaDist == null) {
			if (other.listaDist != null) {
				return false;
			}
		} else if (!listaDist.equals(other.listaDist)) {
			return false;
		}
		if (excluido == null) {
			if (other.excluido != null) {
				return false;
			}
		} else if (!excluido.equals(other.excluido)) {
			return false;
		}
		if (excluidoTipo == null) {
			if (other.excluidoTipo != null) {
				return false;
			}
		} else if (!excluidoTipo.equals(other.excluidoTipo)) {
			return false;
		}
		if (fechaExclusionInicio == null) {
			if (other.fechaExclusionInicio != null) {
				return false;
			}
		} else if (!fechaExclusionInicio.equals(other.fechaExclusionInicio)) {
			return false;
		}
		if (fechaExclusionFin == null) {
			if (other.fechaExclusionFin != null) {
				return false;
			}
		} else if (!fechaExclusionFin.equals(other.fechaExclusionFin)) {
			return false;
		}
		if (razonExclusion == null) {
			if (other.razonExclusion != null) {
				return false;
			}
		} else if (!razonExclusion.equals(other.razonExclusion)) {
			return false;
		}
		if (fechaExclusion == null) {
			if (other.fechaExclusion != null) {
				return false;
			}
		} else if (!fechaExclusion.equals(other.fechaExclusion)) {
			return false;
		}
		if (borrado == null) {
			if (other.borrado != null) {
				return false;
			}
		} else if (!borrado.equals(other.borrado)) {
			return false;
		}
		if (razonBorrado == null) {
			if (other.razonBorrado != null) {
				return false;
			}
		} else if (!razonBorrado.equals(other.razonBorrado)) {
			return false;
		}
		if (fechaBorrado == null) {
			if (other.fechaBorrado != null) {
				return false;
			}
		} else if (!fechaBorrado.equals(other.fechaBorrado)) {
			return false;
		}	
		
		return true;
	}
	
	
}
