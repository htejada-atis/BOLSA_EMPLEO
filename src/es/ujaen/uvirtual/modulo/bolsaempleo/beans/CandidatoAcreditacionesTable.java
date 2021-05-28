package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;


/** Clase candidato de bolsa empleo.
 * @author ATISoluciones 2021
 *
 */
public class CandidatoAcreditacionesTable extends UsuarioBolsaEmpleo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer totalAcreditaciones;
	private Integer totalAcreditacionesValidadas;
	
	/** Constructor por defecto.
	 */
	public CandidatoAcreditacionesTable() {
		super();
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 * @param ptotalAcreditaciones .
	 * @param ptotalAcreditacionesValidadas .
	 */
	public CandidatoAcreditacionesTable(UsuarioBolsaEmpleo pusuario, Integer ptotalAcreditaciones, Integer ptotalAcreditacionesValidadas) {
		super(pusuario);
		this.totalAcreditaciones = ptotalAcreditaciones;
		this.totalAcreditacionesValidadas = ptotalAcreditacionesValidadas;
	}
	
	/** Constructor copia.
	 * @param copia Evaluador a copiar
	 */
	public CandidatoAcreditacionesTable(CandidatoAcreditacionesTable copia) {
		this.totalAcreditaciones = copia.totalAcreditaciones;
		this.totalAcreditacionesValidadas = copia.totalAcreditacionesValidadas;
	}
	
	public Integer getTotalAcreditaciones() {
		return totalAcreditaciones;
	}

	public void setTotalAcreditaciones(Integer acreditaciones) {
		this.totalAcreditaciones = acreditaciones;
	}
	
	public Integer getTotalAcreditacionesValidadas() {
		return totalAcreditacionesValidadas;
	}

	public void setTotalAcreditacionesValidadas(Integer validadas) {
		this.totalAcreditacionesValidadas = validadas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Candidato [totalAcreditaciones=" + totalAcreditaciones + ", totalAcreditacionesValidadas=" + totalAcreditacionesValidadas + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((totalAcreditaciones == null) ? 0 : totalAcreditaciones.hashCode());
		result = prime * result + ((totalAcreditacionesValidadas == null) ? 0 : totalAcreditacionesValidadas.hashCode());
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
		CandidatoAcreditacionesTable other = (CandidatoAcreditacionesTable) obj;
		if (totalAcreditaciones == null) {
			if (other.totalAcreditaciones != null) {
				return false;
			}
		} else if (!totalAcreditaciones.equals(other.totalAcreditaciones)) {
			return false;
		}
		if (totalAcreditacionesValidadas == null) {
			if (other.totalAcreditacionesValidadas != null) {
				return false;
			}
		} else if (!totalAcreditacionesValidadas.equals(other.totalAcreditacionesValidadas)) {
			return false;
		}
		
		return true;
	}
	
}
