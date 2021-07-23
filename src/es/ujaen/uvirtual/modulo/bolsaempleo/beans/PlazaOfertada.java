package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;

/** Clase plaza ofertada para la contratación .
 * @author atis
 */
public class PlazaOfertada implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Area area;
	private String estado;
	private Date fechaCreacion;
	private Date fechaAbierta;
	private Date fechaCerrada;
	

	/** Constructor por defecto.
	 */
	public PlazaOfertada() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param parea .
	 * @param pestado .
	 * @param pfechaCreacion .
	 * @param pfechaAbierta .
	 * @param pfechaCerrada .
	 */
	public PlazaOfertada(Integer pcodNum, Area parea, String pestado, Date pfechaCreacion, Date pfechaAbierta, Date pfechaCerrada) {
		super();
		this.codNum = pcodNum;
		this.area = parea;
		this.estado = pestado;
		this.fechaCreacion = pfechaCreacion;
		this.fechaAbierta = pfechaAbierta;
		this.fechaCerrada = pfechaCerrada;
	}
	
	/** Constructor copia.
	 * @param copia Convocatoria a copiar
	 */
	public PlazaOfertada(PlazaOfertada copia) {
		this.codNum = copia.codNum;
		this.area = new Area(copia.area);
		this.estado = copia.estado;
		this.fechaCreacion = copia.fechaCreacion;
		this.fechaAbierta = copia.fechaAbierta;
		this.fechaCerrada = copia.fechaCerrada;
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
	
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaAbierta() {
		return fechaAbierta;
	}

	public void setFechaAbierta(Date fechaAbierta) {
		this.fechaAbierta = fechaAbierta;
	}

	public Date getFechaCerrada() {
		return fechaCerrada;
	}

	public void setFechaCerrada(Date fechaCerrada) {
		this.fechaCerrada = fechaCerrada;
	}
	
	@Override
	public String toString() {
		return "BolsaEmpleo [codNum=" + codNum + ", area=" + area + ", estado=" + estado + ", fechaCreacion=" + fechaCreacion 
				+ ", fechaAbierta=" + fechaAbierta + ", fechaCerrada=" + fechaCerrada + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime * result + ((fechaAbierta == null) ? 0 : fechaAbierta.hashCode());
		result = prime * result + ((fechaCerrada == null) ? 0 : fechaCerrada.hashCode());
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
		
		PlazaOfertada other = (PlazaOfertada) obj;
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
		if (fechaCreacion == null) {
			if (other.fechaCreacion != null) {
				return false;
			}
		} else if (!fechaCreacion.equals(other.fechaCreacion)) {
			return false;
		}
		if (fechaAbierta == null) {
			if (other.fechaAbierta != null) {
				return false;
			}
		} else if (!fechaAbierta.equals(other.fechaAbierta)) {
			return false;
		}
		if (fechaCerrada == null) {
			if (other.fechaCerrada != null) {
				return false;
			}
		} else if (!fechaCerrada.equals(other.fechaCerrada)) {
			return false;
		}
		
		return true;
	}
}

