package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;


/** Clase mérito solicitud de bolsa empleo.
 * @author jlopez
 *
 */
public class MeritoSolicitud extends Merito implements Serializable {

	private static final long serialVersionUID = 1L;
	private Boolean afinidad;
	private Boolean excluido;
	
	/** Constructor por defecto.
	 */
	public MeritoSolicitud() {
		
	}
	
	/** Constructor con parametros.
	 * @param pmerito .
	 */
	public MeritoSolicitud(Merito pmerito) {
		super(pmerito);
	}
	
	/** Constructor con parametros.
	 * @param pmerito .
	 * @param pexcluido .
	 * @param pafinidad .
	 */
	public MeritoSolicitud(Merito pmerito, Boolean pexcluido, Boolean pafinidad) {
		super(pmerito);
		this.excluido = pexcluido;
		this.afinidad = pafinidad;
	}
	
	
	public Boolean isAfinidad() {
		return afinidad;
	}

	public void setAfinidad(Boolean afinidad) {
		this.afinidad = afinidad;
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
		return "MeritoSolicitud [afinidad=" + afinidad + ", excluido=" + excluido + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((afinidad == null) ? 0 : afinidad.hashCode());
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());
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
		MeritoSolicitud other = (MeritoSolicitud) obj;
		if (afinidad == null) {
			if (other.afinidad != null) {
				return false;
			}
		} else if (!afinidad.equals(other.afinidad)) {
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
