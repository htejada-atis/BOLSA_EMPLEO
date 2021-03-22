package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;


/** Clase fichero de bolsa empleo.
 * @author jlopez
 *
 */
public class Fichero implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String nombre;
	
	
	/** Constructor por defecto.
	 */
	public Fichero() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pnombre .
	 */
	public Fichero(Integer pcodNum, String pnombre) {
		super();
		this.codNum = pcodNum;
		this.nombre = pnombre;
	}
	
	
	/** Constructor copia.
	 * @param copia Fichero a copiar
	 */
	public Fichero(Fichero copia) {
		this.codNum = copia.codNum;
		this.nombre = copia.nombre;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Fichero [codNum=" + codNum + ", nombre=" + nombre + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
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
		Fichero other = (Fichero) obj;
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		
		return true;
	}
	
}
