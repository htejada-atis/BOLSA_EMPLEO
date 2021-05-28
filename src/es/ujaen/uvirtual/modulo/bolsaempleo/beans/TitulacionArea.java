package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;


/** Clase titulación de bolsa empleo.
 * @author jlopez
 *
 */
public class TitulacionArea extends Titulacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Area area;

	
	/** Constructor por defecto.
	 */
	public TitulacionArea() {
		
	}
	
	/** Constructor con parametros.
	 * @param ptitulacion .
	 * @param parea .
	 */
	public TitulacionArea(Titulacion ptitulacion, Area parea) {
		super(ptitulacion);
		this.area = parea;
	}
	
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	@Override
	public String toString() {
		return "TitulaciónArea [titulación= " + super.toString() + ", area=" + area + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + ((area == null) ? 0 : area.hashCode());
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
		TitulacionArea other = (TitulacionArea) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (area == null) {
			if (other.area != null) {
				return false;
			}
		} else if (!area.equals(other.area)) {
			return false;
		}
		
		return true;
	}
	
}
