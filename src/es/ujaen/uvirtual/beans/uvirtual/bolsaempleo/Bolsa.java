package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.Date;

/** Clase bolsa empleo de bolsaempleo.
 * @author atis
 */
public class Bolsa implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Area area;
	private String estado;
	private Boolean baremable;
	private Date fechaActualizacion;
	private Date fechaBloqueo;
	private Date fechaDesBloqueo;
	
	/** Constructor por defecto.
	 */
	public Bolsa() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pidBolsaEmpleo .
	 * @param parea .
	 * @param pestado .
	 * @param pbaremable .
	 * @param pfechaActualizacion .
	 * @param pfechaBloqueo .
	 * @param pfechaDesBloqueo .
	 */
	public Bolsa(Integer pidBolsaEmpleo, Area parea, String pestado, Boolean pbaremable, 
			Date pfechaActualizacion, Date pfechaBloqueo, Date pfechaDesBloqueo) {
		super();
		this.codNum = pidBolsaEmpleo;
		this.area = parea;
		this.estado = pestado;
		this.baremable = pbaremable;
		this.fechaActualizacion = pfechaActualizacion;
		this.fechaBloqueo = pfechaBloqueo;
		this.fechaDesBloqueo = pfechaDesBloqueo;
	}
	
	/** Constructor copia.
	 * @param copia Convocatoria a copiar
	 */
	public Bolsa(Bolsa copia) {
		this.codNum = copia.codNum;
		this.area = new Area(copia.area);
		this.estado = copia.estado;
		this.baremable = copia.baremable;
		this.fechaActualizacion = copia.fechaActualizacion;
		this.fechaBloqueo = copia.fechaBloqueo;
		this.fechaDesBloqueo = copia.fechaDesBloqueo;
	}
	
	public Integer getCodNum() {
		return codNum;
	}
	
	public void setCodNum(Integer idBolsa) {
		this.codNum = idBolsa;
	}
	
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Boolean getBaremable() {
		return baremable;
	}
	
	public void setBaremable(Boolean baremable) {
		this.baremable = baremable;
	}
	
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}
	
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	
	public Date getFechaBloqueo() {
		return fechaBloqueo;
	}
	
	public void setFechaBloqueo(Date fechaBloqueo) {
		this.fechaBloqueo = fechaBloqueo;
	}
	
	public Date getFechaDesBloqueo() {
		return fechaDesBloqueo;
	}
	
	public void setFechaDesBloqueo(Date fechaDesBloqueo) {
		this.fechaDesBloqueo = fechaDesBloqueo;
	}
	
	@Override
	public String toString() {
		return "BolsaEmpleo [idBolsaEmpleo=" + codNum + ", area=" + area.getDescripcion()
				+ ", estado=" + estado + ", baremable=" + baremable + ", fechaActualizacion=" + fechaActualizacion
				+ ", fechaBloqueo=" + fechaBloqueo + ", fechaDesBloqueo=" + fechaDesBloqueo + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((baremable == null) ? 0 : baremable.hashCode());
		result = prime * result + ((fechaActualizacion == null) ? 0 : fechaActualizacion.hashCode());
		result = prime * result + ((fechaBloqueo == null) ? 0 : fechaBloqueo.hashCode());
		result = prime * result + ((fechaDesBloqueo == null) ? 0 : fechaDesBloqueo.hashCode());
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
		
		Bolsa other = (Bolsa) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (area == null) {
			if (other.area != null) {
				return false;
			}
		} else if (!area.equals(other.area)) {
			return false;
		}
		if (estado == null) {
			if (other.estado != null) {
				return false;
			}
		} else if (!estado.equals(other.estado)) {
			return false;
		}
		if (baremable == null) {
			if (other.baremable != null) {
				return false;
			}
		} else if (!baremable.equals(other.baremable)) {
			return false;
		}
		if (fechaActualizacion == null) {
			if (other.fechaActualizacion != null) {
				return false;
			}
		} else if (!fechaActualizacion.equals(other.fechaActualizacion)) {
			return false;
		}
		if (fechaBloqueo == null) {
			if (other.fechaBloqueo != null) {
				return false;
			}
		} else if (!fechaBloqueo.equals(other.fechaBloqueo)) {
			return false;
		}
		if (fechaDesBloqueo == null) {
			if (other.fechaDesBloqueo != null) {
				return false;
			}
		} else if (!fechaDesBloqueo.equals(other.fechaDesBloqueo)) {
			return false;
		}
		
		return true;
	}
}

