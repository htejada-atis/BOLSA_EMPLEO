package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;


/** Clase solicitud de bolsa empleo.
 * @author ATISoluciones 2021
 */
public class Solicitud implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private UsuarioBolsaEmpleo usuario;
	private Convocatoria convocatoria;
	private String estado;
	private Date fechaConfirmacion;
	private InputStream archivo;
	private Boolean excluido;
	private Date fechaExclusion;
	private String razonExclusion;
	
	/** Constructor por defecto.
	 */
	public Solicitud() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pusuario .
	 * @param pconvocatoria .
	 * @param pestado .
	 */
	public Solicitud(Integer pcodNum, UsuarioBolsaEmpleo pusuario, Convocatoria pconvocatoria, String pestado) {
		super();
		this.codNum = pcodNum;
		this.usuario = pusuario;
		this.convocatoria = pconvocatoria;
		this.estado = pestado;			
	}
		
	/** Constructor copia.
	 * @param copia Solicitud a copiar
	 */
	public Solicitud(Solicitud copia) {
		this.codNum = copia.codNum;
		this.usuario = new UsuarioBolsaEmpleo(copia.usuario);
		this.convocatoria = new Convocatoria(copia.convocatoria);
		this.estado = copia.estado;
		this.excluido = copia.excluido;
		this.fechaExclusion = copia.fechaExclusion;
		this.razonExclusion = copia.razonExclusion;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public UsuarioBolsaEmpleo getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBolsaEmpleo usuario) {
		this.usuario = usuario;
	}

	public Convocatoria getConvocatoria() {
		return convocatoria;
	}

	public void setConvocatoria(Convocatoria convocatoria) {
		this.convocatoria = convocatoria;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Date getFechaConfirmacion() {
		return fechaConfirmacion;
	}
	
	public void setFechaConfirmacion(Date fecha) {
		this.fechaConfirmacion = fecha;
	}
	
	public InputStream getArchivo() {
		return archivo;
	}
	
	public void setArchivo(InputStream archivo) {
		this.archivo = archivo;
	}
	
	public Boolean getExcluido() {
		return this.excluido;
	}
	
	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}
	
	public Date getFechaExclusion() {
		return this.fechaExclusion;
	}
	
	public void setFechaExclusion(Date fechaExclusion) {
		this.fechaExclusion = fechaExclusion;
	}
	
	public String getRazonExclusion() {
		return this.razonExclusion;
	}
	
	public void setRazonExclusion(String razonExclusion) {
		this.razonExclusion = razonExclusion;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Solicitud [codNum=" + codNum + ", usuario=" + usuario + ", convocatoria=" + convocatoria 
				+ ", estado=" + estado + ", fechaConfirmacion=" + fechaConfirmacion + ", exluido=" + excluido 
				+ ", fechaExclusion=" + fechaExclusion + ", razonExclusion=" + razonExclusion + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;		
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((convocatoria == null) ? 0 : convocatoria.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((fechaConfirmacion == null) ? 0 : fechaConfirmacion.hashCode());
		result = prime * result + ((archivo == null) ? 0 : archivo.hashCode());
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());
		result = prime * result + ((fechaExclusion == null) ? 0 : fechaExclusion.hashCode());
		result = prime * result + ((razonExclusion == null) ? 0 : razonExclusion.hashCode());
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
		Solicitud other = (Solicitud) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (usuario == null) {
			if (other.usuario != null) {
				return false;
			}
		} else if (!usuario.equals(other.usuario)) {
			return false;
		}
		if (convocatoria == null) {
			if (other.convocatoria != null) {
				return false;
			}
		} else if (!convocatoria.equals(other.convocatoria)) {
			return false;
		}		
		if (estado == null) {
			if (other.estado != null) {
				return false;
			}
		} else if (!estado.equals(other.estado)) {
			return false;
		}
		if (fechaConfirmacion == null) {
			if (other.fechaConfirmacion != null) {
				return false;
			}
		} else if (!fechaConfirmacion.equals(other.fechaConfirmacion)) {
			return false;
		}
		if (archivo == null) {
			if (other.archivo != null) {
				return false;
			}
		} else if (!archivo.equals(other.archivo)) {
			return false;
		}
		if (excluido == null) {
			if (other.excluido != null) {
				return false;
			}
		} else if (!excluido.equals(other.excluido)) {
			return false;
		}
		if (fechaExclusion == null) {
			if (other.fechaExclusion != null) {
				return false;
			}
		} else if (!fechaExclusion.equals(other.fechaExclusion)) {
			return false;
		}
		if (razonExclusion == null) {
			if (other.razonExclusion != null) {
				return false;
			}
		} else if (!razonExclusion.equals(other.razonExclusion)) {
			return false;
		}
		
		return true;
	}	
}
