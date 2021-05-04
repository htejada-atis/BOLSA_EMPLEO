package es.ujaen.uvirtual.beans.uvirtual.bolsaempleo;

import java.io.Serializable;


/** Clase titulación de bolsa empleo.
 * @author jlopez
 *
 */
public class Evaluador extends UsuarioBolsaEmpleo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNumArea;
	private Boolean activo;

	
	/** Constructor por defecto.
	 */
	public Evaluador() {
		super();
	}
	
	/** Constructor con parametros.
	 * @param pcodNumArea .
	 * @param pactivo .
	 */
	public Evaluador(Integer pcodNumArea, boolean pactivo) {
		super();
		this.codNumArea = pcodNumArea;
		this.activo = pactivo;
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 * @param pcodNumArea .
	 * @param pactivo .
	 */
	public Evaluador(UsuarioBolsaEmpleo pusuario, Integer pcodNumArea, boolean pactivo) {
		super(pusuario);
		this.codNumArea = pcodNumArea;
		this.activo = pactivo;
	}
	
	/** Constructor con parametros.
	 * @param pusuario .
	 */
	public Evaluador(UsuarioBolsaEmpleo pusuario) {
		super(pusuario);
	}
	
	/** Constructor copia.
	 * @param copia Evaluador a copiar
	 * @param pusuario usuario a copiar
	 */
	public Evaluador(Evaluador copia, UsuarioBolsaEmpleo pusuario) {
		super(pusuario);
		this.codNumArea = copia.codNumArea;
		this.activo = copia.activo;
	}
	
	public Integer getCodNumArea() {
		return codNumArea;
	}

	public void setCodNumArea(Integer codNumArea) {
		this.codNumArea = codNumArea;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Evaluador [codNumArea=" + codNumArea + ", activo=" + activo + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNumArea == null) ? 0 : codNumArea.hashCode());
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
		Evaluador other = (Evaluador) obj;
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
