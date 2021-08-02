package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/** Clase plaza ofertada para la contratación .
 * @author atis
 */
public class PlazaOfertada implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Area area;
	private Dedicacion dedicacion;
	private String justificacion;
	private String duracionPrevista;
	private String cuatrimestre;
	private String centroDestino;
	private String estado;
	private Date fechaCreacion;
	private Date fechaAbierta;
	private Date fechaCerrada;
	private Date fechaNRI;
	private transient InputStream horario;
	private transient InputStream nri;
	
	
	/** Constructor por defecto.
	 */
	public PlazaOfertada() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros .
	 * @param pcodNum .
	 * @param parea .
	 * @param pdedicacion .
	 * @param pjustificacion .
	 * @param pduracionPrevista .
	 * @param pcuatrimestre .
	 * @param pcentroDestino .
	 * @param pestado .
	 * @param pfechaCreacion .
	 * @param pfechaAbierta .
	 * @param pfechaCerrada .
	 * @param pfechaNRI .
	 * @param phorario .
	 * @param pnri .
	 */
	public PlazaOfertada(Integer pcodNum, Area parea, Dedicacion pdedicacion, String pjustificacion,
			String pduracionPrevista, String pcuatrimestre, String pcentroDestino, String pestado, Date pfechaCreacion,
			Date pfechaAbierta, Date pfechaCerrada, Date pfechaNRI, InputStream phorario, InputStream pnri) {
		super();
		this.codNum = pcodNum;
		this.area = parea;
		this.dedicacion = pdedicacion;
		this.justificacion = pjustificacion;
		this.duracionPrevista = pduracionPrevista;
		this.cuatrimestre = pcuatrimestre;
		this.centroDestino = pcentroDestino;
		this.estado = pestado;
		this.fechaCreacion = pfechaCreacion;
		this.fechaAbierta = pfechaAbierta;
		this.fechaCerrada = pfechaCerrada;
		this.fechaNRI = pfechaNRI;
		this.horario = phorario;
		this.nri = pnri;
	}
	
	/** Constructor copia.
	 * @param copia PlazaOfertada a copiar
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
	
	public Dedicacion getDedicacion() {
		return dedicacion;
	}

	public void setDedicacion(Dedicacion dedicacion) {
		this.dedicacion = dedicacion;
	}

	public String getJustificacion() {
		return justificacion;
	}

	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}

	public String getDuracionPrevista() {
		return duracionPrevista;
	}

	public void setDuracionPrevista(String duracionPrevista) {
		this.duracionPrevista = duracionPrevista;
	}

	public String getCuatrimestre() {
		return cuatrimestre;
	}

	public void setCuatrimestre(String cuatrimestre) {
		this.cuatrimestre = cuatrimestre;
	}

	public String getCentroDestino() {
		return centroDestino;
	}

	public void setCentroDestino(String centroDestino) {
		this.centroDestino = centroDestino;
	}

	public Date getFechaNRI() {
		return fechaNRI;
	}

	public void setFechaNRI(Date fechaNRI) {
		this.fechaNRI = fechaNRI;
	}

	public InputStream getHorario() {
		return horario;
	}

	public void setHorario(InputStream horario) {
		this.horario = horario;
	}

	public InputStream getNri() {
		return nri;
	}

	public void setNri(InputStream nri) {
		this.nri = nri;
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
		result = prime * result + ((dedicacion == null) ? 0 : dedicacion.hashCode());
		result = prime * result + ((justificacion == null) ? 0 : justificacion.hashCode());
		result = prime * result + ((duracionPrevista == null) ? 0 : duracionPrevista.hashCode());
		result = prime * result + ((cuatrimestre == null) ? 0 : cuatrimestre.hashCode());
		result = prime * result + ((centroDestino == null) ? 0 : centroDestino.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime * result + ((fechaAbierta == null) ? 0 : fechaAbierta.hashCode());
		result = prime * result + ((fechaCerrada == null) ? 0 : fechaCerrada.hashCode());
		result = prime * result + ((fechaNRI == null) ? 0 : fechaNRI.hashCode());
		result = prime * result + ((horario == null) ? 0 : horario.hashCode());
		result = prime * result + ((nri == null) ? 0 : nri.hashCode());
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
		if (dedicacion == null) {
			if (other.dedicacion != null) {
				return false;
			}
		} else if (!dedicacion.equals(other.dedicacion)) {
			return false;
		}
		if (justificacion == null) {
			if (other.justificacion != null) {
				return false;
			}
		} else if (!justificacion.equals(other.justificacion)) {
			return false;
		}
		if (duracionPrevista == null) {
			if (other.duracionPrevista != null) {
				return false;
			}
		} else if (!duracionPrevista.equals(other.duracionPrevista)) {
			return false;
		}
		if (cuatrimestre == null) {
			if (other.cuatrimestre != null) {
				return false;
			}
		} else if (!cuatrimestre.equals(other.cuatrimestre)) {
			return false;
		}
		if (centroDestino == null) {
			if (other.centroDestino != null) {
				return false;
			}
		} else if (!centroDestino.equals(other.centroDestino)) {
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
		if (fechaNRI == null) {
			if (other.fechaNRI != null) {
				return false;
			}
		} else if (!fechaNRI.equals(other.fechaNRI)) {
			return false;
		}
		if (horario == null) {
			if (other.horario != null) {
				return false;
			}
		} else if (!horario.equals(other.horario)) {
			return false;
		}
		if (nri == null) {
			if (other.nri != null) {
				return false;
			}
		} else if (!nri.equals(other.nri)) {
			return false;
		}
		
		return true;
	}
}

