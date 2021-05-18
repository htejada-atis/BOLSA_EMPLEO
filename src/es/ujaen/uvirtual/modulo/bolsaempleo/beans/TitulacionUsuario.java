package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.InputStream;
import java.io.Serializable;

/** Clase titulación de bolsa empleo.
 * @author fcampos
 *
 */
public class TitulacionUsuario implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String descripcion;
	private String otratitulacion;
	private transient InputStream archivo;
	private Titulacion titulacion;
	private UsuarioBolsaEmpleo usuario;
	private Boolean borrado;
	private Boolean validada;
	
	/** Constructor por defecto.
	 */
	public TitulacionUsuario() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pdescripcion .
	 * @param parchivo .
	 * @param ptitulacion .
	 * @param pusuario .
	 */
	public TitulacionUsuario(Integer pcodNum, String pdescripcion, InputStream parchivo, Titulacion ptitulacion, UsuarioBolsaEmpleo pusuario) {
		super();
		this.codNum = pcodNum;
		this.descripcion = pdescripcion;
		this.archivo = parchivo;
		this.titulacion = ptitulacion;
		this.usuario = pusuario;
	}
	
	/** Constructor con parametros.
	 * @param pdescripcion .
	 * @param parchivo .
	 * @param ptitulacion .
	 * @param pusuario .
	 */
	public TitulacionUsuario(String pdescripcion, InputStream parchivo, Titulacion ptitulacion, UsuarioBolsaEmpleo pusuario) {
		super();
		this.descripcion = pdescripcion;
		this.archivo = parchivo;
		this.titulacion = ptitulacion;
		this.usuario = pusuario;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 */
	public TitulacionUsuario(Integer pcodNum) {
		super();
		this.codNum = pcodNum;
	}
	
	/** Constructor copia.
	 * @param copia Titulación a copiar
	 */
	public TitulacionUsuario(TitulacionUsuario copia) {
		this.codNum = copia.codNum;
		this.descripcion = copia.descripcion;
		this.otratitulacion = copia.otratitulacion;
		this.archivo = copia.archivo;
		this.titulacion = copia.titulacion;
		this.usuario = copia.usuario;
		this.borrado = copia.borrado;
		this.validada = copia.validada;
	}
	
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getOtraTitulacion() {
		return otratitulacion;
	}

	public void setOtraTitulacion(String potratitulacion) {
		this.otratitulacion = potratitulacion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public InputStream getArchivo() {
		return archivo;
	}
	
	public void setArchivo(InputStream archivo) {
		this.archivo = archivo;
	}
	
	public Titulacion getTitulacion() {
		return titulacion;
	}
	
	public void setTitulacion(Titulacion titulacion) {
		this.titulacion = titulacion;
	}
	
	public UsuarioBolsaEmpleo getUsuario() {
		return usuario;
	}
	
	public void setUsuario(UsuarioBolsaEmpleo usuario) {
		this.usuario = usuario;
	}
	
	public Boolean getBorrado() {
		return borrado;
	}
	
	public void setBorrado(Boolean borrado) {
		this.borrado = borrado;
	}
	
	public Boolean getValidada() {
		return validada;
	}
	
	public void setValidada(Boolean validada) {
		this.validada = validada;
	}
	
	@Override
	public String toString() {
		return "Titulación Usuario [codNum=" + codNum + ", descripcion=" + descripcion + ", otratitulacion=" + otratitulacion + ", usuario=" + usuario + ", titulacion="
				+ titulacion + ", archivo=" + archivo + ", borrado=" + borrado + ", validada=" + validada + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((otratitulacion == null) ? 0 : otratitulacion.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((titulacion == null) ? 0 : titulacion.hashCode());
		result = prime * result + ((archivo == null) ? 0 : archivo.hashCode());
		result = prime * result + ((borrado == null) ? 0 : borrado.hashCode());
		result = prime * result + ((validada == null) ? 0 : validada.hashCode());
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
		TitulacionUsuario other = (TitulacionUsuario) obj;
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (otratitulacion == null) {
			if (other.otratitulacion != null) {
				return false;
			}
		} else if (!otratitulacion.equals(other.otratitulacion)) {
			return false;
		}
		if (usuario == null) {
			if (other.usuario != null) {
				return false;
			}
		} else if (!usuario.equals(other.usuario)) {
			return false;
		}
		if (titulacion == null) {
			if (other.titulacion != null) {
				return false;
			}
		} else if (!titulacion.equals(other.titulacion)) {
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
		if (validada == null) {
			if (other.validada != null) {
				return false;
			}
		} else if (!validada.equals(other.validada)) {
			return false;
		}
		
		return true;
	}
}
