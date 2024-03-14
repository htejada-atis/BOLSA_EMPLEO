package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase evaluador de bolsa empleo para mostrar en la zona de candidatos.
 * 
 * @author ATISoluciones 2024
 *
 */
public class EvaluadorCandidato implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codNum;
	private String nombre;
	private String apellido1;
	private String apellido2;

	/**
	 * Constructor por defecto.
	 */
	public EvaluadorCandidato() {
	}

	/**
	 * Constructor con parametros.
	 * 
	 * @param pcodNum    .
	 * @param pnombre    .
	 * @param papellido1 .
	 * @param papellido2 .
	 */
	public EvaluadorCandidato(Integer pcodNum, String pnombre, String papellido1, String papellido2) {
		this.codNum = pcodNum;
		this.nombre = pnombre;
		this.apellido1 = papellido1;
		this.apellido2 = papellido2;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Evaluador a copiar
	 */
	public EvaluadorCandidato(EvaluadorCandidato copia) {
		this.codNum = copia.codNum;
		this.nombre = copia.nombre;
		this.apellido1 = copia.apellido1;
		this.apellido2 = copia.apellido2;
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

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido) {
		this.apellido1 = apellido;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido) {
		this.apellido2 = apellido;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "EvaluadorCandidato [codNum=" + codNum + ", nombre=" + nombre + ", apellido1=" + apellido1
				+ ", apellido2=" + apellido2 + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codNum == null) ? 0 : codNum.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((apellido1 == null) ? 0 : apellido1.hashCode());
		result = prime * result + ((apellido2 == null) ? 0 : apellido2.hashCode());
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
		EvaluadorCandidato other = (EvaluadorCandidato) obj;
		if (codNum == null) {
			if (other.codNum != null) {
				return false;
			}
		} else if (!codNum.equals(other.codNum)) {
			return false;
		}
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (apellido1 == null) {
			if (other.apellido1 != null) {
				return false;
			}
		} else if (!apellido1.equals(other.apellido1)) {
			return false;
		}
		if (apellido2 == null) {
			if (other.apellido2 != null) {
				return false;
			}
		} else if (!apellido2.equals(other.apellido2)) {
			return false;
		}

		return true;
	}

}
