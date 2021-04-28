package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.List;

/** Clase bolsa para los listados de validacion.
 * @author atis
 */
public class BolsaValidacion extends Bolsa implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer totalMeritosNoValidados;
	private Integer totalMeritosValidados;
	private Integer totalMeritosExcluidos;
	
	/** Constructor por defecto.
	 */
	public BolsaValidacion() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pbolsa .
	 * @param pmeritosNoValidados .
	 * @param pmeritosValidados .
	 * @param pmeritosExcluidos .
	 */
	public BolsaValidacion(Bolsa pbolsa, Integer pmeritosNoValidados, Integer pmeritosValidados, Integer pmeritosExcluidos) {
		super(pbolsa);
		this.totalMeritosNoValidados = pmeritosNoValidados;
		this.totalMeritosValidados = pmeritosValidados;
		this.totalMeritosExcluidos = pmeritosExcluidos;		
	}
	
	/** Constructor copia.
	 * @param copia Convocatoria a copiar
	 */
	public BolsaValidacion(BolsaValidacion copia) {
		super(copia);
		this.totalMeritosNoValidados = copia.totalMeritosNoValidados;
		this.totalMeritosValidados = copia.totalMeritosValidados;
		this.totalMeritosExcluidos = copia.totalMeritosExcluidos;
	}
	
	public Integer getMeritosNoValidados() {
		return this.totalMeritosNoValidados;
	}
	
	public void setMeritosNoValidados(Integer v) {
		totalMeritosNoValidados = v;
	}
	
	/**
	 * Devuelve el total de meritos.
	 * @return total.
	 */
	public Integer getTotalMeritos() {
		return totalMeritosNoValidados + totalMeritosValidados + totalMeritosExcluidos;
	}
	
	@Override
	public String toString() {
		return "BolsaSolicitud [bolsa=" + super.toString() + ", meritosNoValidados=" + totalMeritosNoValidados + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * super.hashCode();
		result = prime * result + ((totalMeritosNoValidados == null) ? 0 : totalMeritosNoValidados.hashCode());
		result = prime * result + ((totalMeritosValidados == null) ? 0 : totalMeritosValidados.hashCode());
		result = prime * result + ((totalMeritosExcluidos == null) ? 0 : totalMeritosExcluidos.hashCode());
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
		
		BolsaValidacion other = (BolsaValidacion) obj;
		if (!super.equals(other)) {
			return false;
		}

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
		
		return true;
	}
}

