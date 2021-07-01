package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/** Clase para gestionar la tabla del bolsas en una solicitud.
 * @author ATISoluciones
 */
public class BolsaSolicitudTable extends Bolsa implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer numeroMeritos;
	
	/** Constructor por defecto.
	 */
	public BolsaSolicitudTable() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pbolsa .
	 * @param pnumeroMeritos .
	 */
	public BolsaSolicitudTable(Bolsa pbolsa, Integer pnumeroMeritos) {
		super(pbolsa);
		this.numeroMeritos = pnumeroMeritos;
	}
	
	public Integer getNumeroMeritos() {
		return numeroMeritos;
	}
	
	public void setNumeroMeritos(Integer numeroMeritos) {
		this.numeroMeritos = numeroMeritos;
	}
	
	@Override
	public String toString() {
		return "BolsaSolicitudTable [bolsa=" + super.toString() + ", numeroMeritos=" + numeroMeritos + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((numeroMeritos == null) ? 0 : numeroMeritos.hashCode());		
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
		BolsaSolicitudTable other = (BolsaSolicitudTable) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (numeroMeritos == null) {
			if (other.numeroMeritos != null) {
				return false;
			}
		} else if (!numeroMeritos.equals(other.numeroMeritos)) {
			return false;
		}
		
		return true;
	}
}

