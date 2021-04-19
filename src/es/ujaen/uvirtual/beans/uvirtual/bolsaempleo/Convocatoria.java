package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.Date;


/** Clase convocatario de bolsa empleo.
 * @author ATISoluciones 
 */
public class Convocatoria implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String descripcion;
	private Date fechaCierre;
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
	 * @param pfechaCierre .
	 * @param pestado .
	 * @param pnumBolsasMaximo .
	 * @param pnumMeritosPorBloque .
	 */
	public Convocatoria(Integer pcodNum, String pdescripcion, Date pfechaCierre, String pestado, Integer pnumBolsasMaximo, Integer pnumMeritosPorBloque) {
		super();
		this.codNum = pcodNum;
		this.descripcion = pdescripcion;
		this.fechaCierre = pfechaCierre;
		this.estado = pestado;
		this.numBolsasMaximo = pnumBolsasMaximo;
		this.numMeritosPorBloque = pnumMeritosPorBloque;
	}
		
	/** Constructor copia.
	 * @param copia Titulación a copiar
	 */
	public Convocatoria(Convocatoria copia) {
		this.codNum = copia.codNum;
		this.descripcion = copia.descripcion;
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
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Convocatoria [codNum=" + codNum + ", descripcion=" + descripcion 
				+ ", fechaCierre=" + fechaCierre + ", estado=" + estado + ", numBolsasMaximo=" + numBolsasMaximo
				+ ", numMeritosPorBloque=" + numMeritosPorBloque + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;		
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((fechaCierre == null) ? 0 : fechaCierre.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((numBolsasMaximo == null) ? 0 : numBolsasMaximo.hashCode());
		result = prime * result + ((numMeritosPorBloque == null) ? 0 : numMeritosPorBloque.hashCode());
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
		
		return true;
	}	
}
