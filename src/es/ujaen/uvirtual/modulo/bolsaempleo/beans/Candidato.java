package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;


/** Clase candidato de bolsa empleo.
 * @author jlopez
 *
 */
public class Candidato extends UsuarioBolsaEmpleo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer totalTitulaciones;
	private Integer totalTitulacionesValidadas;
	
	/** Constructor por defecto.
	 */
	public Candidato() {
		super();
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 * @param ptotalTitulaciones .
	 * @param ptotalTitulacionesValidadas .
	 */
	public Candidato(UsuarioBolsaEmpleo pusuario, Integer ptotalTitulaciones, Integer ptotalTitulacionesValidadas) {
		super(pusuario);
		this.totalTitulaciones = ptotalTitulaciones;
		this.totalTitulacionesValidadas = ptotalTitulacionesValidadas;
	}
	
	/** Constructor copia.
	 * @param copia Evaluador a copiar
	 */
	public Candidato(Candidato copia) {
		this.totalTitulaciones = copia.totalTitulaciones;
		this.totalTitulacionesValidadas = copia.totalTitulacionesValidadas;
	}
	
	public Integer getTotalTitulaciones() {
		return totalTitulaciones;
	}

	public void setTotalTitulaciones(Integer titulaciones) {
		this.totalTitulaciones = titulaciones;
	}
	
	public Integer getTotalTitulacionesValidadas() {
		return totalTitulacionesValidadas;
	}

	public void setTotalTitulacionesValidadas(Integer validadas) {
		this.totalTitulacionesValidadas = validadas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Candidato [totalTitulaciones=" + totalTitulaciones + ", totalTitulacionesValidadas=" + totalTitulacionesValidadas + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((totalTitulaciones == null) ? 0 : totalTitulaciones.hashCode());
		result = prime * result + ((totalTitulacionesValidadas == null) ? 0 : totalTitulacionesValidadas.hashCode());
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
		Candidato other = (Candidato) obj;
		if (totalTitulaciones == null) {
			if (other.totalTitulaciones != null) {
				return false;
			}
		} else if (!totalTitulaciones.equals(other.totalTitulaciones)) {
			return false;
		}
		if (totalTitulacionesValidadas == null) {
			if (other.totalTitulacionesValidadas != null) {
				return false;
			}
		} else if (!totalTitulacionesValidadas.equals(other.totalTitulacionesValidadas)) {
			return false;
		}
		
		return true;
	}
	
}
