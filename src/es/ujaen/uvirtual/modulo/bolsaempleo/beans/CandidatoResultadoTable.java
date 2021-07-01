package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;


/** Clase CandidatoResultadoTable para la tabla de candidatos en resultados de un área.
 * @author ATISoluciones 2021
 */
public class CandidatoResultadoTable extends UsuarioBolsaEmpleo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Double total;
	
	/** Constructor por defecto.
	 */
	public CandidatoResultadoTable() {
		super();
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 */
	public CandidatoResultadoTable(UsuarioBolsaEmpleo pusuario) {
		super(pusuario);
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 * @param ptotal .
	 */
	public CandidatoResultadoTable(UsuarioBolsaEmpleo pusuario, Double ptotal) {
		super(pusuario);
		this.total = ptotal;
	}
	
	/** Constructor copia.
	 * @param copia CandidatoResultadoTable a copiar
	 */
	public CandidatoResultadoTable(CandidatoResultadoTable copia) {
		super(copia);
		this.total = copia.total;
	}
	
	public Double getTotal() {
		return this.total;
	}
	
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "CandidatoResultadoTable [candidato=" + super.toString() + ", total=" + total + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((total == null) ? 0 : total.hashCode());
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
		CandidatoResultadoTable other = (CandidatoResultadoTable) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (total == null) {
			if (other.total != null) {
				return false;
			}
		} else if (!total.equals(other.total)) {
			return false;
		}
		
		return true;
	}
	
}
