package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Clase mérito preferente del usuario.
 * 
 * @author ATISoluciones 2021
 */
public class MeritoPreferenteUsuario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private MeritoPreferente meritoPreferente;
	private UsuarioBolsaEmpleo usuario;
	private String descripcion;	
	private transient InputStream archivo;
	private Boolean borrado;
	private Date fechaBorrado;
	private Boolean validado;
	private Date fechaValidado;

	/**
	 * Constructor por defecto.
	 */
	public MeritoPreferenteUsuario() {

	}

	/**
	 * Constructor con parametros.
	 * @param pcodNum .
	 * @param pmeritoPreferente .
	 * @param pusuario .
	 * @param pdescripcion .
	 * @param parchivo .
	 * @param pborrado .
	 * @param pfechaBorrado .
	 * @param pvalidado .
	 * @param pfechaValidado .
	 */
	public MeritoPreferenteUsuario(Integer pcodNum, MeritoPreferente pmeritoPreferente, UsuarioBolsaEmpleo pusuario, String pdescripcion,
			InputStream parchivo, Boolean pborrado, Date pfechaBorrado, Boolean pvalidado, Date pfechaValidado) {		
		this.codNum = pcodNum;
		this.meritoPreferente = pmeritoPreferente;
		this.usuario = pusuario;
		this.descripcion = pdescripcion;
		this.archivo = parchivo;
		this.borrado = pborrado;
		this.fechaBorrado = pfechaBorrado;
		this.validado = pvalidado;
		this.fechaValidado = pfechaValidado;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia .
	 */
	public MeritoPreferenteUsuario(MeritoPreferenteUsuario copia) {
		this.codNum = copia.codNum;
		this.meritoPreferente = copia.meritoPreferente;
		this.usuario = copia.usuario;
		this.descripcion = copia.descripcion;
		this.archivo = copia.archivo;
		this.borrado = copia.borrado;
		this.fechaBorrado = copia.fechaBorrado;
		this.validado = copia.validado;
		this.fechaValidado = copia.fechaValidado;
	}

	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public MeritoPreferente getMeritoPreferente() {
		return this.meritoPreferente;
	}
	
	public void setMeritoPreferente(MeritoPreferente merito) {
		this.meritoPreferente = merito;
	}
	
	public UsuarioBolsaEmpleo getUsuario() {
		return this.usuario;
	}
	
	public void setUsuario(UsuarioBolsaEmpleo usuario) {
		this.usuario = usuario;
	}
		
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public InputStream getArchivo() {
		return archivo;
	}

	public void setArchivo(InputStream archivo) {
		this.archivo = archivo;
	}
	
	public boolean getBorrado() {
		return borrado;
	}

	public void setBorrado(boolean borrado) {
		this.borrado = borrado;
	}
	
	public Date getFechaBorrado() {
		return fechaBorrado;
	}

	public void setFechaBorrado(Date fechaBorrado) {
		this.fechaBorrado = fechaBorrado;
	}
	
	public boolean getValidado() {
		return validado;
	}

	public void setValidado(boolean validado) {
		this.validado = validado;
	}
	
	public Date getFechaValidado() {
		return fechaValidado;
	}

	public void setFechaValidado(Date fechaValidado) {
		this.fechaValidado = fechaValidado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ApartadoBaremacion [codNum=" + codNum + ", meritoPreferente=" + meritoPreferente + ", usuario=" + usuario 
				+ ", descripcion=" + descripcion + ", archivo=" + archivo + ", borrado=" + borrado + ", fechaBorrado=" + fechaBorrado 
				+ ", validado=" + validado + ", fechaValidado=" + fechaValidado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((meritoPreferente == null) ? 0 : meritoPreferente.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((archivo == null) ? 0 : archivo.hashCode());
		result = prime * result + ((borrado == null) ? 0 : borrado.hashCode());
		result = prime * result + ((fechaBorrado == null) ? 0 : fechaBorrado.hashCode());
		result = prime * result + ((validado == null) ? 0 : validado.hashCode());
		result = prime * result + ((fechaValidado == null) ? 0 : fechaValidado.hashCode());
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
		MeritoPreferenteUsuario other = (MeritoPreferenteUsuario) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (meritoPreferente == null) {
			if (other.meritoPreferente != null) {
				return false;
			}
		} else if (!meritoPreferente.equals(other.meritoPreferente)) {
			return false;
		}
		if (usuario == null) {
			if (other.usuario != null) {
				return false;
			}
		} else if (!usuario.equals(other.usuario)) {
			return false;
		}
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (archivo == null) {
			if (other.archivo != null) {
				return false;
			}
		} else if (!archivo.equals(other.archivo)) {
			return false;
		}
		if (borrado == null) {
			if (other.borrado != null) {
				return false;
			}
		} else if (!borrado.equals(other.borrado)) {
			return false;
		}
		if (fechaBorrado == null) {
			if (other.fechaBorrado != null) {
				return false;
			}
		} else if (!fechaBorrado.equals(other.fechaBorrado)) {
			return false;
		}
		if (validado == null) {
			if (other.validado != null) {
				return false;
			}
		} else if (!validado.equals(other.validado)) {
			return false;
		}
		if (fechaValidado == null) {
			if (other.fechaValidado != null) {
				return false;
			}
		} else if (!fechaValidado.equals(other.fechaValidado)) {
			return false;
		}

		return true;
	}

}
