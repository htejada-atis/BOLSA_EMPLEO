package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/** Clase para gestionar la tabla de áreas en evaluadores.
 * @author ATISoluciones
 */
public class AreaEvaluadoresTable extends Area implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer numeroEvaluadores;
	
	/** Constructor por defecto.
	 */
	public AreaEvaluadoresTable() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param parea .
	 * @param pnumeroEvaluadores .
	 */
	public AreaEvaluadoresTable(Area parea, Integer pnumeroEvaluadores) {
		super(parea);
		this.numeroEvaluadores = pnumeroEvaluadores;
	}
	
	public Integer getNumeroEvaluadores() {
		return numeroEvaluadores;
	}
	
	public void setNumeroEvaluadores(Integer pnumeroEvaluadores) {
		this.numeroEvaluadores = pnumeroEvaluadores;
	}
	
	@Override
	public String toString() {
		return "BolsaEvaluadoresTable [bolsa=" + super.toString() + ", numeroEvaluadores=" + numeroEvaluadores + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * super.hashCode();
		result = prime * result + ((numeroEvaluadores == null) ? 0 : numeroEvaluadores.hashCode());		
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
		
		AreaEvaluadoresTable other = (AreaEvaluadoresTable) obj;
		if (!super.equals(other)) {
			return false;
		}
		
		if (numeroEvaluadores == null) {
			if (other.numeroEvaluadores != null) {
				return false;
			}
		} else if (!numeroEvaluadores.equals(other.numeroEvaluadores)) {
			return false;
		}
		
		return true;
	}
}

