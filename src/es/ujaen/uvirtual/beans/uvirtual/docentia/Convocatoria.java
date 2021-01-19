package es.ujaen.uvirtual.beans.uvirtual.docentia;

import java.io.Serializable;
import java.util.Date;

/** Clase convocatoria de docentia.
 * @author usig
 *
 */
public class Convocatoria implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idConvocatoria;
	private String nombreConvocatoria;
	private String estado;
	private String observaciones;
	private Date fechaLimite;
	private Date fechaComision;
	
	
	/** Constructor por defecto.
	 */
	public Convocatoria() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pidConvocatoria .
	 * @param pnombreConvocatoria .
	 * @param pestado .
	 * @param pobservaciones .
	 * @param pfechaLimite .
	 * @param pfechaComision .
	 */
	public Convocatoria(Integer pidConvocatoria, String pnombreConvocatoria, String pestado, String pobservaciones,
			Date pfechaLimite, Date pfechaComision) {
		super();
		this.idConvocatoria = pidConvocatoria;
		this.nombreConvocatoria = pnombreConvocatoria;
		this.estado = pestado;
		this.observaciones = pobservaciones;
		this.fechaLimite = pfechaLimite;
		this.fechaComision = pfechaComision;
	}
	
	/** Constructor copia.
	 * @param copia Convocatoria a copiar
	 */
	public Convocatoria(Convocatoria copia) {
		this.idConvocatoria = copia.idConvocatoria;
		this.nombreConvocatoria = copia.nombreConvocatoria;
		this.estado = copia.estado;
		this.observaciones = copia.observaciones;
		this.fechaLimite = copia.fechaLimite;
		this.fechaComision = copia.fechaComision;
	}
	
	public Integer getIdConvocatoria() {
		return idConvocatoria;
	}
	
	public void setIdConvocatoria(Integer idConvocatoria) {
		this.idConvocatoria = idConvocatoria;
	}
	
	public String getNombreConvocatoria() {
		return nombreConvocatoria;
	}
	
	public void setNombreConvocatoria(String nombreConvocatoria) {
		this.nombreConvocatoria = nombreConvocatoria;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	public Date getFechaLimite() {
		return fechaLimite;
	}
	
	public void setFechaLimite(Date fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public Date getFechaComision() {
		return fechaComision;
	}

	public void setFechaComision(Date fechaComision) {
		this.fechaComision = fechaComision;
	}
	
	@Override
	public String toString() {
		return "Convocatoria [idConvocatoria=" + idConvocatoria + ", nombreConvocatoria=" + nombreConvocatoria
				+ ", estado=" + estado + ", observaciones=" + observaciones + ", fechaLimite=" + fechaLimite
				+ ", fechaComision=" + fechaComision + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((fechaComision == null) ? 0 : fechaComision.hashCode());
		result = prime * result + ((fechaLimite == null) ? 0 : fechaLimite.hashCode());
		result = prime * result + ((idConvocatoria == null) ? 0 : idConvocatoria.hashCode());
		result = prime * result + ((nombreConvocatoria == null) ? 0 : nombreConvocatoria.hashCode());
		result = prime * result + ((observaciones == null) ? 0 : observaciones.hashCode());
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
		Convocatoria other = (Convocatoria) obj;
		if (estado == null) {
			if (other.estado != null) {
				return false;
			}
		} else if (!estado.equals(other.estado)) {
			return false;
		}
		if (fechaComision == null) {
			if (other.fechaComision != null) {
				return false;
			}
		} else if (!fechaComision.equals(other.fechaComision)) {
			return false;
		}
		if (fechaLimite == null) {
			if (other.fechaLimite != null) {
				return false;
			}
		} else if (!fechaLimite.equals(other.fechaLimite)) {
			return false;
		}
		if (nombreConvocatoria == null) {
			if (other.nombreConvocatoria != null) {
				return false;
			}
		} else if (!nombreConvocatoria.equals(other.nombreConvocatoria)) {
			return false;
		}
		if (observaciones == null) {
			if (other.observaciones != null) {
				return false;
			}
		} else if (!observaciones.equals(other.observaciones)) {
			return false;
		}
		return true;
	}
}
