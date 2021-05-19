package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase mérito solicitud de bolsa empleo.
 * 
 * @author ATISoluciones 2021
 */
public class MeritoValidarTable extends Merito implements Serializable {

	private static final long serialVersionUID = 1L;
	private Boolean excluido;
	private Boolean validado;

	/**
	 * Constructor por defecto.
	 */
	public MeritoValidarTable() {

	}

	/**
	 * Constructor con parametros.
	 * @param pmerito .
	 * @param pexcluido .
	 * @param pvalidado .
	 */
	public MeritoValidarTable(Merito pmerito, Boolean pexcluido, Boolean pvalidado) {
		super(pmerito);
		this.excluido = pexcluido;
		this.validado = pvalidado;
	}


	public Boolean isExcluido() {
		return excluido;
	}

	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}
	
	public Boolean isValidado() {
		return validado;
	}

	public void setValidado(Boolean validado) {
		this.validado = validado;
	}
		
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeritoValidarTable [merito= " + super.toString() + ", excluido=" + excluido + ", validado=" + validado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * super.hashCode();
		result = prime * result + ((excluido == null) ? 0 : excluido.hashCode());	
		result = prime * result + ((validado == null) ? 0 : validado.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings({ "checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity" })
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
		MeritoValidarTable other = (MeritoValidarTable) obj;
		if (!super.equals(other)) {
			return false;
		}
		if (excluido == null) {
			if (other.excluido != null) {
				return false;
			}
		} else if (!excluido.equals(other.excluido)) {
			return false;
		}
		if (validado == null) {
			if (other.validado != null) {
				return false;
			}
		} else if (!validado.equals(other.validado)) {
			return false;
		}
		
		return true;
	}

}
