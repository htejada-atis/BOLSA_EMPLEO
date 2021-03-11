package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.Date;

/** Clase area de conocimiento de bolsaempleo.
 * @author atis
 */
public class Area implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idArea;
	private String nombre;
	private String codigo;
		
	/** Constructor por defecto.
	 */
	public Area() {
		//este contructor esta vacio intencionadamente
	}
	
	/** Constructor con parametros.
	 * @param pidAreaConocimiento .
	 * @param pnombre .
	 * @param pcodigo .
	 */
	public Area(Integer pidAreaConocimiento, String pnombre, String pcodigo) {
		super();
		this.idArea = pidAreaConocimiento;
		this.nombre = pnombre;
		this.codigo = pcodigo;		
	}
	
	/** Constructor copia.
	 * @param copia AreaConocimiento a copiar
	 */
	public Area(Area copia) {
		this.idArea = copia.idArea;
		this.nombre = copia.nombre;
		this.codigo = copia.codigo;		
	}
	
	public Integer getIdAreaConocimiento() {
		return idArea;
	}
	
	public void setIdAreaConocimiento(Integer idAreaConocimiento) {
		this.idArea = idAreaConocimiento;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@Override
	public String toString() {
		return "Area Conocimiento [idAreaConocimiento=" + idArea + ", nombre=" + nombre
				+ ", codigo=" + codigo + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());		
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
		Area other = (Area) obj;
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		return true;
	}
}
