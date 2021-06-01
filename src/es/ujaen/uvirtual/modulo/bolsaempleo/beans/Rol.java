package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase rol de UVIRTUAL.
 * 
 * @author ATISoluciones 2021
 */
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String descripcion;
	private String valor;

	/**
	 * Constructor por defecto.
	 */
	public Rol() {

	}

	/**
	 * Constructor con parametros.
	 * 
	 * @param pcodNum      .
	 * @param pdescripcion .
	 */
	public Rol(Integer pcodNum, String pdescripcion) {
		super();
		this.codNum = pcodNum;
		this.descripcion = pdescripcion;
	}

	/**
	 * Constructor con parametros.
	 * 
	 * @param pcodNum .
	 */
	public Rol(Integer pcodNum) {
		super();
		this.codNum = pcodNum;
	}

	public Integer getCodNum() {
		return codNum;
	}

	public void setCodNum(Integer codNum) {
		this.codNum = codNum;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "Rol [codNum=" + codNum + ", descripcion=" + descripcion + ", valor=" + valor + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		Rol other = (Rol) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}

		return true;
	}
}
