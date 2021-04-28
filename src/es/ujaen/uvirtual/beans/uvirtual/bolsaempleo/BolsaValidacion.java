package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.List;

/** Clase bolsa para los listados de validacion.
 * @author atis
 */
public class BolsaValidacion extends Bolsa implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer meritosNoValidados;
	private Integer meritosValidados;
	private Integer meritosExcluidos;
	private Integer meritosTotal;
	
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
	 * @param pmeritosTotal .
	 */
	public BolsaValidacion(Bolsa pbolsa, Integer pmeritosNoValidados, Integer pmeritosValidados, Integer pmeritosExcluidos, Integer pmeritosTotal) {
		super(pbolsa);
		this.meritosNoValidados = pmeritosNoValidados;
		this.meritosValidados = pmeritosValidados;
		this.meritosExcluidos = pmeritosExcluidos;
		this.meritosTotal = pmeritosTotal;
	}
	
	/** Constructor copia.
	 * @param copia Convocatoria a copiar
	 */
	public BolsaValidacion(BolsaValidacion copia) {
		super(copia);
		this.meritosNoValidados = copia.meritosNoValidados;
		this.meritosValidados = copia.meritosValidados;
		this.meritosExcluidos = copia.meritosExcluidos;
		this.meritosTotal = copia.meritosTotal;		
	}
	
	public Integer getMeritosNoValidados() {
		return this.meritosNoValidados;
	}
	
	public void setMeritosNoValidados(Integer v) {
		meritosNoValidados = v;
	}
	
	@Override
	public String toString() {
		return "BolsaSolicitud [meritosNoValidados=" + meritosNoValidados + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * super.hashCode();
		result = prime * result + ((meritosNoValidados == null) ? 0 : meritosNoValidados.hashCode());
		result = prime * result + ((meritosValidados == null) ? 0 : meritosValidados.hashCode());
		result = prime * result + ((meritosExcluidos == null) ? 0 : meritosExcluidos.hashCode());
		result = prime * result + ((meritosTotal == null) ? 0 : meritosTotal.hashCode());
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

		if (meritosNoValidados == null) {
			if (other.meritosNoValidados != null) {
				return false;
			}
		} else if (!meritosNoValidados.equals(other.meritosNoValidados)) {
			return false;
		}
		if (meritosValidados == null) {
			if (other.meritosValidados != null) {
				return false;
			}
		} else if (!meritosValidados.equals(other.meritosValidados)) {
			return false;
		}
		if (meritosExcluidos == null) {
			if (other.meritosExcluidos != null) {
				return false;
			}
		} else if (!meritosExcluidos.equals(other.meritosExcluidos)) {
			return false;
		}
		if (meritosTotal == null) {
			if (other.meritosTotal != null) {
				return false;
			}
		} else if (!meritosTotal.equals(other.meritosTotal)) {
			return false;
		}
		
		return true;
	}
}

