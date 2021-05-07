package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;


/** Clase candidato para los listados de validacion.
 * @author jlopez
 *
 */
public class CandidatoValidacion extends UsuarioBolsaEmpleo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer totalMeritosNoValidados;
	private Integer totalMeritosValidados;
	private Integer totalMeritosExcluidos;
	private Integer totalMeritos;
	
	/** Constructor por defecto.
	 */
	public CandidatoValidacion() {
		super();
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 * @param pmeritosNoValidados .
	 * @param pmeritosValidados .
	 * @param pmeritosExcluidos .
	 * @param ptotalMeritos .
	 */
	public CandidatoValidacion(UsuarioBolsaEmpleo pusuario, Integer pmeritosNoValidados, Integer pmeritosValidados, Integer pmeritosExcluidos, Integer ptotalMeritos) {
		super(pusuario);
		this.totalMeritosNoValidados = pmeritosNoValidados;
		this.totalMeritosValidados = pmeritosValidados;
		this.totalMeritosExcluidos = pmeritosExcluidos;
		this.totalMeritos = ptotalMeritos;
	}
	
	/** Constructor copia.
	 * @param copia CandidatoValidacion a copiar
	 */
	public CandidatoValidacion(CandidatoValidacion copia) {
		this.totalMeritosNoValidados = copia.totalMeritosNoValidados;
		this.totalMeritosValidados = copia.totalMeritosValidados;
		this.totalMeritosExcluidos = copia.totalMeritosExcluidos;
		this.totalMeritos = copia.totalMeritos;
	}
	
	public Integer getTotalMeritosNoValidados() {
		return this.totalMeritosNoValidados;
	}
	
	public void setTotalMeritosNoValidados(Integer v) {
		totalMeritosNoValidados = v;
	}
	
	public Integer getTotalMeritosValidados() {
		return this.totalMeritosValidados;
	}
	
	public void setTotalMeritosValidados(Integer v) {
		totalMeritosValidados = v;
	}
	
	public Integer getTotalMeritosExcluidos() {
		return this.totalMeritosExcluidos;
	}
	
	public void setTotalMeritosExcluidos(Integer v) {
		totalMeritosExcluidos = v;
	}
	
	public Integer getTotalMeritos() {
		return this.totalMeritos;
	}
	
	public void setTotalMeritos(Integer v) {
		totalMeritos = v;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "CandidatoValidacin [candidato=" + super.toString() + ", meritosNoValidados=" + totalMeritosNoValidados 
				+ ", totalMeritosValidados=" + totalMeritosValidados + ", totalMeritosExcluidos=" + totalMeritosExcluidos 
				+ ", totalMeritos=" + totalMeritos + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * super.hashCode();
		result = prime * result + ((totalMeritosNoValidados == null) ? 0 : totalMeritosNoValidados.hashCode());
		result = prime * result + ((totalMeritosValidados == null) ? 0 : totalMeritosValidados.hashCode());
		result = prime * result + ((totalMeritosExcluidos == null) ? 0 : totalMeritosExcluidos.hashCode());
		result = prime * result + ((totalMeritos == null) ? 0 : totalMeritos.hashCode());
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
		CandidatoValidacion other = (CandidatoValidacion) obj;
		if (totalMeritosNoValidados == null) {
			if (other.totalMeritosNoValidados != null) {
				return false;
			}
		} else if (!totalMeritosNoValidados.equals(other.totalMeritosNoValidados)) {
			return false;
		}
		if (totalMeritosValidados == null) {
			if (other.totalMeritosValidados != null) {
				return false;
			}
		} else if (!totalMeritosValidados.equals(other.totalMeritosValidados)) {
			return false;
		}
		if (totalMeritosExcluidos == null) {
			if (other.totalMeritosExcluidos != null) {
				return false;
			}
		} else if (!totalMeritosExcluidos.equals(other.totalMeritosExcluidos)) {
			return false;
		}
		if (totalMeritos == null) {
			if (other.totalMeritos != null) {
				return false;
			}
		} else if (!totalMeritos.equals(other.totalMeritos)) {
			return false;
		}
		
		return true;
	}
	
}
