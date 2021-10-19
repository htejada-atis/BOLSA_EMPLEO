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
	private String idPlaza;
	private String justificacion;
	private String duracionPrevista;
	private String centroDestino;
	private String cuatrimestre;
	private String curso;
	private String estado;
	private Date fechaCreacion;
	private Date fechaAbierta;
	private Date fechaFinOferta;
	private Date fechaCerrada;
	private Date fechaNRI;
	private String uidUsuario;
	private transient InputStream horario;
	private transient InputStream nri;
	private Boolean abiertaVigente;
	private Boolean activa;
	
	
	/** Constructor por defecto.
	 */
	public PlazaOfertada() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros .
	 * @param pcodNum .
	 */
	public PlazaOfertada(Integer pcodNum) {
		super();
		this.codNum = pcodNum;
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
	 * @param pfechaFinOferta .
	 * @param pfechaCerrada .
	 * @param pfechaNRI .
	 * @param phorario .
	 * @param pnri .
	 */
	public PlazaOfertada(Integer pcodNum, Area parea, Dedicacion pdedicacion, String pjustificacion,
			String pduracionPrevista, String pcuatrimestre, String pcentroDestino, String pestado, Date pfechaCreacion,
			Date pfechaAbierta, Date pfechaFinOferta, Date pfechaCerrada, Date pfechaNRI, InputStream phorario, InputStream pnri) {
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
		this.fechaFinOferta = pfechaFinOferta;
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
		this.dedicacion = copia.dedicacion;
		this.justificacion = copia.justificacion;
		this.duracionPrevista = copia.duracionPrevista;
		this.cuatrimestre = copia.cuatrimestre;
		this.centroDestino = copia.centroDestino;
		this.estado = copia.estado;
		this.fechaCreacion = copia.fechaCreacion;
		this.fechaAbierta = copia.fechaAbierta;
		this.fechaFinOferta = copia.fechaFinOferta;
		this.fechaCerrada = copia.fechaCerrada;
		this.fechaNRI = copia.fechaNRI;
		this.horario = copia.horario;
		this.nri = copia.nri;
		this.abiertaVigente = copia.abiertaVigente;
		this.idPlaza = copia.idPlaza;
		this.activa = copia.activa;
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
	
	public Date getFechaFinOferta() {
		return fechaFinOferta;
	}
	
	public void setFechaFinOferta(Date fechaFinOferta) {
		this.fechaFinOferta = fechaFinOferta;
	}
	
	public Boolean isAbiertaVigente() {
		return abiertaVigente;
	}
	
	public void setAbiertaVigente(Boolean abiertaVigente) {
		this.abiertaVigente = abiertaVigente;
	}
	
	public String getIdPlaza() {
		return idPlaza;
	}
	
	public void setIdPlaza(String idPlaza) {
		this.idPlaza = idPlaza;
	}
	
	public Boolean isActiva() {
		return activa;
	}
	
	public void setActiva(Boolean activa) {
		this.activa = activa;
	}
	
	public String getCurso() {
		return curso;
	}
	
	public void setCurso(String curso) {
		this.curso = curso;
	}
	
	public String getUidUsuario() {
		return uidUsuario;
	}
	
	public void setUidUsuario(String uidUsuario) {
		this.uidUsuario = uidUsuario;
	}
	
	@Override
	public String toString() {
		return "PlazaOfertada [codNum=" + codNum + ", area=" + area + ", estado=" + estado + ", fechaCreacion=" + fechaCreacion + ", fechaAbierta=" 
				+ fechaAbierta + ", fechaFinOferta=" + fechaFinOferta + ", fechaCerrada=" + fechaCerrada + ", fechaFinOferta=" + fechaFinOferta
				+ ", abiertaVigente=" + abiertaVigente + ", idPlaza=" + idPlaza + ", activa=" + activa + ", curso=" + curso + ", uidUsuario=" + uidUsuario + "]";
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
		result = prime * result + ((fechaFinOferta == null) ? 0 : fechaFinOferta.hashCode());
		result = prime * result + ((fechaCerrada == null) ? 0 : fechaCerrada.hashCode());
		result = prime * result + ((fechaNRI == null) ? 0 : fechaNRI.hashCode());
		result = prime * result + ((horario == null) ? 0 : horario.hashCode());
		result = prime * result + ((nri == null) ? 0 : nri.hashCode());
		result = prime * result + ((abiertaVigente == null) ? 0 : abiertaVigente.hashCode());
		result = prime * result + ((idPlaza == null) ? 0 : idPlaza.hashCode());
		result = prime * result + ((activa == null) ? 0 : activa.hashCode());
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
		result = prime * result + ((uidUsuario == null) ? 0 : uidUsuario.hashCode());
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
		if (fechaFinOferta == null) {
			if (other.fechaFinOferta != null) {
				return false;
			}
		} else if (!fechaFinOferta.equals(other.fechaFinOferta)) {
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
		if (abiertaVigente == null) {
			if (other.abiertaVigente != null) {
				return false;
			}
		} else if (!abiertaVigente.equals(other.abiertaVigente)) {
			return false;
		}
		if (idPlaza == null) {
			if (other.idPlaza != null) {
				return false;
			}
		} else if (!idPlaza.equals(other.idPlaza)) {
			return false;
		}
		if (activa == null) {
			if (other.activa != null) {
				return false;
			}
		} else if (!activa.equals(other.activa)) {
			return false;
		}
		if (curso == null) {
			if (other.curso != null) {
				return false;
			}
		} else if (!curso.equals(other.curso)) {
			return false;
		}
		if (uidUsuario == null) {
			if (other.uidUsuario != null) {
				return false;
			}
		} else if (!uidUsuario.equals(other.uidUsuario)) {
			return false;
		}
		
		return true;
	}
	
}