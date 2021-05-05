package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase mérito solicitud de bolsa empleo.
 * 
 * @author Atisoluciones
 */
public class MeritoSolicitud implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Merito merito;
	private Integer codNumSolicitudBolsa;
	private Boolean excluido;

	/**
	 * Constructor por defecto.
	 */
	public MeritoSolicitud() {

	}

	/**
	 * Constructor con parametros.
	 *
	 * @param pcodNum .
	 * @param pmerito   .
	 * @param pcodNumSolicitudBolsa   .
	 * @param pexcluido .
	 */
	public MeritoSolicitud(Integer pcodNum, Merito pmerito, Integer pcodNumSolicitudBolsa, Boolean pexcluido) {
		this.codNum = pcodNum;
		this.merito = pmerito;
		this.codNumSolicitudBolsa = pcodNumSolicitudBolsa;
		this.excluido = pexcluido;		
	}

	public Integer getCodNum() {
		return this.codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public Integer getCodNumSolicitudBolsa() {
		return this.codNumSolicitudBolsa;
	}

	public void setCodNumSolicitudBolsa(Integer codNumSolicitudBolsa) {
		this.codNumSolicitudBolsa = codNumSolicitudBolsa;
	}
	
	public Merito getMerito() {
		return this.merito;
	}

	public void setMerito(Merito merito) {
		this.merito = merito;
	}
	
	public Boolean isExcluido() {
		return excluido;
	}

	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}
		
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeritoSolicitud [codNum=" + codNum + ", merito=" + merito + ", codNumSolicitudBolsa=" + codNumSolicitudBolsa + ", excluido=" + excluido + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((merito == null) ? 0 : merito.hashCode());
		result = prime * result + ((codNumSolicitudBolsa == null) ? 0 : codNumSolicitudBolsa.hashCode());	
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());		
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
		MeritoSolicitud other = (MeritoSolicitud) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (merito == null) {
			if (other.merito != null) {
				return false;
			}
		} else if (!merito.equals(other.merito)) {
			return false;
		}
		if (codNumSolicitudBolsa == null) {
			if (other.codNumSolicitudBolsa != null) {
				return false;
			}
		} else if (!codNumSolicitudBolsa.equals(other.codNumSolicitudBolsa)) {
			return false;
		}
		if (excluido == null) {
			if (other.excluido != null) {
				return false;
			}
		} else if (!excluido.equals(other.excluido)) {
			return false;
		}
		
		return true;
	}

}
