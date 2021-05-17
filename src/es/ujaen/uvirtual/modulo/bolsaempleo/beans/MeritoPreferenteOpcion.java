package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

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
	 */
	public MeritoPreferenteOpcion(Integer pcodNum, Integer pmeritoPreferenteCodNum, String pnombre, Double pfactor) {		
		this.codNum = pcodNum;
		this.meritoPreferenteCodNum = pmeritoPreferenteCodNum;
		this.nombre = pnombre;
		this.factor = pfactor;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeritoPreferenteOpcion [codNum=" + codNum + ", meritoPreferenteCodNum=" + meritoPreferenteCodNum + ", nombre=" + nombre + ", factor=" + factor + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((meritoPreferenteCodNum == null) ? 0 : meritoPreferenteCodNum.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((factor == null) ? 0 : factor.hashCode());
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

		return true;
	}

}
