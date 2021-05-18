package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase opción del mérito preferente.
 * 
 * @author ATISoluciones 2021
 */
public class MeritoPreferenteOpcion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Integer meritoPreferenteCodNum;
	private String nombre;	
	private Double factor;
	private Boolean borrado;
	private Date fechaBorrado;

	/**
	 * Constructor por defecto.
	 */
	public MeritoPreferenteOpcion() {

	}

	/**
	 * Constructor con parametros.
	 * @param pcodNum .
	 * @param pmeritoPreferenteCodNum .
	 * @param pnombre .
	 * @param pfactor .
	 * @param pborrado .
	 * @param pfechaBorrado .
	 */
	public MeritoPreferenteOpcion(Integer pcodNum, Integer pmeritoPreferenteCodNum, String pnombre, Double pfactor, Boolean pborrado, Date pfechaBorrado) {		
		this.codNum = pcodNum;
		this.meritoPreferenteCodNum = pmeritoPreferenteCodNum;
		this.nombre = pnombre;
		this.factor = pfactor;
		this.borrado = pborrado;
		this.fechaBorrado = pfechaBorrado;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia .
	 */
	public MeritoPreferenteOpcion(MeritoPreferenteOpcion copia) {
		this.codNum = copia.codNum;
		this.meritoPreferenteCodNum = copia.codNum;
		this.nombre = copia.nombre;
		this.factor = copia.factor;
		this.borrado = copia.borrado;
		this.fechaBorrado = copia.fechaBorrado;
	}

	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public Integer getMeritoPreferenteCodNum() {
		return this.meritoPreferenteCodNum;
	}
	
	public void setMeritoPreferenteCodNum(Integer merito) {
		this.meritoPreferenteCodNum = merito;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
		
	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
	}
	
	public Boolean isBorrado() {
		return borrado;
	}

	public void setBorrado(Boolean borrado) {
		this.borrado = borrado;
	}
	
	public Date getFechaBorrado() {
		return fechaBorrado;
	}

	public void setFechaBorrado(Date fechaBorrado) {
		this.fechaBorrado = fechaBorrado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeritoPreferenteOpcion [codNum=" + codNum + ", meritoPreferenteCodNum=" + meritoPreferenteCodNum + ", nombre=" + nombre + ", factor=" 
				+ factor + ", borrado=" + borrado + ", fechaBorrado=" + fechaBorrado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((meritoPreferenteCodNum == null) ? 0 : meritoPreferenteCodNum.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((factor == null) ? 0 : factor.hashCode());
		result = prime * result + ((borrado == null) ? 0 : borrado.hashCode());
		result = prime * result + ((fechaBorrado == null) ? 0 : fechaBorrado.hashCode());
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
		MeritoPreferenteOpcion other = (MeritoPreferenteOpcion) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (meritoPreferenteCodNum == null) {
			if (other.meritoPreferenteCodNum != null) {
				return false;
			}
		} else if (!meritoPreferenteCodNum.equals(other.meritoPreferenteCodNum)) {
			return false;
		}
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (factor == null) {
			if (other.factor != null) {
				return false;
			}
		} else if (!factor.equals(other.factor)) {
			return false;
		}
		if (borrado == null) {
			if (other.borrado != null) {
				return false;
			}
		} else if (!borrado.equals(other.borrado)) {
			return false;
		}
		if (fechaBorrado == null) {
			if (other.fechaBorrado != null) {
				return false;
			}
		} else if (!fechaBorrado.equals(other.fechaBorrado)) {
			return false;
		}

		return true;
	}

}
