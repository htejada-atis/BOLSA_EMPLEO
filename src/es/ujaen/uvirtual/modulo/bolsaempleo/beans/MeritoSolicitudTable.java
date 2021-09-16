package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Clase para controlar el listado de meritos añadidos en una solicitud.
 * 
 * @author ATISoluciones 2021
 */
public class MeritoSolicitudTable implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum; // id del merito
	private Merito merito;
	private MeritoSolicitud meritoSolicitud;
	private List<MeritoSolicitudValoracion> valoraciones;
	private Boolean excluido;
	private Boolean anteriorValidacion;

	/**
	 * Constructor por defecto.
	 */
	public MeritoSolicitudTable() {

	}

	/**
	 * Constructor con parametros.
	 *
	 * @param pmerito          .
	 * @param pmeritoSolicitud .
	 * @param pvaloraciones    .
	 * @param pexcluido .
	 */
	public MeritoSolicitudTable(Merito pmerito, MeritoSolicitud pmeritoSolicitud, List<MeritoSolicitudValoracion> pvaloraciones, Boolean pexcluido) {
		this.merito = pmerito;
		this.codNum = pmerito != null ? pmerito.getCodNum() : null;
		this.meritoSolicitud = pmeritoSolicitud;
		this.valoraciones = pvaloraciones;
		this.excluido = pexcluido;
	}

	public Integer getCodNum() {
		return this.codNum;
	}

	public Merito getMerito() {
		return this.merito;
	}

	/**
	 * Establece el merito.
	 * 
	 * @param merito .
	 */
	public void setMerito(Merito merito) {
		this.merito = merito;
		this.codNum = merito != null ? merito.getCodNum() : null;
	}

	public MeritoSolicitud getMeritoSolicitud() {
		return this.meritoSolicitud;
	}

	public void setMeritoSolicitud(MeritoSolicitud meritoSolicitud) {
		this.meritoSolicitud = meritoSolicitud;
	}

	public List<MeritoSolicitudValoracion> getValoraciones() {
		return this.valoraciones;
	}

	public void setValoraciones(List<MeritoSolicitudValoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}
	
	public Boolean getExcluido() {
		return this.excluido;
	}
	
	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}
	
	public Boolean isAnteriorValidacion() {
		return anteriorValidacion;
	}

	public void setAnteriorValidacion(Boolean anteriorValidacion) {
		this.anteriorValidacion = anteriorValidacion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeritoSolicitudTable [merito=" + merito + ", meritoSolicitud=" + meritoSolicitud + ", valoraciones="
				+ valoraciones + ", exclusivo=" + excluido + ", anteriorValidacion=" + anteriorValidacion + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((merito == null) ? 0 : merito.hashCode());
		result = prime * result + ((meritoSolicitud == null) ? 0 : meritoSolicitud.hashCode());
		result = prime * result + ((valoraciones == null) ? 0 : valoraciones.hashCode());
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());
		result = prime * result + ((anteriorValidacion == null) ? 0 : anteriorValidacion.hashCode());
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
		MeritoSolicitudTable other = (MeritoSolicitudTable) obj;
		if (merito == null) {
			if (other.merito != null) {
				return false;
			}
		} else if (!merito.equals(other.merito)) {
			return false;
		}
		if (meritoSolicitud == null) {
			if (other.meritoSolicitud != null) {
				return false;
			}
		} else if (!meritoSolicitud.equals(other.meritoSolicitud)) {
			return false;
		}
		if (valoraciones == null) {
			if (other.valoraciones != null) {
				return false;
			}
		} else if (!valoraciones.equals(other.valoraciones)) {
			return false;
		}
		if (excluido == null) {
			if (other.excluido != null) {
				return false;
			}
		} else if (!excluido.equals(other.excluido)) {
			return false;
		}
		if (anteriorValidacion == null) {
			if (other.anteriorValidacion != null) {
				return false;
			}
		} else if (!anteriorValidacion.equals(other.anteriorValidacion)) {
			return false;
		}

		return true;
	}

}
