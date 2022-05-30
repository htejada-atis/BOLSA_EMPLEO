package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;


/** Clase convocatario de bolsa empleo.
 * @author ATISoluciones 2021
 */
public class Convocatoria implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String descripcion;
	private String curso;
	private Date fechaCierre;
	private Date fechaFinalizacion;
	private String estado;
	private Integer numBolsasMaximo;
	private Integer numMeritosPorBloque;
	
	/** Constructor por defecto.
	 */
	public Convocatoria() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pdescripcion .
	 * @param pcurso .
	 * @param pfechaCierre .
	 * @param pestado .
	 * @param pnumBolsasMaximo .
	 * @param pnumMeritosPorBloque .
	 */
	public Convocatoria(Integer pcodNum, String pdescripcion, String pcurso, Date pfechaCierre, String pestado, Integer pnumBolsasMaximo, Integer pnumMeritosPorBloque) {
		super();
		this.codNum = pcodNum;
		this.descripcion = pdescripcion;
		this.curso = pcurso;
		this.fechaCierre = pfechaCierre;
		this.estado = pestado;
		this.numBolsasMaximo = pnumBolsasMaximo;
		this.numMeritosPorBloque = pnumMeritosPorBloque;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pestado .
	 */
	public Convocatoria(Integer pcodNum, String pestado) {
		super();
		this.codNum = pcodNum;
		this.estado = pestado;
	}
		
	/** Constructor copia.
	 * @param copia Convocatoria a copiar
	 */
	public Convocatoria(Convocatoria copia) {
		this.codNum = copia.codNum;
		this.descripcion = copia.descripcion;
		this.curso = copia.curso;
		this.fechaCierre = copia.fechaCierre;
		this.estado = copia.estado;
		this.numBolsasMaximo = copia.numBolsasMaximo;
		this.numMeritosPorBloque = copia.numMeritosPorBloque;
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
	
	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}
	
	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Integer getNumBolsasMaximo() {
		return numBolsasMaximo;
	}

	public void setNumBolsasMaximo(Integer numBolsasMaximo) {
		this.numBolsasMaximo = numBolsasMaximo;
	}
	
	public Integer getNumMeritosPorBloque() {
		return numMeritosPorBloque;
	}

	public void setNumMeritosPorBloque(Integer numMeritosPorBloque) {
		this.numMeritosPorBloque = numMeritosPorBloque;
	}
	
	public Date getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Convocatoria [codNum=" + codNum + ", descripcion=" + descripcion + ", curso=" + curso + ", fechaCierre=" + fechaCierre 
				+ ", estado=" + estado + ", numBolsasMaximo=" + numBolsasMaximo + ", numMeritosPorBloque=" + numMeritosPorBloque 
				+ ", fechaFinalizacion=" + fechaFinalizacion + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
		result = prime * result + ((fechaCierre == null) ? 0 : fechaCierre.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((numBolsasMaximo == null) ? 0 : numBolsasMaximo.hashCode());
		result = prime * result + ((numMeritosPorBloque == null) ? 0 : numMeritosPorBloque.hashCode());
		result = prime * result + ((fechaFinalizacion == null) ? 0 : fechaFinalizacion.hashCode());
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
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!fechaCierre.equals(other.fechaCierre)) {
			return false;
		}
		if (curso == null) {
			if (other.curso != null) {
				return false;
			}
		} else if (!curso.equals(other.curso)) {
			return false;
		}
		if (fechaCierre == null) {
			if (other.fechaCierre != null) {
				return false;
			}
		} else if (!fechaCierre.equals(other.fechaCierre)) {
			return false;
		}
		if (estado == null) {
			if (other.estado != null) {
				return false;
			}
		} else if (!estado.equals(other.estado)) {
			return false;
		}
		if (numBolsasMaximo == null) {
			if (other.numBolsasMaximo != null) {
				return false;
			}
		} else if (!numBolsasMaximo.equals(other.numBolsasMaximo)) {
			return false;
		}
		if (numMeritosPorBloque == null) {
			if (other.numMeritosPorBloque != null) {
				return false;
			}
		} else if (!numMeritosPorBloque.equals(other.numMeritosPorBloque)) {
			return false;
		}
		if (fechaFinalizacion == null) {
			if (other.fechaFinalizacion != null) {
				return false;
			}
		} else if (!fechaFinalizacion.equals(other.fechaFinalizacion)) {
			return false;
		}
		
		return true;
	}
	
}
