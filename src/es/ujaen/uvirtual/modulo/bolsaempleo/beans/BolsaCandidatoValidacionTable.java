package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/** Una bolsa del candidato seleccionado en la solicitud. Contiene un booleano que indica si un mérito está asignado a una bolsa .
 * @author ATISoluciones 2021
 */
public class BolsaCandidatoValidacionTable extends Bolsa implements Serializable {

	private static final long serialVersionUID = 1L;
	private Boolean contieneMerito;
	
	/** Constructor por defecto.
	 */
	public BolsaCandidatoValidacionTable() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pbolsa .
	 * @param pcontieneMerito .
	 */
	public BolsaCandidatoValidacionTable(Bolsa pbolsa, Boolean pcontieneMerito) {
		super(pbolsa);
		this.contieneMerito = pcontieneMerito;
	}
	
	public Boolean isContieneMerito() {
		return contieneMerito;
	}
	
	public void setContieneMerito(Boolean contieneMerito) {
		this.contieneMerito = contieneMerito;
	}
	
	@Override
	public String toString() {
		return "BolsaCandidatoValidacionTable [bolsa=" + super.toString() + ", contieneMerito=" + contieneMerito + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * super.hashCode();
		result = prime * result + ((contieneMerito == null) ? 0 : contieneMerito.hashCode());
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
		
		BolsaCandidatoValidacionTable other = (BolsaCandidatoValidacionTable) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (contieneMerito == null) {
			if (other.contieneMerito != null) {
				return false;
			}
		} else if (!contieneMerito.equals(other.contieneMerito)) {
			return false;
		}
		
		return true;
	}
}

