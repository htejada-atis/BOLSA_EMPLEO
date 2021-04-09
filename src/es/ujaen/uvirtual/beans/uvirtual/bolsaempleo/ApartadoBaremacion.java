package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;

/** Clase apartado de baremación de bolsa empleo.
 * 
 * @author ATISoluciones 2021 
 */
public class ApartadoBaremacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String codigo;
	private String nombre;
	private Boolean activo;
	
	
	/** Constructor por defecto.
	 */
	public ApartadoBaremacion() {
		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pcodigo .
	 * @param pnombre .
	 * @param pactivo .
	 */
	public ApartadoBaremacion(Integer pcodNum, String pcodigo, String pnombre, Boolean pactivo) {
		super();
		this.codNum = pcodNum;
		this.codigo = pcodigo;
		this.nombre = pnombre;
		this.activo = pactivo;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pcodigo .
	 * @param pnombre .
	 */
	public ApartadoBaremacion(Integer pcodNum, String pcodigo, String pnombre) {
		super();
		this.codNum = pcodNum;
		this.codigo = pcodigo;
		this.nombre = pnombre;
	}
	
	/** Constructor con parametros.
	 * @param pcodigo .
	 * @param pnombre .
	 */
	public ApartadoBaremacion(String pcodigo, String pnombre) {
		super();
		this.codigo = pcodigo;
		this.nombre = pnombre;
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 * @param pactivo .
	 */
	public ApartadoBaremacion(Integer pcodNum, Boolean pactivo) {
		super();
		this.codNum = pcodNum;
		this.activo = pactivo;		
	}
	
	/** Constructor con parametros.
	 * @param pcodNum .
	 */
	public ApartadoBaremacion(Integer pcodNum) {
		super();
		this.codNum = pcodNum;
	}
		
	/** Constructor copia.
	 * @param copia .
	 */
	public ApartadoBaremacion(ApartadoBaremacion copia) {
		this.codNum = copia.codNum;
		this.codigo = copia.codigo;
		this.nombre = copia.nombre;
		this.activo = copia.activo;
	}
	
	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Boolean isActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "ApartadoBaremacion [codNum=" + codNum + ", codigo=" + codigo
				+ ", nombre=" + nombre + ", activo=" + activo + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());	
		result = prime * result + ((activo == null) ? 0 : activo.hashCode());
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
		ApartadoBaremacion other = (ApartadoBaremacion) obj;
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (activo == null) {
			if (other.activo != null) {
				return false;
			}
		} else if (!activo.equals(other.activo)) {
			return false;
		}
		
		return true;
	}
	
}
