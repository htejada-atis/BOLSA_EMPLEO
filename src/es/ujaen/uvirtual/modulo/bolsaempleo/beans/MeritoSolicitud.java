package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Clase mérito solicitud de bolsa empleo.
 * 
 * @author ATISoluciones 2021
 */
public class MeritoSolicitud implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private Merito merito;
	private Boolean excluido;
	private Boolean validado;
	private String observacionCandidato;
	private List<MeritoSolicitudValoracion> valoraciones;

	/**
	 * Constructor por defecto.
	 */
	public MeritoSolicitud() {

	}

	/**
	 * Constructor con parametros.
	 *
	 * @param pcodNum .
	 * @param pmerito .
	 * @param pexcluido .
	 */
	public MeritoSolicitud(Integer pcodNum, Merito pmerito, Boolean pexcluido) {
		this.codNum = pcodNum;
		this.merito = pmerito;		
		this.excluido = pexcluido;		
	}
	
	/**
	 * Constructor con parametros.
	 *
	 * @param pcodNum .
	 * @param pmerito .
	 * @param pexcluido .
	 * @param pvalidado .
	 * @param pobservacionCandidato .
	 */
	public MeritoSolicitud(Integer pcodNum, Merito pmerito, Boolean pexcluido, Boolean pvalidado, String pobservacionCandidato) {
		this.codNum = pcodNum;
		this.merito = pmerito;
		this.excluido = pexcluido;
		this.validado = pvalidado;
		this.observacionCandidato = pobservacionCandidato;
	}
	
	/**
	 * Constructor con parametros .
	 * @param pmerito .
	 * @param pexcluido .
	 * @param pvalidado .
	 */
	public MeritoSolicitud(Merito pmerito, Boolean pexcluido, Boolean pvalidado) {
		this.merito = pmerito;		
		this.excluido = pexcluido;		
		this.validado = pvalidado;
	}

	public Integer getCodNum() {
		return this.codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
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
	
	public Boolean isValidado() {
		return validado;
	}

	public void setValidado(Boolean validado) {
		this.validado = validado;
	}
	
	public String getObservacionCandidato() {
		return observacionCandidato;
	}
	
	public void setObservacionCandidato(String observacion) {
		this.observacionCandidato = observacion;
	}
	
	public List<MeritoSolicitudValoracion> getValoraciones() {
		return this.valoraciones;
	}

	public void setValoraciones(List<MeritoSolicitudValoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}
		
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeritoSolicitud [codNum=" + codNum + ", merito=" + merito + ", excluido=" + excluido
				+ ", validado=" + validado + ", observacionCandidato=" + observacionCandidato + ", valoraciones=" + valoraciones + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((merito == null) ? 0 : merito.hashCode());
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());
		result = prime * result + ((validado == null) ? 0 : validado.hashCode());	
		result = prime * result + ((observacionCandidato == null) ? 0 : observacionCandidato.hashCode());
		result = prime * result + ((valoraciones == null) ? 0 : valoraciones.hashCode());
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
		if (excluido == null) {
			if (other.excluido != null) {
				return false;
			}
		} else if (!excluido.equals(other.excluido)) {
			return false;
		}
		if (validado == null) {
			if (other.validado != null) {
				return false;
			}
		} else if (!validado.equals(other.validado)) {
			return false;
		}
		if (observacionCandidato == null) {
			if (other.observacionCandidato != null) {
				return false;
			}
		} else if (!observacionCandidato.equals(other.observacionCandidato)) {
			return false;
		}
		if (valoraciones == null) {
			if (other.valoraciones != null) {
				return false;
			}
		} else if (!valoraciones.equals(other.valoraciones)) {
			return false;
		}
		
		return true;
	}

}
