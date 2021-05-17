package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.util.Date;


/** Clase titulación de bolsa empleo.
 * @author jlopez
 *
 */
public class Titulacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String nombre;
	private Boolean borrado;
	private Date fechaBorrado;

	
	/** Constructor por defecto.
	 */
	public Titulacion() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pnombre .
	 */
	public Titulacion(Integer pcodNum, String pnombre) {
		super();
		this.codNum = pcodNum;
		this.nombre = pnombre;
	}
	
	/** Constructor con parametros.
	 * @param pnombre .
	 */
	public Titulacion(String pnombre) {
		super();
		this.nombre = pnombre;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 */
	public Titulacion(Integer pcodNum) {
		super();
		this.codNum = pcodNum;
	}
	
	/** Constructor copia.
	 * @param copia Titulación a copiar
	 */
	public Titulacion(Titulacion copia) {
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
	
	public Boolean getBorrado() {
		return borrado;
	}

	public void setBorrado(Boolean borrado) {
		this.borrado = borrado;
	}
	
	public Date getFechaBorrado() {
		return fechaBorrado;
	}

	public void setFechaBorrado(Date fechaBorrado) {
		this.fechaBorrado = fechaBorrado;
	}
	
	@Override
	public String toString() {
		return "Titulación [codNum=" + codNum + ", nombre=" + nombre + ", borrado=" + borrado + ", fecha_borrado=" + fechaBorrado + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((borrado == null) ? 0 : borrado.hashCode());
		result = prime * result + ((fechaBorrado == null) ? 0 : fechaBorrado.hashCode());
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
		Titulacion other = (Titulacion) obj;
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (borrado == null) {
			if (other.borrado != null) {
				return false;
			}
		} else if (!borrado.equals(other.borrado)) {
			return false;
		}
		if (fechaBorrado == null) {
			if (other.fechaBorrado != null) {
				return false;
			}
		} else if (!fechaBorrado.equals(other.fechaBorrado)) {
			return false;
		}
		
		return true;
	}
	
}
