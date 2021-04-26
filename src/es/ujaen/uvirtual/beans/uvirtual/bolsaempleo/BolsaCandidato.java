package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.Date;

/** Clase bolsa empleo de bolsaempleo.
 * @author atis
 */
public class BolsaCandidato extends Bolsa implements Serializable {

	private static final long serialVersionUID = 1L;
	private Boolean excluido;
	
	/** Constructor por defecto.
	 */
	public BolsaCandidato() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pbolsa .
	 * @param pexcluido .
	 */
	public BolsaCandidato(Bolsa pbolsa, Boolean pexcluido) {
		super(pbolsa);
		this.excluido = pexcluido;
	}
	
	public Boolean getExcluido() {
		return excluido;
	}
	
	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}
	
	@Override
	public String toString() {
		return "BolsaCandidato [excluido=" + excluido + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		
		BolsaCandidato other = (BolsaCandidato) obj;
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

