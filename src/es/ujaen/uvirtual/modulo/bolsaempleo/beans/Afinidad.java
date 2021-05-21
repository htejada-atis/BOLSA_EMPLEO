package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.util.Date;

/**
 * Clase afinidades de bolsa empleo.
 * 
 * @author ATISoluciones
 */
public class Afinidad {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String codigo;
	private String descripcion;
	private Double modulacion;
	private Boolean borrada;
	private Date fechaBorrada;
	
	/** Constructor por defecto.
	 */
	public Afinidad() {
		super();
		this.codNum = null;
		this.codigo = "";
		this.descripcion = "";
		this.modulacion = null;
		this.borrada = null;
		this.fechaBorrada = null;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pcodigo .
	 * @param pdescripcion .
	 * @param pmodulacion .
	 * @param pborrada .
	 * @param pfechaBorrada .
	 */
	public Afinidad(Integer pcodNum, String pcodigo, String pdescripcion, Double pmodulacion, Boolean pborrada, Date pfechaBorrada) {
		super();
		this.codNum = pcodNum;
		this.codigo = pcodigo;
		this.descripcion = pdescripcion;
		this.modulacion = pmodulacion;
		this.borrada = pborrada;
		this.fechaBorrada = pfechaBorrada;
	}

	/** Constructor copia.
	 * @param copia Titulación a copiar
	 */
	public Afinidad(Afinidad copia) {
		this.codNum = copia.codNum;
		this.codigo = copia.codigo;
		this.descripcion = copia.descripcion;
		this.modulacion = copia.modulacion;
		this.borrada = copia.borrada;
		this.fechaBorrada = copia.fechaBorrada;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String pcodigo) {
		this.codigo = pcodigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Double getModulacion() {
		return modulacion;
	}

	public void setModulacion(Double pmodulacion) {
		this.modulacion = pmodulacion;
	}
	
	public void setBorrada(Boolean borrada) {
		this.borrada = borrada;
	}
	
	public Boolean getBorrada() {
		return this.borrada;
	}
	
	public void setFechaBorrada(Date f) {
		this.fechaBorrada = f;
	}
	
	public Date getFechaBorrada() {
		return this.fechaBorrada;
	}
	
	public String getCodigoDescripcion() {
		return this.getCodigo() + " - " + this.getDescripcion() + " (" + this.getModulacion() + " %)"; 
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Afinidad [codNum=" + codNum + ", codigo=" + codigo + ", descripcion=" + descripcion + ", modulacion="
				+ modulacion + ", borrada=" + borrada + ", fechaBorrada=" + fechaBorrada + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((modulacion == null) ? 0 : modulacion.hashCode());
		result = prime * result + ((borrada == null) ? 0 : borrada.hashCode());
		result = prime * result + ((fechaBorrada == null) ? 0 : fechaBorrada.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings({ "checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity" })
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
		Afinidad other = (Afinidad) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (modulacion == null) {
			if (other.modulacion != null) {
				return false;
			}
		} else if (!modulacion.equals(other.modulacion)) {
			return false;
		}
		if (borrada == null) {
			if (other.borrada != null) {
				return false;
			}
		} else if (!borrada.equals(other.borrada)) {
			return false;
		}
		if (fechaBorrada == null) {
			if (other.fechaBorrada != null) {
				return false;
			}
		} else if (!fechaBorrada.equals(other.fechaBorrada)) {
			return false;
		}
		
		return true;
	}
}
