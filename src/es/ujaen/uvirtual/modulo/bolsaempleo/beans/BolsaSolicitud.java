package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.List;

/** Una bolsa seleccionada por el candidato en la solicitud. Contiene los meritos asigandos a la bolsa.
 * @author ATISoluciones
 */
public class BolsaSolicitud extends Bolsa implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<MeritoSolicitud> listaMeritos;
	
	/** Constructor por defecto.
	 */
	public BolsaSolicitud() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pbolsa .
	 * @param plistaMeritos .
	 */
	public BolsaSolicitud(Bolsa pbolsa, List<MeritoSolicitud> plistaMeritos) {
		super(pbolsa);
		this.listaMeritos = plistaMeritos;
	}
	
	public List<MeritoSolicitud> getListaMeritos() {
		return listaMeritos;
	}
	
	public Integer getNumeroMeritos() {
		return listaMeritos.size();
	}
	
	public void setListaMeritos(List<MeritoSolicitud> meritos) {
		this.listaMeritos = meritos;
	}
	
	@Override
	public String toString() {
		return "BolsaSolicitud [meritos=" + listaMeritos + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((listaMeritos == null) ? 0 : listaMeritos.hashCode());
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
		
		BolsaSolicitud other = (BolsaSolicitud) obj;
		if (listaMeritos == null) {
			if (other.listaMeritos != null) {
				return false;
			}
		} else if (!listaMeritos.equals(other.listaMeritos)) {
			return false;
		}
		
		return true;
	}
}

