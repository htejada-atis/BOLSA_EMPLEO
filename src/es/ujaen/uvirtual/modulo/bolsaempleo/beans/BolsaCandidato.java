package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/** Clase bolsa candidato de bolsaempleo.
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
		return "BolsaCandidato [bolsa= " + super.toString() + ", excluido=" + excluido + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * super.hashCode();
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
		if (!super.equals(other)) {
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

