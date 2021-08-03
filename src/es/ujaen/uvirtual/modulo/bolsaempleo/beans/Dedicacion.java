package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;

/** Clase dedicación para la contratación .
 * @author atis
 */
public class Dedicacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String texto;
	private Double sueldo;
	private Date fechaVigencia;
	private Boolean activa;
	

	/** Constructor por defecto.
	 */
	public Dedicacion() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param ptexto .
	 * @param psueldo .
	 * @param pfechaVigencia .
	 * @param pactiva .
	 */
	public Dedicacion(Integer pcodNum, String ptexto, Double psueldo, Date pfechaVigencia, Boolean pactiva) {
		super();
		this.codNum = pcodNum;
		this.texto = ptexto;
		this.sueldo = psueldo;
		this.fechaVigencia = pfechaVigencia;
		this.activa = pactiva;
	}
	
	/** Constructor copia.
	 * @param copia Dedicacion a copiar
	 */
	public Dedicacion(Dedicacion copia) {
		this.codNum = copia.codNum;
		this.texto = copia.texto;
		this.sueldo = copia.sueldo;
		this.fechaVigencia = copia.fechaVigencia;
		this.activa = copia.activa;
	}
	
	public Integer getCodNum() {
		return codNum;
	}
	
	public void setCodNum(Integer idBolsa) {
		this.codNum = idBolsa;
	}
	
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Double getSueldo() {
		return sueldo;
	}

	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}

	public Date getFechaVigencia() {
		return fechaVigencia;
	}

	public void setFechaVigencia(Date fechaCreacion) {
		this.fechaVigencia = fechaCreacion;
	}

	public Boolean isActiva() {
		return activa;
	}

	public void setActiva(Boolean activa) {
		this.activa = activa;
	}
	
	@Override
	public String toString() {
		return "Dedicación [codNum=" + codNum + ", texto=" + texto + ", sueldo=" + sueldo 
				+ ", fechaVigencia=" + fechaVigencia + ", activa=" + activa + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((texto == null) ? 0 : texto.hashCode());
		result = prime * result + ((sueldo == null) ? 0 : sueldo.hashCode());
		result = prime * result + ((fechaVigencia == null) ? 0 : fechaVigencia.hashCode());
		result = prime * result + ((activa == null) ? 0 : activa.hashCode());
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
		
		Dedicacion other = (Dedicacion) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (texto == null) {
			if (other.texto != null) {
				return false;
			}
		} else if (!texto.equals(other.texto)) {
			return false;
		}
		if (sueldo == null) {
			if (other.sueldo != null) {
				return false;
			}
		} else if (!sueldo.equals(other.sueldo)) {
			return false;
		}
		if (fechaVigencia == null) {
			if (other.fechaVigencia != null) {
				return false;
			}
		} else if (!fechaVigencia.equals(other.fechaVigencia)) {
			return false;
		}
		if (activa == null) {
			if (other.activa != null) {
				return false;
			}
		} else if (!activa.equals(other.activa)) {
			return false;
		}
		
		return true;
	}
}