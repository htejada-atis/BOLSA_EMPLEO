package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase mérito solicitud de bolsa empleo.
 * 
 * @author ATISoluciones 2021
 */
public class MeritoSolicitudValoracion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private MeritoSolicitud meritoSolicitud;
	private Afinidad afinidad;
	private Float valor;

	/**
	 * Constructor por defecto.
	 */
	public MeritoSolicitudValoracion() {

	}

	/**
	 * Constructor con parametros.
	 *
	 * @param pcodNum .
	 * @param pmeritoSolicitud   .
	 * @param pafinidad   .
	 * @param pvalor .
	 */
	public MeritoSolicitudValoracion(Integer pcodNum, MeritoSolicitud pmeritoSolicitud, Afinidad pafinidad, Float pvalor) {
		this.codNum = pcodNum;
		this.meritoSolicitud = pmeritoSolicitud;
		this.afinidad = pafinidad;
		this.valor = pvalor;
	}

	public Integer getCodNum() {
		return this.codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}
	
	public MeritoSolicitud getMeritoSolicitud() {
		return this.meritoSolicitud;
	}

	public void setMeritoSolicitud(MeritoSolicitud merito) {
		this.meritoSolicitud = merito;
	}
	
	public Afinidad getAfinidad() {
		return this.afinidad;
	}

	public void setAfinidad(Afinidad afinidad) {
		this.afinidad = afinidad;
	}
	
	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}
			
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeritoSolicitudValoracion [codNum=" + codNum + ", meritoSolicitud=" + meritoSolicitud + ", afinidad=" + afinidad + ", valor=" + valor + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((meritoSolicitud == null) ? 0 : meritoSolicitud.hashCode());
		result = prime * result + ((afinidad == null) ? 0 : afinidad.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		MeritoSolicitudValoracion other = (MeritoSolicitudValoracion) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (meritoSolicitud == null) {
			if (other.meritoSolicitud != null) {
				return false;
			}
		} else if (!meritoSolicitud.equals(other.meritoSolicitud)) {
			return false;
		}
		if (afinidad == null) {
			if (other.afinidad != null) {
				return false;
			}
		} else if (!afinidad.equals(other.afinidad)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		
		return true;
	}

}
