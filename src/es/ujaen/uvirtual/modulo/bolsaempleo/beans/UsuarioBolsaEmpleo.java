package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;

import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloRol;
import es.ujaen.uvirtual.utilidades.AdaptadorDocumentoIdentidad;
import es.ujaen.uvirtual.utilidades.Formateador;

/**
 * Clase usuario de UVIRTUAL.
 * 
 * @author ATISoluciones 2021
 */
public class UsuarioBolsaEmpleo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String codcuenta;
	
	// datos vuja
	private String tipodocumento;
	private String idnif;
	private String letranif;
	private String prsnif;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String email;
	
	// datos bep
	private Rol rol;
	private String direccion;
	private String codigopostal;
	private String localidad;
	private String provincia;
	private String telefono;
	private String nacionalidad;	
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

	/**
	 * Constructor por defecto.
	 */
	public UsuarioBolsaEmpleo() {

	}

	/**
	 * Constructor con parametros para Bolsa Empleo.
	 * 
	 * @param prol         .
	 * @param pcodCuenta               .
	 */
	public UsuarioBolsaEmpleo(Rol prol, String pcodCuenta) {
		super();
		this.rol = prol;
		this.codcuenta = pcodCuenta;				
	}
	
	/** Constructor copia.
	 * @param copia .
	 */
	public UsuarioBolsaEmpleo(UsuarioBolsaEmpleo copia) {
		this.codNum = copia.codNum;
		this.codcuenta = copia.codcuenta;
		this.tipodocumento = copia.tipodocumento;
		this.idnif = copia.idnif;
		this.letranif = copia.letranif;
		this.prsnif = copia.prsnif;
		this.nombre = copia.nombre;
		this.apellido1 = copia.apellido1;
		this.apellido2 = copia.apellido2;
		this.email = copia.email;
		this.rol = copia.rol;
		this.direccion = copia.direccion;
		this.codigopostal = copia.codigopostal;
		this.localidad = copia.localidad;
		this.provincia = copia.provincia;
		this.telefono = copia.telefono;
		this.nacionalidad = copia.nacionalidad;
		this.listaDist = copia.listaDist;
		this.excluido = copia.excluido;
		this.excluidoTipo = copia.excluidoTipo;
		this.fechaExclusionInicio = copia.fechaExclusionInicio;
		this.fechaExclusionFin = copia.fechaExclusionFin;
		this.razonExclusion = copia.razonExclusion;
		this.fechaExclusion = copia.fechaExclusion;
		this.borrado = copia.borrado;
		this.razonBorrado = copia.razonBorrado;
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
		
	// datos vuja
	
	public String getTipoDocumento() {
		return tipodocumento;
	}

	public void setTipoDocumento(String ptipodocumento) {
		this.tipodocumento = ptipodocumento;
	}

	public String getIdNif() {
		return idnif;
	}

	public void setIdNif(String pidnif) {
		this.idnif = pidnif;
	}
	
	public String getLetraNif() {
		return letranif;
	}

	public void setLetraNif(String pletranif) {
		this.letranif = pletranif;
	}
	
	public String getPrsNif() {
		return prsnif;
	}

	public void setPrsNif(String pprsnif) {
		this.prsnif = pprsnif;
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
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	/** método que establece los datos de de arcos en bolsa empleo .
	 * @param usu .
	 */
	public void setUsuarioArcos(Usuario usu) {
		this.tipodocumento = usu.getDocumentoTipo();
		this.idnif = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usu);
		this.letranif = AdaptadorDocumentoIdentidad.letraNIF(usu.getDocumentoTipo(), usu.getDocumentoNumero());
		this.prsnif = usu.getDocumentoNumero();
		this.nombre = usu.getNombre();
		this.apellido1 = usu.getApellido1();
		this.apellido2 = usu.getApellido2();
		this.email = usu.getEmailCalculado();
	}
	
	// datos bep
	
	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
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
		return this.getRol() != null && this.getRol().getValor().equals(ModeloRol.ROL_CANDIDATO);
	}

	public boolean isServicioPersonal() {
		return this.getRol() != null && this.getRol().getValor().equals(ModeloRol.ROL_SERVICIO_PERSONAL);
	}

	public boolean isMiembroComision() {
		return this.getRol() != null && this.getRol().getValor().equals(ModeloRol.ROL_MIEMBRO_COMISION);
	}

	public boolean isDirectorDepartamento() {
		return this.getRol() != null && this.getRol().getValor().equals(ModeloRol.ROL_DIRECTOR_DEPARTAMENTO);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Usuario [codNum=" + codNum + ", codcuenta=" + codcuenta + ", tipodocumento=" + tipodocumento + ", idnif=" + idnif + ", letranif=" 
				+ letranif + ", prsnif=" + prsnif + ", nombre=" + nombre + ", apellido1=" + apellido1 + ", apellido2=" + apellido2 + ", email=" 
				+ email + ", rol=" + rol + ", direccion=" + direccion + ", codigopostal=" + codigopostal + ", localidad=" + localidad + ", provincia="
				+ provincia + ", telefono=" + telefono + ", nacionalidad=" + nacionalidad + ", listaDist=" + listaDist + ", excluido=" + excluido 
				+ ", excluidoTipo=" + excluidoTipo + ", fechaExclusionInicio=" + fechaExclusionInicio + ", fechaExclusionFin=" + fechaExclusionFin 
				+ ", razonExclusion=" + razonExclusion + ", fechaExclusion=" + fechaExclusion + ", borrado=" + borrado + ", razonBorrado=" + razonBorrado
				+ ", fechaBorrado=" + fechaBorrado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((codcuenta == null) ? 0 : codcuenta.hashCode());
		result = prime * result + ((tipodocumento == null) ? 0 : tipodocumento.hashCode());
		result = prime * result + ((idnif == null) ? 0 : idnif.hashCode());
		result = prime * result + ((letranif == null) ? 0 : letranif.hashCode());
		result = prime * result + ((prsnif == null) ? 0 : prsnif.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((apellido1 == null) ? 0 : apellido1.hashCode());
		result = prime * result + ((apellido2 == null) ? 0 : apellido2.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((rol == null) ? 0 : rol.hashCode());
		result = prime * result + ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((codigopostal == null) ? 0 : codigopostal.hashCode());
		result = prime * result + ((localidad == null) ? 0 : localidad.hashCode());
		result = prime * result + ((provincia == null) ? 0 : provincia.hashCode());
		result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result + ((nacionalidad == null) ? 0 : nacionalidad.hashCode());
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
	@SuppressWarnings({ "checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity" })
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
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
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
		if (idnif == null) {
			if (other.idnif != null) {
				return false;
			}
		} else if (!idnif.equals(other.idnif)) {
			return false;
		}
		if (letranif == null) {
			if (other.letranif != null) {
				return false;
			}
		} else if (!letranif.equals(other.letranif)) {
			return false;
		}
		if (prsnif == null) {
			if (other.prsnif != null) {
				return false;
			}
		} else if (!prsnif.equals(other.prsnif)) {
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
		if (rol == null) {
			if (other.rol != null) {
				return false;
			}
		} else if (!rol.equals(other.rol)) {
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
